package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {
    @NotBlank(message = "User Name is required.")
    @Size(min = 3, message = "Use Min. 3 character in username.")
    private String name;
    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid Email address.")
    private String email;
    @NotBlank(message = "Password is required.")
    @Size(min = 6, message = "Use Min. 6 character in password.")
    private String password;
    @Size(min = 8, max = 12, message = "Invalid phone number.")
    private String phoneNumber;
    @NotBlank(message = "About is required.")
    private String about;

}
