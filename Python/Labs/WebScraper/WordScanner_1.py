from re import L
import WebScraper_2 as ws

###
# Class collecting statistics about a set of words
class WordScanner:
    ###
    # Class fields:
    # _uniqueWords: set collecting all unique words
    # _nWords: total number of words that were scanned

    ###
    # Class constructor. Resets internal data structures and counts 
    def __init__(self):
        self._uniqueWords = set()
        self._nWords = 0

    ###
    # Scans a word and updates internal tallying structures as needed
    def scanWord(self, word):
        self._uniqueWords.add(word)
        self._nWords += 1

    ###
    # Returns a string representation of this WordScanner instance
    def __str__(self):
        output = f"Total scanned words: {self._nWords}"
        output += f"\nUnique words: {len(self._uniqueWords)}"
        return output

###
# Main method for this module: Reads all words from a given web book and use
# a WordScanner object to collect some statistics about the book
if __name__ == "__main__":
    webScraper = ws.WebScraper("https://www.gutenberg.org/cache/epub/16457/pg16457.txt")
    wordScanner = WordScanner()
    
    for word in webScraper:
        wordScanner.scanWord(word)
    
    # Prints the scraper
    print(webScraper)
    # Prints the scanner
    print(wordScanner)