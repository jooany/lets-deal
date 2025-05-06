package com.jooany.letsdeal.model.entity;

import com.jooany.letsdeal.model.enumeration.SaleStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "sales")
@SequenceGenerator(
        name = "SALES_SEQ_GENERATOR",
        sequenceName = "SALES_SEQ"
)
public class Sale extends SoftDeletableBaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "sale", orphanRemoval = true)
    private List<Proposal> proposals = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "max_price_proposal_id")
    private Proposal maxPriceProposal;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private Integer sellerPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus = SaleStatus.SELLING;

    @Builder(builderMethodName = "salesBuilder")
    private Sale(User writer, Category category, String title, String contents, Integer sellerPrice) {
        this.writer = writer;
        this.category = category;
        this.title = title;
        this.contents = contents;
        this.sellerPrice = sellerPrice;
    }

    public static SaleBuilder builder(
            User writer,
            Category category,
            String title,
            String contents,
            Integer sellerPrice
    ) {
        return salesBuilder()
                .writer(writer)
                .category(category)
                .title(title)
                .contents(contents)
                .sellerPrice(sellerPrice);
    }

    public void update(Category category, String title, String contents, Integer sellerPrice) {
        this.category = category;
        this.title = title;
        this.contents = contents;
        this.sellerPrice = sellerPrice;
    }

    public void updateMaxPriceProposal(Proposal proposal) {
        this.maxPriceProposal = proposal;
    }

    public void addImage(Image image) {
        images.add(image);
        image.updateSale(this);
    }

    public void updateImages(List<Image> images) {
        this.images = images;
    }

    public void updateProposals(List<Proposal> proposals) {
        this.proposals = proposals;
    }

}
