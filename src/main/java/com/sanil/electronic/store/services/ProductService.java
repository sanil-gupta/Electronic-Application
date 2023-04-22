package com.sanil.electronic.store.services;

import com.sanil.electronic.store.dtos.PageableResponse;
import com.sanil.electronic.store.dtos.ProductDto;

public interface ProductService {
    //create
    ProductDto createProduct(ProductDto productDto);

    //update
    ProductDto updateProduct(ProductDto productDto, String productId);

    //delete
    void deleteProduct(String productId);

    //get single
    ProductDto getSingleProduct(String productId);

    //get all
    PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get all live product
    PageableResponse<ProductDto> getAllLiveProduct(int pageNumber, int pageSize, String sortBy, String sortDir);

    //search product
    PageableResponse<ProductDto> searchByTitle(String subtitle, int pageNumber, int pageSize, String sortBy, String sortDir);

    //create product with category
    ProductDto createProductForGivenCategoryId(ProductDto productDto, String categoryId);

    //update category of product
    ProductDto updateProductForTheGivenCategoryId(String productId, String categoryId);

    //get all product of given categoryId
    PageableResponse<ProductDto> getAllProductsWithTheGivenCategoryId(String categoryId,int pageNumber,int pageSize, String sortBy, String sortDir);
}
