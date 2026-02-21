package com.healthcare.system.serviceimplementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.healthcare.system.dtos.PrescriptionDTO;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.models.Patient;
import com.healthcare.system.models.Prescription;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.PatientRepository;
import com.healthcare.system.repository.PrescriptionRepository;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceImplementationTest {

	@Mock
	private PrescriptionRepository prescriptionRepository;

	@Mock
	private DoctorRepository doctorRepository;

	@Mock
	private PatientRepository patientRepository;

	@InjectMocks
	private PrescriptionServiceImplementation prescriptionService;

	@Test
	void createPrescriptionPersistsData() {
		UUID doctorId = UUID.randomUUID();
		UUID patientId = UUID.randomUUID();
		Doctor doctor = new Doctor();
		doctor.setDoctorId(doctorId);
		Patient patient = new Patient();
		patient.setPatientId(patientId);

		PrescriptionDTO request = new PrescriptionDTO(null, doctorId, patientId, "Ibuprofen", "200mg",
				"Take twice daily", null);
		Prescription saved = new Prescription();
		saved.setPrescriptionId(UUID.randomUUID());
		saved.setDoctor(doctor);
		saved.setPatient(patient);
		saved.setMedicationName("Ibuprofen");
		saved.setDosage("200mg");
		saved.setInstructions("Take twice daily");

		when(doctorRepository.getDoctorById(doctorId)).thenReturn(doctor);
		when(patientRepository.getPatientById(patientId)).thenReturn(patient);
		when(prescriptionRepository.save(any(Prescription.class))).thenReturn(saved);

		PrescriptionDTO result = prescriptionService.createPrescription(doctorId, patientId, request);

		assertThat(result.getDoctorId()).isEqualTo(doctorId);
		assertThat(result.getPatientId()).isEqualTo(patientId);
		assertThat(result.getMedicationName()).isEqualTo("Ibuprofen");
	}

	@Test
	void getPrescriptionsByPatientReturnsDtos() {
		UUID patientId = UUID.randomUUID();
		Patient patient = new Patient();
		patient.setPatientId(patientId);
		Doctor doctor = new Doctor();
		doctor.setDoctorId(UUID.randomUUID());
		Prescription prescription = new Prescription();
		prescription.setPrescriptionId(UUID.randomUUID());
		prescription.setPatient(patient);
		prescription.setDoctor(doctor);
		prescription.setMedicationName("Ibuprofen");

		when(patientRepository.getPatientById(patientId)).thenReturn(patient);
		when(prescriptionRepository.findByPatientPatientId(patientId)).thenReturn(List.of(prescription));

		List<PrescriptionDTO> result = prescriptionService.getPrescriptionsByPatient(patientId);

		assertThat(result).hasSize(1);
	}
}
