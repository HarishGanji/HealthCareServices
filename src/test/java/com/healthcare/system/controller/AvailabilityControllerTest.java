package com.healthcare.system.controller;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.healthcare.system.dtos.AvailabilitySlotDTO;
import com.healthcare.system.service.AvailabilityService;

@WebMvcTest(AvailabilityController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(authorities = "ADMIN")
class AvailabilityControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AvailabilityService availabilityService;

	@Test
	void createAvailabilityReturnsSlot() throws Exception {
		UUID doctorId = UUID.randomUUID();
		LocalDateTime startTime = LocalDateTime.of(2025, 5, 1, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2025, 5, 1, 10, 0);
		AvailabilitySlotDTO slot = new AvailabilitySlotDTO(UUID.randomUUID(), doctorId, startTime, endTime, true);

		when(availabilityService.createAvailabilitySlot(eq(doctorId), eq(startTime), eq(endTime))).thenReturn(slot);

		mockMvc.perform(post("/availability/doctor/{doctorId}", doctorId)
						.param("startTime", startTime.toString())
						.param("endTime", endTime.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.slotId").value(slot.getSlotId().toString()))
				.andExpect(jsonPath("$.available").value(true));
	}

	@Test
	void getAvailabilityReturnsList() throws Exception {
		UUID doctorId = UUID.randomUUID();
		AvailabilitySlotDTO slot = new AvailabilitySlotDTO(UUID.randomUUID(), doctorId, LocalDateTime.now(),
				LocalDateTime.now().plusHours(1), true);
		when(availabilityService.getAvailabilityByDoctor(doctorId)).thenReturn(List.of(slot));

		mockMvc.perform(get("/availability/doctor/{doctorId}", doctorId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].doctorId").value(doctorId.toString()));
	}

	@Test
	void updateAvailabilityReturnsUpdatedSlot() throws Exception {
		UUID slotId = UUID.randomUUID();
		AvailabilitySlotDTO slot = new AvailabilitySlotDTO(slotId, UUID.randomUUID(), LocalDateTime.now(),
				LocalDateTime.now().plusHours(1), false);
		when(availabilityService.updateAvailability(eq(slotId), eq(false))).thenReturn(slot);

		mockMvc.perform(patch("/availability/{slotId}", slotId)
						.param("available", "false"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.available").value(false));
	}
}
