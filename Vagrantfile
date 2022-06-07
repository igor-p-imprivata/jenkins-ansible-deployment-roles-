require 'fileutils'

workspace = "/app"

Vagrant.configure("2") do |config|
  config.vm.box = "generic/ubuntu1804"
  config.vm.provision :shell, path: "bootstrap.sh"
  config.vm.hostname = "ansible-master"

  config.vm.synced_folder ".", workspace

  config.vm.post_up_message = "The ansible sandbox is up. Run 'vagrant ssh' to get started. \n Switch to /app "


  # allow specifying port forwards via an environment variable, since this is a multi-use sandbox machine
  # the VAGRANT_PORTS variable should contain a list of port forward specs separated on whitespace.
  # a spec can either be a port number or a host_port:guest_port pair.
  #
  # for example, this invocation: VAGRANT_PORTS="8000:80 8888" vagrant up
  # will create these forwards:
  #
  # default: 80 (guest) => 8000 (host) (adapter 1)
  # default: 8888 (guest) => 8888 (host) (adapter 1)
  # default: 22 (guest) => 2222 (host) (adapter 1)

  "#{ENV['VAGRANT_PORTS']}".split.each do |conf|
    host_port, guest_port = conf.split ':'
    guest_port = host_port if guest_port.nil?
    config.vm.network "forwarded_port", host: host_port, guest: guest_port
  end

end


# -*- mode: ruby -*-
# vi: set ft=ruby :
