package com.jooany.letsdeal.model.entity;

import com.jooany.letsdeal.model.enumeration.SaleStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/*
    @NoArgsConstructor(access = AccessLevel.PROTECTED) : 이 클래스의 기본 생성자의 접근 제어자를 protected로 지정하여,
        같은 패키지 혹은 자식 클래스에서만 기본 생성자를 호출할 수 있도록 제한한다.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name ="\"sales\"")
@SequenceGenerator(
        name = "SALE_SEQ_GENERATOR",
        sequenceName = "SALE_SEQ",
        initialValue = 1, allocationSize = 50
)
@Where(clause = "deleted_at is NULL")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "sale", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sale", orphanRemoval = true)
    private List<Proposal> proposals = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "max_price_proposal_id")
    private Proposal maxPriceProposal;

    @Column(name="title")
    private String title;

    @Column(name="contents")
    private String contents;

    @Column(name="seller_price")
    private Integer sellerPrice;

    @Column(name = "sale_status")
    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updateAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void registerdAt() { this.registeredAt = Timestamp.from(Instant.now());}

    @PreUpdate
    void updatedAt() { this.updateAt = Timestamp.from(Instant.now());}

    public static Sale of(User user, Category category, String title, String contents, Integer sellerPrice){
        return Sale.builder()
                .user(user)
                .category(category)
                .title(title)
                .contents(contents)
                .sellerPrice(sellerPrice)
                .saleStatus(SaleStatus.SELLING)
                .build();
    }

    public void update(Category category, String title, String contents, Integer sellerPrice){
        this.category = category;
        this.title = title;
        this.contents = contents;
        this.sellerPrice = sellerPrice;
    }

    public void updateMaxPriceProposal(Proposal proposal){
        this.maxPriceProposal = proposal;
    }

    public void addImage(Image image) {
        images.add(image);
        image.setSale(this);
    }

}
