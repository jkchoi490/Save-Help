package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesHistory;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesHistoryRepository;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class DailyNecessitiesReportService {

    private final DailyNecessitiesRepository necessitiesRepository;
    private final DailyNecessitiesHistoryRepository historyRepository;

    public DailyNecessitiesReportService(DailyNecessitiesRepository necessitiesRepository,
                                         DailyNecessitiesHistoryRepository historyRepository) {
        this.necessitiesRepository = necessitiesRepository;
        this.historyRepository = historyRepository;
    }

    /*
    public ByteArrayInputStream generateReport(Long centerId, LocalDate startDate, LocalDate endDate) throws Exception {
        List<DailyNecessities> items;
        if (centerId == null) {
            items = necessitiesRepository.findAll();
        } else {
            //items = necessitiesRepository.findByProvidedBy_Id(centerId);
        }

        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : LocalDateTime.MIN;
        LocalDateTime end = endDate != null ? endDate.atTime(23, 59, 59) : LocalDateTime.now();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("DailyNecessitiesReport");

        // 헤더 생성
        Row header = sheet.createRow(0);
        String[] columns = {"품목명", "카테고리", "단위", "재고", "센터", "승인상태", "입출고 기록"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
        }

        int rowIdx = 1;
        for (DailyNecessities item : items) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(item.getName());
            row.createCell(1).setCellValue(item.getCategory().name());
            row.createCell(2).setCellValue(item.getUnit());
            row.createCell(3).setCellValue(item.getStock());
            row.createCell(4).setCellValue(item.getProvidedBy().getName());
            row.createCell(5).setCellValue(item.getApprovalStatus().name());

            // 입출고 기록
            List<DailyNecessitiesHistory> histories = historyRepository.findByItem_Id(item.getId()).stream()
                    .filter(h -> !h.getTimestamp().isBefore(start) && !h.getTimestamp().isAfter(end))
                    .toList();
            StringBuilder historyStr = new StringBuilder();
            for (DailyNecessitiesHistory h : histories) {
                historyStr.append(h.getType()).append("(").append(h.getQuantity()).append(") by ")
                        .append(h.getUser().getName()).append(" at ").append(h.getTimestamp()).append("; ");
            }
            row.createCell(6).setCellValue(historyStr.toString());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

     */
}
