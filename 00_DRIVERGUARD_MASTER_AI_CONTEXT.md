# DRIVERGUARD — MASTER AI CONTEXT & PROJECT SPECIFICATION PACK

## Bộ Dữ Liệu Ngữ Cảnh Chuẩn Hóa Dành Cho Bất Kỳ Hệ Thống AI (LLM / Agent / Dev AI)

> **Phiên bản:** 3.0 Master Unified  
> **Mục đích:** Cung cấp đầy đủ và trọn vẹn ngữ cảnh, triết lý thiết kế, ràng buộc kỹ thuật và logic vận hành để bất kỳ AI nào (Claude, GPT-4, Gemini, DeepSeek, Cursor, Copilot) khi đọc file này đều hiểu 100% bản chất đề tài DriverGuard và định hướng triển khai chuẩn xác, không bị ảo giác hoặc lạc hướng.

---

## 1. PROJECT METADATA & ONE-SENTENCE SUMMARY

- **Tên dự án:** **DriverGuard** — Nền tảng Giám sát An toàn Tài xế & Vận hành Đội xe Thông minh.
- **Định nghĩa 1 câu (Elevator Pitch):**
  > _DriverGuard là hệ thống an toàn buồng lái ứng dụng Edge AI trên điện thoại Android để phát hiện buồn ngủ/mất tập trung và hú còi cứu mạng tức thì $\le 300\text{ ms}$ (Offline-first 100%), kết nối 100.000 xe qua MQTT siêu nhẹ và điều phối luồng can thiệp nhân văn 2 cấp có con người kiểm duyệt (Human-in-the-Loop)._
- **Quy mô mục tiêu:** 100.000 phương tiện kết nối đồng thời (Case study điển hình: Đội xe taxi điện Xanh SM tại Việt Nam).
- **Môi trường triển khai Edge:** Điện thoại Android gắn trên giá đỡ táp-lô bên phải vô lăng của tài xế.

---

## 2. NHỮNG NGUYÊN TẮC BẤT BIẾN (ARCHITECTURAL INVARIANTS)

> ⚠️ **BẤT KỲ AI NÀO KHI THẢO LUẬN HOẶC VIẾT CODE CHO DỰ ÁN NÀY ĐỀU PHẢI TUÂN THỦ 4 NGUYÊN TẮC BẤT BIẾN SAU:**

1. **AN TOÀN TẠI BIÊN LÀ TỐI THƯỢNG (EDGE FIRST - OFFLINE FIRST):**  
   Còi hú cứu mạng trong buồng lái $\le 300\text{ ms}$ là xử lý 100% nội bộ trên điện thoại (On-device). **TUYỆT ĐỐI KHÔNG** chờ máy chủ Cloud hay kết nối Internet mới phát cảnh báo.
2. **KHÔNG STREAM VIDEO 24/7 (PRIVACY & COST PRESERVATION):**  
   99% dữ liệu gửi về máy chủ là JSON metadata nhẹ (GPS, trạng thái, telemetry). **NGHIÊM CẤM** stream video liên tục lên Cloud (vì sẽ tốn > 2 triệu USD/tháng cho 100k xe và vi phạm nghiêm trọng quyền riêng tư buồng lái). Chỉ trích xuất duy nhất video clip ngắn 5 giây khi có sự cố khẩn cấp Cấp 3, tự hủy sau 24h.
3. **QUYỀN CAN THIỆP THUỘC VỀ CON NGƯỜI (HUMAN-IN-THE-LOOP - 2 TIERS):**  
   AI chỉ đóng vai trò phát hiện và đề xuất. Không có AI nào được tự động phạt tài xế. Phải qua quy trình 2 cấp: **Cán bộ quản lý an toàn (Manager) duyệt clip 5s $\rightarrow$ Tài xế có quyền Chấp thuận dừng nghỉ hoặc Khiếu nại (bị chói nắng, ngáp cơ học)**.
4. **HẠ TẦNG 100.000 XE DÙNG MQTT OVER TLS:**  
   Không dùng HTTP polling (quá nặng header) và không dùng WebSocket thô trực tiếp vào web server (cạn RAM do connection state). Sử dụng **MQTT v5 Broker (EMQX)** với 2 mức chất lượng dịch vụ:
   - **QoS 0:** Dành cho telemetry GPS định kỳ (cho phép drop khi nghẽn mạng).
   - **QoS 1:** Dành cho sự cố an toàn (bắt buộc ACK, không được mất tin).

---

## 3. RANH GIỚI PHẠM VI (SCOPE BOUNDARY: CORE MVP VS. PHASE 2)

| Tiêu chí          | CORE MVP (Trọng tâm hiện tại)                                                                                                                                                                                                                           | PHASE 2 ROADMAP (Lộ trình tương lai)                                                                                                                          |
| :---------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | :------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Phân hệ chính** | **Safety Core (An Toàn Cốt Lõi)**                                                                                                                                                                                                                       | **Smart Fleet Dispatch (Điều Phối Đội Xe)**                                                                                                                   |
| **Tính năng**     | • Giám sát AI MediaPipe on-device (20 FPS)<br>• Còi hú $\le 300\text{ ms}$ khi mắt nhắm $\ge 1.5\text{s}$<br>• Lọc 5 lớp chống báo sai (chói nắng, ngáp)<br>• Quy trình duyệt 2 cấp (HITL)<br>• MQTT Gateway cho 100k xe<br>• Audit Log lưu vết 3–5 năm | • Dự báo nhu cầu đặt xe (Demand Model)<br>• Gợi ý điều xe rảnh tới vùng nhiều khách<br>• Tối ưu quãng đường chạy rỗng<br>• Điều phối sạc pin cho xe taxi điện |
| **Lưu ý cho AI**  | **TẬP TRUNG TOÀN LỰC VÀO PHẦN NÀY**                                                                                                                                                                                                                     | Không đưa logic điều phối vào luồng cứu mạng MVP                                                                                                              |

---

## 4. BẢNG ÁNH XẠ 5 BÀI TOÁN THỰC TẾ & 5 GIẢI PHÁP KIẾN TRÚC (CAUSAL TRACEABILITY)

|  STT  | Bài toán thực tế buồng lái                                                                                 | Hậu quả nếu dùng giải pháp ngây thơ                                                                   | Giải pháp kiến trúc của DriverGuard                                                                                                                     |
| :---: | :--------------------------------------------------------------------------------------------------------- | :---------------------------------------------------------------------------------------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **1** | **Tai nạn trong tích tắc trên cao tốc**<br>Lái 90 km/h, ngủ gật 2s = xe trôi 50m. Mất sóng 4G đèo dốc.     | Đẩy ảnh lên cloud xử lý mất 3–5 giây $\rightarrow$ Xe đâm vào dải phân cách trước khi còi kịp hú.     | **Edge AI Offline-first:** Chạy MediaPipe FaceMesh C++ trực tiếp trên chip điện thoại, còi hú $\le 300\text{ ms}$.                                      |
| **2** | **Cảnh báo sai gây ức chế (False Alarms)**<br>Chói nắng nheo mắt, ngáp mỏi hàm, dừng đèn đỏ quay mặt.      | Báo động nhầm liên tục $\rightarrow$ Tài xế bực bội, dán băng keo che cam, tắt app, đình công.        | **Cửa sổ trượt 3s + Multimodal Fusion:** Đánh giá $PERCLOS_{3s}$, kết hợp $MAR$, tư thế đầu và lọc vận tốc xe $v < 5\text{ km/h}$.                      |
| **3** | **100.000 xe gửi GPS làm sập server**<br>Mỗi xe gửi GPS 3s/lần $\rightarrow$ 33.000 requests/giây.         | Dùng HTTP REST quá tải header; Dùng WebSocket sập RAM server khi 100k xe duy trì connection state.    | **MQTT over TLS trên EMQX Cluster:** Gói tin nhị phân siêu nhẹ, 100k kết nối chỉ tốn $< 2.5\text{ GB RAM}$, phân luồng QoS 0 / QoS 1.                   |
| **4** | **Bùng nổ chi phí Cloud & Bị kiện riêng tư**<br>Lưu video 12h/ngày của 100k xe tốn 1.8M GB/tháng.          | Chi phí Cloud Storage $> 2.16\text{M USD/tháng}$; Tài xế kiện vì bị camera giám sát 24/7 trong cabin. | **Buffer 5s trong RAM + S3 Auto-expire 24h:** 99% chỉ gửi JSON. Chỉ trích xuất 5s khi vi phạm Cấp 3, tự hủy sau 24h. Chi phí $< 20\text{ USD/tháng}$.   |
| **5** | **Tranh chấp phạt oan & Yêu cầu pháp lý**<br>Tài xế khiếu nại "tôi bị chói nắng"; CSGT yêu cầu trích xuất. | Hãng xe phạt oan bị kiện; Không có bằng chứng đối soát với cơ quan chức năng sau tai nạn.             | **Quy trình HITL 2 cấp + Sổ cái Audit Log:** Manager duyệt clip $\rightarrow$ Tài xế có quyền khiếu nại $\rightarrow$ Toàn bộ lưu vết bất biến 3–5 năm. |

---

## 5. THÔNG SỐ VÀ NGƯỠNG ĐỊNH LƯỢNG KỸ THUẬT (EXACT THRESHOLDS)

### 5.1. Chỉ số sinh trắc học từng khung hình (Biometric Landmarks)

- **EAR (Eye Aspect Ratio - Độ mở mắt):**
  - Bình thường: $EAR \approx 0.28 - 0.35$
  - Nhắm mắt / sụp mi: $EAR < 0.20$
- **MAR (Mouth Aspect Ratio - Độ mở miệng):**
  - Bình thường: $MAR < 0.35$
  - Mở miệng to / ngáp: $MAR > 0.60$
- **Tư thế đầu (Head Pose Angles):**
  - $Pitch \le -20^\circ$: Gục đầu xuống vô lăng.
  - $|Yaw| \ge 30^\circ$: Ngoảnh mặt khỏi hướng kính lái (đã trừ bù góc lệch táp-lô $15^\circ - 20^\circ$).

### 5.2. Cửa sổ trượt 3 giây và Điểm rủi ro (Risk Score: 0 – 100)

- **Tần số lấy mẫu:** 20 FPS (tương đương 60 frames trong cửa sổ 3 giây).
- **PERCLOS (Percentage of Eye Closure):** Tỷ lệ frames có $EAR < 0.20$ trong 60 frames gần nhất.
- **Hệ số bù trừ tốc độ GPS ($speedFactor$):**
  - Khi $v < 5\text{ km/h}$ (dừng đèn đỏ, kẹt xe): $speedFactor = 0.35$ (giảm phạt quay đầu).
  - Khi $v \ge 70\text{ km/h}$ (cao tốc): Mọi vi phạm nhân hệ số $1.5\times$.

### 5.3. Bảng 3 Cấp độ Kích hoạt (Escalation Matrix)

- **Cấp 0 (Score < 40):** Bình thường. Không ghi log, không gửi server.
- **Cấp 1 (Score 40 – 69):** Cảnh báo nhẹ tại chỗ (Beep nhẹ hoặc rung điện thoại). Không gửi server.
- **Cấp 2 (Score 70 – 84):** Vi phạm đáng ngờ. Còi hú tại cabin trong vòng $\le 300\text{ ms}$. Gửi gói tin JSON sự kiện về server qua **MQTT QoS 1**.
- **Cấp 3 (Score $\ge 85$ hoặc Cấp 2 trên cao tốc):** Vi phạm khẩn cấp. Còi hú tối đa + Giọng nói. Cắt buffer video 5 giây (RAM) upload lên S3 qua Pre-signed URL. Bắn popup khẩn cấp lên Dashboard cho Quản lý duyệt (HITL).

---

## 6. DATA CONTRACTS & SCHEMAS

### 6.1. MQTT Topic Architecture

```
telemetry/gps/{vehicleId}     --> QoS 0 (Payload: lat, lng, speed, heading, timestamp)
safety/alert/{vehicleId}      --> QoS 1 (Payload: eventId, metrics, riskScore, clipS3Url)
command/driver/{driverId}     --> QoS 1 (Payload: commandId, action: REST_MANDATORY, reason)
ack/driver/{driverId}         --> QoS 1 (Payload: commandId, status: ACCEPTED | DISPUTED)
```

### 6.2. JSON Payload Sự kiện An toàn (`safety/alert`)

```json
{
  "eventId": "evt_98f4c2e1-7b0a-4a21-9d22-12f5a8901bca",
  "eventType": "safety.drowsiness.escalation.v1",
  "occurredAt": "2026-09-28T15:30:00.120Z",
  "deviceId": "DEV_ANDROID_8841",
  "driverId": "TX_HN_1042",
  "vehiclePlate": "29E-888.99",
  "telemetry": {
    "latitude": 21.028511,
    "longitude": 105.854444,
    "speedKmh": 85.5,
    "roadType": "HIGHWAY_EXPRESSWAY"
  },
  "metrics": {
    "perclos3s": 0.55,
    "headPitchDeg": -24.5,
    "headYawDeg": 3.8,
    "riskScore": 88
  },
  "evidence": {
    "hasVideoClip": true,
    "clipDurationSec": 5,
    "clipS3Url": "https://s3.fleet.driverguard.io/clips/20260928/TX_HN_1042_evt_98f4c2e1.mp4",
    "clipExpiresAt": "2026-09-29T15:30:00.120Z"
  }
}
```

### 6.3. Bảng DDL PostgreSQL Sổ Cái Kiểm Toán (Audit Trail)

```sql
CREATE TABLE safety_audit_ledger (
    audit_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id VARCHAR(64) NOT NULL,
    driver_id VARCHAR(32) NOT NULL,
    vehicle_plate VARCHAR(16) NOT NULL,
    risk_score INT NOT NULL,
    perclos_value NUMERIC(4,3) NOT NULL,
    speed_kmh NUMERIC(5,2) NOT NULL,
    clip_s3_key VARCHAR(255),
    manager_id VARCHAR(32),
    manager_action VARCHAR(32) NOT NULL, -- CONFIRMED_VIOLATION | REJECTED_FALSE_ALARM | DISMISSED
    manager_decided_at TIMESTAMPTZ,
    driver_response VARCHAR(32),        -- ACCEPTED_REST | DISPUTED_GLARE | TIMEOUT
    driver_responded_at TIMESTAMPTZ,
    digital_signature TEXT,             -- Chữ ký số chống sửa đổi
    created_at TIMESTAMPTZ DEFAULT NOW()
);
```

---

## 7. QUY TRÌNH DUYỆT 2 CẤP (2-TIER HITL APPROVAL FLOW)

```
[Camera AI On-device]
       │
       ▼ (Score >= 85 trên cao tốc)
[Hú còi cabin <= 300ms + Cắt Clip 5s + Gửi MQTT QoS 1]
       │
       ▼
[Manager Dashboard bung Popup đỏ kèm Clip 5s]
       │
       ├────────────────────────────────────────┐
       ▼ (Cấp 1: Manager bấm BÁC BỎ)           ▼ (Cấp 1: Manager bấm XÁC NHẬN)
[Xóa Clip S3 ngay, không trừ điểm]       [Gửi lệnh dừng nghỉ qua MQTT tới xe]
                                                │
                                                ▼
                                    [Màn hình App Tài xế hiển thị 2 nút]
                                                │
                                ┌───────────────┴───────────────┐
                                ▼                               ▼
                     (Cấp 2: Bác tài ĐỒNG Ý)         (Cấp 2: Bác tài KHIẾU NẠI)
                  [Bật map tới trạm dừng nghỉ]    [Ghi chú chói nắng, chuyển]
                  [Khóa nhận cuốc 30 phút]        [phúc khảo sau ca, không phạt]
```

---

## 8. HƯỚNG DẪN PROMPT CHO BẤT KỲ AI NÀO (AI SYSTEM PROMPT TEMPLATE)

Khi bạn muốn đưa dự án này vào một cuộc trò chuyện AI mới (ChatGPT, Claude, Gemini, v.v.), hãy copy đoạn prompt ngắn sau:

```markdown
Bạn là Kiến trúc sư Hệ thống Cấp cao và Kỹ sư Trưởng AI của dự án "DriverGuard" — Hệ thống Giám sát An toàn Buồng lái & Vận hành 100.000 Xe Taxi Công nghệ (Xanh SM).

Quy tắc bất biến bạn phải luôn tuân thủ:

1. Core MVP là "Safety Core": AI MediaPipe chạy trực tiếp trên Android, còi hú cứu mạng tại chỗ <= 300ms (Offline-first 100%), không phụ thuộc mạng.
2. Tuyệt đối KHÔNG stream video 24/7 lên cloud (tiết kiệm chi phí và bảo vệ riêng tư). 99% chỉ gửi JSON metadata. Chỉ cắt buffer clip 5s khi vi phạm Cấp 3 và tự hủy sau 24h trên S3.
3. Hạ tầng 100k xe dùng MQTT over TLS (QoS 0 cho GPS, QoS 1 cho vi phạm/lệnh can thiệp), không dùng HTTP hay WebSocket thô.
4. Quản trị bằng quy trình HITL 2 cấp (Manager duyệt clip 5s -> Tài xế có quyền chấp thuận hoặc khiếu nại chói nắng).
5. Phân hệ điều phối xe rảnh (Fleet Dispatch) là Phase 2 Roadmap, không nằm trong Core MVP.

Mọi đề xuất kỹ thuật, đoạn mã hoặc tài liệu bạn đưa ra phải luôn bám sát các nguyên tắc trên và liên kết chặt chẽ với 5 bài toán thực tế buồng lái.
```
