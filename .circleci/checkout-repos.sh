#!/bin/bash

set -euxo pipefail

# Environment variables
export DEBIAN_FRONTEND=noninteractive

apt-get update
apt-get install xvfb
Xvfb :1 -screen 0 1024x768x16 &> xvfb.log  &
ps aux | grep X

#startx
xdg-open http://127.0.0.1:6901/?password=vncpassword
exit;

#xdg-open http://127.0.0.1:6901/?password=vncpassword

git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-mailbox.git ~/zm-mailbox
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-ajax.git ~/zm-ajax
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-web-client.git ~/zm-web-client
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-zimlets.git ~/zm-zimlets