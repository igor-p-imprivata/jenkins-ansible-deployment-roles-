
@setlocal

set SOURCE_FILE=log

for /F "tokens=2-4 delims=/ " %%i in ('date /t') do set DATE_NUMBER=%%k%%i%%j
for /F "tokens=5-8 delims=:. " %%i in ('echo.^| time ^| find "current" ') do set TIME_NUMBER=%%i%%j%%k%%l

set NEW_FILE_NAME=%SOURCE_FILE%-%DATE_NUMBER%%TIME_NUMBER%.log

rename %SOURCE_FILE% %NEW_FILE_NAME%

@endlocal