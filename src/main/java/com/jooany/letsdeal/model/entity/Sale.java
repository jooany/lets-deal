package com.jooany.letsdeal.model.entity;

import com.jooany.letsdeal.model.enumeration.SaleStatus;
import jakarta.persistence.*;
import lombok.*;

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
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sale", orphanRemoval = true)
    private List<Proposal> proposals = new ArrayList<>();

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

    @PrePersist
    void registerdAt() { this.registeredAt = Timestamp.from(Instant.now());}

    @PreUpdate
    void updatedAt() { this.updateAt = Timestamp.from(Instant.now());}

    public static Sale of(User user, Category category, String title, String contents, Integer sellerPrice){
        Sale sale = new Sale();
        sale.setUser(user);
        sale.setCategory(category);
        sale.setTitle(title);
        sale.setContents(contents);
        sale.setSellerPrice(sellerPrice);
        sale.setSaleStatus(SaleStatus.SELLING);
        return sale;
    }

    public void update(Category category, String title, String contents, Integer sellerPrice){
        this.category = category;
        this.title = title;
        this.contents = contents;
        this.sellerPrice = sellerPrice;
    }

    public void addImage(Image image) {
        images.add(image);
        image.setSale(this);
    }

}
