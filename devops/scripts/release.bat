RELVERSION=%1
NEWDEVVERSION=%2
call mvn versions:set -DnewVersion=%RELVERSION
call git commit -m "Setting release version to %RELVERSION"
call git tag %RELVERSION
call mvn versions:set -DnewVerion=%NEWDEVVERSION
call git commit -m "Setting next development version to %NEWDEVVERSION"

REM call git push
REM call git push --tags
