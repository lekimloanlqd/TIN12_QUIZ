# QUIZ SMART – HỌC VUI, LUYỆN TẬP THÔNG MINH

Ứng dụng ôn tập trắc nghiệm Tin học 12 – Lấy cảm hứng từ giao diện sinh động và gamification của Quizizz. Tích hợp trọn bộ câu hỏi, đáp án và giải thích chuẩn từ tài liệu **Bài 7: HTML và Cấu trúc trang web**.

Hỗ trợ chạy đồng thời cả **Website (Vercel)** và **Ứng dụng di động Android (APK)** trong cùng một dự án!

---

## 🌐 Khắc Phục Lỗi 404 Khi Triển Khai Lên Vercel

### Nguyên nhân lỗi:
Trước đó dự án chỉ chứa mã nguồn ứng dụng di động Android (Gradle), thiếu tệp trang web `index.html` và tệp cấu hình định tuyến `vercel.json`. Khi Vercel import repo sẽ không tìm thấy trang hiển thị, dẫn đến lỗi:
`This page doesn't exist - 404 NOT_FOUND`.

### Đã được xử lý triệt để:
1. **`index.html`**: Đã tạo ứng dụng Web Quiz Smart hoàn chỉnh, tích hợp Tailwind CSS, âm thanh Web Audio API, đầy đủ 26 ý đánh giá Bài 7, chế độ Khách (Guest Mode), mã PIN `789123`, Bảng điều khiển giáo viên và xuất file Excel / CSV.
2. **`vercel.json`**: Cấu hình rewrite mọi đường dẫn về `index.html` để học sinh truy cập bất kỳ link nào (ví dụ `/quiz/bai-7-html`) đều không bao giờ bị lỗi 404.
3. **`package.json`**: Cung cấp script để hệ thống Vercel tự động nhận diện và deploy thành công 100%.

### Cách cập nhật trên Vercel:
- **Cách 1**: Đẩy commit mới nhất từ AI Studio sang GitHub -> Vercel sẽ tự động nhận diện bản cập nhật và triển khai lại trong vài giây.
- **Cách 2**: Truy cập vào dashboard dự án trên [Vercel.com](https://vercel.com) -> Vào mục **Deployments** -> Bấm vào dấu 3 chấm góc phải bản deploy -> Chọn **Redeploy**.

---

## 🚀 Hướng Dẫn Xuất Bản Lên GitHub

### Cách 1: Xuất bản trực tiếp từ AI Studio (Khuyên dùng)
1. Ở góc trên cùng bên phải giao diện Google AI Studio, nhấp vào biểu tượng **Settings** (hoặc nút **Export / Push**).
2. Chọn **Push to GitHub** (hoặc **Export to GitHub**).
3. Đăng nhập hoặc liên kết tài khoản GitHub của bạn.
4. Đặt tên Repository (ví dụ: `quiz-smart-tin-hoc-12`) và chọn chế độ **Public** hoặc **Private** rồi nhấn **Create & Push**.

### Cách 2: Tải file ZIP về và đẩy lên GitHub bằng Git
1. Nhấp vào menu tùy chọn trong AI Studio -> chọn **Download as ZIP**.
2. Giải nén thư mục trên máy tính của bạn.
3. Mở terminal (CMD, PowerShell hoặc Git Bash) tại thư mục dự án và thực hiện các lệnh:
```bash
git init
git add .
git commit -m "feat: Fix Vercel 404 and optimize Quiz Smart web app"
git branch -M main
git remote add origin https://github.com/TÊN_TÀI_KHOẢN_CỦA_BẠN/quiz-smart-tin-hoc-12.git
git push -u origin main
```

---

## 🌟 Tổng Quan Tính Năng Ứng Dụng

### 1. Chế độ Khách (Guest Mode) – Không cần đăng nhập
- Học sinh không cần đăng ký tài khoản hay nhớ mật khẩu.
- Chỉ cần nhập **Họ và tên** cùng **Lớp** (ví dụ: `12/1B`, `12A1`...) là vào làm bài ngay.
- Hỗ trợ tham gia nhanh bằng **Mã PIN** (Mã PIN Bài 7: `789123`).
- Toàn bộ kết quả và lịch sử làm bài được lưu trữ bền vững.

### 2. Ba Chế Độ Luyện Tập Linh Hoạt
1. **Luyện tập từng câu**: Xem ngay phản hồi *CHÍNH XÁC! 🎉* hoặc *CHƯA CHÍNH XÁC! 💡*, hiển thị đáp án đúng và lời giải thích chi tiết trích từ tài liệu gốc ngay sau mỗi câu.
2. **Kiểm tra tính điểm**: Làm toàn bộ 26 ý đánh giá, di chuyển tự do giữa các câu thông qua bảng điều hướng lưới câu hỏi, nộp bài tính điểm trên thang 10.
3. **Thử thách Quizizz**: Đồng hồ đếm ngược, chuỗi Streak Combo tăng điểm thưởng, xáo trộn thứ tự câu hỏi và âm thanh phản hồi vui nhộn.

### 3. Tích Hợp Đầy Đủ 26 Ý Đánh Giá Bài 7 – Tin Học 12
- **Phần 1: 10 câu trắc nghiệm nhiều lựa chọn** (4 phương án A, B, C, D).
- **Phần 2: 4 bối cảnh trắc nghiệm Đúng/Sai** (16 phát biểu: a, b, c, d với 4 cấp độ Biết, Hiểu, Vận dụng).
- Đáp án và giải thích trích dẫn nguyên văn từ tài liệu chính thức:
  - Bối cảnh 1 (Quan sát trang web): Đ – Đ – Đ – S.
  - Bối cảnh 2 (Nhận diện thẻ và phần tử): Đ – Đ – Đ – S.
  - Bối cảnh 3 (Tổ chức cấu trúc tệp HTML): Đ – Đ – Đ – S.
  - Bối cảnh 4 (Soạn thảo HTML): Đ – Đ – Đ – S.

### 4. Bảng Điều Khiển Giáo Viên (Teacher Dashboard)
- Đăng nhập giáo viên với tài khoản mẫu: `giaovien` / `123456`.
- **Thống kê tổng quan**: Tổng số học sinh, số lượt làm bài, điểm trung bình, biểu đồ phân bố điểm số (Giỏi, Khá, Trung bình, Chưa đạt) và phân loại học sinh Guest vs Tài khoản.
- **Bảng điểm học sinh**: Tìm kiếm theo họ tên, lọc theo lớp, lọc theo hình thức tham gia, xem chi tiết từng lượt làm bài.
- **Xuất bảng điểm ra file Excel / CSV**: Dễ dàng chia sẻ hoặc lưu trữ bảng điểm.
- **Quản lý bài & Mã PIN**: Tạo và chia sẻ mã PIN, cấu hình thời gian làm bài, bật/tắt nhận bài và cho phép làm lại.
