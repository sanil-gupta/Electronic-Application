package com.sanil.electronic.store.dtos;

import com.sanil.electronic.store.entities.Category;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDto {
    private String productId;
    @NotBlank(message = "Title Required!")
    private String title;
    @NotBlank(message = "Description Requited!!")
    private String description;
    private double price;
    private int discountedPrice;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    @NotBlank
    private String productImageName;
    private CategoryDto category;
}
