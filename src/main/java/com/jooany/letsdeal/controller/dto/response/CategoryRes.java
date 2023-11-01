package com.jooany.letsdeal.controller.dto.response;

import java.sql.Timestamp;

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
	private Integer sortOrder;
	private Timestamp registeredAt;
	private Timestamp updateAt;
	private Timestamp deletedAt;

	public static CategoryRes fromEntity(Category category) {
		CategoryRes categoryRes = CategoryRes.builder()
			.id(category.getId())
			.categoryName(category.getCategoryName())
			.sortOrder(category.getSortOrder())
			.registeredAt(category.getRegisteredAt())
			.updateAt(category.getUpdateAt())
			.deletedAt(category.getDeletedAt())
			.build();
		return categoryRes;
	}
}
