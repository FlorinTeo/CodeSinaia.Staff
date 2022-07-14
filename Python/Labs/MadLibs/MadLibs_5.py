import os
import sys

class MadLibs:
    ###
    # Class variables keeping track of
    # the words being read fron the file
    #_words, _iRow, _iCol, _outputText

    ###
    # Given an input file, it initializes the
    # state variables with the content read from the file
    def __init__(self, filePath):
        if not os.path.isfile(filePath):
            raise Exception(f"File {filePath} cannot be located!")

        # read the content into an array of strings each standing
        # for one line of text
        lines = open(filePath).readlines()

        # break the content further into a global two dimensional jagged array
        # of words. Each row is an array of words on that corresponding line.
        self._words = [[] for r in range(len(lines))]
        for r in range(len(lines)):
            # split the array around spaces and other delimiters
            self._words[r] = lines[r].split()

    ###
    # Resets the iterator to the first row and first column
    def __iter__(self):
        # initialize the state variables. We start from
        # first row and first column, with an empty output text
        self._iRow = 0
        self._iCol = 0
        self._outputText = ""
        return self

    ###
    # Extracts the next tagged word and updates the global variables
    def __next__(self):
        while self._iRow < len(self._words):
            while self._iCol < len(self._words[self._iRow]):
                word = self._words[self._iRow][self._iCol]
                self._iCol += 1
                if word.startswith("<"):
                    return word
                self._outputText += f"{word} "
            self._outputText += "\n"
            self._iRow += 1
            self._iCol = 0
        raise StopIteration

    ###
    # Adds the given replacement word to the output
    def addToOutput(self, replacement):
        self._outputText
        self._outputText += f"{replacement} "

###
# Tests the PlayMadLibs function on the simplest MadLibs text
if __name__ == "__main__":
    # print the resulting text
    madLibs = MadLibs("Python\Labs\MadLibs\input\simple.txt")
    for tag in madLibs:
        iStart = tag.find("<")
        iEnd = tag.find(">")
        word = input(f"{tag[iStart+1:iEnd]}?> ")
        madLibs.addToOutput(tag[0:iStart] + word + tag[iEnd+1:])
    print(madLibs._outputText)