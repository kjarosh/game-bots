@echo off

set JAVA=java
for /r %%i in (mancala-bot-*.jar) do %JAVA% -jar %%i %*
