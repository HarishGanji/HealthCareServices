package com.healthcare.system;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.system.controller.PatientController;
import com.healthcare.system.models.Patient;
import com.healthcare.system.security.JWTutil;
import com.healthcare.system.service.PatientService;
import com.healthcare.system.serviceimplementation.UserDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(PatientController.class)
@AutoConfigureMockMvc(addFilters = false) // Automatically configures MockMvc for testing.
//addFilters = false → Disables Spring Security filters
//this is used in Spring Boot tests when working with MockMvc. It disables security filters like authentication and authorization, allowing you to test controller methods without login or security constraints.
class PatientControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PatientService patientServ;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private JWTutil jwtUtil;

	@MockitoBean
	private UserDetailsServiceImpl userDetailsService;

	UUID patientId = UUID.randomUUID();

	@Test
	void testCompleteProfile_Success() throws Exception {
		Patient patient = new Patient();
		patient.setPatientId(patientId);
		patient.setPatientName("John Doe");
		patient.setAge("30");
		patient.setEmail("johndoe@example.com");

		String json = objectMapper.writeValueAsString(patient);// converts the patient object into a JSON-formatte// string.
		when(patientServ.completeProfile(patient.getPatientId(), patient)).thenReturn(patient);
		mockMvc.perform(post("/patient/profile/{patientId}", patient.getPatientId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk())
	            .andExpect(jsonPath("$.patientId").value(patient.getPatientId().toString()))
				.andExpect(jsonPath("$.patientName").value("John Doe"))
				.andExpect(jsonPath("$.age").value("30"))
				.andExpect(jsonPath("$.email").value("johndoe@example.com"));
		verify(patientServ, times(1)).completeProfile(patient.getPatientId(), patient);
	}

	@Test
	void testCompleteProfile_Failure_MissingFields() throws Exception {
		Patient patient = new Patient();
		patient.setPatientName("John rey");
		patient.setAge("35");
		patient.setEmail(null);

		String json = objectMapper.writeValueAsString(patient);
		when(patientServ.completeProfile(patientId, patient)).thenReturn(patient);
		mockMvc.perform(post("/patient/profile/{patientId}", patientId).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.patientName").value(0))
	            .andExpect(jsonPath("$.age").value(10))
	            .andExpect(jsonPath("$.email").value(75));
		verify(patientServ, times(0)).completeProfile(patientId, patient);
	}
}
