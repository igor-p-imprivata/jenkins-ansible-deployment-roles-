FROM ubuntu:18.04
LABEL maintainer="Igor P"

ENV pip_packages "ansible==2.10 pywinrm pywinrm[kerberos] pywinrm[credssp] asciinema termtosvg flake8 testinfra molecule yamllint ansible-lint"

# Install dependencies
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
       apt-utils \
       libkrb5-dev \
       libssl-dev \
       build-essential \
       python3-setuptools \
       python3-dev \
       python3-pip \
       software-properties-common \
       rsyslog systemd systemd-cron sudo \
    && ln -s /usr/bin/python3 /usr/bin/python \
    && ln -s /usr/bin/pip3 /usr/bin/pip \
    && rm -Rf /var/lib/apt/lists/* \
    && rm -Rf /usr/share/doc && rm -Rf /usr/share/man \
    && apt-get clean
RUN sed -i 's/^\($ModLoad imklog\)/#\1/' /etc/rsyslog.conf

# Install Ansible via Pip.
#RUN python -m pip install pip==20.3.1 && pip install $pip_packages
RUN python -m pip install pip==21.2.1 && pip install $pip_packages

#COPY initctl_faker .
#RUN chmod +x initctl_faker && rm -fr /sbin/initctl && ln -s /initctl_faker /sbin/initctl

# Install Ansible inventory file.
RUN mkdir -p /etc/ansible
RUN echo "[local]\nlocalhost ansible_connection=local" > /etc/ansible/hosts

#VOLUME ["/sys/fs/cgroup", "/tmp", "/run"]
#CMD ["/lib/systemd/systemd"]
