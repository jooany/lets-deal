package com.jooany.letsdeal.model.entity;

import com.jooany.letsdeal.model.enumeration.SaleStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="\"sales\"")
@SQLDelete(sql = "UPDATE \"sales\" SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is NULL")
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

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

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

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

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
