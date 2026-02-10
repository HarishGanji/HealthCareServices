package com.healthcare.system.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.healthcare.system.dtos.AvailabilitySlotDTO;

/**
 * Service contract for managing doctor availability slots.
 */
public interface AvailabilityService {

	AvailabilitySlotDTO createAvailabilitySlot(UUID doctorId, LocalDateTime startTime, LocalDateTime endTime);

	List<AvailabilitySlotDTO> getAvailabilityByDoctor(UUID doctorId);

	AvailabilitySlotDTO updateAvailability(UUID slotId, boolean available);
}
