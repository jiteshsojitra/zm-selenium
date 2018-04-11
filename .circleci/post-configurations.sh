#!/bin/bash

set -euxo pipefail

# Create test output links
ln -s ~/zm-selenium/test-output/*/*/*/*/TestNG/index.html ~/zm-selenium/test-output/report.html
ln -s ~/zm-selenium/test-output/*/*/*/*/debug/projects/harness.log ~/zm-selenium/test-output

# Print process output to check java memory use
top -n 1 -b > ~/zm-selenium/process-output.txt