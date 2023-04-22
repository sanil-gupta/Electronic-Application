package com.sanil.electronic.store.controllers;

import com.sanil.electronic.store.dtos.AddItemToCartRequest;
import com.sanil.electronic.store.dtos.ApiResponseMessage;
import com.sanil.electronic.store.dtos.CartDto;
import com.sanil.electronic.store.services.CartService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.locks.ReadWriteLock;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    //add item to cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody AddItemToCartRequest request, @PathVariable("userId") String userid) {
        CartDto cartDto = cartService.addItemToCart(userid, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/item/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable("userId") String userId, @PathVariable("itemId") int itemId) {
        cartService.removeItemFormCart(userId, itemId);
        ApiResponseMessage response = ApiResponseMessage.builder().message("Item is removed")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable("userId") String userId) {
        cartService.clearCart(userId);
        ApiResponseMessage response = ApiResponseMessage.builder().message("Cart is blank")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable("userId") String userId) {
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

}
