package com.example.lbook.dto.rq;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserForm {
    private String username;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;
    @NotBlank(message = "oldPassword must be not blank!")
    private String email;
    private String phone;
    private String profilePicture;
}