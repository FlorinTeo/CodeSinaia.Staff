import WordleHelper

def printHelp():
    print("""Available commands:
    add {url}:
        add words from the given url.
        i.e> add https://www.gutenberg.org/files/16/16-0.txt
    clear {words|hints}:
        clears the database of words or the list of provided hints.
    match {pattern}:
        matches a given pattern against the database and prints top results.
        i.e> match ~H+UM~AN
        where WORDLE hints are ~(Orange), +(Green). A letter without a prefix
        is assumed to be (Black).
    save {file_name}:
        saves the wordle database to a text file.
    load {file_name}:
        loads the wordle database from a text file.
    stats {words|hints}:
        prints stats data on words database or the list of hints. 
    quit:
        quits WORDLE Helper.""")

print("Let me help you with today's WORDLE...")
wordleHelper = WordleHelper.WordleHelper()

# command loop
while(True):
    try:
        cmdLine = input("WORDLE Helper? > ").split()
        cmd = cmdLine[0].upper()
        if cmd == "?" or cmd == "HELP":
            printHelp()
        elif cmd == "ADD":
            wordleHelper.cmdAdd(cmdLine[1:])
        elif cmd == "LOAD":
            wordleHelper.cmdLoad(cmdLine[1:])
        elif cmd == "SAVE":
            wordleHelper.cmdSave(cmdLine[1:])
        elif cmd == "MATCH":
            wordleHelper.cmdMatch(cmdLine[1:])
        elif cmd == "CLEAR":
            wordleHelper.cmdClear(cmdLine[1:])
        elif cmd == "STATS":
            wordleHelper.cmdStats(cmdLine[1:])
        elif cmd == "QUIT" or cmd == "EXIT":
            break
        else:
            print(f"Command '{cmd}' is not supported")
    except Exception as e:
        print(f"ERROR: {e}")

print("Goodbye!")
