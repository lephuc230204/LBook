package com.example.lbook.dto.rq;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpForm {
    @NotBlank(message = "Username must me not blank! ")
    private String username;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Past(message="Date of birth must be a date in the past")
    private LocalDate dob;

    @Email(message = "The email is not in the correct format! ")
    @NotBlank(message = "password must me not blank! ")
    private String email;

    @NotBlank(message = "password must me not blank! ")
    private String password;

    @NotBlank(message = "Cof-password must me not blank! ")
    private String confirmPassword;
    public String getUsername() {
        return email;
    }
}