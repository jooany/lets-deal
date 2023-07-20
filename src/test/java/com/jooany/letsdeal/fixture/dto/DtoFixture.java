package com.jooany.letsdeal.fixture.dto;

import com.jooany.letsdeal.controller.dto.ImageDto;
import com.jooany.letsdeal.controller.dto.request.SaleSaveReq;
import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.SaleListRes;
import com.jooany.letsdeal.controller.dto.response.SaleRes;
import com.jooany.letsdeal.model.enumeration.SaleStatus;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DtoFixture {

    private static String imageUrl = "https://letsdeal-bucket.s3.ap-northeast-2.amazonaws.com/sales/7c18e420-b218-440e-a8af-8df76d6ee223.JPG";

    public static SearchCondition createKeywordSearchCondition(){
        return SearchCondition.builder()
                .keyword("단스탠드")
                .build();
    }

    public static List<SaleListRes> createSaleResList(){
        List<SaleListRes> sales = new ArrayList<>();

        SaleListRes saleRes1 = SaleListRes.builder()
                .id(1L)
                .imageUrl(imageUrl)
                .title("화이트 단스탠드")
                .sellerPrice(10000)
                .saleStatus(SaleStatus.SELLING)
                .maxBuyerPrice(8000)
                .registeredAt(Timestamp.from(Instant.now()))
                .build();

        SaleListRes saleRes2 = SaleListRes.builder()
                .id(2L)
                .imageUrl(imageUrl)
                .title("실버 장스탠드")
                .sellerPrice(15000)
                .saleStatus(SaleStatus.SELLING)
                .maxBuyerPrice(9000)
                .registeredAt(Timestamp.from(Instant.now()))
                .build();

        sales.add(saleRes1);
        sales.add(saleRes2);

        return sales;
    }
    public static SaleRes createSaleRes(){
        return SaleRes.builder()
                .id(1L)
                .userId(1L)
                .userName("testUser")
                .categoryId(1L)
                .categoryName("가구/인테리어")
                .images(createImages())
                .maxBuyerPrice(8000)
                .title("화이트 단스탠드")
                .contents("미개봉 새상품입니다.")
                .sellerPrice(10000)
                .saleStatus(SaleStatus.SELLING)
                .registeredAt(Timestamp.from(Instant.now()))
                .build();
    }

    public static List<ImageDto> createImages(){
        List<ImageDto> images = new ArrayList<>();

        ImageDto image1 = ImageDto.builder()
                .id(1L)
                .imageUrl(imageUrl)
                .sortOrder(1)
                .build();

        ImageDto image2 = ImageDto.builder()
                .id(2L)
                .imageUrl(imageUrl)
                .sortOrder(2)
                .build();

        images.add(image1);
        images.add(image2);

        return images;
    }
    
    // Request DTO
    public static SaleSaveReq createSaleSaveReq(){
        return SaleSaveReq.builder()
                .categoryId(1L)
                .title("해피 책상")
                .contents("2년 정도 쓴 책상이에요. 상태 깨끗해요.")
                .sellerPrice(2000)
                .build();
    }

}
