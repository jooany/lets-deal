package com.jooany.letsdeal.controller;

import com.jooany.letsdeal.controller.dto.response.CategoryRes;
import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Response<List<CategoryRes>> getCategoryList() {
        return Response.success(categoryService.getCategoryList());
    }
}
