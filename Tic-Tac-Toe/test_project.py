from project import difficulty
from project import initialise_grid
from project import player_move
from project import three_in_row
from project import gamestatus
from project import switch

def test_difficulty():
    assert difficulty("easy") == 3
    assert difficulty("Medium") == 4
    assert difficulty("hArD") == 5

def test_initialise_grid():
    assert initialise_grid(3) == [["","",""],["","",""],["","",""]]

def test_player_move():
    assert player_move("22",[["","",""],["","",""],["","",""]],"X") == [["","",""],["","X",""],["","",""]]

def test_three_in_row():
    assert three_in_row(["","X",""]) == False
    assert three_in_row(["X","X","O"]) == False
    assert three_in_row(["","",""]) == False
    assert three_in_row(["X","X","X",""]) == True
    assert three_in_row(["","X","X","X",""]) == True

def test_gamestatus():
    assert gamestatus([["","",""],["","X",""],["","",""]],3,"X") == (False, "")
    assert gamestatus([["X","O","X"],["O","X","O"],["O","X","O"]],3,"X") == (True, "Draw!!!")
    assert gamestatus([["X","X","X"],["","",""],["","",""]],3,"X") == (True, "Player X won!!! Congrats!!!")
    assert gamestatus([["","","X"],["","","X"],["","","X"]],3,"X") == (True, "Player X won!!! Congrats!!!")
    assert gamestatus([["X","",""],["","X",""],["","","X"]],3,"X") == (True, "Player X won!!! Congrats!!!")
    assert gamestatus([["","X","",""],["","","X",""],["","","","X"],["","","",""]],4,"X") == (True, "Player X won!!! Congrats!!!")
    assert gamestatus([["","","X",""],["","X","",""],["X","","",""],["","","",""]],4,"X") == (True, "Player X won!!! Congrats!!!")
    assert gamestatus([["","","","",""],["","","","",""],["","","","","X"],["","","","X",""],["","","X","",""]],5,"X") == (True, "Player X won!!! Congrats!!!")
    assert gamestatus([["","","","",""],["","","","",""],["X","","","",""],["","X","","",""],["","","X","",""]],5,"X") == (True, "Player X won!!! Congrats!!!")

def test_switch():
    assert switch("X") == "O"
    assert switch("O") == "X"
