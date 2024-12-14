package com.example.lbook.service.impl;

import com.example.lbook.dto.rp.CartItemDto;
import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rp.ResponseError;
import com.example.lbook.dto.rq.CartItemForm;
import com.example.lbook.entity.Book;
import com.example.lbook.entity.Cart;
import com.example.lbook.entity.CartItem;

import com.example.lbook.entity.User;
import com.example.lbook.repository.BookRepository;
import com.example.lbook.repository.CartItemRepository;
import com.example.lbook.repository.CartRepository;
import com.example.lbook.repository.UserRepository;
import com.example.lbook.service.CartItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public ResponseData<String> addCartItem(Long bookId, CartItemForm cartItemForm) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            log.error("User not found for email {}", email);
            return new ResponseError<>(404, "User not found");
        }

        Cart cart = cartRepository.findByUser_UserId(user.getUserId());

        Book book = bookRepository.findById(bookId)
                .orElse(null);
        if (book == null) {
            log.error("Book not found for id {}", bookId);
            return new ResponseError<>(404, "Book not found");
        }

        if (book.isApproved()) {
            log.error("Book with id {} is approved and cannot be added to the cart", bookId);
            return new ResponseError<>(400, "Book is approved and cannot be added to the cart");
        }

        if (cartItemForm.getAmount() > book.getCurrentAmount()) {
            log.error("Requested quantity is greater than available quantity");
            return new ResponseError<>(400, "Requested quantity is greater than available quantity");
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getBook().getBookId().equals(book.getBookId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem itemToUpdate = existingItem.get();

            Long newAmount = itemToUpdate.getAmount() + cartItemForm.getAmount();

            if (newAmount > book.getCurrentAmount()) {
                log.error("Requested quantity is greater than available quantity");
                return new ResponseError<>(400, "Requested quantity is greater than available quantity");
            }

            itemToUpdate.setAmount(newAmount);
            itemToUpdate.setPrice(book.getPrice() * newAmount);

            cartItemRepository.save(itemToUpdate);

            book.setCurrentAmount(book.getCurrentAmount() - cartItemForm.getAmount());
            bookRepository.save(book);

            return new ResponseData<>(200, "Cart item updated successfully");
        } else {
            CartItem newItem = CartItem.builder()
                    .book(book)
                    .amount(cartItemForm.getAmount())
                    .price(book.getPrice() * cartItemForm.getAmount())
                    .cart(cart)
                    .build();

            cartItemRepository.save(newItem);

            book.setCurrentAmount(book.getCurrentAmount() - cartItemForm.getAmount());
            bookRepository.save(book);

            return new ResponseData<>(200, "Cart item added successfully");
        }
    }



    @Override
    public ResponseData<String> removeCartItem(Long bookId, CartItemForm cartItemForm) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            log.error("User not found for email {}", email);
            return new ResponseError<>(404, "User not found");
        }

        Cart cart = cartRepository.findByUser_UserId(user.getUserId());
        if (cart == null) {
            log.error("Cart not found for user id {}", user.getUserId());
            return new ResponseError<>(404, "Cart not found");
        }

        CartItem cartItem = cartItemRepository.findByCartAndBook_BookId(cart, bookId).orElse(null);
        if (cartItem == null) {
            log.error("Item not found in cart for book id {}", bookId);
            return new ResponseError<>(404, "Item not found in cart");
        }

        if (cartItem.getAmount() < cartItemForm.getAmount()) {
            log.error("Attempt to remove more items than present in the cart");
            return new ResponseError<>(400, "Cannot remove more items than are in the cart");
        }

        Long newAmount = cartItem.getAmount() - cartItemForm.getAmount();
        if (newAmount > 0) {
            cartItem.setAmount(newAmount);
            cartItem.setPrice(cartItem.getBook().getPrice() * newAmount);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.delete(cartItem);
        }

        Book book = cartItem.getBook();
        Long newCurrentAmount = book.getCurrentAmount() + cartItemForm.getAmount();
        book.setCurrentAmount(newCurrentAmount);
        bookRepository.save(book);

        return new ResponseData<>(200, "Cart item removed successfully");
    }



    @Override
    public ResponseData<String> deleteCartItem(Long cartItemId) {
        // Lấy thông tin người dùng đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Tìm người dùng theo email
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            log.error("User not found for email {}", email);
            return new ResponseError<>(404, "User not found");
        }

        // Lấy giỏ hàng của người dùng
        Cart cart = cartRepository.findByUser_UserId(user.getUserId());
        if (cart == null) {
            log.error("Cart not found for user id {}", user.getUserId());
            return new ResponseError<>(404, "Cart not found");
        }

        // Tìm CartItem theo ID
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        if (cartItem == null) {
            log.error("Item not found for id {}", cartItemId);
            return new ResponseError<>(404, "Item not found");
        }

        if (!cartItem.getCart().getCartId().equals(cart.getCartId())) {
            log.error("Unauthorized attempt to delete cart item {} by user {}", cartItemId, email);
            return new ResponseError<>(403, "You are not authorized to delete this item");
        }

        // Xóa CartItem
        cartItemRepository.delete(cartItem);
        log.info("Cart item {} deleted successfully by user {}", cartItemId, email);
        return new ResponseData<>(200, "Cart item deleted successfully");
    }


    @Override
    public ResponseData<Page<CartItemDto>> getCartItems(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            log.error("User not found for email {}", email);
            return new ResponseError<>(404, "User not found");
        }

        Cart cart = cartRepository.findByUser_UserId(user.getUserId());

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("cartItemId")));
        Page<CartItem> cartItems = cartItemRepository.findByCart(cart, pageable);
        Page<CartItemDto> cartItemDtos = cartItems.map(CartItemDto::toDto);

        return new ResponseData<>(200, "Cart items retrieved successfully", cartItemDtos);
    }

}
