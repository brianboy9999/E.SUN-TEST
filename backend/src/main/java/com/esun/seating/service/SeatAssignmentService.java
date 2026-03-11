package com.esun.seating.service;

import com.esun.seating.model.EmployeeDto;
import com.esun.seating.model.SeatRowDto;
import com.esun.seating.repository.EmployeeRepository;
import com.esun.seating.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeatAssignmentService {

    private final SeatRepository seatRepository;
    private final EmployeeRepository employeeRepository;

    public SeatAssignmentService(SeatRepository seatRepository, EmployeeRepository employeeRepository) {
        this.seatRepository = seatRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<SeatRowDto> getSeats() {
        return seatRepository.findAllSeats();
    }

    public List<EmployeeDto> getEmployees() {
        return employeeRepository.findAllEmployees();
    }

    @Transactional
    public void assignSeat(String empId, Integer newSeatSeq) {
        seatRepository.clearEmployeeSeat(empId);
        seatRepository.clearTargetSeatOccupant(newSeatSeq);

        int updated = seatRepository.assignEmployeeToSeat(empId, newSeatSeq);
        if (updated == 0) {
            throw new IllegalArgumentException("Employee not found: " + empId);
        }
    }
}
