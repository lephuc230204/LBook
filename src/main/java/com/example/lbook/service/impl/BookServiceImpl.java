package com.example.lbook.service.impl;

import com.example.lbook.dto.rp.BookDto;
import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rq.BookForm;
import com.example.lbook.entity.Author;
import com.example.lbook.entity.Book;
import com.example.lbook.entity.Category;
import com.example.lbook.repository.AuthorRepository;
import com.example.lbook.repository.BookRepository;
import com.example.lbook.repository.CategoryRepository;
import com.example.lbook.service.AuthorService;
import com.example.lbook.service.BookService;
import com.example.lbook.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public BookDto createBook(BookForm bookForm) {
        log.info("Creating book");
        Book newBook = new Book();
        newBook.setBookName(bookForm.getBookName());
        newBook.setPrice(bookForm.getPrice());
        newBook.setDescription(bookForm.getDescription());
        newBook.setAmount(bookForm.getAmount());
        newBook.setPostingDate(bookForm.getPostingDate());

        // Check or create Category
        Category category = categoryService.checkOrCreateCategory(bookForm.getCategoryName());
        newBook.setCategory(category);

        // Check or create Author
        Author author = authorService.checkOrCreateAuthor(bookForm.getAuthorName());
        newBook.setAuthor(author);

        bookRepository.save(newBook);

        // Process and save image
        if (bookForm.getImage() != null && !bookForm.getImage().isEmpty()) {
            log.info("Image received: {}", bookForm.getImage().getOriginalFilename());
            try {
                Path uploadPath = Paths.get(System.getProperty("user.dir") + "/public/uploads");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    log.info("Directory created: {}",System.getProperty("user.dir") + "/public/uploads");
                }

                // Use the book's ID as the image filename
                String fileName = newBook.getBookId().toString()
                        + bookForm.getImage().getOriginalFilename().substring(bookForm.getImage().getOriginalFilename().lastIndexOf("."));
                Path filePath = uploadPath.resolve(fileName);
                bookForm.getImage().transferTo(filePath.toFile());

                newBook.setImage(fileName);
                bookRepository.save(newBook); // Update book with image name
            } catch (IOException e) {
                log.error("Failed to upload image: {}", e.getMessage());
                return null;
            }
        } else {
            log.warn("No image provided in the form.");
        }

        log.info("Book created successfully with ID: {}", newBook.getBookId());
        return BookDto.toDto(newBook);
    }

    @Override
    public List<BookDto> getMyBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto getBookById(Long bookId) {
        return BookDto.toDto(bookRepository.findById(bookId).orElse(null));
    }

}
