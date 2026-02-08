package com.healthcare.system.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
	void bookAppointmentReturnsCreatedAppointment() throws Exception {
		UUID patientId = UUID.randomUUID();
		UUID doctorId = UUID.randomUUID();
		LocalDateTime appointmentTime = LocalDateTime.of(2025, 2, 1, 10, 0);
		AppointmentDTO response = new AppointmentDTO(UUID.randomUUID(), patientId, doctorId, Status.PENDING,
				appointmentTime, LocalDateTime.now(), LocalDateTime.now());

		when(appointmentService.bookAppointment(eq(patientId), eq(doctorId), eq(appointmentTime))).thenReturn(response);

		mockMvc.perform(post("/appointment/appointment/{patientId}/{doctorId}", patientId, doctorId)
						.param("appointmentDateTime", appointmentTime.toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.appointmentId").value(response.getAppointmentId().toString()))
				.andExpect(jsonPath("$.status").value(Status.PENDING.name()));
	}

	@Test
	void getAllAppointmentsReturnsList() throws Exception {
		AppointmentDTO appointment = new AppointmentDTO(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
				Status.PENDING, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
		when(appointmentService.getAllAppointments()).thenReturn(List.of(appointment));

		mockMvc.perform(get("/appointment"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].appointmentId").value(appointment.getAppointmentId().toString()));
	}

	@Test
	void getAppointmentByIdReturnsAppointment() throws Exception {
		UUID appointmentId = UUID.randomUUID();
		AppointmentDTO appointment = new AppointmentDTO(appointmentId, UUID.randomUUID(), UUID.randomUUID(),
				Status.PENDING, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
		when(appointmentService.getAppointmentById(appointmentId)).thenReturn(appointment);

		mockMvc.perform(get("/appointment/{appointmentId}", appointmentId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.appointmentId").value(appointmentId.toString()));
	}

	@Test
	void rescheduleAppointmentUpdatesTime() throws Exception {
		UUID appointmentId = UUID.randomUUID();
		LocalDateTime updatedTime = LocalDateTime.of(2025, 3, 1, 9, 30);
		AppointmentDTO appointment = new AppointmentDTO(appointmentId, UUID.randomUUID(), UUID.randomUUID(),
				Status.RESCHEDULED, updatedTime, LocalDateTime.now(), LocalDateTime.now());
		when(appointmentService.rescheduleAppointment(eq(appointmentId), eq(updatedTime))).thenReturn(appointment);

		mockMvc.perform(patch("/appointment/{appointmentId}/reschedule", appointmentId)
						.param("appointmentDateTime", updatedTime.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.appointmentDateTime").value(updatedTime.toString()))
				.andExpect(jsonPath("$.status").value(Status.RESCHEDULED.name()));
	}

	@Test
	void updateAppointmentStatusReturnsUpdatedStatus() throws Exception {
		UUID appointmentId = UUID.randomUUID();
		AppointmentDTO appointment = new AppointmentDTO(appointmentId, UUID.randomUUID(), UUID.randomUUID(),
				Status.ACCEPTED, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
		when(appointmentService.updateAppointmentStatus(eq(appointmentId), eq(Status.ACCEPTED))).thenReturn(appointment);

		mockMvc.perform(patch("/appointment/{appointmentId}/status", appointmentId)
						.param("status", Status.ACCEPTED.name()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value(Status.ACCEPTED.name()));
	}
}
