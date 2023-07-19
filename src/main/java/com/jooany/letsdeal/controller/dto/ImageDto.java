package com.jooany.letsdeal.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jooany.letsdeal.model.entity.Image;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageDto {
    private Long id;
    private String imageUrl;
    private Integer sortOrder;

    @QueryProjection
    public ImageDto(Long id, String imageUrl, Integer sortOrder) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }

    public static ImageDto from(Image image){
        return new ImageDto(
                image.getId(),
                image.getImageUrl(),
                image.getSortOrder()
        );
    }
}
