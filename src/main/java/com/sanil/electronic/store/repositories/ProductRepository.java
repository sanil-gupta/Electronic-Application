package com.sanil.electronic.store.repositories;

import com.sanil.electronic.store.dtos.PageableResponse;
import com.sanil.electronic.store.entities.Category;
import com.sanil.electronic.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findByTitle(String subTitle, Pageable pageable);

    Page<Product> findByLiveTrue(Pageable pageable);

    Page<Product> findByCategory(Category category, Pageable pageable);

}
