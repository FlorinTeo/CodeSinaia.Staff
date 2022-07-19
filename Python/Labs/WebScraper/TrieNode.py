from re import L
from typing import OrderedDict

class TrieNode:
    ###
    # Class fields:
    # _char: the character value in the node
    # _freq: the frequency of this node (occurrences of words prefixed with the n-grams containing the node)
    # _upLink: the parent TrieNdoe
    # _downLinks: a dictionary mapping a character(key) to the corresponding TrieNode(value)

    def __init__(self, char, upLink):
        self._char = char
        self._freq = 0
        self._upLink = upLink
        self._downLinks = OrderedDict({})

    ###
    # Add a word to the Trie recursively
    def addWord(self, word):
        # update the frequency of this node
        self._freq += 1
        # extract the remaining suffix to be looked at
        suffix = word[1:] if len(word) > 0 and self._char != None else word

        # fix point: if there's nothing left to add we're done
        if len(suffix) == 0:
            return
        
        # identify the next (down) node to call for the given suffix
        if self._downLinks.__contains__(suffix[0]):
            downNode = self._downLinks[suffix[0]]
        else:
            downNode = TrieNode(suffix[0], self)
            self._downLinks[suffix[0]] = downNode
        
        # recursively ask the downNode to add the suffix
        downNode.addWord(suffix)

    ###
    # Prints the trace of a given word through the TRIE
    def trace(self, word):
        if self._char != None:
            print(f"{self._char} : {self._freq}")
            word = word[1:]
        else:
            print(f". : {self._freq}")
        
        if len(word) == 0:
            return

        if self._downLinks.__contains__(word[0]):
            self._downLinks[word[0]].trace(word)
        else:
            print(f"{word} : <missing>")

if __name__ == "__main__":
    root = TrieNode(None, None)
    root.addWord("EBOOK")
    root.addWord("EBONY")
    root.addWord("EBONE")
    root.addWord("BREAK")
    root.trace("BREAD")
