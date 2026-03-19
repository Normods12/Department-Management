package com.department.Dto;


import com.department.Entity.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Name is required")
    private String name;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Role is required")
    private Employee.Role role;

    @NotNull(message = "Salary is required")
    private Double salary;
    private LocalDate joinDate;
    @NotNull(message = "Department id")
    private Long departmentId;
}
