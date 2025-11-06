package com.example.atharv.Service;



import com.example.atharv.Entity.Event;
import com.example.atharv.Repository.EventRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@Service
public class ReportService {
    
    @Autowired
    private EventRepository eventRepository;
    
     // Generate CSV Report for Organizer Events
    public byte[] generateOrganizerEventsCSV(Long organizerId) throws IOException {
        List<Event> events = eventRepository.findByOrganizerId(organizerId);
        
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("Event ID", "Title", "Description", "Category", "Date", 
                           "Start Time", "End Time", "Location", 
                           "Max Volunteers", "Current Volunteers", "Status", "Organizer"));
        
        for (Event event : events) {
            csvPrinter.printRecord(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getCategory(),
                event.getEventDate() != null ? event.getEventDate().toString() : "",
                event.getStartTime() != null ? event.getStartTime().toString() : "",
                event.getEndTime() != null ? event.getEndTime().toString() : "",
                event.getLocation(),
                event.getMaxVolunteers(),
                event.getCurrentVolunteers(),
                event.getStatus(),
                event.getOrganizerName()
            );
        }
        
        csvPrinter.flush();
        return writer.toString().getBytes();
    }

    // Generate Excel Report for Organizer Events
    public byte[] generateOrganizerEventsExcel(Long organizerId) throws IOException {
        List<Event> events = eventRepository.findByOrganizerId(organizerId);
        
        try (Workbook workbook = new XSSFWorkbook(); 
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("My Events Report");
            // Create date format
            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
            
            // Create time format
            CellStyle timeStyle = workbook.createCellStyle();
            timeStyle.setDataFormat(createHelper.createDataFormat().getFormat("hh:mm AM/PM"));
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Event ID", "Title", "Description", "Category", "Date", 
                               "Start Time", "End Time", "Location", "Max Volunteers", 
                               "Current Volunteers", "Status", "Organizer"};
            
            // Style for header
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            int rowNum = 1;
            for (Event event : events) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(event.getId());
                row.createCell(1).setCellValue(event.getTitle());
                row.createCell(2).setCellValue(event.getDescription() != null ? event.getDescription() : "");
                row.createCell(3).setCellValue(event.getCategory() != null ? event.getCategory() : "");
                row.createCell(4).setCellValue(event.getEventDate() != null ? event.getEventDate().toString() : "");
                row.createCell(5).setCellValue(event.getStartTime() != null ? event.getStartTime().toString() : "");
                row.createCell(6).setCellValue(event.getEndTime() != null ? event.getEndTime().toString() : "");
                row.createCell(7).setCellValue(event.getLocation() != null ? event.getLocation() : "");
                row.createCell(8).setCellValue(event.getMaxVolunteers());
                row.createCell(9).setCellValue(event.getCurrentVolunteers());
                row.createCell(10).setCellValue(event.getStatus() != null ? event.getStatus() : "UPCOMING");
                row.createCell(11).setCellValue(event.getOrganizerName() != null ? event.getOrganizerName() : "");
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}