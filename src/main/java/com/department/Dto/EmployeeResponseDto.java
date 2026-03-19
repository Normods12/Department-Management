package com.department.Dto;

import com.department.Entity.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public class EmployeeResponseDto {

    private Long id;
    private String email;
    private String password;
    private String name;
    @Enumerated(EnumType.STRING)
    private Employee.Role role;
    private Double salary;
    private LocalDate joinDate;
}
