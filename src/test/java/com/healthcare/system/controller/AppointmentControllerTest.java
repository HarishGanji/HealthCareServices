package com.healthcare.system.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.healthcare.system.dtos.AppointmentDTO;
import com.healthcare.system.enums.Status;
import com.healthcare.system.service.AppointmentService;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AppointmentService appointmentService;

	@Test
	void bookAppointment_returnsCreatedAppointment() throws Exception {
		UUID patientId = UUID.randomUUID();
		UUID doctorId = UUID.randomUUID();
		LocalDateTime appointmentTime = LocalDateTime.of(2025, 1, 1, 10, 0);
		AppointmentDTO response = buildAppointmentDTO(patientId, doctorId, appointmentTime, Status.PENDING);

		when(appointmentService.bookAppointment(patientId, doctorId, appointmentTime)).thenReturn(response);

		mockMvc.perform(post("/appointment/appointment/{patientId}/{doctorId}", patientId, doctorId)
				.param("appointmentDateTime", "2025-01-01T10:00:00")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.patient_Id").value(patientId.toString()))
				.andExpect(jsonPath("$.doctor_Id").value(doctorId.toString()))
				.andExpect(jsonPath("$.status").value("PENDING"));

		verify(appointmentService).bookAppointment(patientId, doctorId, appointmentTime);
	}

	@Test
	void getAllAppointments_returnsList() throws Exception {
		UUID patientId = UUID.randomUUID();
		UUID doctorId = UUID.randomUUID();
		AppointmentDTO response = buildAppointmentDTO(patientId, doctorId, LocalDateTime.of(2025, 1, 2, 10, 0),
				Status.PENDING);

		when(appointmentService.getAllAppointments()).thenReturn(List.of(response));

		mockMvc.perform(get("/appointment"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].patient_Id").value(patientId.toString()))
				.andExpect(jsonPath("$[0].doctor_Id").value(doctorId.toString()));

		verify(appointmentService).getAllAppointments();
	}

	@Test
	void updateAppointmentStatus_updatesStatus() throws Exception {
		UUID appointmentId = UUID.randomUUID();
		UUID patientId = UUID.randomUUID();
		UUID doctorId = UUID.randomUUID();
		AppointmentDTO response = buildAppointmentDTO(patientId, doctorId, LocalDateTime.of(2025, 1, 3, 10, 0),
				Status.ACCEPTED);
		response.setAppointmentId(appointmentId);

		when(appointmentService.updateAppointmentStatus(appointmentId, Status.ACCEPTED)).thenReturn(response);

		mockMvc.perform(patch("/appointment/{appointmentId}/status", appointmentId)
				.param("status", "ACCEPTED"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.appointmentId").value(appointmentId.toString()))
				.andExpect(jsonPath("$.status").value("ACCEPTED"));

		verify(appointmentService).updateAppointmentStatus(appointmentId, Status.ACCEPTED);
	}

	private AppointmentDTO buildAppointmentDTO(UUID patientId, UUID doctorId, LocalDateTime appointmentTime,
			Status status) {
		AppointmentDTO dto = new AppointmentDTO();
		dto.setAppointmentId(UUID.randomUUID());
		dto.setPatient_Id(patientId);
		dto.setDoctor_Id(doctorId);
		dto.setAppointmentDateTime(appointmentTime);
		dto.setStatus(status);
		return dto;
	}
}
