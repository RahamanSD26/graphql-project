package com.graphql.learn.controller;

import com.graphql.learn.config.Constants;
import com.graphql.learn.domain.employee.Employee;
import com.graphql.learn.dto.employee.EmployeeDTO;
import com.graphql.learn.pagination.CustomPage;
import com.graphql.learn.service.employee.EmployeeService;
import com.graphql.learn.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CommonUtil commonUtil;

    @MutationMapping("createEmployee")
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeDTO createEntity(@Argument("employee") EmployeeDTO requestDTO) throws Exception {
        return employeeService.createEntity(requestDTO);
    }
    @QueryMapping("getEmployee")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public EmployeeDTO getEntityById(@Argument String guid) throws Exception {
        return employeeService.getEntityById(guid);
    }
    @QueryMapping("allEmployees")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<Employee> getAllEntities(@Argument int page) throws Exception {
        return employeeService.getAllEmployees(page);
    }


    @MutationMapping("updateEmployee")
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeDTO updateEntity(@Argument("guid") String id,@Argument("employee") EmployeeDTO requestDTO) throws Exception {
        return employeeService.updateEntity(id, requestDTO);
    }

    @MutationMapping("deleteEmployee")
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeDTO deleteEntityById(@Argument("guid") String id) throws Exception {
        return employeeService.deleteEntityById(id);
    }

}
