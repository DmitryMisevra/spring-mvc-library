package ru.javacode.springmvclibrary.author.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javacode.springmvclibrary.author.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
