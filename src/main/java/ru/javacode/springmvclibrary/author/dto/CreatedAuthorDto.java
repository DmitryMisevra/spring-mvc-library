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
public class CreatedAuthorDto {

    @NotBlank(message = "Не указано имя автора")
    private String authorName;
    @NotBlank(message = "Не указано имя автора")
    private String authorSurname;
}
