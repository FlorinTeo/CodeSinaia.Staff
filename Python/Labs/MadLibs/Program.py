import os
import sys

if (len(sys.argv) < 2):
    raise Exception("Missing file path argument.")

filePath = sys.argv[1]

if not os.path.isfile(filePath):
    raise Exception(f"File {filePath} cannot be located!")

text = open(filePath)
for line in text:
    words = line.split()
    print(words)
