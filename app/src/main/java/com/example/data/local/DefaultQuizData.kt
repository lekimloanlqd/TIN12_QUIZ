package com.example.data.local

import com.example.data.model.QuestionItem
import com.example.data.model.QuestionType
import com.example.data.model.Quiz
import com.example.data.model.TrueFalseStatement

object DefaultQuizData {
    val defaultQuiz = Quiz(
        id = "bai-7-html",
        title = "Bài 7 – HTML và Cấu trúc trang web",
        subject = "Tin học 12",
        grade = "Khối 12",
        teacherName = "Lưu Gia Ly (GV Tin học 12/1B)",
        pinCode = "789123",
        durationMinutes = 20,
        isOpen = true,
        allowRetake = true,
        totalMultipleChoice = 10,
        totalTrueFalseContexts = 4,
        totalEvaluationPoints = 26
    )

    val defaultClasses = listOf(
        "12/1B", "12A1", "12A2", "12A3", "12A4", "12A5", "12Tin", "12Toan"
    )

    val sampleQuizzes = listOf(
        defaultQuiz,
        Quiz(
            id = "bai-8-dinh-dang-van-ban",
            title = "Bài 8 – Định dạng văn bản và siêu liên kết",
            subject = "Tin học 12",
            grade = "Khối 12",
            teacherName = "Lưu Gia Ly (GV Tin học)",
            pinCode = "456789",
            durationMinutes = 15,
            isOpen = true,
            allowRetake = true,
            totalMultipleChoice = 10,
            totalTrueFalseContexts = 4,
            totalEvaluationPoints = 26
        ),
        Quiz(
            id = "bai-9-bang-bieu-hinh-anh",
            title = "Bài 9 – Bảng biểu và chèn hình ảnh đa phương tiện",
            subject = "Tin học 12",
            grade = "Khối 12",
            teacherName = "Tổ Tin Học",
            pinCode = "123456",
            durationMinutes = 25,
            isOpen = true,
            allowRetake = true,
            totalMultipleChoice = 10,
            totalTrueFalseContexts = 4,
            totalEvaluationPoints = 26
        )
    )

    val questions: List<QuestionItem> = listOf(
        // ================= PHẦN 1: 10 CÂU TRẮC NGHIỆM NHIỀU LỰA CHỌN =================
        QuestionItem(
            id = 1,
            part = 1,
            questionNumber = 1,
            type = QuestionType.MULTIPLE_CHOICE,
            level = "Nhận biết",
            prompt = "HTML là viết tắt của cụm từ nào sau đây?",
            options = listOf(
                "A. Hypertext Markup Language.",
                "B. Hyperlink Text Management Language.",
                "C. Hightext Markup Language.",
                "D. Hypermedia Text Management Language."
            ),
            correctOptionIndex = 0,
            explanation = "HTML là viết tắt của Hypertext Markup Language, nghĩa là ngôn ngữ đánh dấu siêu văn bản."
        ),
        QuestionItem(
            id = 2,
            part = 1,
            questionNumber = 2,
            type = QuestionType.MULTIPLE_CHOICE,
            level = "Nhận biết",
            prompt = "Một tệp HTML thường có phần mở rộng nào sau đây?",
            options = listOf(
                "A. .doc hoặc .docx.",
                "B. .html hoặc .htm.",
                "C. .ppt hoặc .pptx.",
                "D. .jpg hoặc .png."
            ),
            correctOptionIndex = 1,
            explanation = "Tệp HTML thường có phần mở rộng .html hoặc .htm."
        ),
        QuestionItem(
            id = 3,
            part = 1,
            questionNumber = 3,
            type = QuestionType.MULTIPLE_CHOICE,
            level = "Nhận biết",
            prompt = "Các kí tự đánh dấu HTML (tag) được viết trong cặp dấu nào?",
            options = listOf(
                "A. Dấu ngoặc tròn \"(\", \")\".",
                "B. Dấu ngoặc vuông \"[\", \"]\".",
                "C. Dấu \"<\", \">\".",
                "D. Dấu ngoặc nhọn \"{\", \"}\"."
            ),
            correctOptionIndex = 2,
            explanation = "Thẻ HTML được viết trong cặp dấu \"<\", \">\"."
        ),
        QuestionItem(
            id = 4,
            part = 1,
            questionNumber = 4,
            type = QuestionType.MULTIPLE_CHOICE,
            level = "Nhận biết",
            prompt = "Phát biểu nào sau đây đúng về dòng <!DOCTYPE html> trong tệp HTML?",
            options = listOf(
                "A. Đây là một phần tử HTML bắt buộc nằm trong <body>.",
                "B. Đây là thẻ đóng của phần tử <html>.",
                "C. Đây là nội dung sẽ hiển thị trực tiếp trên trang web.",
                "D. Đây là dòng khai báo cho trình duyệt biết tệp có định dạng HTML."
            ),
            correctOptionIndex = 3,
            explanation = "<!DOCTYPE html> thông báo cho trình duyệt biết đây là tệp có định dạng HTML; sách coi đây là dòng khai báo HTML, không phải phần tử HTML."
        ),
        QuestionItem(
            id = 5,
            part = 1,
            questionNumber = 5,
            type = QuestionType.MULTIPLE_CHOICE,
            level = "Thông hiểu",
            prompt = "Phương án nào mô tả đúng nhất về \"phần tử HTML\"?",
            options = listOf(
                "A. Chỉ là tên của một thẻ HTML được viết giữa hai dấu \"<\", \">\".",
                "B. Là toàn bộ phần thẻ và nội dung của thẻ trong trang web.",
                "C. Chỉ là phần nội dung văn bản được trình duyệt hiển thị.",
                "D. Là tên tệp HTML kèm theo phần mở rộng .html của tệp."
            ),
            correctOptionIndex = 1,
            explanation = "Phần tử HTML được hiểu là toàn bộ phần thẻ và nội dung của thẻ."
        ),
        QuestionItem(
            id = 6,
            part = 1,
            questionNumber = 6,
            type = QuestionType.MULTIPLE_CHOICE,
            level = "Thông hiểu",
            prompt = "Trong cấu trúc tệp HTML, quan hệ giữa phần tử <div> và phần tử <h1> được đặt bên trong <div> có thể hình dung như thế nào?",
            options = listOf(
                "A. Quan hệ giữa hai phần tử hoàn toàn độc lập.",
                "B. Quan hệ giữa hai phần tử cùng nằm ở nút gốc.",
                "C. Quan hệ cha – con trong cây thông tin HTML.",
                "D. Quan hệ giữa trình duyệt và tệp văn bản nguồn."
            ),
            correctOptionIndex = 2,
            explanation = "Các phần tử HTML có thể lồng nhau và quan hệ lồng nhau có thể hình dung như quan hệ cha – con trong cây thông tin."
        ),
        QuestionItem(
            id = 7,
            part = 1,
            questionNumber = 7,
            type = QuestionType.MULTIPLE_CHOICE,
            level = "Thông hiểu",
            prompt = "Trong cấu trúc cơ bản của tệp HTML, phần tử <head> có vai trò nào sau đây?",
            options = listOf(
                "A. Chứa các phần tử liên quan chung đến toàn bộ trang web.",
                "B. Chứa tất cả thông tin nội dung sẽ hiển thị trên trang web.",
                "C. Chỉ chứa các đoạn văn bản được trình bày bằng thẻ <p>.",
                "D. Chỉ chứa các hình ảnh được chèn trực tiếp vào trang web."
            ),
            correctOptionIndex = 0,
            explanation = "<head> chứa các phần tử liên quan chung đến toàn bộ trang web; <title>, <meta>, <style>, <script> thường có thể nằm trong phần này."
        ),
        QuestionItem(
            id = 8,
            part = 1,
            questionNumber = 8,
            type = QuestionType.MULTIPLE_CHOICE,
            level = "Vận dụng thực tế",
            prompt = "Bạn An muốn đặt nội dung chính của trang web, như tiêu đề, đoạn văn và các phần tử nội dung khác. Theo cấu trúc cơ bản của HTML, An nên đặt các nội dung đó chủ yếu trong phần tử nào?",
            options = listOf(
                "A. <meta>.",
                "B. <title>.",
                "C. <head>.",
                "D. <body>."
            ),
            correctOptionIndex = 3,
            explanation = "<body> chứa tất cả các phần tử còn lại là thông tin của trang web."
        ),
        QuestionItem(
            id = 9,
            part = 1,
            questionNumber = 9,
            type = QuestionType.MULTIPLE_CHOICE,
            level = "Vận dụng thực tế",
            prompt = "Một bạn viết đoạn mã: <p>Trang này do <em>nhóm bạn bè</em> lập</p>. Nhận xét nào phù hợp với kiến thức về cấu trúc HTML?",
            options = listOf(
                "A. <em> là phần tử gốc của toàn bộ tệp HTML.",
                "B. Phần tử <em> được lồng bên trong phần tử <p>.",
                "C. Phần tử <p> được lồng bên trong phần tử <em>.",
                "D. Hai phần tử <p> và <em> luôn phải đứng độc lập."
            ),
            correctOptionIndex = 1,
            explanation = "<em> nằm bên trong <p>, vì vậy phần tử <em> là phần tử con được lồng trong phần tử <p>."
        ),
        QuestionItem(
            id = 10,
            part = 1,
            questionNumber = 10,
            type = QuestionType.MULTIPLE_CHOICE,
            level = "Vận dụng thực tế",
            prompt = "Một học sinh sử dụng trang web hỗ trợ soạn thảo HTML trực tuyến. Theo bài học, thao tác nào phù hợp với cách làm này?",
            options = listOf(
                "A. Soạn mã ở khung bên phải rồi tắt trình duyệt để xem kết quả.",
                "B. Soạn mã trong Notepad và không cần kết nối Internet.",
                "C. Soạn mã ở khung bên trái, nháy Run để kiểm tra kết quả ở khung bên phải.",
                "D. Chỉ cần nhập tên tệp HTML, hệ thống sẽ tự tạo toàn bộ mã nguồn."
            ),
            correctOptionIndex = 2,
            explanation = "Với trình soạn thảo HTML trực tuyến, mã được soạn ở khung bên trái và có thể nháy Run để kiểm tra kết quả ở khung bên phải; cách này cần Internet và trình duyệt."
        ),

        // ================= PHẦN 2: 4 BỐI CẢNH ĐÚNG / SAI (16 Ý ĐÁNH GIÁ) =================
        QuestionItem(
            id = 11,
            part = 2,
            questionNumber = 1,
            type = QuestionType.TRUE_FALSE_CONTEXT,
            level = "4 cấp độ (Biết, Hiểu, Vận dụng)",
            prompt = "Xác định các phát biểu sau là Đúng hay Sai theo bối cảnh quan sát trang web.",
            contextTitle = "Bối cảnh 1 – Quan sát một trang web",
            contextDescription = "Mai mở một trang web bằng trình duyệt và đồng thời xem tệp văn bản nguồn của trang. Cô nhận thấy nội dung trong tệp nguồn có các kí hiệu như <html>, <head>, <body>, <p>.",
            statements = listOf(
                TrueFalseStatement(
                    key = "a",
                    level = "Biết",
                    text = "HTML là viết tắt của Hypertext Markup Language.",
                    isTrue = true,
                    explanation = "HTML là viết tắt của Hypertext Markup Language."
                ),
                TrueFalseStatement(
                    key = "b",
                    level = "Hiểu",
                    text = "Tệp văn bản nguồn của trang web có thể chứa các thẻ HTML dùng để xác định các phần tử HTML.",
                    isTrue = true,
                    explanation = "Các thẻ HTML xác định các phần tử HTML trong tệp nguồn."
                ),
                TrueFalseStatement(
                    key = "c",
                    level = "Hiểu",
                    text = "Trình duyệt web có chức năng hiển thị thông tin của trang web theo định dạng được thiết lập trong HTML.",
                    isTrue = true,
                    explanation = "Trình duyệt có chức năng hiển thị nội dung trang web theo định dạng đã thiết lập."
                ),
                TrueFalseStatement(
                    key = "d",
                    level = "Vận dụng",
                    text = "Vì trang web được hiển thị bằng trình duyệt nên tệp nguồn HTML không cần tồn tại dưới dạng tệp văn bản.",
                    isTrue = false,
                    explanation = "Trang HTML được thiết lập từ các tệp văn bản; việc hiển thị bằng trình duyệt không loại bỏ vai trò của tệp nguồn."
                )
            )
        ),

        QuestionItem(
            id = 12,
            part = 2,
            questionNumber = 2,
            type = QuestionType.TRUE_FALSE_CONTEXT,
            level = "4 cấp độ (Biết, Hiểu, Vận dụng)",
            prompt = "Xác định các phát biểu sau là Đúng hay Sai về nhận diện thẻ và phần tử HTML.",
            contextTitle = "Bối cảnh 2 – Nhận diện thẻ và phần tử",
            contextDescription = "Nam có đoạn mã: <p>Trang này được lập bởi <em>nhóm bạn bè</em></p>. Bạn ấy muốn hiểu cách các phần tử HTML lồng nhau.",
            statements = listOf(
                TrueFalseStatement(
                    key = "a",
                    level = "Biết",
                    text = "<p> và <em> đều là các thẻ HTML xuất hiện trong đoạn mã.",
                    isTrue = true,
                    explanation = "<p> và <em> là các thẻ HTML."
                ),
                TrueFalseStatement(
                    key = "b",
                    level = "Hiểu",
                    text = "Phần tử HTML có thể hiểu là toàn bộ phần thẻ và nội dung của thẻ.",
                    isTrue = true,
                    explanation = "Phần tử HTML gồm thẻ và nội dung của thẻ."
                ),
                TrueFalseStatement(
                    key = "c",
                    level = "Hiểu",
                    text = "Trong đoạn mã trên, <em> được lồng bên trong <p>, nên có thể hình dung quan hệ giữa chúng là cha – con.",
                    isTrue = true,
                    explanation = "<em> được lồng trong <p>, nên có quan hệ cha – con trong cây thông tin."
                ),
                TrueFalseStatement(
                    key = "d",
                    level = "Vận dụng",
                    text = "Nếu đổi vị trí để <p> nằm hoàn toàn bên trong <em>, cấu trúc sẽ vẫn giống hệt đoạn mã ban đầu vì thứ tự lồng nhau không ảnh hưởng đến quan hệ phần tử.",
                    isTrue = false,
                    explanation = "Thay đổi quan hệ lồng nhau sẽ làm thay đổi cấu trúc cây; không thể coi hai cách sắp xếp là hoàn toàn giống nhau."
                )
            )
        ),

        QuestionItem(
            id = 13,
            part = 2,
            questionNumber = 3,
            type = QuestionType.TRUE_FALSE_CONTEXT,
            level = "4 cấp độ (Biết, Hiểu, Vận dụng)",
            prompt = "Xác định các phát biểu sau là Đúng hay Sai về tổ chức cấu trúc tệp HTML.",
            contextTitle = "Bối cảnh 3 – Tổ chức cấu trúc tệp HTML",
            contextDescription = "Lan đang tạo một tệp HTML có cấu trúc gồm <!DOCTYPE html>, <html>, <head>, <body>. Bạn ấy cần xác định nơi đặt thông tin chung và nội dung trang.",
            statements = listOf(
                TrueFalseStatement(
                    key = "a",
                    level = "Biết",
                    text = "Phần tử <html> là phần tử bắt buộc và là phần tử gốc của cây HTML.",
                    isTrue = true,
                    explanation = "<html> là phần tử bắt buộc và là nút gốc của cây HTML."
                ),
                TrueFalseStatement(
                    key = "b",
                    level = "Hiểu",
                    text = "Phần tử <head> thường chứa các phần tử liên quan chung đến toàn bộ trang web, trong đó có thể có <title>.",
                    isTrue = true,
                    explanation = "<head> chứa thông tin liên quan chung đến toàn bộ trang; <title> thường nằm trong đó."
                ),
                TrueFalseStatement(
                    key = "c",
                    level = "Hiểu",
                    text = "Phần tử <meta> nằm trong <head> và có thể dùng để mô tả thông tin bổ sung như cách mã hoá Unicode.",
                    isTrue = true,
                    explanation = "<meta> nằm trong <head> và có thể mô tả thông tin bổ sung như cách mã hoá Unicode."
                ),
                TrueFalseStatement(
                    key = "d",
                    level = "Vận dụng",
                    text = "Muốn đưa các đoạn văn bản nội dung của trang vào đúng khu vực hiển thị chính, Lan nên đặt chúng trong <head> thay vì <body>.",
                    isTrue = false,
                    explanation = "<body> mới là nơi chứa các phần tử còn lại là thông tin của trang web; không phải <head>."
                )
            )
        ),

        QuestionItem(
            id = 14,
            part = 2,
            questionNumber = 4,
            type = QuestionType.TRUE_FALSE_CONTEXT,
            level = "4 cấp độ (Biết, Hiểu, Vận dụng)",
            prompt = "Xác định các phát biểu sau là Đúng hay Sai về các phương tiện và phần mềm soạn thảo HTML.",
            contextTitle = "Bối cảnh 4 – Soạn thảo HTML",
            contextDescription = "Huy muốn tạo tệp HTML. Bạn ấy cân nhắc dùng Notepad, Notepad++, Sublime Text hoặc một trang soạn thảo HTML trực tuyến.",
            statements = listOf(
                TrueFalseStatement(
                    key = "a",
                    level = "Biết",
                    text = "Notepad là phần mềm soạn thảo văn bản đơn giản, có sẵn trên hệ điều hành Windows.",
                    isTrue = true,
                    explanation = "Notepad là trình soạn thảo văn bản đơn giản có sẵn trên Windows."
                ),
                TrueFalseStatement(
                    key = "b",
                    level = "Hiểu",
                    text = "Notepad++ là phần mềm mã nguồn mở, miễn phí và hỗ trợ soạn thảo chương trình với nhiều ngôn ngữ, trong đó có HTML.",
                    isTrue = true,
                    explanation = "Notepad++ là phần mềm mã nguồn mở, miễn phí và hỗ trợ nhiều ngôn ngữ, có HTML."
                ),
                TrueFalseStatement(
                    key = "c",
                    level = "Hiểu",
                    text = "Khi dùng trang hỗ trợ soạn thảo HTML trực tuyến, máy tính cần có kết nối Internet và trình duyệt; người dùng có thể nháy Run để kiểm tra kết quả.",
                    isTrue = true,
                    explanation = "Soạn thảo HTML trực tuyến cần Internet và trình duyệt; Run dùng để kiểm tra kết quả."
                ),
                TrueFalseStatement(
                    key = "d",
                    level = "Vận dụng",
                    text = "Các trình duyệt như Chrome, Firefox và Microsoft Edge được xếp cùng nhóm với Notepad++ và Sublime Text vì đều là phần mềm soạn thảo HTML.",
                    isTrue = false,
                    explanation = "Chrome, Firefox và Microsoft Edge là trình duyệt web, không phải nhóm phần mềm soạn thảo HTML như Notepad++ và Sublime Text."
                )
            )
        )
    )
}
