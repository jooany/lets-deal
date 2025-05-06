package com.jooany.letsdeal.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "images")
@SequenceGenerator(
        name = "IMAGES_SEQ_GENERATOR",
        sequenceName = "IMAGES_SEQ"
)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Integer sortOrder;

    @Builder(builderMethodName = "imageBuilder")
    private Image(Sale sale, String imageUrl, Integer sortOrder) {
        this.sale = sale;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }

    public static ImageBuilder builder(Sale sale, String imageUrl, Integer sortOrder) {
        return imageBuilder()
                .sale(sale)
                .imageUrl(imageUrl)
                .sortOrder(sortOrder);
    }

    public void updateSale(Sale sale) {
        this.sale = sale;
    }

}
