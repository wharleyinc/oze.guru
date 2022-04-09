package com.wharleyinc.guru.oze.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wharleyinc.guru.oze.domain.Staff;
import com.wharleyinc.guru.oze.service.CSVService;
import com.wharleyinc.guru.oze.service.PatientService;
import com.wharleyinc.guru.oze.service.StaffService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class StaffResourceTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    StaffService staffService;
    @MockBean
    PatientService patientService;
    @MockBean
    CSVService csvService;


    @Test
    public void createStaff_success() throws Exception {
        Staff staff = new Staff();
        staff.setName("Olawale");
        staff.setRegistrationDate(LocalDate.now().minusYears(1));

        Mockito.when(staffService.save(staff)).thenReturn(staff);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/staff")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(staff));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void updateStaff_error() throws Exception {
        Staff staff = new Staff();
        staff.setId(1l);
        staff.setName("Olawale");
        staff.setUuid(UUID.randomUUID());
        staff.setRegistrationDate(LocalDate.now().minusYears(1));

        Mockito.when(staffService.updateStaffProfile(staff)).thenReturn(java.util.Optional.of(staff));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/updateStaff/" + staff.getUuid())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(staff));

        mockMvc.perform(mockRequest)
                .andExpect(status().is4xxClientError());
    }
}
