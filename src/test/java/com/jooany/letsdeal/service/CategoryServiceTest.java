package com.jooany.letsdeal.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.jooany.letsdeal.controller.dto.response.CategoryRes;
import com.jooany.letsdeal.fixture.entity.EntityFixture;
import com.jooany.letsdeal.model.entity.Category;
import com.jooany.letsdeal.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("local")
class CategoryServiceTest {
	@Mock
	private CategoryRepository categoryRepository;
	@InjectMocks
	private CategoryService categoryService;

	@DisplayName("카테고리 목록을 조회한다.")
	@Test
	void getCategoryList() {
		Category category = EntityFixture.createCategory();
		List<Category> categoryList = new ArrayList<>();
		categoryList.add(category);

		given(categoryRepository.findAllByDeletedAtIsNotNull()).willReturn(categoryList);

		List<CategoryRes> result = categoryService.getCategoryList();
		assertThat(result.get(0).getId()).isEqualTo(category.getId());
		assertThat(result.get(0).getCategoryName()).isEqualTo(category.getCategoryName());
		assertThat(result.get(0).getRegisteredAt()).isEqualTo(category.getRegisteredAt());
		assertThat(result.get(0).getUpdatedAt()).isEqualTo(category.getUpdatedAt());
		assertThat(result.get(0).getDeletedAt()).isEqualTo(category.getDeletedAt());
		verify(categoryRepository, times(1)).findAllByDeletedAtIsNotNull();
	}
}