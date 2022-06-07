#!/bin/bash

docker run --name=ansible-container-2.8 --rm -it -v /local/ansible:/app:ro ansible:2.8 /bin/bash
