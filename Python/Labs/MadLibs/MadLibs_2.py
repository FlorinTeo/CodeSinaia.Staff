import os
import sys

###
# Function taking as input a MadLibs text file, plays the game
# and returns the resulting text.
def PlayMadLibs(filePath):
    if not os.path.isfile(filePath):
        raise Exception(f"File {filePath} cannot be located!")

    # Initialize a string variable to hold the output of the game
    # then read the input file line by line and word by word
    outputText = ""
    inputText = open(filePath)
    for line in inputText:
        outputText += "\n"
        words = line.split()
        for word in words:
            suffix = ""
            # if word starts with "<", extract the placeholder text
            # and ask the user for its substitute
            if word.startswith("<"):
                suffix = word[word.index(">")+1:]
                word = input(f"{word[1:word.index('>')]}?> ")
            outputText += f" {word}{suffix}"
    return outputText

###
# Tests the PlayMadLibs function on the simplest MadLibs text
if __name__ == "__main__":
    # print the resulting text
    print(PlayMadLibs("input\simple.txt"))
