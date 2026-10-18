\# Kiểm thử tự động website PrestaShop bằng Selenium



\## 1. Giới thiệu



Dự án xây dựng hệ thống kiểm thử tự động các chức năng giao diện của website thương mại điện tử PrestaShop, sử dụng Selenium WebDriver và Java.



Dự án được thực hiện trong khuôn khổ đồ án môn học, tập trung vào việc thiết kế test case, xây dựng các kịch bản kiểm thử tự động và đánh giá kết quả thực tế của các chức năng trên website.



\## 2. Công nghệ sử dụng



\- \*\*Java:\*\* Ngôn ngữ lập trình xây dựng kịch bản kiểm thử.

\- \*\*Selenium WebDriver:\*\* Tự động hóa thao tác trên trình duyệt.

\- \*\*TestNG:\*\* Tổ chức, quản lý và thực thi các bài kiểm thử.

\- \*\*Maven:\*\* Quản lý thư viện và cấu hình dự án.

\- \*\*Page Object Model (POM):\*\* Tách biệt phần tương tác với giao diện và phần thực thi kiểm thử.

\- \*\*Docker:\*\* Triển khai môi trường chạy PrestaShop và cơ sở dữ liệu.

\- \*\*IntelliJ IDEA:\*\* Môi trường phát triển và thực thi các bài kiểm thử.



\## 3. Các chức năng kiểm thử



Dự án tập trung kiểm thử các chức năng chính trên giao diện người dùng (Front Office) của PrestaShop.



| Chức năng | Nội dung kiểm thử |

|---|---|

| Đăng ký  | Kiểm tra dữ liệu đầu vào, các trường bắt buộc, thông báo lỗi và đăng ký tài khoản |

| Đăng nhập | Kiểm tra thông tin đăng nhập hợp lệ và không hợp lệ |

| Thêm sản phẩm vào giỏ hàng | Kiểm tra thao tác thêm sản phẩm và thông tin sản phẩm hiển thị trong giỏ hàng |

| Thanh toán | Kiểm tra giao diện và các bước trong quy trình thanh toán |



Các kịch bản kiểm thử bao gồm những trường hợp dữ liệu hợp lệ, dữ liệu không hợp lệ và các điều kiện đầu vào khác nhau, tùy theo từng chức năng.



\## 4. Kiến trúc dự án



Dự án áp dụng mô hình Page Object Model (POM) nhằm tăng khả năng tái sử dụng và thuận tiện cho việc bảo trì mã nguồn.



\- \*\*Pages:\*\* Chứa các lớp đại diện cho từng trang, locator và phương thức tương tác với giao diện.

\- \*\*Tests:\*\* Chứa các lớp kiểm thử cho từng chức năng.

\- \*\*BaseTest:\*\* Quản lý các thiết lập dùng chung cho quá trình chạy kiểm thử, nếu được sử dụng trong cấu hình hiện tại.

\- \*\*TestNG XML:\*\* Cấu hình danh sách các lớp kiểm thử và cách tổ chức thực thi bộ kiểm thử.



\## 5. Yêu cầu môi trường



\- JDK tương thích với cấu hình trong `pom.xml`.

\- Apache Maven.

\- IntelliJ IDEA hoặc IDE hỗ trợ Java.

\- Google Chrome và ChromeDriver tương thích.

\- Docker Desktop.

\- Git.



\## 6. Hướng dẫn cài đặt và chạy kiểm thử



\### Bước 1: Tải mã nguồn



```bash

git clone https://github.com/lephuongminh0102/PrestaShop-Selenium-Automation.git

cd PrestaShop-Selenium-Automation

```



\### Bước 2: Khởi chạy môi trường PrestaShop



Mở Docker Desktop và khởi chạy các container theo cấu hình Docker Compose của dự án.



Đảm bảo website PrestaShop đã hoạt động và có thể truy cập tại địa chỉ được cấu hình trong mã nguồn kiểm thử.



\### Bước 3: Mở dự án



Mở thư mục dự án bằng IntelliJ IDEA, sau đó chờ Maven tải các thư viện được khai báo trong `pom.xml`.



\### Bước 4: Chạy toàn bộ bài kiểm thử bằng TestNG



Mở file cấu hình TestNG XML của dự án, nhấp chuột phải vào file và chọn \*\*Run\*\*.



TestNG sẽ thực thi các lớp kiểm thử được khai báo trong file cấu hình. Vì vậy, cần đảm bảo file XML đã liệt kê đầy đủ các lớp test muốn chạy.



Có thể chạy riêng từng lớp kiểm thử bằng cách nhấp chuột phải vào file test tương ứng và chọn \*\*Run\*\*.



\## 7. Đánh giá kết quả kiểm thử



Kết quả thực thi được sử dụng để đối chiếu hành vi thực tế của website với kết quả mong đợi trong các test case.



Thông qua việc tự động hóa các kịch bản kiểm thử, dự án hỗ trợ kiểm tra tính đúng đắn của các chức năng, phát hiện lỗi và giảm thao tác kiểm thử thủ công lặp lại.



\## 8. Phạm vi dự án



Dự án tập trung vào kiểm thử tự động các chức năng giao diện người dùng của PrestaShop, không bao gồm phát triển lại nền tảng hoặc xây dựng module tùy chỉnh ngoài phạm vi đồ án.



\## 9. Repository



Mã nguồn dự án được lưu trữ tại GitHub:



https://github.com/lephuongminh0102/PrestaShop-Selenium-Automation



