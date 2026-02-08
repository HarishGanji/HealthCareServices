package com.healthcare.system.serviceimplementation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.dtos.PrescriptionDTO;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.models.Patient;
import com.healthcare.system.models.Prescription;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.PatientRepository;
import com.healthcare.system.repository.PrescriptionRepository;
import com.healthcare.system.service.PrescriptionService;

@Service
public class PrescriptionServiceImplementation implements PrescriptionService {

	@Autowired
	private PrescriptionRepository prescriptionRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Override
	public PrescriptionDTO createPrescription(UUID doctorId, UUID patientId, PrescriptionDTO prescription) {
		Doctor doctor = doctorRepository.getDoctorById(doctorId);
		if (doctor == null) {
			throw new RuntimeException("Doctor not Found");
		}
		Patient patient = patientRepository.getPatientById(patientId);
		if (patient == null) {
			throw new RuntimeException("Patient Not Found");
		}

		Prescription entity = new Prescription();
		entity.setDoctor(doctor);
		entity.setPatient(patient);
		entity.setMedicationName(prescription.getMedicationName());
		entity.setDosage(prescription.getDosage());
		entity.setInstructions(prescription.getInstructions());
		entity.setIssuedAt(prescription.getIssuedAt());

		return toDto(prescriptionRepository.save(entity));
	}

	@Override
	public List<PrescriptionDTO> getPrescriptionsByPatient(UUID patientId) {
		Patient patient = patientRepository.getPatientById(patientId);
		if (patient == null) {
			throw new RuntimeException("Patient Not Found");
		}
		return prescriptionRepository.findByPatientPatientId(patientId).stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<PrescriptionDTO> getPrescriptionsByDoctor(UUID doctorId) {
		Doctor doctor = doctorRepository.getDoctorById(doctorId);
		if (doctor == null) {
			throw new RuntimeException("Doctor not Found");
		}
		return prescriptionRepository.findByDoctorDoctorId(doctorId).stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}

	private PrescriptionDTO toDto(Prescription prescription) {
		return new PrescriptionDTO(prescription.getPrescriptionId(), prescription.getDoctor().getDoctorId(),
				prescription.getPatient().getPatientId(), prescription.getMedicationName(), prescription.getDosage(),
				prescription.getInstructions(), prescription.getIssuedAt());
	}
}
