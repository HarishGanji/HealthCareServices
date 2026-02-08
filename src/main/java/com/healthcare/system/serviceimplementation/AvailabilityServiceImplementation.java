package com.healthcare.system.serviceimplementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.dtos.AvailabilitySlotDTO;
import com.healthcare.system.exception.BadRequestException;
import com.healthcare.system.exception.ConflictException;
import com.healthcare.system.exception.ResourceNotFoundException;
import com.healthcare.system.models.AvailabilitySlot;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.repository.AvailabilitySlotRepository;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.service.AvailabilityService;

/**
 * Availability service implementation for doctor scheduling.
 */
@Service
public class AvailabilityServiceImplementation implements AvailabilityService {

	@Autowired
	private AvailabilitySlotRepository slotRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Override
	public AvailabilitySlotDTO createAvailabilitySlot(UUID doctorId, LocalDateTime startTime, LocalDateTime endTime) {
		Doctor doctor = doctorRepository.getDoctorById(doctorId);
		if (doctor == null) {
			throw new ResourceNotFoundException("Doctor not found");
		}
		if (startTime == null || endTime == null || !endTime.isAfter(startTime)) {
			throw new BadRequestException("Invalid availability window");
		}
		boolean overlaps = slotRepository.existsOverlappingSlot(doctor, startTime, endTime);
		if (overlaps) {
			throw new ConflictException("Availability overlaps existing slot");
		}

		AvailabilitySlot slot = new AvailabilitySlot();
		slot.setDoctor(doctor);
		slot.setStartTime(startTime);
		slot.setEndTime(endTime);
		slot.setAvailable(true);

		return toDto(slotRepository.save(slot));
	}

	@Override
	public List<AvailabilitySlotDTO> getAvailabilityByDoctor(UUID doctorId) {
		Doctor doctor = doctorRepository.getDoctorById(doctorId);
		if (doctor == null) {
			throw new ResourceNotFoundException("Doctor not found");
		}
		return slotRepository.findByDoctorDoctorId(doctorId).stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public AvailabilitySlotDTO updateAvailability(UUID slotId, boolean available) {
		AvailabilitySlot slot = slotRepository.findById(slotId)
				.orElseThrow(() -> new ResourceNotFoundException("Availability slot not found"));
		slot.setAvailable(available);
		return toDto(slotRepository.save(slot));
	}

	private AvailabilitySlotDTO toDto(AvailabilitySlot slot) {
		return new AvailabilitySlotDTO(slot.getSlotId(), slot.getDoctor().getDoctorId(), slot.getStartTime(),
				slot.getEndTime(), slot.isAvailable());
	}
}
