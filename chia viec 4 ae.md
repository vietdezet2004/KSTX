## 5. KẾ HOẠCH GITHUB COMMIT THEO TRỤC THỜI GIAN (CHRONOLOGICAL COMMIT TIMELINE)

> 💡 **Quy tắc vàng phân bổ Commit:**  
> Toàn bộ quá trình code được chia làm **5 CHẶNG THỜI GIAN TUẦN TỰ**.  
> Tại mỗi chặng, các thành viên thực hiện commit theo đúng thứ tự đánh số.  
> Các commit có chữ thường là **đã có code sẵn trong project, chỉ việc tạo commit theo quy trình**.  
> Các commit có **CHỮ IN ĐẬM LÀ VIỆC CHƯA CÓ TRONG CODE, CẦN BẮT TAY CODE MỚI HOÀN TOÀN**.

```
TRỤC THỜI GIAN THỰC HIỆN CỦA 4 THÀNH VIÊN:
┌──────────────────────────────────────────────────────────────────────────────────────────────┐
│ [MỐC 1: KỆ GỐC] ──> [MỐC 2: HỒ SƠ & XE] ──> [MỐC 3: VẬN HÀNH & LOGS] ──> [MỐC 4: HITL/MQTT] │
│ (Account, Address,   (Staff, Driver, Trip,  (Assignment, Incidents,   (Risk Engine, MQTT,  │
│  VehicleType, Stop)   RouteStop)             VehicleLog, SafetyLog)    HITL Hub, S3 Janitor)│
└──────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

### ⏳ MỐC THỜI GIAN 1 (TUẦN 1): THIẾT LẬP NỀN TẢNG & CÁC "KỆ SÁCH GỐC" ĐỘC LẬP (LEVEL 0 & 1)

- **Ý nghĩa:** Tạo dựng toàn bộ các bảng Master Data độc lập không có khóa ngoại chéo. Cả 4 người làm song song trên nhánh cá nhân.

| Thứ tự Commit | Thành viên | Nhánh cá nhân                              | Nội dung Commit Message                                                                                | Trạng thái Code trong Project             |
| :-----------: | :--------: | :----------------------------------------- | :----------------------------------------------------------------------------------------------------- | :---------------------------------------- |
|   **C1.1**    |  **(1)**   | `feature/tv1-auth-identity-audit`          | `feat(core): initialize BaseUuidEntity, ApiResponse and GlobalExceptionHandler`                        | Đã có code sẵn trong `src/`               |
|   **C1.2**    |  **(1)**   | `feature/tv1-auth-identity-audit`          | `feat(security): setup Spring Security 6, JWT TokenProvider, and JwtAuthenticationFilter`              | Đã có code sẵn trong `src/`               |
|   **C1.3**    |  **(1)**   | `feature/tv1-auth-identity-audit`          | `feat(auth): implement AuthService and AuthController with login, register, refresh`                   | Đã có code sẵn trong `src/`               |
|   **C1.4**    |  **(1)**   | `feature/tv1-auth-identity-audit`          | `feat(account-entity): define Account entity, AccountRole, AccountStatus enums, and AccountRepository` | Đã có code sẵn trong `src/`               |
|   **C1.5**    |  **(1)**   | `feature/tv1-auth-identity-audit`          | **`feat(accounts): implement CRUD service and controller for Account management`**                     | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C1.6**    |  **(3)**   | `feature/tv3-routes-telemetry-risk-engine` | `feat(address): implement Address entity, repository, service, and controller`                         | Đã có code sẵn trong `src/`               |
|   **C1.7**    |  **(3)**   | `feature/tv3-routes-telemetry-risk-engine` | `feat(route): implement Route entity, repository, service, and controller`                             | Đã có code sẵn trong `src/`               |
|   **C1.8**    |  **(3)**   | `feature/tv3-routes-telemetry-risk-engine` | `feat(stop): implement Stop entity, StopType enum, repository, and controller`                         | Đã có code sẵn trong `src/`               |
|   **C1.9**    |  **(4)**   | `feature/tv4-vehicles-mqtt-hitl-realtime`  | `feat(vehicle-type-entity): define VehicleType entity and OpenAPI Swagger configuration`               | Đã có code sẵn trong `src/`               |
|   **C1.10**   |  **(4)**   | `feature/tv4-vehicles-mqtt-hitl-realtime`  | **`feat(vehicle-types): implement VehicleType repository, service and CRUD controller`**               | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C1.11**   |  **(4)**   | `feature/tv4-vehicles-mqtt-hitl-realtime`  | `feat(vehicle-entity): define Vehicle entity, VehicleStatus enum, and VehicleRepository`               | Đã có code sẵn trong `src/`               |
|   **C1.12**   |  **(4)**   | `feature/tv4-vehicles-mqtt-hitl-realtime`  | **`feat(vehicles): implement Vehicle fleet CRUD service and controller`**                              | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C1.13**   |  **(2)**   | `feature/tv2-drivers-trips-incidents`      | `feat(license-class-entity): define LicenseClass and License entities`                                 | Đã có code sẵn trong `src/`               |
|   **C1.14**   |  **(2)**   | `feature/tv2-drivers-trips-incidents`      | **`feat(license-classes): implement LicenseClass repository, service and controller`**                 | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |

> 🛑 **CHECKPOINT MERGE ĐỢT 1:**  
> Sau khi xong Mốc 1, cả 4 thành viên tạo Pull Request merge vào nhánh `develop`.  
> Tất cả cùng chạy lệnh: `git checkout develop && git pull origin develop` để có đầy đủ "Kệ gốc".

---

### ⏳ MỐC THỜI GIAN 2 (TUẦN 2): THỰC THỂ PHỤ THUỘC CẤP 1 & HỒ SƠ TÀI XẾ (LEVEL 2)

- **Ý nghĩa:** Giải quyết nút thắt lớn nhất: `Driver` cần `Staff` của (1) và `Vehicle` của (4); `Trip` cần `Route` của (3).

| Thứ tự Commit | Thành viên | Nhánh cá nhân                              | Nội dung Commit Message                                                                      | Trạng thái Code trong Project             |
| :-----------: | :--------: | :----------------------------------------- | :------------------------------------------------------------------------------------------- | :---------------------------------------- |
|   **C2.1**    |  **(1)**   | `feature/tv1-auth-identity-audit`          | `feat(staff-entity): define Staff entity mapped to Account and Address`                      | Đã có code sẵn trong `src/`               |
|   **C2.2**    |  **(1)**   | `feature/tv1-auth-identity-audit`          | **`feat(staff): implement Staff repository, DTOs, service and CRUD controller`**             | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C2.3**    |  **(3)**   | `feature/tv3-routes-telemetry-risk-engine` | `feat(route-stops): implement RouteStop sequence mapping service and controller`             | Đã có code sẵn trong `src/`               |
|   **C2.4**    |  **(2)**   | `feature/tv2-drivers-trips-incidents`      | `feat(trip-entity): define Trip entity and TripRepository mapped to Route`                   | Đã có code sẵn trong `src/`               |
|   **C2.5**    |  **(2)**   | `feature/tv2-drivers-trips-incidents`      | **`feat(trips): implement Trip scheduling service and controller with status transitions`**  | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C2.6**    |  **(2)**   | `feature/tv2-drivers-trips-incidents`      | `feat(driver-entity): define Driver entity and DriverRepository linked to Staff and Vehicle` | Đã có code sẵn trong `src/`               |
|   **C2.7**    |  **(2)**   | `feature/tv2-drivers-trips-incidents`      | **`feat(drivers): implement Driver profile service and controller with photo handling`**     | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |

> 🛑 **CHECKPOINT MERGE ĐỢT 2:**  
> Sau khi xong Mốc 2, cả 4 thành viên merge vào nhánh `develop`.  
> Tất cả cùng `git pull origin develop` để có đầy đủ `Driver`, `Trip`, `Staff`.

---

### ⏳ MỐC THỜI GIAN 3 (TUẦN 3): NGHIỆP VỤ VẬN HÀNH & NHẬT KÝ TELEMETRY (LEVEL 3)

- **Ý nghĩa:** Hoàn thiện bảng phân công, sự cố, lộ trình chạy thực tế và chuẩn bị bảng Log an toàn mới.

| Thứ tự Commit | Thành viên | Nhánh cá nhân                              | Nội dung Commit Message                                                                               | Trạng thái Code trong Project             |
| :-----------: | :--------: | :----------------------------------------- | :---------------------------------------------------------------------------------------------------- | :---------------------------------------- |
|   **C3.1**    |  **(2)**   | `feature/tv2-drivers-trips-incidents`      | **`feat(licenses): implement Driver License repository, service and controller`**                     | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C3.2**    |  **(2)**   | `feature/tv2-drivers-trips-incidents`      | `feat(assignment-entity): define Assignment entity linked to Trip, Driver, and Vehicle`               | Đã có code sẵn trong `src/`               |
|   **C3.3**    |  **(2)**   | `feature/tv2-drivers-trips-incidents`      | **`feat(assignments): implement Assignment service with validation to prevent overlap schedule`**     | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C3.4**    |  **(2)**   | `feature/tv2-drivers-trips-incidents`      | `feat(incident-entity): define Incident entity, IncidentType, IncidentSeverity enums`                 | Đã có code sẵn trong `src/`               |
|   **C3.5**    |  **(2)**   | `feature/tv2-drivers-trips-incidents`      | **`feat(incidents): implement Incident service triggering auto trip cancellation for HIGH severity`** | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C3.6**    |  **(3)**   | `feature/tv3-routes-telemetry-risk-engine` | `feat(trip-progress): implement TripProgress actual arrival and leave logging APIs`                   | Đã có code sẵn trong `src/`               |
|   **C3.7**    |  **(3)**   | `feature/tv3-routes-telemetry-risk-engine` | `feat(vehicle-logs): implement VehicleLog GPS telemetry history tracking APIs`                        | Đã có code sẵn trong `src/`               |
|   **C3.8**    |  **(3)**   | `feature/tv3-routes-telemetry-risk-engine` | **`feat(safety-log-entity): define SafetyAlertLog entity, SafetyAlertLevel, SafetyAlertType enums`**  | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C3.9**    |  **(3)**   | `feature/tv3-routes-telemetry-risk-engine` | **`feat(safety-logs): implement SafetyAlertLog recording service and REST query controller`**         | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |

> 🛑 **CHECKPOINT MERGE ĐỢT 3:**  
> Merge toàn bộ vào nhánh `develop`. Lúc này 100% Database Entity và CRUD cơ bản đã sẵn sàng!

---

### ⏳ MỐC THỜI GIAN 4 (TUẦN 4): REALTIME HITL, MQTT GATEWAY, RISK ENGINE & KIỂM TOÁN (LEVEL 4)

- **Ý nghĩa:** Chặng kỹ thuật chuyên sâu nhất — Kết nối điện thoại tài xế, tính toán rủi ro và luồng duyệt 2 cấp.

| Thứ tự Commit | Thành viên | Nhánh cá nhân                              | Nội dung Commit Message                                                                                 | Trạng thái Code trong Project             |
| :-----------: | :--------: | :----------------------------------------- | :------------------------------------------------------------------------------------------------------ | :---------------------------------------- |
|   **C4.1**    |  **(3)**   | `feature/tv3-routes-telemetry-risk-engine` | **`feat(risk-aggregator): implement Cumulative Risk Aggregator service and driver safety profile API`** | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C4.2**    |  **(4)**   | `feature/tv4-vehicles-mqtt-hitl-realtime`  | **`feat(system-health): implement GET /health endpoint for server & MQTT broker monitoring`**           | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C4.3**    |  **(4)**   | `feature/tv4-vehicles-mqtt-hitl-realtime`  | **`feat(mqtt-gateway): configure Spring Integration MQTT connecting EMQX for mobile telemetry`**        | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C4.4**    |  **(4)**   | `feature/tv4-vehicles-mqtt-hitl-realtime`  | **`feat(hitl-entity): define HitlCase entity and HitlStatus enum for 2-tier approval`**                 | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C4.5**    |  **(4)**   | `feature/tv4-vehicles-mqtt-hitl-realtime`  | **`feat(hitl-service): implement Safety HITL service for manager approval and driver ACK`**             | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C4.6**    |  **(4)**   | `feature/tv4-vehicles-mqtt-hitl-realtime`  | **`feat(websocket): configure WebSocket hub pushing realtime safety alerts to dashboard`**              | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C4.7**    |  **(4)**   | `feature/tv4-vehicles-mqtt-hitl-realtime`  | **`feat(storage-janitor): implement S3 pre-signed upload URL and 24h retention janitor`**               | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C4.8**    |  **(4)**   | `feature/tv4-vehicles-mqtt-hitl-realtime`  | **`feat(analytics): implement Causal Safety Analytics engine and reports`**                             | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C4.9**    |  **(1)**   | `feature/tv1-auth-identity-audit`          | **`feat(audit-log-entity): define immutable AuditLog entity and AuditLogRepository`**                   | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C4.10**   |  **(1)**   | `feature/tv1-auth-identity-audit`          | **`feat(audit-service): implement AuditLog recording service, JPA Auditing, and query controller`**     | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |

---

### ⏳ MỐC THỜI GIAN 5 (TUẦN 5): KIỂM THỬ TOÀN DIỆN & TỐI ƯU BẢO VỆ ĐỒ ÁN (PHASE 5)

- **Ý nghĩa:** Đảm bảo `.\mvnw.cmd test` **BUILD SUCCESS 100%**, mỗi người có ít nhất 25 Unit Tests.

| Thứ tự Commit | Thành viên | Nhánh cá nhân                              | Nội dung Commit Message                                                                                | Trạng thái Code trong Project             |
| :-----------: | :--------: | :----------------------------------------- | :----------------------------------------------------------------------------------------------------- | :---------------------------------------- |
|   **C5.1**    |  **(1)**   | `feature/tv1-auth-identity-audit`          | `test(auth): add unit tests for AuthController and AuthService`                                        | Đã có code sẵn trong `src/test/`          |
|   **C5.2**    |  **(1)**   | `feature/tv1-auth-identity-audit`          | **`test(identity-audit): add unit tests for Account, Staff, and AuditLog services (25+ tests)`**       | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C5.3**    |  **(2)**   | `feature/tv2-drivers-trips-incidents`      | **`test(fleet-ops): add comprehensive unit tests for Driver, Trip, Assignment, Incident (25+ tests)`** | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C5.4**    |  **(3)**   | `feature/tv3-routes-telemetry-risk-engine` | `test(routing-progress): add unit tests for Address, Stop, Route, Progress, VehicleLog`                | Đã có 82 tests trong `src/test/`          |
|   **C5.5**    |  **(3)**   | `feature/tv3-routes-telemetry-risk-engine` | **`test(risk-engine): add unit tests for SafetyAlertLog and Cumulative Risk Aggregator`**              | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |
|   **C5.6**    |  **(4)**   | `feature/tv4-vehicles-mqtt-hitl-realtime`  | **`test(mqtt-hitl): add unit tests for Vehicles, HITL Workflow, S3 Janitor, Analytics (25+ tests)`**   | **CHƯA CÓ TRONG CODE - BẮT TAY CODE MỚI** |

---

## 6. DANH MỤC COMMIT GOM THEO TỪNG NHÁNH CÁ NHÂN (ĐỂ TỰ COPY-PASTE)

Từng bạn có thể copy trực tiếp danh sách commit dưới đây khi làm việc trên branch của mình:

### Nhánh Thành viên 1 (`feature/tv1-auth-identity-audit`):

1. `commit 1`: `feat(core): initialize BaseUuidEntity, ApiResponse and GlobalExceptionHandler` _(Đã có)_
2. `commit 2`: `feat(security): setup Spring Security 6, JWT TokenProvider, and JwtAuthenticationFilter` _(Đã có)_
3. `commit 3`: `feat(auth): implement AuthService and AuthController with login, register, refresh` _(Đã có)_
4. `commit 4`: `feat(account-entity): define Account entity, AccountRole, AccountStatus enums, and AccountRepository` _(Đã có)_
5. **`commit 5`**: **`feat(accounts): implement CRUD service and controller for Account management`** _(MỚI)_
6. `commit 6`: `feat(staff-entity): define Staff entity mapped to Account and Address` _(Đã có)_
7. **`commit 7`**: **`feat(staff): implement Staff repository, DTOs, service and CRUD controller`** _(MỚI)_
8. **`commit 8`**: **`feat(audit-log-entity): define immutable AuditLog entity and AuditLogRepository`** _(MỚI)_
9. **`commit 9`**: **`feat(audit-service): implement AuditLog recording service, JPA Auditing, and query controller`** _(MỚI)_
10. **`commit 10`**: **`test: add comprehensive unit tests for Account, Staff, and AuditLog services (25+ tests)`** _(MỚI)_

---

### Nhánh Thành viên 2 (`feature/tv2-drivers-trips-incidents`):

1. `commit 1`: `feat(license-class-entity): define LicenseClass and License entities` _(Đã có)_
2. **`commit 2`**: **`feat(license-classes): implement LicenseClass repository, service and controller`** _(MỚI)_
3. `commit 3`: `feat(trip-entity): define Trip entity and TripRepository mapped to Route` _(Đã có)_
4. **`commit 4`**: **`feat(trips): implement Trip scheduling service and controller with status transitions`** _(MỚI)_
5. `commit 5`: `feat(driver-entity): define Driver entity and DriverRepository linked to Staff and Vehicle` _(Đã có)_
6. **`commit 6`**: **`feat(drivers): implement Driver profile service and controller with photo handling`** _(MỚI)_
7. **`commit 7`**: **`feat(licenses): implement Driver License repository, service and controller`** _(MỚI)_
8. `commit 8`: `feat(assignment-entity): define Assignment entity linked to Trip, Driver, and Vehicle` _(Đã có)_
9. **`commit 9`**: **`feat(assignments): implement Assignment service with validation to prevent overlap schedule`** _(MỚI)_
10. `commit 10`: `feat(incident-entity): define Incident entity, IncidentType, IncidentSeverity enums` _(Đã có)_
11. **`commit 11`**: **`feat(incidents): implement Incident service triggering auto trip cancellation for HIGH severity`** _(MỚI)_
12. **`commit 12`**: **`test: add comprehensive unit tests for Driver, Trip, Assignment, Incident (25+ tests)`** _(MỚI)_

---

### Nhánh Thành viên 3 (`feature/tv3-routes-telemetry-risk-engine`):

1. `commit 1`: `feat(address): implement Address entity, repository, service, and controller` _(Đã có)_
2. `commit 2`: `feat(route): implement Route entity, repository, service, and controller` _(Đã có)_
3. `commit 3`: `feat(stop): implement Stop entity, StopType enum, repository, and controller` _(Đã có)_
4. `commit 4`: `feat(route-stops): implement RouteStop sequence mapping service and controller` _(Đã có)_
5. `commit 5`: `feat(trip-progress): implement TripProgress actual arrival and leave logging APIs` _(Đã có)_
6. `commit 6`: `feat(vehicle-logs): implement VehicleLog GPS telemetry history tracking APIs` _(Đã có)_
7. **`commit 7`**: **`feat(safety-log-entity): define SafetyAlertLog entity, SafetyAlertLevel, SafetyAlertType enums`** _(MỚI)_
8. **`commit 8`**: **`feat(safety-logs): implement SafetyAlertLog recording service and REST query controller`** _(MỚI)_
9. **`commit 9`**: **`feat(risk-aggregator): implement Cumulative Risk Aggregator service and driver safety profile API`** _(MỚI)_
10. **`commit 10`**: **`test: add unit tests for SafetyAlertLog and Cumulative Risk Aggregator`** _(MỚI)_

---

### Nhánh Thành viên 4 (`feature/tv4-vehicles-mqtt-hitl-realtime`):

1. `commit 1`: `feat(vehicle-type-entity): define VehicleType entity and OpenAPI Swagger configuration` _(Đã có)_
2. **`commit 2`**: **`feat(vehicle-types): implement VehicleType repository, service and CRUD controller`** _(MỚI)_
3. `commit 3`: `feat(vehicle-entity): define Vehicle entity, VehicleStatus enum, and VehicleRepository` _(Đã có)_
4. **`commit 4`**: **`feat(vehicles): implement Vehicle fleet CRUD service and controller`** _(MỚI)_
5. **`commit 5`**: **`feat(system-health): implement GET /health endpoint for server & MQTT broker monitoring`** _(MỚI)_
6. **`commit 6`**: **`feat(mqtt-gateway): configure Spring Integration MQTT connecting EMQX for mobile telemetry`** _(MỚI)_
7. **`commit 7`**: **`feat(hitl-entity): define HitlCase entity and HitlStatus enum for 2-tier approval`** _(MỚI)_
8. **`commit 8`**: **`feat(hitl-service): implement Safety HITL service for manager approval and driver ACK`** _(MỚI)_
9. **`commit 9`**: **`feat(websocket): configure WebSocket hub pushing realtime safety alerts to dashboard`** _(MỚI)_
10. **`commit 10`**: **`feat(storage-janitor): implement S3 pre-signed upload URL and 24h retention janitor`** _(MỚI)_
11. **`commit 11`**: **`feat(analytics): implement Causal Safety Analytics engine and reports`** _(MỚI)_
12. **`commit 12`**: **`test: add comprehensive unit tests for Vehicles, HITL, S3, Analytics (25+ tests)`** _(MỚI)_

---

## 7. QUY CHUẨN LẬP TRÌNH BẮT BUỘC ĐỐI VỚI CẢ 4 THÀNH VIÊN

1. **Phân tầng nghiêm ngặt (Strict Layering):**
   - `Entity`: Đặt trong `com.safedriving.entity`, kế thừa `BaseUuidEntity` (trừ bảng có ID đặc thù).
   - `Repository`: Đặt trong `com.safedriving.repository`, kế thừa `JpaRepository`.
   - `Service`: Interface trong `com.safedriving.service`, class triển khai trong `com.safedriving.service.impl`.
   - `Controller`: Đặt trong `com.safedriving.controller`, gắn Swagger `@Tag`, `@Operation` và `@PreAuthorize`.
   - `DTO`: Request DTO trong `com.safedriving.dto.request`, Response DTO trong `com.safedriving.dto.response`. **Tuyệt đối không trả Entity trực tiếp ra Controller**.
2. **Chuẩn bọc Response Envelope:**
   - Mọi Controller phải trả về `ResponseEntity<ApiResponse<T>>` hoặc `ResponseEntity<ApiResponse<Void>>`.
   - Thành công có dữ liệu: `ApiResponse.success("Thông báo...", data)`.
   - Thành công không có dữ liệu (xóa): `ApiResponse.success("Đã xóa...")`.
3. **Chuẩn Xử lý Ngoại lệ (Exception Handling):**
   - Không tìm thấy bản ghi theo ID: ném `ResourceNotFoundException("Không tìm thấy ... với ID: " + id)`.
   - Dữ liệu không hợp lệ: ném `BadRequestException("Lý do...")`.
   - Toàn bộ ngoại lệ được bắt tự động bởi `GlobalExceptionHandler`.
4. **Kiểm thử tự động trước khi Push Code:**
   - Mỗi Controller và Service phải có unit test với Mockito + JUnit 5.
   - Chạy lệnh `.\mvnw.cmd test` tại máy cá nhân, đảm bảo **100% BUILD SUCCESS** trước khi tạo Pull Request vào nhánh `main` hoặc `develop`.

---

## 7. TỔNG HỢP KHỐI LƯỢNG CÔNG VIỆC THEO THÀNH VIÊN

> **Cách tính:** Số API được đếm lại từ các endpoint mô tả ở mục 3; mỗi module ghi CRUD được tính 5 API. Đây là số lượng bề mặt API, không phải thước đo đầy đủ độ khó. Số test và commit là mục tiêu trong kế hoạch, không thay thế việc ước lượng effort theo nghiệp vụ và tích hợp.

| Thành viên     | Phạm vi chính                                             | Entity / Enum theo ma trận |    API theo mô tả chi tiết    | Phần việc ngoài CRUD                                                                 | Test / Commit mục tiêu | Đánh giá tải tương đối                                                                                             |
| :------------- | :-------------------------------------------------------- | :------------------------: | :---------------------------: | :----------------------------------------------------------------------------------- | :--------------------: | :----------------------------------------------------------------------------------------------------------------- |
| **TV1 (183)**  | Auth, Account, Staff, Audit Log                           |     3 Entity + 3 Enum      |          **17 API**           | Spring Security/JWT, JPA Auditing, phân quyền, global exception                      |  ~25 test / 8 commit   | **Trung bình**: security và audit có độ khó đáng kể, nhưng phạm vi nghiệp vụ/API nhỏ nhất.                         |
| **TV2 (844)**  | License, Driver, Trip, Assignment, Incident               |     6 Entity + 3 Enum      |          **30 API**           | Kiểm tra hạng bằng/hạn bằng, chống trùng lịch, xử lý chuyển trạng thái chuyến        |  ~25 test / 8 commit   | **Cao**: nhiều module nghiệp vụ, quan hệ dữ liệu và validation liên module.                                        |
| **TV3 (184)**  | Address, Stop, Route, Trip Progress, GPS, Safety Log/Risk |     7 Entity + 2 Enum      |          **35 API**           | MQTT event consumer boundary, khử trùng lặp, cộng dồn rủi ro, safety profile/summary |  ~25 test / 8 commit   | **Rất cao**: nhiều API nhất, nhiều entity nhất và có logic tính điểm/ngưỡng an toàn.                               |
| **TV4 (Lead)** | Vehicle, MQTT, HITL, realtime, storage, analytics         |     3 Entity + 2 Enum      | **18 API + 1 kênh WebSocket** | EMQX, command/ACK, quy trình HITL, S3 presigned upload, retention janitor, analytics |  ~25 test / 9 commit   | **Rất cao / có nguy cơ quá tải**: số API vừa phải nhưng tập trung nhiều tích hợp hạ tầng và phụ thuộc vào TV1/TV3. |

### 7.1. Đối chiếu số liệu

- **TV1:** danh sách chi tiết cộng lại là 4 Auth + 5 Account + 5 Staff + 3 Audit Log = **17 API**, không phải 18.
- **TV2:** 2 module License CRUD + Driver 5 + Trip 5 + Assignment CRUD + Incident CRUD = **30 API**, không phải 26.
- **TV3:** 6 module CRUD (Address, Stop, Route, Route Stop, Trip Progress, Vehicle Log) là 30 API; cộng 5 API Safety là **35 API**, không phải 26.
- **TV4:** Health 1 + Vehicle Type 5 + Vehicle 5 + HITL 4 + Storage 1 + Analytics 2 = **18 API**, chưa tính WebSocket. MQTT là giao tiếp nội bộ với broker, không tính là REST API.
- Tổng theo mô tả hiện tại là **100 REST API**, không phải 88 theo các con số ở ma trận. Mục tiêu commit chi tiết là **33 commit** (8 + 8 + 8 + 9); mục tiêu unit test cộng lại khoảng **100 test**.

### 7.2. Nhận xét cân bằng và cách điều chỉnh

Theo bản phân công hiện tại, khối lượng **chưa cân bằng**: TV1 thấp hơn về số module/API; TV2 và TV3 nặng về CRUD cộng nghiệp vụ; TV4 nặng về tích hợp và đang là điểm nghẽn phối hợp. Không nên san đều bằng cách ép số API, test hay commit giống nhau vì độ khó của Security, MQTT và HITL khác nhau.

Để cân bằng theo năng lực thực tế và giữ đúng Core MVP:

1. Giữ TV1 sở hữu Auth/RBAC và Audit Log; bổ sung thiết kế hợp đồng phân quyền, audit cho các quyết định HITL và review security cho MQTT credentials/ACL. Không biến endpoint tạo Audit Log công khai thành cách ghi audit duy nhất; log quyết định nên được ghi từ service nghiệp vụ.
2. TV2 ưu tiên các thực thể vận hành hiện có chỉ khi Safety Core cần liên kết tài xế/xe/chuyến. Đưa xây mới điều phối, lập lịch chuyến và xử lý Incident không thiết yếu sang ngoài MVP/Phase 2.
3. TV3 sở hữu schema/payload sự kiện an toàn, lưu log metadata, idempotency và risk aggregation; thống nhất ngưỡng/kết quả với TV4 trước khi code để tránh chia đôi một luồng nghiệp vụ.
4. TV4 sở hữu cấu hình EMQX, command/ACK và HITL orchestration. Chuyển phần dashboard realtime sang một task riêng hoặc dùng MQTT over WSS nếu dashboard cần subscribe; **không triển khai WebSocket thô**. Storage chỉ xử lý clip 5 giây cho Cấp 3 và retention 24 giờ.
5. Phân bổ lại phần triển khai/tích hợp cụ thể sau khi loại bỏ scope Phase 2 và xác định migration từ code hiện hữu; khi đó mới chốt effort theo ngày công. Không coi số commit mục tiêu là tiêu chí đánh giá đóng góp.
