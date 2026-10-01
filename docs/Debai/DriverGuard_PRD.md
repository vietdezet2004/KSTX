# DRIVERGUARD — PRODUCT REQUIREMENTS DOCUMENT (PRD)
## Tài Liệu Yêu Cầu Sản Phẩm Chuẩn Hóa — Phiên Bản Master v3.0

| Thuộc tính | Chi tiết |
| :--- | :--- |
| **Tên sản phẩm** | **DriverGuard** — Nền tảng Giám sát An toàn Tài xế  |
| **Phiên bản tài liệu** | **3.0 — Master Standard Edition** |
| **Phân loại** | Edge AI • Connected Fleet IoT • Human-in-the-Loop Safety |
| **Trọng tâm cốt lõi (Core MVP)** | Giám sát thị giác on-device (Offline-first $\le 300\text{ ms}$), Kết nối MQTT 100k xe, Duyệt can thiệp an toàn 2 cấp (HITL) |
| **Lộ trình mở rộng (Phase 2)** | Điều phối xe rảnh thông minh (Smart Fleet Dispatching theo Demand Model) |
| **Đối tượng áp dụng** | Đội xe taxi quy mô lớn (Điển hình: 100.000 xe điện Xanh SM tại Việt Nam) |
| **Trạng thái tài liệu** | Production-Ready / Chuẩn Hội đồng Thẩm định |

---

## 1. TỔNG QUAN VÀ TẦM NHÌN SẢN PHẨM (EXECUTIVE SUMMARY & PRODUCT VISION)

### 1.1. Tuyên bố Tầm nhìn (Vision Statement)
> **"DriverGuard được phát triển nhằm xóa bỏ hoàn toàn các vụ tai nạn thảm khốc do ngủ gật và mất tập trung trong ngành vận tải taxi, bằng cách đưa trí tuệ nhân tạo thị giác trực tiếp xuống buồng lái để cứu mạng tức thời trong $\le 300\text{ ms}$ (Offline-first), kết hợp luồng can thiệp nhân văn của con người (Human-in-the-Loop) trên nền tảng kết nối 100.000 xe siêu nhẹ."**

### 1.2. 5 Bài toán Sống còn từ Thực tế Vận hành
Hệ thống DriverGuard sinh ra để giải quyết triệt để 5 "nỗi đau" nhức nhối trong ngành taxi công nghệ:

```
┌──────────────────────────────────────────────────────────────────────────────────┐
│                   5 NỖI ĐAU THỰC TẾ & LỜI GIẢI CỦA DRIVERGUARD                   │
│                                                                                  │
│ [1. TAI NẠN TRONG TÍCH TẮC]  ──► Ngủ gật 2s trên cao tốc = Xe trôi 50m tử thần    │
│                                  LỜI GIẢI: Edge AI xử lý on-device còi hú ≤ 300ms│
│ [2. BÁO ĐỘNG SAI GÂY ỨC CHẾ] ──► Chói nắng, ngáp cơ học làm tài xế tắt ứng dụng  │
│                                  LỜI GIẢI: Cửa sổ trượt 3s + Fusion đa biến       │
│ [3. NGHẼN MẠNG 100.000 XE]   ──► HTTP/REST làm cạn kiệt RAM và sập máy chủ       │
│                                  LỜI GIẢI: MQTT over TLS, QoS 0/1 cực nhẹ        │
│ [4. BÙNG NỔ CHI PHÍ & ĐỜI TƯ]──► Lưu video 24/7 tốn 2M USD/tháng, bị kiện riêng tư│
│                                  LỜI GIẢI: 99% JSON metadata + Clip 5s tự hủy 24h│
│ [5. TRANH CHẤP & PHÁP LÝ]    ──► Bị phạt oan dẫn đến đình công và khiếu nại      │
│                                  LỜI GIẢI: Quy trình HITL 2 cấp + Audit Log 5 năm│
└──────────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. PHÂN ĐỊNH RANH GIỚI PHẠM VI (SCOPE BOUNDARY & ROADMAP)

Để đảm bảo tính khả thi tuyệt đối trong giai đoạn MVP và đáp ứng chặt chẽ các phản biện của Hội đồng Mentor, phạm vi hệ thống được phân định dứt khoát:

```
┌────────────────────────────────────────────────────────────────────────┐
│                   HỆ THỐNG DRIVERGUARD TOÀN DIỆN                       │
│                                                                        │
│  ┌─────────────────────────────────┐   ┌────────────────────────────┐  │
│  │     CORE MVP (AN TOÀN CỐT LÕI)   │   │ PHASE 2 (LỘ TRÌNH MỞ RỘNG) │  │
│  │  • Edge AI On-device (20 FPS)   │   │  • Smart Fleet Dispatch    │  │
│  │  • Cảnh báo cứu mạng ≤ 300ms   │───►│  • Demand Forecasting AI   │  │
│  │  • Duyệt an toàn 2 cấp (HITL)   │   │  • Tối ưu cuốc rỗng xe điện│  │
│  │  • Hạ tầng MQTT 100.000 xe      │   │  • Điều hướng sạc pin thông│  │
│  │  • Sổ cái Audit Log 3-5 năm     │   │    minh theo ca trực       │  │
│  └─────────────────────────────────┘   └────────────────────────────┘  │
└────────────────────────────────────────────────────────────────────────┘
```

### 2.1. Trong phạm vi Core MVP (In-Scope)
1. **Safety Core Edge Engine:** Thuật toán AI chạy trên điện thoại/Edge phát hiện 3 hành vi: Nhắm mắt kéo dài (PERCLOS), Ngoảnh mặt khỏi đường (Head Yaw), Gục đầu (Head Pitch).
2. **Cảnh báo tức thì tại chỗ (Local Immediate Alert):** Âm thanh cảnh báo/rung phát ra trong vòng $\le 300\text{ ms}$ khi thỏa ngưỡng, không cần mạng Internet.
3. **Quy trình duyệt can thiệp 2 cấp (2-Tier HITL):** Manager xem clip 5s phê duyệt lệnh can thiệp $\rightarrow$ Tài xế có quyền chấp thuận dừng xe hoặc bấm khiếu nại (bị chói nắng/nhận diện sai).
4. **Hạ tầng truyền tin MQTT over TLS:** Giao thức kết nối 100.000 xe với EMQX Broker. Tách biệt kênh GPS (QoS 0) và Sự cố an toàn (QoS 1).
5. **Chính sách dữ liệu & Bảo vệ đời tư:** 99% luồng dữ liệu chỉ truyền JSON metadata. Chỉ trích xuất clip ngắn 5 giây khi có sự cố Cấp 3, tự động xóa vĩnh viễn trên Cloud sau 24h.
6. **Audit Trail & Báo cáo Nhân quả (Causal Analytics):** Lưu vết bất biến quyết định can thiệp 3–5 năm; phân tích tương quan giữa số giờ lái xe và tỷ lệ buồn ngủ.

### 2.2. Ngoài phạm vi Core MVP (Out-of-Scope / Phase 2 Roadmap)
* **Smart Fleet Dispatching:** Không đưa việc tối ưu cuốc khách, dự báo nhu cầu vào luồng Core MVP (chuyển sang Phase 2 để tránh loãng mục tiêu an toàn).
* **Can thiệp điều khiển xe tự hành (ADAS Level 3+):** Hệ thống không can thiệp phanh, ga, vô-lăng.
* **Truyền hình ảnh/video liên tục (Live streaming 24/7):** Nghiêm cấm stream video liên tục để bảo vệ quyền riêng tư và chi phí hạ tầng.

---

## 3. CHÂN DUNG NGƯỜI DÙNG & ĐỘNG CƠ HÀNH VI (TARGET PERSONAS)

### 3.1. Chân dung 1: Bác tài Taxi Công nghệ (The Driver)
* **Đại diện:** Bác tài Nguyễn Văn A (38 tuổi), lái taxi điện ca đêm 10–12 tiếng.
* **Môi trường làm việc:** Buồng lái xe, điện thoại Android đặt trên giá đỡ taplo lệch phải. Thường xuyên đối mặt với ánh nắng rọi xiên buổi chiều và mỏi mắt về đêm.
* **Nỗi đau & E ngại:**
  * Rất sợ ngủ gật trên cao tốc gây nguy hiểm tính mạng.
  * Ghét bị AI soi mói đời tư hoặc báo động giả (như lúc đang ngáp mỏi miệng hoặc chói nắng).
  * Sợ bị hệ thống trừ điểm thi đua và phạt oan mà không có cách giải thích.
* **Yêu cầu đối với hệ thống:** Cảnh báo phải đúng lúc; còi chỉ hú to khi nguy hiểm thật; có nút bấm phản hồi/khiếu nại nếu AI báo nhầm.

### 3.2. Chân dung 2: Cán bộ An toàn Đội xe (Fleet Safety Manager)
* **Đại diện:** Chị Trần Thị B (32 tuổi), Trưởng bộ phận Giám sát An toàn Đội xe.
* **Môi trường làm việc:** Trung tâm điều hành, quản lý cụm 2.000 – 5.000 xe taxi đang lưu thông trên bản đồ số.
* **Nỗi đau & E ngại:**
  * Bị quá tải thông báo (Alert Fatigue) nếu hệ thống gửi quá nhiều cảnh báo rác.
  * Không thể ngồi xem hàng nghìn camera video trực tiếp.
  * Cần bằng chứng pháp lý rõ ràng khi yêu cầu tài xế dừng xe nghỉ ngơi để tránh tranh chấp lao động.
* **Yêu cầu đối với hệ thống:** Hệ thống tự động lọc 99% cảnh báo nhẹ; chỉ đẩy popup đỏ khi có vi phạm Cấp 3; kèm clip 5s để xem lướt qua 3 giây là ra quyết định; có sổ cái kiểm toán 3–5 năm.

---

## 4. BỘ YÊU CẦU CHỨC NĂNG CHI TIẾT (FUNCTIONAL REQUIREMENTS - FR)

### FR-01: Giám Sát Thị Giác & Trích Xuất Sinh Trắc Tại Biên (Edge Biometric Vision)
* **FR-01.1:** Ứng dụng Android chạy nền thu nhận luồng hình ảnh từ camera trước ở tốc độ ổn định $15 - 20\text{ FPS}$.
* **FR-01.2:** Sử dụng Google MediaPipe FaceMesh (chạy trên GPU/NPU di động qua TFLite C++) để trích xuất 468 điểm mốc khuôn mặt mà không gửi khung hình lên cloud.
* **FR-01.3:** Tính toán liên tục các chỉ số sinh trắc học theo từng khung hình:
  * $EAR$ (Eye Aspect Ratio): Đo độ mở mắt.
  * $MAR$ (Mouth Aspect Ratio): Đo độ mở miệng (phát hiện ngáp).
  * $Head\ Pitch$ & $Head\ Yaw$: Góc cúi gục đầu và góc ngoảnh mặt sang hai bên.
* **FR-01.4:** Bù trừ góc lệch camera taplo (Taplo Offset Calibration): Tự động trừ góc nghiêng tĩnh $\approx 15^\circ - 25^\circ$ do đặt điện thoại lệch phải tài xế.

### FR-02: Đánh Giá Rủi Cơ & Cảnh Báo Phân Cấp (Multi-modal Risk Engine & Tiered Alerts)
* **FR-02.1:** Áp dụng thuật toán Cửa Sổ Trượt 3 giây (60 frames) để tính chỉ số $PERCLOS_{3s}$ (% thời gian mắt nhắm $> 80\%$). Tuyệt đối không đánh giá rủi ro chỉ bằng 1 khung hình đơn lẻ.
* **FR-02.2:** Tích hợp bộ lọc vận tốc xe từ GPS: Giảm hệ số nhạy cảm khi xe đang dừng đèn đỏ hoặc chạy $< 5\text{ km/h}$ để tránh báo động giả lúc xe đứng yên.
* **FR-02.3:** Cơ chế phân cấp 3 mức cảnh báo nội bộ:
  * **Cấp 1 (Score 40 – 69):** Báo động nhắc nhở cục bộ bằng tiếng Beep nhẹ hoặc rung điện thoại. Không ghi nhận lỗi vi phạm về máy chủ.
  * **Cấp 2 (Score 70 – 84):** Còi hú to tại cabin trong vòng $\le 300\text{ ms}$. Gửi gói tin JSON metadata sự kiện về máy chủ qua MQTT QoS 1.
  * **Cấp 3 (Score 85 – 100):** Còi hú âm lượng tối đa kết hợp giọng nói nhắc nhở. Cắt bộ đệm 5 giây video trong RAM đẩy lên S3 qua Pre-signed URL và kích hoạt luồng duyệt HITL khẩn cấp.

### FR-03: Cơ Chế Lưu Trữ Cuốn Chiếu & Bảo Vệ Quyền Riêng Tư (Rolling Buffer & Privacy)
* **FR-03.1:** Ứng dụng duy trì bộ đệm cuốn chiếu (Circular Video Buffer) 10 giây trong bộ nhớ RAM tạm thời của điện thoại.
* **FR-03.2:** Không bao giờ ghi video liên tục vào bộ nhớ flash hay stream lên mạng.
* **FR-03.3:** Chỉ khi sự kiện Cấp 3 được kích hoạt, hệ thống mới trích xuất đoạn video 5 giây (2s trước và 3s sau thời điểm vi phạm) và mã hóa trước khi tải lên Cloud.
* **FR-03.4:** Đặt quy tắc tự động xóa (S3 Lifecycle Auto-expire) tiêu hủy vĩnh viễn video clip sau $24 - 48\text{ giờ}$.

### FR-04: Kết Nối Bền Bỉ Ngoại Tuyến & Giao Thức Đội Xe (Offline-first & MQTT Protocol)
* **FR-04.1 (Durable Outbox):** Khi xe chạy qua vùng mất sóng 4G/cao tốc, mọi sự kiện an toàn được lưu vào SQLite cục bộ. Ngay khi có kết nối trở lại, hệ thống tự động đồng bộ lên máy chủ theo thứ tự thời gian.
* **FR-04.2 (MQTT Transport):**
  * Dữ liệu GPS định kỳ (3s/lần) gửi lên topic `telemetry/gps/{vehicleId}` theo chuẩn **QoS 0** (cho phép drop gói tin khi nghẽn mạng để tiết kiệm tài nguyên).
  * Dữ liệu vi phạm an toàn gửi lên topic `safety/alert/{vehicleId}` theo chuẩn **QoS 1** (bắt buộc ACK, đảm bảo không thất lạc tin).
  * Kênh nhận lệnh can thiệp từ quản lý subscribe topic `command/driver/{driverId}` theo chuẩn **QoS 1**.

### FR-05: Quy Trình Can Thiệp Hai Cấp Nhân Văn (2-Tier HITL Approval Flow)
* **FR-05.1 (Cấp duyệt 1 - Manager):** Khi nhận cảnh báo Cấp 3, Dashboard của Quản lý tự động bung popup hiển thị clip 5s. Quản lý có 3 lựa chọn trong vòng 10 giây:
  * *Xác nhận vi phạm:* Gửi chỉ thị khuyến nghị dừng xe nghỉ ngơi tới buồng lái.
  * *Bác bỏ (False Alarm):* Đánh dấu cảnh báo sai (do chói nắng/camera rung). Video clip lập tức được xóa, không trừ điểm tài xế.
  * *Hỗ trợ khẩn cấp:* Kích hoạt cuộc gọi thoại hai chiều hoặc gửi cứu hộ.
* **FR-05.2 (Cấp duyệt 2 - Tài xế):** Khi nhận chỉ thị dừng nghỉ từ Quản lý, màn hình ứng dụng tài xế hiển thị 2 nút bấm to rõ:
  * *Chấp thuận dừng nghỉ:* Ứng dụng tự động bật bản đồ điều hướng tới Trạm dừng nghỉ/Cây xăng gần nhất; hệ thống tạm thời khóa nhận cuốc mới trong 30 phút.
  * *Khiếu nại (Dispute):* Tài xế bấm nút "Khiếu nại - Tôi bị chói nắng/vẫn tỉnh táo". Hệ thống không khóa tài xế, chuyển hồ sơ sang trạng thái phúc khảo sau ca trực.

### FR-06: Sổ Cái Kiểm Toán Bất Biến & Báo Cáo Nhân Quả (Audit Trail & Causal Analytics)
* **FR-06.1:** Mọi sự kiện an toàn, quyết định xác nhận/bác bỏ của Manager và phản hồi Chấp thuận/Khiếu nại của Tài xế đều được lưu vết bất biến trong cơ sở dữ liệu với khóa chữ ký số và timestamp. Lưu trữ tối thiểu $3 - 5\text{ năm}$ phục vụ điều tra pháp lý và bảo hiểm.
* **FR-06.2:** Cung cấp báo cáo phân tích nhân quả (Causal Safety Reports): Đo lường mối tương quan giữa số giờ lái xe liên tục với tần suất vi ngủ; phân tích điểm an toàn trung bình theo từng khung giờ trong ngày.

---

## 5. BỘ YÊU CẦU PHI CHỨC NĂNG (NON-FUNCTIONAL REQUIREMENTS - NFR)

| Mã NFR | Danh mục | Yêu cầu kỹ thuật chi tiết | Chỉ số định lượng mục tiêu |
| :--- | :--- | :--- | :--- |
| **NFR-01** | **Thời gian đáp ứng (Latency)** | Độ trễ từ lúc khuôn mặt thỏa điều kiện vi ngủ đến lúc còi hú phát ra trong cabin. | **$\le 300\text{ ms}$** (Critical for saving lives) |
| **NFR-02** | **Tính sẵn sàng (Availability)** | Khả năng tự động phát hiện và cảnh báo khi xe hoàn toàn mất mạng Internet / mất sóng 4G. | **100% Offline-first** |
| **NFR-03** | **Tốc độ xử lý (Throughput)** | Tốc độ nhận diện và trích xuất FaceMesh trên thiết bị Android tầm trung. | **$\ge 15 - 20\text{ FPS}$** ổn định |
| **NFR-04** | **Độ chính xác (Accuracy)** | Tỷ lệ phát hiện đúng các sự cố ngủ gật thực tế (Recall). | **$\ge 90\%$** trên tập benchmark |
| **NFR-05** | **Độ đặc hiệu (False Alarms)** | Tần suất phát sinh cảnh báo sai trong điều kiện lái xe bình thường. | **$\le 1\text{ lần / 30 phút}$** lái xe |
| **NFR-06** | **Chịu tải mạng (Scalability)** | Tài nguyên RAM máy chủ duy trì trạng thái 100.000 kết nối MQTT đồng thời. | **$\le 2.5\text{ GB RAM}$** trên cụm EMQX |
| **NFR-07** | **Chi phí lưu trữ (Storage Cost)** | Chi phí lưu trữ Cloud S3 cho bằng chứng video của toàn bộ 100.000 xe mỗi tháng. | **$\le 20\text{ USD / tháng}$** (Nhờ clip 5s tự hủy 24h) |
| **NFR-08** | **Bảo vệ quyền riêng tư (Privacy)** | Tuyệt đối không lưu giữ hình ảnh khuôn mặt tài xế nếu không có vi phạm nghiêm trọng. | **100% tuân thủ Privacy by Design** |
| **NFR-09** | **Tuổi thọ bằng chứng (Audit Retention)**| Thời gian lưu trữ hồ sơ số liệu kiểm toán các quyết định can thiệp an toàn. | **$3 - 5\text{ năm}$** bất biến |

---

## 6. TIÊU CHÍ NGHIỆM THU MVP (DEFINITION OF DONE & ACCEPTANCE CRITERIA)

Dự án DriverGuard MVP được coi là hoàn thành và đạt tiêu chuẩn bàn giao khi thỏa mãn toàn bộ 8 tiêu chí kiểm thử sau:
1. **Kiểm thử Offline:** Tắt hoàn toàn Wi-Fi/4G trên điện thoại Android, người thử nghiệm nhắm mắt liên tục $> 1.5\text{s}$ $\rightarrow$ Còi hú cục bộ vang lên trong buồng lái trong vòng $\le 300\text{ ms}$.
2. **Kiểm thử Bù trượt:** Đặt camera nghiêng góc $20^\circ$ trên taplo bên phải, người thử nghiệm nhìn thẳng vào kính lái $\rightarrow$ Hệ thống không phát sinh báo động nhầm.
3. **Kiểm thử Lọc dừng xe:** Xe chạy thử nghiệm dừng chờ đèn đỏ ($v = 0\text{ km/h}$), người thử nghiệm ngáp mở miệng $\rightarrow$ Hệ thống hạ điểm rủi ro, không kích hoạt còi Cấp 2 hay Cấp 3.
4. **Kiểm thử Tải ngoại tuyến (Outbox Sync):** Tạo 3 sự kiện Cấp 2 trong lúc mất mạng, sau đó bật lại 4G $\rightarrow$ 100% sự kiện được gửi tuần tự về server qua MQTT, không mất mát dữ liệu.
5. **Kiểm thử Luồng HITL 2 cấp:** Khi kích hoạt sự kiện Cấp 3 $\rightarrow$ Video clip 5s xuất hiện trên Dashboard Quản lý trong $\le 2\text{s}$; Quản lý bấm "Xác nhận vi phạm" $\rightarrow$ Điện thoại tài xế nhận lệnh yêu cầu nghỉ trong $\le 1\text{s}$.
6. **Kiểm thử Quyền khiếu nại:** Tài xế bấm "Khiếu nại - Chói nắng" trên app $\rightarrow$ Dashboard quản lý cập nhật trạng thái "Disputed", hệ thống không phạt trừ điểm tài xế.
7. **Kiểm thử Vòng đời S3:** Video clip 5s tải lên S3 được cấu hình tự động biến mất sau 24h qua S3 Lifecycle Rule.
8. **Kiểm thử Tải 100k kết nối mô phỏng:** Chạy test script duy trì 100.000 kết nối MQTT client gửi keep-alive định kỳ $\rightarrow$ Broker CPU $< 40\%$, RAM $< 3\text{ GB}$.
