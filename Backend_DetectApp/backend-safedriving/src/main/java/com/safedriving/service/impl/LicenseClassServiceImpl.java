package com.safedriving.service.impl;

import com.safedriving.dto.request.LicenseClassRequest;
import com.safedriving.dto.response.LicenseClassResponse;
import com.safedriving.entity.LicenseClass;
import com.safedriving.exception.BadRequestException;
import com.safedriving.exception.ResourceNotFoundException;
import com.safedriving.repository.LicenseClassRepository;
import com.safedriving.service.LicenseClassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseClassServiceImpl implements LicenseClassService {

    private final LicenseClassRepository licenseClassRepository;

    @Override
    @Transactional(readOnly = true)
    public List<LicenseClassResponse> getAllLicenseClasses() {
        log.info("Lấy danh sách tất cả hạng giấy phép lái xe");
        return licenseClassRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LicenseClassResponse getLicenseClassById(Short id) {
        log.info("Tra cứu hạng giấy phép lái xe với ID: {}", id);
        LicenseClass licenseClass = licenseClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hạng giấy phép lái xe với ID: " + id));
        return toResponse(licenseClass);
    }

    @Override
    @Transactional
    public LicenseClassResponse createLicenseClass(LicenseClassRequest request) {
        log.info("Tạo mới hạng giấy phép lái xe: {}", request.getCode());

        if (licenseClassRepository.existsById(request.getId())) {
            throw new BadRequestException("ID hạng bằng đã tồn tại: " + request.getId());
        }

        if (licenseClassRepository.existsByCode(request.getCode().trim().toUpperCase())) {
            throw new BadRequestException("Mã hạng bằng đã tồn tại: " + request.getCode().trim().toUpperCase());
        }

        LicenseClass licenseClass = LicenseClass.builder()
                .id(request.getId())
                .code(request.getCode().trim().toUpperCase())
                .name(request.getName().trim())
                .capacity(request.getCapacity())
                .build();

        LicenseClass saved = licenseClassRepository.save(licenseClass);
        log.info("Tạo hạng giấy phép lái xe thành công với ID: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public LicenseClassResponse updateLicenseClass(Short id, LicenseClassRequest request) {
        log.info("Cập nhật hạng giấy phép lái xe với ID: {}", id);
        LicenseClass licenseClass = licenseClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hạng giấy phép lái xe với ID: " + id));

        if (request.getCode() != null) {
            String newCode = request.getCode().trim().toUpperCase();
            if (!licenseClass.getCode().equals(newCode) && licenseClassRepository.existsByCode(newCode)) {
                throw new BadRequestException("Mã hạng bằng đã tồn tại: " + newCode);
            }
            licenseClass.setCode(newCode);
        }
        if (request.getName() != null) {
            licenseClass.setName(request.getName().trim());
        }
        if (request.getCapacity() != null) {
            licenseClass.setCapacity(request.getCapacity());
        }

        LicenseClass updated = licenseClassRepository.save(licenseClass);
        log.info("Cập nhật hạng giấy phép lái xe thành công cho ID: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteLicenseClass(Short id) {
        log.info("Xóa hạng giấy phép lái xe với ID: {}", id);
        LicenseClass licenseClass = licenseClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hạng giấy phép lái xe với ID: " + id));

        licenseClassRepository.delete(licenseClass);
        log.info("Đã xóa hạng giấy phép lái xe với ID: {}", id);
    }

    private LicenseClassResponse toResponse(LicenseClass licenseClass) {
        return LicenseClassResponse.builder()
                .id(licenseClass.getId())
                .code(licenseClass.getCode())
                .name(licenseClass.getName())
                .capacity(licenseClass.getCapacity())
                .build();
    }
}
