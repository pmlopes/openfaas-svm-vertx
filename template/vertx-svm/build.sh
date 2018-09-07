#!/bin/sh
set -e
export PATH=~/.local/graalvm-ce-1.0.0-rc6/bin:$PATH
export JAVA_HOME=~/.local/graalvm-ce-1.0.0-rc6

./mvnw -Pnative-image -f entrypoint/pom.xml
./mvnw -Pnative-image -f function/pom.xml
./target/myapp-0.0.1-SNAPSHOT-fat
