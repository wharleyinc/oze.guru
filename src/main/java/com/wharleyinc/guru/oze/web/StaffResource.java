package com.wharleyinc.guru.oze.web;

import com.wharleyinc.guru.oze.domain.Staff;
import com.wharleyinc.guru.oze.service.CSVService;
import com.wharleyinc.guru.oze.service.PatientService;
import com.wharleyinc.guru.oze.service.StaffService;
import com.wharleyinc.guru.oze.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing staff.
*/
@RestController
@RequestMapping("/api")
public class StaffResource {

    private final Logger log = LoggerFactory.getLogger(StaffResource.class);

    private final StaffService staffService;

    private final PatientService patientService;

    private final CSVService csvService;


    public StaffResource(StaffService staffService, PatientService patientService, CSVService csvService) {
        this.staffService = staffService;
        this.patientService = patientService;
        this.csvService = csvService;
    }

    @PostMapping("/data")
    public ResponseEntity<Void> initData() {
        log.debug("REST request to load initial data");
        staffService.generateInitialData();
        return ResponseEntity.noContent().build();
    }

    /**
     * {@code POST  /staff} : Create a new staff.
     *
     * @param staff the staff to create.
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with body the new staff, or with status {@code 400 (Bad Request)} if the status has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/staff")
    public ResponseEntity<Staff> createStaffProfile(@Valid @RequestBody Staff staff) throws URISyntaxException {
        log.debug("REST request to save StaffResponse : {}", staff);
        if (staff.getId() != null) {
            throw new URISyntaxException(HttpStatus.BAD_REQUEST.toString(), "A new StaffResponse cannot already have an ID");
        }
        return ResponseEntity.ok().body(staffService.save(staff));
    }

    /**
     * {@code PATCH /staff/update} : Updates an existing StaffResponse.
     *
     * @param staff the staff object to update.
     * @param id the staffID to validate.
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)} and with empty body.
     */
    @PatchMapping(value = "/staff/update/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity updateStaffProfile(@PathVariable(value = "id", required = true) final String id, @NotNull @RequestBody Staff staff) {
        log.debug("REST request to update Staff profile: {}", staff);

        Optional<Staff> validatedStaff =  staffService.findStaffProfileByUuid(UUID.fromString(id));
        if (validatedStaff.isEmpty()) {
            return ResponseUtil.wrapOrNotFound(validatedStaff);
        }

        return ResponseUtil.wrapOrNotFound(staffService.updateStaffProfile(staff));

    }
}

