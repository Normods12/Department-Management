package com.department.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.department.Entity.Employee;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Employee,Integer> {



    Employee findByEmail(String email);
}
