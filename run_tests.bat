@echo off
setlocal enabledelayedexpansion

set TEST_DIR=test
set PROGRAM_NAME=myprogramname

set passed=0
set failed=0
set total=0
set failed_tests=

echo =======================================
echo        JAVA TEST RUNNER ENGINE
echo =======================================
echo.

if not exist "%TEST_DIR%" (
    echo Error: Directory '%TEST_DIR%' not found.
    exit /b 1
)

for /d %%D in ("%TEST_DIR%\*") do (
    set dir_name=%%~nD
    if exist "%%D\commands.txt" (
        set /a total+=1
        set /a passed+=0
        <nul set /p ="Running Test [!dir_name!]... "

        java %PROGRAM_NAME% < "%%D\commands.txt" > nul 2>&1

        if !errorlevel! == 0 (
            echo [ SUCCESS ]
            set /a passed+=1
        ) else (
            echo [ FAILURE ]
            set /a failed+=1
            set failed_tests=!failed_tests! !dir_name!
        )
    )
)

echo.
echo +-------------------------------------+
echo ^|  TEST RESULTS SUMMARY               ^|
echo +-------------------------------------+
echo ^| Total Tests Run: %total%
echo ^| Passed:          %passed%
echo ^| Failed:          %failed%
echo +-------------------------------------+

if not "%failed_tests%"=="" (
    echo.
    echo Failed Cases:
    for %%T in (%failed_tests%) do echo  - %%T
    echo.
)

if %failed% gtr 0 ( exit /b 1 ) else ( exit /b 0 )
