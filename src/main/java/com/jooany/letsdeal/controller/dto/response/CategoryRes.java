package com.jooany.letsdeal.controller.dto.response;

import java.time.LocalDateTime;

import com.jooany.letsdeal.model.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CategoryRes {
	private Long id;
	private String categoryName;
	private LocalDateTime registeredAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;

	public static CategoryRes fromEntity(Category category) {
		CategoryRes categoryRes = CategoryRes.builder()
			.id(category.getId())
			.categoryName(category.getCategoryName())
			.registeredAt(category.getRegisteredAt())
			.updatedAt(category.getUpdatedAt())
			.deletedAt(category.getDeletedAt())
			.build();
		return categoryRes;
	}
}
