package com.wharleyinc.guru.oze.util;

import com.wharleyinc.guru.oze.domain.Patient;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class CSVHelper {
    public static ByteArrayInputStream patientToCSV(Patient patient) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            List<? extends Serializable> data = Arrays.asList(
                    String.valueOf(patient.getId()),
                    patient.getName(),
                    patient.getAge(),
                    patient.getLastVisitDate(),
                    patient.getUpdatedAt());
            csvPrinter.printRecord(data);
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
