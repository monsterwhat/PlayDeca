# PlayDeca Quarkus Development Server Launcher
# PowerShell version with enhanced features

Write-Host "=============================================" -ForegroundColor Cyan
Write-Host "   PlayDeca - Quarkus Development Server" -ForegroundColor White
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host ""

# Check if project directory exists
$projectPath = "G:\Documents\GitHub\PlayDeca\PlayDeca"
if (Test-Path $projectPath) {
    Write-Host "[✓] Changed to project directory: $projectPath" -ForegroundColor Green
    
    # Change to project directory
    Set-Location $projectPath
    
    Write-Host ""
    Write-Host "[🚀] Starting Quarkus development server..." -ForegroundColor Yellow
    Write-Host ""
    
    try {
        # Start Quarkus in development mode
        Start-Process -FilePath "mvn" -ArgumentList "quarkus:dev" -NoNewWindow -Wait -ErrorAction Stop
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host ""
            Write-Host "=============================================" -ForegroundColor Green
            Write-Host " ✅ Quarkus server running successfully!" -ForegroundColor Green
            Write-Host " 🌐 Access your application at: http://localhost:8080" -ForegroundColor White
            Write-Host "=============================================" -ForegroundColor Green
            Write-Host ""
            Write-Host "[⏹] Press Ctrl+C to stop the server" -ForegroundColor Gray
            Write-Host ""
            
            # Wait for the process to complete
            Wait-Process -Name "mvn" -ErrorAction Stop
            
            Write-Host ""
            Write-Host "🛑 Quarkus server stopped" -ForegroundColor Yellow
        } else {
            Write-Host ""
            Write-Host "[✗] Failed to start Quarkus server!" -ForegroundColor Red
            Write-Host "    Exit code: $LASTEXITCODE" -ForegroundColor Red
            Write-Host ""
            Write-Host "[🔧] Please check the error messages above" -ForegroundColor Gray
        }
    }
    catch {
        Write-Host ""
        Write-Host "[✗] Error: $($_.Exception.Message)" -ForegroundColor Red
        Write-Host ""
        Write-Host "[🔧] Please ensure you have Java 21+ and Maven installed" -ForegroundColor Gray
    }
} else {
    Write-Host ""
    Write-Host "[✗] ERROR: PlayDeca project directory not found!" -ForegroundColor Red
    Write-Host "    Expected: $projectPath" -ForegroundColor Gray
    Write-Host ""
    Write-Host "[🔧] Please ensure the project exists and try again" -ForegroundColor Gray
    Write-Host ""
    Write-Host "Press any key to exit..." -ForegroundColor Gray
    $null = $Host.UI.RawUI.ReadKey()
}