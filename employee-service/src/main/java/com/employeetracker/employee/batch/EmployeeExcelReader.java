package com.employeetracker.employee.batch;

import com.employeetracker.employee.dto.EmployeeRequest;
import org.apache.poi.ss.usermodel.*;
import org.springframework.batch.infrastructure.item.ItemReader;
//import org.springframework.batch.item.ItemReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class EmployeeExcelReader
        implements ItemReader<EmployeeRequest> {

    private final Workbook workbook;

    private final Sheet sheet;

    private int currentRow = 1;

    public EmployeeExcelReader(String filePath) {

        try {

            FileInputStream inputStream =
                    new FileInputStream(filePath);

            this.workbook =
                    WorkbookFactory.create(inputStream);

            this.sheet =
                    workbook.getSheetAt(0);

            validateHeader();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to read Excel file",
                    e
            );
        }
    }

    private void validateHeader() {

        Row headerRow =
                sheet.getRow(0);

        if (headerRow == null) {

            throw new IllegalArgumentException(
                    "Excel header row is missing"
            );
        }

        String[] expectedHeaders = {

                "employeeId",
                "name",
                "email",
                "department",
                "salary"
        };

        for (int i = 0;
             i < expectedHeaders.length;
             i++) {

            Cell cell =
                    headerRow.getCell(i);

            String actualHeader =
                    cell == null
                            ? ""
                            : new DataFormatter()
                            .formatCellValue(cell)
                            .trim();

            if (!expectedHeaders[i]
                    .equalsIgnoreCase(actualHeader)) {

                throw new IllegalArgumentException(
                        "Invalid header at column "
                                + (i + 1)
                                + ". Expected: "
                                + expectedHeaders[i]
                );
            }
        }
    }

    @Override
    public EmployeeRequest read() {

        while (
                currentRow <=
                        sheet.getLastRowNum()
        ) {

            Row row =
                    sheet.getRow(currentRow++);

            if (row == null ||
                    isEmptyRow(row)) {

                continue;
            }

            return EmployeeRequest.builder()

                    .employeeId(
                            getString(
                                    row.getCell(0)
                            )
                    )

                    .name(
                            getString(
                                    row.getCell(1)
                            )
                    )

                    .email(
                            getString(
                                    row.getCell(2)
                            )
                    )

                    .department(
                            getString(
                                    row.getCell(3)
                            )
                    )

                    .salary(
                            getBigDecimal(
                                    row.getCell(4)
                            )
                    )

                    .build();
        }

        closeWorkbook();

        return null;
    }

    private boolean isEmptyRow(
            Row row
    ) {

        for (int i = 0; i < 5; i++) {

            Cell cell =
                    row.getCell(i);

            if (cell != null &&
                    cell.getCellType()
                            != CellType.BLANK) {

                String value =
                        getString(cell);

                if (value != null &&
                        !value.isBlank()) {

                    return false;
                }
            }
        }

        return true;
    }

    private String getString(
            Cell cell
    ) {

        if (cell == null) {
            return null;
        }

        DataFormatter formatter =
                new DataFormatter();

        String value =
                formatter.formatCellValue(cell);

        return value == null
                ? null
                : value.trim();
    }

    private BigDecimal getBigDecimal(
            Cell cell
    ) {

        if (cell == null) {
            return null;
        }

        if (cell.getCellType()
                == CellType.NUMERIC) {

            return BigDecimal.valueOf(
                    cell.getNumericCellValue()
            );
        }

        String value =
                getString(cell);

        if (value == null ||
                value.isBlank()) {

            return null;
        }

        try {

            return new BigDecimal(value);

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Invalid salary value: "
                            + value
            );
        }
    }

    private void closeWorkbook() {

        try {

            workbook.close();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to close Excel workbook",
                    e
            );
        }
    }
}