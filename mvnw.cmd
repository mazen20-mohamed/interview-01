@ECHO OFF
SETLOCAL

SET "BASE_DIR=%~dp0"
SET "WRAPPER_DIR=%BASE_DIR%.mvn\wrapper"
SET "PROPERTIES_FILE=%WRAPPER_DIR%\maven-wrapper.properties"

IF NOT EXIST "%PROPERTIES_FILE%" (
  ECHO Missing %PROPERTIES_FILE%
  EXIT /B 1
)

FOR /F "tokens=1,* delims==" %%A IN (%PROPERTIES_FILE%) DO (
  IF "%%A"=="distributionUrl" SET "DISTRIBUTION_URL=%%B"
)

IF "%DISTRIBUTION_URL%"=="" (
  ECHO distributionUrl is not configured in %PROPERTIES_FILE%
  EXIT /B 1
)

FOR %%F IN ("%DISTRIBUTION_URL%") DO SET "ARCHIVE_NAME=%%~nxF"
SET "MAVEN_DIR_NAME=%ARCHIVE_NAME:-bin.zip=%"
SET "MAVEN_HOME=%WRAPPER_DIR%\%MAVEN_DIR_NAME%"
SET "MAVEN_BIN=%MAVEN_HOME%\bin\mvn.cmd"

IF NOT EXIST "%MAVEN_BIN%" (
  ECHO Downloading Maven from %DISTRIBUTION_URL%
  powershell -NoProfile -ExecutionPolicy Bypass -Command ^
    "$ProgressPreference = 'SilentlyContinue';" ^
    "$archive = Join-Path '%WRAPPER_DIR%' '%ARCHIVE_NAME%';" ^
    "Invoke-WebRequest -Uri '%DISTRIBUTION_URL%' -OutFile $archive;" ^
    "Expand-Archive -Path $archive -DestinationPath '%WRAPPER_DIR%' -Force;" ^
    "Remove-Item $archive -Force"
  IF ERRORLEVEL 1 EXIT /B 1
)

CALL "%MAVEN_BIN%" %*
EXIT /B %ERRORLEVEL%
