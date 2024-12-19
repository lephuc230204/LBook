package com.example.lbook.dto.rp;

import com.example.lbook.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasic {
    private String username;
    private String email;
    private String phone;
    private List<String> fullAddress;
    private LocalDate createdDate;
    private LocalDate dob;

    public static UserBasic to(User user) {
        return UserBasic.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .phone(user.getPhone())
                .fullAddress(user.getAddresses().stream()
                        .map(address -> "AddressID: " +address.getAddressId() + ", full Address: " + address.getFullAddress())
                        .collect(Collectors.toList())

                )
                .createdDate(user.getCreatedDate())
                .dob(user.getDob())
                .build();
    }

}
