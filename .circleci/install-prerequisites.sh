#!/bin/bash

set -euxo pipefail

# Setup
mkdir -p ~/.zcs-deps
mkdir -p ~/.ivy2/cache

# Update the repository sources list
apt-get -qq -y autoremove
apt-get -qq -y update

# Install pre-requisites
apt-get -qq install -y apt-utils \
	software-properties-common \
	build-essential \
	vim \
	ant ant-optional ant-contrib \
	openssh-server \
	wget \
	iputils-ping \
	git \
	firefox \
	xdg-utils \
	libappindicator1 \
	fonts-liberation

# Java
echo "===> add webupd8 repository..."  && \
echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee /etc/apt/sources.list.d/webupd8team-java.list  && \
echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list  && \
apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886  && \
apt-get update  && \
echo "===> install Java"  && \
echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections  && \
echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections  && \
DEBIAN_FRONTEND=noninteractive  apt-get install -y --force-yes oracle-java8-installer oracle-java8-set-default

# Chrome
wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
dpkg -i google-chrome-stable_current_amd64.deb; apt-get -fy install

# Ant contrib
cd ~/.zcs-deps
wget https://files.zimbra.com/repository/ant-contrib/ant-contrib-1.0b1.jar