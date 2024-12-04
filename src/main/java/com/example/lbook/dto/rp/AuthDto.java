package com.example.lbook.dto.rp;

import com.example.lbook.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
    private Long id;
    private String token;
    private String status;
    private String result;

    public static AuthDto from(User user, String token, String status, String result) {
        return AuthDto.builder()
                .id(user.getId())
                .token(token)
                .status(status)
                .result(result)
                .build();
    }
}
