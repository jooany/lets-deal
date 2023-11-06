package com.jooany.letsdeal.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.jooany.letsdeal.facade.RedissonLockMaxProposalFacade;
import com.jooany.letsdeal.model.entity.Sale;
import com.jooany.letsdeal.repository.SaleRepository;

@SpringBootTest
@ActiveProfiles("local")
public class LockTest {

	@Autowired
	private SaleService saleService;

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private RedissonLockMaxProposalFacade redissonLockMaxProposalFacade;

	@Test
	public void 락_동시에_3개의요청() throws InterruptedException {
		int threadCount = 3;
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			Integer buyerPrice = 10000 + 10 * i;

			executorService.submit(() -> {
				try {
					redissonLockMaxProposalFacade.saveProposal(1L, buyerPrice, "testUser");
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		Sale saleResult = saleRepository.findById(1L).orElseThrow();
		assertEquals(10020, saleResult.getMaxPriceProposal().getBuyerPrice());
	}

	@Test
	public void 락x_동시3개요청() throws InterruptedException {
		int threadCount = 3;
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			Integer buyerPrice = 10000 + 10 * i;

			executorService.submit(() -> {
				try {
					saleService.saveProposal(1L, buyerPrice, "testUser");
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		Sale saleResult = saleRepository.findById(1L).orElseThrow();
		assertEquals(10020, saleResult.getMaxPriceProposal().getBuyerPrice());
	}
}
