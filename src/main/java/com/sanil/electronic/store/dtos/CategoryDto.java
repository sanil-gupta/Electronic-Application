package com.sanil.electronic.store.dtos;

import com.sanil.electronic.store.validate.ImageNameValid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private String categoryId;
    @NotBlank
    @Size(min = 4, message = "Title must be of minimum 4 character")
    private String title;
    @NotBlank(message = "Description Required !!")
    private String description;
    private String imageName;
}
