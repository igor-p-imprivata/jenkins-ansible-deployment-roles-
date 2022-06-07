PIESSH.py Usage:

python.exe PIESSH.py IP port username password path\to\output\files

prerequisites:
- python 3.x must be installed on your windows computer.
- paramiko library must be installed.  Run "pip install paramiko"

IP: ip address or hostname of PIE machine
port: port for ssh connection
username: username of account to log into via ssh
password: password for account
outputdir: optional, directory to save results.txt and encrypted.txt file to.  If not specified, defaults to current working directory.

To enable more convienent calling of the python script, Wrapper.bat can be used, which calls the appropiate script without having to type in the cmd line every time.  You can also create multiple copies of this script, with different output directories, if you want to scan multiple PIE machines.

Warning: putting common passwords that generate alot of matched lines, like "password", will cause the ssh session to essentially hang, as it is trying to send too much text in one request.  Keep the passwords.txt file to actual passwords that may be present, instead of any dummy passwords.


Documentation on other pen testing tools:
https://intranet.imprivata.com/display/OSQA/How+To+Use+Pen+Testing+Tools+for+ISXTrace%2C+PIE%2C+Windows+memory%2C+and+OneSign+Appliances