package com.department.Dto;

import com.department.Entity.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {

    private Long id;
    private String email;

    private String name;

    private Double salary;
    private LocalDate joinDate;
}
