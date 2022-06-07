from collections import defaultdict
from math import sqrt

import ssh
import logging
import sys
import os

passwordsToSearch = []
count = 0
fileString = ""


# uses natural letter occurrence probabilities to determine with a certain degree of accuracy
# whether or not the password is encrypted.
def encrypted(text):
    scores = defaultdict(lambda: 0)
    for letter in text:
        scores[letter] += 1
    largest = max(scores.values())
    average = len(text) / 256.0
    return largest > average + 5 * sqrt(average)


def collect_logs(output_file_name, path_to_files, file_command, command_string):
    try:
        filename = sys.argv[5] + output_file_name
    except IndexError:
        filename = os.getcwd() + "\\" + output_file_name

    systemlogslist = connection.run("cd " + path_to_files, file_command).splitlines()
    print(systemlogslist)

    with open(filename, 'w') as file:
        for log in systemlogslist:
            # run a word search on all the files, outputting the result to a text file
            # separated by the log in which they are found
            fullcommandstring = command_string + "\"" + log + "\""
            file.write("This is log file: " + log + " " + connection.run("cd " + path_to_files, fullcommandstring))
    return filename


with open(os.getcwd() + "\\" + r"passwords.txt", 'r') as file:
    for line in file:
        passwordsToSearch.append(line.strip(' \n\r\t'))

logging.basicConfig(level=logging.WARNING)
connection = ssh.SSHConnection(host=sys.argv[1], port=sys.argv[2])
try:
    connection.connect(username=sys.argv[3], password=sys.argv[4])
except TimeoutError:
    print("SSH connection unsuccessful, connection timed out.")
    sys.exit(1)
concatString = "'"
for i in range(0, len(passwordsToSearch)):
    if i is not len(passwordsToSearch) - 1:
        concatString += passwordsToSearch[i] + "|"
    else:
        concatString += passwordsToSearch[i] + "'"

fileString = collect_logs(r"results.txt",
                          "/usr/lib/imprivata/runtime/log/",
                          "ls *.log*",
                          " grep -iE " + concatString + " ")
with open(fileString, 'r') as file:
    for line in file:
        for password in passwordsToSearch:
            if password in line:
                count += 1
                print("Password found: " + password + " is in this line:\n" + line)

fileString = collect_logs(r"encrypted.txt",
                          "/usr/lib/imprivata/runtime/offline/Agent/Users/",
                          "find -type f",
                          "cat ")
with open(fileString, 'r') as file:
    for line in file:
        for password in passwordsToSearch:
            if not encrypted(line) and password in line:
                count += 1
                print("Password is not encrypted: " + line)

# fus section
try:
    filename = sys.argv[5] + r"fus.txt"
except IndexError:
    filename = os.getcwd() + "\\" + r"fus.txt"
with open(filename, 'w') as file:
    try:
        filecontents = connection.run("cd /usr/lib/imprivata/runtime/etc", "cat fus_config")
        file.write(filecontents)
    except:
        print("fus_config is not readable.")
        pass
with open(filename, 'r') as file:
    for line in file:
        for password in passwordsToSearch:
            if not encrypted(line) and password in line:
                count += 1
                print("Password is not encrypted: " + line)

print("Number of password instances found = " + str(count))
