package com.healthcare.system.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.healthcare.system.enums.Role;
import com.healthcare.system.models.AvailabilitySlot;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.models.User;

@DataJpaTest
class AvailabilitySlotRepositoryTest {

	@Autowired
	private AvailabilitySlotRepository slotRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void overlappingSlotsDetected() {
		User doctorUser = new User();
		doctorUser.setUsername("doctor");
		doctorUser.setEmail("doc@example.com");
		doctorUser.setPassword("password");
		doctorUser.setRole(Role.DOCTOR);

		Doctor doctor = new Doctor();
		doctor.setDoctorName("Dr. Test");
		doctor.setEmail("doc@example.com");
		doctor.setContactNumber("555-0001");
		doctor.setUser(doctorUser);

		entityManager.persist(doctorUser);
		entityManager.persist(doctor);

		LocalDateTime start = LocalDateTime.of(2025, 6, 1, 9, 0);
		LocalDateTime end = LocalDateTime.of(2025, 6, 1, 10, 0);
		AvailabilitySlot slot = new AvailabilitySlot();
		slot.setDoctor(doctor);
		slot.setStartTime(start);
		slot.setEndTime(end);
		slot.setAvailable(true);
		entityManager.persistAndFlush(slot);

		boolean overlaps = slotRepository.existsOverlappingSlot(doctor, start.plusMinutes(15), end.plusMinutes(15));
		List<AvailabilitySlot> slots = slotRepository.findByDoctorDoctorId(doctor.getDoctorId());

		assertThat(overlaps).isTrue();
		assertThat(slots).hasSize(1);
	}
}
