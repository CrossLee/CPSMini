@echo off
echo ����ʹ��CPSMini���벻Ҫ�رմ˴��ڡ�
@echo ��ǰĿ¼�ǣ�%cd% >nul 
@echo Welcome to use CPSMini >nul 
@copy %cd%\JoymindComm.dll C:\ 2>nul >nul 
@mkdir C:\CPSMini 2>nul >nul 
@copy %cd%\date.txt C:\CPSMini\ 2>nul >nul 
@del %cd%\date.txt 2>nul >nul 
@del %cd%\..\CPSMini.rar 2>nul >nul 
@java -jar %cd%\Z.jar 
