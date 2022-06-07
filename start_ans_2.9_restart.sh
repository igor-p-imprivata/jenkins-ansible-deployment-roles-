#!/bin/bash

docker run --name=ansible-container-2.9 --restart unless-stopped -it -v /local/ansible:/app:ro ansible:2.9 /bin/bash
