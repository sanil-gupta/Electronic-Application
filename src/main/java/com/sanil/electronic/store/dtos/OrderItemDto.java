package com.sanil.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItemDto {

    private int orderItemId;
    private int quantity;
    private Double totalPrice;
    private ProductDto product;
}
