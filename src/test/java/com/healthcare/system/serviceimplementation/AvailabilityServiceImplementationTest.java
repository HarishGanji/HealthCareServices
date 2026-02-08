package com.healthcare.system.serviceimplementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.healthcare.system.dtos.AvailabilitySlotDTO;
import com.healthcare.system.models.AvailabilitySlot;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.repository.AvailabilitySlotRepository;
import com.healthcare.system.repository.DoctorRepository;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceImplementationTest {

	@Mock
	private AvailabilitySlotRepository slotRepository;

	@Mock
	private DoctorRepository doctorRepository;

	@InjectMocks
	private AvailabilityServiceImplementation availabilityService;

	@Test
	void createAvailabilitySlotCreatesSlot() {
		UUID doctorId = UUID.randomUUID();
		Doctor doctor = new Doctor();
		doctor.setDoctorId(doctorId);
		LocalDateTime startTime = LocalDateTime.of(2025, 5, 10, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2025, 5, 10, 10, 0);
		AvailabilitySlot slot = new AvailabilitySlot(UUID.randomUUID(), doctor, startTime, endTime, true);

		when(doctorRepository.getDoctorById(doctorId)).thenReturn(doctor);
		when(slotRepository.existsOverlappingSlot(eq(doctor), eq(startTime), eq(endTime))).thenReturn(false);
		when(slotRepository.save(org.mockito.ArgumentMatchers.any(AvailabilitySlot.class))).thenReturn(slot);

		AvailabilitySlotDTO result = availabilityService.createAvailabilitySlot(doctorId, startTime, endTime);

		assertThat(result.getDoctorId()).isEqualTo(doctorId);
		assertThat(result.isAvailable()).isTrue();
	}

	@Test
	void updateAvailabilityUpdatesSlot() {
		UUID slotId = UUID.randomUUID();
		Doctor doctor = new Doctor();
		doctor.setDoctorId(UUID.randomUUID());
		AvailabilitySlot slot = new AvailabilitySlot(slotId, doctor, LocalDateTime.now(),
				LocalDateTime.now().plusHours(1), true);

		when(slotRepository.findById(slotId)).thenReturn(Optional.of(slot));
		when(slotRepository.save(slot)).thenReturn(slot);

		AvailabilitySlotDTO result = availabilityService.updateAvailability(slotId, false);

		assertThat(result.isAvailable()).isFalse();
	}

	@Test
	void getAvailabilityByDoctorThrowsWhenMissing() {
		UUID doctorId = UUID.randomUUID();
		when(doctorRepository.getDoctorById(doctorId)).thenReturn(null);

		assertThatThrownBy(() -> availabilityService.getAvailabilityByDoctor(doctorId))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("Doctor not Found");
	}

	@Test
	void getAvailabilityByDoctorReturnsSlots() {
		UUID doctorId = UUID.randomUUID();
		Doctor doctor = new Doctor();
		doctor.setDoctorId(doctorId);
		AvailabilitySlot slot = new AvailabilitySlot(UUID.randomUUID(), doctor, LocalDateTime.now(),
				LocalDateTime.now().plusHours(1), true);

		when(doctorRepository.getDoctorById(doctorId)).thenReturn(doctor);
		when(slotRepository.findByDoctorDoctorId(doctorId)).thenReturn(List.of(slot));

		List<AvailabilitySlotDTO> result = availabilityService.getAvailabilityByDoctor(doctorId);

		assertThat(result).hasSize(1);
	}
}
