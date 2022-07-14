import os
import sys

###
# Global variables keeping track of
# the words being read fron the file
global _words, _iRow, _iCol, _outputText

###
# Given an input file, it initializes the
# state variables with the content read from the file
def initMadLibs(filePath):
    if not os.path.isfile(filePath):
        raise Exception(f"File {filePath} cannot be located!")

    # read the content into an array of strings each standing
    # for one line of text
    lines = open(filePath).readlines()

    # break the content further into a global two dimensional jagged array
    # of words. Each row is an array of words on that corresponding line.
    global _words
    _words = [[] for r in range(len(lines))]
    for r in range(len(lines)):
        # split the array around spaces and other delimiters
        _words[r] = lines[r].split()

    # initialize the state variables. We start from
    # first row and first column, with an empty output text
    global _iRow, _iCol, _outputText
    _iRow = 0
    _iCol = 0
    _outputText = ""

###
# Extracts the next tagged word and updates the global variables
def nextTag():
    global _words, _iRow, _iCol, _outputText
    while _iRow < len(_words):
        while _iCol < len(_words[_iRow]):
            word = _words[_iRow][_iCol]
            _iCol += 1
            if word.startswith("<"):
                return word
            _outputText += f"{word} "
        _outputText += "\n"
        _iRow += 1
        _iCol = 0
    return None

###
# Adds the given replacement word to the output
def addToOutput(replacement):
    global _outputText
    _outputText += f"{replacement} "

###
# Tests the PlayMadLibs function on the simplest MadLibs text
if __name__ == "__main__":
    # print the resulting text
    initMadLibs("Python\Labs\MadLibs\input\simple.txt")
    tag = nextTag()
    while tag != None:
        iStart = tag.find("<")
        iEnd = tag.find(">")
        word = input(f"{tag[iStart+1:iEnd]}?> ")
        addToOutput(tag[0:iStart] + word + tag[iEnd+1:])
        tag = nextTag()
    print(_outputText)