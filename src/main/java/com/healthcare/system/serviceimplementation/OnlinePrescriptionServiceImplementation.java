package com.healthcare.system.serviceimplementation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import com.healthcare.system.dtos.OnlinePrescriptionAnswerDTO;
import com.healthcare.system.dtos.OnlinePrescriptionRequestDTO;
import com.healthcare.system.enums.OnlinePrescriptionRequestStatus;
import com.healthcare.system.enums.Status;
import com.healthcare.system.exception.BadRequestException;
import com.healthcare.system.exception.ResourceNotFoundException;
import com.healthcare.system.models.Appointment;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.models.OnlinePrescriptionRequest;
import com.healthcare.system.models.Patient;
import com.healthcare.system.models.Prescription;
import com.healthcare.system.repository.AppointmentRepository;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.OnlinePrescriptionRequestRepository;
import com.healthcare.system.repository.PatientRepository;
import com.healthcare.system.repository.PrescriptionRepository;
import com.healthcare.system.service.OnlinePrescriptionService;

/**
 * Online prescription flow implementation for request, response, and PDF download.
 */
@Service
public class OnlinePrescriptionServiceImplementation implements OnlinePrescriptionService {

	private final AppointmentRepository appointmentRepository;
	private final PatientRepository patientRepository;
	private final DoctorRepository doctorRepository;
	private final OnlinePrescriptionRequestRepository requestRepository;
	private final PrescriptionRepository prescriptionRepository;

	public OnlinePrescriptionServiceImplementation(AppointmentRepository appointmentRepository,
			PatientRepository patientRepository,
			DoctorRepository doctorRepository,
			OnlinePrescriptionRequestRepository requestRepository,
			PrescriptionRepository prescriptionRepository) {
		this.appointmentRepository = appointmentRepository;
		this.patientRepository = patientRepository;
		this.doctorRepository = doctorRepository;
		this.requestRepository = requestRepository;
		this.prescriptionRepository = prescriptionRepository;
	}

	@Override
	public OnlinePrescriptionRequestDTO createRequest(UUID appointmentId, UUID patientId, String questions) {
		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
		Patient patient = patientRepository.getPatientById(patientId);
		if (patient == null) {
			throw new ResourceNotFoundException("Patient not found");
		}
		if (!appointment.getPatient().getPatientId().equals(patientId)) {
			throw new BadRequestException("Appointment does not belong to patient");
		}
		if (appointment.getStatus() != Status.COMPLETED) {
			throw new BadRequestException("Online prescription request is allowed only after completed appointment");
		}

		OnlinePrescriptionRequest request = new OnlinePrescriptionRequest();
		request.setAppointment(appointment);
		request.setPatient(patient);
		request.setDoctor(appointment.getDoctor());
		request.setPatientQuestions(questions);
		request.setStatus(OnlinePrescriptionRequestStatus.REQUESTED);
		request.setRequestedAt(LocalDateTime.now());

		return toDto(requestRepository.save(request));
	}

	@Override
	public OnlinePrescriptionRequestDTO answerRequest(UUID requestId, UUID doctorId, OnlinePrescriptionAnswerDTO answerDTO) {
		OnlinePrescriptionRequest request = requestRepository.findById(requestId)
				.orElseThrow(() -> new ResourceNotFoundException("Online prescription request not found"));
		Doctor doctor = doctorRepository.getDoctorById(doctorId);
		if (doctor == null) {
			throw new ResourceNotFoundException("Doctor not found");
		}
		if (!request.getDoctor().getDoctorId().equals(doctorId)) {
			throw new BadRequestException("Request does not belong to this doctor");
		}
		if (request.getStatus() != OnlinePrescriptionRequestStatus.REQUESTED) {
			throw new BadRequestException("Request has already been processed");
		}

		Prescription prescription = new Prescription();
		prescription.setDoctor(doctor);
		prescription.setPatient(request.getPatient());
		prescription.setMedicationName(answerDTO.getMedicationName());
		prescription.setDosage(answerDTO.getDosage());
		prescription.setInstructions(answerDTO.getInstructions());
		prescription.setIssuedAt(LocalDateTime.now());
		Prescription savedPrescription = prescriptionRepository.save(prescription);

		request.setDoctorResponse(answerDTO.getDoctorResponse());
		request.setStatus(OnlinePrescriptionRequestStatus.ANSWERED);
		request.setRespondedAt(LocalDateTime.now());
		request.setPrescription(savedPrescription);

		return toDto(requestRepository.save(request));
	}

	@Override
	public List<OnlinePrescriptionRequestDTO> getDoctorRequests(UUID doctorId) {
		return requestRepository.findByDoctorDoctorId(doctorId).stream().map(this::toDto).collect(Collectors.toList());
	}

	@Override
	public List<OnlinePrescriptionRequestDTO> getPatientRequests(UUID patientId) {
		return requestRepository.findByPatientPatientId(patientId).stream().map(this::toDto).collect(Collectors.toList());
	}

	@Override
	public byte[] downloadPrescriptionPdf(UUID requestId, UUID patientId) {
		OnlinePrescriptionRequest request = requestRepository.findById(requestId)
				.orElseThrow(() -> new ResourceNotFoundException("Online prescription request not found"));
		if (!request.getPatient().getPatientId().equals(patientId)) {
			throw new BadRequestException("Request does not belong to patient");
		}
		if (request.getPrescription() == null) {
			throw new BadRequestException("Prescription is not generated yet");
		}
		return generatePrescriptionPdf(request);
	}

	private byte[] generatePrescriptionPdf(OnlinePrescriptionRequest request) {
		try (PDDocument document = new PDDocument(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			PDPage page = new PDPage();
			document.addPage(page);
			Prescription prescription = request.getPrescription();

			try (PDPageContentStream content = new PDPageContentStream(document, page)) {
				content.beginText();
				content.setFont(PDType1Font.HELVETICA_BOLD, 14);
				content.newLineAtOffset(50, 750);
				content.showText("Online Prescription");
				content.setFont(PDType1Font.HELVETICA, 11);
				content.newLineAtOffset(0, -30);
				content.showText("Doctor: " + request.getDoctor().getDoctorName());
				content.newLineAtOffset(0, -20);
				content.showText("Patient: " + request.getPatient().getPatientName());
				content.newLineAtOffset(0, -20);
				content.showText("Medication: " + prescription.getMedicationName());
				content.newLineAtOffset(0, -20);
				content.showText("Dosage: " + prescription.getDosage());
				content.newLineAtOffset(0, -20);
				content.showText("Instructions: " + prescription.getInstructions());
				content.newLineAtOffset(0, -20);
				content.showText("Doctor Response: " + safe(request.getDoctorResponse()));
				content.newLineAtOffset(0, -20);
				content.showText("Issued At: " + prescription.getIssuedAt());
				content.endText();
			}

			document.save(outputStream);
			return outputStream.toByteArray();
		} catch (IOException e) {
			throw new BadRequestException("Unable to generate prescription PDF");
		}
	}

	private String safe(String value) {
		return value == null ? "" : value;
	}

	private OnlinePrescriptionRequestDTO toDto(OnlinePrescriptionRequest request) {
		return new OnlinePrescriptionRequestDTO(
				request.getRequestId(),
				request.getPatient().getPatientId(),
				request.getDoctor().getDoctorId(),
				request.getAppointment().getAppointmentId(),
				request.getPatientQuestions(),
				request.getDoctorResponse(),
				request.getStatus(),
				request.getRequestedAt(),
				request.getRespondedAt(),
				request.getPrescription() == null ? null : request.getPrescription().getPrescriptionId());
	}
}
