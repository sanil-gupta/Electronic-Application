package com.sanil.electronic.store.repositories;

import com.sanil.electronic.store.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {

    List<Category> findByTitleContaining(String keyword);
}
