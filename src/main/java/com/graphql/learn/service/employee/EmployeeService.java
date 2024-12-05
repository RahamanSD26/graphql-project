package com.graphql.learn.service.employee;

import com.graphql.learn.domain.employee.Employee;
import com.graphql.learn.dto.employee.EmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    EmployeeDTO createEntity(EmployeeDTO requestDTO) throws Exception;

    EmployeeDTO getEntityById(String id) throws Exception;

    Page<EmployeeDTO> getAllEntities(Pageable pageable)throws Exception;

    List<Employee> getAllEmployees(int page)throws Exception;

    EmployeeDTO getProfileByMobileNumberWithNull(String mobileNumber) throws Exception;

    EmployeeDTO updateEntity(String id, EmployeeDTO requestDTO) throws Exception;

    EmployeeDTO deleteEntityById(String id) throws Exception;

    Long getAllEntitiesCount();
}
