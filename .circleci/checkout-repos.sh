#!/bin/bash

set -euxo pipefail

# Additionally get circleci container public ip address
public_ip_address=$(wget -qO- http://checkip.amazonaws.com)
echo $public_ip_address

# Checkout dependent repositories
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-mailbox.git
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-ajax.git
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-web-client.git
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-zimlets.git
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-network-selenium.git