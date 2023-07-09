package KnightsTour;

public class Program {

    // chess board initialized to all 0 (Java default)
    private static int[][] _board = new int[8][8];

    public static void main(String[] args) {
        boolean done = false;

        // randomize the starting position
        int rndR = (int)(Math.random() * 8);
        int rndC = (int)(Math.random() * 8);

        System.out.printf("KnightsTour starting from position (%d, %d)\n", rndR, rndC);

        // go through every possible starting position, until a knight run completes the board
        for (int r = 0; !done && r < _board.length; r++) {
            for (int c = 0; !done && c < _board[r].length; c++) {
                done = knightRun(0, (r + rndR) % _board.length, (c + rndC) % _board[c].length);
            }
        }

        System.out.printf("Solution %s found!\n", done ? "" : "NOT");
        printBoard();
    }

    private static boolean knightRun(int hops, int r, int c) {
        // fill it this position  with the current hops count.
        hops++;
        _board[r][c] = hops;

        // if this was the last hop completing the board, return success.
        if (hops == _board.length * _board[0].length) {
            return true;
        }

        // go through all possible next moves the knight can take from its current position 
        for (int dr=-2; dr <= 2; dr++) {
            for (int dc = -2; dc <= 2; dc++) {
                // knigth's positions are offset with either -2, -1, 1 or 2 for both its row and column
                // but only when when the offsets have different values.
                if (Math.abs(dr * dc)==2 && isValid(r+dr, c+dc) && knightRun(hops, r+dr, c+dc)) {
                    return true;
                }
            }
        }

        // if we're here, the knight could not fill the board. Return the position to "free" and bail with failure.
        _board[r][c] = 0;
        return false;
    }

    private static boolean isValid(int r, int c) {
        return r >= 0 && c >= 0 && r < _board.length && c < _board[0].length && _board[r][c] == 0;
    }

    private static void printBoard() {
        for (int r = 0; r < _board.length; r++) {
            for (int c = 0; c < _board[r].length; c++) {
                System.out.printf("%4d", _board[r][c]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
