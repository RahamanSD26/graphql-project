package com.graphql.learn.repository.employee;

import com.graphql.learn.domain.employee.Employee;
import com.graphql.learn.repository.BaseRepository;

import java.util.Optional;

public interface EmployeeRepository extends BaseRepository<Employee,String> {
    Optional<Employee> findByMobileNumber(String mobileNumber);
}
