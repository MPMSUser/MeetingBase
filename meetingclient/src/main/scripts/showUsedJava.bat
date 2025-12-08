@echo off
setlocal

if not "%JAVA_HOME%"=="" goto JavaHomeOk
for %%i in (java.exe) do set "JAVACMD=%%~$PATH:i"
goto checkJavaCmd

:JavaHomeOk
set "JAVACMD=%JAVA_HOME%\bin\java.exe"

:checkJavaCmd
if exist "%JAVACMD%" goto showJava
@echo "An executable Java could not be found" >&2
goto end

:showJava
@echo Used Java is: "%JAVACMD%" 

:end
pause