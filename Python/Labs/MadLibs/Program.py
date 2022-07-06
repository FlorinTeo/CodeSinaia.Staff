import os
import sys

if (len(sys.argv) < 2):
    raise Exception("Missing file path argument.")

filePath = sys.argv[1]

if not os.path.isfile(filePath):
    raise Exception(f"File {filePath} cannot be located!")

inputText = open(filePath)
outputText = ''
for line in inputText:
    words = line.split()
    for word in words:
        suffix = ""
        if word.startswith("<"):
            suffix = word[word.index('>')+1:]
            word = input(f"{word[1:word.index('>')]}?> ")
        outputText += f" {word}{suffix}"
    outputText += "\n"
print()
print(outputText)
