from tkinter.tix import INTEGER
from tokenize import String
import WordChecker

class WordleHelper:

    # class fields:
    # _wordChecker: engine performing a check of a given word 
    #               against the aggregated set of constraints.
    # _wordleWordsMap: database of WORDLE words to be evaluated
    #               for extracting proposals matching all constraints.


    def __init__(self):
        self._wordleChecker = WordChecker.WordChecker()
        self._wordleWordsMap = dict(); #use python dictionaries as hash maps
        self.counter = 0; #word counter for dictionary

    def cmdAdd(self, args):
        print(f"cmdAdd {list(args)}")
        #adds one word at a time
        if(len(args) != 1):
            print(f"Input error")
        word = args[0].lower();
        #word checker to make sure it is five letters
        self._wordleWordsMap[self.counter] = word;
        self.counter = self.counter + 1; #increment word counter
        print(f"Added {word.upper()} to the words list!")


    def cmdLoad(self, args):
        print(f"cmdLoad {list(args)}")

    def cmdSave(self, args):
        print(f"cmdSave {list(args)}")

    def cmdMatch(self, args):
        print(f"cmdMatch {list(args)}")

    def cmdClear(self, args):
        print(f"cmdClear {list(args)}")
        self.counter = 0; #reset word counter

