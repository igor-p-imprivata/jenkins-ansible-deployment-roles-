#!/bin/bash
docker run --name=ansible-container --rm -it -v /local/ansible:/app:ro ansible:00 /bin/bash
