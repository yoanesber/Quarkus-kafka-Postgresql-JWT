@echo off
setlocal ENABLEDELAYEDEXPANSION

echo Waiting for Kafka to be ready...

REM Try to connect to Kafka server
set /A retries=30

:wait_loop
docker exec kafka-server kafka-topics.sh --bootstrap-server localhost:9092 --list >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Kafka is ready!
    goto :eof
) else (
    echo Waiting...
    timeout /t 1 >nul
    set /A retries=!retries!-1
    if !retries! LEQ 0 (
        echo Timeout waiting for Kafka.
        exit /b 1
    )
    goto wait_loop
)
