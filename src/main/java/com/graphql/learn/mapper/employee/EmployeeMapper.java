package com.graphql.learn.mapper.employee;

import com.graphql.learn.domain.employee.Employee;
import com.graphql.learn.dto.employee.EmployeeDTO;
import com.graphql.learn.util.CommonUtil;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public static Employee toEntity(EmployeeDTO dto){
        Employee entity=new Employee();

        //Common Fields
        CommonUtil.mapCommonFieldsToEntity(entity,dto);

        //Specific Fields
       entity.setName(dto.getName());
       entity.setAge(dto.getAge());
       entity.setDepartment(dto.getDepartment());
       entity.setAddress(dto.getAddress());
       entity.setEmpId(dto.getEmpId());
       entity.setMobileNumber(dto.getMobileNumber());
       return entity;
    }

    public static EmployeeDTO toDTO(Employee entity){
        EmployeeDTO dto=new EmployeeDTO();

        //Common Fields
        CommonUtil.mapCommonFieldsToDto(entity,dto);

        //Specific Fields
        dto.setName(entity.getName());
        dto.setAge(entity.getAge());
        dto.setDepartment(entity.getDepartment());
        dto.setAddress(entity.getAddress());
        dto.setEmpId(entity.getEmpId());
        dto.setMobileNumber(entity.getMobileNumber());
        return dto;
    }

    public static EmployeeDTO toUpdatedDTO(EmployeeDTO currentDto, EmployeeDTO newDto){
        if(newDto.getName()!=null){
            currentDto.setName(newDto.getName());
        }
        if(newDto.getAge()!=null){
            currentDto.setAge(newDto.getAge());
        }
        if(newDto.getDepartment()!=null){
            currentDto.setDepartment(newDto.getDepartment());
        }
        if(newDto.getAddress()!=null){
            currentDto.setAddress(newDto.getAddress());
        }
        if(newDto.getEmpId()!=null){
            currentDto.setEmpId(newDto.getEmpId());
        }
        if(newDto.getMobileNumber()!=null){
            currentDto.setMobileNumber(newDto.getMobileNumber());
        }
        return currentDto;
    }
}
