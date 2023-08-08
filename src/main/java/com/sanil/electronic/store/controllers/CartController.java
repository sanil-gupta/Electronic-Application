package com.sanil.electronic.store.controllers;

import com.sanil.electronic.store.dtos.AddItemToCartRequest;
import com.sanil.electronic.store.dtos.ApiResponseMessage;
import com.sanil.electronic.store.dtos.CartDto;
import com.sanil.electronic.store.services.CartService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/carts")
@Api(value = "CartController",description = "APIs related to cart")
public class CartController {

    @Autowired
    private CartService cartService;

    //add item to cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody AddItemToCartRequest request, @PathVariable("userId") String userid) {
        CartDto cartDto = cartService.addItemToCart(userid, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    //remove item from cart
    @DeleteMapping("/{userId}/item/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable("itemId") int itemId) {
        cartService.removeItemFormCart(itemId);
        ApiResponseMessage response = ApiResponseMessage.builder().message("Item is removed")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //clear cart
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable("userId") String userId) {
        cartService.clearCart(userId);
        ApiResponseMessage response = ApiResponseMessage.builder().message("Cart is blank")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //get cart
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable("userId") String userId) {
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

}
