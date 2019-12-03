#!/bin/bash

${MAVEN_OPTS:=""}
${DB_PROPERTIES:=""}

rpmdir=$PWD
sources=SOURCES
finaljar=${sources}/bridge-restore.jar
retval=0

if [ "$1" = "clean" ]; then
    echo "Cleaning"
    rm -rf BUILD/
    rm -rf BUILDROOT/
    rm -rf RPMS/
    rm -rf SRPMS/
    rm -rf SOURCES/
    rm -rf tmp/
    exit 0
fi

if [ ! -d ${sources} ]; then
    mkdir ${sources}
fi

cd ../

# Get the version of the build and trim off the -SNAPSHOT
echo "Getting version from maven..."
full_version=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive exec:exec)
if [ $? -ne 0 ]; then
    echo "Error getting version from maven exec plugin"
    exit -1
fi

version=$(echo ${full_version} | sed 's/-.*//')
release_type=$(echo ${full_version} | sed 's/.*-//')

jarfile=target/bridge-restore-${version}-${release_type}-jar-with-dependencies.jar

if [ ! -e ${jarfile} ]; then
    echo "Building latest jar..."
    mvn -q ${MAVEN_CLI_OPTS} ${DB_PROPERTIES} clean package
    if [ $? -ne 0 ]; then
        echo "Error building ingest-server"
        exit 99
    fi
else
    echo "Jar already built"
fi


# Copy the artifacts
cp ${jarfile} rpm/${finaljar}
cp src/main/sh/bridge-restore.sh rpm/${sources}
cp src/main/resources/bridge-restore.yaml rpm/${sources}

# cd back to where we started and build the rpm
cd ${rpmdir}
rpmbuild -ba --define="_topdir $PWD" --define="_tmppath $PWD/tmp" --define="ver $version" SPECS/bridge-restore.spec
