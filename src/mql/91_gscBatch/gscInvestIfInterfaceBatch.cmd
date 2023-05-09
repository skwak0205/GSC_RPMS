for /f "tokens=1-4 delims=/ " %%i IN ('date /t') do (
  set year=%%i
  set month=%%j
  set day=%%k
)

D:\R2023x\3DSpace\win_b64\code\bin\mql.exe -c "set context user admin_platform pass Qwer1234;verbose on;exec prog gscFinancialInterface -method executeInvestInterface;" >> D:\enovia\logs\batch\gscInvestIflog_%year%.log