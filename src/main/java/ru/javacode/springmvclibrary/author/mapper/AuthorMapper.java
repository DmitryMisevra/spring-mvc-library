package ru.javacode.springmvclibrary.author.mapper;

import org.springframework.stereotype.Component;
import ru.javacode.springmvclibrary.author.dto.AuthorDto;
import ru.javacode.springmvclibrary.author.dto.CreatedAuthorDto;
import ru.javacode.springmvclibrary.author.dto.UpdatedAuthorDto;
import ru.javacode.springmvclibrary.author.model.Author;

@Component
public class AuthorMapper {

    public Author CreatedAuthorDtoToAuthor(CreatedAuthorDto createdAuthorDto) {
        return Author.builder()
                .authorName(createdAuthorDto.getAuthorName())
                .authorSurname(createdAuthorDto.getAuthorSurname())
                .build();
    }

    public Author UpdatedAuthorDtoToAuthor(UpdatedAuthorDto updatedAuthorDto) {
        return Author.builder()
                .authorName(updatedAuthorDto.getAuthorName())
                .authorSurname(updatedAuthorDto.getAuthorSurname())
                .build();
    }

    public AuthorDto AuthorToAuthorDto(Author author) {
        return AuthorDto.builder()
                .authorId(author.getAuthorId())
                .authorName(author.getAuthorName())
                .authorSurname(author.getAuthorSurname())
                .build();
    }
}
