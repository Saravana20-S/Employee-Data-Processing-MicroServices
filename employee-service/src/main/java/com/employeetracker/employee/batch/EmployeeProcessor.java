package com.employeetracker.employee.batch;

import com.employeetracker.employee.dto.EmployeeRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;

public class EmployeeProcessor implements ItemProcessor<EmployeeRequest, EmployeeRequest> {
    @Override
    public @Nullable EmployeeRequest process(EmployeeRequest item) throws Exception {
        return null;
    }
}
