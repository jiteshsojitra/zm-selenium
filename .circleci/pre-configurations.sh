#!/bin/bash

set -euxo pipefail
set +o nounset

echo $SELENIUM_SERVER_HOST
echo $SELENIUM_BROWSER

# Configuration
seleniumConfigFile="$HOME/zm-selenium/conf/config.properties"

# Update server
echo -e "Updating server host in config.properties file"
configServer=`cat $seleniumConfigFile | grep server.host`
configServer=`echo $configServer | cut -d \= -f 2`
sed -i "0,/$configServer/s/$configServer/$SELENIUM_SERVER_HOST/" $seleniumConfigFile

# Update browser
echo -e "Updating browser in config.properties file"
configBrowser=`cat $seleniumConfigFile | grep browser`
configBrowser=`echo $configBrowser | cut -d \= -f 2`
sed -i "0,/$configBrowser/s/$configBrowser/$SELENIUM_BROWSER/" $seleniumConfigFile

# Copy private key to the container
echo -e "Copy private key to the container"
cat ~/.ssh/id_rsa_* > ~/.ssh/id_rsa