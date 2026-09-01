package com.employeetracker.employee.batch;

import com.employeetracker.employee.dto.EmployeeRequest;
import com.employeetracker.employee.entity.Employee;
import com.employeetracker.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeWriter
        implements ItemWriter<EmployeeRequest> {

    private final EmployeeRepository
            employeeRepository;

    @Override
    public void write(
            Chunk<? extends EmployeeRequest> chunk
    ) {

        for (EmployeeRequest request :
                chunk.getItems()) {

            Employee employee =
                    Employee.builder()
                            .employeeId(
                                    request.getEmployeeId()
                            )
                            .name(
                                    request.getName()
                            )
                            .email(
                                    request.getEmail()
                            )
                            .department(
                                    request.getDepartment()
                            )
                            .salary(
                                    request.getSalary()
                            )
                            .build();

            employeeRepository.save(employee);
        }
    }
}