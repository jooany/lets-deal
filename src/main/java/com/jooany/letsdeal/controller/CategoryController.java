package com.jooany.letsdeal.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jooany.letsdeal.controller.dto.response.CategoryRes;
import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	public Response<List<CategoryRes>> getCategoryList() {
		return Response.success(categoryService.getCategoryList());
	}
}
