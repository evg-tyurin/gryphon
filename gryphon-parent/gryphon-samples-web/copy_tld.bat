echo off 

set PROJECT_HOME=%1

echo target file must be writable

copy /Y %PROJECT_HOME%\gryphon\src\main\resources\gryphon.tld %PROJECT_HOME%\gryphon-samples-web\src\main\webapp\WEB-INF\
