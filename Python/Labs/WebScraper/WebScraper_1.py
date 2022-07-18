import os
import requests

class WebScraper:
    ###
    # Class fields:
    # _url: URL to be scraped
    # _filename: filename to be used to save the scraped words
    # _words: words extracted from the URL page
    # _nWords: total number of words extracted
    # _scraped: boolean indicating whether the page was scraped

    ###
    # Class constructor. Retains internally the URL and resets the state fields:
    # No words, count is 0, page had not been scraped yet.
    def __init__(self, url, filename):
        self._url = url
        self._filename = filename
        self._words = ""
        self._nWords = 0
        self._scraped = False
        self._saved = False

    ###
    # Check whether a world fits the wordle criteria: only 5 letters, each in the range a-Z or A-Z
    # return True if criteria is met, False otherwise
    def isWordleWord(word):
        return True if len(word) == 5 and word.isalpha() else False

    ###
    # Extracts and all words containing only the wordle words (5 letters only, a-z & A-Z only)
    # each word is captialized and placed on a separate line
    def scrape(self):
        if self._scraped:
            raise Exception("#Error#: Web page already scraped!")

        lines = requests.get(self._url).text.splitlines()
        for line in lines:
            words = line.split()
            for word in words:
                if WebScraper.isWordleWord(word):
                    self._words += f"{word.upper()}\n"
                    self._nWords += 1
        self._scraped = True

    ###
    # Saves the scraped content to the previously given _filename on the local disk
    def save(self):
        if not self._scraped:
            raise Exception("#Error#: Web page NOT scraped yet!")
        if self._saved:
            raise Exception("#Error#: Web page already saved!")
        
        file = open(self._filename, "w")
        file.write(self._words)
        self._saved = True

    ###
    # Returns the string representation of the object
    def __str__(self):
        if not self._scraped:
            output = f"Page {self._url} not scraped yet!"
        else:
            output = f"{self._nWords} wordle words scraped from page {self._url}"
            if not self._saved:
                output += f"\nFile {self._filename} not saved yet."
            else:
                output += f"\nFile {self._filename} of size {os.path.getsize(self._filename)} saved to disk."
        return output

# Tests the PlayMadLibs function on the simplest MadLibs text
if __name__ == "__main__":
    # print the resulting text
    webScraper = WebScraper("https://www.gutenberg.org/files/2701/2701-0.txt", "Mobby Dick.txt")
    print(webScraper)
    webScraper.scrape()
    print(webScraper)
    webScraper.save()
    print(webScraper)
