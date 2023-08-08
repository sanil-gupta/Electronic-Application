package com.sanil.electronic.store.services;


import com.sanil.electronic.store.dtos.CreateOrderRequest;
import com.sanil.electronic.store.dtos.OrderDto;
import com.sanil.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface OrderService {


    //create Order
    OrderDto createOrder(CreateOrderRequest orderDto);

    //remove Order
    void removeOrder(String orderId);

    //get Order of user
    List<OrderDto> getOrderOfUser(String userId);

    //get orders
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

}
