#!/usr/bin/env bash

echo "Script executed from: ${PWD}"

cd bin

for entry in "$search_dir"/*
do
  echo "$entry"
done


# Run my program.
java -cp ./dependencies/*:. Server $1

exit 0