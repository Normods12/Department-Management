package com.department.Controller;

import com.department.Dto.*;
import com.department.Service.DepService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class DepController {

    @Autowired
    DepService depService;

    @PostMapping("/departments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepResponseDto> addDepartment(@RequestBody DepartmentDto  departmentDto) {

        return depService.addDepartment(departmentDto);
    }

    @GetMapping("/departments/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DepResponseDto>> displayDepartments(@PathVariable Integer id) {

        return depService.displayDepartments(id);
    }

    @GetMapping("/departments/{id}/budget-utilization")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<BudgetResponseDto> displayBudgetUtilization(@PathVariable Integer id) {

        return depService.displayBudgetUtilization(id);
    }

    @PostMapping("/employees")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<EmployeeResponseDto> addEmployee(@RequestBody EmployeeDto employeeDto) {

        return depService.addEmployee(employeeDto);
    }

    @PutMapping("/employees/{id}/transfer")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@RequestBody ModEmployee modEmployee, @PathVariable Long id) {
        return depService.updateEmployee(modEmployee);
    }


}
