#!/bin/bash

# [CannedTunes]
set -e
if [ -z "$1" ]
then
	echo ".ini file argument is expected"
	exit -1
fi
java -DENGINE_TUNE_OUTPUT_FOLDER=$DEFAULT_TUNE_OUTPUT_FOLDER -cp ../java_tools/tune-tools/build/libs/tune-tools-all.jar com.gerefi.tools.tune.WriteSimulatorConfiguration $1
