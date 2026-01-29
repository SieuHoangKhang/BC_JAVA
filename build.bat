@echo off
REM Simple build script for QuanLyNoiThat
REM Run this from NetBeans: Clean & Build (Shift+F11)

setlocal enabledelayedexpansion
set JAVA_HOME=C:\Program Files\Java\jdk-25.0.2

if not exist "build\classes" mkdir "build\classes"

echo Compiling Java files...
for /r "src" %%f in (*.java) do (
    "%JAVA_HOME%\bin\javac.exe" -encoding UTF-8 -cp "build\classes;lib\*" -d "build\classes" "%%f" 2>nul
)

echo Build complete!
pause
