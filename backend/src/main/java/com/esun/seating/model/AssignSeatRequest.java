package com.esun.seating.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AssignSeatRequest(
        @NotBlank
        @Pattern(regexp = "^[A-Za-z0-9]{5}$", message = "empId must be 5 alphanumeric characters")
        String empId,

        @NotNull
        Integer newSeatSeq
) {
}
