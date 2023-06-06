package com.jooany.letsdeal.controller;

import com.jooany.letsdeal.controller.dto.request.SaleCreateReq;
import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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

//    @GetMapping
//    public Response<Page<SaleRes>> list(Pageable pageable, Authentication authentication) {
//        return Response.success(saleService.list(pageable).map(SaleRes::fromPostDto));
//    }

    @PostMapping
    public Response<Void> createSale(@RequestPart SaleCreateReq saleCreateReq, @RequestPart(required = false)List<MultipartFile> imageFiles, Authentication authentication) throws IOException {
        saleService.createSale(saleCreateReq, imageFiles, authentication.getName());
        return Response.success();
    }
}
