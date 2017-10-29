#!/bin/bash
echo Replacing ui.menu.witspirit.be with our current build...
aws s3 sync --delete ../../menu-ui/dist s3://ui.menu.witspirit.be
echo ui.menu.witspirit.be up to date.
