package com.healthcare.system.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.system.dtos.PrescriptionDTO;
import com.healthcare.system.service.PrescriptionService;

@WebMvcTest(PrescriptionController.class)
@AutoConfigureMockMvc(addFilters = false)
class PrescriptionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PrescriptionService prescriptionService;

	@Test
	void createPrescriptionReturnsDto() throws Exception {
		UUID doctorId = UUID.randomUUID();
		UUID patientId = UUID.randomUUID();
		PrescriptionDTO request = new PrescriptionDTO(null, doctorId, patientId, "Ibuprofen", "200mg",
				"Take twice daily", null);
		PrescriptionDTO response = new PrescriptionDTO(UUID.randomUUID(), doctorId, patientId, "Ibuprofen", "200mg",
				"Take twice daily", null);

		when(prescriptionService.createPrescription(eq(doctorId), eq(patientId), eq(request))).thenReturn(response);

		mockMvc.perform(post("/prescriptions/doctor/{doctorId}/patient/{patientId}", doctorId, patientId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.prescriptionId").value(response.getPrescriptionId().toString()))
				.andExpect(jsonPath("$.medicationName").value("Ibuprofen"));
	}

	@Test
	void getPrescriptionsByPatientReturnsList() throws Exception {
		UUID patientId = UUID.randomUUID();
		PrescriptionDTO response = new PrescriptionDTO(UUID.randomUUID(), UUID.randomUUID(), patientId,
				"Ibuprofen", "200mg", "Take twice daily", null);
		when(prescriptionService.getPrescriptionsByPatient(patientId)).thenReturn(List.of(response));

		mockMvc.perform(get("/prescriptions/patient/{patientId}", patientId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].patientId").value(patientId.toString()));
	}
}
