import os
import requests

class WebScraper:
    ###
    # Class fields:
    # _url: URL to be scraped
    # _filename: filename to be used to save the scraped words
    # _words: words extracted from the URL page
    # _iWords: current word in the array _words
    # _scraped: boolean indicating whether the page was scraped

    ###
    # Class constructor. Retains internally the URL and resets the state fields:
    # No words, count is 0, page had not been scraped yet.
    def __init__(self, url, filename = None):
        self._url = url
        self._filename = filename
        self._words = []
        self._iWord = 0
        self._scraped = False
        self._saved = False

    ###
    # Check whether a world fits the wordle criteria: only 5 letters, each in the range a-Z or A-Z
    # return True if criteria is met, False otherwise
    def isWordleWord(word):
        return True if len(word) == 5 and word.isalpha() else False

    ###
    # Initializes the iterator:
    # If the webpage was scraped previously, just reset the current _iWord to 0
    # Otherwise scrape the page for the first time and filter the wordle words and and store them in the internal array _words
    def __iter__(self):
        if not self._scraped:
            lines = requests.get(self._url).text.splitlines()
            for line in lines:
                words = line.split()
                for word in words:
                    if WebScraper.isWordleWord(word):
                        self._words.append(word.upper())
            self._scraped = True
        self._iWord = 0
        return self

    ###
    # Moves the iterator to the next word in the internal array _words, if available
    def __next__(self):
        if self._iWord == len(self._words):
            raise StopIteration
        word = self._words[self._iWord]
        self._iWord += 1
        return word

    ###
    # Saves the scraped content to the previously given _filename on the local disk
    def save(self):
        if self._filename == None:
            raise Exception("#Error#: No filename provided!")
        if not self._scraped:
            raise Exception("#Error#: Web page NOT scraped yet!")
        if self._saved:
            raise Exception("#Error#: Web page already saved!")
        
        file = open(self._filename, "w")
        for word in self._words: file.write(word + '\n')
        self._saved = True

    ###
    # Returns the string representation of the object
    def __str__(self):
        if not self._scraped:
            output = f"Page {self._url} not scraped yet!"
        else:
            output = f"{len(self._words)} wordle words scraped from page {self._url}"
            if self._saved:
                output += f"\nFile {self._filename} of size {os.path.getsize(self._filename)} saved to disk."
            elif self._filename != None:
                output += f"\nFile {self._filename} not saved yet."
            else:
                output += f"\nNo file saving requested."
        return output

# Tests the PlayMadLibs function on the simplest MadLibs text
if __name__ == "__main__":
    # print the resulting text
    webScraper = WebScraper("https://www.gutenberg.org/files/2701/2701-0.txt", "MobbyDick_words.txt")
    print(webScraper)
    # print 
    for word in webScraper:
        print(word)
    webScraper.save()
    print(webScraper)
