package com.jooany.letsdeal.controller;

import com.jooany.letsdeal.controller.dto.request.SaleSaveReq;
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
    public Response<Void> saveSale(@RequestPart("saleCreateReq") SaleSaveReq saleCreateReq, @RequestPart(required = false)List<MultipartFile> imageFiles, Authentication authentication) throws IOException {
        saleService.saveSale(saleCreateReq, imageFiles, authentication.getName());
        return Response.success();
    }

    @PatchMapping("/{id}")
    public Response<Void> updateSale(@PathVariable Long id, @RequestPart("saleCreateReq") SaleSaveReq saleCreateReq, @RequestPart(required = false)List<MultipartFile> imageFiles, Authentication authentication) throws IOException {
        saleService.updateSale(id, saleCreateReq, imageFiles, authentication.getName());
        return Response.success();
    }

    @DeleteMapping("/{id}")
    public Response<Void> updateSale(@PathVariable Long id, Authentication authentication){
        saleService.deleteSale(id, authentication.getName());
        return Response.success();
    }
}
