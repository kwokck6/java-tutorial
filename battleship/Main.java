package battleship;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static User friend = new User("friend");
    static User foe = new User("foe");
    public static boolean allShipSunk = false;
    static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        System.out.println("Player 1, place your ships on the game field\n");
        friend.initializeBoard();
        System.out.println();
        do {
            System.out.println("Press Enter and pass the move to another player\n...");
        } while (!scanner.nextLine().equals(""));
        System.out.println("Player 2, place your ships on the game field\n");
        foe.initializeBoard();
        System.out.println();
        do {
            System.out.println("Press Enter and pass the move to another player\n...");
        } while (!scanner.nextLine().equals(""));
        System.out.println("The game starts!");
        mainGame();
    }

    public static void mainGame() {
        String coordinates;
        boolean error;
        String response;
        do {
            System.out.println();
            foe.fogBoard.printBoard();
            System.out.println("---------------------");
            friend.secretBoard.printBoard();
            System.out.println();
            System.out.println("Player 1, it's your turn:");
            error = true;
            do {
                coordinates = scanner.next().toUpperCase();
                try {
                    checkValidInput(coordinates);
                    error = false;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } while (error);
            Status status = placeHit(foe, coordinates);
            allShipSunk = foe.secretBoard.isAllShipSunk();
            if (allShipSunk) {
                System.out.println("You sank the last ship. You won. Congratulations!");
                break;
            } else if (status == Status.HIT) {
                shipIsSunk(friend, coordinates);
            } else {
                System.out.println("You missed!");
            }
            do {
                System.out.println("Press Enter and pass the move to another player\n...");
                response = scanner.nextLine();
                response = scanner.nextLine();
            } while (!response.equals(""));
            System.out.println();
            friend.fogBoard.printBoard();
            System.out.println("---------------------");
            foe.secretBoard.printBoard();
            System.out.println();
            System.out.println("Player 2, it's your turn:");
            error = true;
            do {
                coordinates = scanner.next().toUpperCase();
                try {
                    checkValidInput(coordinates);
                    error = false;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } while (error);
            status = placeHit(friend, coordinates);
            allShipSunk = friend.secretBoard.isAllShipSunk();
            if (allShipSunk) {
                System.out.println("You sank the last ship. You won. Congratulations!");
                break;
            } else if (status == Status.HIT) {
                shipIsSunk(friend, coordinates);
            } else {
                System.out.println("You missed!");
            }
            do {
                System.out.println("Press Enter and pass the move to another player\n...");
                response = scanner.nextLine();
                response = scanner.nextLine();
            } while (!response.equals(""));
        } while (true);
    }

    static void checkValidInput(String input) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile("^[A-J]([1-9]|(10))$");
        Matcher startMatcher = pattern.matcher(input);
        boolean valid = startMatcher.find();
        if (!valid) {
            throw new IllegalArgumentException("Error! You entered the wrong coordinates! Try again:");
        }
    }

    private static Status placeHit(User user, String coordinates) {
        int row = coordinates.charAt(0) - 65;
        int col = Integer.parseInt(coordinates.substring(1)) - 1;
        if (user.secretBoard.board[row][col] == Status.OCCUPIED || user.secretBoard.board[row][col] == Status.HIT) {
            user.secretBoard.board[row][col] = Status.HIT;
            user.fogBoard.board[row][col] = Status.HIT;
            return Status.HIT;
        } else {
            user.secretBoard.board[row][col] = Status.MISS;
            user.fogBoard.board[row][col] = Status.MISS;
            return Status.MISS;
        }
    }
    
    private static void shipIsSunk(User user, String coordinates) {
        int row = coordinates.charAt(0) - 65;
        int col = Integer.parseInt(coordinates.substring(1)) - 1;
        boolean isSank = false;
        try {
            isSank = user.secretBoard.board[row - 1][col] != Status.OCCUPIED;
        } catch (ArrayIndexOutOfBoundsException e) {
            // ignore exception and continue
        }
        try {
            isSank &= user.secretBoard.board[row + 1][col] != Status.OCCUPIED;
        } catch (ArrayIndexOutOfBoundsException e) {
            // ignore exception and continue
        }
        try {
            isSank &= user.secretBoard.board[row][col - 1] != Status.OCCUPIED;
        } catch (ArrayIndexOutOfBoundsException e) {
            // ignore exception and continue
        }
        try {
            isSank &= user.secretBoard.board[row][col + 1] != Status.OCCUPIED;
        } catch (ArrayIndexOutOfBoundsException e) {
            // ignore exception and continue
        }

        if (isSank) {
            System.out.println("You sank a ship!");
        } else {
            System.out.println("You hit a ship!");
        }
    }
}

class User {
    Board secretBoard = new Board();
    Board fogBoard = new Board();
    String role;
    static Scanner scanner = new Scanner(System.in);

    User(String role) {
        this.role = role;
    }

    void initializeBoard() {
        secretBoard.printBoard();
        System.out.println();
        for (ShipType shipType: ShipType.values()) {
            System.out.printf("Enter the coordinates of the %s (%d cells):%n",
                    shipType.toString(), shipType.getLength());
            boolean error = true;
            String start;
            String end;
            int startRow;
            int startCol;
            int endRow;
            int endCol;
            do {
                start = scanner.next().toUpperCase();
                end = scanner.next().toUpperCase();
                startRow = Math.min(start.charAt(0), end.charAt(0)) - 65;
                startCol = Math.min(Integer.parseInt(start.substring(1)),
                        Integer.parseInt(end.substring(1))) - 1;
                endRow = Math.max(start.charAt(0), end.charAt(0)) - 65;
                endCol = Math.max(Integer.parseInt(start.substring(1)),
                        Integer.parseInt(end.substring(1))) - 1;
                try {
                    Main.checkValidInput(start);
                    Main.checkValidInput(end);
                    checkValidPosition(shipType, startRow, startCol, endRow, endCol);
                    error = false;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } while (error);
            placeShip(startRow, startCol, endRow, endCol);
            this.secretBoard.printBoard();
            System.out.println();
        }
    }

    private void placeShip(int startRow, int startCol, int endRow, int endCol) {
        if (startRow == endRow) {
            for (int i = startCol; i <= endCol; i++) {
                this.secretBoard.board[startRow][i] = Status.OCCUPIED;
            }
        } else {
            for (int i = startRow; i <= endRow; i++) {
                this.secretBoard.board[i][startCol] = Status.OCCUPIED;
            }
        }
    }

    private void checkValidPosition(ShipType shipType, int startRow, int startCol, int endRow, int endCol)
            throws IllegalArgumentException {
        // check direction of ships
        if ((startRow != endRow) == (startCol != endCol)) {
            throw new IllegalArgumentException("Error! Wrong ship location! Try again:");
        }
        // check length of ships
        int length = startRow == endRow? endCol - startCol + 1: endRow - startRow + 1;
        if (length != shipType.getLength()) {
            throw new IllegalArgumentException(String.format("Error! Wrong length of the %s! Try again:", shipType));
        }
        // check overlap or near
        for (int i = startRow - 1; i <= endRow + 1; i++) {
            for (int j = startCol - 1; j <= endCol + 1; j++) {
                try {
                    if (this.secretBoard.board[i][j] == Status.OCCUPIED) {
                        throw new IllegalArgumentException("Error! You placed it too close to another one. Try again:");
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    // ignore exception and continue
                }
            }
        }
    }
}

class Board {
    Status[][] board = new Status[10][10];

    public Board() {
        for (Status[] row : this.board) {
            Arrays.fill(row, Status.FOG);
        }
    }

    void printBoard() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");  // horizontal header
        char rowLabel = 'A';
        for (Status[] row : this.board) {
            System.out.print(rowLabel);
            for (Status column: row) {
                System.out.print(" " + column.toString());
            }
            System.out.println();
            rowLabel += 1;
        }
    }

    boolean isAllShipSunk() {
        for (Status[] row: this.board) {
            for (Status col: row) {
                if (col == Status.OCCUPIED) {
                    return false;
                }
            }
        }
        return true;
    }
}

enum Status {
    FOG("~"), OCCUPIED("O"), HIT("X"), MISS("M");

    private final String repr;

    Status(String repr) {
        this.repr = repr;
    }

    public String toString() {
        return this.repr;
    }
}

enum ShipType {
    AIRCRAFT_CARRIER("Aircraft Carrier", 5),
    BATTLESHIP("Battleship", 4),
    SUBMARINE("Submarine", 3),
    CRUISER("Cruiser", 3),
    DESTROYER("Destroyer", 2);

    private final String repr;
    private final int length;

    ShipType(String repr, int length) {
        this.repr = repr;
        this.length = length;
    }

    public int getLength() {
        return this.length;
    }

    public String toString() {
        return this.repr;
    }
}
