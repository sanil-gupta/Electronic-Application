package com.sanil.electronic.store.dtos;

import com.sanil.electronic.store.validate.ImageNameValid;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userId;
    @Size(min=3, max = 20,message = "Invalid Name!!")
    private String name;
    //@Email(message = "Invalid Email!!")
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid user email")
    @NotBlank(message = "Email is Required")
    private String email;
    @NotBlank(message = "Password required!!")
    private String password;
    @Size(min=4, max = 6, message = "Invalid gender!!")
    private String gender;
    @NotBlank(message = "Write something about yourself")
    private String about;
    @ImageNameValid
    private String imageName;
}
