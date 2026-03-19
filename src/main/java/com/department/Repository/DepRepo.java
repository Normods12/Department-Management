package com.department.Repository;

import com.department.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepRepo extends JpaRepository<Department,Long> {
}
