#!/usr/bin/env bash

# Run my program.
java -cp gson-2.8.6.jar: Server $1

java -cp "/bin/commons-lang3-3.5.jar:/bin/gson-2.8.6.jar:" qrcode

exit 0
