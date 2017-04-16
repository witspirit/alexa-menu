@echo off
set version=%1
aws lambda update-function-code --function-name=alexa-menu-skill --zip-file=fileb://..\..\alexa-menu-skill\target\alexa-menu-skill-%version%-lambda.jar
