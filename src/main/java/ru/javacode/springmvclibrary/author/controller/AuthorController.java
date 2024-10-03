package ru.javacode.springmvclibrary.author.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javacode.springmvclibrary.author.dto.AuthorDto;
import ru.javacode.springmvclibrary.author.dto.CreatedAuthorDto;
import ru.javacode.springmvclibrary.author.dto.UpdatedAuthorDto;
import ru.javacode.springmvclibrary.author.service.AuthorService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/authors")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;


    @GetMapping(path = "/{authorId}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable @Min(1) Long authorId) {
        return ResponseEntity.ok(authorService.getAuthorById(authorId));
    }

    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody CreatedAuthorDto createdAuthorDto) {
        return new ResponseEntity<>(authorService.createAuthor(createdAuthorDto), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{authorId}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable @Min(1) Long authorId,
                                                  @Valid @RequestBody UpdatedAuthorDto updatedAuthorDto) {
        return ResponseEntity.ok(authorService.updateAuthor(authorId, updatedAuthorDto));
    }

    @DeleteMapping(path = "/{authorId}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable @Min(1) Long authorId) {
        authorService.deleteAuthor(authorId);
        return ResponseEntity.noContent().build();

    }

    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }
}
