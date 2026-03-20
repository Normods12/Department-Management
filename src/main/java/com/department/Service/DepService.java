package com.department.Service;

import com.department.Dto.*;
import com.department.Entity.Department;
import com.department.Entity.Employee;
import com.department.Exception.DuplicateResourceException;
import com.department.Exception.ResourceNotFoundException;
import com.department.Repository.DepRepo;
import com.department.Repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DepService {

    @Autowired
    DepRepo depRepo;

    @Autowired
    UserRepo userRepo;

    public DepResponseDto addDepartment(DepartmentDto departmentDto){
        log.debug("Checking if department exists : {}",departmentDto.getDepartmentName());
        if(depRepo.existsByDepartmentCode(departmentDto.getDepartmentCode())){
            log.warn("Department already exists");
            throw new DuplicateResourceException("Department already exists");
        }
        log.info("Adding department : {}",departmentDto.getDepartmentName());
        Department dep = new Department();
        dep.setDepartmentName(departmentDto.getDepartmentName());
        dep.setDepartmentCode(departmentDto.getDepartmentCode());
        dep.setBudget(departmentDto.getBudget());
        dep.setCreatedAt(LocalDate.now());
        dep.setUpdatedAt(LocalDate.now());

        depRepo.save(dep);
        log.info("Department added : {}",departmentDto.getDepartmentName());
        return new DepResponseDto(dep.getId(), dep.getDepartmentName(), dep.getDepartmentCode(),dep.getBudget(),dep.getCreatedAt(),dep.getUpdatedAt(), dep.getEmployees() );
    }

    public List<DepResponseDto> displayDepartments(){
        log.debug("Checking if departments exists");
        List<Department> departments = depRepo.findAll();
        if(departments.isEmpty()){
            log.warn("Departments not found");
        }
        List<DepResponseDto> deps = new ArrayList<>();
        for(Department department: departments){
            DepResponseDto dep = new DepResponseDto();
            dep.setId(department.getId());
            dep.setDepartmentName(department.getDepartmentName());
            dep.setDepartmentCode(department.getDepartmentCode());
            dep.setBudget(department.getBudget());
            dep.setCreatedAt(department.getCreatedAt());
            dep.setUpdatedAt(department.getUpdatedAt());
            dep.setEmployees(department.getEmployees());
            deps.add(dep);
        }

        return  deps;
    }

    public BudgetResponseDto displayBudgetUtilization(Long id){
        log.debug("Checking if the department exists : {}",id);
        Department department= depRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Department not Found"));
        log.info("Department found : {}",department.getDepartmentName());
        double budgetGiven = department.getBudget();
        double totalSalary = 0;
        double utilizedBudget = 0;
        double remBudget = 0;
        List<Employee> employees = department.getEmployees();
        for(Employee employee:employees){
            totalSalary += employee.getSalary();
        }

            utilizedBudget = totalSalary;
            remBudget = budgetGiven - totalSalary;


        return new BudgetResponseDto(department.getDepartmentName(), utilizedBudget,remBudget);


    }

    public EmployeeResponseDto addEmployee(EmployeeDto employeeDto){

        log.debug("Checking if Employee exists : {}",employeeDto.getEmail());
        if(userRepo.existsByEmail(employeeDto.getEmail())) {
            log.warn("Employee already exists");
            throw new DuplicateResourceException("Employee already exists");
        }


        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setJoinDate(employeeDto.getJoinDate());
        employee.setSalary(employeeDto.getSalary());
        employee.setRole(Employee.Role.EMPLOYEE);
        Department department = depRepo.findById(employeeDto.getDepartmentId())
                .orElseThrow(()-> new ResourceNotFoundException("Department not Found"));
        log.info("Department assigned to Employee: {}",department.getDepartmentName());
        employee.setDepartment(department);
        userRepo.save(employee);
        return new EmployeeResponseDto((long)employee.getId(),employee.getEmail(),employee.getName(),employee.getSalary(),employee.getJoinDate());
    }


    public EmployeeResponseDto updateEmployee(ModEmployee modEmployee,int id){

        log.debug("Checking if Employee exists : {}",id);
        Employee employee = userRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Employee not found"));
        log.info("Employee found : {}",employee.getName());
        log.debug("Checking if Department exists: {}",modEmployee.getId());

                Department dep = depRepo.findById(modEmployee.getId())
                        .orElseThrow(()-> new ResourceNotFoundException("Department not found"));
        log.info("Department found : {}",dep.getDepartmentName());
        employee.setDepartment(dep);
        userRepo.save(employee);
        return new EmployeeResponseDto((long)employee.getId(),employee.getEmail(),employee.getName(),employee.getSalary(),employee.getJoinDate());

    }

}
