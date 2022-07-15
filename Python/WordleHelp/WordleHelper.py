import string
import WordChecker
import WordScraper
import os.path
import os

class WordleHelper:
    ###
    # class constants:
    # The number of characters in a WORDLE word
    WORDLE_LENGTH = 5
    # The number of results to suggest when matching hints
    TOP_N_RESULTS = 200
    # Arguments for commands taking arguments such as {clear} and {stats}
    ARG_WORDS = "words"
    ARG_HINTS = "hints"

    ###
    # Class constructor and fields:
    # _wordChecker: engine performing a check of a given word 
    #               against the aggregated set of constraints.
    # _wordleWordsMap: database of WORDLE words to be evaluated
    #               for extracting proposals matching all constraints.
    def __init__(self):
        self._wordChecker = WordChecker.WordChecker()
        self._wordleWordsMap = {}

    ###
    # Checks whether the given word a distinct set of characters
    def hasDistinctLetters(word):
        charMap = set()
        for ch in word:
            if ch in charMap: return False
            charMap.add(ch)
        return True

    ###
    # Checks whether the given word has only alphabetic characters
    def hasOnlyLetters(word):
        for ch in word:
            if not ch.isalpha(): return False
        return True

    ###
    # Checks whether the given word is a proper WORDLE word
    def isWordleWord(word):
        if len(word) != WordleHelper.WORDLE_LENGTH: return False
        if not WordleHelper.hasOnlyLetters(word): return False
        #if not WordleHelper.hasDistinctLetters(word): return False
        return True

    ###
    # Command handler for adding more words to the database, from a given url source
    # @param args - url string pointing to a web page to scrape and load words from.
    # @throws IOException
    def cmdAdd(self, args):
        nNewWordleWords = 0
        webScraper = WordScraper.WordScraper(" ".join(args))
        for word in webScraper:
            word = word.upper()
            if WordleHelper.isWordleWord(word):
                wordCount = 1
                if word in self._wordleWordsMap:
                    wordCount = self._wordleWordsMap[word] + 1
                else:
                    nNewWordleWords += 1
                self._wordleWordsMap[word] = wordCount
        print(f"Added {nNewWordleWords} to the words list!")

    ###
    # Command handler for saving the content of the WordleWordsMap to a text file.
    # The text is a sequence of lines, each containing the word, tab-separated by its
    # number of occurrences in the map
    # @param args - name of the file to save into.
    # @throws FileNotFoundException 
    def cmdLoad(self, args):
        if len(args) != 1:
            raise Exception(f"Missing or invalid argument '{args}'. Expected file path!")
        filePath = args[0]
        if not os.path.isfile(filePath):
            raise Exception(f"File {filePath} cannot be located!")
        file = open(filePath)
        nLines = 0
        for line in file:
            wordRecord = line.split()
            self._wordleWordsMap[wordRecord[0]] = int(wordRecord[1])
            nLines += 1
            print("+", end="") if nLines % 10 == 0 else print(".", end="")
            if nLines % 80 == 0: print() 
        print()

    ###
    # Command handler for saving the content of the WordleWordsMap to a text file.
    # The text is a sequence of lines, each containing the word, tab-separated by its
    # number of occurrences in the map
    # @param args - name of the file to save into.
    # @throws FileNotFoundException 
    def cmdSave(self, args):
        if len(args) != 1:
            raise Exception(f"Missing or invalid argument '{args}'. Expected file path!")
        filePath = args[0]
        file = open(filePath, "w")
        nLines = 0
        for word in self._wordleWordsMap:
            file.write(f"{word}\t{self._wordleWordsMap[word]}\n")
            nLines += 1
            print("+", end="") if nLines % 10 == 0 else print(".", end="")
            if nLines % 80 == 0: print()
        print()

     ###
     # Command handler for incorporating more hints into the WordChecker, and
     # print out the words from the dictionary that verify all accumulated hints.
     # @param args - if present it's like ["H", "~U", "M", "A", "+N"]
     # which will be used to update the WordChecker.
    def cmdMatch(self, args):
        hint = "".join(args).upper()
        if hint != "":
            self._wordChecker.update(hint)
        nResults = 0
        nPrinted = 0
        for word in self._wordleWordsMap:
            if self._wordChecker.check(word):
                nResults += 1
                if nResults <= WordleHelper.TOP_N_RESULTS:
                    nPrinted += 1
                    print(f"{word} ", end= "" if nPrinted % 10 != 0 else "\n")
        if nPrinted % 10 != 0:
            print()
        print(f"________ Found {nResults} matches. _________")

    ###
    # Command handler for clearing either the internal database of words
    # or the history of hints
    # @param args - either of "words" or "hints"
    def cmdClear(self, args):
        if len(args) == 0:
            os.system('cls')
            return
        errMessage = f"Missing or invalid argument '{args}'. Expected {WordleHelper.ARG_WORDS} or {WordleHelper.ARG_HINTS}!"
        if len(args) != 1: 
            raise Exception(errMessage)
        arg = args[0]
        if arg == WordleHelper.ARG_WORDS:
            self._wordleWordsMap = {}
            print(f"Database of WORDLE words is now empty!")
        elif arg == WordleHelper.ARG_HINTS:
            self._wordChecker.clear()
            print(f"History of hints is now empty!")
        else:
            raise Exception(errMessage)

    ###
    # Command handler for clearing either the internal database of words
    # or the history of hints
    # @param args - either of "words" or "hints"
    def cmdStats(self, args):
        errMessage = f"Missing or invalid argument '{args}'. Expected {WordleHelper.ARG_WORDS} or {WordleHelper.ARG_HINTS}!"
        if len(args) != 1: 
            raise Exception(errMessage)
        arg = args[0]
        if arg == WordleHelper.ARG_WORDS:
            print(f"{len(self._wordleWordsMap.keys())} WORDLE words in the database!")
        elif arg == WordleHelper.ARG_HINTS:
            print(f"({len(self._wordChecker._allHints)}) hints in the database:")
            for hint in self._wordChecker._allHints:
                print(f"    {hint}")
        else:
            raise Exception(errMessage)