# QuanLyNoiThat - Hệ Thống Quản Lý Nội Thất

Ứng dụng quản lý bán hàng nội thất với Java Swing + SQL Server.

## 🚀 Cách Chạy Qua NetBeans

### 1. Chuẩn Bị CSDL
Mở **SQL Server Management Studio** và chạy:
```
database/DB_QuanLyNoiThat.sql
database/SampleData.sql
```

### 2. Mở Dự Án Trong NetBeans
- File → Open Project → Chọn thư mục `QuanLyNoiThat`
- NetBeans tự động nhận diện project.xml

### 3. Build & Run
- Nhấn **F11** để Build
- Nhấn **F6** để Run
- Hoặc: Run → Run Project

### 4. Đăng Nhập
```
Tài khoản: admin
Mật khẩu: 123456
```

## 📋 Cấu Trúc Dự Án

```
QuanLyNoiThat/
├── src/noithat/
│   ├── Main.java              (Entry point)
│   ├── database/
│   ├── models/
│   ├── utils/
│   └── views/
├── lib/                        (JDBC Driver)
├── database/                   (SQL scripts)
├── build.xml                   (Ant build file)
├── manifest.mf                 (Manifest)
└── README.md                   (File này)
```

## 🔑 Thông Tin Kết Nối CSDL

- **Server**: localhost:1433
- **Database**: DB_QuanLyNoiThat
- **User**: sa
- **Password**: 123

## 👥 Tài Khoản Test

| Tài Khoản | Mật Khẩu | Vai Trò |
|-----------|----------|--------|
| admin | 123456 | Admin |
| manager1 | 123456 | Manager |
| staff1 | 123456 | Staff |
| staff2 | 123456 | Staff |

## ✨ Chức Năng Chính

- ✅ Quản lý sản phẩm (CRUD)
- ✅ Quản lý khách hàng
- ✅ Quản lý đơn hàng
- ✅ Quản lý kho
- ✅ Quản lý danh mục
- ✅ Quản lý nhà cung cấp
- ✅ Báo cáo & thống kê
- ✅ Kiểm soát quyền hạn
- ✅ Ghi log hoạt động

## 💻 Yêu Cầu

- Java JDK 11+
- SQL Server 2019+
- NetBeans 12+

## 🐛 Troubleshooting

**Lỗi kết nối CSDL:**
- Kiểm tra SQL Server đang chạy
- Kiểm tra database đã được tạo
- Kiểm tra DatabaseHelper.java có đúng host/port/user

**Lỗi Build:**
- Clean & Build project lại (Shift+F11)
- Kiểm tra lib folder có JDBC driver

---

**Version**: 1.0 | **Status**: Ready ✅
- [x] Định dạng tiền: 1.500.000,00 đ
- [x] Định dạng ngày: dd/MM/yyyy HH:mm:ss

## 📊 Database Schema

### Các bảng chính:
1. **Users** - Người dùng hệ thống
   - UserID, Username, Password, FullName, RoleID, Phone, Email, IsActive

2. **Roles** - Vai trò người dùng
   - RoleID, RoleName (Admin, Manager, Staff)

3. **RolePermissions** - Phân quyền chi tiết (63 rows)
   - RoleID, Feature (Sản Phẩm, Đơn Hàng, etc), Action (CREATE, READ, UPDATE, DELETE, EXPORT)

4. **Categories** - Danh mục sản phẩm
   - CategoryID, CategoryName, Description, IsActive

5. **Suppliers** - Nhà cung cấp
   - SupplierID, SupplierName, ContactPerson, Phone, Email, Address, IsActive

6. **Products** - Sản phẩm nội thất
   - ProductID, ProductName, Description, Unit, Price, CategoryID, SupplierID, IsActive

7. **Customers** - Khách hàng
   - CustomerID, CustomerName, Phone, Email, Address, TotalSpent, IsActive

8. **Orders** - Đơn hàng
   - OrderID, CustomerID, OrderDate, DeliveryDate, TotalAmount, Status, Notes, CreatedBy

9. **OrderDetails** - Chi tiết đơn hàng
   - OrderDetailID, OrderID, ProductID, Quantity, UnitPrice, TotalPrice

10. **Inventory** - Lịch sử kho
    - InventoryID, ProductID, Quantity, LastUpdated

11. **ActivityLog** - Log hoạt động (ghi file)
    - Timestamp, UserID, Username, Action, Details

## 🎨 Screenshots

### Màn hình đăng nhập
[Screenshot sẽ được thêm vào sau khi chạy thử]

### Dashboard chính
[Screenshot sẽ được thêm vào sau khi chạy thử]

### Quản lý sản phẩm
[Screenshot sẽ được thêm vào sau khi chạy thử]

## ❗ Xử lý lỗi thường gặp

### Lỗi: "Không thể kết nối database"
- Kiểm tra SQL Server đã chạy chưa
- Kiểm tra tên server, port, username, password trong `DatabaseHelper.java`
- Kiểm tra firewall có block port 1433 không

### Lỗi: "Không tìm thấy JDBC Driver"
- Kiểm tra đã thêm file `mssql-jdbc-xxx.jar` vào thư mục `lib/` chưa
- Kiểm tra đã add JAR vào classpath trong NetBeans chưa

### Lỗi: "Login không thành công"
- Kiểm tra đã chạy script `SampleData.sql` chưa
- Thử đăng nhập với: username=`admin`, password=`123456`

## 📝 Ghi chú
- Hệ thống sử dụng **Soft Delete** - khi xóa dữ liệu sẽ set `IsActive = 0` thay vì xóa vĩnh viễn
- Password hiện tại lưu dạng plain text (chỉ cho mục đích học tập)
- Một số chức năng vẫn đang trong giai đoạn phát triển

## 📞 Liên hệ
- **Sinh viên**: [Tên sinh viên]
- **MSSV**: [Mã số sinh viên]
- **Giáo viên hướng dẫn**: Cô Trinh
- **Email**: admin@noithat.com

## 📜 License
Dự án này được phát triển cho mục đích học tập.

---
**Lưu ý**: Đây là phiên bản 1.0 đang được phát triển. Một số chức năng có thể chưa hoàn thiện.
