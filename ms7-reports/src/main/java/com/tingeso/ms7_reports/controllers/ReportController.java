package com.tingeso.ms7_reports.controllers;

import com.tingeso.ms7_reports.services.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/generate")
    public ResponseEntity<?> generate(@RequestParam @DateTimeFormat(pattern = "MM-yyyy") YearMonth start,
                                  @RequestParam @DateTimeFormat(pattern = "MM-yyyy") YearMonth end) {
        return ResponseEntity.ok(reportService.generateReport(start, end, false));
    }
}
