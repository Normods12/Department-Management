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

        log.info("Received request to add department : {}",departmentDto.getDepartmentName());
        DepResponseDto dto  = depService.addDepartment(departmentDto);
        log.info("Added department successfully : {}",dto.getDepartmentName());
        return new ResponseEntity<>(dto, HttpStatus.CREATED) ;

    }

    @GetMapping("/departments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DepResponseDto>> displayDepartments() {
        log.info("Fetching List of Departments");
        List<DepResponseDto> list = depService.displayDepartments();
        log.info("List of Departments successfully, No of Departments : {}",list.toArray().length);
        return new ResponseEntity<>(list, HttpStatus.OK) ;
    }

    @GetMapping("/departments/{id}/budget-utilization")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<BudgetResponseDto> displayBudgetUtilization(@PathVariable Long id) {

        log.info("Received request to display budget utilization of {}",id);
        BudgetResponseDto budgetResponseDto = depService.displayBudgetUtilization(id);
        log.info("Budget Utilization was fetched for department : {}",budgetResponseDto.getDeptName());
        return new ResponseEntity<>(budgetResponseDto, HttpStatus.OK) ;


    }

    @PostMapping("/employees")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<EmployeeResponseDto> addEmployee(@RequestBody EmployeeDto employeeDto) {

        log.info("Received request to add Employee: {}",employeeDto.getEmail());
        EmployeeResponseDto emp =  depService.addEmployee(employeeDto);
        log.info("Added Employee successfully : {}",emp.getEmail());
        return new ResponseEntity<>(emp, HttpStatus.CREATED);
    }

    @PutMapping("/employees/{id}/transfer")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@RequestBody ModEmployee modEmployee, @PathVariable int id) {

       log.info("Received request to transfer Employee: {}",id);
       EmployeeResponseDto emp =  depService.updateEmployee(modEmployee,id);
       log.info("Updated Employee successfully : {}",emp.getEmail());
        return new ResponseEntity<>(emp, HttpStatus.OK);


    }


}
