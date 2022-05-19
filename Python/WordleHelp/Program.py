import WordleHelper

def printHelp():
    print("""Available commands:
    add {url}:
        add words from the given url.
        i.e> add https://www.gutenberg.org/files/16/16-0.txt
    clear {words|matches}:
        clears the database of words or the past matches
    match {pattern}:
        matches a given pattern against the database and prints top results.
        i.e> match ~H+U-M-A-N
        where WORDLE hints are ~(Orange), +(Green) and -(Black)
    save {file_name}:
        saves the wordle database to a text file.
    load {file_name}:
        loads the wordle database from a text file.
    quit:
        quits WORDLE Helper.""")

print("Let me help you with today's WORDLE...")
wordleHelper = WordleHelper.WordleHelper()

while(True):
    cmdLine = input("WORDLE Helper? > ").split()
    cmd = cmdLine[0].upper()
    if (cmd == "?" or cmd == "HELP"):
        printHelp()
    elif(cmd == "ADD"):
        wordleHelper.cmdAdd(cmdLine[1:])
    elif(cmd == "LOAD"):
        wordleHelper.cmdLoad(cmdLine[1:])
    elif(cmd == "SAVE"):
        wordleHelper.cmdSave(cmdLine[1:])
    elif(cmd == "MATCH"):
        wordleHelper.cmdMatch(cmdLine[1:])
    elif(cmd == "CLEAR"):
        wordleHelper.cmdClear(cmdLine[1:])
    elif(cmd == "QUIT" or cmd == "EXIT"):
        break
    else:
        print(f"Command '{cmd}' is not supported")
print("Goodbye!")
