#!/bin/bash

set -euxo pipefail

# Checkout dependent repositories
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-mailbox.git ~/zm-mailbox
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-ajax.git ~/zm-ajax
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-web-client.git ~/zm-web-client
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-zimlets.git ~/zm-zimlets
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-network-selenium.git ~/zm-network-selenium

# Get circleci container public ip address
public_ip_address=$(wget -qO- http://checkip.amazonaws.com)
echo $public_ip_address

echo $SELENIUM_SERVER_HOST
echo $SELENIUM_BROWSER