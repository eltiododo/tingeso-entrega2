package com.tingeso.ms7_reports.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    YearMonth start;
    YearMonth end;
    boolean usesCategory;
    Map<String, Map<YearMonth, Integer>> earningsByMonth;
}
