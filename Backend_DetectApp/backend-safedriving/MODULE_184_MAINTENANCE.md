# HƯỚNG DẪN BẢO TRÌ VÀ PHÁT TRIỂN TIẾP CHO MÃ NHIỆM VỤ 184
> **Dành cho AI Agents và Developers:** File này là tài liệu theo dõi tiến độ (Task Tracker) và tiêu chuẩn kiến trúc (Architecture Blueprint) để duy trì tính nhất quán khi bảo trì hoặc mở rộng hệ thống mà không làm đứt gãy context.

---

## 1. TỔNG QUAN NHIỆM VỤ (MÃ 184)
* **Nguồn đặc tả gốc:** `DANH_SACH_CHUC_NANG_API (1).md`
* **Quy mô phân công:** Gồm **7 module** (tổng cộng 35 REST APIs).
* **Tiến độ hiện tại:** **7/7 module** hoàn thành xuất sắc (35/35 APIs, 100% unit tests passing).

### Bảng theo dõi tiến độ:
| STT | Module | Endpoint | Số lượng API | Trạng thái | Unit Test |
| :---: | :--- | :--- | :---: | :---: | :---: |
| 1 | **Addresses** (Module 4) | `/addresses` | 5 | ✅ ĐÃ XONG (5/5) | ✅ 12 tests |
| 2 | **Vehicle Logs** (Module 15) | `/vehicle-logs` | 5 | ✅ ĐÃ XONG (5/5) | ✅ 13 tests |
| 3 | **Stops** (Module 16) | `/stops` | 5 | ✅ ĐÃ XONG (5/5) | ✅ 13 tests |
| 4 | **Routes** (Module 17) | `/routes` | 5 | ✅ ĐÃ XONG (5/5) | ✅ 13 tests |
| 5 | **Route Stops** (Module 18) | `/route-stops` | 5 | ✅ ĐÃ XONG (5/5) | ✅ 11 tests |
| 6 | **Trip Progress** (Module 20) | `/trip-progress` | 5 | ✅ ĐÃ XONG (5/5) | ✅ 10 tests |
| 7 | **Violations** (Module 23) | `/violations` | 5 | ✅ ĐÃ XONG (5/5) | ✅ 11 tests |

---

## 2. QUY CHUẨN KIẾN TRÚC BẮT BUỘC (ARCHITECTURAL CONVENTIONS)
Mọi module khi bảo trì hoặc phát triển mở rộng phải tuân thủ 100% các quy tắc sau:

1. **Phân tầng nghiêm ngặt (Strict Layering):**
   * `Entity`: Đọc từ `com.safedriving.entity`.
   * `Repository`: Đặt tại `com.safedriving.repository`, kế thừa `JpaRepository<Entity, IdType>`.
   * `Service Layer`:
     * Interface tại `com.safedriving.service.<Name>Service.java`.
     * Class triển khai tại `com.safedriving.service.impl.<Name>ServiceImpl.java` (gắn `@Service`, `@Transactional`, `@RequiredArgsConstructor`, `@Slf4j`).
   * `Controller`: Đặt tại `com.safedriving.controller.<Name>Controller.java`, gắn Swagger `@Tag`, `@Operation`, và Spring Security `@PreAuthorize`.
   * `DTO`:
     * Request DTO: `com.safedriving.dto.request.<Name>Request.java` (có Jakarta Validation: `@NotBlank`, `@NotNull`...).
     * Response DTO: `com.safedriving.dto.response.<Name>Response.java`.
     * **Quy tắc vàng:** Không bao giờ expose trực tiếp JPA Entity ra Controller.

2. **Chuẩn bọc Response Envelope:**
   * Tất cả Controller trả về `ResponseEntity<ApiResponse<T>>` hoặc `ResponseEntity<ApiResponse<Void>>`.
   * Thành công có dữ liệu: `ApiResponse.success(message, data)`.
   * Thành công không có dữ liệu (xóa): `ApiResponse.success(message)`.

3. **Chuẩn Xử lý Ngoại lệ (Exceptions):**
   * Không tìm thấy bản ghi theo ID: ném `ResourceNotFoundException("Không tìm thấy ... với ID: " + id)`.
   * Lỗi dữ liệu/nghiệp vụ: ném `BadRequestException("Lý do...")`.
   * Toàn bộ lỗi được bắt tự động bởi `com.safedriving.exception.GlobalExceptionHandler`.

4. **Chuẩn Kiểm thử (Testing):**
   * Viết đủ JUnit 5 + Mockito cho cả Service và Controller.
   * Cấu hình test in-memory H2 đã có tại `src/test/resources/application.yml`.
   * Chạy lệnh kiểm tra: `.\mvnw.cmd test` (phải BUILD SUCCESS 100%).

---

## 3. DANH MỤC 7 MODULE ĐÃ HOÀN THÀNH (7/7)

### 3.1. Module Addresses (`/addresses`)
* **Entity:** `Address.java` (UUID `String`)
* **DTO:** `AddressRequest.java`, `AddressResponse.java`
* **Repository:** `AddressRepository.java`
* **Service:** `AddressService.java`, `AddressServiceImpl.java`
* **Controller:** `AddressController.java`
* **Tests:** `AddressServiceTest.java`, `AddressControllerTest.java`

### 3.2. Module Vehicle Logs (`/vehicle-logs`)
* **Entity:** `VehicleLog.java` (Khóa chính `Long`), liên kết `Vehicle.java`
* **DTO:** `VehicleLogRequest.java`, `VehicleLogResponse.java`
* **Repositories:** `VehicleLogRepository.java`, `VehicleRepository.java`
* **Service:** `VehicleLogService.java`, `VehicleLogServiceImpl.java`
* **Controller:** `VehicleLogController.java` (hỗ trợ `?vehicleId=...`)
* **Tests:** `VehicleLogServiceTest.java`, `VehicleLogControllerTest.java`

### 3.3. Module Stops (`/stops`)
* **Entity:** `Stop.java` (UUID `String`), liên kết `Address.java`, enum `StopType.java`
* **DTO:** `StopRequest.java`, `StopResponse.java` (chứa nested `AddressResponse`)
* **Repository:** `StopRepository.java`
* **Service:** `StopService.java`, `StopServiceImpl.java`
* **Controller:** `StopController.java`
* **Tests:** `StopServiceTest.java`, `StopControllerTest.java`

### 3.4. Module Routes (`/routes`)
* **Entity:** `Route.java` (UUID `String`)
* **DTO:** `RouteRequest.java`, `RouteResponse.java`
* **Repository:** `RouteRepository.java`
* **Service:** `RouteService.java`, `RouteServiceImpl.java`
* **Controller:** `RouteController.java`
* **Tests:** `RouteServiceTest.java`, `RouteControllerTest.java`

### 3.5. Module Route Stops (`/route-stops`)
* **Entity:** `RouteStop.java` (UUID `String`), liên kết `Route.java`, `Stop.java`
* **DTO:** `RouteStopRequest.java`, `RouteStopResponse.java` (nested `StopResponse`)
* **Repository:** `RouteStopRepository.java`
* **Service:** `RouteStopService.java`, `RouteStopServiceImpl.java`
* **Controller:** `RouteStopController.java` (hỗ trợ `?routeId=...`)
* **Tests:** `RouteStopServiceTest.java`, `RouteStopControllerTest.java`

### 3.6. Module Trip Progress (`/trip-progress`)
* **Entity:** `TripProgress.java` (UUID `String`), liên kết `Trip.java`
* **DTO:** `TripProgressRequest.java`, `TripProgressResponse.java`
* **Repositories:** `TripProgressRepository.java`, `TripRepository.java`
* **Service:** `TripProgressService.java`, `TripProgressServiceImpl.java`
* **Controller:** `TripProgressController.java` (hỗ trợ `?tripId=...`)
* **Tests:** `TripProgressServiceTest.java`, `TripProgressControllerTest.java`

### 3.7. Module Violations (`/violations`)
* **Entity:** `Violation.java` (UUID `String`), liên kết `Driver.java`, `Incident.java`, `Account.java`
* **DTO:** `ViolationRequest.java`, `ViolationResponse.java`
* **Repositories:** `ViolationRepository.java`, `DriverRepository.java`, `IncidentRepository.java`
* **Service:** `ViolationService.java`, `ViolationServiceImpl.java`
* **Controller:** `ViolationController.java` (hỗ trợ `?driverId=...`)
* **Tests:** `ViolationServiceTest.java`, `ViolationControllerTest.java`
