@echo off
echo 欢饮使用CPSMini，请不要关闭此窗口。
@echo 当前目录是：%cd% >nul 
@echo Welcome to use CPSMini >nul 
@copy %cd%\JoymindComm.dll C:\ 2>nul >nul 
@mkdir C:\CPSMini 2>nul >nul 
@copy %cd%\date.txt C:\CPSMini\ 2>nul >nul 
@del %cd%\date.txt 2>nul >nul 
@del %cd%\..\CPSMini.rar 2>nul >nul 
@java -jar %cd%\Z.jar 
