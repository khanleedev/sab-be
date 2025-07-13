package org.project.social_account_business.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.project.social_account_business.exception.BadRequestException;
import org.project.social_account_business.form.UploadItemProductForm;
import org.project.social_account_business.form.ticket_product_info.UploadTicketProductInfoForm;
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
    private static final String[] ITEMS = {"Name", "Description", "Price", "Max Item / Account"};
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
//                && row.getCell(3).getStringCellValue().equals(ITEMS[3])
                && row.getCell(3).getStringCellValue().equals(ITEMS[3]);
    }

    private String getCellString(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return "";
    }

    private BigDecimal getCellBigDecimal(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return new BigDecimal(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                throw new BadRequestException("Invalid decimal format: " + cell.getStringCellValue());
            }
        }
        return BigDecimal.ZERO;
    }

    private int getCellInt(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Integer.parseInt(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                throw new BadRequestException("Invalid integer format: " + cell.getStringCellValue());
            }
        }
        return 0;
    }


    private Map<Integer, Consumer<Cell>> createItemCellMapping(UploadItemProductForm form) {
        Map<Integer, Consumer<Cell>> map = new HashMap<>();

        map.put(0, cell -> form.setName(getCellString(cell)));
        map.put(1, cell -> form.setDescription(getCellString(cell)));
        map.put(2, cell -> form.setPrice(getCellBigDecimal(cell)));
        map.put(3, cell -> form.setMaxPurchasePerAccount(getCellInt(cell)));

        return map;
    }

    private UploadItemProductForm createItemUpload(Row row) throws Exception {
        UploadItemProductForm form = new UploadItemProductForm();
        try {
            Map<Integer, Consumer<Cell>> cellMapping = createItemCellMapping(form);

            for (Map.Entry<Integer, Consumer<Cell>> entry : cellMapping.entrySet()) {
                Cell cell = row.getCell(entry.getKey());
                if (cell != null) {
                    entry.getValue().accept(cell);
                }
            }
        } catch (Exception e) {
            throw new Exception("Failed to parse Excel file.\nError: " + e.getMessage() + "\nAt row num: " + row.getRowNum());
        }
        return form;
    }

    public List<UploadItemProductForm> mapExcelToData(InputStream inputStream) throws Exception {
        List<UploadItemProductForm> list = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            for (Sheet sheet : workbook) {
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        if (!validateHeaders(row)) {
                            throw new BadRequestException("Invalid Excel column format");
                        }
                        continue;
                    }
                    list.add(createItemUpload(row));
                }
            }
        } catch (IOException ex) {
            throw new BadRequestException("Failed to parse Excel file.\nError: " + ex.getMessage());
        }
        return list;
    }
    public List<UploadTicketProductInfoForm> mapExcelToTicketProductInfos(InputStream inputStream) throws Exception {
        List<UploadTicketProductInfoForm> infoList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            for (Sheet sheet : workbook) {
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        // Validate headers
                        if (!row.getCell(0).getStringCellValue().equalsIgnoreCase("UID") ||
                                !row.getCell(1).getStringCellValue().equalsIgnoreCase("PASS") ||
                                !row.getCell(2).getStringCellValue().equalsIgnoreCase("2FA") ||
                                !row.getCell(3).getStringCellValue().equalsIgnoreCase("MAIL") ||
                                !row.getCell(4).getStringCellValue().equalsIgnoreCase("PASS MAIL") ||
                                !row.getCell(5).getStringCellValue().equalsIgnoreCase("MAIL VERY")) {
                            throw new BadRequestException("Excel format must be: UID | PASS | 2FA | MAIL | PASS MAIL | MAIL VERY");
                        }
                        continue;
                    }

                    UploadTicketProductInfoForm info = new UploadTicketProductInfoForm();
                    info.setUid(getCellValue(row.getCell(0)));
                    info.setPass(getCellValue(row.getCell(1)));
                    info.setTwoFA(getCellValue(row.getCell(2)));
                    info.setMail(getCellValue(row.getCell(3)));
                    info.setPassMail(getCellValue(row.getCell(4)));
                    info.setMailVerify(getCellValue(row.getCell(5)));

                    infoList.add(info);
                }
            }
        } catch (IOException e) {
            throw new BadRequestException("Error reading Excel file: " + e.getMessage());
        }
        return infoList;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((long) cell.getNumericCellValue());
        return cell.toString();
    }

}
