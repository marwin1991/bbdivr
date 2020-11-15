import os

input="used_images.txt"

with open(input) as file:
    for line in file.readlines():
        line = line.replace("\n","").replace("\r","")
        print(line)
        os.system("docker pull -q " + line)
        os.system("docker inspect -f \"{{ .Created }}\" " + line)

