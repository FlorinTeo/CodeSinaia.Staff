from re import L
import WebScraper_2 as ws

###
# Class collecting statistics about a set of words
class WordScanner:
    ###
    # Class fields:
    # _uniqueWords: set collecting all unique words
    # _mapWordCounts: map pairing each word (key) to its occurrence count (value)
    # _nWords: total number of words that were scanned

    ###
    # Class constructor. Resets internal data structures and counts 
    def __init__(self):
        self._uniqueWords = set()
        self._mapWordCounts = {}
        self._nWords = 0

    ###
    # Scans a word and updates internal tallying structures as needed
    def scanWord(self, word):
        self._uniqueWords.add(word)
        count = 0
        if self._mapWordCounts.__contains__(word):
            count = self._mapWordCounts[word]
        count += 1
        self._mapWordCounts[word] = count
        self._nWords += 1

    ###
    # Groups together all words acording to their frequencies (count of occurrence)
    # Returns a dictionary mapping each frequency to the list of words having that frequency
    def getFrequencies(self):
        mapFrq = {}
        for word in self._mapWordCounts:
            count = self._mapWordCounts[word]
            if not mapFrq.__contains__(count):
                mapFrq[count] = []
            mapFrq[count].append(word)
        return mapFrq

    ###
    # Returns a string representation of this WordScanner instance
    def __str__(self):
        output = f"Total scanned words: {self._nWords}"
        output += f"\nUnique words: {len(self._uniqueWords)}"
        mapFrq = self.getFrequencies()
        sortedFrq = list(sorted(mapFrq, reverse=True))
        for n in range(0,9):
            output += f"\n{sortedFrq[n]} occurrences: "
            output += str(mapFrq[sortedFrq[n]][:10])
        return output

###
# Main method for this module: Reads all words from a given web book and use
# a WordScanner object to collect some statistics about the book
if __name__ == "__main__":
    webScraper = ws.WebScraper("https://www.gutenberg.org/cache/epub/16457/pg16457.txt")
    wordScanner = WordScanner()
    
    # Scan all words in the scanner
    for word in webScraper:
        wordScanner.scanWord(word)
    
    # Prints the scraper
    print(webScraper)
    # Prints the scanner
    print(wordScanner)
