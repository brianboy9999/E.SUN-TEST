package com.esun.seating.controller;

import com.esun.seating.model.ApiResponse;
import com.esun.seating.model.AssignSeatRequest;
import com.esun.seating.model.EmployeeDto;
import com.esun.seating.model.SeatRowDto;
import com.esun.seating.service.SeatAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class SeatAssignmentController {

    private final SeatAssignmentService service;

    public SeatAssignmentController(SeatAssignmentService service) {
        this.service = service;
    }

    @GetMapping("/seats")
    public ResponseEntity<List<SeatRowDto>> getSeats() {
        return ResponseEntity.ok(service.getSeats());
    }

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDto>> getEmployees() {
        return ResponseEntity.ok(service.getEmployees());
    }

    @PostMapping("/seats/assign")
    public ResponseEntity<ApiResponse> assignSeat(@Valid @RequestBody AssignSeatRequest request) {
        service.assignSeat(request.empId(), request.newSeatSeq());
        return ResponseEntity.ok(new ApiResponse(true, "Seat updated"));
    }
}
