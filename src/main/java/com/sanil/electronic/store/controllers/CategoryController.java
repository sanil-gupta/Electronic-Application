package com.sanil.electronic.store.controllers;

import com.sanil.electronic.store.dtos.*;
import com.sanil.electronic.store.services.CategoryService;
import com.sanil.electronic.store.services.FileService;
import com.sanil.electronic.store.services.ProductService;
import io.swagger.annotations.Api;
import org.hibernate.sql.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/categories")
@Api(value = "CategoryController",description = "APIs related to CategoryController")
public class CategoryController {

    Logger logger = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;

    @Autowired
    private ProductService productService;
    @Value("${category.profile.image.path}")
    private String imageUploadPath;

    //create
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestBody CategoryDto categoryDto) {

        CategoryDto updateCategory = categoryService.update(categoryDto, categoryId);
        return new ResponseEntity<>(updateCategory, HttpStatus.CREATED);
    }

    //delete
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable("categoryId") String categoryId) {
        categoryService.delete(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category is deleted Successfully !!").httpStatus(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
                                                                @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        PageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //get single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingle(@PathVariable("categoryId") String categoryId) {
        CategoryDto categoryDto = categoryService.getById(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    //search
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryDto>> search(@PathVariable("keyword") String keyword) {
        List<CategoryDto> categoryDto = categoryService.searchCategory(keyword);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    //upload category image
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(
            @RequestParam("categoryImage") MultipartFile categoryImage,
            @PathVariable("categoryId") String categoryId) throws IOException {
        String imageName = fileService.uploadFile(categoryImage, imageUploadPath);

        CategoryDto category = categoryService.getById(categoryId);
        category.setImageName(imageName);
        CategoryDto categoryDto = categoryService.update(category, categoryId);

        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).message("Upload Image Successfully !!").success(true).httpStatus(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    //serve user image
    @GetMapping("/image/{categoryId}")
    public void serveUserImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto categoryDto = categoryService.getById(categoryId);
        logger.info("user image name : {} ", categoryDto.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath, categoryDto.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

    //create product with category
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestBody ProductDto productDto) {
        ProductDto productWithCategory = productService.createProductForGivenCategoryId(productDto, categoryId);
        return new ResponseEntity<>(productWithCategory, HttpStatus.CREATED);
    }

    //update category of Product

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryOfProduct(@PathVariable("categoryId") String categoryId,
                                                              @PathVariable("productId") String productId) {
        ProductDto productDto = productService.updateProductForTheGivenCategoryId(productId, categoryId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    //get all products of given categoryId
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>>  getProductsOfGivenCategoryId(
            @PathVariable("categoryId") String categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir)
    {
        PageableResponse<ProductDto> response = productService.getAllProductsWithTheGivenCategoryId(categoryId,pageNumber,pageSize,sortBy,sortDir);
        return  new ResponseEntity<>(response,HttpStatus.OK);
    }

}
