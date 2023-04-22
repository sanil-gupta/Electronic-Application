package com.sanil.electronic.store.services;

import com.sanil.electronic.store.dtos.AddItemToCartRequest;
import com.sanil.electronic.store.dtos.CartDto;

public interface CartService {

    //add item to cart
    //case 1: if cart for the user is not available : we will create  the cart and then add item
    //case 2: if cart available for the user then we directly add the item to cart

    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //Remove item from Cart
    void removeItemFormCart(String userId,int CartItem);

    //Remove all item from cart
    void clearCart(String userId);

    //fetch single cart for particular user
    CartDto getCartByUser(String userId);
}
