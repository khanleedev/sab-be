package org.project.social_account_business.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.project.social_account_business.exception.BadRequestException;
import org.project.social_account_business.form.UploadItemProductForm;
import org.project.social_account_business.mapper.TicketProductMapper;
import org.project.social_account_business.service.ticket.TicketService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
@Slf4j
public class ExcelService {
    private static final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String[] ITEMS = {"Name", "Description", "Price", "Quantity", "Max Item / Account"};
    private final TicketService ticketService;
    private final TicketProductMapper ticketProductMapper;

    public ExcelService(TicketService ticketService, TicketProductMapper ticketProductMapper) {
        this.ticketService = ticketService;
        this.ticketProductMapper = ticketProductMapper;
    }

    public boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    private boolean validateHeaders(Row row) {
        return row.getCell(0).getStringCellValue().equals(ITEMS[0])
                && row.getCell(1).getStringCellValue().equals(ITEMS[1])
                && row.getCell(2).getStringCellValue().equals(ITEMS[2])
                && row.getCell(3).getStringCellValue().equals(ITEMS[3])
                && row.getCell(4).getStringCellValue().equals(ITEMS[4]);
    }

    private Map<Integer, Consumer<Cell>> createItemCellMapping(UploadItemProductForm uploadItemProductForm) {
        Map<Integer, Consumer<Cell>> cellMapping = new HashMap<>();
        cellMapping.put(0, cell -> uploadItemProductForm.setName(cell.getStringCellValue()));
        cellMapping.put(1, cell -> uploadItemProductForm.setDescription(cell.getStringCellValue()));
        cellMapping.put(2, cell -> uploadItemProductForm.setPrice(BigDecimal.valueOf(cell.getNumericCellValue())));
        cellMapping.put(3, cell -> uploadItemProductForm.setQuantity((int) cell.getNumericCellValue()));
        cellMapping.put(4, cell -> uploadItemProductForm.setMaxPurchasePerAccount((int) cell.getNumericCellValue()));
        return cellMapping;
    }

    private UploadItemProductForm createItemUpload(Row row) throws Exception {
        UploadItemProductForm testCaseUpload = new UploadItemProductForm();
        try{
            Map<Integer, Consumer<Cell>> cellMapping = createItemCellMapping(testCaseUpload);

            for (Map.Entry<Integer, Consumer<Cell>> entry : cellMapping.entrySet()) {
                Cell cell = row.getCell(entry.getKey());
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    entry.getValue().accept(cell);
                }
            }
        }catch (Exception e){
            throw new Exception("Failed to parse Excel file.\nError: " + e.getMessage() + "\nAt row num: " + row.getRowNum());
        }
        return testCaseUpload;
    }

    public List<UploadItemProductForm> mapExcelToData(InputStream inputStream) throws Exception {
        List<UploadItemProductForm> uploadItemProductForms = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            for (Sheet sheet : workbook) {
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        if (!validateHeaders(row)) {
                            throw new BadRequestException("Invalid Excel column format");
                        }
                        continue;
                    }

                    UploadItemProductForm uploadItemProductForm = createItemUpload(row);
                    uploadItemProductForms.add(uploadItemProductForm);
                }
            }
        } catch (IOException ex) {
            throw new BadRequestException("Failed to parse Excel file.\nError: " + ex.getMessage());
        }
        return uploadItemProductForms;
    }

}
