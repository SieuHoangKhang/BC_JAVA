-- =============================================
-- DỮ LIỆU MẪU - HỆ THỐNG QUẢN LÝ NỘI THẤT
-- =============================================

USE DB_QuanLyNoiThat;
GO

-- =============================================
-- USERS (bổ sung tài khoản mẫu nếu chưa có)
-- =============================================
IF NOT EXISTS (SELECT 1 FROM Users WHERE Username = 'manager1')
    INSERT INTO Users (Username, Password, FullName, Role) VALUES
    ('manager1', '123456', N'Nguyễn Quản Lý', 'Manager');

IF NOT EXISTS (SELECT 1 FROM Users WHERE Username = 'staff1')
    INSERT INTO Users (Username, Password, FullName, Role) VALUES
    ('staff1', '123456', N'Nhân Viên A', 'Staff');

IF NOT EXISTS (SELECT 1 FROM Users WHERE Username = 'staff2')
    INSERT INTO Users (Username, Password, FullName, Role) VALUES
    ('staff2', '123456', N'Nhân Viên B', 'Staff');
GO

-- =============================================
-- 1. INSERT CATEGORIES
-- =============================================
INSERT INTO Categories (CategoryName, Description) VALUES
(N'Sofa', N'Các loại ghế sofa phòng khách'),
(N'Bàn Ăn', N'Bộ bàn ăn cho gia đình'),
(N'Giường Ngủ', N'Giường ngủ các loại'),
(N'Tủ Quần Áo', N'Tủ đựng quần áo, tủ áo'),
(N'Bàn Làm Việc', N'Bàn học, bàn làm việc'),
(N'Kệ Tủ', N'Kệ sách, tủ tivi, kệ trang trí'),
(N'Ghế Ngồi', N'Ghế ăn, ghế văn phòng'),
(N'Tủ Bếp', N'Tủ bếp, kệ bếp'),
(N'Nội Thất Văn Phòng', N'Bàn ghế văn phòng'),
(N'Phụ Kiện', N'Gương, đèn, tranh trang trí');
GO

-- =============================================
-- 2. INSERT SUPPLIERS
-- =============================================
INSERT INTO Suppliers (SupplierName, ContactPerson, Phone, Email, Address) VALUES
(N'Công ty Nội Thất Hòa Phát', N'Nguyễn Văn Hòa', '0281234567', 'hoaphat@noithat.vn', N'Quận 12, TP.HCM'),
(N'Nội Thất Xinh', N'Trần Thị Xinh', '0287654321', 'info@noithatxinh.com', N'Bình Thạnh, TP.HCM'),
(N'IKEA Vietnam', N'Lê Minh Tuấn', '0289999888', 'ikea@vietnam.com', N'Quận 7, TP.HCM'),
(N'Nội Thất Aconcept', N'Phạm Văn An', '0283456789', 'sales@aconcept.vn', N'Quận 1, TP.HCM'),
(N'Nội Thất Nhập Khẩu Luxury', N'Đỗ Thị Linh', '0285555666', 'luxury@furniture.vn', N'Quận 3, TP.HCM');
GO

-- =============================================
-- 3. INSERT PRODUCTS
-- =============================================
INSERT INTO Products (ProductName, CategoryID, SupplierID, Price, Stock, Description, Material, Size, Color) VALUES
-- SOFA (1)
(N'Sofa Băng 3 Chỗ Hiện Đại', 1, 1, 8500000, 15, N'Sofa băng 3 chỗ phong cách hiện đại, êm ái', N'Vải nhung', N'210x85x75cm', N'Xám đậm'),
(N'Sofa Góc Chữ L Cao Cấp', 1, 3, 15000000, 8, N'Sofa góc chữ L cho phòng khách rộng', N'Da thật', N'280x180x80cm', N'Nâu'),
(N'Sofa Đơn Scandinavian', 1, 2, 4500000, 20, N'Sofa đơn phong cách Bắc Âu', N'Vải bố', N'90x85x75cm', N'Be'),
(N'Sofa Phòng Khách Nhập Khẩu', 1, 5, 18000000, 5, N'Sofa cao cấp nhập khẩu từ Châu Âu', N'Da lộn', N'250x95x90cm', N'Đen'),
(N'Sofa Kiểu Hàn Quốc', 1, 4, 9500000, 12, N'Sofa kiểu Hàn Quốc chân thấp hiện đại', N'Vải cotton blend', N'200x85x70cm', N'Kem'),

-- BÀN ĂN (2)
(N'Bộ Bàn Ăn 6 Ghế Gỗ Sồi', 2, 1, 12000000, 10, N'Bộ bàn ăn gỗ sồi tự nhiên 6 ghế', N'Gỗ sồi', N'160x80x75cm', N'Nâu gỗ'),
(N'Bàn Ăn Tròn 4 Ghế', 2, 2, 6500000, 12, N'Bàn ăn tròn 4 ghế nhỏ gọn', N'MDF phủ veneer', N'Ø100x75cm', N'Trắng'),
(N'Bộ Bàn Ăn Mặt Đá 8 Ghế', 2, 4, 25000000, 5, N'Bàn ăn mặt đá cao cấp 8 ghế', N'Đá marble', N'200x100x75cm', N'Trắng vân đen'),
(N'Bàn Ăn Gỗ Hương 6 Ghế', 2, 1, 16000000, 6, N'Bàn ăn gỗ hương thiết kế cổ điển', N'Gỗ hương', N'170x85x80cm', N'Nâu sáng'),
(N'Bàn Ăn Kiếng Cường Lực 4 Ghế', 2, 3, 7800000, 14, N'Bàn ăn mặt kiếng cường lực 4 chỗ', N'Kiếng + Thép', N'120x70x75cm', N'Trong'),

-- GIƯỜNG NGỦ (3)
(N'Giường Ngủ 1m6 Gỗ Công Nghiệp', 3, 1, 5500000, 18, N'Giường ngủ 1m6 hiện đại có ngăn kéo', N'Gỗ công nghiệp', N'160x200cm', N'Nâu óc chó'),
(N'Giường Ngủ 1m8 Bọc Nỉ', 3, 3, 9000000, 10, N'Giường ngủ 1m8 bọc nỉ cao cấp', N'Gỗ + Nỉ', N'180x200cm', N'Xám'),
(N'Giường Tầng Trẻ Em', 3, 2, 7000000, 8, N'Giường tầng cho bé an toàn', N'Gỗ thông', N'120x200cm', N'Trắng'),
(N'Giường Ngủ Kinh Tế 1m2', 3, 1, 3800000, 22, N'Giường ngủ 1m2 kinh tế chất lượng tốt', N'Gỗ công nghiệp', N'120x200cm', N'Nâu'),
(N'Giường Ngủ Có Đầu Giường', 3, 4, 11000000, 7, N'Giường ngủ 1m8 có đầu giường bọc nỉ', N'Gỗ MDF + Vải', N'180x200cm', N'Trắng kem'),

-- TỦ QUẦN ÁO (4)
(N'Tủ Áo 2 Cánh Gương', 4, 1, 6000000, 12, N'Tủ áo 2 cánh có gương lớn', N'Gỗ công nghiệp', N'120x55x200cm', N'Trắng'),
(N'Tủ Áo 4 Cánh Hiện Đại', 4, 2, 12000000, 7, N'Tủ áo 4 cánh rộng rãi', N'Gỗ MDF', N'220x60x220cm', N'Xám'),
(N'Tủ Áo Cửa Lùa 3 Cánh', 4, 4, 15000000, 5, N'Tủ áo cửa lùa tiết kiệm diện tích', N'Gỗ công nghiệp', N'180x60x220cm', N'Vân gỗ'),
(N'Tủ Áo Mini Văn Phòng', 4, 1, 4500000, 16, N'Tủ áo nhỏ gọn cho phòng riêng', N'Gỗ công nghiệp', N'80x50x180cm', N'Đen'),
(N'Tủ Áo Có Ngăn Kéo', 4, 3, 13500000, 8, N'Tủ áo 3 cánh có ngăn kéo bên dưới', N'Gỗ MDF chất lượng cao', N'200x65x220cm', N'Nâu sáng'),

-- BÀN LÀM VIỆC (5)
(N'Bàn Làm Việc Đơn Giản', 5, 1, 2500000, 25, N'Bàn làm việc 1m2 đơn giản', N'Gỗ công nghiệp', N'120x60x75cm', N'Nâu'),
(N'Bàn Góc Chữ L Văn Phòng', 5, 3, 4500000, 15, N'Bàn góc chữ L cho văn phòng', N'MDF', N'140x120x75cm', N'Đen'),
(N'Bàn Đứng Điều Chỉnh Độ Cao', 5, 5, 8000000, 6, N'Bàn đứng điều chỉnh độ cao tự động', N'Thép + MDF', N'140x70cm', N'Trắng'),
(N'Bàn Học Sinh Gỗ', 5, 1, 1800000, 28, N'Bàn học sinh gỗ công nghiệp', N'Gỗ công nghiệp', N'100x50x75cm', N'Trắng'),
(N'Bàn Làm Việc Cao Cấp Gỗ Walnut', 5, 5, 12500000, 4, N'Bàn làm việc gỗ walnut nguyên khối', N'Gỗ walnut', N'150x75x75cm', N'Nâu đỏ'),

-- KỆ TỦ (6)
(N'Kệ Sách 5 Tầng', 6, 1, 3000000, 20, N'Kệ sách 5 tầng đa năng', N'Gỗ công nghiệp', N'80x30x180cm', N'Vân gỗ'),
(N'Tủ Tivi Hiện Đại 1m8', 6, 2, 5500000, 12, N'Tủ tivi 1m8 có ngăn kệ', N'MDF', N'180x40x50cm', N'Trắng'),
(N'Kệ Trang Trí Treo Tường', 6, 3, 1500000, 30, N'Bộ 3 kệ trang trí treo tường', N'Gỗ', N'60x15cm', N'Đen'),
(N'Tủ Giày Đôi Cửa', 6, 1, 3800000, 14, N'Tủ giày 2 cửa 6 ngăn', N'Gỗ công nghiệp', N'80x35x100cm', N'Trắng kem'),
(N'Kệ Sách 3 Tầng Nhỏ Gọn', 6, 2, 1800000, 35, N'Kệ sách 3 tầng tiết kiệm diện tích', N'Gỗ MDF', N'60x25x90cm', N'Đen'),

-- GHẾ NGỒI (7)
(N'Ghế Ăn Gỗ Cao Su', 7, 1, 800000, 40, N'Ghế ăn gỗ cao su tự nhiên', N'Gỗ cao su', N'45x45x90cm', N'Nâu'),
(N'Ghế Văn Phòng Lưng Lưới', 7, 3, 2500000, 18, N'Ghế văn phòng có tựa lưng lưới', N'Lưới + Nhựa', N'Adjustable', N'Đen'),
(N'Ghế Bar Cao Cấp', 7, 4, 1800000, 15, N'Ghế bar phong cách hiện đại', N'Da + Thép', N'45x110cm', N'Trắng'),
(N'Ghế Ăn Bọc Nỉ', 7, 2, 1200000, 32, N'Ghế ăn bọc nỉ êm ái', N'Gỗ + Nỉ', N'45x45x90cm', N'Xám'),
(N'Ghế Sofa Đơn Nhỏ', 7, 3, 2800000, 20, N'Ghế sofa đơn kích thước nhỏ', N'Vải cotton', N'80x80x75cm', N'Be'),

-- TỦ BẾP (8)
(N'Tủ Bếp Dưới Gỗ Bạch Đàn', 8, 1, 8500000, 9, N'Tủ bếp dưới 1m2 gỗ bạch đàn', N'Gỗ bạch đàn', N'120x60x80cm', N'Nâu gỗ'),
(N'Tủ Bếp Trên Kính Cường Lực', 8, 2, 5500000, 11, N'Tủ bếp trên có kính cường lực', N'Gỗ MDF + Kiếng', N'120x35x70cm', N'Trắng'),
(N'Bộ Tủ Bếp 1m8 Cao Cấp', 8, 4, 22000000, 3, N'Bộ tủ bếp 1m8 dưới + trên hoàn thiện', N'Gỗ công nghiệp cao cấp', N'180x60x150cm', N'Trắng'),
(N'Kệ Bếp Treo Tường Inox', 8, 5, 2800000, 18, N'Kệ bếp treo tường inox 1m2', N'Inox 304', N'120x25cm', N'Bạc'),

-- NƯỚC PHÒNG (9) - Thêm
(N'Bộ Sofa Góc Văn Phòng', 9, 1, 11500000, 6, N'Bộ sofa góc cho văn phòng hiện đại', N'Vải microfiber', N'240x150cm', N'Xám nhạt'),
(N'Bàn Họp Gỗ Dáng Tròn', 9, 3, 7800000, 4, N'Bàn họp tròn cho 6 người', N'Gỗ MDF phủ veneer', N'Ø120x75cm', N'Nâu sáng'),
(N'Ghế Lưng Cao Trưởng Phòng', 9, 4, 6500000, 5, N'Ghế lưng cao chất liệu da', N'Da + Cơ chế lưng cao', N'70x70x120cm', N'Đen'),
(N'Tủ Hồ Sơ 4 Ngăn Kéo', 9, 1, 4200000, 8, N'Tủ hồ sơ 4 ngăn kéo', N'Thép sơn tĩnh điện', N'42x52x130cm', N'Xám'),

-- PHỤ KIỆN (10)
(N'Gương Trang Trí Tròn D60', 10, 5, 1200000, 25, N'Gương tròn trang trí viền đen', N'Gương + Khung kim loại', N'Ø60cm', N'Vàng đồng'),
(N'Đèn Bàn Đọc Sách', 10, 3, 650000, 35, N'Đèn bàn LED chống lóa', N'Kim loại', N'30x40cm', N'Đen'),
(N'Tranh Canvas Trừu Tượng', 10, 5, 800000, 20, N'Bộ 3 tranh canvas nghệ thuật', N'Canvas', N'40x60cm', N'Đa màu'),
(N'Gương Phòng Tắm Hình Chữ Nhật', 10, 2, 950000, 28, N'Gương phòng tắm khung nhôm', N'Gương + Nhôm', N'60x80cm', N'Bạc'),
(N'Đèn Sàn Góc Cổ Điển', 10, 4, 2100000, 10, N'Đèn sàn góc phong cách cổ điển', N'Thép + Vải', N'40x150cm', N'Đen + Be'),
(N'Tấm Giảm Âm Chống Ồn', 10, 5, 500000, 50, N'Bộ 9 tấm giảm âm foam', N'Foam chống cháy', N'30x30x3cm', N'Xám đen'),
(N'Bộ Rèm Cửa Hiện Đại', 10, 3, 1500000, 15, N'Bộ rèm cửa kích thước 2mx1.5m', N'Vải cotton blend', N'200x150cm', N'Trắng kem'),
(N'Trang Trí Tường Gỗ DIY', 10, 1, 300000, 60, N'Trang trí tường gỗ tự lắp ráp', N'Gỗ plywood', N'Various', N'Tự nhiên');
GO

-- =============================================
-- 4. INSERT CUSTOMERS
-- =============================================
INSERT INTO Customers (CustomerName, Phone, Email, Address) VALUES
(N'Nguyễn Văn An', '0912345001', 'nguyenvanan@gmail.com', N'123 Lê Lợi, Q1, TP.HCM'),
(N'Trần Thị Bình', '0912345002', 'tranbinhdh@yahoo.com', N'456 Nguyễn Huệ, Q1, TP.HCM'),
(N'Lê Văn Cường', '0912345003', 'cuongle@outlook.com', N'789 Hai Bà Trưng, Q3, TP.HCM'),
(N'Phạm Thị Dung', '0912345004', NULL, N'321 CMT8, Q10, TP.HCM'),
(N'Hoàng Văn Em', '0912345005', 'emhoang@gmail.com', N'654 Cách Mạng Tháng 8, Q3, TP.HCM'),
(N'Vũ Thị Phương', '0912345006', 'phuongvu@gmail.com', N'987 Võ Văn Tần, Q3, TP.HCM'),
(N'Đỗ Văn Giang', '0912345007', 'giangdo@yahoo.com', N'159 Pasteur, Q1, TP.HCM'),
(N'Bùi Thị Hà', '0912345008', 'habui88@gmail.com', N'753 Đinh Tiên Hoàng, Q1, TP.HCM'),
(N'Ngô Văn Huy', '0912345009', NULL, N'852 Lý Thường Kiệt, Q10, TP.HCM'),
(N'Phan Thị Lan', '0912345010', 'lanphan@gmail.com', N'147 Nguyễn Đình Chiểu, Q3, TP.HCM'),
(N'Trương Văn Kiên', '0912345011', 'kienvuong@gmail.com', N'258 Ung Văn Khiêm, Q4, TP.HCM'),
(N'Lương Thị Mai', '0912345012', 'mailuong23@gmail.com', N'369 Phan Đình Phùng, Q11, TP.HCM'),
(N'Đặng Văn Long', '0912345013', 'longdang@outlook.com', N'741 Tô Ký, Q12, TP.HCM'),
(N'Công ty ABC Nội Thất', '0983456789', 'info@abcnoithat.com.vn', N'159 Khu Công Nghiệp, Q9, TP.HCM'),
(N'Resort Mekong Palace', '0927123456', 'sales@mekongpalace.com', N'Tỉnh Tiền Giang'),
(N'Khách sạn Diamond City', '0911234567', 'procurement@diamondcity.com', N'Quận 1, TP.HCM'),
(N'Cơ Sở Mầm Non Ngôi Sao', '0963456789', 'director@noisisao.edu.vn', N'Quận 7, TP.HCM'),
(N'Văn Phòng Công Ty Ngân Hàng VPBank', '0908765432', 'admin@vpbank.com', N'Quận 3, TP.HCM'),
(N'Nhà Hàng Thanh Tâm', '0944567890', 'contact@thanhtam.com.vn', N'Quận 2, TP.HCM'),
(N'Quán Cà Phê Vintage', '0956789012', NULL, N'Quận 5, TP.HCM');
GO

-- =============================================
-- 5. INSERT SAMPLE ORDERS
-- =============================================
-- Đơn hàng 1
DECLARE @OrderID1 INT;
EXEC sp_CreateOrder 
    @CustomerID = 1, 
    @UserID = 3, 
    @PaymentMethod = N'Tiền mặt',
    @Note = N'Giao hàng giờ hành chính',
    @OrderID = @OrderID1 OUTPUT;

EXEC sp_AddOrderDetail @OrderID1, 1, 1, 0; -- Sofa 3 chỗ
EXEC sp_AddOrderDetail @OrderID1, 16, 2, 0; -- Kệ sách
UPDATE Orders SET Status = 'Completed' WHERE OrderID = @OrderID1;

-- Đơn hàng 2
DECLARE @OrderID2 INT;
EXEC sp_CreateOrder 
    @CustomerID = 2, 
    @UserID = 3, 
    @PaymentMethod = N'Chuyển khoản',
    @Note = NULL,
    @OrderID = @OrderID2 OUTPUT;

EXEC sp_AddOrderDetail @OrderID2, 4, 1, 5; -- Bộ bàn ăn 6 ghế - giảm 5%
EXEC sp_AddOrderDetail @OrderID2, 19, 6, 0; -- Ghế ăn
UPDATE Orders SET Status = 'Completed' WHERE OrderID = @OrderID2;

-- Đơn hàng 3
DECLARE @OrderID3 INT;
EXEC sp_CreateOrder 
    @CustomerID = 3, 
    @UserID = 4, 
    @PaymentMethod = N'Tiền mặt',
    @Note = N'Giao vào cuối tuần',
    @OrderID = @OrderID3 OUTPUT;

EXEC sp_AddOrderDetail @OrderID3, 7, 1, 0; -- Giường 1m6
EXEC sp_AddOrderDetail @OrderID3, 10, 1, 10; -- Tủ áo 2 cánh - giảm 10%
UPDATE Orders SET Status = 'Completed' WHERE OrderID = @OrderID3;

-- Đơn hàng 4 (đang chờ)
DECLARE @OrderID4 INT;
EXEC sp_CreateOrder 
    @CustomerID = 4, 
    @UserID = 3, 
    @PaymentMethod = N'Chuyển khoản',
    @Note = NULL,
    @OrderID = @OrderID4 OUTPUT;

EXEC sp_AddOrderDetail @OrderID4, 2, 1, 0; -- Sofa góc L
EXEC sp_AddOrderDetail @OrderID4, 17, 1, 0; -- Tủ tivi
UPDATE Orders SET Status = 'Processing' WHERE OrderID = @OrderID4;

-- Đơn hàng 5
DECLARE @OrderID5 INT;
EXEC sp_CreateOrder 
    @CustomerID = 5, 
    @UserID = 4, 
    @PaymentMethod = N'Tiền mặt',
    @Note = N'Khách cần tư vấn lắp đặt',
    @OrderID = @OrderID5 OUTPUT;

EXEC sp_AddOrderDetail @OrderID5, 13, 2, 0; -- Bàn làm việc
EXEC sp_AddOrderDetail @OrderID5, 20, 2, 0; -- Ghế văn phòng
UPDATE Orders SET Status = 'Completed' WHERE OrderID = @OrderID5;

-- Đơn hàng 6 - Công ty ABC Nội Thất
DECLARE @OrderID6 INT;
EXEC sp_CreateOrder 
    @CustomerID = 14, 
    @UserID = 2, 
    @PaymentMethod = N'Chuyển khoản',
    @Note = N'Khách hàng doanh nghiệp - Dự toán lớn',
    @OrderID = @OrderID6 OUTPUT;

EXEC sp_AddOrderDetail @OrderID6, 29, 10, 15; -- Sofa góc văn phòng - giảm 15%
EXEC sp_AddOrderDetail @OrderID6, 31, 8, 10; -- Ghế lưng cao - giảm 10%
EXEC sp_AddOrderDetail @OrderID6, 32, 12, 0; -- Tủ hồ sơ
UPDATE Orders SET Status = 'Completed' WHERE OrderID = @OrderID6;

-- Đơn hàng 7 - Resort Mekong Palace
DECLARE @OrderID7 INT;
EXEC sp_CreateOrder 
    @CustomerID = 15, 
    @UserID = 3, 
    @PaymentMethod = N'Chuyển khoản',
    @Note = N'Nội thất cho phòng resort - Giao nhiều đợt',
    @OrderID = @OrderID7 OUTPUT;

EXEC sp_AddOrderDetail @OrderID7, 1, 20, 20; -- Sofa 3 chỗ - giảm 20%
EXEC sp_AddOrderDetail @OrderID7, 5, 15, 15; -- Sofa Hàn Quốc - giảm 15%
EXEC sp_AddOrderDetail @OrderID7, 10, 30, 20; -- Ghế ăn - giảm 20%
EXEC sp_AddOrderDetail @OrderID7, 18, 50, 20; -- Tranh canvas - giảm 20%
UPDATE Orders SET Status = 'Processing' WHERE OrderID = @OrderID7;

-- Đơn hàng 8 - Khách sạn Diamond City
DECLARE @OrderID8 INT;
EXEC sp_CreateOrder 
    @CustomerID = 16, 
    @UserID = 4, 
    @PaymentMethod = N'Chuyển khoản',
    @Note = N'Nội thất phòng khách sạn 5 sao',
    @OrderID = @OrderID8 OUTPUT;

EXEC sp_AddOrderDetail @OrderID8, 2, 5, 25; -- Sofa góc L - giảm 25%
EXEC sp_AddOrderDetail @OrderID8, 8, 3, 15; -- Bộ bàn ăn đá - giảm 15%
EXEC sp_AddOrderDetail @OrderID8, 35, 15, 0; -- Gương phòng tắm
UPDATE Orders SET Status = 'Completed' WHERE OrderID = @OrderID8;

-- Đơn hàng 9 - Cơ sở mầm non
DECLARE @OrderID9 INT;
EXEC sp_CreateOrder 
    @CustomerID = 17, 
    @UserID = 3, 
    @PaymentMethod = N'Tiền mặt',
    @Note = N'Nội thất cho phòng học trẻ em',
    @OrderID = @OrderID9 OUTPUT;

EXEC sp_AddOrderDetail @OrderID9, 9, 8, 10; -- Giường tầng trẻ em - giảm 10%
EXEC sp_AddOrderDetail @OrderID9, 24, 12, 10; -- Kệ sách nhỏ - giảm 10%
EXEC sp_AddOrderDetail @OrderID9, 38, 30, 0; -- Trang trí tường
UPDATE Orders SET Status = 'Completed' WHERE OrderID = @OrderID9;

-- Đơn hàng 10 - Văn phòng VPBank
DECLARE @OrderID10 INT;
EXEC sp_CreateOrder 
    @CustomerID = 18, 
    @UserID = 2, 
    @PaymentMethod = N'Chuyển khoản',
    @Note = N'Nâng cấp nội thất văn phòng tầng 10',
    @OrderID = @OrderID10 OUTPUT;

EXEC sp_AddOrderDetail @OrderID10, 11, 20, 20; -- Bàn góc L - giảm 20%
EXEC sp_AddOrderDetail @OrderID10, 21, 25, 15; -- Ghế văn phòng lưng lưới - giảm 15%
EXEC sp_AddOrderDetail @OrderID10, 30, 6, 10; -- Bàn họp - giảm 10%
EXEC sp_AddOrderDetail @OrderID10, 34, 10, 0; -- Kệ bếp inox
UPDATE Orders SET Status = 'Processing' WHERE OrderID = @OrderID10;

-- Đơn hàng 11 - Nhà Hàng Thanh Tâm
DECLARE @OrderID11 INT;
EXEC sp_CreateOrder 
    @CustomerID = 19, 
    @UserID = 4, 
    @PaymentMethod = N'Tiền mặt',
    @Note = N'Bộ bàn ghế ăn cho nhà hàng',
    @OrderID = @OrderID11 OUTPUT;

EXEC sp_AddOrderDetail @OrderID11, 4, 6, 20; -- Bộ bàn ăn 6 ghế - giảm 20%
EXEC sp_AddOrderDetail @OrderID11, 6, 4, 15; -- Bàn ăn tròn - giảm 15%
EXEC sp_AddOrderDetail @OrderID11, 19, 24, 15; -- Ghế ăn bọc nỉ - giảm 15%
UPDATE Orders SET Status = 'Completed' WHERE OrderID = @OrderID11;

-- Đơn hàng 12 - Quán Cà Phê Vintage
DECLARE @OrderID12 INT;
EXEC sp_CreateOrder 
    @CustomerID = 20, 
    @UserID = 3, 
    @PaymentMethod = N'Tiền mặt',
    @Note = N'Nội thất cà phê kiểu vintage',
    @OrderID = @OrderID12 OUTPUT;

EXEC sp_AddOrderDetail @OrderID12, 3, 8, 10; -- Sofa đơn Scandinavian - giảm 10%
EXEC sp_AddOrderDetail @OrderID12, 22, 10, 0; -- Ghế bar
EXEC sp_AddOrderDetail @OrderID12, 36, 20, 0; -- Gương trang trí
EXEC sp_AddOrderDetail @OrderID12, 37, 15, 0; -- Đèn sàn
UPDATE Orders SET Status = 'Completed' WHERE OrderID = @OrderID12;

GO

-- =============================================
-- 6. INSERT INVENTORY TRANSACTIONS
-- =============================================
-- Nhập kho từ nhà cung cấp tháng 1
INSERT INTO Inventory (ProductID, TransactionType, Quantity, Note, UserID) VALUES
-- SOFA - nhập từ nhà cung cấp
(1, 'In', 20, N'Nhập Sofa 3 chỗ từ Hòa Phát - Lô 01/2026', 2),
(2, 'In', 10, N'Nhập Sofa góc từ IKEA - Lô 01/2026', 2),
(4, 'In', 8, N'Nhập Sofa IKEA từ IKEA Vietnam - Lô 01/2026', 2),
(5, 'In', 12, N'Nhập Sofa Hàn Quốc từ Aconcept - Lô 01/2026', 2),

-- BÀN ĂN - nhập từ nhà cung cấp
(6, 'In', 12, N'Nhập Bộ bàn ăn 6 ghế từ Hòa Phát - Lô 01/2026', 2),
(7, 'In', 15, N'Nhập Bàn ăn tròn từ Nội Thất Xinh - Lô 01/2026', 2),
(8, 'In', 6, N'Nhập Bộ bàn ăn đá từ Aconcept - Lô 01/2026', 2),
(9, 'In', 8, N'Nhập Bàn ăn gỗ hương từ Hòa Phát - Lô 01/2026', 2),

-- GIƯỜNG - nhập từ nhà cung cấp
(11, 'In', 20, N'Nhập Giường 1m6 từ Hòa Phát - Lô 01/2026', 2),
(12, 'In', 12, N'Nhập Giường 1m8 bọc nỉ từ IKEA Vietnam - Lô 01/2026', 2),
(13, 'In', 10, N'Nhập Giường tầng từ Nội Thất Xinh - Lô 01/2026', 2),
(14, 'In', 25, N'Nhập Giường 1m2 kinh tế từ Hòa Phát - Lô 01/2026', 2),

-- TỦ ÁO - nhập từ nhà cung cấp
(15, 'In', 15, N'Nhập Tủ áo 2 cánh từ Hòa Phát - Lô 01/2026', 2),
(16, 'In', 8, N'Nhập Tủ áo 4 cánh từ Nội Thất Xinh - Lô 01/2026', 2),
(17, 'In', 6, N'Nhập Tủ áo cửa lùa từ Aconcept - Lô 01/2026', 2),

-- BÀN LÀM VIỆC - nhập từ nhà cung cấp
(20, 'In', 30, N'Nhập Bàn làm việc đơn từ Hòa Phát - Lô 01/2026', 2),
(21, 'In', 18, N'Nhập Bàn góc chữ L từ IKEA Vietnam - Lô 01/2026', 2),
(22, 'In', 8, N'Nhập Bàn đứng điều chỉnh từ Nhập Khẩu Luxury - Lô 01/2026', 2),

-- KỆ TỦ - nhập từ nhà cung cấp
(24, 'In', 25, N'Nhập Kệ sách 5 tầng từ Hòa Phát - Lô 01/2026', 2),
(25, 'In', 15, N'Nhập Tủ tivi 1m8 từ Nội Thất Xinh - Lô 01/2026', 2),
(26, 'In', 35, N'Nhập Kệ trang trí từ IKEA Vietnam - Lô 01/2026', 2),

-- GHẾ NGỒI - nhập từ nhà cung cấp
(27, 'In', 45, N'Nhập Ghế ăn gỗ từ Hòa Phát - Lô 01/2026', 2),
(28, 'In', 20, N'Nhập Ghế văn phòng lưng lưới từ IKEA Vietnam - Lô 01/2026', 2),
(29, 'In', 18, N'Nhập Ghế bar từ Aconcept - Lô 01/2026', 2),

-- PHỤ KIỆN - nhập từ nhà cung cấp
(34, 'In', 30, N'Nhập Gương trang trí từ Nhập Khẩu Luxury - Lô 01/2026', 2),
(35, 'In', 40, N'Nhập Đèn bàn LED từ IKEA Vietnam - Lô 01/2026', 2),
(36, 'In', 25, N'Nhập Tranh canvas từ Nhập Khẩu Luxury - Lô 01/2026', 2),
(37, 'In', 30, N'Nhập Gương phòng tắm từ Nội Thất Xinh - Lô 01/2026', 2),
(38, 'In', 12, N'Nhập Đèn sàn góc từ Aconcept - Lô 01/2026', 2),

-- Các giao dịch xuất do bán hàng (thực hiện sau mỗi bán hàng - tự động hoặc manual)
(1, 'Out', 1, N'Xuất Sofa 3 chỗ - Đơn hàng #1', 3),
(4, 'Out', 1, N'Xuất Bộ bàn ăn 6 ghế - Đơn hàng #2', 3),
(7, 'Out', 1, N'Xuất Giường 1m6 - Đơn hàng #3', 4),
(2, 'Out', 1, N'Xuất Sofa góc - Đơn hàng #4', 3),
(20, 'Out', 2, N'Xuất Bàn làm việc - Đơn hàng #5', 4),

-- Nhập bổ sung lần 2
(1, 'In', 5, N'Nhập bổ sung Sofa 3 chỗ - Lô 02/2026', 2),
(6, 'In', 5, N'Nhập bổ sung Bộ bàn ăn - Lô 02/2026', 2),
(27, 'In', 15, N'Nhập bổ sung Ghế ăn - Lô 02/2026', 2),

-- Xuất do bán hàng khách doanh nghiệp
(29, 'Out', 10, N'Xuất Sofa góc văn phòng - Đơn hàng #6', 2),
(31, 'Out', 8, N'Xuất Ghế lưng cao - Đơn hàng #6', 2),
(1, 'Out', 20, N'Xuất Sofa 3 chỗ - Đơn hàng #7', 3),
(5, 'Out', 15, N'Xuất Sofa Hàn Quốc - Đơn hàng #7', 3),
(27, 'Out', 30, N'Xuất Ghế ăn - Đơn hàng #7', 3),

-- Xuất hàng hành chính khách sạn
(2, 'Out', 5, N'Xuất Sofa góc - Đơn hàng #8', 4),
(8, 'Out', 3, N'Xuất Bộ bàn ăn đá - Đơn hàng #8', 4),

-- Xuất cho mầm non
(13, 'Out', 8, N'Xuất Giường tầng - Đơn hàng #9', 3),

-- Xuất cho văn phòng
(21, 'Out', 20, N'Xuất Bàn góc L - Đơn hàng #10', 2),
(28, 'Out', 25, N'Xuất Ghế văn phòng - Đơn hàng #10', 2),

-- Xuất cho nhà hàng
(6, 'Out', 6, N'Xuất Bộ bàn ăn - Đơn hàng #11', 4),
(7, 'Out', 4, N'Xuất Bàn ăn tròn - Đơn hàng #11', 4),
(25, 'Out', 8, N'Xuất Tủ tivi - Đơn hàng #11', 4),

-- Xuất cho cà phê
(3, 'Out', 8, N'Xuất Sofa Scandinavian - Đơn hàng #12', 3),
(29, 'Out', 10, N'Xuất Ghế bar - Đơn hàng #12', 3);

GO

-- Cập nhật tổng mua hàng của khách
UPDATE Customers 
SET TotalPurchase = (
    SELECT ISNULL(SUM(o.TotalAmount), 0)
    FROM Orders o
    WHERE o.CustomerID = Customers.CustomerID AND o.Status = 'Completed'
);
GO

-- =============================================
-- THỐNG KÊ DỮ LIỆU MẪU
-- =============================================
PRINT '╔════════════════════════════════════════════════════════════════════╗';
PRINT '║      DỮ LIỆU MẪU ĐÃ ĐƯỢC THÊM VÀO HỆ THỐNG THÀNH CÔNG!          ║';
PRINT '╚════════════════════════════════════════════════════════════════════╝';
PRINT '';
PRINT '📊 THỐNG KÊ DỮ LIỆU:';
PRINT '───────────────────────────────────────────';
SELECT '👥 Người dùng' AS Category, COUNT(*) AS Count FROM Users;
SELECT '📂 Danh mục' AS Category, COUNT(*) AS Count FROM Categories;
SELECT '🏭 Nhà cung cấp' AS Category, COUNT(*) AS Count FROM Suppliers;
SELECT '🛋️ Sản phẩm' AS Category, COUNT(*) AS Count FROM Products WHERE IsActive = 1;
SELECT '👤 Khách hàng' AS Category, COUNT(*) AS Count FROM Customers;
SELECT '📦 Đơn hàng' AS Category, COUNT(*) AS Count FROM Orders;
SELECT '💰 Tổng doanh thu' AS Category, CAST(SUM(TotalAmount) AS NVARCHAR(50)) AS Count FROM Orders WHERE Status = 'Completed';
PRINT '───────────────────────────────────────────';
PRINT '';
PRINT '🔑 TÀI KHOẢN ĐĂNG NHẬP MẶC ĐỊNH:';
PRINT '───────────────────────────────────────────';
PRINT '  ✓ Username: admin      | Password: 123456 | Role: Admin';
PRINT '  ✓ Username: manager1   | Password: 123456 | Role: Manager';
PRINT '  ✓ Username: staff1     | Password: 123456 | Role: Staff';
PRINT '  ✓ Username: staff2     | Password: 123456 | Role: Staff';
PRINT '───────────────────────────────────────────';
PRINT '';
PRINT '📦 DANH SÁCH SẢN PHẨM MẪU:';
PRINT '  • 5 Sofa các loại (1 - 5)';
PRINT '  • 5 Bàn ăn (6 - 10)';
PRINT '  • 5 Giường ngủ (11 - 15)';
PRINT '  • 5 Tủ quần áo (16 - 20)';
PRINT '  • 5 Bàn làm việc (21 - 25)';
PRINT '  • 5 Kệ tủ (26 - 30)';
PRINT '  • 5 Ghế ngồi (31 - 35)';
PRINT '  • 4 Tủ bếp (36 - 39)';
PRINT '  • 4 Nội thất văn phòng (40 - 43)';
PRINT '  • 8 Phụ kiện (44 - 51)';
PRINT '───────────────────────────────────────────';
PRINT '';
PRINT '🏢 KHÁCH HÀNG MẪU:';
PRINT '  • 10 khách hàng cá nhân (ID 1-10)';
PRINT '  • 10 khách hàng doanh nghiệp (ID 11-20)';
PRINT '    - Công ty ABC Nội Thất';
PRINT '    - Resort Mekong Palace';
PRINT '    - Khách sạn Diamond City';
PRINT '    - Cơ sở Mầm non Ngôi Sao';
PRINT '    - Văn phòng VPBank';
PRINT '    - Nhà hàng Thanh Tâm';
PRINT '    - Quán cà phê Vintage';
PRINT '───────────────────────────────────────────';
PRINT '';
PRINT '📋 ĐƠN HÀNG & GIAO DỊCH:';
PRINT '  • 12 đơn hàng mẫu (từ khách hàng cá nhân và doanh nghiệp)';
PRINT '  • Đơn hàng hoàn thành: 8 đơn';
PRINT '  • Đơn hàng đang xử lý: 4 đơn';
PRINT '  • 50+ giao dịch kho (nhập/xuất)';
PRINT '───────────────────────────────────────────';
PRINT '';
