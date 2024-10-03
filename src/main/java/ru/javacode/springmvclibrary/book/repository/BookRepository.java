package ru.javacode.springmvclibrary.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javacode.springmvclibrary.book.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
