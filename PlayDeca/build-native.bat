@echo off
set GRAALVM_HOME=G:\Downloads\graalvm-jdk-25_windows-x64_bin\graalvm-jdk-25.0.1+8.1
set JAVA_HOME=G:\Downloads\graalvm-jdk-25_windows-x64_bin\graalvm-jdk-25.0.1+8.1
set PATH=G:\Downloads\graalvm-jdk-25_windows-x64_bin\graalvm-jdk-25.0.1+8.1\bin;C:\ProgramData\chocolatey\lib\maven\apache-maven-3.9.11\bin;%PATH%

echo ====================================
echo Building PlayDeca Native Executable
echo ====================================
echo.

echo Environment set:
echo GRAALVM_HOME=%GRAALVM_HOME%
echo JAVA_HOME=%JAVA_HOME%
echo.

echo Cleaning and building native image...
call mvn -Pnative clean package -DskipTests

echo.
echo ====================================
echo Build Complete!
echo ====================================
echo.

set EXE_NAME=PlayDeca-runner.exe
if exist "target\%EXE_NAME%" (
    echo ✅ Native executable created successfully!
    echo 📦 Location: target\%EXE_NAME%
    echo 📏 Size: 
    dir "target\%EXE_NAME%" | find "%EXE_NAME%"
    echo.
    echo 🚀 To run: target\%EXE_NAME%
    echo 🌐 Application will be available at: http://localhost:8080/
) else (
    echo ❌ Build failed! Check the error messages above.
)

echo.
pause
