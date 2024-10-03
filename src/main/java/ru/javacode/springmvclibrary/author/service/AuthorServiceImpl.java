package ru.javacode.springmvclibrary.author.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javacode.springmvclibrary.author.dto.AuthorDto;
import ru.javacode.springmvclibrary.author.dto.CreatedAuthorDto;
import ru.javacode.springmvclibrary.author.dto.UpdatedAuthorDto;
import ru.javacode.springmvclibrary.author.mapper.AuthorMapper;
import ru.javacode.springmvclibrary.author.model.Author;
import ru.javacode.springmvclibrary.author.repository.AuthorRepository;
import ru.javacode.springmvclibrary.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    @Transactional(readOnly = true)
    public AuthorDto getAuthorById(Long authorId) {
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("Автор с " +
                "id " + authorId + " не найден"));
        return authorMapper.AuthorToAuthorDto(author);
    }

    @Override
    public AuthorDto createAuthor(CreatedAuthorDto createdAuthorDto) {
        Author author = authorMapper.CreatedAuthorDtoToAuthor(createdAuthorDto);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.AuthorToAuthorDto(savedAuthor);
    }

    @Override
    public AuthorDto updateAuthor(Long authorId, UpdatedAuthorDto updatedAuthorDto) {
        Author authorToUpdate = authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException(
                "Автор с id " + authorId + " не найден"));
        if (updatedAuthorDto.getAuthorName() != null) {
            authorToUpdate.setAuthorName(updatedAuthorDto.getAuthorName());
        }
        if (updatedAuthorDto.getAuthorSurname() != null) {
            authorToUpdate.setAuthorSurname(updatedAuthorDto.getAuthorSurname());
        }
        Author savedAuthor = authorRepository.save(authorToUpdate);
        return authorMapper.AuthorToAuthorDto(savedAuthor);
    }

    @Override
    public void deleteAuthor(Long authorId) {
        authorRepository.deleteById(authorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(authorMapper::AuthorToAuthorDto)
                .toList();
    }
}
