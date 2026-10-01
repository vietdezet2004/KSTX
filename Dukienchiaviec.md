# KẾ HOẠCH PHÂN CHIA CÔNG VIỆC DỰ ÁN DRIVERGUARD (DEV-02)

## Nền Tảng Giám Sát An Toàn Tài Xế & Buồng Lái Thông Minh (Connected Fleet IoT & Edge AI)

> **Nhóm thực hiện:** Team P-136  
> **Cơ cấu phân vai chuẩn hóa:**
>
> - **NGUYỄN CÔNG DUẨN (Team Lead):** Phụ trách **Mobile App (Android Native)** & Quản trị Tiến độ Dự án
> - **ĐẠT:** Phụ trách **Model AI (Edge AI, On-device Inference & Evaluation Benchmark)**
> - **PHÙNG QUỐC VIỆT:** Phụ trách **Frontend (Web Admin HITL Dashboard & UI/UX)**
> - **HOÀNG VŨ:** Phụ trách **Backend Platform (FastAPI / Spring Boot, CSDL, MQTT Broker & DevOps)**
>
> **Phiên bản tài liệu:** 3.1 — Master Team Allocation  
> **Căn cứ tài liệu kỹ thuật:**
>
> - [Master PRD v3.0](file:///docs/Debai/DriverGuard_PRD.md) (5 nỗi đau buồng lái, Scope Core MVP & NFRs)
> - [Architecture SAD v3.0](file:///docs/Debai/DriverGuard_SAD.md) (C4 Model, Kiến trúc MQTT over TLS, Luồng duyệt 2 cấp HITL)
> - [Database Design v3.0](file:///docs/Debai/DATABASE_DESIGN_DRIVERGUARD.md) (Schema 8 bảng tối ưu, Audit Ledger WORM, Config ngưỡng động)
> - [Risk Scorecard v3.0](file:///docs/Debai/Risk-score-v2.md) (Công thức $R(t)$, $K_{\text{bối cảnh}}$, 4 vùng màu Xanh/Vàng/Cam/Đỏ)
> - [Biên bản Mentor Private 2](file:///Mentor_require.md) (Evaluation người Việt, thông luồng thứ Tư, không hard-code ngưỡng)
> - [Checklist 10 Deliverables AI20K](file:///README.md)

---

## 1. ĐÁNH GIÁ ĐỘ CÂN BẰNG KHỐI LƯỢNG (WORKLOAD BALANCE MATRIX)

Sơ đồ phân chia mới phân tách độc lập theo 4 tầng kiến trúc chuyên biệt, đảm bảo không chồng chéo trách nhiệm và cân bằng tuyệt đối **~25% effort / thành viên**:

```
┌────────────────────────────────────────────────────────────────────────────────────────┐
│                      MA TRẬN ĐÁNH GIÁ CÂN BẰNG TẢI (WORKLOAD MATRIX)                   │
├──────────────┬────────────────────────┬─────────────┬──────────────┬───────────────────┤
│ Thành viên   │ Phân vai & Trọng tâm   │ Độ khó KT   │ Tỷ lệ tải    │ Deliverables AI20K│
├──────────────┼────────────────────────┼─────────────┼──────────────┼───────────────────┤
│ DUẨN (Lead)  │ Mobile App (Android)   │ ★★★★☆ (Cao) │ 25% (Chuẩn)  │ #1, #6, #7, #9    │
│ ĐẠT          │ Model AI & Benchmark   │ ★★★★☆ (Cao) │ 25% (Chuẩn)  │ #1, #10           │
│ VIỆT         │ Frontend Web Admin UI  │ ★★★☆☆ (Khá) │ 25% (Chuẩn)  │ #1, #2, #8        │
│ VŨ           │ Backend & DevOps Cloud │ ★★★★☆ (Cao) │ 25% (Chuẩn)  │ #1, #3, #4, #5    │
└──────────────┴────────────────────────┴─────────────┴──────────────┴───────────────────┘
```

---

## 2. BẢNG PHÂN CÔNG NHIỆM VỤ CHI TIẾT THEO TỪNG THÀNH VIÊN

### 👤 1. NGUYỄN CÔNG DUẨN — Team Lead & Mobile Client Lead
>
> **Sứ mệnh:** Xây dựng ứng dụng di động buồng lái (Android Native), quản trị luồng cảnh báo cứu mạng tức thì tại chỗ $\le 300\text{ ms}$ (Offline-first), tích hợp mạng MQTT và giữ nhịp tiến độ toàn đội.

| Nhóm việc | Hạng mục công việc chi tiết | Thư mục / File sở hữu | Output bàn giao |
| :--- | :--- | :--- | :--- |
| **Android App Buồng Lái** | • Phát triển ứng dụng Android Native (Java/Kotlin, SDK 26–34).<br>• Cấu hình **CameraX API** thu nhận luồng hình ảnh camera trước tốc độ ổn định 15–20 FPS.<br>• Nhúng pipeline xử lý hình ảnh, tích hợp engine Model AI từ Đạt chuyển sang.<br>• Quản trị vòng đời ứng dụng xe (Foreground Service), chống crash khi chạy nền đường dài. | `mobile/` | App Android khởi động mượt mà, render camera preview 15–20 FPS liên tục. |
| **Cảnh báo tức thì $\le 300\text{ ms}$** | • Cài đặt cơ chế phát cảnh báo âm thanh và rung đa cấp (Offline-first):<br>  - Vùng Vàng ($R = 30-59$): Rung nhẹ + âm chime nhắc nhở.<br>  - Vùng Cam ($R = 60-79$): Còi hú cabin 75dB.<br>  - Vùng Đỏ ($R = 80-100$): Còi hú cực đại $100\text{ dB}$ kèm giọng nói *"Bác tài buồn ngủ, tấp xe dừng ngay!"*.<br>• Đảm bảo độ trễ từ lúc model phát hiện đến lúc còi hú $\le 300\text{ ms}$. | `mobile/` (Audio & Notification) | Kích hoạt còi hú tức thì trong buồng lái ngay cả khi bật Airplane Mode (ngắt mạng hoàn toàn). |
| **RAM Video Buffer & Network** | • Duy trì bộ đệm tròn cuốn chiếu (Circular Video Buffer) 10 giây trong RAM.<br>• Khi sự cố Cấp 3 kích hoạt: trích xuất video 5s (2s trước + 3s sau), nén MP4/JPEG và upload lên Cloud qua Pre-signed URL do Backend (Vũ) cấp.<br>• Tích hợp MQTT Client (Paho Android): bắn GPS định kỳ topic `telemetry/gps/{vehicleId}` (QoS 0) và Sự cố an toàn `safety/alert/{vehicleId}` (QoS 1).<br>• Cài đặt SQLite Durable Outbox lưu tạm khi mất sóng 4G, tự động đồng bộ khi có mạng. | `mobile/` (Storage & Network) | Luồng upload clip 5s tự động và đồng bộ ngoại tuyến hoạt động trơn tru. |
| **Giao diện Tài xế (HITL Cấp 2)** | • Màn hình ca lái: viền màn hình đổi màu động theo vùng rủi ro (Xanh $\rightarrow$ Vàng $\rightarrow$ Cam $\rightarrow$ Đỏ chớp).<br>• Màn hình tiếp nhận lệnh dừng nghỉ từ Manager: hiển thị 2 nút bấm to bản:<br>  - **Chấp thuận dừng nghỉ:** tự động mở bản đồ dẫn đường trạm nghỉ, khóa nhận cuốc 30 phút.<br>  - **Khiếu nại (Dispute):** bấm xác nhận chói nắng/vẫn tỉnh táo để phúc khảo sau ca. | `mobile/` (UI/UX) | Giao diện buồng lái thân thiện, dễ thao tác an toàn khi đang lái xe. |
| **Quản trị Tiến độ (Lead)** | • Điều phối các mốc check-in, đặc biệt mốc **Check-in Thứ Tư** với Mentor.<br>• Chịu trách nhiệm Deliverable #6 (**Video Demo**) và Deliverable #7 (**Slide Pitch Deck**), phân vai kịch bản thuyết trình bảo vệ luận điểm Roadmap phần cứng. | `presentation/`, `WORKLOG.md` | Bộ slide và video demo buồng lái thuyết phục trước Hội đồng. |

---

### 👤 2. ĐẠT — Model AI & Edge Inference / Evaluation Benchmark Lead
>
> **Sứ mệnh:** Chịu trách nhiệm về "trái tim thuật toán" của dự án — mô hình thị giác biên on-device và hoàn thành **bộ chỉ số bắt buộc Evaluation Report trên dữ liệu người Việt** theo chỉ đạo của Mentor.

| Nhóm việc | Hạng mục công việc chi tiết | Thư mục / File sở hữu | Output bàn giao |
| :--- | :--- | :--- | :--- |
| **Thị giác Máy tính Biên (Edge AI)** | • Triển khai **Google MediaPipe FaceMesh** chạy tối ưu trên GPU/NPU di động qua TensorFlow Lite C++.<br>• Trích xuất 468 điểm mốc khuôn mặt, tính toán liên tục các chỉ số sinh trắc học:<br>  - $EAR$ (Eye Aspect Ratio): phát hiện nhắm mắt vi ngủ.<br>  - $MAR$ (Mouth Aspect Ratio): phát hiện ngáp sâu $\ge 2.0\text{s}$.<br>  - $Head\ Yaw$ & $Pitch$: đo góc nghiêng/quay đầu (ngoảnh mặt quá $30^\circ$, cúi nhìn điện thoại, gục đầu).<br>• Xây dựng thuật toán bù trừ góc lệch giá đỡ táp-lô (*Taplo Offset Calibration* $15^\circ - 25^\circ$) để tránh nhận diện nhầm khi đặt điện thoại lệch phải. | `src/models/`, `mobile/` (AI Core) | Module trích xuất sinh trắc học chạy ổn định $\ge 15 - 20\text{ FPS}$ với độ trễ thấp. |
| **Dynamic Risk Engine On-Device** | • Cài đặt thuật toán Cửa sổ trượt 3 giây (60 frames) tính chỉ số $PERCLOS_{3s}$ (% mắt nhắm $> 80\%$).<br>• Lập trình công thức tích lũy rủi ro thời gian thực $R(t)$ theo [Risk-score-v2.md](file:///docs/Debai/Risk-score-v2.md):<br>  $$R(t) = \max\Big(0, \, \min\big(100, \, R(t - 1) + \Delta R_{\text{vi phạm}} \times K_{\text{bối cảnh}} - \Delta R_{\text{hạ nhiệt}}\big)\Big)$$<br>• Tích hợp hệ số $K_{\text{bối cảnh}}$ (vận tốc GPS, khung giờ trưa/đêm, thời gian lái liên tục) và logic hạ nhiệt cooldown (giữ 30s, hạ tự nhiên hoặc reset khi dừng nghỉ). | Thuật toán trong `Risk-score-v2.md` | Engine tính điểm rủi ro chính xác theo từng giây, chống báo động ảo khi kẹt xe. |
| **Xây dựng Dataset Người Việt** | • Thu thập và gán nhãn thủ công (Ground Truth) bộ dữ liệu tối thiểu **100 – 300 khuôn mặt người Việt** theo yêu cầu bắt buộc của Mentor:<br>  - Ban ngày nắng gắt, chói nắng hắt xiên qua kính lái.<br>  - Ban đêm buồng lái tối, điều kiện ánh sáng yếu.<br>  - Người có mắt một mí, mắt híp tự nhiên (chống false positive).<br>  - Người đeo kính cận, kính râm. | `eval/data/` | Dataset chuẩn người Việt kèm file nhãn chú thích (annotations) chi tiết. |
| **Evaluation Report Khoa Học (Deliverable #10)** | • Thực nghiệm đo đạc mô hình trước khi tinh chỉnh để lấy chỉ số baseline.<br>• Tinh chỉnh hyperparameter và ngưỡng phát hiện ($EAR, MAR, Yaw$) trên dataset người Việt.<br>• Đo đạc và lập bảng so sánh trước/sau:<br>  - **Precision, Recall, F1-Score** cho từng hành vi vi phạm (mục tiêu F1 $\ge 90\%$).<br>  - Đo đạc tỷ lệ báo động giả (False Positive Rate $\le 1$ lần/30 phút lái xe) theo NFR-05.<br>• Hoàn thiện báo cáo khoa học tại `eval/results/report.md`. | `eval/results/report.md` | Deliverable #10 hoàn tất: Tài liệu đánh giá khoa học chuẩn mực, sẵn sàng phản biện ban giám khảo. |

---

### 👤 3. PHÙNG QUỐC VIỆT — Frontend Lead (Web Admin HITL Dashboard & UI/UX)
>
> **Sứ mệnh:** Xây dựng trung tâm điều hành đội xe thời gian thực cho Cán bộ An toàn (Fleet Safety Manager), quy trình duyệt can thiệp 2 cấp (HITL) và tối ưu trải nghiệm người dùng toàn diện.

| Nhóm việc | Hạng mục công việc chi tiết | Thư mục / File sở hữu | Output bàn giao |
| :--- | :--- | :--- | :--- |
| **Bản đồ Giám sát Đội xe Realtime** | • Xây dựng Web Dashboard (React / Vite hoặc Next.js + TailwindCSS).<br>• Tích hợp bản đồ số (Leaflet / Mapbox OpenStreetMap) theo dõi vị trí toàn bộ đội xe thời gian thực.<br>• Hiển thị marker xe kèm mã màu động tương ứng 4 vùng rủi ro: Xanh (An toàn), Vàng (Cảnh giác), Cam (Nguy cơ cao), Đỏ (Nguy cấp khẩn cấp).<br>• Bộ lọc thông minh: lọc xe theo vùng rủi ro, theo đội xe hoặc tìm kiếm nhanh theo biển số xe/tên tài xế. | `web/src/components/map/`, `web/src/pages/` | Bản đồ mượt mà, cập nhật trạng thái xe realtime qua kết nối WebSocket/SSE từ backend. |
| **Quy trình Duyệt Can Thiệp (HITL Cấp 1)** | • **Modal Popup Báo Động Đỏ:** Khi nhận cảnh báo Vùng Đỏ (Cấp 3) từ backend, tự động bung popup kèm chuông cảnh báo khẩn cấp.<br>• **Trình phát Video Clip 5s:** Tích hợp video player phát ngay đoạn clip 5s bằng chứng vi phạm buồng lái từ S3.<br>• **Bảng thông số vi phạm:** Hiển thị tức thời chỉ số $EAR, MAR$, góc quay đầu, vận tốc xe lúc vi phạm, thời gian lái liên tục trong ca.<br>• **Bộ 3 nút hành động cho Manager:**<br>  1. *Xác nhận vi phạm:* Bấm gửi chỉ thị yêu cầu tài xế dừng xe nghỉ ngơi.<br>  2. *Bác bỏ (False Alarm):* Đánh dấu báo động sai (do chói nắng) $\rightarrow$ lệnh hệ thống tự hủy clip và không trừ điểm tài xế.<br>  3. *Hỗ trợ khẩn cấp:* Kích hoạt cảnh báo gọi điện cứu hộ. | `web/src/components/hitl/` | Luồng duyệt can thiệp hoàn tất trong vòng 10 giây, thao tác chuẩn xác. |
| **Màn hình Cấu hình Ngưỡng Động** | • Xây dựng giao diện **Cấu hình ngưỡng an toàn (`safety_threshold_config`)** theo đúng yêu cầu Mentor (tuyệt đối không hard-code):<br>  - Slider điều chỉnh ngưỡng $EAR$ nhắm mắt (0.18 – 0.25).<br>  - Slider điều chỉnh ngưỡng $MAR$ ngáp (0.60 – 0.80).<br>  - Cấu hình ngưỡng tốc độ cao tốc ($70\text{ km/h}$) và thời gian lưu trữ clip (1 ngày ở MVP, 30–90 ngày ở Production).<br>  - Lưu trực tiếp cấu hình về backend qua REST API. | `web/src/pages/settings/` | Manager có thể tinh chỉnh ngưỡng cảnh báo linh hoạt ngay trên giao diện web. |
| **Sổ Cái Kiểm Toán & Báo Cáo Nhân Quả** | • Màn hình tra cứu **Sổ cái kiểm toán bất biến (Audit Ledger)** 3–5 năm phục vụ thanh tra pháp lý/bảo hiểm.<br>• Giao diện báo cáo nhân quả (Causal Safety Charts): Biểu đồ tương quan giữa số giờ lái xe liên tục và tỷ lệ xuất hiện vi ngủ buồng lái. | `web/src/pages/reports/` | Báo cáo trực quan, số liệu minh bạch, đáp ứng chuẩn kiểm toán. |
| **Tài liệu & Hồ sơ Dự án** | • Chịu trách nhiệm Deliverable #2: Chuyển đổi và hoàn thiện [README.md](file:///c:/Users/Phung%20Quoc%20Viet/Desktop/AI_in_Action/Detect_APP/README.md) từ boilerplate thành README chuẩn mực dự án DriverGuard.<br>• Cập nhật và duy trì Deliverable #8: [JOURNAL.md](file:///c:/Users/Phung%20Quoc%20Viet/Desktop/AI_in_Action/Detect_APP/JOURNAL.md) (Nhật ký phát triển kỹ thuật). | `README.md`, `JOURNAL.md` | Bộ tài liệu dự án chuyên nghiệp, đúng nhận diện thương hiệu DriverGuard. |

---

### 👤 4. HOÀNG VŨ — Backend Platform & Cloud DevOps Lead
>
> **Sứ mệnh:** Xây dựng hạ tầng dữ liệu và trung tâm dịch vụ kết nối 100.000 xe, tối ưu broker MQTT, triển khai AI Escalation Agent và tự động hóa toàn bộ quy trình DevOps đám mây.

| Nhóm việc | Hạng mục công việc chi tiết | Thư mục / File sở hữu | Output bàn giao |
| :--- | :--- | :--- | :--- |
| **Thiết Kế & Cài Đặt CSDL (PostgreSQL 16+)** | • Setup CSDL PostgreSQL 16+ theo bản thiết kế chuẩn [DATABASE_DESIGN_DRIVERGUARD.md](file:///docs/Debai/DATABASE_DESIGN_DRIVERGUARD.md).<br>• Triển khai đầy đủ 8 bảng Core MVP:<br>  - `account`, `staff`, `vehicle`, `driver`<br>  - `vehicle_log` (tối ưu khóa BIGINT, partitioning theo tháng phục vụ 33.000 msg/s)<br>  - `safety_alert_log` (lưu chi tiết $EAR, MAR, Yaw$, điểm rủi ro, S3 key, idempotency qua `event_id`)<br>  - `hitl_case` (quản lý trạng thái duyệt 2 cấp)<br>  - `safety_threshold_config` (lưu các bộ ngưỡng động)<br>• Cài đặt bảng WORM `safety_audit_ledger`: tạo Database Trigger ngăn chặn tuyệt đối lệnh `UPDATE`/`DELETE`, băm chữ ký số HMAC/SHA-256 bảo đảm tính bất biến 3–5 năm. | `src/models/`, `db/migration/` | Schema CSDL hoàn chỉnh 100%, sẵn sàng cho mốc **Check-in Thứ Tư**. |
| **MQTT Broker & Ingestion Service** | • Cấu hình cụm MQTT Broker (EMQX / Mosquitto over TLS Port 8883) hỗ trợ 100.000 kết nối đồng thời.<br>• Xây dựng Telemetry Ingestion Worker tiêu thụ 2 luồng tin nhắn:<br>  - `telemetry/gps/{vehicleId}`: QoS 0 (chấp nhận drop gói tin khi nghẽn mạng để tiết kiệm RAM)<br>  - `safety/alert/{vehicleId}`: QoS 1 (bắt buộc ACK, đảm bảo không thất lạc sự cố)<br>• Cài đặt logic Idempotency Key lọc trùng lặp khi xe từ vùng mất sóng 4G gửi dồn log về. | `src/services/mqtt.py`, `src/services/ingestion.py` | Pipeline Ingestion vận hành ổn định, tiêu thụ mượt mà dữ liệu giả lập. |
| **AI Safety Escalation Agent & APIs** | • Xây dựng Safety Escalation Agent bằng LangGraph / FastAPI.<br>• Phân tích rủi ro đa biến: kết hợp điểm $R(t)$, số lần vi phạm trong ca, thời gian lái liên tục và tốc độ GPS $\rightarrow$ gợi ý tự động phương án xử lý cho Manager.<br>• Xây dựng hệ thống REST API chuẩn OpenAPI/Swagger cho Mobile và Web.<br>• Tích hợp WebSocket / SSE Hub đẩy sự cố khẩn cấp tức thì lên Web Admin của Việt.<br>• Cấp Pre-signed URL cho Mobile upload clip 5 giây lên S3/MinIO và tạo Janitor tự hủy clip sau 24h. | `src/agents/`, `src/api/routes.py`, `src/services/` | Agent vận hành chính xác, tài liệu Swagger `/docs` đầy đủ. |
| **DevOps, Docker & Cloud Deploy (Deliverable #5)** | • Viết `Dockerfile` tối ưu multi-stage, `docker-compose.yml` khởi chạy trọn cụm (Backend, PostgreSQL, EMQX Broker).<br>• Cấu hình GitHub Actions CI chạy kiểm tra `ruff` linting và `pytest` với độ bao phủ unit test $\ge 60\%$.<br>• Triển khai backend lên Cloud (Render / Railway / Fly.io) với domain thật, kết nối HTTPS/WSS công khai (**Live URL** - Deliverable #5). | `Dockerfile`, `docker-compose.yml`, `.github/workflows/` | Deliverable #5 hoàn tất: Hệ thống backend chạy ổn định 24/7 trên môi trường Production. |

---

## 3. MA TRẬN PHỐI HỢP & GIAO TIẾP LIÊN TẦNG (CROSS-LAYER HANDSHAKE)

```
        ┌────────────────────────────────────────────────────────┐
        │                  MA TRẬN GIAO THƯƠNG                   │
        ├──────────────────────┬─────────────────────────────────┤
        │ Giao diện kết nối    │ Hai thành viên chịu trách nhiệm │
        ├──────────────────────┼─────────────────────────────────┤
        │ AI Model ◄──Pipeline──► Mobile │ ĐẠT (AI)   ◄────────► DUẨN (Mobile)│
        │ Mobile ◄──MQTT/S3──► Backend   │ DUẨN (App) ◄────────► VŨ (Backend) │
        │ Backend ◄──WS/REST──► Web UI   │ VŨ (BE)    ◄────────► VIỆT (FE)    │
        │ Web UI ◄──Config/Eval──► AI    │ VIỆT (FE)  ◄────────► ĐẠT (AI)     │
        │ DevOps ◄──Deploy CI/CD──► Toàn bộ│ VŨ (DevOps)◄────────► CẢ NHÓM   │
        └──────────────────────┴─────────────────────────────────┘
```

1. **Giữa Đạt & Duẩn (Model AI $\longleftrightarrow$ Mobile App):**
   - Đạt đóng gói model MediaPipe FaceMesh & thuật toán $PERCLOS_{3s} / R(t)$ thành thư viện/class gọn gàng; Duẩn nhúng vào Android Native và nối luồng CameraX.
   - Duẩn hỗ trợ Đạt test thuật toán trực tiếp trên camera điện thoại để thu thập dữ liệu benchmark.
2. **Giữa Duẩn & Vũ (Mobile App $\longleftrightarrow$ Backend & MQTT):**
   - Thống nhất payload JSON gói tin `REALTIME_ALERT` gửi qua MQTT QoS 1.
   - Thống nhất API xin Pre-signed URL để upload video 5 giây lên S3.
3. **Giữa Vũ & Việt (Backend $\longleftrightarrow$ Web Frontend Admin):**
   - Vũ thiết lập kênh WebSocket/SSE đẩy sự cố đỏ; Việt bắt sự kiện để kích hoạt popup chuông báo động trên Web.
   - Vũ cung cấp các REST API cho Manager duyệt vi phạm (`CONFIRMED`, `REJECTED`, `EMERGENCY`) và API cập nhật ngưỡng động.
4. **Giữa Việt & Đạt (Web Config $\longleftrightarrow$ Model AI Parameters):**
   - Việt thiết kế giao diện cấu hình ngưỡng động ($EAR, MAR$, thời gian lưu clip); Đạt đảm bảo thuật toán nhận diện tôn trọng các tham số cấu hình này từ database.

---

## 4. TIẾN ĐỘ THỰC HIỆN THEO CÁC MỐC QUAN TRỌNG

```
Tuần 1 (Hiện tại)                  Thứ Tư (Check-in Mentor)         Cuối Tuần 1 (Pre-MVP)            Tuần 2 (Demo Day)
──────┬──────────────────────────────────────┬────────────────────────────────┬───────────────────────────►
      │                                      │                                │
      ▼                                      ▼                                ▼
[Setup & Phân rã]                     [THÔNG LUỒNG HỆ THỐNG]           [HOÀN THIỆN CORE FEATURES]       [BENCHMARK & DEMO DAY]
• Vũ: Setup DB + MQTT Broker          • DB 8 bảng hoạt động            • Duẩn: Mobile còi hú ≤ 300ms    • Đạt: Chốt F1-score ≥ 90%
• Duẩn: Setup Android CameraX         • Mobile bắn tin MQTT giả lập    • Việt: Web duyệt HITL clip 5s   • Vũ: Chốt Live URL Cloud
• Đạt: Xây dựng FaceMesh & Dataset    • Web nhận popup cảnh báo        • Vũ: Safety Agent hoàn tất      • Duẩn: Video Demo + Slide
• Việt: Khung Web + Bản đồ            • Báo cáo tiến độ Mentor         • Tích hợp thông suốt 5 luồng    • Cả nhóm: Tổng duyệt
```

---

## 5. QUY CHUẨN LÀM VIỆC & KỶ LUẬT ĐỘI NGŨ

1. **Tuân thủ kỷ luật kiến trúc:** Nghiêm cấm tự ý thay đổi cấu trúc CSDL, chuyển đổi giao thức mạng hay thay đổi logic chấm điểm mà không thông báo trong nhóm và xin ý kiến Mentor.
2. **Duy trì ghi chép AI Log:** Cả 4 thành viên bắt buộc kiểm tra hook ghi log AI (`setup_hooks.ps1` hoặc `setup_hooks.sh`) hoạt động tốt trên máy cá nhân trước khi push code để bảo vệ điểm quá trình.
3. **Commit theo chuẩn Conventional Commits:**
   - `feat(mobile): ...` (Duẩn)
   - `feat(ai): ...` hoặc `eval: ...` (Đạt)
   - `feat(web): ...` (Việt)
   - `feat(backend): ...` hoặc `chore(devops): ...` (Vũ)
