package com.graphql.learn.service.employee;

import com.graphql.learn.config.Constants;
import com.graphql.learn.domain.employee.Employee;
import com.graphql.learn.dto.employee.EmployeeDTO;
import com.graphql.learn.mapper.employee.EmployeeMapper;
import com.graphql.learn.repository.employee.EmployeeRepository;
import com.graphql.learn.util.CommonUtil;
import com.mongodb.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeDTO createEntity(EmployeeDTO requestDTO) throws Exception {
        //Checking For Existing Country
        if(getProfileByMobileNumberWithNull(requestDTO.getMobileNumber())!=null){
            throw new Exception("ERR_ADMIN_0001") ;
        }

        if(requestDTO.getIsActive()==null){
            requestDTO.setIsActive(true);
        }
        //Incrementally Generating ID & Retry If Got DuplicateKeyException Due To ID
        while(true){
            String newGeneratedGuid= CommonUtil.getNewGeneratedId(employeeRepository, Constants.ID_PREFIX_EMPLOYEE,Constants.ID_PART_NUMBER_COUNT_PROFILE);
            requestDTO.setGuid(newGeneratedGuid);
            try {
                return EmployeeMapper.toDTO(employeeRepository.save(EmployeeMapper.toEntity(requestDTO)));
            }catch(Exception ex){
                if(ex instanceof DuplicateKeyException){continue;}
                throw new Exception("ERR_ADMIN_0002");
            }
        }
    }

    @Override
    public EmployeeDTO getEntityById(String id) throws Exception {
        Optional<Employee> optEntity = employeeRepository.findByGuid(id) ;
        if(optEntity.isEmpty()){
            throw new Exception("ERR_ADMIN_0003") ;
        }
        return EmployeeMapper.toDTO(optEntity.get()) ;
    }

    @Override
    public Page<EmployeeDTO> getAllEntities(Pageable pageable) {
        Page<Employee> entityPage = employeeRepository.findAll(pageable) ;
        if(entityPage.getContent().size()>0){
            return new PageImpl<>(entityPage.getContent().stream().map(EmployeeMapper::toDTO).collect(Collectors.toList()), pageable, entityPage.getTotalElements()) ;
        }else{
            return new PageImpl<>(new ArrayList<>(), pageable, entityPage.getTotalElements()) ;
        }
    }

    @Override
    public List<Employee> getAllEmployees(int page)throws Exception {
        int pageSize = 10;
        int offset = (page - 1) * pageSize;
        return employeeRepository.findAll(PageRequest.of(offset, pageSize)).getContent();
    }

    @Override
    public EmployeeDTO getProfileByMobileNumberWithNull(String mobileNumber) throws Exception {
        Optional<Employee> profile = employeeRepository.findByMobileNumber(mobileNumber) ;
        return profile.map(EmployeeMapper::toDTO).orElse(null);
    }

    @Override
    public EmployeeDTO updateEntity(String id, EmployeeDTO requestDTO) throws Exception {
        EmployeeDTO currentDto = getEntityById(id);
        if (!currentDto.getMobileNumber().equals(requestDTO.getMobileNumber())) {
            if (getProfileByMobileNumberWithNull(requestDTO.getMobileNumber()) != null) {
                throw new Exception("ERR_ADMIN_0024");
            }
        }
        return EmployeeMapper.toDTO(employeeRepository.save(EmployeeMapper.toEntity(EmployeeMapper.toUpdatedDTO(currentDto,requestDTO)))) ;
    }

    @Override
    public EmployeeDTO deleteEntityById(String id) throws Exception {
        EmployeeDTO currentDto = getEntityById(id) ;
        employeeRepository.deleteById(currentDto.getId());
        return currentDto ;
    }

    @Override
    public Long getAllEntitiesCount() {
        return employeeRepository.count();
    }
}
