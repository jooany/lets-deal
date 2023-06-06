package com.jooany.letsdeal.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jooany.letsdeal.model.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageDto {
    private Long id;
    private String imageUrl;
    private Integer sortOrder;

    public static ImageDto from(Image image){
        return new ImageDto(
                image.getId(),
                image.getImageUrl(),
                image.getSortOrder()
        );
    }
}
