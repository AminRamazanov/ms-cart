package com.example.mscart.model.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderContactInfoDto {
    @NotBlank(message = "Contact name is required")
    @Size(min = 2, max = 50, message = "Contact name must be between 2 and 50 characters")
    private String contactName;

    @NotBlank(message = "Contact surname is required")
    @Size(min = 2, max = 50, message = "Contact surname must be between 2 and 50 characters")
    private String contactSurname;

    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]{3,}@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Invalid email format"
    )
    private String email;

    @NotBlank(message = "PHONE NUMBER CANNOT BE EMPTY")
    @Pattern(regexp = "^\\+994(50|51|55|70|77|99)[0-9]{7}$",
            message = "PHONE NUMBER MUST BE A VALID AZERBAIJAN PHONE NUMBER")
    private String contactPhone;

    @Future(message = "Desired delivery time must be in the future")
    @NotNull(message = "Desired delivery time is required")
    private LocalDateTime desiredDeliveryTime;
}
