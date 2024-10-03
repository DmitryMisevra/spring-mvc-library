package ru.javacode.springmvclibrary.author.service;

import ru.javacode.springmvclibrary.author.dto.AuthorDto;
import ru.javacode.springmvclibrary.author.dto.CreatedAuthorDto;
import ru.javacode.springmvclibrary.author.dto.UpdatedAuthorDto;

import java.util.List;

public interface AuthorService {

    AuthorDto getAuthorById(Long authorId);
    AuthorDto createAuthor(CreatedAuthorDto createdAuthorDto);
    AuthorDto updateAuthor(Long authorId, UpdatedAuthorDto updatedAuthorDto);
    void deleteAuthor(Long authorId);
    List<AuthorDto> getAllAuthors();
}
