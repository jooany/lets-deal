package com.jooany.letsdeal.fixture.entity;

import com.jooany.letsdeal.model.entity.*;
import com.jooany.letsdeal.model.enumeration.UserRole;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EntityFixture {

    private static String imageUrl = "https://letsdeal-bucket.s3.ap-northeast-2.amazonaws.com/sales/7c18e420-b218-440e-a8af-8df76d6ee223.JPG";

    public static User createUser() {
        User user = User.builder("testUser", "encodedPassword", "nickname")
                .userRole(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        return user;
    }

    public static User createUser(String userName, String password, String nickname) {
        User user = User.builder(userName, password, nickname)
                .userRole(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        return user;
    }

    public static User createAdmin() {
        User user = User.builder("admin", "encodedPassword", "admin")
                .userRole(UserRole.USER)
                .build();
        ReflectionTestUtils.setField(user, "id", 3L);

        return user;
    }

    public static Category createCategory() {
        Category category = Category.builder("가구/인테리어").build();
        ReflectionTestUtils.setField(category, "id", 1L);
        return category;
    }

    public static Sale createSale() {
        Sale sale = Sale.builder(
                        createUser(),
                        createCategory(),
                        "화이트 단스탠드",
                        "미개봉 새상품입니다.",
                        10000)
                .build();

        ReflectionTestUtils.setField(sale, "id", 1L);
        ReflectionTestUtils.setField(sale, "registeredAt", LocalDateTime.now());

        List<Image> images = createImages(sale);
        sale.updateImages(images);

        List<Proposal> proposals = createProposals(sale);
        sale.updateProposals(proposals);

        sale.updateMaxPriceProposal(proposals.get(1));

        return sale;
    }

    public static List<Image> createImages(Sale sale) {
        List<Image> images = new ArrayList<>();

        Image image1 = Image.builder(sale, imageUrl, 1).build();
        ReflectionTestUtils.setField(image1, "id", 1L);

        Image image2 = Image.builder(sale, imageUrl, 2).build();
        ReflectionTestUtils.setField(image1, "id", 2L);

        images.add(image1);
        images.add(image2);

        return images;
    }

    public static List<Proposal> createProposals(Sale sale) {
        List<Proposal> proposals = new ArrayList<>();

        Proposal proposal = createProposal(sale);

        proposals.add(proposal);
        proposals.add(proposal);

        return proposals;
    }

    public static Proposal createProposal(Sale sale) {
        Proposal proposal = Proposal.builder(
                createUser(),
                sale,
                8000
        ).build();
        ReflectionTestUtils.setField(proposal, "id", 1L);
        ReflectionTestUtils.setField(proposal, "registeredAt", LocalDateTime.now());
        return proposal;
    }

}
