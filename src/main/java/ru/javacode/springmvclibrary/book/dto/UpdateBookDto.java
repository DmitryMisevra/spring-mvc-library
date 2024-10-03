package ru.javacode.springmvclibrary.book.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdateBookDto {

    private String bookName;
    @Size(max = 200, message = "Размер описания не может превышать 200 символов")
    private String description;
    @Positive(message = "Id автора должно быть положительным числом")
    private Long authorId;
}
