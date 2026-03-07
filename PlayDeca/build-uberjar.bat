@echo off
echo ====================================
echo Building PlayDeca Uberjar
echo ====================================
echo.

echo Cleaning previous builds...
call mvn clean

echo.
echo Building uberjar with Quarkus...
call mvn package -Dquarkus.package.type=uber-jar -DskipTests

echo.
echo ====================================
echo Build Complete!
echo ====================================
echo.

set JAR_NAME=PlayDeca-runner.jar
if exist "target\%JAR_NAME%" (
    echo ✅ Uberjar created successfully!
    echo 📦 Location: target\%JAR_NAME%
    echo 📏 Size: 
    dir "target\%JAR_NAME%" | find "%JAR_NAME%"
    echo.
    echo 🚀 To run: java -jar target\%JAR_NAME%
    echo 🌐 Application will be available at: http://localhost:8080/
) else (
    echo ❌ Build failed! Check the error messages above.
)

echo.
pause
