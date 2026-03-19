package com.department.Dto;

import com.department.Entity.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetResponseDto {

    private String deptName;
    private Double utilizedBudget;
    private Double remBudget;

}
