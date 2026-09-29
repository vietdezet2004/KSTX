package com.safedriving.service;

import com.safedriving.dto.response.VehicleLogResponse;
import com.safedriving.entity.VehicleLog;
import com.safedriving.exception.ResourceNotFoundException;
import com.safedriving.repository.VehicleLogRepository;
import com.safedriving.service.impl.VehicleLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleLogServiceTest {

    @Mock
    private VehicleLogRepository vehicleLogRepository;

    @InjectMocks
    private VehicleLogServiceImpl vehicleLogService;

    private VehicleLog testLog;

    @BeforeEach
    void setUp() {
        testLog = VehicleLog.builder()
                .id(100L)
                .timeVehicleLog(LocalDateTime.now())
                .lat(new BigDecimal("21.028511"))
                .lng(new BigDecimal("105.854444"))
                .build();
    }

    @Test
    @DisplayName("getAllVehicleLogs - Lấy tất cả khi vehicleId null")
    void getAllVehicleLogs_All_Success() {
        when(vehicleLogRepository.findAllByOrderByTimeVehicleLogDesc()).thenReturn(List.of(testLog));

        List<VehicleLogResponse> results = vehicleLogService.getAllVehicleLogs(null);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(100L, results.get(0).getId());
    }

    @Test
    @DisplayName("getAllVehicleLogs - Lọc theo vehicleId khi truyền vehicleId")
    void getAllVehicleLogs_ByVehicleId_Success() {
        when(vehicleLogRepository.findByVehicleIdOrderByTimeVehicleLogDesc("veh-uuid-1")).thenReturn(List.of(testLog));

        List<VehicleLogResponse> results = vehicleLogService.getAllVehicleLogs("veh-uuid-1");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("getVehicleLogById - Thành công khi ID tồn tại")
    void getVehicleLogById_Success() {
        when(vehicleLogRepository.findById(100L)).thenReturn(Optional.of(testLog));

        VehicleLogResponse response = vehicleLogService.getVehicleLogById(100L);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(100L, response.getId());
    }

    @Test
    @DisplayName("getVehicleLogById - Thất bại ném ResourceNotFoundException khi không tìm thấy")
    void getVehicleLogById_NotFound() {
        when(vehicleLogRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleLogService.getVehicleLogById(999L));
    }

}
