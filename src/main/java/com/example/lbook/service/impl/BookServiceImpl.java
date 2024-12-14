package com.example.lbook.service.impl;
import org.springframework.security.access.AccessDeniedException;
import com.example.lbook.dto.rp.BookDto;
import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rq.BookForm;
import com.example.lbook.entity.*;
import com.example.lbook.repository.AuthorRepository;
import com.example.lbook.repository.BookRepository;
import com.example.lbook.repository.CategoryRepository;
import com.example.lbook.repository.UserRepository;
import com.example.lbook.service.AuthorService;
import com.example.lbook.service.BookService;
import com.example.lbook.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    public BookDto createBook(BookForm bookForm) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccessDeniedException("User is not authorized"));
        log.info("Creating book");
        Book newBook = new Book();
        newBook.setBookName(bookForm.getBookName());
        newBook.setPrice(bookForm.getPrice());
        newBook.setDescription(bookForm.getDescription());
        newBook.setQuantity(bookForm.getAmount());
        newBook.setCurrentQuantity(bookForm.getAmount());
        newBook.setPostingDate(LocalDate.now());
        newBook.setApproved(false);
        newBook.setUser(user);

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
                // Update the path to save in /public/uploads/bookImages
                Path uploadPath = Paths.get(System.getProperty("user.dir") + "/public/uploads/bookImages");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    log.info("Directory created: {}", System.getProperty("user.dir") + "/public/uploads/bookImages");
                }

                // Use the book's ID as the image filename
                String fileName = newBook.getBookId(). toString()
                        + bookForm.getImage().getOriginalFilename().substring(bookForm.getImage().getOriginalFilename().lastIndexOf("."));
                Path filePath = uploadPath.resolve(fileName);
                bookForm.getImage().transferTo(filePath.toFile());

                newBook.setImage(fileName);
            } catch (IOException e) {
                log.error("Failed to upload image: {}", e.getMessage());
                throw new RuntimeException("Image upload failed", e);
            }
        } else {
            log.warn("No image provided in the form.");
            newBook.setImage("default.jpg"); // Gán giá trị mặc định nếu không có ảnh
        }

        // Save the book
        bookRepository.save(newBook);
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

    @Override
    public String updateBook(BookForm bookForm, Long bookId) {

        log.info("Update book");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccessDeniedException("User is not authorized"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new UsernameNotFoundException("Post not found"));

        if (user.getUserId() != book.getUser().getId()) {
            return "user is not authorized";
        }
        book.setBookName(bookForm.getBookName());
        book.setPrice(bookForm.getPrice());
        book.setDescription(bookForm.getDescription());
        book.setQuantity(bookForm.getAmount() + book.getQuantity());
        book.setCurrentQuantity(book.getCurrentQuantity() + bookForm.getAmount());

        Category category = categoryService.checkOrCreateCategory(bookForm.getCategoryName());
        book.setCategory(category);

        Author author = authorService.checkOrCreateAuthor(bookForm.getAuthorName());
        book.setAuthor(author);
        bookRepository.save(book);
        return "update book successfully";

    }

    @Override
    public String deleteBook(Long bookId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccessDeniedException("User is not authorized"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new UsernameNotFoundException("Post not found"));
        if(user.getUserId() != book.getUser().getId()) {
            return "user is not authorized";
        }
        bookRepository.deleteById(bookId);
        return "Book deleted successfully";
    }

}
