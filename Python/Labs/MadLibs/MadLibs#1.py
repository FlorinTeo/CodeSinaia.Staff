import os
import sys

###
# Simple implementation of the MadLibs game.
# Script expects one command line argument, pointing to a text file on the disk
# The text file is a sequence of lines, each being a sequence of words.
# Words starting with "<" are placeholders to be filled in by the user.
# ref: https://en.wikipedia.org/wiki/Mad_Libs

# Check we have at least one command line argument
if (len(sys.argv) < 2):
    raise Exception("Missing file path argument.")

# Argument is expected to be a file on the disk
filePath = sys.argv[1]
if not os.path.isfile(filePath):
    raise Exception(f"File {filePath} cannot be located!")

# Initialize a string variable to hold the output of the game
# then read the input file line by line and word by word
outputText = ''
inputText = open(filePath)
for line in inputText:
    words = line.split()
    for word in words:
        suffix = ""
        # if word starts with "<", extract the placeholder text
        # and ask the user for its substitute
        if word.startswith("<"):
            suffix = word[word.index(">")+1:]
            word = input(f"{word[1:word.index('>')]}?> ")
        outputText += f" {word}{suffix}"
    outputText += "\n"

# print the resulting text
print()
print(outputText)
