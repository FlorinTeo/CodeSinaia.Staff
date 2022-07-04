import math

###
# Demo code on how to use print(), input(), string literals
# variables loops and conditional statements.
print("""This program reads a number from the console
and factors it into powers of prime numbers.""")
number = int(input("Input n = "))
print(f"{number} = ", end="")
factor = 2
first = True
while factor <= math.sqrt(number):
    power = 0
    while number % factor == 0:
        power += 1
        number /= factor
    if power > 0:
        if not first:
            print(" * ", end="")
        else:
            print("", end="")
            first = False
        print(factor, end="")
        if power > 1:
            print(f"^{power}", end="")
    factor += 1
if number > 1:
    print(f"{('' if first else ' * ')}{int(number)}", end="")
