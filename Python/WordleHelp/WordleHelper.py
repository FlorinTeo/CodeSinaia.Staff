import WordChecker

class WordleHelper:

    # class fields:
    # _wordChecker: engine performing a check of a given word 
    #               against the aggregated set of constraints.
    # _wordleWordsMap: database of WORDLE words to be evaluated
    #               for extracting proposals matching all constraints.

    def __init__(self):
        self._wordleChecker = WordChecker.WordChecker()
        self._wordleWordsMap = {}

    def cmdAdd(self, args):
        print(f"cmdAdd {list(args)}")

    def cmdLoad(self, args):
        print(f"cmdLoad {list(args)}")

    def cmdSave(self, args):
        print(f"cmdSave {list(args)}")

    def cmdMatch(self, args):
        print(f"cmdMatch {list(args)}")

    def cmdClear(self, args):
        print(f"cmdClear {list(args)}")
