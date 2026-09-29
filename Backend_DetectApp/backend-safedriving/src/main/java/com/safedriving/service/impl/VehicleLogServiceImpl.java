package com.safedriving.service.impl;

import com.safedriving.dto.response.VehicleLogResponse;
import com.safedriving.entity.VehicleLog;
import com.safedriving.exception.ResourceNotFoundException;
import com.safedriving.repository.VehicleLogRepository;
import com.safedriving.service.VehicleLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleLogServiceImpl implements VehicleLogService {

    private final VehicleLogRepository vehicleLogRepository;

    @Override
    @Transactional(readOnly = true)
    public List<VehicleLogResponse> getAllVehicleLogs(String vehicleId) {
        if (vehicleId != null && !vehicleId.isBlank()) {
            log.info("Lấy danh sách nhật ký hành trình cho phương tiện ID: {}", vehicleId);
            return vehicleLogRepository.findByVehicleIdOrderByTimeVehicleLogDesc(vehicleId.trim()).stream()
                    .map(this::toResponse)
                    .toList();
        }
        log.info("Lấy tất cả nhật ký hành trình");
        return vehicleLogRepository.findAllByOrderByTimeVehicleLogDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleLogResponse getVehicleLogById(Long id) {
        log.info("Tra cứu nhật ký phương tiện với ID: {}", id);
        VehicleLog logEntry = findVehicleLogOrThrow(id);
        return toResponse(logEntry);
    }

    private VehicleLog findVehicleLogOrThrow(Long id) {
        return vehicleLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhật ký phương tiện với ID: " + id));
    }

    private VehicleLogResponse toResponse(VehicleLog vehicleLog) {
        return VehicleLogResponse.builder()
                .id(vehicleLog.getId())
                .vehicleId(vehicleLog.getVehicle() != null ? vehicleLog.getVehicle().getId() : null)
                .plateNumber(vehicleLog.getVehicle() != null ? vehicleLog.getVehicle().getPlateNumber() : null)
                .timeVehicleLog(vehicleLog.getTimeVehicleLog())
                .lat(vehicleLog.getLat())
                .lng(vehicleLog.getLng())
                .build();
    }
}
