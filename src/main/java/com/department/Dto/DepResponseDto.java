package com.department.Dto;

import com.department.Entity.Employee;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DepResponseDto {


    private Long id;
    private String departmentName;
    private String departmentCode;
    private Double budget;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private List<Employee> employees;
}
