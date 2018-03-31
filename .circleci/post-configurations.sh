#!/bin/bash

set -euxo pipefail

# Post configuration
ln -s ~/zm-selenium/test-output/*/*/*/*/TestNG ~/zm-selenium/test-output
ln -s ~/zm-selenium/test-output/*/*/*/*/debug/projects/harness.log ~/zm-selenium/test-output