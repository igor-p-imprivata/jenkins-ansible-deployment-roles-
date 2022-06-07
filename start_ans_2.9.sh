#!/bin/bash

docker run --name=ansible-container-2.9 --rm -it -v /local/ansible:/app:ro ansible:2.9 /bin/bash
