import requests

###
# Iterable class taking a URL as a parameter in the constructor,
# fetching from the web the page at that link (assumed plain text)
# and iterating through each individual word within that page.
class WordScraper:
    ###
    # Class fields:
    # _content[][]: jagged array containing individual words
    # _iLine: current line index (initially 0)
    # _iWord: current word index within _content[_iLine]

    ###
    # "Static" class method, returning the flat text extracted from one
    # of the following sources:
    # - a webpage (if the source starts with "http")
    # - raw input from the console (if any text is provided)
    # - clipboard content (if any)
    def loadText(source):
        if source.startsWith("http"):
            return requests.get(source).text
        elif source != "":
            return source
        else:
            raise Exception("Missing content in any of the supported forms (URL, clipboard, raw text)")

    ###
    # Class constructor.
    # url: URL to be scraped (i.e. https://www.gutenberg.org/files/147/147-0.txt)
    def __init__(self, source):
        lines = WordScraper.loadText(source).splitlines()
        self._content = []
        self._iLine = 0
        for line in lines:
            words = line.split()
            self._content.append([])
            for word in words:
                self._content[self._iLine].append(word)
            self._iLine += 1

    ###
    # Iterable initialization:
    # Scrapes the full page and loads it in the jagged array
    # Sets the initial indexes to the postion *before* the first word
    # (_iLine = 0, iWord = -1)
    def __iter__(self):
        self._iLine = 0
        self._iWord = -1
        return self

    ###
    # Iterable next step:
    # Moves the indexes to the next word, skipping as needed
    # over the empty lines and stopping if reaching the end of the text.
    # Returns the new current word, if any.
    def __next__(self):
        # move to the next word
        self._iWord += 1
        while self._iLine < len(self._content) and self._iWord >= len(self._content[self._iLine]):
            self._iLine += 1
            self._iWord = 0
        # if exhausted all lines, stop iterating
        if self._iLine >= len(self._content):
            raise StopIteration
        # _iLine and _iWord are valid and pointing to the next word
        return  self._content[self._iLine][self._iWord]