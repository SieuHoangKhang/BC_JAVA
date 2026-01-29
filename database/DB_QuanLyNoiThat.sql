-- =============================================
-- HỆ THỐNG QUẢN LÝ CỬA HÀNG NỘI THẤT
-- Database: DB_QuanLyNoiThat
-- Tác giả: Theo yêu cầu giáo viên
-- =============================================

-- Tạo database mới
IF EXISTS (SELECT name FROM master.dbo.sysdatabases WHERE name = N'DB_QuanLyNoiThat')
BEGIN
    DROP DATABASE DB_QuanLyNoiThat;
END
GO

CREATE DATABASE DB_QuanLyNoiThat;
GO

USE DB_QuanLyNoiThat;
GO

-- =============================================
-- BẢNG 1: USERS - Người dùng hệ thống
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
    -- Cập nhật tổng tiền của đơn hàng bị ảnh hưởng
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
-- STORED PROCEDURE: Tạo đơn hàng mới
-- =============================================
CREATE PROCEDURE sp_CreateOrder
    @CustomerID INT,
    @UserID INT,
    @PaymentMethod VARCHAR(50),
    @Note NVARCHAR(500),
    @OrderID INT OUTPUT
AS
BEGIN
    INSERT INTO Orders (CustomerID, UserID, PaymentMethod, Note)
    VALUES (@CustomerID, @UserID, @PaymentMethod, @Note);
    
    SET @OrderID = SCOPE_IDENTITY();
END;
GO

-- =============================================
-- STORED PROCEDURE: Thêm sản phẩm vào đơn hàng
-- =============================================
CREATE PROCEDURE sp_AddOrderDetail
    @OrderID INT,
    @ProductID INT,
    @Quantity INT,
    @Discount DECIMAL(5,2) = 0
AS
BEGIN
    DECLARE @UnitPrice DECIMAL(18, 0);
    
    -- Lấy giá sản phẩm hiện tại
    SELECT @UnitPrice = Price FROM Products WHERE ProductID = @ProductID;
    
    -- Thêm vào chi tiết đơn hàng
    INSERT INTO OrderDetails (OrderID, ProductID, Quantity, UnitPrice, Discount)
    VALUES (@OrderID, @ProductID, @Quantity, @UnitPrice, @Discount);
    
    -- Trừ tồn kho
    UPDATE Products SET Stock = Stock - @Quantity WHERE ProductID = @ProductID;
END;
GO

PRINT 'Database DB_QuanLyNoiThat đã được tạo thành công!';

USE DB_QuanLyNoiThat;
SELECT * FROM Users;
SELECT * FROM Products;

USE DB_QuanLyNoiThat;
INSERT INTO Users (Username, Password, FullName, Role) 
VALUES ('admin', '123456', N'Administrator', 'Admin');