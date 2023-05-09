for /f "tokens=1-4 delims=/ " %%i IN ('date /t') do (
  set year=%%i
  set month=%%j
  set day=%%k
)

D:\GS_OneDrive\V6R2023x\server\win_b64\code\bin\mql.exe -b matrix-r.gsdev -c "set context user admin_platform pass Qwer1234;verbose on;exec prog gscEAIBatch -method callEAI invest;" >> D:\enovia\logs\batch\gscInvestEAIBatchlog_%year%.log