#!/bin/bash
echo Updating Alexa Menu Skill Lambda Code to version $1
aws lambda update-function-code --function-name=alexa-menu-skill --zip-file=fileb://alexa-menu-skill/target/alexa-menu-skill-$1-lambda.jar
echo Updating Alexa Menu Skill Lambda Configuration to version $1
aws lambda update-function-configuration --function-name=alexa-menu-skill --role arn:aws:iam::138759191763:role/service-role/AlexaMenuSkillRole --handler be.witspirit.alexamenu.AlexaMenuHandler --description "Travis release build $1" --runtime java8
echo Alexa Menu Skill Lambda Updated to version $1