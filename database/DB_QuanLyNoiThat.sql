-- =============================================
-- HE THONG QUAN LY CUA HANG NOI THAT
-- Database: DB_QuanLyNoiThat
-- Phien ban hoan chinh voi du lieu mau
-- =============================================

-- Ngat ket noi va xoa database cu (neu ton tai)
USE master;
GO

IF EXISTS (SELECT name FROM sys.databases WHERE name = N'DB_QuanLyNoiThat')
BEGIN
    -- Dong tat ca cac ket noi hien tai
    ALTER DATABASE DB_QuanLyNoiThat SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE DB_QuanLyNoiThat;
END
GO

-- Tao database moi
CREATE DATABASE DB_QuanLyNoiThat;
GO

USE DB_QuanLyNoiThat;
GO

-- =============================================
-- BANG 1: USERS - Nguoi dung he thong
-- =============================================
CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(100) NOT NULL,
    FullName NVARCHAR(100) NOT NULL,
    Role VARCHAR(20) NOT NULL CHECK (Role IN ('Admin', 'Manager', 'Staff')),
    Phone VARCHAR(15),
    Email VARCHAR(100),
    CreatedDate DATETIME DEFAULT GETDATE(),
    IsActive BIT DEFAULT 1
);
GO

-- =============================================
-- BẢNG PHÂN QUYỀN: ROLE_PERMISSIONS
-- =============================================
CREATE TABLE RolePermissions (
    Role VARCHAR(20) NOT NULL,
    Module VARCHAR(50) NOT NULL,
    Action VARCHAR(20) NOT NULL,
    Allowed BIT NOT NULL DEFAULT 0,
    PRIMARY KEY (Role, Module, Action)
);
GO

-- Seed quyền mặc định
INSERT INTO RolePermissions (Role, Module, Action, Allowed) VALUES
-- Admin: full quyền
('Admin','PRODUCT','VIEW',1),('Admin','PRODUCT','CREATE',1),('Admin','PRODUCT','UPDATE',1),('Admin','PRODUCT','DELETE',1),
('Admin','CATEGORY','VIEW',1),('Admin','CATEGORY','CREATE',1),('Admin','CATEGORY','UPDATE',1),('Admin','CATEGORY','DELETE',1),
('Admin','SUPPLIER','VIEW',1),('Admin','SUPPLIER','CREATE',1),('Admin','SUPPLIER','UPDATE',1),('Admin','SUPPLIER','DELETE',1),
('Admin','CUSTOMER','VIEW',1),('Admin','CUSTOMER','CREATE',1),('Admin','CUSTOMER','UPDATE',1),('Admin','CUSTOMER','DELETE',1),
('Admin','INVENTORY','VIEW',1),('Admin','INVENTORY','CREATE',1),('Admin','INVENTORY','UPDATE',1),('Admin','INVENTORY','DELETE',1),
('Admin','ORDER','VIEW',1),('Admin','ORDER','CREATE',1),('Admin','ORDER','UPDATE',1),('Admin','ORDER','DELETE',1),
('Admin','REPORT','VIEW',1),('Admin','REPORT','EXPORT',1),('Admin','REPORT','PRINT',1),
-- Manager: không xóa
('Manager','PRODUCT','VIEW',1),('Manager','PRODUCT','CREATE',1),('Manager','PRODUCT','UPDATE',1),('Manager','PRODUCT','DELETE',0),
('Manager','CATEGORY','VIEW',1),('Manager','CATEGORY','CREATE',1),('Manager','CATEGORY','UPDATE',1),('Manager','CATEGORY','DELETE',0),
('Manager','SUPPLIER','VIEW',1),('Manager','SUPPLIER','CREATE',1),('Manager','SUPPLIER','UPDATE',1),('Manager','SUPPLIER','DELETE',0),
('Manager','CUSTOMER','VIEW',1),('Manager','CUSTOMER','CREATE',1),('Manager','CUSTOMER','UPDATE',1),('Manager','CUSTOMER','DELETE',0),
('Manager','INVENTORY','VIEW',1),('Manager','INVENTORY','CREATE',1),('Manager','INVENTORY','UPDATE',1),('Manager','INVENTORY','DELETE',0),
('Manager','ORDER','VIEW',1),('Manager','ORDER','CREATE',1),('Manager','ORDER','UPDATE',1),('Manager','ORDER','DELETE',0),
('Manager','REPORT','VIEW',1),('Manager','REPORT','EXPORT',1),('Manager','REPORT','PRINT',1),
-- Staff: hạn chế
('Staff','PRODUCT','VIEW',1),('Staff','PRODUCT','CREATE',0),('Staff','PRODUCT','UPDATE',0),('Staff','PRODUCT','DELETE',0),
('Staff','CATEGORY','VIEW',0),('Staff','CATEGORY','CREATE',0),('Staff','CATEGORY','UPDATE',0),('Staff','CATEGORY','DELETE',0),
('Staff','SUPPLIER','VIEW',0),('Staff','SUPPLIER','CREATE',0),('Staff','SUPPLIER','UPDATE',0),('Staff','SUPPLIER','DELETE',0),
('Staff','CUSTOMER','VIEW',1),('Staff','CUSTOMER','CREATE',1),('Staff','CUSTOMER','UPDATE',1),('Staff','CUSTOMER','DELETE',0),
('Staff','INVENTORY','VIEW',1),('Staff','INVENTORY','CREATE',1),('Staff','INVENTORY','UPDATE',0),('Staff','INVENTORY','DELETE',0),
('Staff','ORDER','VIEW',1),('Staff','ORDER','CREATE',1),('Staff','ORDER','UPDATE',1),('Staff','ORDER','DELETE',0),
('Staff','REPORT','VIEW',1),('Staff','REPORT','EXPORT',0),('Staff','REPORT','PRINT',0);
GO

-- =============================================
-- BẢNG 2: CATEGORIES - Danh mục sản phẩm
-- =============================================
CREATE TABLE Categories (
    CategoryID INT IDENTITY(1,1) PRIMARY KEY,
    CategoryName NVARCHAR(100) NOT NULL,
    Description NVARCHAR(500),
    CreatedDate DATETIME DEFAULT GETDATE()
);
GO

-- =============================================
-- BẢNG 3: SUPPLIERS - Nhà cung cấp
-- =============================================
CREATE TABLE Suppliers (
    SupplierID INT IDENTITY(1,1) PRIMARY KEY,
    SupplierName NVARCHAR(200) NOT NULL,
    ContactPerson NVARCHAR(100),
    Phone VARCHAR(15) NOT NULL,
    Email VARCHAR(100),
    Address NVARCHAR(300),
    CreatedDate DATETIME DEFAULT GETDATE(),
    IsActive BIT DEFAULT 1
);
GO

-- =============================================
-- BẢNG 4: PRODUCTS - Sản phẩm nội thất
-- =============================================
CREATE TABLE Products (
    ProductID INT IDENTITY(1,1) PRIMARY KEY,
    ProductName NVARCHAR(200) NOT NULL,
    CategoryID INT NOT NULL,
    SupplierID INT NOT NULL,
    Price DECIMAL(18, 0) NOT NULL CHECK (Price >= 0),
    Stock INT NOT NULL DEFAULT 0 CHECK (Stock >= 0),
    Description NVARCHAR(1000),
    ImagePath VARCHAR(300),
    Material NVARCHAR(100),
    Size NVARCHAR(50),
    Color NVARCHAR(50),
    CreatedDate DATETIME DEFAULT GETDATE(),
    IsActive BIT DEFAULT 1,
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID),
    FOREIGN KEY (SupplierID) REFERENCES Suppliers(SupplierID)
);
GO

-- =============================================
-- BẢNG 5: CUSTOMERS - Khách hàng
-- =============================================
CREATE TABLE Customers (
    CustomerID INT IDENTITY(1,1) PRIMARY KEY,
    CustomerName NVARCHAR(100) NOT NULL,
    Phone VARCHAR(15) NOT NULL,
    Email VARCHAR(100),
    Address NVARCHAR(300),
    CreatedDate DATETIME DEFAULT GETDATE(),
    TotalPurchase DECIMAL(18, 0) DEFAULT 0
);
GO

-- =============================================
-- BẢNG 6: ORDERS - Đơn hàng
-- =============================================
CREATE TABLE Orders (
    OrderID INT IDENTITY(1,1) PRIMARY KEY,
    CustomerID INT NOT NULL,
    UserID INT NOT NULL,
    OrderDate DATETIME DEFAULT GETDATE(),
    TotalAmount DECIMAL(18, 0) NOT NULL DEFAULT 0,
    Status VARCHAR(20) NOT NULL DEFAULT 'Pending' CHECK (Status IN ('Pending', 'Processing', 'Completed', 'Cancelled')),
    PaymentMethod VARCHAR(50),
    Note NVARCHAR(500),
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO

-- =============================================
-- BẢNG 7: ORDER_DETAILS - Chi tiết đơn hàng
-- =============================================
CREATE TABLE OrderDetails (
    OrderDetailID INT IDENTITY(1,1) PRIMARY KEY,
    OrderID INT NOT NULL,
    ProductID INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    UnitPrice DECIMAL(18, 0) NOT NULL,
    Discount DECIMAL(5, 2) DEFAULT 0 CHECK (Discount >= 0 AND Discount <= 100),
    Subtotal AS (Quantity * UnitPrice * (1 - Discount/100)) PERSISTED,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID) ON DELETE CASCADE,
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);
GO

-- =============================================
-- BẢNG 8: INVENTORY - Quản lý kho
-- =============================================
CREATE TABLE Inventory (
    InventoryID INT IDENTITY(1,1) PRIMARY KEY,
    ProductID INT NOT NULL,
    TransactionType VARCHAR(10) NOT NULL CHECK (TransactionType IN ('In', 'Out')),
    Quantity INT NOT NULL CHECK (Quantity > 0),
    TransactionDate DATETIME DEFAULT GETDATE(),
    Note NVARCHAR(500),
    UserID INT NOT NULL,
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO

-- =============================================
-- TRIGGER: Tự động cập nhật tồn kho khi nhập/xuất
-- =============================================
CREATE TRIGGER trg_UpdateStock 
ON Inventory
AFTER INSERT
AS
BEGIN
    DECLARE @ProductID INT, @Quantity INT, @Type VARCHAR(10);
    
    SELECT @ProductID = ProductID, @Quantity = Quantity, @Type = TransactionType
    FROM inserted;
    
    IF @Type = 'In'
        UPDATE Products SET Stock = Stock + @Quantity WHERE ProductID = @ProductID;
    ELSE IF @Type = 'Out'
        UPDATE Products SET Stock = Stock - @Quantity WHERE ProductID = @ProductID;
END;
GO

-- =============================================
-- TRIGGER: Tự động cập nhật tổng tiền đơn hàng
-- =============================================
CREATE TRIGGER trg_UpdateOrderTotal 
ON OrderDetails
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    UPDATE Orders
    SET TotalAmount = (
        SELECT ISNULL(SUM(Subtotal), 0)
        FROM OrderDetails
        WHERE OrderID = Orders.OrderID
    )
    WHERE OrderID IN (
        SELECT DISTINCT OrderID FROM inserted
        UNION
        SELECT DISTINCT OrderID FROM deleted
    );
END;
GO

-- =============================================
-- VIEW: Thống kê sản phẩm
-- =============================================
CREATE VIEW vw_ProductStats AS
SELECT 
    p.ProductID,
    p.ProductName,
    c.CategoryName,
    s.SupplierName,
    p.Price,
    p.Stock,
    ISNULL(SUM(od.Quantity), 0) AS TotalSold,
    ISNULL(SUM(od.Subtotal), 0) AS TotalRevenue
FROM Products p
LEFT JOIN Categories c ON p.CategoryID = c.CategoryID
LEFT JOIN Suppliers s ON p.SupplierID = s.SupplierID
LEFT JOIN OrderDetails od ON p.ProductID = od.ProductID
LEFT JOIN Orders o ON od.OrderID = o.OrderID AND o.Status = 'Completed'
WHERE p.IsActive = 1
GROUP BY p.ProductID, p.ProductName, c.CategoryName, s.SupplierName, p.Price, p.Stock;
GO

-- =============================================
-- =================== DỮ LIỆU MẪU ===================
-- =============================================

-- Thêm Users
INSERT INTO Users (Username, Password, FullName, Role, Phone, Email) VALUES
('admin', '123456', N'Administrator', 'Admin', '0901234567', 'admin@noithat.com'),
('manager1', '123456', N'Nguyễn Văn Quản Lý', 'Manager', '0901234568', 'manager@noithat.com'),
('staff1', '123456', N'Trần Thị Nhân Viên', 'Staff', '0901234569', 'staff1@noithat.com'),
('staff2', '123456', N'Lê Văn Nhân Viên 2', 'Staff', '0901234570', 'staff2@noithat.com');
GO

-- Thêm Categories
INSERT INTO Categories (CategoryName, Description) VALUES
(N'Ghế Sofa', N'Các loại ghế sofa da, vải, nỉ cao cấp'),
(N'Bàn Gỗ', N'Bàn gỗ tự nhiên và gỗ công nghiệp'),
(N'Tủ Quần Áo', N'Tủ quần áo các kích thước'),
(N'Giường Ngủ', N'Giường ngủ đơn, đôi, king size'),
(N'Kệ Sách', N'Kệ sách và tủ trang trí'),
(N'Bàn Trang Điểm', N'Bàn trang điểm có gương'),
(N'Đèn Trang Trí', N'Đèn chùm, đèn bàn, đèn sàn'),
(N'Phụ Kiện', N'Các phụ kiện trang trí nội thất');
GO

-- Thêm Suppliers
INSERT INTO Suppliers (SupplierName, ContactPerson, Phone, Email, Address) VALUES
(N'Công Ty Gỗ Mỹ Nghệ Xanh', N'Phạm Văn Gỗ', '0912345678', 'gonhap@xanh.vn', N'123 Đường Gỗ, Quận 5, TP.HCM'),
(N'HTX Mỹ Nghệ Đồ Gỗ', N'Nguyễn Thị Mộc', '0912345679', 'monghe@htx.vn', N'456 Đường Hàng Thủ Công, Long An'),
(N'Công Ty Nội Thất Star', N'Lê Hùng Star', '0912345680', 'star@noithat.vn', N'789 Đường Công Nghiệp, Bình Dương'),
(N'Nhà Máy Sản Xuất Nội Thất Kim Oanh', N'Trần Kim Oanh', '0912345681', 'kimoanh@nm.vn', N'321 Đường Sản Xuất, Đồng Nai');
GO

-- Thêm Products
INSERT INTO Products (ProductName, CategoryID, SupplierID, Price, Stock, Description, Material, Size, Color) VALUES
(N'Sofa Da Cao Cấp 3 Chỗ', 1, 1, 15000000, 10, N'Sofa da thật nhập khẩu, bền đẹp', N'Da thật', '220x90x85cm', N'Nâu'),
(N'Sofa Vải Xanh Navy', 1, 2, 8500000, 15, N'Sofa vải nỉ cao cấp, màu xanh navy đẹp', N'Vải nỉ', '200x85x80cm', N'Xanh Navy'),
(N'Bàn Gỗ Teak Cao Cấp', 2, 1, 6500000, 20, N'Bàn gỗ teak tự nhiên, chân thép không gỉ', N'Gỗ Teak', '160x80x75cm', N'Nâu tự nhiên'),
(N'Bàn Trà Oval Gỗ Sồi', 2, 3, 4200000, 25, N'Bàn trà hình oval, gỗ sồi Mỹ', N'Gỗ Sồi', '120x60x45cm', N'Nâu sáng'),
(N'Tủ Quần Áo 4 Cánh', 3, 2, 12500000, 8, N'Tủ quần áo rộng rãi, có gương', N'Gỗ MDF', '200x60x220cm', N'Trắng'),
(N'Tủ Trang Trí Đa Năng', 3, 4, 5800000, 12, N'Tủ trang trí có ngăn kéo', N'Gỗ công nghiệp', '120x40x180cm', N'Vàng óc chó'),
(N'Giường Ngủ King Size', 4, 1, 18500000, 5, N'Giường ngủ cỡ lớn, đầu giường bọc da', N'Gỗ + Da', '200x200cm', N'Xám'),
(N'Giường Ngủ Đơn Trẻ Em', 4, 3, 7500000, 15, N'Giường ngủ đơn cho trẻ em, màu sắc tươi sáng', N'Gỗ thông', '120x190cm', N'Hồng'),
(N'Kệ Sách 5 Tầng', 5, 2, 2800000, 30, N'Kệ sách hiện đại, tối giản', N'Gỗ MDF', '80x30x180cm', N'Trắng'),
(N'Kệ Trang Trí Treo Tường', 5, 4, 1200000, 40, N'Kệ treo tường mini, trang trí', N'Gỗ tre', '60x20x25cm', N'Nâu'),
(N'Bàn Trang Điểm Gương Tròn', 6, 1, 4500000, 18, N'Bàn trang điểm có gương tròn, đèn LED', N'Gỗ + Kính', '90x40x75cm', N'Trắng'),
(N'Bàn Trang Điểm Có Ngăn', 6, 3, 3800000, 22, N'Bàn trang điểm nhiều ngăn kéo', N'Gỗ MDF', '80x45x70cm', N'Hồng nhạt'),
(N'Đèn Chùm Pha Lê', 7, 1, 25000000, 3, N'Đèn chùm pha lê pha lê cao cấp', N'Pha lê + Kim loại', '80x80x60cm', N'Trong suốt'),
(N'Đèn Bàn Đọc Sách LED', 7, 4, 850000, 50, N'Đèn bàn LED, điều chỉnh độ sáng', N'Kim loại + Nhựa', '40x20x45cm', N'Đen'),
(N'Khung Tranh Treo Tường', 8, 2, 350000, 60, N'Khung tranh trang trí đa kích thước', N'Gỗ + Kính', '50x70cm', N'Vàng'),
(N'Gương Trang Trí Vuông', 8, 1, 1200000, 35, N'Gương trang trí hình vuông viền kim loại', N'Kính + Kim loại', '60x60cm', N'Bạc');
GO

-- Thêm Customers
INSERT INTO Customers (CustomerName, Phone, Email, Address, TotalPurchase) VALUES
(N'Nguyễn Thị Lan', '0909876541', 'lan.nguyen@email.com', N'123 Lê Lợi, Quận 1, TP.HCM', 25000000),
(N'Trần Văn Minh', '0909876542', 'minh.tran@email.com', N'456 Nguyễn Trãi, Quận 5, TP.HCM', 18500000),
(N'Lê Hoàng Mai', '0909876543', 'mai.le@email.com', N'789 Điện Biên Phủ, Quận 3, TP.HCM', 42000000),
(N'Phạm Quốc Hùng', '0909876544', 'hung.pham@email.com', N'321 Võ Văn Ngân, Thủ Đức', 15000000),
(N'Đặng Thị Hà', '0909876545', 'ha.dang@email.com', N'654 Cộng Hòa, Tân Bình', 32000000),
(N'Bùi Văn Tài', '0909876546', 'tai.bui@email.com', N'987 Lý Thường Kiệt, Quận 10', 28000000),
(N'Vũ Thị Thu', '0909876547', 'thu.vu@email.com', N'147 Hoàng Văn Thụ, Phú Nhuận', 9500000),
(N'Đinh Văn Nam', '0909876548', 'nam.dinh@email.com', N'258 Vạn Hạnh, Quận 10', 16500000);
GO

-- Thêm Orders (Đơn hàng mẫu)
INSERT INTO Orders (CustomerID, UserID, TotalAmount, Status, PaymentMethod, Note) VALUES
(1, 1, 15000000, 'Completed', 'Tiền mặt', N'Khách hàng VIP'),
(2, 2, 8500000, 'Completed', 'Chuyển khoản', N'Giao hàng tận nơi'),
(3, 1, 12500000, 'Processing', 'Thẻ tín dụng', N'Yêu cầu giao buổi tối'),
(4, 3, 6500000, 'Pending', 'Tiền mặt', N'Liên hệ trước khi giao'),
(5, 2, 18500000, 'Completed', 'Trả góp', N'Trả góp 6 tháng'),
(6, 1, 4200000, 'Completed', 'Chuyển khoản', N'Khách quen'),
(7, 3, 28000000, 'Cancelled', 'Tiền mặt', N'Hủy do khách đổi ý'),
(8, 2, 12000000, 'Processing', 'Thẻ tín dụng', N'Giao hàng ngoại thành');
GO

-- Thêm Order Details
INSERT INTO OrderDetails (OrderID, ProductID, Quantity, UnitPrice, Discount) VALUES
(1, 1, 1, 15000000, 0),
(2, 2, 1, 8500000, 0),
(3, 5, 1, 12500000, 0),
(4, 3, 1, 6500000, 0),
(5, 7, 1, 18500000, 5),
(6, 4, 1, 4200000, 0),
(7, 10, 10, 1200000, 10),
(8, 6, 2, 5800000, 5);
GO

-- Thêm Inventory Transactions
INSERT INTO Inventory (ProductID, TransactionType, Quantity, Note, UserID) VALUES
(1, 'In', 15, N'Nhập hàng đầu tháng', 1),
(2, 'In', 20, N'Nhập hàng đầu tháng', 1),
(3, 'In', 25, N'Nhập hàng đầu tháng', 2),
(4, 'In', 30, N'Nhập hàng đầu tháng', 2),
(5, 'In', 10, N'Nhập hàng đầu tháng', 1),
(6, 'In', 15, N'Nhập hàng đầu tháng', 3),
(7, 'In', 8, N'Nhập hàng đầu tháng', 1),
(8, 'In', 20, N'Nhập hàng đầu tháng', 2),
(9, 'In', 35, N'Nhập hàng đầu tháng', 3),
(10, 'In', 50, N'Nhập hàng đầu tháng', 2),
(1, 'Out', 5, N'Bán cho khách', 1),
(2, 'Out', 5, N'Bán cho khách', 2),
(3, 'Out', 5, N'Bán cho khách', 1);
GO

PRINT '===========================================';
PRINT '  Database DB_QuanLyNoiThat đã được tạo thành công!';
PRINT '  Tổng số: 4 Users, 8 Categories, 4 Suppliers';
PRINT '  16 Products, 8 Customers, 8 Orders, 8 OrderDetails';
PRINT '===========================================';

-- Kiểm tra dữ liệu
SELECT 'Users:' AS N'Tiêu đề';
SELECT * FROM Users;

SELECT 'Categories:' AS N'Tiêu đề';
SELECT * FROM Categories;

SELECT 'Suppliers:' AS N'Tiêu đề';
SELECT * FROM Suppliers;

SELECT 'Products:' AS N'Tiêu đề';
SELECT * FROM Products;

SELECT 'Customers:' AS N'Tiêu đề';
SELECT * FROM Customers;
