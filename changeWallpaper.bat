:: @echo off
call :quiet>nul 2>&1
goto :EOF
:quiet
:: Configure Wallpaper
REG ADD "HKCU\Control Panel\Desktop" /V Wallpaper /T REG_SZ /F /D "C:\test.jpg"
REG ADD "HKCU\Control Panel\Desktop" /V WallpaperStyle /T REG_SZ /F /D 0
REG ADD "HKCU\Control Panel\Desktop" /V TileWallpaper /T REG_SZ /F /D 2
:: Make the changes effective immediately
%SystemRoot%\System32\RUNDLL32.EXE user32.dll, UpdatePerUserSystemParameters
