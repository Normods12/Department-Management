package com.department.Repository;

import com.department.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepRepo extends JpaRepository<Department,Long> {
    boolean existsByDepartmentCode(String departmentCode);
    boolean existsByEmail(String email);
}
