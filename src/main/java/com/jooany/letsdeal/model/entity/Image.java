package com.jooany.letsdeal.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name ="\"image\"")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @Column(name="imageUrl")
    private String imageUrl;

    @Column(name="sortOrder")
    private Integer sortOrder;

    public static Image of(Sale sale, String imageUrl, Integer sortOrder){
        Image image = new Image();
        image.setSale(sale);
        image.setImageUrl(imageUrl);
        image.setSortOrder(sortOrder);
        return image;
    }


}
