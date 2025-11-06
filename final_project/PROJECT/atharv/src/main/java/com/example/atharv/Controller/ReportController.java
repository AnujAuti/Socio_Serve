package com.example.atharv.Controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.example.atharv.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    // Download Organizer Events CSV Report
    @GetMapping("/organizer/{organizerId}/events/csv")
    public ResponseEntity<byte[]> downloadOrganizerEventsCSV(@PathVariable Long organizerId) {
        try {
            byte[] csvBytes = reportService.generateOrganizerEventsCSV(organizerId);
            
            String filename = "my-events-report.csv";
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvBytes);
                    
        } catch (IOException e) {
            System.out.println("Error generating CSV report: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Download Organizer Events Excel Report
    @GetMapping("/organizer/{organizerId}/events/excel")
    public ResponseEntity<byte[]> downloadOrganizerEventsExcel(@PathVariable Long organizerId) {
        try {
            byte[] excelBytes = reportService.generateOrganizerEventsExcel(organizerId);
            
            String filename = "my-events-report.xlsx";
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(excelBytes);
                    
        } catch (IOException e) {
            System.out.println("Error generating Excel report: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
