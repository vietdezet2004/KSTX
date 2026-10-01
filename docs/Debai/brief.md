Brief – Hệ Thống Giám Sát Hành Trình & An Toàn Buồng Lái Thông Minh (AppDetect)
Mã đề tài: DEV-02 |
Khối: Kinh doanh & Vận hành Dịch vụ Gọi xe & Vận tải (VSF GSM) |
Ngày: 20/09/2026

Nhóm phát triển: 4 thành viên — Mobile (Android Native - Java), Backend (Spring Boot & FastAPI), Frontend Web Admin (React / Vanilla JS + CSS), AI On-Device & Edge Computing (TensorFlow Lite, Google ML Kit)

1. Problem (Vấn đề thực tế)
Các ứng dụng gọi xe và quản lý đội xe truyền thống hiện chỉ theo dõi hành trình qua định vị GPS bên ngoài xe, hoàn toàn thiếu khả năng kiểm soát hành vi và độ an toàn của tài xế bên trong buồng lái theo thời gian thực, cũng như việc đối soát vi phạm của tài xế phục vụ cho thống kê :

Nguy cơ tai nạn do thể trạng & hành vi: Tài xế chạy xe liên tục nhiều giờ đối mặt với tình trạng ngủ gật (drowsiness), ngáp ngủ mệt mỏi, mất tập trung (ngoảnh mặt quá 30 độ) hoặc chủ quan không thắt dây an toàn. Việc thiếu cảnh báo tức thời dẫn đến nguy cơ tai nạn nghiêm trọng.
Rủi ro gian lận danh tính: Tình trạng tài xế cho mượn tài khoản hoặc người khác lái thay tiềm ẩn rủi ro lớn về pháp lý và an toàn cho hành khách.
Điểm nghẽn về hạ tầng giám sát từ xa: Các giải pháp camera truyền thống (dashcam thông thường) chỉ ghi hình offline vào thẻ nhớ (chỉ xem lại sau khi tai nạn đã xảy ra). Nếu liên tục stream video 24/7 từ hàng nghìn xe về trung tâm điều hành qua mạng 4G/5G sẽ gây quá tải băng thông, chi phí viễn thông khổng lồ, đồng thời làm thiết bị trên xe bị quá nhiệt và cạn pin nhanh chóng.
2. Persona (Đối tượng người dùng)
Primary – "Tài xế công nghệ / Tài xế đường dài" (22–50 tuổi)
Đặc điểm: Di chuyển liên tục 8–10 tiếng/ngày trong môi trường đô thị ùn tắc hoặc đường trường cao tốc; dễ mệt mỏi vào các khung giờ khuya và trưa.
Nhu cầu: Cần một trợ lý ảo giám sát an toàn tự động, nhắc nhở tức thì bằng âm thanh và hình ảnh ngay trong cabin khi xuất hiện dấu hiệu ngủ gật hoặc quên cài dây an toàn, giúp tự bảo vệ bản thân và hành khách mà không tạo cảm giác phiền hà hay can thiệp thô bạo.
Secondary – "Điều phối viên & Giám sát An toàn Vận hành (Safety Dispatcher / Web Admin)"
Đặc điểm: Nhân viên trực tại trung tâm vận hành đội xe của SM.
Nhu cầu: Cần dashboard tập trung giám sát toàn bộ đội xe trên bản đồ thời gian thực, nhận thông báo cảnh báo tức thì kèm ảnh chụp bằng chứng khi có sự cố vi phạm, và có thể chủ động kích hoạt luồng video trực tiếp (On-Demand Video Feed) vào buồng lái khi cần can thiệp khẩn cấp.
3. AI Leverage (Sức mạnh từ AI Streaming — Giải pháp công nghệ mà camera truyền thống không thể làm được:

Bóc tách & phân tích hành vi buồng lái đa luồng ngay tại biên (On-Device Inference):
Sử dụng TensorFlow Lite Face Detector kết hợp Google ML Kit Face Mesh / Contour tính toán các chỉ số sinh trắc học thời gian thực: tỉ lệ mở mắt (EAR – Eye Aspect Ratio < 0.21 trong > 3s báo ngủ gật), tỉ lệ mở miệng (MAR – Mouth Aspect Ratio > 0.7 báo ngáp), góc quay đầu (Head Euler Yaw > 30° trong > 3s báo mất tập trung), và phát hiện mất mặt tài xế (> 3s).
Kết hợp mạng YOLO Person Detection (320px) phát hiện người ngồi và cơ chế crop vùng nét cao từ khung hình gốc đưa vào Seatbelt Detector (640px) nhận diện chuẩn xác trạng thái cài dây an toàn.
Nhận diện khuôn mặt xác thực tài xế đầu ca (Face Verification qua REST API) chống gian lận tài khoản.
Kiến trúc mạng Hybrid tối ưu chi phí viễn thông và độ trễ:
AI on-device xử lý khép kín tại điện thoại, chỉ gửi sự kiện cảnh báo nhẹ dạng JSON (REALTIME_ALERT) qua WebSocket (< 50ms) và tải ảnh chụp vi phạm snapshot (~30KB) qua HTTP Multipart 1-roundtrip khi có sự cố.
Video stream nén JPEG qua UDP (Port 9090, 5 FPS) chỉ kích hoạt theo yêu cầu (On-Demand) khi Dispatcher từ Web gửi lệnh START_STREAM.
Kiểm tra so sánh: Bỏ AI vs Có AI:
Bỏ AI: Doanh nghiệp phải duy trì hàng trăm nhân viên xem camera thủ công hoặc chỉ biết sự cố khi tai nạn đã xảy ra; tốn hàng chục GB dung lượng 4G/xe/ngày (chi phí viễn thông rất lớn) , thực tế không ai làm vậy .
Có AI: Phát hiện và cảnh báo âm thanh/hình ảnh chớp đỏ trong buồng lái tức thì (< 100ms); truyền cảnh báo về Server trong < 50ms; giảm hơn 90% chi phí truyền dẫn dữ liệu.
Khả thi kỹ thuật:
Pipeline chạy đa luồng với 4 ExecutorService trên Android (CameraX, Person/Seatbelt Detector, Drowsiness Detector, UI Manager), đạt tốc độ ổn định 15–20 FPS trên thiết bị Android tầm trung.
Kiểm thử mô phỏng với Mock Server (FastAPI + UDP Socket Receiver + WebSocket) đảm bảo tích hợp thông suốt 5/5 kịch bản giám sát.
4. MVP Scope — 1 User · 1 Job · 1 Outcome
1 User: Tài xế xe dịch vụ / taxi điện đang trong ca vận hành buồng lái.
1 Job: Tự động phát hiện nguy cơ mất an toàn (ngủ gật, ngáp, mất tập trung, không thắt dây an toàn) và cảnh báo tức thời cho tài xế lẫn trung tâm điều hành.
1 Outcome: Tài xế lái xe an toàn, duy trì 100% tuân thủ thắt dây, ngăn ngừa 100% rủi ro ngủ gật gây tai nạn; Trung tâm điều phối nắm bắt tức thời tình trạng đội xe kèm ảnh bằng chứng trực quan.
4 Tính năng cốt lõi giữ lại (Core MVP Features)
Xác thực khuôn mặt tài xế đầu ca (DriverVerifyActivity): Chụp ảnh khuôn mặt gửi về API xác minh danh tính trước khi cho phép vào màn hình ca lái.
Hệ thống AI giám sát buồng lái On-Device (DriverMonitorActivity): Chạy song song nhận diện Người, Dây an toàn, Ngủ gật (EAR), Ngáp (MAR), Mất tập trung (Yaw) và hiển thị cảnh báo đỏ trực quan (Red Flash Overlay) theo thứ tự ưu tiên nghiêm ngặt.
Cơ chế mạng Hybrid Realtime Alert & Telemetry: Bắn cảnh báo sự cố tức thời qua WebSocket (REALTIME_ALERT), đẩy tọa độ GPS nền mỗi 5 giây (POST /vehicle-logs), và gửi bản ghi vi phạm kèm ảnh bằng chứng snapshot (POST /violations).
Truyền video UDP On-Demand & Web Admin Dashboard: Bật/tắt stream video 5 FPS buồng lái từ xa qua lệnh WebSocket (START_STREAM / STOP_STREAM) để Dispatcher giám sát trực tiếp trên giao diện Web Dashboard.
Tính năng cắt bỏ (Out of MVP Scope / Lộ trình Future Phase 2)
Điều phối tài xế / xe rảnh thông minh (Smart Fleet Dispatching theo Demand Forecasting Model) — chuyển sang Phase 2 (Future) sau khi hoàn thiện khung an toàn MVP.
Tích hợp cảm biến vật lý trên xe qua cổng CAN bus / OBD-II
Hệ thống tổng đài gọi điện VoIP trực tiếp giữa Admin và tài xế
Nhận diện biển số xe (ALPR) hoặc camera góc rộng ngoài xe
Giả định sống còn (Crucial Assumption)
Liệu smartphone thông thường gắn trên táp-lô có duy trì được năng lực xử lý AI On-Device liên tục ở mức nhiệt độ an toàn và độ trễ dưới 100ms trong điều kiện di chuyển thực tế dưới trời nắng hay không? , có hãng xe nào bỏ ra số tiền lớn để
5. Tech Stack (Công nghệ triển khai)
AI & Edge Inference: TensorFlow Lite Runtime, Google ML Kit Face Mesh / Contour, YOLOv8/v5 Nano On-device (Person & Seatbelt Detection models).
Mobile Client: Android Native (Java, SDK 26–34), CameraX API, OkHttp 3, Java-WebSocket, UDP Datagram Socket, Google Play Services Fused Location.
Backend Services: Spring Boot / Python FastAPI, WebSockets (ws://...:8080/ws/realtime), UDP Datagram Server (Port 9090), PostgreSQL / H2 Database, OpenAPI / Swagger 3.0.
Web Admin Portal: HTML5, CSS3, JavaScript / React, MJPEG Stream Viewer, Leaflet / Mapbox OpenStreetMap.
Testing & Tooling: Mock Server Uvicorn + UDP Stream Receiver, Postman, Android Studio Profiler.

Dữ liệu đo đạc
Pain point xử lí
