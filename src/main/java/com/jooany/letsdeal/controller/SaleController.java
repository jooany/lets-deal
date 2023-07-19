package com.jooany.letsdeal.controller;

import com.jooany.letsdeal.controller.dto.request.SaleCreateReq;
import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.controller.dto.response.SaleListRes;
import com.jooany.letsdeal.controller.dto.response.SaleRes;
import com.jooany.letsdeal.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
@Slf4j
public class SaleController {

    @Autowired
    private final SaleService saleService;

    @GetMapping
    public Response<Page<SaleListRes>> getSaleList(SearchCondition condition, Pageable pageable, Authentication authentication) {
        return Response.success(saleService.getSaleList(condition, pageable, authentication.getName()));
    }

    @GetMapping("/{id}")
    public Response<SaleRes> getSale(@PathVariable Long id) {
        return Response.success(saleService.getSaleInfo(id));
    }

    @PostMapping
    public Response<Void> createSale(@RequestPart("saleCreateReq") SaleCreateReq saleCreateReq, @RequestPart(required = false)List<MultipartFile> imageFiles, Authentication authentication) throws IOException {
        saleService.createSale(saleCreateReq, imageFiles, authentication.getName());
        return Response.success();
    }
}
