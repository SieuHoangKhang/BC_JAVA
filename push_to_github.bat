@echo off
chcp 65001 >nul
echo ============================================
echo  PUSH CODE TO GITHUB - QuanLyNoiThat
echo ============================================
echo.

REM Navigate to project directory using short path
cd /d %USERPROFILE%\..\Admin\.cursor\projects\d-MON-HOC-JAVA-C-TRINH-DO-AN-JAVA-QuanLyNoiThat

echo [1/6] Checking git repository...
if exist .git (
    echo     ✓ Git repository exists
) else (
    echo     Initializing new git repository...
    git init
)

echo.
echo [2/6] Setting remote origin...
git remote remove origin 2>nul
git remote add origin https://github.com/SieuHoangKhang/BC_JAVA.git
echo     Remote: https://github.com/SieuHoangKhang/BC_JAVA.git

echo.
echo [3/6] Adding files...
git add -A

echo.
echo [4/6] Creating commit...
set /p commit_msg="Enter commit message (or press Enter for default): "
if "%commit_msg%"=="" set commit_msg="Initial commit - QuanLyNoiThat Java Swing Application"
git commit -m "%commit_msg%"

echo.
echo [5/6] Fetching and merging any existing changes...
git fetch origin
git merge origin/main --allow-unrelated-histories -m "Merge existing repository"

echo.
echo [6/6] Pushing to GitHub...
git push -u origin main

echo.
echo ============================================
echo  DONE! Check https://github.com/SieuHoangKhang/BC_JAVA
echo ============================================
pause
