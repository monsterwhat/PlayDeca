@echo off
title PlayDeca - Quarkus Development Server
color 0A

echo ================================================
echo    PlayDeca - Quarkus Development Server
echo ================================================
echo.

REM Check if project directory exists
if exist "G:\Documents\GitHub\PlayDeca\PlayDeca" (
    echo [32m✓ Changed to project directory.[0m
    
    REM Change to project directory
    cd /d "G:\Documents\GitHub\PlayDeca\PlayDeca"
) else (
    echo [31m✗ ERROR: PlayDeca project directory not found![0m
    echo [33mPlease ensure project exists at: G:\Documents\GitHub\PlayDeca\PlayDeca[0m
    pause
    exit /b 1
)

echo.
echo [36m🚀 Starting Quarkus development server...[0m
echo.

REM Compile and start Quarkus
mvn quarkus:dev

if %ERRORLEVEL% NEQ 0 (
    echo [31m✗ Failed to start Quarkus server![0m
    echo [33mPlease check the error messages above.[0m
    pause
    exit /b 1
)

echo.
echo [32m============================================[0m
echo [32m ✅ Quarkus server running at:[0m
echo [36mhttp://localhost:8080[0m
echo [32m============================================[0m
echo.
echo [33mPress Ctrl+C to stop the server[0m
echo.

REM Keep the window open
:loop
    timeout /t 1 >nul
    goto loop