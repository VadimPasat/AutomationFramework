Write-Host "====================================================="
Write-Host "Checking Allure History"
Write-Host "====================================================="

if (-Not (Test-Path "allure-history/history")) {

    Write-Host "First execution detected..."
    Write-Host "Running CLEAN execution..."

    mvn clean test -Pall-tests

}
else {

    Write-Host "Existing history detected..."
    Write-Host "Running NORMAL execution..."

    mvn test -Pall-tests
}

Write-Host "====================================================="
Write-Host "Generating Allure Report"
Write-Host "====================================================="

allure generate target/allure-results -o target/allure-report --clean
