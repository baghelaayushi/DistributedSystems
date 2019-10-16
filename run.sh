#!/usr/bin/env bash

# Run my program.
java -cp .:commons-lang3-3.5.jar:gson-2.8.6  -cp . Server $1

exit 0
