package com.graphql.learn.dto.employee;

import com.graphql.learn.dto.BaseEntityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO extends BaseEntityDTO {
    private String name;
    private String age;
    private String address;
    private String department;
    private String empId;
    private String mobileNumber;
}
