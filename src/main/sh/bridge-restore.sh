#!/bin/sh

RESTORE_CONFIG=/usr/local/chronopolis/restore/restore.yaml
java -jar -D${RESTORE_CONFIG} /usr/local/chronopolis/restore/restore.jar
