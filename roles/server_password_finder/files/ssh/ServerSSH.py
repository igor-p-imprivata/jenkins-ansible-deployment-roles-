import ssh
import logging
import sys
import os

passwordsToSearch = []
count = 0
fileString = ""

with open(os.getcwd() + "\\" + r"passwords.txt", 'r') as file:
    for line in file:
        passwordsToSearch.append(line.strip(' \n\r\t'))

logging.basicConfig(level=logging.WARNING)
connection = ssh.SSHConnection(host=sys.argv[1], port=sys.argv[5])
connection.connect(username=sys.argv[2], password=sys.argv[3])
concatString = "'"
for i in range(0, len(passwordsToSearch)):
    if i is not len(passwordsToSearch)-1:
        concatString += passwordsToSearch[i] + "|"
    else:
        concatString += passwordsToSearch[i] + "'"

try:
    fileString = sys.argv[4] + "results.txt"
except IndexError:
    fileString = os.getcwd() + "\\" + '..' + "\\" + r"results.txt"

systemLogs = connection.run("cd /home/root/home/imprivata/log", "ls system.log*")
systemLogsList = systemLogs.splitlines()

with open(fileString, 'w') as file:
    for log in systemLogsList:
        # run a word search on all the files, outputting the result to a text file separated by the log in which they
        # are found
        commandString = " grep -iE " + concatString + " " + log
        file.write("This is log file: " + log + " " + connection.run("cd /home/root/home/imprivata/log", commandString))

onLog = "0"
with open(fileString, 'r') as file:
    for line in file:
        if "This is log file" in line:
            # This try/except is only needed for the case where there is only one file, system.log,
            # and not system.log.1, system.log.2, etc.  onLog defaults to 0, so it doesnt need to set to 0 explicitly
            try:
                onLog = line.partition("system.log.")[2][0]
            except IndexError:
                pass
            print("On log: system.log" + onLog)
        for password in passwordsToSearch:
            if password in line:
                count += 1
                print("Password found: " + password + " is in this line:\n" + line)
print("Number of password instances found = " + str(count))

