#!/bin/bash

set -euxo pipefail

# Environment variables
export DEBIAN_FRONTEND=noninteractive
export GIT_BRANCH=develop

git clone --depth=1 -b $GIT_BRANCH https://github.com/Zimbra/zm-mailbox.git ~/zm-mailbox
git clone --depth=1 -b $GIT_BRANCH https://github.com/Zimbra/zm-ajax.git ~/zm-ajax
git clone --depth=1 -b $GIT_BRANCH https://github.com/Zimbra/zm-web-client.git ~/zm-web-client
git clone --depth=1 -b $GIT_BRANCH https://github.com/Zimbra/zm-zimlets.git ~/zm-zimlets