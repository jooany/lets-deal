package com.jooany.letsdeal.fixture.entity;

import com.jooany.letsdeal.model.entity.*;
import com.jooany.letsdeal.model.enumeration.ProposalStatus;
import com.jooany.letsdeal.model.enumeration.SaleStatus;
import com.jooany.letsdeal.model.enumeration.UserRole;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class EntityFixture {

    private static String imageUrl = "https://letsdeal-bucket.s3.ap-northeast-2.amazonaws.com/sales/7c18e420-b218-440e-a8af-8df76d6ee223.JPG";
    public static User createUser() {
        return User.builder()
                .id(1L)
                .userName("testUser")
                .password("encodedPassword")
                .userRole(UserRole.USER)
                .build();
    }

    public static User createUser(String userName, String password) {
        return User.builder()
                .id(1L)
                .userName(userName)
                .password(password)
                .userRole(UserRole.USER)
                .build();
    }

    public static User createAdmin() {
        return User.builder()
                .id(3L)
                .userName("admin")
                .password("encodedPassword")
                .userRole(UserRole.ADMIN)
                .build();
    }

    public static Category createCategory() {
        return Category.builder()
                .id(1L)
                .categoryName("가구/인테리어")
                .build();
    }

    public static Sale createSale() {
        Sale sale = Sale.builder()
                .id(1L)
                .user(createUser())
                .category(createCategory())
                .title("화이트 단스탠드")
                .contents("미개봉 새상품입니다.")
                .sellerPrice(10000)
                .saleStatus(SaleStatus.SELLING)
                .registeredAt(Timestamp.from(Instant.now()))
                .build();

        List<Image> images = createImages(sale);
        sale.setImages(images);

        List<Proposal> proposals = createProposals(sale);
        sale.setProposals(proposals);

        return sale;
    }

    public static List<Image> createImages(Sale sale) {
        List<Image> images = new ArrayList<>();

        Image image1 = Image.builder()
                .id(1L)
                .sale(sale)
                .imageUrl(imageUrl)
                .sortOrder(1)
                .build();

        Image image2 = Image.builder()
                .id(2L)
                .sale(sale)
                .imageUrl(imageUrl)
                .sortOrder(2)
                .build();

        images.add(image1);
        images.add(image2);

        return images;
    }

    public static List<Proposal> createProposals(Sale sale) {
        List<Proposal> proposals = new ArrayList<>();

        Proposal proposal1 = Proposal.builder()
                .id(1L)
                .user(createUser())
                .sale(sale)
                .buyerPrice(8000)
                .proposalStatus(ProposalStatus.WAITING)
                .registeredAt(Timestamp.from(Instant.now()))
                .build();

        Proposal proposal2 = Proposal.builder()
                .id(1L)
                .user(createAdmin())
                .sale(sale)
                .buyerPrice(9000)
                .proposalStatus(ProposalStatus.WAITING)
                .registeredAt(Timestamp.from(Instant.now()))
                .build();

        proposals.add(proposal1);
        proposals.add(proposal2);

        return proposals;
    }
}
