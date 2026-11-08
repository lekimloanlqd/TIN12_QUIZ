# QUIZ SMART – HỌC VUI, LUYỆN TẬP THÔNG MINH

Ứng dụng ôn tập trắc nghiệm Tin học 12 – Lấy cảm hứng từ giao diện sinh động và gamification của Quizizz. Tích hợp trọn bộ câu hỏi, đáp án và giải thích chuẩn từ tài liệu **Bài 7: HTML và Cấu trúc trang web**.

---

## 🚀 Hướng Dẫn Xuất Bản Lên GitHub (Publish to GitHub)

### Cách 1: Xuất bản trực tiếp từ AI Studio (Khuyên dùng - Nhanh nhất)
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
git commit -m "feat: Khoi tao du an Quiz Smart Tin hoc 12 - Bai 7 HTML"
git branch -M main
git remote add origin https://github.com/TÊN_TÀI_KHOẢN_CỦA_BẠN/quiz-smart-tin-hoc-12.git
git push -u origin main
```

---

## 🤖 Tự Động Build APK Qua GitHub Actions

Dự án đã được cấu hình sẵn tệp workflow tại `.github/workflows/build.yml`:
- Khi bạn push mã nguồn lên nhánh `main`, hệ thống GitHub Actions sẽ tự động chạy tiến trình kiểm thử và biên dịch APK (`assembleDebug`).
- Bạn có thể vào tab **Actions** trên GitHub repository của mình, nhấp vào lần chạy gần nhất và tải file `QuizSmart-Debug-APK` về máy tính hoặc điện thoại Android để cài đặt trực tiếp.

---

## 💻 Mở & Chạy Dự Án Trên Android Studio

1. Tải và cài đặt **Android Studio** (bản Jellyfish, Koala hoặc mới hơn).
2. Mở Android Studio -> Chọn **Open...** và điều hướng đến thư mục dự án.
3. Đợi Android Studio đồng bộ Gradle (Sync Project with Gradle Files).
4. Nhấn nút **Run 'app'** (phím tắt `Shift + F10`) để chạy ứng dụng trên máy ảo hoặc thiết bị Android thật.
5. Để xuất bản file APK thủ công từ máy tính:
   ```bash
   ./gradlew assembleDebug
   ```
   File APK sẽ được tạo tại đường dẫn: `app/build/outputs/apk/debug/app-debug.apk`.

---

## 🌟 Tổng Quan Tính Năng Ứng Dụng

### 1. Chế độ Khách (Guest Mode) – Không cần đăng nhập
- Học sinh không cần đăng ký tài khoản hay nhớ mật khẩu.
- Chỉ cần nhập **Họ và tên** cùng **Lớp** (ví dụ: `12/1B`, `12A1`...) là vào làm bài ngay.
- Hỗ trợ tham gia nhanh bằng **Mã PIN** (Mã PIN Bài 7: `789123`).
- Toàn bộ kết quả và lịch sử làm bài được lưu trữ bền vững vào cơ sở dữ liệu Room SQLite trên thiết bị.

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
