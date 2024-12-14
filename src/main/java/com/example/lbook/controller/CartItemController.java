package com.example.lbook.controller;



import com.example.lbook.dto.rp.CartItemDto;
import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rq.CartItemForm;
import com.example.lbook.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("api/v1/cart-items")
@RestController
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping("/add/{bookId}")
    public ResponseEntity<ResponseData<String>> addCartItem(@Valid @PathVariable Long bookId, @RequestBody CartItemForm cartItemForm){
        return ResponseEntity.ok(cartItemService.addCartItem(bookId, cartItemForm));
    }

    @PostMapping("/remove/{bookId}")
    public ResponseEntity<ResponseData<String>> removeCartItem(@Valid @PathVariable Long bookId, @RequestBody CartItemForm cartItemForm){
        return ResponseEntity.ok(cartItemService.removeCartItem(bookId, cartItemForm));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ResponseData<String>> deleteCartItem(@PathVariable Long cartItemId){
        return ResponseEntity.ok(cartItemService.deleteCartItem(cartItemId));
    }

    @GetMapping
    public ResponseEntity<ResponseData<Page<CartItemDto>>> getCartItems( @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(cartItemService.getCartItems(page, size));
    }
}
