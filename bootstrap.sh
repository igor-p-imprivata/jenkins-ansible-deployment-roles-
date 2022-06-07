#!/usr/bin/env bash

apt-get update
sudo apt-get upgrade -y
sudo ln -s /usr/bin/python3 /usr/bin/python
sudo apt-get install python3-pip -y
sudo ln -s /usr/bin/pip3 /usr/bin/pip
sudo pip install ansible==2.7.11
sudo pip install pywinrm
sudo apt-get install libkrb5-dev libssl-dev git-lfs -y
sudo pip install pywinrm[kerberos]
sudo pip install pywinrm[credssp]
sudo pip install asciinema
sudo pip install termtosvg

###sudo apt-get install build-essential checkinstall
###sudo apt-get install libreadline-gplv2-dev  libncursesw5-dev libssl-dev libsqlite3-dev tk-dev libgdbm-dev libc6-dev libbz2-dev zlib1g-dev
###cd /usr/src && sudo wget https://www.python.org/ftp/python/3.6.6/Python-3.6.6.tgz
###sudo tar xzf Python-3.6.6.tgz
###sudo apt-get install zlib1g-dev
###cd Python-3.6.6 && sudo ./configure --enable-optimizations && sudo make altinstall
#apt-get upgrade -y
##sudo apt-add-repository --yes ppa:ansible/ansible
##apt-get install software-properties-common gcc python-dev libkrb5-dev libssl-dev libffi-dev build-essential -y
#sudo apt-add-repository --yes ppa:ansible/ansible
##apt-get update
##sudo apt-get install ansible python-pip -y
##sudo pip install --upgrade setuptools
##sudo pip install pywinrm
##sudo pip install pywinrm[kerberos]
##sudo pip install pywinrm[credssp]
###sudo ln -s /usr/local/bin/python3.6 /usr/bin/python3.6
###sudo rm /usr/bin/python3
###sudo ln -s /usr/bin/python3.6 /usr/bin/python3
###sudo rm /usr/bin/python
###sudo ln -s /usr/bin/python3 /usr/bin/python
###sudo ln -s /usr/local/bin/pip3.6 /usr/bin/pip3
###sudo ln -s /usr/bin/pip3 /usr/bin/pip
