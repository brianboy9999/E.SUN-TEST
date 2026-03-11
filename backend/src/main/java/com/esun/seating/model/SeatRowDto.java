package com.esun.seating.model;

public record SeatRowDto(
        Integer FLOOR_SEAT_SEQ,
        Integer FLOOR_NO,
        Integer SEAT_NO,
        String EMP_ID
) {
}
