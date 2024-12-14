package com.example.lbook.service.impl;

import com.example.lbook.dto.rp.CommentDto;
import com.example.lbook.dto.rp.UserDto;
import com.example.lbook.dto.rq.CommentForm;
import com.example.lbook.entity.BookPost;
import com.example.lbook.entity.Comment;
import com.example.lbook.entity.User;
import com.example.lbook.repository.BookPostRepository;
import com.example.lbook.repository.CommentRepository;
import com.example.lbook.repository.UserRepository;
import com.example.lbook.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookPostRepository bookPostRepository;

    @Override
    public CommentDto createComment(CommentForm commentForm) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccessDeniedException("User is not authorized"));

        BookPost bookPost = bookPostRepository.findById(commentForm.getBookPostId()).orElseThrow(() -> new RuntimeException("Book Post not found"));

        Comment parentComment = null;
        if (commentForm.getParentCommentId() != 0) {
            parentComment = commentRepository.findById(commentForm.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent Comment not found"));
        }

        Comment comment = Comment.builder()
                .user(user)
                .bookPost(bookPost)
                .parentComment(parentComment)
                .content(commentForm.getContent())
                .likes(0L)
                .createdDate(LocalDate.now())
                .build();

        commentRepository.save(comment);

        return CommentDto.toDto(comment);
    }

    @Override
    public List<CommentDto> getAllComments() {
        // Lấy tất cả các bình luận từ cơ sở dữ liệu
        List<Comment> comments = commentRepository.findAll();

        // Chuyển đổi các đối tượng Comment thành CommentDto
        return comments.stream()
                .map(CommentDto::toDto)  // Sử dụng phương thức toDto để chuyển đổi Comment thành CommentDto
                .filter(commentDto -> commentDto.getParentCommentId() == null)
                .sorted(Comparator.comparingLong(CommentDto::getCommentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentsByParentId(Long commentId) {
        List<Comment> comments = commentRepository.findByParentComment_CommentId(commentId);
        return comments.stream()
                .map(CommentDto::toDto) // Chuyển đổi sang DTO
                .collect(Collectors.toList());
    }

    @Override
    public String updateComment(long commentId, CommentForm commentForm) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccessDeniedException("User is not authorized"));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(user.getId())) {
            log.error("User with ID {} does not have permission to update comment ID {}", user.getId(), commentId);
            throw new AccessDeniedException("You do not have permission to update this comment");
        }

        comment.setContent(commentForm.getContent());
        comment.setCreatedDate(LocalDate.now());

        commentRepository.save(comment);

        return "Updated Comment Successfully";
    }

    @Override
    public String deleteComment(long commentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AccessDeniedException("User is not authorized"));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(user.getId())) {
            log.error("User with ID {} does not have permission to delete comment ID {}", user.getId(), commentId);
            throw new AccessDeniedException("You do not have permission to delete this comment");
        }

        commentRepository.delete(comment);
        return "Deleted Comment Successfully";
    }


    @Override
    public String likeComment(long commentId) {
        // Lấy thông tin người dùng từ SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User is not authorized"));

        // Tìm comment theo commentId
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));

        // Kiểm tra xem người dùng đã like comment này chưa
        if (comment.getLikedUsers().contains(user.getId())) {
            // Nếu đã like, hủy like
            comment.getLikedUsers().remove(user.getId());
            comment.setLikes(comment.getLikes() - 1);
            commentRepository.save(comment);
            return "Unliked Comment Successfully";
        } else {
            // Nếu chưa like, thêm ID người dùng vào danh sách
            comment.getLikedUsers().add(user.getId());
            comment.setLikes(comment.getLikes() + 1);
            commentRepository.save(comment);
            return "Liked Comment Successfully";
        }
    }
}
