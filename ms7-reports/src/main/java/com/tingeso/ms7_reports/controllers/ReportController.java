package com.tingeso.ms7_reports.controllers;

import com.tingeso.ms7_reports.dtos.ReportResponse;
import com.tingeso.ms7_reports.services.ReportService;
import com.tingeso.ms7_reports.services.SheetService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;
    private final SheetService sheetService;

    public ReportController(ReportService reportService, SheetService sheetService) {
        this.reportService = reportService;
        this.sheetService = sheetService;
    }

    @GetMapping("/generate-data")
    public ResponseEntity<?> generateData(
            @RequestParam @DateTimeFormat(pattern = "MM-yyyy") YearMonth start,
            @RequestParam @DateTimeFormat(pattern = "MM-yyyy") YearMonth end,
            @RequestParam boolean usingCategory) {
        return ResponseEntity.ok(reportService.generateReport(start, end, usingCategory));
    }

    @GetMapping(value = "/generate",
            produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<?> generate(
            @RequestParam @DateTimeFormat(pattern = "MM-yyyy") YearMonth start,
            @RequestParam @DateTimeFormat(pattern = "MM-yyyy") YearMonth end,
            @RequestParam boolean usingCategory) {

        try {
            ReportResponse reportResponse = reportService.generateReport(start, end, usingCategory);

            byte[] excelBytes = sheetService.generateExcelReport(reportResponse);

            String reportType = usingCategory ? "categoria_reserva" : "cantidad_personas";
            String filename = String.format("reporte-%s_%s-%s.xlsx",
                    reportType,
                    start.format(DateTimeFormatter.ofPattern("MMyyyy")),
                    end.format(DateTimeFormatter.ofPattern("MMyyyy")));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentLength(excelBytes.length)
                    .body(new ByteArrayResource(excelBytes));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }
}
