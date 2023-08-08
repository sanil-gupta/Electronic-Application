package com.sanil.electronic.store.services.impl;

import com.sanil.electronic.store.dtos.AddItemToCartRequest;
import com.sanil.electronic.store.dtos.CartDto;
import com.sanil.electronic.store.entities.Cart;
import com.sanil.electronic.store.entities.CartItem;
import com.sanil.electronic.store.entities.Product;
import com.sanil.electronic.store.entities.User;
import com.sanil.electronic.store.exception.BadApiRequestException;
import com.sanil.electronic.store.exception.ResourceNotFoundException;
import com.sanil.electronic.store.repositories.CartItemRepository;
import com.sanil.electronic.store.repositories.CartRepository;
import com.sanil.electronic.store.repositories.ProductRepository;
import com.sanil.electronic.store.repositories.UserRepository;
import com.sanil.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        int quantity = request.getQuantity();
        String productId = request.getProductId();

        if (quantity <= 0) {
            throw new BadApiRequestException("Requested quantity is not valid !!");
        }

        //fetch the user from DB
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found in DB!"));

        //fetch the product from DB
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not in DB"));

        //fetch the cart but here those two cases apply which we mention in cartService interface
        Cart cart = null;

        try {
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }
        //if cart item already present: then update
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        items = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                updated.set(true);
            }

            return item;
        }).collect(Collectors.toList());

        //create items
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(product.getDiscountedPrice() * quantity)
                    .cart(cart)
                    .product(product)
                    .build();

            cart.getItems().add(cartItem);
        }
        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);

        return mapper.map(updatedCart, CartDto.class);

    }

    @Override
    public void removeItemFormCart(int cartItem) {
        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("CartIem not found in DB"));
        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {
        //fetch user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given Id !!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart not found with the given id"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given Id !!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart not found with the given id"));
        return mapper.map(cart, CartDto.class);
    }
}
