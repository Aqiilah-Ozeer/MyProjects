from tabulate import tabulate
import random
import sys
import re

def main():
    print("Welcome to Tic-Tac-Toe!")
    level = difficulty(input("Choose your level of difficulty (easy, medium, hard): "))
    grid = initialise_grid(level)
    display(grid)
    player = "X"
    while True:
        grid = player_move(input(f"Player {player} enter your move (row & column, move eg: 22): "), grid, player)
        display(grid)
        end, text = gamestatus(grid, level, player)
        if end == True:
            sys.exit(text)
        player = switch(player)

def difficulty(l):
   while True:
        if l.lower() == "easy":
            print("Easy level: 3x3 Grid")
            return 3
        elif l.lower() == "medium":
            print("Medium level: 4x4 Grid with Obstacles(#)")
            return 4
        elif l.lower() == "hard":
            print("Hard level: 5x5 Grid with Obstacles(#)")
            return 5
        else:
            l = input("INVALID LEVEL!!! Choose your level of difficulty AGAIN(easy, medium, hard): ")

def initialise_grid(l):
    g = [["" for _ in range(l)] for _ in range(l)]  # creating an empty grid of required dimensions
    # creating obstacels for each level
    num_blocks = 3 if l == 5 else 2 if l == 4 else 0
    for _ in range(num_blocks):
        while True: # ensure not the same cell is block more than 1 time
            row, col = random.randrange(l), random.randrange(l)
            if g[row][col] == "":
                g[row][col] = "#"
                break
    return g

def display(g):
    headers = [""] + [str(i+1) for i in range(len(g))] # creating appropriate header for each level
    table = [[str(i+1)] + row for i, row in enumerate(g)]  # numerating each row
    print(tabulate(table, headers, tablefmt="fancy_grid"))

def player_move(m, g, p):
    while True:     # checking correct format and range another alternative: re.fullmatch(r"([1-{}])([1-{}])".format(len(g), len(g)), m)
        if matches := re.fullmatch(r"([1-" + str(len(g)) + "])([1-" + str(len(g)) + "])", m):
            row = int(matches.group(1)) - 1       # extrating the index for row & col from move entered
            column = int(matches.group(2)) - 1
            if g[row][column] == "":
                g[row][column] = p
                break
            else:
                m = input(f"OCCUPIED CELL!!! Player {p} enter your move AGAIN(row & column, move eg: 22): ")
        else:
            m = input(f"INVALID MOVE!!! Player {p} enter your move AGAIN(row & column, move eg: 22): ")
    return g

def three_in_row(r):
    for i in range(len(r)-2):   # stoping at 3rd to last
        if r[i] != "":   # skipping empty cell at start
            if r[i] == r[i+1] == r[i+2]:
                return True
    return False

def gamestatus(g,l,p):
    for i in range(l):
        c = [g[j][i] for j in range(l)]
        if three_in_row(g[i]) or three_in_row(c):  # check row and col for win
            return True, f"Player {p} won!!! Congrats!!!"
    dl = [g[n][n] for n in range(l)]
    dr = [g[n][l-n-1] for n in range(l)]
    if three_in_row(dl) or three_in_row(dr):   # check main left & right diagonals for win
        return True, f"Player {p} won!!! Congrats!!!"
    if l != 3:  # check other diagonals in larger grids for win
        for z in range(1,l-2):  # creating lists of remaining diagonals
            dla = [g[n][n+z] for n in range(l-z)] # above main left diagonal
            dlb = [g[n+z][n] for n in range(l-z)] # below main left diagonal
            dra = [g[n][l-n-1-z] for n in range(l-z)] # above main right diagonal
            drb = [g[n+z][l-n-1] for n in range(l-z)] # below main right diagonal
            if three_in_row(dla) or three_in_row(dra) or three_in_row(dlb) or three_in_row(drb):
                return True, f"Player {p} won!!! Congrats!!!"
    if all(cell != "" for row in g for cell in row):   # check if all cells occupied for draw
            return True, "Draw!!!"
    return False, ""

def switch(p):
    return "O" if p == "X" else "X"

if __name__ == "__main__":
    main()
