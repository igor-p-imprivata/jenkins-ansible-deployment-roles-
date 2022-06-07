#!/bin/bash
docker run --name=ansible-container --rm -it -v /local/ansible:/app:rw ansible:00 /bin/bash
