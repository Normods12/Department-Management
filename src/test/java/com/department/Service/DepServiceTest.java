package com.department.Service;

import com.department.Dto.*;
import com.department.Entity.Department;
import com.department.Entity.Employee;
import com.department.Exception.DuplicateResourceException;
import com.department.Exception.ResourceNotFoundException;
import com.department.Repository.DepRepo;
import com.department.Repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Tells Spring we are using Mockito for ultra-fast tests
public class DepServiceTest {

    @Mock
    private DepRepo depRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private DepService depService; // Injects the fake repos into your real service

    private Department testDepartment;
    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        // This runs before EVERY test to give us fresh, clean dummy data
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setDepartmentName("Engineering");
        testDepartment.setDepartmentCode("ENG-01");
        testDepartment.setBudget(100000.0);
        testDepartment.setEmployees(new ArrayList<>());

        testEmployee = new Employee();
        testEmployee.setId(100);
        testEmployee.setName("John Doe");
        testEmployee.setEmail("john@company.com");
        testEmployee.setSalary(60000.0);
        testEmployee.setRole(Employee.Role.EMPLOYEE);
        testEmployee.setDepartment(testDepartment);
    }

    // --- 1. Testing Department Logic ---

    @Test
    void addDepartment_Success() {
        // Arrange: What the DTO looks like coming from React
        DepartmentDto dto = new DepartmentDto();
        dto.setDepartmentName("Engineering");
        dto.setDepartmentCode("ENG-01");
        dto.setBudget(100000.0);

        // Tell the fake repo: "When existsByDepartmentCode is called, say FALSE (it's unique)"
        when(depRepo.existsByDepartmentCode("ENG-01")).thenReturn(false);
        // Tell the fake repo: "When save is called, just return our test department"
        when(depRepo.save(any(Department.class))).thenReturn(testDepartment);

        // Act: Run your actual method
        DepResponseDto response = depService.addDepartment(dto);

        // Assert: Prove it worked
        assertNotNull(response);
        assertEquals("Engineering", response.getDepartmentName());
        verify(depRepo, times(1)).save(any(Department.class)); // Verifies save() was called exactly once
    }

    @Test
    void addDepartment_ThrowsDuplicateException() {
        DepartmentDto dto = new DepartmentDto();
        dto.setDepartmentCode("ENG-01");

        // Tell fake repo: "Say TRUE, this code already exists!"
        when(depRepo.existsByDepartmentCode("ENG-01")).thenReturn(true);

        // Assert: Prove your custom exception fires
        assertThrows(DuplicateResourceException.class, () -> depService.addDepartment(dto));
        verify(depRepo, never()).save(any(Department.class)); // Proves we didn't save bad data
    }

    @Test
    void displayDepartments_Success_UsesOptimizedQuery() {
        // Tell fake repo to return a list of 1 department using your NEW optimized method
        when(depRepo.findAllWithEmployees()).thenReturn(List.of(testDepartment));

        List<DepResponseDto> result = depService.displayDepartments();

        assertEquals(1, result.size());
        assertEquals("Engineering", result.get(0).getDepartmentName());
        verify(depRepo, times(1)).findAllWithEmployees(); // Proves you avoided the N+1 trap!
    }

    // --- 2. Testing The Math (Budget Logic) ---

    @Test
    void displayBudgetUtilization_CalculatesCorrectly() {
        // Add two employees to the department (60k + 30k = 90k used out of 100k budget)
        Employee emp2 = new Employee();
        emp2.setSalary(30000.0);
        testDepartment.getEmployees().add(testEmployee); // 60k
        testDepartment.getEmployees().add(emp2);         // 30k

        when(depRepo.findById(1L)).thenReturn(Optional.of(testDepartment));

        BudgetResponseDto response = depService.displayBudgetUtilization(1L);

        // Proves your math logic is flawless
        assertEquals(90000.0, response.getUtilizedBudget());
        assertEquals(10000.0, response.getRemBudget()); // 100k - 90k
    }

    // --- 3. Testing Employee Logic ---

    @Test
    void addEmployee_Success() {
        EmployeeDto dto = new EmployeeDto();
        dto.setName("Jane Doe");
        dto.setEmail("jane@company.com");
        dto.setSalary(50000.0);
        dto.setDepartmentId(1L);
        dto.setJoinDate(LocalDate.now());

        when(userRepo.existsByEmail("jane@company.com")).thenReturn(false);
        when(depRepo.findById(1L)).thenReturn(Optional.of(testDepartment));
        when(userRepo.save(any(Employee.class))).thenReturn(new Employee());

        EmployeeResponseDto response = depService.addEmployee(dto);

        assertNotNull(response);
        assertEquals("jane@company.com", response.getEmail());
    }

    @Test
    void addEmployee_ThrowsNotFoundException_WhenDeptIsInvalid() {
        EmployeeDto dto = new EmployeeDto();
        dto.setEmail("jane@company.com");
        dto.setDepartmentId(999L); // A fake department ID

        when(userRepo.existsByEmail("jane@company.com")).thenReturn(false);
        when(depRepo.findById(999L)).thenReturn(Optional.empty()); // Repo finds nothing

        // Proves your app safely rejects bad department IDs
        assertThrows(ResourceNotFoundException.class, () -> depService.addEmployee(dto));
    }
}