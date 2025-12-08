@echo off
setlocal

if not "%JAVA_HOME%"=="" goto JavaHomeOk
for %%i in (javaw.exe) do set "JAVACMD=%%~$PATH:i"
goto checkJavaCmd

:JavaHomeOk
set "JAVACMD=%JAVA_HOME%\bin\javaw.exe"

:checkJavaCmd
if exist "%JAVACMD%" goto setDir
@echo "An executable Java could not be found" >&2
goto end

:setDir
set "PRGDIR=%~dp0.."
cd %PRGDIR%

set MAIN_CLASS=de.meetingapps.services.meetingclient.meetingClient.MainClientMeeting

::set JAVA_PARAMS=

set VM_PARAMS= --module-path "javafx-sdk-17.0.12\lib"
set VM_PARAMS= %VM_PARAMS% --add-modules=javafx.controls,javafx.fxml
::set VM_PARAMS= %VM_PARAMS% -Dlog4j.configurationFile=config/log4j2.xml

set LIBS=lib
start "meetingClient" "%JAVACMD%" -cp %LIBS%/* %VM_PARAMS%  %MAIN_CLASS% %JAVA_PARAMS%"
:end
