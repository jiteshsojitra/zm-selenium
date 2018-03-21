#!/bin/bash

set -euxo pipefail

# Environment variables
export DEBIAN_FRONTEND=noninteractive

#startx
Xvfb :0 -screen 0 800x6000x24 +extension GLX -nolisten tcp -dpi 96
export DISPLAY=:0
xdg-open http://www.google.com
exit;

#xdg-open http://127.0.0.1:6901/?password=vncpassword

git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-mailbox.git ~/zm-mailbox
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-ajax.git ~/zm-ajax
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-web-client.git ~/zm-web-client
git clone --depth=1 -b $CIRCLE_BRANCH https://github.com/Zimbra/zm-zimlets.git ~/zm-zimlets