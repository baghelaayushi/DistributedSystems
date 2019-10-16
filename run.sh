#!/usr/bin/env bash

echo "Script executed from: ${PWD}"

for entry in "$search_dir"/*
do
  echo "$entry"
done


# Run my program.
java -cp ./dependencies/*:./src/main/java Server $1

exit 0