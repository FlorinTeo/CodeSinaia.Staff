import math

_first = True

###
# returns the multiplication symbol, if needed
def multiply():
    global _first
    if _first == True:
        _first = False
        return ""
    else:
        return " * "

###
# returns the formatted prime factor
def factorString(factor, power):
    return f"{factor}^{power}" if power > 1 else f"{factor}"

###
# divides the number n by the factor f for as long as n is still a multiple of f.
# returns a tuple formed by the calculated power of f and the updated n.
def powDivide(factor, number):
    power = 0
    while number % factor == 0:
        power += 1
        number /= factor
    return (power, number)

###
# Demo code on how to use print(), input(), string literals
# variables loops and conditional statements.
print("""This program reads a number from the console
and factors it into powers of prime numbers.""")
number = int(input("Input n = "))
print(f"{number} = ", end="")
factor = 2
while factor <= math.sqrt(number):
    (power, number) = powDivide(factor, number)
    if power > 0:
        print(f"{multiply()}{factorString(factor, power)}", end="")
    factor += 1
if number > 1:
    print(f"{multiply()}{int(number)}")
