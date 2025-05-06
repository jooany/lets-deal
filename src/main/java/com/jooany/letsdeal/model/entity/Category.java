package com.jooany.letsdeal.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "categories")
@SequenceGenerator(
        name = "CATEGORIES_SEQ_GENERATOR",
        sequenceName = "CATEGORIES_SEQ"
)
public class Category extends SoftDeletableBaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORIES_SEQ_GENERATOR")
    private Long id;

    @Column(nullable = false)
    private String categoryName;

    @Builder(builderMethodName = "categoryBuilder")
    private Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public static CategoryBuilder builder(String categoryName) {
        return categoryBuilder()
                .categoryName(categoryName);
    }
}
