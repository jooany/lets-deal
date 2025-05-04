package com.jooany.letsdeal.model.entity;

import java.sql.Timestamp;
import java.time.Instant;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "\"category\"")
@SQLDelete(sql = "UPDATE \"category\" SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is NULL")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "category_name")
	private String categoryName;

	@Column
	private Integer sortOrder;

	@Column(name = "registered_at", nullable = false)
	private Timestamp registeredAt;

	@Column(name = "updated_at", nullable = false)
	private Timestamp updateAt;

	@Column(name = "deleted_at")
	private Timestamp deletedAt;

	@PrePersist
	void registerdAt() {
		this.registeredAt = Timestamp.from(Instant.now());
	}

	@PreUpdate
	void updatedAt() {
		this.updateAt = Timestamp.from(Instant.now());
	}

}
