ServerSSH.py Usage:

python.exe ServerSSH.py IP username password [outputdir]

IP: ip address or hostname of server
username: username of account to log into via ssh
password: password for account
outputdir: optional, directory to save results.txt file to.  If not specified, defaults to current working directory.

To enable more convienent calling of the python script, Wrapper.bat can be used, which calls the appropiate script without having to type in the cmd line every time.

Warning: putting common passwords that generate alot of matched lines, like "password", will cause the ssh session to essentially hang, as it is trying to send too much text data in one request.  Keep the passwords.txt file to actual passwords that may be present, instead of any dummy passwords.


Documentation on other pen testing tools:
https://intranet.imprivata.com/display/OSQA/How+To+Use+Pen+Testing+Tools+for+ISXTrace%2C+PIE%2C+Windows+memory%2C+and+OneSign+Appliances