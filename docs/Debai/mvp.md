# **DRIVERGUARD SAFETY CORE**

# **Báo Cáo Đánh Giá MVP**

| Thuộc tính | Nội dung |
| ----- | ----- |
| Phiên bản | 1.1 |
| Trạng thái | Pre-evaluation — chờ phỏng vấn và benchmark |
| Chu kỳ MVP | 6 tuần: Build → User Validation → Iterate → Final Benchmark |
| Phạm vi đánh giá | Safety Core dành cho tài xế taxi |
| Bối cảnh | Tài xế taxi tại Hà Nội thường chạy ca kéo dài hoặc ca tối. |

**Quyết định phạm vi:** Báo cáo này chỉ đánh giá một giả thuyết sản phẩm: DriverGuard có thể phát hiện dấu hiệu mất tỉnh táo và tạo ra phản ứng an toàn kịp thời từ tài xế. Fleet Intelligence Agent và bài toán điều phối doanh thu là một thí nghiệm riêng, không được gộp vào Magic Metric của Safety Core.

## **1\. Chân Dung Người Dùng Target (Persona)**

* **Đối tượng mục tiêu:** Tài xế taxi tại Hà Nội thường chạy ca kéo dài hoặc ca tối, từng gặp tình trạng buồn ngủ/mất tập trung khi lái và thường tiếp tục chạy để hoàn thành chuyến hoặc duy trì thu nhập.  
* **Đặc điểm nhận diện:**  
  * Có trải nghiệm thực tế về buồn ngủ hoặc mất tập trung khi đang lái xe.  
  * Thường lái nhiều giờ, lái ca tối/ban đêm hoặc chịu áp lực hoàn thành cuốc và duy trì thu nhập.  
  * Có xu hướng tiếp tục chạy dù đã mệt để hoàn thành chuyến hoặc tránh mất cơ hội nhận cuốc.  
  * Sử dụng điện thoại thông minh trong quá trình làm việc để nhận cuốc và dẫn đường.  
  * Không muốn bị quay và truyền hình ảnh khuôn mặt liên tục lên server.

Persona được xác định theo **hành vi và hoàn cảnh lái xe**, không giả định theo độ tuổi khi chưa có dữ liệu phỏng vấn.

**Người liên quan nhưng không phải persona cốt lõi:** Fleet manager đóng vai trò HITL khi hệ thống phát hiện critical event trong Fleet Mode.

## **2\. Nêu Vấn Đề (Problem Statement)**

* **Người dùng (Who):** Tài xế taxi tại Hà Nội thường chạy ca dài/ca tối, đã từng buồn ngủ hoặc mất tập trung khi lái và có xu hướng tiếp tục chạy vì chuyến đi hoặc thu nhập.  
* **Cần thực hiện việc gì (Job to be done):** Nhận biết kịp thời khi mức tỉnh táo đã giảm tới ngưỡng nguy hiểm và thực hiện hành động an toàn trước khi mất khả năng kiểm soát xe.  
* **Trong bối cảnh nào (Context):** Sau nhiều giờ lái liên tục, khi đi vào chập tối, ban đêm, thời tiết xấu, đường đơn điệu hoặc thời điểm cơ thể dễ buồn ngủ.  
* **Giải pháp tạm thời hiện tại (Workaround):** Tự đánh giá mức tỉnh táo; mở cửa sổ, uống cà phê/nước tăng lực, nghe nhạc, nói chuyện hoặc cố lái tới khi hoàn thành chuyến. Khi có người đi cùng, hành khách hoặc đồng nghiệp có thể nhắc nhở.  
* **Hệ quả tiêu cực (Consequence):** Tài xế có thể nhận biết quá muộn, phản xạ chậm, mất tập trung hoặc ngủ gật trong vài giây. Workaround phụ thuộc cảm nhận chủ quan và không tạo được cảnh báo liên tục khi tài xế đi một mình.

  ### Problem statement rút gọn

Tài xế taxi lái xe theo ca dài cần được cảnh báo khách quan và kịp thời khi xuất hiện dấu hiệu mất tỉnh táo, vì khả năng tự nhận biết có thể đến muộn và các biện pháp tự xoay sở hiện tại không theo dõi liên tục được trạng thái của họ.

### Bằng chứng ban đầu

* Theo số liệu Bộ Công an được báo chí dẫn lại, năm 2023 cả nước xảy ra 21.880 vụ tai nạn giao thông đường bộ; 0,33% được phân loại do mệt mỏi, ngủ gật, tương đương khoảng 72 vụ.\[1\]  
* Báo Công Lý năm 2019 dẫn một ước tính lịch sử rằng mỗi năm có hơn 6.000 người chết trong các vụ tai nạn liên quan tới tài xế ngủ gật. Nguồn này không công bố kỳ dữ liệu hoặc phương pháp tính nên chỉ dùng làm tham khảo, không dùng làm baseline.\[2\]  
* NHTSA lưu ý tai nạn do buồn ngủ thường khó xác định sau sự cố và có khả năng bị ghi nhận thiếu.\[3\]

  ## **3\. Phân Tích Bệnh Gốc (Root Cause Analysis)**

* **Triệu chứng bề nổi:**  
  * Mắt nhắm lâu hơn nhịp chớp mắt thông thường.  
  * Tần suất nhắm mắt tăng theo thời gian.  
  * Gục đầu hoặc quay mặt khỏi hướng đường.  
  * Phản ứng chậm, mất tập trung hoặc không duy trì hướng nhìn.  
  * Tài xế cố tiếp tục hành trình dù đã xuất hiện dấu hiệu mệt mỏi.  
* **Bệnh gốc nằm ngoài khả năng giải quyết trực tiếp của sản phẩm:**  
  * Thiếu ngủ và rối loạn nhịp sinh học.  
  * Thời gian lái kéo dài, đặc biệt trong ca tối/ban đêm.  
  * Áp lực hoàn thành chuyến, nhận cuốc và duy trì thu nhập.  
* **Khoảng trống DriverGuard có thể giải quyết:**  
  * Tài xế tự nhận biết trạng thái nguy hiểm quá muộn hoặc đánh giá sai mức tỉnh táo của mình.  
  * Không có giám sát khách quan, liên tục khi tài xế đi một mình.  
  * Cảnh báo hoặc dấu hiệu hiện tại chưa dẫn rõ ràng tới hành động dừng/nghỉ an toàn.

DriverGuard không chữa nguyên nhân thiếu ngủ, không loại bỏ áp lực thu nhập và không thay thế việc nghỉ ngơi. MVP chỉ can thiệp vào khoảng trống có thể giải quyết bằng sản phẩm: **phát hiện đủ sớm → cảnh báo → tạo ra hành động an toàn trước khi tài xế tiếp tục lái trong trạng thái nguy hiểm**.

### Chuỗi nguyên nhân

* Thiếu ngủ / lái kéo dài / áp lực nhận cuốc  
*                     │  
*                     ▼  
*         Mức tỉnh táo giảm dần  
*                     │  
*                     ▼  
*  Tài xế đánh giá sai hoặc nhận biết quá muộn  
*                     │  
*                     ▼  
*       Không có cảnh báo khách quan kịp thời  
*                     │  
*                     ▼  
*   Tiếp tục lái trong trạng thái rủi ro cao

  ## **4\. Lợi Thế Cạnh Tranh Từ AI (AI Advantage)**

* **Vai trò của AI:**  
  * Phân tích liên tục camera để nhận diện trạng thái mắt và tư thế đầu.  
  * Tổng hợp tín hiệu theo thời gian bằng PERCLOS và temporal smoothing thay vì phản ứng với từng frame riêng lẻ.  
  * Hoạt động on-device để giảm độ trễ và duy trì cảnh báo khi mất mạng.  
  * Cho phép hiệu chỉnh theo góc đặt camera và baseline của từng phiên sử dụng.  
* **Giá trị vượt trội:**  
  * Theo dõi liên tục trong khi tài xế không thể tự quan sát chính mình.  
  * Phân biệt tốt hơn giữa một lần chớp mắt bình thường và xu hướng mất tỉnh táo kéo dài.  
  * Cảnh báo theo ngữ cảnh thời gian thay vì sử dụng timer đơn giản.  
  * Không cần truyền video liên tục lên cloud.  
  * Có thể tạo metadata có cấu trúc để fleet manager can thiệp khi rủi ro vượt ngưỡng.

  ### Required Agent \+ HITL — cùng phục vụ outcome an toàn

Khi critical event xảy ra trong Fleet Mode, Safety Escalation Agent có thể tổng hợp risk score, số lần cảnh báo, tốc độ và thời gian lái để đề xuất hành động. Fleet manager là HITL, có quyền yêu cầu dừng an toàn, tạm ngừng giao cuốc mới hoặc đánh dấu cảnh báo sai.

Agent không thay thế cảnh báo local, không tự can thiệp vào xe và không tạo ra một MVP thứ hai. Vai trò của Agent \+ HITL là củng cố cùng một chuỗi giá trị an toàn: **phát hiện → cảnh báo → xác nhận → can thiệp có người phê duyệt**.

### Future Experiment — không thuộc Core MVP

Agent điều phối để tối ưu doanh thu được giữ như một hướng mở rộng sau MVP. Thí nghiệm này có persona, problem, job, outcome và Magic Metric riêng nên không được pitch ngang hàng hoặc gộp vào kết quả đánh giá Safety Core.

## **5\. Phạm Vi Sản Phẩm Khả Thi Tối Thiểu (MVP Scope)**

* **1 User:** Tài xế taxi tại Hà Nội thường chạy ca dài/ca tối, từng buồn ngủ hoặc mất tập trung khi lái và vẫn có xu hướng tiếp tục chạy.  
* **1 Job:** Nhận cảnh báo kịp thời khi xuất hiện dấu hiệu mất tỉnh táo nguy hiểm.  
* **1 Outcome:** Tài xế nhận cảnh báo hợp lệ và bắt đầu hành động dừng/nghỉ an toàn trước khi tiếp tục hành trình.

  ### Các tính năng bắt buộc

* Phát hiện nhắm mắt kéo dài, quay đầu khỏi hướng đường và gục đầu.  
* Đánh giá tín hiệu theo cửa sổ thời gian.  
* Cảnh báo local bằng âm thanh/rung.  
* Hoạt động offline.  
* Ghi nhận timestamp, loại sự kiện, confidence và phản hồi của người dùng.  
* Cho phép tài xế xác nhận cảnh báo đúng/sai khi xe đã dừng.

  ### Ngoài phạm vi đánh giá cốt lõi

* Điều phối taxi để tăng doanh thu.  
* Cảnh báo điểm đường nguy hiểm.  
* Stream video liên tục.  
* ADAS can thiệp điều khiển xe.  
* Chứng minh giảm tai nạn ngoài thực tế.  
* Cá nhân hóa dài hạn, OTA model và triển khai production.

  ###  Phạm vi

* CORE MVP  
* Tài xế taxi → phát hiện mất tỉnh táo  
* → cảnh báo → thúc đẩy hành động dừng/nghỉ an toàn  
*   
* REQUIRED AGENT \+ HITL  
* Safety Escalation Agent tổng hợp mức rủi ro  
* → Fleet manager duyệt hành động can thiệp  
*   
* FUTURE EXPERIMENT  
* Agent điều phối để tối ưu doanh thu

  ## **6\. Giả Định Sống Còn & Rủi Ro (Leap of Faith Assumptions)**

  ### Giả định 1 — Dấu hiệu nguy hiểm có thể được phát hiện đủ tin cậy bằng thiết bị MVP

* **Giả định:** Camera điện thoại hoặc camera hồng ngoại cùng MediaPipe có thể phát hiện các tín hiệu bắt buộc ở tốc độ tối thiểu 15 FPS.  
* **Nếu sai:** Thu hẹp operating condition, chuẩn hóa vị trí đặt camera, bổ sung camera IR hoặc model chuyên biệt; không tuyên bố hỗ trợ điều kiện chưa đạt benchmark.

  ### Giả định 2 — Cảnh báo không tạo ra quá nhiều false positive

* **Giả định:** Temporal smoothing, PERCLOS và calibration có thể giữ cảnh báo sai ở mức người dùng chấp nhận được.  
* **Nếu sai:** Tài xế sẽ tắt hoặc bỏ qua ứng dụng. Cần điều chỉnh ngưỡng, giảm số hành vi được hỗ trợ hoặc chuyển sang cảnh báo nhẹ trước khi phát cảnh báo mạnh.

  ### Giả định 3 — Cảnh báo dẫn đến hành động an toàn

* **Giả định:** Khi nhận cảnh báo có lý do rõ ràng, tài xế sẽ thừa nhận rủi ro và dừng/nghỉ thay vì tiếp tục lái.  
* **Nếu sai:** Thay đổi nội dung và cấp độ cảnh báo, bổ sung gợi ý điểm dừng, tạm ngừng giao cuốc mới trong Fleet Mode hoặc thiết kế cơ chế HITL phù hợp.

  ### Giả định 4 — Người dùng chấp nhận camera khi quyền riêng tư được bảo vệ

* **Giả định:** Tài xế sẵn sàng sử dụng hệ thống nếu video được xử lý on-device và không bị stream liên tục.  
* **Nếu sai:** Cần giảm dữ liệu thu thập, minh bạch hóa luồng xử lý hoặc thay đổi mô hình triển khai. Upload media không được bật mặc định.

  ## **7\. Chỉ Số Đo Lường Cốt Lõi (Magic Metric)**

* **Tên chỉ số:** **Tỷ lệ chuyển đổi từ cảnh báo nguy hiểm hợp lệ sang hành động an toàn** (*Safe Response Rate after Valid Critical Alert*).  
* **Công thức:**  
* (Số cảnh báo critical hợp lệ có phản ứng an toàn trong thời gian quy định \\  
* Tổng số cảnh báo critical hợp lệ được phát trong bài kiểm thử )× 100%

* **Mục tiêu thử nghiệm:** Ít nhất 80% cảnh báo critical hợp lệ khiến người tham gia xác nhận và chọn hành động dừng/nghỉ trong vòng 60 giây ở tình huống kiểm thử có kiểm soát.

Magic Metric trên là **proxy hành vi trong kiểm thử**, không phải bằng chứng tài xế ngoài thực tế sẽ dừng xe hoặc tỷ lệ tai nạn sẽ giảm.

### Guardrail Metrics

| Chỉ số | Target MVP | Kết quả |
| ----- | ----: | ----: |
| Recall sự kiện nguy hiểm | ≥90% | Pending measurement |
| Precision sự kiện | ≥85% | Pending measurement |
| Cảnh báo sai khi tỉnh táo | ≤1 lần/30 phút video | Pending measurement |
| Tốc độ xử lý | ≥15 FPS | Pending measurement |
| Độ trễ sau khi thỏa ngưỡng | ≤300 ms | Pending measurement |
| Hoạt động khi mất mạng | 100% cảnh báo local | Pending test |

Một Magic Metric tốt không được đánh đổi bằng cách phát quá nhiều cảnh báo; vì vậy Safe Response Rate chỉ có ý nghĩa khi các guardrail về precision và false positive đồng thời đạt yêu cầu.

## 8\. Đánh Giá Tính Khả Thi (Feasibility)

MVP được đánh giá trên sáu nhóm bắt buộc: **Data, Accuracy, Latency, Cost, Trust và Human Interlock**. Accuracy và latency là guardrail kỹ thuật; không thay thế Magic Metric về thay đổi hành vi.

| Nhóm đánh giá | Câu hỏi cần trả lời | Cách đo MVP | Trạng thái |
| ----- | ----- | ----- | ----- |
| Data | Dữ liệu có đủ đại diện cho góc camera, ánh sáng, kính và hành vi mục tiêu không? | Benchmark trên video có nhãn, chia theo điều kiện vận hành | Pending |
| Accuracy | Hệ thống có phát hiện đúng sự kiện nguy hiểm mà không gây quá nhiều cảnh báo sai không? | Recall, precision và false positive rate | Pending |
| Latency | Cảnh báo có được phát đủ nhanh trên thiết bị demo không? | FPS và độ trễ từ lúc thỏa ngưỡng tới lúc cảnh báo | Pending |
| Cost | Chạy camera và inference liên tục có phù hợp với thiết bị và ngân sách vận hành không? | Pin, nhiệt độ, CPU/RAM, mobile data và backend cost | Pending |
| Trust | Tài xế có hiểu, tin và chấp nhận cảnh báo/camera không? | Phỏng vấn trực tiếp, tỷ lệ xác nhận đúng/sai và lý do tắt app | Pending |
| Human Interlock | Con người có quyền xác nhận và quyết định hành động can thiệp không? | Demo luồng Fleet manager duyệt can thiệp và đánh dấu cảnh báo sai | Pending |

### Cost Benchmark bắt buộc

Kiến trúc inference on-device và không stream video có thể giúp giảm chi phí cloud, nhưng lợi thế này chỉ được khẳng định sau khi có số đo. Benchmark tối thiểu gồm:

| Chỉ số cost | Đơn vị báo cáo | Điều kiện đo | Kết quả |
| ----- | ----- | ----- | ----: |
| Mức tiêu hao pin | % pin/giờ | Camera và inference chạy liên tục trên thiết bị demo | Pending |
| Nhiệt độ thiết bị | °C trung bình/đỉnh | Sau tối thiểu 60 phút chạy liên tục | Pending |
| CPU/RAM | % CPU và MB RAM trung bình/đỉnh | Cùng kịch bản benchmark | Pending |
| Mobile data | KB hoặc MB/chuyến | Chỉ gửi metadata, không stream video | Pending |
| Chi phí backend | VND hoặc USD/tài xế/tháng; trên 1.000 sự kiện | Gồm ingest, lưu trữ và xử lý metadata | Pending |

Điều kiện pass/fail cụ thể sẽ được chốt sau baseline tuần 1 trên thiết bị demo; không đặt target giả khi chưa có số đo nền.

## **9\. Kết Quả Phỏng Vấn Người Dùng (User Research)**

* **Số lượng phỏng vấn yêu cầu:** Tối thiểu 5 tài xế thật, gặp và phỏng vấn trực tiếp.  
* **Số lượng đã hoàn thành:** 0/5 — chưa thực hiện.

Không tính việc chỉ gửi form là một cuộc phỏng vấn. Không tạo insight hoặc trích dẫn giả bằng AI. Phần này chỉ được hoàn thành sau khi team gặp trực tiếp và phỏng vấn người dùng thật.

### Tiêu chí tuyển người tham gia

* Đang hoặc từng làm tài xế taxi/dịch vụ chở người tại Hà Nội.  
* Có kinh nghiệm lái theo ca dài hoặc vào buổi tối/ban đêm.  
* Đã từng cảm thấy buồn ngủ hoặc mất tập trung khi lái xe.  
* Có sử dụng điện thoại phục vụ công việc lái xe.

  ### Câu hỏi phỏng vấn cốt lõi

1. Lần gần nhất anh/chị buồn ngủ khi lái xe xảy ra trong hoàn cảnh nào?  
2. Anh/chị nhận ra mình buồn ngủ bằng dấu hiệu gì và thường nhận ra sớm hay muộn?  
3. Khi đó anh/chị đã làm gì? Vì sao vẫn tiếp tục hoặc quyết định dừng?  
4. Điều gì khiến anh/chị bỏ qua một cảnh báo an toàn?  
5. Mức cảnh báo nào ít gây giật mình nhưng vẫn đủ để hành động?  
6. Anh/chị có chấp nhận camera theo dõi nếu toàn bộ hình ảnh được xử lý trên điện thoại không?  
7. Trong trường hợp nào anh/chị chấp nhận fleet manager nhận cảnh báo?  
8. Điều gì khiến anh/chị tắt hoặc gỡ ứng dụng sau vài ngày sử dụng?

   ### Bảng ghi nhận

| ID | Hồ sơ người tham gia | Ngày | Workaround hiện tại | Insight chính | Trạng thái |
| ----- | ----- | ----- | ----- | ----- | ----- |
| P01 | Chờ tuyển | — | — | — | Pending |
| P02 | Chờ tuyển | — | — | — | Pending |
| P03 | Chờ tuyển | — | — | — | Pending |
| P04 | Chờ tuyển | — | — | — | Pending |
| P05 | Chờ tuyển | — | — | — | Pending |

   ### Điều kiện hoàn thành User Research

* Có tối thiểu 5 cuộc phỏng vấn trực tiếp với tài xế thật; khảo sát/form chỉ là dữ liệu bổ trợ.  
* Ghi lại hành vi và tình huống đã xảy ra, không chỉ hỏi người dùng có thích ý tưởng hay không.  
* Tổng hợp các pattern lặp lại giữa nhiều người.  
* Chỉ thay đổi Persona, Problem Statement hoặc cảnh báo khi có bằng chứng từ phỏng vấn.

  ## **10\. Roadmap MVP Trong 6 Tuần**

| Giai đoạn | Mục tiêu | Deliverable/Exit criteria |
| ----- | ----- | ----- |
| Tuần 1–3 — Build | Hoàn thiện Safety Core, telemetry và luồng Agent \+ HITL phục vụ an toàn | Demo end-to-end; có log sự kiện; không đưa điều phối doanh thu vào core flow |
| Tuần 4 — User Validation | Đưa prototype cho người dùng thử trong môi trường an toàn và phỏng vấn trực tiếp | Hoàn thành tối thiểu 5/5 cuộc phỏng vấn trực tiếp; tổng hợp pattern và phản đối chính |
| Tuần 5 — Iterate | Điều chỉnh persona, ngưỡng phát hiện, nội dung/cấp độ cảnh báo và luồng HITL theo bằng chứng | Có changelog nêu rõ feedback nào dẫn tới thay đổi nào |
| Tuần 6 — Final Benchmark & Pitch | Chạy benchmark cuối, đối chiếu Magic Metric với guardrails và chuẩn bị trình bày | Có kết quả Data, Accuracy, Latency, Cost, Trust, HITL; pitch một MVP an toàn duy nhất |

  ## Kết luận đánh giá hiện tại

MVP hiện đã có giả thuyết, phạm vi, target và phương pháp đo. Safety Core là MVP duy nhất; Safety Escalation Agent \+ HITL phục vụ cùng outcome an toàn, còn điều phối doanh thu là thí nghiệm tương lai. Chưa thể kết luận sản phẩm “đã được xác thực” cho tới khi hoàn thành:

1. Tối thiểu 5 cuộc phỏng vấn trực tiếp với tài xế thật.  
2. Benchmark Safety Core trên video có nhãn và thiết bị demo.  
3. Kiểm thử hành vi cảnh báo trong môi trường có kiểm soát.  
4. Đối chiếu Magic Metric với các guardrail về false positive và recall.  
5. Đo cost trên thiết bị và backend: pin, nhiệt độ, CPU/RAM, mobile data và chi phí trên mỗi tài xế hoặc 1.000 sự kiện.  
6. Hoàn thành chu kỳ sáu tuần và ghi nhận các thay đổi dựa trên feedback.

   ## **Tài liệu tham khảo**

   ---

* Người Đưa Tin, [Nguyên nhân đằng sau các vụ tai nạn giao thông nghiêm trọng, hết sức báo động](https://www.nguoiduatin.vn/nguyen-nhan-dang-sau-cac-vu-tai-nan-giao-thong-nghiem-trong-het-suc-bao-dong-204652574.htm); Tuổi Trẻ Online, [Bắt “bệnh” ngủ gà ngủ gật dễ gây tai nạn của lái xe đường dài](https://tuoitre.vn/bat-benh-ngu-ga-ngu-gat-de-gay-tai-nan-cua-lai-xe-duong-dai-20241021134716699.htm). ↩︎  
* Báo Công Lý, [Hơn 6.000 người chết mỗi năm do lái xe ngủ gật](https://congly.vn/hon-6000-nguoi-chet-moi-nam-do-lai-xe-ngu-gat-59146.html). ↩︎  
* NHTSA, [Drowsy Driving — Data and Surveillance](https://www.nhtsa.gov/book/countermeasures-that-work/drowsy-driving/data-surveillance). ↩︎

*   
* 