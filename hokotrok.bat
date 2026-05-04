@echo off
:: Make sure we have the latest compiled code in the build directory
if not exist "build" mkdir build
javac src/main/java/*.java -d build

java -cp build Main %*
