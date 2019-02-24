/**
 * Main Driver for game loop.
 */

import java.util.Scanner;

/**
 * Initialized the state of the game, and runs the game loop.
 *
 */
public class GameManager {
    private static String black = "black";
    private static String white = "white";

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        // Initializes standard chess board and sets up TextActuator.
        Board board = new Board();
        Space[][] spaces = board.getBoard();
        initBoardStandardChess(spaces);
        GameState currentState = new GameState(0, board);
        TextActuator actuator = new TextActuator(0);
        Scanner kb = new Scanner(System.in);

        // Sets up game loop.
        boolean gameIsRunning = true;
        String s;
        Position PBefore, PAfter;

        // The game loop.
        while (gameIsRunning){

            //Prints Board.
            actuator.printBoard(spaces);
            // Gets move from player, parses move and attempts to make move if legal.
            System.out.println("Enter the move you want to make(Ex. B1,A3): ");
            s=kb.nextLine();
            String[] split = s.split(",");
            PBefore = Position.parsePosition(split[0]);
            PAfter = Position.parsePosition(split[1]);
            System.out.println(PBefore.row + " " + PBefore.col + " " + PAfter.row + " " + PAfter.col);
            //Add a check to make sure entered move works.
            spaces[PBefore.row][PBefore.col].getpiece().move(spaces, PBefore.row, PBefore.col, PAfter.row, PAfter.col);
            //Check for check mate, if in check mate set gameIsRunning to false.
        }
    }


    private static void initBoardStandardChess(Space[][] spaces) {
        for (int i = 0; i < spaces.length; i++) {
            spaces[1][i] = new Space(new Pawn(black, 0));
            spaces[spaces.length - 2][i] = new Space(new Pawn(white, 0));
        }

        int[] emptyArray = new int[]{0,0,0,0,0,0,0,0}; //TODO: add values OR have classes have predefined values
        initStandardRow(spaces[0], black, emptyArray);
        initStandardRow(spaces[spaces.length - 1], white, emptyArray);
    }

    private static void initStandardRow(Space[] row, String color, int[] valueArray) {
        row[0] = new Space(new Rook(color, valueArray[0]));
        row[1] = new Space(new Knight(color, valueArray[1]));
        row[2] = new Space(new Bishop(color, valueArray[2]));
        row[3] = new Space(new Queen(color, valueArray[3]));
        row[4] = new Space(new King(color, valueArray[4]));
        row[5] = new Space(new Bishop(color, valueArray[5]));
        row[6] = new Space(new Knight(color, valueArray[6]));
        row[7] = new Space(new Rook(color, valueArray[7]));
    }

    private static <T> void reverseArray(T[] array) {
        for (int i = 0; i < (array.length / 2) - 1; i++) {
            T temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }
}
