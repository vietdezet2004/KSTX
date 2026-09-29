package com.safedriving.controller;

import com.safedriving.dto.common.ApiResponse;
import com.safedriving.dto.request.LicenseClassRequest;
import com.safedriving.dto.response.LicenseClassResponse;
import com.safedriving.service.LicenseClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/license-classes")
@RequiredArgsConstructor
@Tag(name = "License Classes", description = "Quản lý danh mục hạng giấy phép lái xe")
public class LicenseClassController {

    private final LicenseClassService licenseClassService;

    @GetMapping
    @Operation(summary = "Lấy danh sách hạng bằng lái", description = "Truy xuất danh sách tất cả các hạng bằng lái xe")
    public ResponseEntity<ApiResponse<List<LicenseClassResponse>>> getAllLicenseClasses() {
        List<LicenseClassResponse> licenseClasses = licenseClassService.getAllLicenseClasses();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách hạng bằng lái thành công", licenseClasses));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Tra cứu hạng bằng lái", description = "Lấy thông tin chi tiết của một hạng bằng lái theo ID")
    public ResponseEntity<ApiResponse<LicenseClassResponse>> getLicenseClassById(@PathVariable Short id) {
        LicenseClassResponse licenseClass = licenseClassService.getLicenseClassById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin hạng bằng lái thành công", licenseClass));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Tạo hạng bằng lái mới", description = "Thêm một hạng bằng lái mới vào hệ thống (ADMIN, MANAGER)")
    public ResponseEntity<ApiResponse<LicenseClassResponse>> createLicenseClass(
            @Valid @RequestBody LicenseClassRequest request) {
        LicenseClassResponse createdClass = licenseClassService.createLicenseClass(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo hạng bằng lái mới thành công", createdClass));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Cập nhật hạng bằng lái", description = "Sửa đổi thông tin hạng bằng lái hiện có (ADMIN, MANAGER)")
    public ResponseEntity<ApiResponse<LicenseClassResponse>> updateLicenseClass(
            @PathVariable Short id,
            @Valid @RequestBody LicenseClassRequest request) {
        LicenseClassResponse updatedClass = licenseClassService.updateLicenseClass(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật hạng bằng lái thành công", updatedClass));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Xóa hạng bằng lái", description = "Xóa một hạng bằng lái khỏi hệ thống (ADMIN, MANAGER)")
    public ResponseEntity<ApiResponse<Void>> deleteLicenseClass(@PathVariable Short id) {
        licenseClassService.deleteLicenseClass(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa hạng bằng lái thành công"));
    }
}
