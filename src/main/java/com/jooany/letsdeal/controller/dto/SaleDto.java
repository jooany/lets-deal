package com.jooany.letsdeal.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jooany.letsdeal.model.entity.Category;
import com.jooany.letsdeal.model.entity.Image;
import com.jooany.letsdeal.model.entity.Sale;
import com.jooany.letsdeal.model.entity.User;
import com.jooany.letsdeal.model.enumeration.SaleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleDto {
    private Long id;
    private User user;
    private Category category;
    private List<ImageDto> images;
    private String title;
    private String contents;
    private Integer sellerPrice;
    private SaleStatus saleStatus;
    private Timestamp registeredAt;
    private Timestamp updateAt;
    private Timestamp deletedAt;

    public static SaleDto from(Sale sale){
        List<ImageDto> imageDtos = new ArrayList<>();

        List<Image> images = sale.getImages();
        for(Image image : images){
            imageDtos.add(ImageDto.from(image));
        }

        return new SaleDto(
                sale.getId(),
                sale.getUser(),
                sale.getCategory(),
                imageDtos,
                sale.getTitle(),
                sale.getContents(),
                sale.getSellerPrice(),
                sale.getSaleStatus(),
                sale.getRegisteredAt(),
                sale.getUpdateAt(),
                sale.getDeletedAt()
        );
    }
}
