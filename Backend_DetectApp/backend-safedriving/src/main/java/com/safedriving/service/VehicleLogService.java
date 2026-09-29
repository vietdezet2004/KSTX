package com.safedriving.service;

import com.safedriving.dto.response.VehicleLogResponse;

import java.util.List;

public interface VehicleLogService {

    List<VehicleLogResponse> getAllVehicleLogs(String vehicleId);

    VehicleLogResponse getVehicleLogById(Long id);

}
