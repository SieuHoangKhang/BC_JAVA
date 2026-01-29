@echo off
chcp 65001 >nul
echo ========================================
echo  PUSH PROJECT LEN GITHUB
echo  Repository: https://github.com/SieuHoangKhang/BC_JAVA.git
echo ========================================
echo.

cd /d "%~dp0"

echo [1/6] Dang kiem tra trang thai Git...
git status
echo.

echo [2/6] Them cac thay doi...
git add -A
echo.

echo [3/6] Nhap noi dung commit (hoac nhan Enter de dung mac dinh)...
set /p commit_msg="Nhap noi dung commit: "
if "%commit_msg%"=="" set commit_msg=Cap nhat du an QuanLyNoiThat

git commit -m "%commit_msg%"
echo.

echo [4/6] Dang lay cap nhat tu remote...
git pull origin main --allow-unrelated-histories
echo.

echo [5/6] Dang day len GitHub...
git push -u origin main
echo.

echo [6/6] Hoan thanh!
echo ========================================
echo Du an da duoc day len GitHub thanh cong!
echo Xem tai: https://github.com/SieuHoangKhang/BC_JAVA
echo ========================================

pause
