package com.tingeso.ms7_reports.services;

import com.tingeso.ms7_reports.dtos.ReportResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SheetService {

    public byte[] generateExcelReport(ReportResponse reportResponse) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte de ventas");
            createHeaderRow(sheet, reportResponse);
            createDataRows(sheet, reportResponse);
            createTotalRow(sheet, reportResponse);
            autoSizeColumns(sheet, reportResponse);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            return bos.toByteArray();
        }
    }

    private void createHeaderRow(Sheet sheet, ReportResponse report) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());

        // Header: Categoria o rango
        headerRow.createCell(0).setCellValue(
                report.isUsesCategory() ?
                        "Número de vueltas o tiempo máximo permitido" :
                        "Número de personas"
        );

        // Header: Meses
        List<YearMonth> months = getAllMonthsInRange(report.getStart(), report.getEnd());
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM-yyyy", new Locale("es", "CL"));

        for (int i = 0; i < months.size(); i++) {
            Cell cell = headerRow.createCell(i + 1);
            cell.setCellValue(months.get(i).format(monthFormatter));
            cell.setCellStyle(headerStyle);
        }

        // Header: Total
        Cell totalHeader = headerRow.createCell(months.size() + 1);
        totalHeader.setCellValue("Total");
        totalHeader.setCellStyle(headerStyle);
    }

    private void createDataRows(Sheet sheet, ReportResponse report) {
        Map<String, Map<YearMonth, Integer>> data = report.getEarningsByMonth();
        List<YearMonth> months = getAllMonthsInRange(report.getStart(), report.getEnd());
        AtomicInteger rowIndex = new AtomicInteger(1);

        // Crear filas
        //      -       | mes0 | mes1 | ...
        // -------------+------+------+----
        // | categoria0 |  $   |   $  |
        // | categoria1 |  $   |   $  |
        // | ...        |      |      |
        data.forEach((category,monthIncome) -> {
            Row row = sheet.createRow(rowIndex.getAndIncrement());
            row.createCell(0).setCellValue(category);

            // Computar valor total de cada categoria
            int categoryTotal = 0;
            for (int i = 0; i < months.size(); i++) {
                YearMonth month = months.get(i);
                int amount = monthIncome.getOrDefault(month, 0);
                row.createCell(i + 1).setCellValue(amount);
                categoryTotal += amount;
            }

            // Agregar total despues del ultimo mes
            row.createCell(months.size() + 1).setCellValue(categoryTotal);
        });
    }

    private void createTotalRow(Sheet sheet, ReportResponse report) {
        Map<String, Map<YearMonth, Integer>> data = report.getEarningsByMonth();
        List<YearMonth> months = getAllMonthsInRange(report.getStart(), report.getEnd());
        Row totalRow = sheet.createRow(sheet.getLastRowNum() + 1);
        CellStyle totalStyle = createTotalStyle(sheet.getWorkbook());

        // "Total" label
        Cell totalLabel = totalRow.createCell(0);
        totalLabel.setCellValue("Total");
        totalLabel.setCellStyle(totalStyle);

        // Computar total mensual
        int grandTotal = 0;
        for (int i = 0; i < months.size(); i++) {
            YearMonth month = months.get(i);
            int monthTotal = data.values().stream()
                    .mapToInt(monthData -> monthData.getOrDefault(month, 0))
                    .sum();

            Cell cell = totalRow.createCell(i + 1);
            cell.setCellValue(monthTotal);
            cell.setCellStyle(totalStyle);
            grandTotal += monthTotal;
        }

        // Grand total
        Cell grandTotalCell = totalRow.createCell(months.size() + 1);
        grandTotalCell.setCellValue(grandTotal);
        grandTotalCell.setCellStyle(totalStyle);
    }

    private void autoSizeColumns(Sheet sheet, ReportResponse report) {
        int columnCount = getAllMonthsInRange(report.getStart(), report.getEnd()).size() + 2;
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    private CellStyle createTotalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setBorderTop(BorderStyle.DOUBLE);
        return style;
    }

    private List<YearMonth> getAllMonthsInRange(YearMonth start, YearMonth end) {
        List<YearMonth> months = new ArrayList<>();
        for (YearMonth month = start; !month.isAfter(end); month = month.plusMonths(1)) {
            months.add(month);
        }
        return months;
    }
}
