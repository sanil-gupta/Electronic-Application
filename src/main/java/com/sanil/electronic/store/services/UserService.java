package com.sanil.electronic.store.services;

import com.sanil.electronic.store.dtos.PageableResponse;
import com.sanil.electronic.store.dtos.UserDto;

import java.util.List;

public interface UserService {

    //create user
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto, String userId);

    //delete
    void deleteUser(String userId);

    //get all user
    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single user by id
    UserDto getUserById(String userid);

    //get single user by email
    UserDto getUserByEmail(String email);

    //search user with given specific keyword
    List<UserDto> searchUsers(String keyword);

    //other user specific user....
}
