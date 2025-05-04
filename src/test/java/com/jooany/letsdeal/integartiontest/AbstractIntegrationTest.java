package com.jooany.letsdeal.integartiontest;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.jooany.letsdeal.util.ITUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class AbstractIntegrationTest {

	@Autowired
	private ITUtils itUtils;

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
		.withDatabaseName("testdb")
		.withUsername("testuser")
		.withPassword("testpass");

	@Container
	static GenericContainer<?> redis = new GenericContainer<>("redis:7")
		.withExposedPorts(6379);

	@DynamicPropertySource
	static void overrideProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
		registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);

		registry.add("spring.redis.host", redis::getHost);
		registry.add("spring.redis.port", () -> redis.getMappedPort(6379));
	}

	@PersistenceContext
	protected EntityManager em;

	@BeforeEach
	@Transactional
	public void clearDatabase() {
		em.flush();
		List<String> tableNames = em.createNativeQuery("""
				SELECT tablename FROM pg_tables
				WHERE schemaname = 'public'
			""").getResultList();

		em.createNativeQuery("SET session_replication_role = replica")
			.executeUpdate();

		for (String tableName : tableNames) {
			em.createNativeQuery("TRUNCATE TABLE " + tableName + " CASCADE")
				.executeUpdate();
		}

		em.createNativeQuery("SET session_replication_role = DEFAULT")
			.executeUpdate();
	}
}
