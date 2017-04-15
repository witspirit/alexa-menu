@echo off
set RELVERSION=%1
set NEWDEVVERSION=%2
echo "Preparing release version: %RELVERSION"
call mvn versions:set -DnewVersion=%RELVERSION
call git commit -m "Setting release version to %RELVERSION"
call git tag %RELVERSION
echo "Preparing next development version: %NEWDEVVERSION"
call mvn versions:set -DnewVerion=%NEWDEVVERSION
call git commit -m "Setting next development version to %NEWDEVVERSION"

REM call git push
REM call git push --tags
