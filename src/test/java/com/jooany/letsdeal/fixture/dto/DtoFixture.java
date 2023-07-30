package com.jooany.letsdeal.fixture.dto;

import com.jooany.letsdeal.controller.dto.ImageDto;
import com.jooany.letsdeal.controller.dto.MyProposalRes;
import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.controller.dto.request.SaleSaveReq;
import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.ProposalRes;
import com.jooany.letsdeal.controller.dto.response.SaleInfoRes;
import com.jooany.letsdeal.controller.dto.response.SaleRes;
import com.jooany.letsdeal.model.enumeration.ProposalStatus;
import com.jooany.letsdeal.model.enumeration.SaleStatus;
import com.jooany.letsdeal.model.enumeration.UserRole;

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

    public static List<SaleRes> createSaleInfoResList(){
        List<SaleRes> sales = new ArrayList<>();

        SaleRes saleInfoRes1 = SaleRes.builder()
                .id(1L)
                .imageUrl(imageUrl)
                .title("화이트 단스탠드")
                .sellerPrice(10000)
                .saleStatus(SaleStatus.SELLING)
                .maxBuyerPrice(8000)
                .registeredAt(Timestamp.from(Instant.now()))
                .build();

        SaleRes saleInfoRes2 = SaleRes.builder()
                .id(2L)
                .imageUrl(imageUrl)
                .title("실버 장스탠드")
                .sellerPrice(15000)
                .saleStatus(SaleStatus.SELLING)
                .maxBuyerPrice(9000)
                .registeredAt(Timestamp.from(Instant.now()))
                .build();

        sales.add(saleInfoRes1);
        sales.add(saleInfoRes2);

        return sales;
    }
    public static SaleInfoRes createSaleInfoRes(){
        return SaleInfoRes.builder()
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

    public static UserDto createUserDto() {
        return UserDto.builder()
                .id(1L)
                .username("testUser")
                .password("encodedPassword")
                .userRole(UserRole.USER)
                .registeredAt(Timestamp.from(Instant.now()))
                .build();
    }

    public static List<ProposalRes> createProposalResList() {
        List<ProposalRes> proposals = new ArrayList<>();

        ProposalRes proposal = ProposalRes.builder()
                .id(1L)
                .saleId(1L)
                .userId(1L)
                .userName("testUser")
                .buyerPrice(6000)
                .proposalStatus(ProposalStatus.WAITING)
                .isProposedByCurrentUser(false)
                .registeredAt(Timestamp.from(Instant.now()))
                .build();

        proposals.add(proposal);

        return proposals;
    }

    public static List<MyProposalRes> createMyProposalResList() {
        List<MyProposalRes> myProposals = new ArrayList<>();

        MyProposalRes myProposal = MyProposalRes.builder()
                .id(1L)
                .saleId(1L)
                .buyerPrice(6000)
                .proposalStatus(ProposalStatus.WAITING)
                .registeredAt(Timestamp.from(Instant.now()))
                .build();

        myProposals.add(myProposal);

        return myProposals;
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
