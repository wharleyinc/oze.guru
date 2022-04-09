package com.wharleyinc.guru.oze.service;

import com.wharleyinc.guru.oze.domain.Patient;
import com.wharleyinc.guru.oze.repository.PatientRepository;
import com.wharleyinc.guru.oze.util.CSVHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class CSVService {

    private final Logger log = LoggerFactory.getLogger(CSVService.class);

    private final PatientRepository patientRepository;

    public CSVService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public ByteArrayInputStream load(Long patientID) {
        log.debug("Request to load patient's profile to csv:  {}", patientID);
        Patient patient = patientRepository.findById(patientID).get();
        return CSVHelper.patientToCSV(patient);
    }
}
