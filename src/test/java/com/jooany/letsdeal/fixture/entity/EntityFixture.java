package com.jooany.letsdeal.fixture.entity;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.jooany.letsdeal.model.entity.Category;
import com.jooany.letsdeal.model.entity.Image;
import com.jooany.letsdeal.model.entity.Proposal;
import com.jooany.letsdeal.model.entity.Sale;
import com.jooany.letsdeal.model.entity.User;
import com.jooany.letsdeal.model.enumeration.ProposalStatus;
import com.jooany.letsdeal.model.enumeration.SaleStatus;
import com.jooany.letsdeal.model.enumeration.UserRole;

public class EntityFixture {

	private static String imageUrl = "https://letsdeal-bucket.s3.ap-northeast-2.amazonaws.com/sales/7c18e420-b218-440e-a8af-8df76d6ee223.JPG";

	public static User createUser() {
		return User.builder()
			.id(1L)
			.userName("testUser")
			.password("encodedPassword")
			.nickname("nickname")
			.userRole(UserRole.USER)
			.build();
	}

	public static User createUser(String userName, String password, String nickname) {
		return User.builder()
			.id(1L)
			.userName(userName)
			.password(password)
			.nickname(nickname)
			.userRole(UserRole.USER)
			.build();
	}

	public static User createAdmin() {
		return User.builder()
			.id(3L)
			.userName("admin")
			.password("encodedPassword")
			.nickname("admin")
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
		sale.updateImages(images);

		List<Proposal> proposals = createProposals(sale);
		sale.updateProposals(proposals);

		sale.updateMaxPriceProposal(proposals.get(1));

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

		Proposal proposal = createProposal(sale);

		proposals.add(proposal);
		proposals.add(proposal);

		return proposals;
	}

	public static Proposal createProposal(Sale sale) {
		return Proposal.builder()
			.id(1L)
			.user(createUser())
			.sale(sale)
			.buyerPrice(8000)
			.proposalStatus(ProposalStatus.REQUESTING)
			.registeredAt(Timestamp.from(Instant.now()))
			.build();
	}
}
