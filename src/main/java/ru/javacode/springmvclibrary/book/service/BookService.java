package ru.javacode.springmvclibrary.book.service;

import ru.javacode.springmvclibrary.book.dto.BookDto;
import ru.javacode.springmvclibrary.book.dto.CreateBookDto;
import ru.javacode.springmvclibrary.book.dto.UpdateBookDto;
import ru.javacode.springmvclibrary.book.model.Book;

import java.util.List;

public interface BookService {

    BookDto getBookById(Long bookId);
    BookDto createBook(CreateBookDto createBookDto);
    BookDto updateBook(Long bookId, UpdateBookDto updateBookDto);
    List<BookDto> getAllBooks(Integer offset, Integer limit, String[] sort);
    void deleteBook(Long bookId);
}
