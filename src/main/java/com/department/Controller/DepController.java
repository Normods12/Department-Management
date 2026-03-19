package com.department.Controller;

import com.department.Dto.*;
import com.department.Service.DepService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

        return new ResponseEntity<>(depService.addDepartment(departmentDto), HttpStatus.OK) ;

    }

    @GetMapping("/departments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DepResponseDto>> displayDepartments() {

        return new ResponseEntity<>(depService.displayDepartments(), HttpStatus.OK) ;
    }

    @GetMapping("/departments/{id}/budget-utilization")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<BudgetResponseDto> displayBudgetUtilization(@PathVariable Long id) {
        return new ResponseEntity<>(depService.displayBudgetUtilization(id), HttpStatus.OK) ;


    }

    @PostMapping("/employees")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<EmployeeResponseDto> addEmployee(@RequestBody EmployeeDto employeeDto) {

        return new ResponseEntity<>(depService.addEmployee(employeeDto), HttpStatus.OK);
    }

    @PutMapping("/employees/{id}/transfer")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@RequestBody ModEmployee modEmployee, @PathVariable int id) {
        return new ResponseEntity<>(depService.updateEmployee(modEmployee,id), HttpStatus.OK);


    }


}
