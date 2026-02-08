package com.healthcare.system.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilitySlotDTO {

	private UUID slotId;
	private UUID doctorId;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private boolean available;
}
