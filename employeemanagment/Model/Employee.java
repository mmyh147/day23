package com.example.employeemanagment.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
public class Employee {

    @NotEmpty
    @Size(min = 3)
    String id;

    @NotEmpty
    @Size(min = 5)
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name Only characters are allowed")
    String name;

    @NotEmpty
    @Email
    String email;

    @NotEmpty
    @Pattern(regexp = "^05\\d{8}$", message = "Invalid phone number format")
    String phone;


    @Min(26)
    Integer age;

    @Pattern(regexp = "^(supervisor|coordinator)$", message = "Invalid role. Must be either supervisor or coordinator")
    String position;
    @AssertFalse
    Boolean onLeave;

    @NotNull
    @PastOrPresent()
    LocalDate hireDate;
    @Positive
    Integer annualLeave;

}
