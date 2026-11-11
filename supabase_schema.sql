-- ========================================================
-- QUIZ SMART: SUPABASE POSTGRESQL DATABASE SCHEMA
-- HỌC VUI, LUYỆN TẬP THÔNG MINH - TIN HỌC 12 (BÀI 7)
-- ========================================================
-- Chạy toàn bộ mã script này trong mục SQL Editor của Supabase
-- để tạo các bảng lưu trữ vĩnh viễn, bảo mật RLS và Realtime.

-- 1. BẢNG LƯU TRỮ KẾT QUẢ LÀM BÀI CỦA HỌC SINH (STUDENT ATTEMPTS)
CREATE TABLE IF NOT EXISTS public.student_attempts (
    id TEXT PRIMARY KEY,
    student_name TEXT NOT NULL,
    class_name TEXT NOT NULL,
    room_id TEXT,
    room_code TEXT,
    participation_type TEXT DEFAULT 'GUEST',
    score NUMERIC(4, 2) NOT NULL,
    correct_count INTEGER NOT NULL,
    total_points INTEGER NOT NULL,
    accuracy_rate NUMERIC(5, 2) NOT NULL,
    duration_seconds INTEGER NOT NULL,
    submitted_at BIGINT NOT NULL,
    practice_mode TEXT DEFAULT 'INSTANT',
    session_data JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Tự động thêm cột phòng nếu bảng đã tạo trước đó
ALTER TABLE public.student_attempts ADD COLUMN IF NOT EXISTS room_id TEXT;
ALTER TABLE public.student_attempts ADD COLUMN IF NOT EXISTS room_code TEXT;

-- Index tăng tốc tìm kiếm và sắp xếp kết quả
CREATE INDEX IF NOT EXISTS idx_attempts_submitted_at ON public.student_attempts (submitted_at DESC);
CREATE INDEX IF NOT EXISTS idx_attempts_class_name ON public.student_attempts (class_name);
CREATE INDEX IF NOT EXISTS idx_attempts_student_name ON public.student_attempts (student_name);
CREATE INDEX IF NOT EXISTS idx_attempts_room_code ON public.student_attempts (room_code);

-- 2. BẢNG QUẢN LÝ PHÒNG HỌC & BÁO CÁO THEO LỚP (QUIZ ROOMS)
CREATE TABLE IF NOT EXISTS public.quiz_rooms (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    class_name TEXT NOT NULL,
    quiz_id TEXT NOT NULL,
    quiz_title TEXT NOT NULL,
    room_code TEXT NOT NULL UNIQUE,
    duration_minutes INTEGER DEFAULT 20,
    notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_rooms_class_name ON public.quiz_rooms (class_name);
CREATE INDEX IF NOT EXISTS idx_rooms_code ON public.quiz_rooms (room_code);

-- 3. BẢNG LƯU TRỮ NGÂN HÀNG CÂU HỎI (QUIZ QUESTIONS)
CREATE TABLE IF NOT EXISTS public.quiz_questions (
    id BIGINT PRIMARY KEY,
    part INTEGER DEFAULT 1,
    type TEXT DEFAULT 'MCQ',
    level TEXT,
    prompt TEXT NOT NULL,
    options JSONB,
    correct_index INTEGER,
    explanation TEXT,
    statements JSONB,
    context_title TEXT,
    context_desc TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 4. BẢNG CÀI ĐẶT BÀI TẬP VÀ MÃ PIN (QUIZ SETTINGS)
CREATE TABLE IF NOT EXISTS public.quiz_settings (
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    subject TEXT NOT NULL,
    pin_code TEXT NOT NULL,
    duration_minutes INTEGER DEFAULT 20,
    total_points INTEGER DEFAULT 26,
    is_redo_allowed BOOLEAN DEFAULT TRUE,
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Cài đặt mặc định cho Bài 7
INSERT INTO public.quiz_settings (id, title, subject, pin_code, duration_minutes, total_points, is_redo_allowed)
VALUES ('bai-7-html', 'Bài 7: HTML và Cấu Trúc Trang Web', 'Tin học 12', '789123', 20, 26, true)
ON CONFLICT (id) DO NOTHING;

-- Phòng mẫu mặc định cho các lớp
INSERT INTO public.quiz_rooms (id, name, class_name, quiz_id, quiz_title, room_code, duration_minutes, notes)
VALUES 
  ('room-12a1', 'Phòng Ôn Tập Bài 7 HTML - Lớp 12A1', '12A1', 'bai-7-html', 'Bài 7: HTML và Cấu Trúc Trang Web', '121001', 20, 'Ôn tập trắc nghiệm Bài 7 có chấm điểm hệ số 1. Nộp bài trước 21h00.'),
  ('room-12-1b', 'Phòng Kiểm Tra 15 Phút - Lớp 12/1B', '12/1B', 'bai-7-html', 'Bài 7: HTML và Cấu Trúc Trang Web', '121002', 20, 'Lớp 12/1B vào làm bài nghiêm túc, điểm lưu vĩnh viễn trên Supabase Cloud.'),
  ('room-12a2', 'Phòng Luyện Thi Trắc Nghiệm - Lớp 12A2', '12A2', 'bai-7-html', 'Bài 7: HTML và Cấu Trúc Trang Web', '121003', 20, 'Luyện tập kỹ năng đọc và nhận biết thẻ HTML cấu trúc trang web.')
ON CONFLICT (id) DO NOTHING;

-- 5. CẤP QUYỀN TRUY CẬP CHO VAI TRÒ ANON VÀ AUTHENTICATED (BẮT BUỘC ĐỂ TRÁNH LỖI PERMISSION DENIED)
GRANT USAGE ON SCHEMA public TO anon, authenticated;
GRANT ALL ON TABLE public.student_attempts TO anon, authenticated;
GRANT ALL ON TABLE public.quiz_rooms TO anon, authenticated;
GRANT ALL ON TABLE public.quiz_questions TO anon, authenticated;
GRANT ALL ON TABLE public.quiz_settings TO anon, authenticated;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO anon, authenticated;

-- 6. KÍCH HOẠT ROW LEVEL SECURITY (RLS)
ALTER TABLE public.student_attempts ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.quiz_rooms ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.quiz_questions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.quiz_settings ENABLE ROW LEVEL SECURITY;

-- 7. CHÍNH SÁCH BẢO MẬT (RLS POLICIES)

-- Học sinh và Giáo viên có thể đọc bảng điểm
DROP POLICY IF EXISTS "Cho phep doc ket qua lam bai" ON public.student_attempts;
CREATE POLICY "Cho phep doc ket qua lam bai" ON public.student_attempts
    FOR SELECT USING (true);

-- Cho phép học sinh gửi kết quả làm bài lên hệ thống
DROP POLICY IF EXISTS "Cho phep them ket qua lam bai" ON public.student_attempts;
CREATE POLICY "Cho phep them ket qua lam bai" ON public.student_attempts
    FOR INSERT WITH CHECK (true);

-- Cho phép xóa kết quả (dành cho giáo viên quản trị)
DROP POLICY IF EXISTS "Cho phep quan ly xoa ket qua" ON public.student_attempts;
CREATE POLICY "Cho phep quan ly xoa ket qua" ON public.student_attempts
    FOR DELETE USING (true);

-- Cho phép cập nhật kết quả
DROP POLICY IF EXISTS "Cho phep sua ket qua" ON public.student_attempts;
CREATE POLICY "Cho phep sua ket qua" ON public.student_attempts
    FOR UPDATE USING (true);

-- Quyền cho bảng phòng học (quiz_rooms)
DROP POLICY IF EXISTS "Cho phep doc danh sach phong" ON public.quiz_rooms;
CREATE POLICY "Cho phep doc danh sach phong" ON public.quiz_rooms
    FOR SELECT USING (true);

DROP POLICY IF EXISTS "Cho phep them phong" ON public.quiz_rooms;
CREATE POLICY "Cho phep them phong" ON public.quiz_rooms
    FOR INSERT WITH CHECK (true);

DROP POLICY IF EXISTS "Cho phep cap nhat phong" ON public.quiz_rooms;
CREATE POLICY "Cho phep cap nhat phong" ON public.quiz_rooms
    FOR ALL USING (true);

DROP POLICY IF EXISTS "Cho phep xoa phong" ON public.quiz_rooms;
CREATE POLICY "Cho phep xoa phong" ON public.quiz_rooms
    FOR DELETE USING (true);

-- Quyền đọc câu hỏi cho học sinh làm bài
DROP POLICY IF EXISTS "Cho phep doc ngan hang cau hoi" ON public.quiz_questions;
CREATE POLICY "Cho phep doc ngan hang cau hoi" ON public.quiz_questions
    FOR SELECT USING (true);

-- Quyền quản lý câu hỏi (Thêm/Sửa/Xóa)
DROP POLICY IF EXISTS "Cho phep quan ly cau hoi" ON public.quiz_questions;
CREATE POLICY "Cho phep quan ly cau hoi" ON public.quiz_questions
    FOR ALL USING (true);

-- Quyền đọc & cập nhật cài đặt
DROP POLICY IF EXISTS "Cho phep doc cai dat" ON public.quiz_settings;
CREATE POLICY "Cho phep doc cai dat" ON public.quiz_settings
    FOR SELECT USING (true);

-- Cho phép cập nhật cài đặt
DROP POLICY IF EXISTS "Cho phep cap nhat cai dat" ON public.quiz_settings;
CREATE POLICY "Cho phep cap nhat cai dat" ON public.quiz_settings
    FOR ALL USING (true);

-- 8. BẬT TÍNH NĂNG REALTIME (ĐỂ GIÁO VIÊN NHẬN ĐIỂM HỌC SINH TỨC THÌ QUA WEBSOCKET)
DO $$
BEGIN
    ALTER PUBLICATION supabase_realtime ADD TABLE public.student_attempts;
EXCEPTION WHEN OTHERS THEN
    NULL;
END $$;

DO $$
BEGIN
    ALTER PUBLICATION supabase_realtime ADD TABLE public.quiz_rooms;
EXCEPTION WHEN OTHERS THEN
    NULL;
END $$;
