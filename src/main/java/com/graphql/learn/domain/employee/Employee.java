package com.graphql.learn.domain.employee;

import com.graphql.learn.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Employee extends BaseEntity {
    private String name;
    private String age;
    private String address;
    private String department;
    private String empId;
    private String mobileNumber;

}
