package ru.javacode.springmvclibrary.user.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private String name;
    private String login;
    private Long id;
    private String email;
}
