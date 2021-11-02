#!/bin/sh

# Fail if any pipeline fails
set -e
set -o pipefail

api=https://papermc.io/api/v2/projects/paper

# load latest mc version
if [ -z "${MC_VERSION}" ] || [ "${MC_VERSION}" = "latest" ]
  then
  MC_VERSION=$(wget -qO - $api | jq -r '.versions[-1]')
fi

# Find paper build for mc version
if [ -z "${PAPER_BUILD}" ] || [ "${PAPER_BUILD}" = "latest" ]
  then
  PAPER_BUILD=$(wget -qO - "${api}/versions/${MC_VERSION}" | jq '.builds[-1]')
fi

# Download paper clip
echo "Downloading ${api}/versions/${MC_VERSION}/builds/${PAPER_BUILD}/downloads/paper-${MC_VERSION}-${PAPER_BUILD}.jar"
wget "${api}/versions/${MC_VERSION}/builds/${PAPER_BUILD}/downloads/paper-${MC_VERSION}-${PAPER_BUILD}.jar" -O PaperClip.jar

# Run paper clip to build paper.jar
java -jar PaperClip.jar
rm -rf logs eula.txt server.properties
