package ru.javacode.springmvclibrary.author.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.javacode.springmvclibrary.book.model.Book;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthorDto {

    private Long authorId;
    private String authorName;
    private String authorSurname;
}
