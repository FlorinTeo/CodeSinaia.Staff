import json

class WordRecord(json.JSONEncoder):
    def __init__(self, word, frequency, label):
        self._word = word
        self._frq = frequency
        self._lbl = label

    def __str__(self):
        return f"{self._word} : {{{self._frq}, {self._lbl}}}"

    def toCSV(self):
        return f"{self._word},{self._frq},{self._lbl}"

class WordRecordEncoder(json.JSONEncoder):
    def default(self, o):
        return o.__dict__

if __name__ == "__main__":
    words = [
        WordRecord("ebook", 10, 1),
        WordRecord("beard", 12, 5)]

    for word in words:
        print(word)

    outCSVFile = open("words.csv", "w")
    for word in words:
        outCSVFile.write(f"{word.toCSV()}\n")
    outCSVFile.close()

    outJSONFile1 = open("words1.json", "w")
    outJSONFile1.write(json.dumps(list(words), indent=4, cls=WordRecordEncoder ))
    outJSONFile1.close()

    outJSONFile2 = open("words2.json", "w")
    json.dump(list(words), outJSONFile2, indent=4, default=lambda o: o.__dict__)
    outJSONFile2.close()

    inJSONFile = open("words2.json", "r")
    words = json.load(inJSONFile,object_hook=lambda d:
        WordRecord(d["_word"],d["_frq"],d["_lbl"]))

    for word in words:
        print(f"{word.__str__()}")