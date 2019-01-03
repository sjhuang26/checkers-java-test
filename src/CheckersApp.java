import java.util.ArrayList;
import java.util.Scanner;

public class CheckersApp {
    private Game game;
    private Scanner scanner;

    public void launch() {
        scanner = new Scanner(System.in);
        System.out.println("Checkers");
        System.out.println("<> brackets represent commands that can be entered");
        System.out.println("<new-game> to start a new game, <quit> to quit");
        String command;
        while (true) {
            command = scanner.nextLine().trim();
            if (command.equals("new-game")) {
                System.out.println("New game.");
                game = new Game();
                game.play();
            } else if (command.equals("quit")) {
                System.out.println("Quit.");
                return;
            } else {
                System.out.println("Invalid command.");
            }
        }
    }

    private static class Game {
        Board board;
        public Game() {
            board = new Board();
        }

        public GameResult play() {

        }
    }

    private enum Player {
        RED,
        BLACK;

        public static Player nextPlayer(Player currentPlayer) {
            switch (currentPlayer) {
                case RED: return BLACK;
                case BLACK: return RED;
            }
            throw new RuntimeException("Missing player");
        }
    }

    private enum GameResult {
        RED_WON,
        BLACK_WON,
        QUIT;
    }

    private static class Board {
        private Square[][] squares;

        public Board() {
            squares = new Square[8][8];
            for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                    squares[i][j] = Square.BLANK;
                }
            }
        }

        public void applyMove(Move move) {
            SquareReference[] jumps = move.getJumps();
            Square[][] newSquares = squares.clone();
            SquareReference start = jumps[0];
            Square startSquare = getSquare(start);
            if (startSquare == Square.BLANK) {
                throw new RuntimeException("cannot move a blank square");
            }
            if (startSquare == Square.RED_KING || startSquare == Square.RED_NORMAL) {
                if (move.getPlayer() == Player.BLACK) {
                    throw new RuntimeException("cannot move a black piece");
                }
            } else {
                if (move.getPlayer() == Player.RED) {
                    throw new RuntimeException("cannot move a red piece");
                }
            }
            boolean isKing = startSquare == Square.RED_KING || startSquare == Square.BLACK_KING;
        }

        public void print() {
            for (int i = -1; i < 8; ++i) {
                for (int j = -1; j < 8; ++j) {
                    if (i == -1) {
                        if (j == -1) {
                            System.out.print(' ');
                        } else {
                            System.out.print(i);
                        }
                    } else if (j == -1) {
                        System.out.print((char) ((int) 'a' + i));
                    } else {
                        System.out.print(squares[i][j].toString());
                    }
                    System.out.print(' ');
                }
                System.out.print('\n');
            }
        }

        public Square getSquare(SquareReference squareReference) {
            return getSquare(squareReference.getRow(), squareReference.getColumn());
        }

        public Square getSquare(int row, int column) {
            return squares[row][column];
        }
    }

    private static class Move {
        private SquareReference[] jumps;
        private Player player;

        public Move(SquareReference[] jumps) {
            this.jumps = jumps;
        }

        public Move(String input) {
            this.jumps = parseSquareReferences(input);
        }

        private static SquareReference[] parseSquareReferences(String input) {
            String[] tokens = input.split("\\s+");
            ArrayList<SquareReference> arr = new ArrayList<>();
            for (String token : tokens) {
                arr.add(new SquareReference(token.trim()));
            }
            SquareReference[] res = (SquareReference[]) arr.toArray();
            if (res.length < 2) {
                throw new RuntimeException("moves must have at least 2 coordinates");
            }
            return res;
        }

        public SquareReference[] getJumps() {
            return jumps;
        }

        public Player getPlayer() {
            return player;
        }
    }

    private static class SquareReference {
        private int row;
        private int column;

        public SquareReference(int row, int column) {
            if (!(0 <= row && row <= 7 && 0 <= column && column <= 7)) {
                throw new RuntimeException("invalid row/column in coordinates");
            }
            this.row = row;
            this.column = column;
        }

        public SquareReference(String value) {
            this(value.charAt(0) - 'a', parseInt(value.substring(1)));
        }

        private static int parseInt(String value) {
            try {
                return Integer.parseInt(value.substring(1));
            } catch (NumberFormatException ex) {
                throw new RuntimeException("invalid integer in coordinates");
            }
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }
    }

    private enum Square {
        BLANK(' '),
        RED_NORMAL('r'),
        RED_KING('R'),
        BLACK_NORMAL('b'),
        BLACK_KING('B');

        private char character;

        Square(char character) {
            this.character = character;
        }

        @Override
        public String toString() {
            return Character.toString(character);
        }
    }
}