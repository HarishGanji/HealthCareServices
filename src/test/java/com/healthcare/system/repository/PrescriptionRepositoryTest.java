package com.healthcare.system.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.healthcare.system.enums.Role;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.models.Patient;
import com.healthcare.system.models.Prescription;
import com.healthcare.system.models.User;

@DataJpaTest
class PrescriptionRepositoryTest {

	@Autowired
	private PrescriptionRepository prescriptionRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void findByDoctorAndPatientReturnsPrescriptions() {
		User doctorUser = new User();
		doctorUser.setUsername("doctor");
		doctorUser.setEmail("doc@example.com");
		doctorUser.setPassword("password");
		doctorUser.setRole(Role.DOCTOR);

		User patientUser = new User();
		patientUser.setUsername("patient");
		patientUser.setEmail("patient@example.com");
		patientUser.setPassword("password");
		patientUser.setRole(Role.PATIENT);

		Doctor doctor = new Doctor();
		doctor.setDoctorName("Dr. Test");
		doctor.setEmail("doc@example.com");
		doctor.setContactNumber("555-0001");
		doctor.setUser(doctorUser);

		Patient patient = new Patient();
		patient.setPatientName("Patient One");
		patient.setEmail("patient@example.com");
		patient.setContactNumber("555-0002");
		patient.setUser(patientUser);

		entityManager.persist(doctorUser);
		entityManager.persist(patientUser);
		entityManager.persist(doctor);
		entityManager.persist(patient);

		Prescription prescription = new Prescription();
		prescription.setDoctor(doctor);
		prescription.setPatient(patient);
		prescription.setMedicationName("Ibuprofen");
		prescription.setDosage("200mg");
		prescription.setInstructions("Take twice daily");
		entityManager.persistAndFlush(prescription);

		List<Prescription> byDoctor = prescriptionRepository.findByDoctorDoctorId(doctor.getDoctorId());
		List<Prescription> byPatient = prescriptionRepository.findByPatientPatientId(patient.getPatientId());

		assertThat(byDoctor).hasSize(1);
		assertThat(byPatient).hasSize(1);
	}
}
