package com.employeetracker.employee.batch;

import com.employeetracker.employee.dto.EmployeeRequest;
import com.employeetracker.employee.entity.ImportError;
import com.employeetracker.employee.repository.ImportErrorRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.listener.SkipListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImportSkipListener
        implements SkipListener<EmployeeRequest, EmployeeRequest> {

    private final ImportErrorRepository importErrorRepository;

    private Long importJobId;

    public void setImportJobId(Long importJobId) {
        this.importJobId = importJobId;
    }

    @Override
    public void onSkipInRead(
            Throwable throwable
    ) {

        saveError(
                null,
                null,
                null,
                "Error while reading Excel row: "
                        + throwable.getMessage()
        );
    }

    @Override
    public void onSkipInWrite(
            EmployeeRequest item,
            Throwable throwable
    ) {

        saveError(
                item,
                item != null
                        ? item.getEmployeeId()
                        : null,
                item != null
                        ? item.getEmail()
                        : null,
                "Error while writing employee: "
                        + throwable.getMessage()
        );
    }

    @Override
    public void onSkipInProcess(
            EmployeeRequest item,
            Throwable throwable
    ) {

        saveError(
                item,
                item != null
                        ? item.getEmployeeId()
                        : null,
                item != null
                        ? item.getEmail()
                        : null,
                throwable.getMessage()
        );
    }

    private void saveError(
            EmployeeRequest item,
            String employeeId,
            String email,
            String message
    ) {

        if (importJobId == null) {
            return;
        }

        ImportError error =
                ImportError.builder()
                        .importJobId(importJobId)
                        .rowNumber(0)
                        .employeeId(employeeId)
                        .email(email)
                        .errorMessage(
                                message == null
                                        ? "Unknown import error"
                                        : message
                        )
                        .build();

        importErrorRepository.save(error);
    }
}