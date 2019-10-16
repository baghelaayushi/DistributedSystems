#!/usr/bin/env bash

echo "Script executed from: ${PWD}"

BASEDIR=$(dirname $0)
echo "Script location: ${BASEDIR}"



# Run my program.
java -cp ./dependencies/*:. Server $1

exit 0