# KẾ HOẠCH PHÂN CHIA CÔNG VIỆC — DỰ ÁN DRIVERGUARD (MASTER v3.0)

## MỤC TIÊU CHIẾN LƯỢC: CHẠY THÔNG LUỒNG TOÀN HỆ THỐNG (END-TO-END) VÀO THỨ 7

> **Hạn chót tích hợp (Integration Deadline):** 17:00 Thứ 7  
> **Tiêu chí nghiệm thu Thứ 7:** Kịch bản "Golden E2E Flow" chạy thông suốt:  
> `Android Buồng lái (Ngủ gật -> Còi hú ≤ 300ms -> Clip 5s) -> MQTT Broker (EMQX) -> Backend FastAPI (Lưu DB & Bắn SSE) -> Web Dashboard (Bật Popup 5s duyệt) -> Bắn lệnh MQTT -> Android Tài xế (Hiện popup xác nhận dừng nghỉ / khiếu nại) -> Sổ cái Audit Ledger đóng vết bất biến`.

---

### 👤 1. Phan Hoàng Vũ — BACKEND CORE ARCHITECT & IOT / REALTIME ENGINE

* **Mục tiêu:** Xây dựng "Trái tim" của hệ thống trên nền tảng **FastAPI (Python)** — Xử lý kết nối telemetry/alert qua MQTT, quản lý CSDL PostgreSQL 16 (Schema v3.0), sinh Pre-signed URL cho clip 5s, điều phối luồng duyệt HITL 2 cấp và lưu Sổ cái kiểm toán WORM bất biến.

#### 1.1. Trách nhiệm kỹ thuật chi tiết

1. **Khởi tạo CSDL PostgreSQL v3.0 (Alembic Migrations):**
   * Triển khai toàn bộ schema chuẩn hóa theo `DATABASE_DESIGN_DRIVERGUARD.md` bằng SQLAlchemy Models & Alembic:
     * `account`, `staff`, `vehicle`, `driver`.
     * `vehicle_log`: Khóa chính `BIGINT GENERATED ALWAYS AS IDENTITY` tối ưu cho 100k xe.
     * `safety_alert_log`: Lưu trữ sự kiện an toàn, metrics sinh trắc học ($PERCLOS, MAR, Pitch, Yaw$), idempotency key `event_id`, URL clip S3/MinIO.
     * `hitl_case`: Quản lý trạng thái duyệt 2 cấp (`PENDING_REVIEW`, `CONFIRMED_VIOLATION`, `REJECTED_FALSE_ALARM`, `ACCEPTED_REST`, `DISPUTED_GLARE`).
     * `safety_audit_ledger`: Trigger WORM cấm `UPDATE`/`DELETE`, băm SHA-256 HMAC chống giả mạo chứng cứ.
     * `safety_threshold_config`: Bảng lưu ngưỡng động ($EAR, MAR, Pitch, Yaw, Speed, S3 TTL$), tuyệt đối không hard-code theo lệnh Mentor.
2. **IoT Ingestion & Messaging Gateway (aiomqtt / Paho-MQTT):**
   * Subscribe các MQTT Topic qua EMQX Broker:
     * `telemetry/gps/{vehicleId}` (QoS 0): Nhận tọa độ định vị xe.
     * `safety/alert/{vehicleId}` (QoS 1): Nhận sự kiện vi phạm Cấp 2 và Cấp 3 theo đúng JSON Data Contract (SAD mục 5.2).
   * Publish lệnh can thiệp tới thiết bị buồng lái:
     * `command/driver/{driverId}` (QoS 1): Gửi chỉ thị dừng xe nghỉ ngơi khi Manager bấm xác nhận.
3. **Core Business API & Realtime Dispatch (FastAPI):**
   * REST API CRUD cấu hình ngưỡng an toàn (`/api/v1/safety-config`).
   * S3/MinIO Storage Service: API sinh Pre-signed URL PUT để Mobile upload nhanh clip 5s; Pre-signed GET cho Dashboard xem video; Worker dọn dẹp clip tự hủy sau 24h.
   * Realtime Push Service (SSE / WebSocket): Khi nhận alert Cấp 3 từ MQTT, lập tức push event khẩn cấp xuống Web Dashboard trong $< 500\text{ ms}$.
   * API tiếp nhận kết quả duyệt của Manager (`POST /api/v1/hitl/review`) -> Ghi nhận `hitl_case` -> Bắn lệnh MQTT xuống Mobile -> Ký số HMAC ghi vào `safety_audit_ledger`.

#### 1.2. Mục tiêu bàn giao để Thông Luồng Thứ 7

* [ ] Backend FastAPI khởi động mượt mà kết nối PostgreSQL và EMQX trên Docker.

* [ ] Migration script Alembic chạy sạch 100% không lỗi.
* [ ] Handler MQTT lắng nghe topic `safety/alert/+` ghi nhận dữ liệu vào DB và bắn SSE ra ngoài.
* [ ] Endpoint sinh Pre-signed URL cho Mobile upload clip MP4 và endpoint duyệt HITL hoạt động chuẩn xác.

---

### 👤 2. Nguyễn Công Duẩn — EDGE AI & MOBILE APPLICATION ENGINEER

* **Mục tiêu:** Xây dựng ứng dụng Android buồng lái chuyên dụng — Nhận diện nguy cơ ngủ gật/mất tập trung tại chỗ $\le 300\text{ ms}$ (Offline-first 100%), cắt video cuốn chiếu 5s trong RAM đẩy lên MinIO/S3, truyền tin MQTT bền bỉ và hiển thị màn hình tương tác Cấp 2 cho tài xế.

#### 2.1. Trách nhiệm kỹ thuật chi tiết

1. **CameraX & On-Device Vision Engine (MediaPipe FaceMesh):**
   * Tích hợp Google MediaPipe FaceMesh chạy trên GPU/NPU di động (15–20 FPS ổn định).
   * Trích xuất 468 landmark, tính toán chỉ số sinh trắc thời gian thực: $EAR$ (mắt), $MAR$ (miệng), $Head\ Pitch$ (gục đầu), $Head\ Yaw$ (ngoảnh mặt).
   * Cài đặt **Cửa sổ trượt 3 giây (Sliding Window 60 frames)** tính $PERCLOS_{3s}$ kết hợp bù trừ vận tốc GPS ($v < 5\text{ km/h}$ giảm nhạy lúc kẹt xe/đèn đỏ; $v \ge 70\text{ km/h}$ nâng mức khẩn cấp).
2. **Còi hú cứu mạng buồng lái ($\le 300\text{ ms}$):**
   * Kích hoạt âm thanh còi hú/rung khẩn cấp tức thì trên thiết bị khi chạm ngưỡng Cấp 2 ($Score \ge 70$) và Cấp 3 ($Score \ge 85$).
   * Nguyên tắc sống còn: Còi hú độc lập 100% offline, tuyệt đối không chờ phản hồi từ máy chủ.
3. **Circular Video Buffer RAM & Upload Clip 5s:**
   * Duy trì buffer cuốn chiếu 10s video trong RAM.
   * Khi chạm sự cố Cấp 3: Trích xuất 5 giây video (2s trước + 3s sau vi phạm) xuất ra file MP4 dung lượng nhẹ ($\approx 1 - 2\text{ MB}$).
   * Xin Pre-signed URL từ Backend và upload trực tiếp lên MinIO/S3 qua HTTP PUT.
4. **Giao thức MQTT Client & Xử lý Ngoại tuyến (Durable Outbox):**
   * MQTT Client kết nối EMQX:
     * Gửi GPS định kỳ lên `telemetry/gps/{vehicleId}` (QoS 0).
     * Gửi sự kiện an toàn lên `safety/alert/{vehicleId}` (QoS 1) kèm URL clip S3 đã upload.
     * Subscribe nhận lệnh can thiệp `command/driver/{driverId}` (QoS 1).
   * SQLite Room Outbox: Khi mất sóng 4G/GPS đường đèo, lưu sự kiện vào SQLite cục bộ; có mạng tự động đẩy bù theo thứ tự thời gian.
5. **Giao diện Buồng lái & Tương tác Cấp 2 (Driver Challenge):**
   * Khi nhận chỉ thị can thiệp từ Quản lý, màn hình bung thông báo to rõ với 2 nút bấm: `[ĐỒNG Ý DỪNG NGHỈ]` hoặc `[KHIẾU NẠI (BỊ CHÓI NẮNG)]`.
   * Bấm nút -> Gửi gói tin ACK phản hồi lên MQTT topic để Backend cập nhật Sổ cái.

#### 2.2. Mục tiêu bàn giao để Thông Luồng Thứ 7

* [ ] Ứng dụng Android (APK) mở camera trước, nhận diện khuôn mặt, giả lập nhắm mắt $> 1.5\text{s}$ -> Hú còi tại chỗ $< 300\text{ ms}$.

* [ ] Trích xuất được clip 5s và upload thành công lên MinIO qua Pre-signed URL.
* [ ] Publish được bản tin MQTT `safety/alert` đúng định dạng JSON chuẩn.
* [ ] Lắng nghe được lệnh từ topic `command/driver/{driverId}` và hiển thị giao diện 2 nút cho tài xế phản hồi.

---

### 👤 3. Đỗ Thành Đạt — AI MODELING, DATASET & EVALUATION SPECIALIST

* **Mục tiêu:** Chịu trách nhiệm phần "sống còn" bắt buộc trong Đề bài và phản biện của Hội đồng Mentor — Xây dựng tập dữ liệu tài xế người Việt, tinh chỉnh thuật toán Multimodal Fusion để triệt tiêu báo động giả và lập Báo cáo Khoa học (Evaluation Report v1.0).

#### 3.1. Trách nhiệm kỹ thuật chi tiết

1. **Xây dựng Tập dữ liệu Benchmark Tài xế Người Việt (Vietnamese Driver Dataset):**
   * Thu thập và gán nhãn thủ công (Ground Truth) tối thiểu 100 – 300 video/ảnh khuôn mặt người Việt trong bối cảnh thực tế buồng lái:
     * Đặc thù khuôn mặt người Việt: Mắt một mí/mắt híp, người đeo kính cận, kính râm nhẹ.
     * Điều kiện môi trường: Ban ngày, ban đêm, ngược sáng, ánh nắng rọi xiên gây chói nheo mắt.
     * Trạng thái hành vi: Lái xe bình thường, ngáp nói chuyện, nheo mắt do chói nắng, ngủ gật thật, gục đầu.
2. **Khử cảnh báo sai bằng Hợp nhất Đa tín hiệu (Multimodal Feature Fusion):**
   * Đánh giá baseline mô hình MediaPipe mặc định $\rightarrow$ Đo đạc tỷ lệ báo sai (False Positive).
   * Tinh chỉnh công thức kết hợp 5 lớp điều kiện (theo SAD mục 13):
     * *Phân biệt Ngáp thật vs Nói chuyện:* $MAR \ge 0.60$ kết hợp đồng thời $EAR \le 0.18$ trong $> 1.5\text{s}$.
     * *Phân biệt Gục đầu ngủ vs Nhìn táp-lô:* $Pitch \le -20^\circ$ kết hợp mắt nhắm $EAR < 0.20$.
     * *Bù trừ góc lệch camera táp-lô:* Tự động căn chỉnh offset $\approx 15^\circ - 25^\circ$ khi điện thoại đặt lệch phải tài xế.
   * Xuất bộ tham số ngưỡng tối ưu (JSON/Config) để nạp vào Android app của Duẩn và bảng `safety_threshold_config` của Vũ.
3. **Đo đạc & Lập Báo cáo Đánh giá Mô hình (Evaluation Report v1.0):**
   * Xây dựng script Python đánh giá tự động:
     * Tính Ma trận nhầm lẫn (Confusion Matrix: TP, FP, TN, FN).
     * Đo lường chỉ số sống còn: $Recall \ge 90\%$, $Precision \ge 85\%$, $F_1\text{-score} \ge 0.88$.
     * Đo tỷ lệ báo sai theo thời gian lái (False Alarm Rate): $FAR \le 1.5\text{ lần / 100 giờ}$.
   * Soạn thảo tài liệu **Evaluation Report v1.0** (kèm biểu đồ so sánh Baseline vs Optimized Model) sẵn sàng làm minh chứng bảo vệ trước Mentor.

#### 3.2. Mục tiêu bàn giao để Thông Luồng Thứ 7

* [ ] Tập dữ liệu gán nhãn chuẩn hóa (Ground Truth) lưu trữ trên Google Drive / repo.

* [ ] Bộ tham số ngưỡng ($EAR, MAR, Pitch, Yaw, Weights$) đã được kiểm chứng giảm $\ge 80\%$ cảnh báo giả khi chói nắng.
* [ ] Script Python tự động tính Confusion Matrix & F1-score chạy ra kết quả định lượng cụ thể.
* [ ] Bản nháp đồ thị Evaluation Report v1.0 để đưa vào báo cáo nghiệm thu.

---

### 👤 4. Phùng Quốc Việt — FRONTEND WEB DASHBOARD & DEVOPS / CI-CD

* **Mục tiêu:** Xây dựng Giao diện Điều hành An toàn trực quan (Fleet Safety Dashboard) cho Cán bộ Quản lý (HITL Tier 1) và thiết lập toàn bộ Hạ tầng Docker, EMQX, MinIO, CI/CD Runner VPS đảm bảo hệ thống sẵn sàng thông luồng mượt mà.

#### 4.1. Trách nhiệm kỹ thuật chi tiết

1. **Hạ tầng Container & DevOps (Docker & VPS):**
   * Thiết lập cụm Docker Compose chuẩn hóa:
     * `postgres:16`: CSDL quan hệ chính (port 5432).
     * `emqx:5.x`: MQTT Broker hiệu năng cao (port 1883 / 8883 / 18083 Dashboard).
     * `minio`: S3-compatible Object Storage lưu video clip 5s (port 9000 / 9001).
     * `backend-fastapi`: API Server & Ingestion worker (port 8000).
   * Cấu hình mạng Docker nội bộ kết nối thông suốt giữa các service.
   * Thiết lập GitHub Actions Workflow tự động lint, test và build Docker container.
2. **Web Dashboard Quản trị & Điều hành An toàn (React / Next.js):**
   * Giao diện chuẩn Dark Mode hiện đại, responsive, chuyên nghiệp cho trung tâm giám sát.
   * **Bản đồ Giám sát Đội xe Realtime (Leaflet / Mapbox):**
     * Hiển thị vị trí xe theo GPS telemetry nhận từ Backend.
     * Đổi màu icon trạng thái xe: Xanh (Bình thường), Vàng (Cảnh báo nhẹ), Đỏ (Đang có sự cố nguy hiểm).
   * **Trung tâm Can thiệp HITL Cấp 1 (Safety Incident Center):**
     * Lắng nghe sự kiện Realtime qua SSE / WebSocket từ Backend.
     * Bật **Popup khẩn cấp tức thì (< 1s)** khi có cảnh báo Cấp 3: Phát video 5 giây trích xuất từ MinIO/S3, biểu đồ chỉ số sinh trắc lúc xảy ra ($PERCLOS, EAR, Speed$).
     * 2 nút hành động cho Quản lý:
       * `[XÁC NHẬN NGUY HIỂM]` -> Gửi lệnh bắt buộc dừng xe nghỉ ngơi tới buồng lái.
       * `[BÁC BỎ BÁO SAI]` -> Đánh dấu cảnh báo sai (do chói nắng/rung giật), xóa clip, không trừ điểm tài xế.
   * **Màn hình Cấu hình Ngưỡng Động (Safety Config):**
     * Cho phép Quản trị viên thay đổi trực quan các ngưỡng $EAR, MAR, Pitch$, vận tốc kích hoạt, thời gian lưu clip S3 (1 ngày vs 30 ngày) lưu vào DB.
   * **Màn hình Tra cứu Sổ Cái Kiểm Toán (Audit Ledger):**
     * Bảng tra cứu lịch sử vi phạm bất biến: hiển thị ID, tài xế, loại sự cố, quyết định Manager, phản hồi tài xế, mã băm chữ ký số HMAC-SHA256.

#### 4.2. Mục tiêu bàn giao để Thông Luồng Thứ 7

* [ ] Toàn bộ hạ tầng Docker Compose (`postgres`, `emqx`, `minio`, `backend`) dựng lên chạy ổn định trên môi trường dev/VPS.

* [ ] Giao diện Web kết nối SSE với Backend, nhận event vi phạm bật popup video 5s trong vòng $< 1\text{s}$.
* [ ] Bấm nút "Xác nhận vi phạm" gọi API backend thành công, kích hoạt lệnh gửi xuống mobile.
* [ ] Bản đồ Leaflet load được vị trí xe mẫu và bảng Audit Ledger hiển thị được dữ liệu kiểm toán.

---

## 🎯 KỊCH BẢN TỔNG DUYỆT THÔNG LUỒNG (GOLDEN E2E FLOW) — 17:00 THỨ 7

Khi cả 4 thành viên ráp nối, kịch bản demo kiểm thử thông luồng sẽ diễn ra theo đúng quy trình thực tế sau:

```
[1. BUỒNG LÁI (Duẩn + Đạt)]
  - Duẩn mở App Android trước camera, nhắm mắt quá 1.5 giây.
  - Còi hú khẩn cấp vang lên tại chỗ trong vòng <= 300 ms (Offline-first cứu mạng).
  - App cắt 5s video trong RAM, xin Pre-signed URL upload lên MinIO (S3).
  - App publish gói tin MQTT 'safety/alert/XE_01' (QoS 1 kèm link clip MinIO).
       │
       ▼ (Giao thức MQTT Port 1883)
[2. HẠ TẦNG & BACKEND (Vũ + Việt)]
  - Broker EMQX nhận tin -> Backend FastAPI Ingestion tiêu thụ bản tin MQTT.
  - Backend ghi nhận sự kiện vào 'safety_alert_log' và tạo 'hitl_case'.
  - Backend lập tức bắn sự kiện qua Server-Sent Events (SSE) tới Web Dashboard.
       │
       ▼ (Realtime SSE < 500ms)
[3. WEB DASHBOARD HITL (Việt)]
  - Màn hình Web của Việt tự động bung Popup cảnh báo đỏ chớp nháy.
  - Trình duyệt phát mượt clip 5 giây tài xế vừa nhắm mắt từ MinIO.
  - Việt (vai trò Safety Manager) bấm nút: [XÁC NHẬN NGUY HIỂM - YÊU CẦU NGHỈ 30 PHÚT].
       │
       ▼ (HTTP POST /api/v1/hitl/review -> MQTT Publish)
[4. PHẢN HỒI BUỒNG LÁI (Duẩn + Vũ)]
  - Backend nhận lệnh duyệt, ghi Sổ cái, publish MQTT 'command/driver/TX_01'.
  - Điện thoại Android của Duẩn lập tức nhận lệnh, màn hình hiện popup chỉ thị dừng xe kèm 2 nút:
    [ĐỒNG Ý DỪNG NGHỈ] hoặc [KHIẾU NẠI CHÓI NẮNG].
  - Duẩn bấm [ĐỒNG Ý DỪNG NGHỈ] -> Bắn ACK qua MQTT.
  - Web Dashboard chuyển trạng thái "Tài xế đã chấp thuận dừng nghỉ".
  - Sổ cái 'safety_audit_ledger' lưu lại toàn bộ chuỗi chứng cứ với chữ ký số HMAC-SHA256 bất biến.
```

---

## 📋 MA TRẬN PHỐI HỢP KỸ THUẬT & GIAO DIỆN CHUNG (DATA CONTRACT)

Để tránh xung đột khi tích hợp vào Thứ 7, 4 thành viên thống nhất tuyệt đối các giao thức kỹ thuật sau:

| Thành phần | Giao thức / Định dạng | Chi tiết quy ước | Phụ trách chính |
| :--- | :--- | :--- | :--- |
| **MQTT Broker** | MQTT v5.0 (Port 1883) | EMQX container, cấu hình anonymous/basic auth cho dev | Việt + Vũ |
| **Topic GPS** | `telemetry/gps/{vehicleId}` | QoS 0, JSON `{lat, lng, speed, timestamp}` gửi 3s/lần | Duẩn $\rightarrow$ Vũ |
| **Topic Vi phạm** | `safety/alert/{vehicleId}` | QoS 1, JSON `{eventId, driverId, metrics, evidence}` (SAD 5.2) | Duẩn $\rightarrow$ Vũ |
| **Topic Lệnh lái** | `command/driver/{driverId}` | QoS 1, JSON `{commandId, action: "REQUIRE_REST", reason}` | Vũ $\rightarrow$ Duẩn |
| **Upload Clip 5s** | HTTP PUT Pre-signed URL | File MP4 H.264 nhẹ ($< 2\text{ MB}$), tự hủy sau 24h trên MinIO | Vũ + Duẩn |
| **Dashboard Realtime** | Server-Sent Events (SSE) | Endpoint `/api/v1/realtime/alerts` đẩy JSON alert về Web | Vũ $\rightarrow$ Việt |
| **Ngưỡng mô hình** | JSON Object config | `{ear_threshold: 0.20, mar_threshold: 0.60, perclos_window: 60}` | Đạt $\rightarrow$ Vũ + Duẩn |
