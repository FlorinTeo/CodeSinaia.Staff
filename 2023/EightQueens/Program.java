package EightQueens;

public class Program {

    private static int[][] _board = new int[8][8];

    public static void main(String[] args) {
        if (placeQueen(1)) {
            System.out.printf("Solution FOUND!\n");
            printBoard();
        } else {
            System.out.println("NO solution found!\n");
        }
    }

    private static boolean placeQueen(int nQueen) {
        int rnd = (int)(Math.random() * 8);
        for (int r = 0; r < _board.length; r++) {
            for (int c = 0; c < _board[r].length; c++) {
                int rR = (r + rnd) % 8;
                int rC = (c + rnd) % 8;
                if (_board[rR][rC] == 0) {
                    updateBoard(rR, rC, 1);
                    if  (nQueen == 8 || placeQueen(nQueen+1)) {
                        // trick: as we're on a straight backgrack,
                        // switch this queen's position to it's counter
                        // as a negative number to disambiguate from the
                        // other markings when printing the board.
                        _board[rR][rC] = -nQueen;
                        return true;
                    } else {
                        updateBoard(rR, rC, -1);
                    }
                }
            }
        }
        return false;
    }

    private static void updateBoard(int r, int c, int value) {
        for (int i = 0; i < 8; i++) {
            _board[r][i] += (i == r) ? 0 : value;
            _board[i][c] += (i == c) ? 0 : value;
        }

        for (int i=1; i < 8; i++) {
            if (r + i < 8) {
                if (c + i < 8) {
                    _board[r + i][c + i] += value;
                }
                if (c - i >= 0) {
                    _board[r + i][c - i] += value;
                }
            }
            if (r - i >= 0) {
                if (c + i < 8) {
                    _board[r - i][c + i] += value;
                }
                if (c - i >= 0) {
                    _board[r - i][c - i] += value;
                }
            }
        }
        _board[r][c]=(value > 0) ? 1 : 0;
    }

    public static void printBoard() {
        for (int r = 0; r < _board.length; r++) {
            for (int c = 0; c < _board[r].length; c++) {
                if (_board[r][c] < 0) {
                    System.out.printf("%d ", -_board[r][c]);
                } else {
                    System.out.printf(". ");
                }
            }
            System.out.println();
        }
    }
}
