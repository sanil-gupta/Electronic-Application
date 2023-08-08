package com.sanil.electronic.store.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class JwtResponse {

    private String jwtToken;
    private UserDto user;
}
