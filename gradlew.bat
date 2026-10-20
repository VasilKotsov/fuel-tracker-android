@echo off
setlocal
set GRADLE_VERSION=8.7
set GRADLE_HOME=%USERPROFILE%\.gradle\gradle-%GRADLE_VERSION%
set ARCHIVE_PATH=%USERPROFILE%\.gradle\gradle-%GRADLE_VERSION%-bin.zip
set DIST_URL=https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip

if not exist "%GRADLE_HOME%\bin\gradle.bat" (
    if not exist "%ARCHIVE_PATH%" (
        echo Downloading Gradle %GRADLE_VERSION%...
        powershell -Command "Invoke-WebRequest -Uri '%DIST_URL%' -OutFile '%ARCHIVE_PATH%'"
    )

    echo Unpacking Gradle %GRADLE_VERSION%...
    powershell -Command "Expand-Archive -Path '%ARCHIVE_PATH%' -DestinationPath '%USERPROFILE%\.gradle' -Force"
)

call "%GRADLE_HOME%\bin\gradle.bat" %*
