package com.tingeso.ms7_reports.services;

import com.tingeso.ms7_reports.dtos.ReportResponse;
import com.tingeso.ms7_reports.dtos.ReservationCategory;
import com.tingeso.ms7_reports.dtos.ReservationSummary;
import com.tingeso.ms7_reports.enums.ClientAmountRange;
import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.YearMonth;
import java.util.*;

@Service
@Getter
public class ReportService {
    private final RestTemplate restTemplate;

    public ReportService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ReservationSummary> getSummary(YearMonth start, YearMonth end) {
        // conectar a ms5
        String urlFormat = "http://ms5-reservations/api/reservations-receipts/receipt/get-between/?start=%tm-%d&end=%tm-%d";
        String url = String.format(urlFormat, start.getMonth(), start.getYear(), end.getMonth(), end.getYear());

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReservationSummary>>(){})
                .getBody();

    }

    public Map<String, ReservationCategory> getCategories() {
        return restTemplate.exchange(
                "http://ms1-reservation-categories/api/reservation-category/get",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, ReservationCategory>>(){}
                ).getBody();
    }

    private List<YearMonth> getAllMonthsInRange(YearMonth start, YearMonth end) {
        List<YearMonth> months = new ArrayList<>();
        for (YearMonth month = start;
             !month.isAfter(end);
             month = month.plusMonths(1)) {
            months.add(month);
        }
        return months;
    }

    public ReportResponse generateReport(YearMonth start, YearMonth end, boolean usingCategory) {
        List<ReservationSummary> reservations = getSummary(start, end);

        List<YearMonth> allMonths = getAllMonthsInRange(start, end);
        Map<String, ?> allCategories =
                usingCategory ? getCategories() :
                ClientAmountRange.clientAmountRangeMap();

        // zeroes[:] de matlab version meses lmao
        Map<String, Map<YearMonth, Integer>> reportData = new LinkedHashMap<>();
        allCategories.keySet().forEach(category -> {
            Map<YearMonth, Integer> monthData = new HashMap<>();
            // "columna x cada mes independientemente si tienen montos o no."
            allMonths.forEach(month -> monthData.put(month, 0));
            reportData.put(category, monthData);
        });

        // llenar con valores
        reservations.forEach(res -> {
            YearMonth month = YearMonth.from(res.getBookingDate());
            String category = usingCategory ? res.getCategory() :
                    ClientAmountRange.getClientAmountRange(res.getClientAmount()).toString();
            Integer amount = res.getCostTotal();

            // obtener total mensual
            reportData.get(category).merge(month, amount, Integer::sum);
        });

        // flat map goes here
        return new ReportResponse(start, end, usingCategory, reportData);
    }
}
