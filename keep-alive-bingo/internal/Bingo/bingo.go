package bingo

import (
	"fmt"
	"math/rand"
)

var (
	rows = 5
	cols = 5
)

type Bingo struct {
	Board   [5][5]int
	Numbers [75]bool
}

func NewBingo() *Bingo {
	return &Bingo{
		Board: generateBingoBoard(),
	}
}

func generateBingoBoard() [5][5]int {

	var bingoNumbers [75]bool
	var bingoBoard [5][5]int

	// Generate random numbers for the bingo board
	for i := 0; i < rows; i++ {
		for j := 0; j < cols; j++ {
			// Ensure that each number is unique
			var num int
			for {
				num = rand.Intn(15) + j*15 + 1
				if !bingoNumbers[num-1] {
					bingoNumbers[num-1] = true
					break
				}
			}
			bingoBoard[i][j] = num
		}
	}

	// Set the center of the board to 0
	bingoBoard[2][2] = 0

	return bingoBoard

}

func (b *Bingo) PrintBingoBoard() string {
	var board string
	board = fmt.Sprintln("B\tI\tN\tG\tO")
	for i := 0; i < rows; i++ {
		for j := 0; j < cols; j++ {
			if b.Board[i][j] == 0 {
				board += "X\t"
			} else {
				if j == cols-1 {
					board += fmt.Sprintf("%d", b.Board[i][j])
				} else {
					board += fmt.Sprintf("%d\t", b.Board[i][j])
				}

			}
		}
		board += fmt.Sprintln()
	}
	return board
}

func (b *Bingo) CheckBingo() bool {
	// Check rows
	for i := 0; i < rows; i++ {
		if b.Board[i][0] == 0 && b.Board[i][1] == 0 && b.Board[i][2] == 0 && b.Board[i][3] == 0 && b.Board[i][4] == 0 {
			return true
		}
	}

	// Check columns
	for i := 0; i < cols; i++ {
		if b.Board[0][i] == 0 && b.Board[1][i] == 0 && b.Board[2][i] == 0 && b.Board[3][i] == 0 && b.Board[4][i] == 0 {
			return true
		}
	}

	// Check diagonals
	if b.Board[0][0] == 0 && b.Board[1][1] == 0 && b.Board[2][2] == 0 && b.Board[3][3] == 0 && b.Board[4][4] == 0 {
		return true
	}
	if b.Board[0][4] == 0 && b.Board[1][3] == 0 && b.Board[2][2] == 0 && b.Board[3][1] == 0 && b.Board[4][0] == 0 {
		return true
	}

	return false
}

func (b *Bingo) BroadcastNumber() int {
	var num int
	for {
		num = rand.Intn(75) + 1
		if !b.Numbers[num-1] {
			b.Numbers[num-1] = true
			break
		}
	}
	return num
}

func (b *Bingo) IsNumberBroadcasted(num int) bool {
	return b.Numbers[num-1]
}

func (b *Bingo) MarkNumber(num int) bool {
	// check if the number is broadcasted
	if b.IsNumberBroadcasted(num) {
		for i := 0; i < rows; i++ {
			for j := 0; j < cols; j++ {
				if b.Board[i][j] == num {
					b.Board[i][j] = 0
					return true
				}
			}
		}
	}

	return false
}
