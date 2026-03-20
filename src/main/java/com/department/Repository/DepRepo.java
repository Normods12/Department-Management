package com.department.Repository;

import com.department.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepRepo extends JpaRepository<Department,Long> {
    boolean existsByDepartmentCode(String departmentCode);
    boolean existsByEmail(String email);
    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.employees")
    List<Department> findAllWithEmployees();
}
