package com.safedriving.service;

import com.safedriving.dto.request.LicenseClassRequest;
import com.safedriving.dto.response.LicenseClassResponse;

import java.util.List;

public interface LicenseClassService {
    List<LicenseClassResponse> getAllLicenseClasses();

    LicenseClassResponse getLicenseClassById(Short id);

    LicenseClassResponse createLicenseClass(LicenseClassRequest request);

    LicenseClassResponse updateLicenseClass(Short id, LicenseClassRequest request);

    void deleteLicenseClass(Short id);
}
