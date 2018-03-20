#!/bin/bash

set -x
set -e
set -o pipefail

export DEBIAN_FRONTEND=noninteractive

git clone --depth=1 -b develop https://github.com/Zimbra/zm-mailbox.git ~/zm-mailbox
git clone --depth=1 -b develop https://github.com/Zimbra/zm-ajax.git ~/zm-ajax
git clone --depth=1 -b develop https://github.com/Zimbra/zm-web-client.git ~/zm-web-client
git clone --depth=1 -b develop https://github.com/Zimbra/zm-zimlets.git ~/zm-zimlets