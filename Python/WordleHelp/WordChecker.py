import WordleHelper

class WordChecker:
    ###
    # Class constants. Character markers in a WORDLE hint
    GREEN = '+'
    ORANGE = '~'

    ###
    # Clears the history of hints
    def clear(self):
        self._greenChars = [None for i in range(0, WordleHelper.WordleHelper.WORDLE_LENGTH)]
        self._blackChars = set()
        self._orangeChars = {}
        self._allHints = []
        print(f"WorldChecker is cleared!")

    ###
    # Class constructor and fields:
    # _greenChars: array of characters as they have been found to match positions in the word
    # _blackChars: set of characters known to not be present in the word
    # _orangeChars: map of <character, array of true/false>. if character was flagged as orange
    #               on a given position in the word, then _orangeChars[character][position] is true
    #               meaning that the character is not valid for that position but should be
    #               present in the word on any different postion.
    def __init__(self):
        self.clear()

    ###
    # Checks whether the given hint is valid: it should contain exactly WORDLE_LENGTH
    # characters, 0 or more of which can be prefixed by either a GREEN or an ORANGE marker.
    def isValidHint(hint):
        chCount = 0
        markerOk = True
        for ch in hint:
            if markerOk and (ch == WordChecker.GREEN or ch == WordChecker.ORANGE):
                markerOk = False
                continue
            if not ch.isalpha():
                return False
            chCount += 1
            markerOk = chCount < WordleHelper.WordleHelper.WORDLE_LENGTH
            if chCount > WordleHelper.WordleHelper.WORDLE_LENGTH:
                return False
        return chCount == WordleHelper.WordleHelper.WORDLE_LENGTH

    ###
    # Update the green, black and orange sets with a new WORDLE hint.
    # The WORDLE hints are in the form of a valid WORDLE word, with
    # 0 or more characters prefixed by either +(green) or ~(orange)
    # marker, indicating an exact match or just presence in the word.
    def update(self, hint):
        if not WordChecker.isValidHint(hint):
            raise Exception(f"Invalid WORDLE hint '{hint}'!")
        iChar = 0
        lastMarker = None
        for ch in hint:
            if not ch.isalpha():
                lastMarker = ch
                continue
            if lastMarker == None and not self._greenChars.__contains__(ch) and not self._orangeChars.keys().__contains__(ch):
                self._blackChars.add(ch)
            elif lastMarker == WordChecker.GREEN:
                if self._greenChars[iChar] != None and self._greenChars[iChar] != ch:
                    raise Exception(f"Conflicting hint at char {iChar}")
                self._greenChars[iChar] = ch
            elif lastMarker == WordChecker.ORANGE:
                if not ch in self._orangeChars:
                    self._orangeChars[ch] = [False for i in range(0, WordleHelper.WordleHelper.WORDLE_LENGTH)]
                self._orangeChars[ch][iChar] = True
            iChar += 1
            lastMarker = None
        self._allHints.append(hint)

     ###
     # Checks whether the given word is conforming with all the WORDLE hints
     # as they are captured in the green, black and orange collections.
     # @param word - word to be tested.
     # @return true if the word is conforming with all hints, false otherwise.
    def check(self, word):
        if not WordleHelper.WordleHelper.isWordleWord(word):
            raise Exception(f"Invalid WORDLE word '{word}'")
        matchedAsOrange = set()
        for i in range(0, len(word)):
            if word[i] in self._blackChars:
                return False
            if self._greenChars[i] != None and self._greenChars[i] != word[i]:
                return False
            if word[i] in self._orangeChars:
                if self._orangeChars[word[i]][i]:
                    return False
                matchedAsOrange.add(word[i])
        return len(matchedAsOrange) == len(self._orangeChars)
        