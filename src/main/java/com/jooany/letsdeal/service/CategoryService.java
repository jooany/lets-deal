package com.jooany.letsdeal.service;

import com.jooany.letsdeal.controller.dto.response.CategoryRes;
import com.jooany.letsdeal.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryRes> getCategoryList(){
        return categoryRepository.findAllByOrderBySortOrderAsc().stream().map(CategoryRes::fromEntity).collect(Collectors.toList());
    }
}
