package com.example.lbook.service.impl;

import com.example.lbook.dto.rp.BookDto;
import com.example.lbook.dto.rp.BookPostDto;
import com.example.lbook.dto.rq.BookPostForm;
import com.example.lbook.entity.*;
import com.example.lbook.repository.BookPostRepository;
import com.example.lbook.repository.BookRepository;
import com.example.lbook.repository.UserRepository;
import com.example.lbook.service.AuthorService;
import com.example.lbook.service.BookPostService;
import com.example.lbook.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class BookPostServiceImpl implements BookPostService {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookPostRepository bookPostRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    private String title;
    private String bookName;
    private MultipartFile image;
    private LocalDate postingDate;

    @Override
    public BookPostDto create(BookPostForm form){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccessDeniedException("User is not authorized"));
        log.info("Creating book");
        BookPost newBookPost = new BookPost();
        newBookPost.setTitle(form.getTitle());
        newBookPost.setPostingDate(LocalDate.now());

        Book book = bookRepository.findById(form.getBookId()).orElseThrow(() -> new RuntimeException("Book not found"));
        newBookPost.setBook(book);
        newBookPost.setUser(user);
        bookPostRepository.save(newBookPost);

        // Process and save image
        if (form.getImage() != null && !form.getImage().isEmpty()) {
            log.info("Image received: {}", form.getImage().getOriginalFilename());
            try {
                // Update the path to save in /public/uploads/bookImages
                Path uploadPath = Paths.get(System.getProperty("user.dir") + "/public/uploads/bookPostImages");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    log.info("Directory created: {}",System.getProperty("user.dir") + "/public/uploads/bookPostImages");
                }

                // Use the book's ID as the image filename
                String fileName = newBookPost.getBookPostId().toString()
                        + form.getImage().getOriginalFilename().substring(form.getImage().getOriginalFilename().lastIndexOf("."));
                Path filePath = uploadPath.resolve(fileName);
                form.getImage().transferTo(filePath.toFile());

                newBookPost.setImage(fileName);
                bookPostRepository.save(newBookPost); // Update book with image name
            } catch (IOException e) {
                log.error("Failed to upload image: {}", e.getMessage());
                throw new RuntimeException("Image upload failed", e); // Better to throw exception than return null
            }
        } else {
            log.warn("No image provided in the form.");
        }
        bookPostRepository.save(newBookPost);
        log.info("Book created successfully with ID: {}", newBookPost.getBookPostId());
        return BookPostDto.toDto(newBookPost);
    }
// đang sửa
    @Override
    public BookPostDto update(BookPostForm bookPostForm, Long bookPostId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        BookPost bookPost = bookPostRepository.findById(bookPostId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return null;
    }

    @Override
    public List<BookPostDto> getAll() {
        return bookPostRepository.findAll()
                .stream()
                .map(BookPostDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public String deletePost(Long bookPostId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccessDeniedException("User is not authorized"));
        BookPost bookPost = bookPostRepository.findById(bookPostId)
                .orElseThrow(() -> new UsernameNotFoundException("Post not found"));
        if(user.getUserId() != bookPost.getUser().getId()) {
            return "user is not authorized";
        }

        bookPostRepository.deleteById(bookPostId);

        return "Post deleted successfully";
    }
    @Override
    public String likePost(Long bookPostId) {
        // Lấy thông tin người dùng từ SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User is not authorized"));

        // Tìm comment theo commentId
        BookPost bookPost = bookPostRepository.findById(bookPostId).orElseThrow(() -> new RuntimeException("Comment not found"));

        if (bookPost.getLikes() == null) {
            bookPost.setLikes(0L);
        }

        // Kiểm tra xem người dùng đã like comment này chưa
        if (bookPost.getLikedUsers().contains(user.getId())) {
            // Nếu đã like, hủy like
            bookPost.getLikedUsers().remove(user.getId());
            bookPost.setLikes(bookPost.getLikes() - 1);
            bookPostRepository.save(bookPost);
            return "Unliked Comment Successfully";
        } else {
            // Nếu chưa like, thêm ID người dùng vào danh sách
            bookPost.getLikedUsers().add(user.getId());
            bookPost.setLikes(bookPost.getLikes() + 1);
            bookPostRepository.save(bookPost);
            return "Liked Comment Successfully";
        }
    }
}
