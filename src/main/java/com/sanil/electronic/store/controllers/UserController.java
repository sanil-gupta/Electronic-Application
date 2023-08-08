package com.sanil.electronic.store.controllers;

import com.sanil.electronic.store.dtos.ApiResponseMessage;
import com.sanil.electronic.store.dtos.ImageResponse;
import com.sanil.electronic.store.dtos.PageableResponse;
import com.sanil.electronic.store.dtos.UserDto;
import com.sanil.electronic.store.services.FileService;
import com.sanil.electronic.store.services.UserService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
@Api(value = "UserController", description = "All APIs Related to UserController !!")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto)
    {
       UserDto newUserDto =  userService.createUser(userDto);
       return new ResponseEntity<>(newUserDto, HttpStatus.CREATED);
    }

    //update
   @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid
                                    @PathVariable("userId") String userId,
                                    @RequestBody UserDto dto)
   {
       UserDto updatedUserDto = userService.updateUser(dto, userId);
       return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
   }

    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId)
    {
        userService.deleteUser(userId);
        ApiResponseMessage response = ApiResponseMessage
                                .builder()
                                .message("User is deleted successfully !!")
                                .success(true)
                                .httpStatus(HttpStatus.OK)
                                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
                        @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                        @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
                        @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir)
    {
        PageableResponse<UserDto> response = userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir);
       return new ResponseEntity<>(response,HttpStatus.OK);
    }
    //get single
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") String userId)
    {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }
    //get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email)
    {
       return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }
    //search user
    @GetMapping("/search/{keywords}")
   public ResponseEntity<List<UserDto>> searchUser(@PathVariable("keywords") String keywords)
    {
        return new ResponseEntity<>(userService.searchUsers(keywords),HttpStatus.OK);
    }

    //upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile userImage, @PathVariable("userId") String userId) throws IOException {
       String imageName = fileService.uploadFile(userImage,imageUploadPath);
       UserDto user = userService.getUserById(userId);
       user.setImageName(imageName);
       UserDto userDto = userService.updateUser(user,userId);
       ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).message("Uploaded successfully").success(true).httpStatus(HttpStatus.CREATED).build();
       return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    //serve user image
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user = userService.getUserById(userId);
        logger.info("user image name : {} ",user.getImageName());
        InputStream resource  =fileService.getResource(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}