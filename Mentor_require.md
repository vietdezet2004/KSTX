
# Mentor Private 2 – Review kỹ thuật

**Thời gian:** 11 giờ tối  
**Hình thức:** Mentor Private, hỗ trợ on-demand ngoài giờ (tiếp nối MentorPrivate1)  
**Nhóm:** Đề tài giám sát tài xế, phát hiện buồn ngủ (nhóm từ MD01–MD03)  
**Bối cảnh:** Nhóm trình bày lại toàn bộ luồng hệ thống sau khi chuẩn bị theo yêu cầu từ buổi trước.

## Tóm tắt

- **Sản phẩm cốt lõi:** Giám sát tài xế, phát hiện buồn ngủ hoặc mất tập trung và cảnh báo theo thời gian thực để đảm bảo an toàn.
- **Hướng mở rộng:** Dùng dữ liệu hành vi để tạo báo cáo định kỳ và đề xuất điều phối chuyến đi. Ví dụ: khi tài xế mệt, điều tài xế khác đến khu vực có nhu cầu cao.
- **Góp ý cần ưu tiên:** Thiết kế ngưỡng cảnh báo linh hoạt; lưu đầy đủ log; chuẩn bị Evaluation Report trên dữ liệu người Việt; trình bày rõ roadmap phần cứng.
- **Mốc gần nhất:** Check-in vào thứ Tư về tiến độ thông luồng hệ thống và tình trạng setup database.

## 1. Phát hiện và cảnh báo

### Nhóm đề xuất

- Hai hành vi cần theo dõi: gục đầu và nhắm mắt quá lâu.
- Sau 3 lần vi phạm trong khung thời gian, cảnh báo trực tiếp trên thiết bị; đến lần thứ 4 thì gửi log về server.
- Log gồm GPS, timestamp, loại vi phạm và liên kết video clip 5–10 giây trên cloud để admin xem xét.

### Mentor góp ý

- Ngưỡng 3 lần trong 5 phút quá thấp: ngáp hoặc gật đầu vài lần trong chuyến đi dài chưa chắc đã là tình huống nguy hiểm.
- Không hard-code một ngưỡng duy nhất. Ngưỡng cần linh hoạt và có thể cấu hình.
- Tách thành hai luồng:
	- **Ghi nhận và hiển thị trên dashboard:** ngưỡng thấp hơn để theo dõi được nhiều hành vi.
	- **Yêu cầu admin hành động:** ngưỡng cao hơn; chỉ gửi push notification/mention khi tình huống thực sự đáng chú ý.
- Ví dụ được gợi ý: chỉ escalate khi tài xế ngáp hơn 10 lần trong 30 phút.

> Chia ngưỡng linh hoạt để người dùng nắm được tình trạng tài xế, đồng thời chỉ yêu cầu can thiệp khi có nguy cơ thực sự.

## 2. Lưu trữ dữ liệu

### Đề xuất ban đầu của nhóm

| Loại dữ liệu | Thời gian lưu | Mục đích |
| --- | --- | --- |
| Video clip 5–10 giây | 1 ngày rồi xóa | Để admin xác nhận sự việc tại thời điểm xảy ra |
| Audit log (timestamp, GPS, loại vi phạm, confidence từng lần) | Lâu dài | Lập báo cáo tháng và phân tích về sau |

### Mentor góp ý

- Lưu video 1 ngày có thể chấp nhận ở MVP/demo. Khi chạy production, thời gian lưu cần cấu hình được, ví dụ 30 hoặc 90 ngày, để phục vụ audit.
- Mentor lưu ý rằng tài xế đã đồng ý điều khoản giám sát khi ký hợp đồng, nên có cơ sở để lưu dữ liệu lâu hơn.
- Nên gửi lên server đầy đủ cả 3 log trước đó, không chỉ log ở lần thứ 4. Mỗi log cần có timestamp và confidence.
- Về lâu dài, dữ liệu có thể hỗ trợ phân tích trạng thái tinh thần, năng suất làm việc và mức độ hài lòng nghề nghiệp của tài xế.

## 3. Gửi dữ liệu từ thiết bị lên server

### Giao thức

- Nhóm chọn **MQTT** thay cho WebSocket vì MQTT nhẹ, phù hợp với thiết bị IoT/embedded.
- Nhóm nêu lý do WebSocket có thể khó đáp ứng 100.000 kết nối liên tục; mentor đồng ý với lựa chọn MQTT.

### Khi thiết bị mất mạng

- Cảnh báo vẫn chạy trực tiếp trên thiết bị, không phụ thuộc kết nối mạng.
- Log được lưu cục bộ; khi có mạng trở lại, thiết bị tự đồng bộ log lên server.
- Mentor đánh giá hướng xử lý này ổn.

### Dữ liệu hình ảnh/video

- Nhóm hiện gửi video clip thay vì ảnh đơn.
- Mentor gợi ý lấy nhiều frame liên tiếp, khoảng 2–3 frame/giây, thay vì chỉ detect trên một ảnh để tăng độ chính xác và giảm false positive.

## 4. Dữ liệu và đánh giá model

### Hiện trạng

- Nhóm dùng MediaPipe để nhận diện khuôn mặt.
- Model được train trên dữ liệu nước ngoài nên có thể sai số khi áp dụng cho người Việt, do khác biệt về mắt và đường nét khuôn mặt.
- Một số trường hợp chưa xử lý tốt: nheo mắt do nắng và điều kiện ánh sáng yếu.

### Yêu cầu Evaluation Report

Mentor nhấn mạnh đây là nội dung bắt buộc cho demo cuối. Ban giám khảo có thể hỏi các chỉ số **Precision, Recall và F1-score**; thiếu evaluation test sẽ bị trừ điểm nặng.

Quy trình được góp ý:

1. Chạy model trước khi fine-tune để lấy kết quả baseline.
2. Fine-tune hoặc điều chỉnh hyperparameter.
3. Chạy lại model và so sánh kết quả trước/sau.
4. Đánh giá trên tập ảnh người Việt có nhãn thủ công, sau đó so sánh nhãn với output của model để tính Precision, Recall và F1-score.

Quy mô và điều kiện dữ liệu:

- **Tối thiểu:** 100 gương mặt trong các điều kiện ánh sáng khác nhau.
- **Mục tiêu tốt hơn:** 300–500 gương mặt.
- Cần bao phủ các trường hợp như ánh sáng ban ngày/ban đêm, mắt híp/mắt to và người đeo kính.

## 5. Roadmap phần cứng

Mentor gợi ý nhóm giải thích roadmap thay vì chỉ nói “dùng điện thoại”:

- **MVP:** Dùng điện thoại để giả lập camera độc lập và processing unit.
- **Tương lai:** Thay điện thoại bằng phần cứng nhúng trong ô tô cùng camera độc lập.
- **Nguyên tắc thiết kế:** Core algorithm giữ nguyên; chỉ thay adapter phần cứng.

Cách trình bày này giúp làm rõ khả năng mở rộng của giải pháp với ban giám khảo.

## 6. Kỷ luật làm việc nhóm

- Một thành viên tự ý thay đổi thiết kế hệ thống mà không báo nhóm/mentor và được yêu cầu viết giải trình.
- Mentor nhấn mạnh rằng trong môi trường doanh nghiệp, thay đổi thiết kế mà không thông báo là không chấp nhận được.
- Không nộp giải trình sẽ không có điểm phần đó.
- Nhóm cần ghi lại các yêu cầu và quyết định của mentor trong GitHub/tài liệu để tránh bỏ sót hoặc không nhớ nội dung đã được dặn.

> “View face thì anh không đặt nặng, nhưng doanh nghiệp thì sẽ đặt nặng. Hãy làm quen với cách làm việc đúng tinh thần doanh nghiệp từ bây giờ.”

## 7. Mốc tiếp theo

| Mốc | Thời gian | Nội dung |
| --- | --- | --- |
| Check-in nhanh | Thứ Tư | Báo cáo đã thông luồng hệ thống đến đâu và database đã setup chưa |
| Mentor Duty chính thức | Theo lịch | Trình bày tiến độ MVP |
| Giai đoạn pre-MVP | Trước khi có MVP | Mentor on-call hỗ trợ |
| Sau MVP | Sau khi có MVP | Tập trung polish sản phẩm; mentor không còn hỗ trợ chi tiết từng vấn đề kỹ thuật, chỉ review tổng thể |

**Cảnh báo tiến độ:** Nếu đến thứ Tư tiếp theo nhóm vẫn chưa thông luồng, rủi ro không kịp MVP là rất cao.

## Việc nhóm cần theo dõi

- [ ] Chốt ngưỡng cảnh báo có thể cấu hình và tách ngưỡng ghi nhận khỏi ngưỡng cần admin can thiệp.
- [ ] Lưu và đồng bộ đầy đủ các log vi phạm, gồm timestamp và confidence của từng lần.
- [ ] Xác định cách cấu hình thời gian lưu video cho MVP và production.
- [ ] Chuẩn bị tập dữ liệu người Việt có gắn nhãn thủ công và Evaluation Report (baseline, kết quả sau fine-tune, Precision, Recall, F1-score).
- [ ] Xem xét lấy nhiều frame liên tiếp ở mức 2–3 frame/giây.
- [ ] Chuẩn bị cách trình bày roadmap từ điện thoại giả lập ở MVP sang phần cứng nhúng trên ô tô.
- [ ] Check-in thứ Tư: cập nhật tiến độ thông luồng và setup database.
- [ ] Ghi lại các dặn dò/quyết định của mentor trong GitHub hoặc tài liệu chung.

---

*Nguồn: Transcript buổi Mentor Private 2 – Chương trình AI Thực Chiến Build Phase.*

1. Core Value nhóm trình bày lại
Chức năng chính: Giám sát tài xế, phát hiện buồn ngủ / mất tập trung → cảnh báo real-time để đảm bảo an toàn
Hướng mở rộng sau core: Dùng dữ liệu hành vi tài xế để tạo báo cáo định kỳ + đề xuất điều hướng chuyến đi (khi tài xế mệt, điều tài xế khác đến khu vực có nhu cầu cao)

2. Thiết kế luồng phát hiện & cảnh báo
Logic ngưỡng cảnh báo (nhóm đề xuất)
Theo dõi trong khung thời gian ngắn (~5 phút)
2 loại hành vi theo dõi: gục đầu và nhắm mắt quá lâu
Sau 3 lần vi phạm trong khung thời gian → cảnh báo trực tiếp trên thiết bị + gửi log về server lần thứ 4
Log gửi kèm: GPS, timestamp, loại vi phạm, link video clip 5–10 giây trên cloud cho admin xem xét
Phản hồi của Mentor về ngưỡng
3 lần trong 5 phút là quá thấp — ngáp/gật 3 cái trong 5 phút lúc lái xe dài là bình thường, không phải nguy hiểm
Cần thiết kế ngưỡng linh hoạt, có thể cấu hình được, không hard-code một con số
Nên phân tách 2 luồng riêng biệt:
Ghi nhận + hiển thị trên dashboard → thấp ngưỡng, ghi nhiều
Thông báo buộc admin phải hành động (push notification, mention) → cao ngưỡng hơn, chỉ khi thực sự nguy hiểm
Ví dụ tốt hơn: "Tài xế ngáp hơn 10 lần trong 30 phút" → mới đủ để escalate lên admin
"Nên chia ngưỡng linh hoạt hơn để vừa đảm bảo user nắm được hiện trạng tài xế, vừa enforce hành động trước khi nguy hiểm thực sự xảy ra."

3. Data Retention Policy
Thiết kế nhóm đề xuất
Loại dữ liệu
Thời gian lưu
Mục đích
Video clip (5–10 giây)
1 ngày rồi xóa
Để admin xác nhận tại thời điểm xảy ra
Audit log (timestamp, GPS, loại vi phạm, confidence từng lần)
Lâu dài
Làm báo cáo tháng, phân tích về sau

Phản hồi của Mentor
Giữ video 1 ngày ở MVP/demo là chấp nhận được, nhưng trong production phải cấu hình được (ví dụ 30 ngày, 90 ngày) để phục vụ audit
Tài xế khi ký hợp đồng đã đồng ý điều khoản giám sát → có cơ sở pháp lý để lưu dữ liệu dài hơn
Nên lưu toàn bộ 3 lần log trước khi gửi về server (không chỉ lần thứ 4), bao gồm timestamp và confidence của từng lần
Mở rộng tiềm năng: dữ liệu này trong tương lai có thể dùng để phân tích trạng thái tinh thần, năng suất làm việc, mức độ hài lòng nghề nghiệp của tài xế

4. Truyền tải dữ liệu từ thiết bị lên server
Lựa chọn protocol
Nhóm chọn MQTT thay vì WebSocket
Lý do: WebSocket duy trì 100.000 kết nối liên tục → server không chịu được; MQTT nhẹ hơn nhiều, phù hợp với IoT/embedded
Mentor đồng ý: không có vấn đề gì với lựa chọn MQTT
Xử lý khi mất mạng (offline handling)
Cảnh báo tài xế vẫn xảy ra on-device ngay lập tức, không phụ thuộc mạng
Log được lưu cục bộ trên thiết bị → khi có mạng trở lại → tự động sync lên server
Mentor nhận xét: hướng này ổn
Định dạng dữ liệu
Nhóm gửi video clip thay vì ảnh đơn
Mentor gợi ý: nên chụp nhiều frame liên tiếp (2–3 frame/giây) thay vì detect trên 1 ảnh duy nhất → tăng độ chính xác và giảm false positive

5. Vấn đề dữ liệu & độ chính xác model cho người Việt
Hiện trạng
Nhóm đang dùng thư viện MediaPipe để nhận diện khuôn mặt
Model được train trên data nước ngoài → có sai số khi áp dụng cho người Việt (mắt đông hơn, đường nét khuôn mặt khác)
Một số edge case chưa xử lý được: nheo mắt do nắng, điều kiện ánh sáng yếu
Yêu cầu cứng từ Mentor — phải có Evaluation Report
Mentor nhấn mạnh đây là điểm bắt buộc tại buổi demo cuối:
"Không có evaluation test là bị trừ điểm rất nặng. Ban giám khảo chắc chắn sẽ hỏi về F-metric (F1 score, Precision, Recall)."
Quy trình evaluation cần thực hiện:
Chạy model trước khi fine-tune → đo accuracy baseline
Thực hiện fine-tune / chỉnh hyperparameter
Chạy lại → so sánh kết quả trước/sau
Evaluation trên tập ảnh người Việt được gắn nhãn thủ công:
Tối thiểu: 100 gương mặt ở các điều kiện ánh sáng khác nhau
Tốt hơn: 300–500 gương mặt
Các điều kiện cần cover: ánh sáng ban ngày, ban đêm, mắt híp, mắt to, đeo kính, v.v.
So sánh nhãn thủ công với output model → tính Precision, Recall, F1

6. Lộ trình phần cứng — cách giải thích với ban giám khảo
Mentor gợi ý nhóm cần trình bày rõ roadmap phần cứng thay vì chỉ nói "dùng điện thoại":
"Thời điểm MVP: dùng điện thoại để giả lập camera độc lập + processing unit. Trong tương lai: thay thế điện thoại bằng phần cứng nhúng trong ô tô, camera độc lập. Core algorithm không thay đổi, chỉ thay adapter phần cứng."
Đây là cách lý giải thuyết phục cho ban giám khảo về tính scalable của giải pháp.

7. Kỷ luật & văn hóa làm việc nhóm
Mentor nhắc một vấn đề nội bộ trong buổi:
Một thành viên tự ý thay đổi thiết kế hệ thống mà không báo lại nhóm/mentor → bị yêu cầu viết giải trình
Lý do: Trong môi trường doanh nghiệp thực, tự ý thay đổi thiết kế mà không thông báo là không chấp nhận được
Không viết giải trình → không có điểm phần đó
"View face thì anh không đặt nặng, nhưng doanh nghiệp thì sẽ đặt nặng. Hãy làm quen với cách làm việc đúng tinh thần doanh nghiệp từ bây giờ."

8. Deadline & kế hoạch tiếp theo
Mốc
Deadline
Nội dung
Check-in nhanh
Thứ Tư
Nhóm báo cáo tiến độ: đã thông luồng đến đâu, DB đã setup chưa
Mentor Duty chính thức
Theo lịch
Trình bày MVP progress
Pre-MVP
Trước khi có MVP
Mentor sẽ on-call hỗ trợ
Sau MVP
Sau khi có MVP
Tập trung polish sản phẩm, mentor không còn giải đáp chi tiết kỹ thuật nữa

Lưu ý từ Mentor:
Cần note lại tất cả những gì mentor nói, có GitHub/tài liệu để track → không được phép "không nhớ mentor đã dặn gì"
Nếu thứ Tư tiếp theo mà nhóm vẫn chưa thông luồng → rủi ro rất cao không kịp MVP
Sau giai đoạn MVP: mentor chỉ review sản phẩm tổng thể, không ngồi support từng vấn đề kỹ thuật nữa

Báo cáo được tổng hợp từ transcript buổi Mentor Private 2 – Chương trình AI Thực Chiến Build Phase.

