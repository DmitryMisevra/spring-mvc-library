package ru.javacode.springmvclibrary.author.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdatedAuthorDto {

    private String authorName;
    private String authorSurname;
}
