import MadLibs_2

# plays MadLibs game on a sequence of input files
# by calling the PlayMadLibs function from MadLibs_2 module
print("Hello to MadLibs!")
for fileName in ["tarzan.txt", "university.txt"]:
    filePath = f"input\{fileName}"
    print(f"\nPlaying MadLibs on '{filePath}'")
    print(MadLibs_2.playMadLibs(filePath))
