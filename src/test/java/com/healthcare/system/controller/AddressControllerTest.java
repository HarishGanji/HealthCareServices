package com.healthcare.system.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Address;
import com.healthcare.system.service.AddressService;

@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
class AddressControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AddressService addressService;

	@Test
	void addAddressReturnsAddressDto() throws Exception {
		UUID entityId = UUID.randomUUID();
		Address request = new Address();
		request.setStreet("123 Main St");
		request.setCity("Austin");
		request.setState("TX");
		request.setZipCode("73301");
		request.setCountry("USA");
		AddressDTO response = new AddressDTO(UUID.randomUUID(), "123 Main St", "Austin", "TX", "73301", "USA");

		when(addressService.addAddress(eq(entityId), eq(request), eq("PATIENT"))).thenReturn(response);

		mockMvc.perform(post("/address/address/{entityId}", entityId)
						.param("role", "PATIENT")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.city").value("Austin"))
				.andExpect(jsonPath("$.state").value("TX"));
	}
}
