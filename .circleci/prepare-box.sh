#!/bin/bash

set -x
set -e
set -o pipefail

# Setup
mkdir -p ~/.zcs-deps
mkdir -p ~/.ivy2/cache
cd ~/.zcs-deps
wget https://files.zimbra.com/repository/ant-contrib/ant-contrib-1.0b1.jar

# Environment variables
export DEBIAN_FRONTEND=noninteractive
export GIT_BRANCH=develop
export JAVA_HOME=/usr/lib/jvm/java-8-oracle

# Update the repository sources list
apt-get -qq -y autoremove
apt-get -qq -y update

# Install pre-requisites
apt-get -qq install -y git
apt-get -qq install -y wget
apt-get -qq install -y apt-utils
apt-get -qq install -y software-properties-common
apt-get -qq install -y build-essential
apt-get -qq install -y sudo
apt-get -qq install -y iputils-ping
apt-get -qq install -y openssh-server
apt-get -qq install -y vim
apt-get -qq install -y wget
apt-get -qq install -y git
apt-get -qq install -y ant ant-optional ant-contrib
apt-get -qq install -y firefox

# Java
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
RUN add-apt-repository ppa:webupd8team/java
RUN apt-get update ; echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
RUN apt-get install -y oracle-java8-installer oracle-java8-set-default

# Chrome
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
RUN dpkg -i google-chrome-stable_current_amd64.deb; apt-get -fy install