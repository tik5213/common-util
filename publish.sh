#!/bin/bash
set -e  #"Exit immediately if a simple command exits with a non-zero status."
#set +e # don’t bail out of bash script if cache doesn’t exist
old_dir=`pwd`
cd `dirname "$0"`








 ./gradlew clean build bintrayUpload -PbintrayUser="${bintrayUser}" -PbintrayKey="${bintrayKey}" -PdryRun=false














cd "${old_dir}"
