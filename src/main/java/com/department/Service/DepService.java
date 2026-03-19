package com.department.Service;

import com.department.Dto.*;
import com.department.Entity.Department;
import com.department.Entity.Employee;
import com.department.Repository.DepRepo;
import com.department.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepService {

    @Autowired
    DepRepo depRepo;

    @Autowired
    UserRepo userRepo;

    public DepResponseDto addDepartment(DepartmentDto departmentDto){
        Department dep = new Department();
        dep.setDepartmentName(departmentDto.getDepartmentName());
        dep.setDepartmentCode(departmentDto.getDepartmentCode());
        dep.setBudget(departmentDto.getBudget());
        dep.setCreatedAt(LocalDate.now());
        dep.setUpdatedAt(LocalDate.now());

        depRepo.save(dep);
        return new DepResponseDto(dep.getId(), dep.getDepartmentName(), dep.getDepartmentCode(),dep.getBudget(),dep.getCreatedAt(),dep.getUpdatedAt(), dep.getEmployees() );
    }

    public List<DepResponseDto> displayDepartments(){
        List<Department> departments = depRepo.findAll();
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
        Department department= depRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Department not Found"));
        double budgetGiven = department.getBudget();
        double totalSalary = 0;
        double utilizedBudget = 0;
        double remBudget = 0;
        List<Employee> employees = department.getEmployees();
        for(Employee employee:employees){
            totalSalary += employee.getSalary();
        }
        try{
            utilizedBudget = totalSalary;
            remBudget = budgetGiven - totalSalary;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        return new BudgetResponseDto(department.getDepartmentName(), utilizedBudget,remBudget);


    }

    public EmployeeResponseDto addEmployee(EmployeeDto employeeDto){

        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setName(employeeDto.getName());
        employee.setJoinDate(employeeDto.getJoinDate());
        employee.setSalary(employeeDto.getSalary());
        employee.setRole(Employee.Role.EMPLOYEE);
        Department department = depRepo.findById(employeeDto.getDepartmentId())
                .orElseThrow(()-> new RuntimeException("Department not Found"));
        employee.setDepartment(department);
        userRepo.save(employee);
        return new EmployeeResponseDto((long)employee.getId(),employee.getEmail(),employee.getName(),employee.getSalary(),employee.getJoinDate());
    }


    public EmployeeResponseDto updateEmployee(ModEmployee modEmployee,int id){

        Employee employee = userRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Employee not found"))

                Department dep = depRepo.findById(modEmployee.getId())
                        .orElseThrow(()-> new RuntimeException("Department not found"));

        employee.setDepartment(dep);
        userRepo.save(employee);
        return new EmployeeResponseDto((long)employee.getId(),employee.getEmail(),employee.getName(),employee.getSalary(),employee.getJoinDate());

    }




}
