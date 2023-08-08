package com.sanil.electronic.store.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {

    @NotBlank(message = "Cart ID Required")
    private String cartId;
    @NotBlank(message = "User ID Required")
    private String userId;

    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    @NotBlank(message = "Address Is required")
    private String billingAddress;
    @NotBlank(message = "Phone Number is Required")
    private String billingPhone;
    @NotBlank(message = "Billing name is Required")
    private String billingName;
    private Date orderdDate;
}
