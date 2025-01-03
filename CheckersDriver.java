package CheckersProject;
import java.util.Scanner;
public class CheckersDriver {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to Checkers by Tahj Williams! Enter \"Start\" to begin.");
        for (int i = 0; i < 4; i++) {
            String starter = input.next();
            if (starter.equalsIgnoreCase("Start")) {
                playCheckers(input);
                input.close();
                break;
            } else {
                System.out.println("Please, I worked really hard on this");
                if (i == 3){
                    System.out.println("Okay :/");
                }
            }
        }
    }
    public static void playCheckers(Scanner stdin){
        System.out.println("Player 1: \"Red\" or \"Black\"?");
        String p1Color = stdin.next();
        String p2Color = oppositeColor(p1Color);
        System.out.println("Player 2: You are " + p2Color + ".");
        Player p1 = createPlayer(p1Color);
        Player p2 = createPlayer(p2Color);
        Board checkersBoard = new Board();
        checkersBoard.setBoardShown();
        int loser, winner;
        while (true){
            checkersBoard.printBoard();
            playerMovePiece(p1, p2, checkersBoard, stdin, 1);
            checkersBoard.printBoard();
            playerMovePiece(p2, p1, checkersBoard, stdin, 2);
            if (playerLost(p1)){
                loser = 1;
                winner = 2;
                break;
            }
            if (playerLost(p2)){
                loser = 2;
                winner = 1;
                break;
            }
        }
        System.out.println("Player " + loser + " has been defeated. Player " + winner + " is victorious!!");
    }

    public static String oppositeColor(String p1Color){
        if (p1Color.equalsIgnoreCase("Red")){
            return "Black";
        }
        else{
            return "Red";
        }
    }

    public static Player createPlayer(String color){
        if (color.equalsIgnoreCase(Color.BLACK + "")){
            return new Player(Color.BLACK);
        }
        else {
            return new Player(Color.RED);
        }
    }

    public static void playerMovePiece(Player movingPlayer, Player opposingPlayer, Board checkersBoard, Scanner input, int playerNum){
        // TODO: Make playerMovePiece either return a blank new line as a string or return a String saying why the move was unsuccessful
        // TODO: Could make each major stage of this method into its own while loop, so that the player doesn't have to start from the beginning if they mess up
        System.out.println("Player " + playerNum + ": It is your turn.");
        while (true){
            System.out.println("Enter the coordinates for the piece that you wish to move: ");
            String initialCoords = input.next();
            if (!(Board.validateCoords(initialCoords))){
                System.out.println("Inputted coordinates were formatted incorrectly. For example, it should resemble the following: \"E4\".");
                continue;
            }
            int[] initialIndexes = retrieveIndexes(initialCoords);
            Space initialSpace = checkersBoard.getSpaceOnBoard(initialIndexes);
            if (!(initialSpace.hasPieceAtSpace())){
                System.out.println("There is no piece to move at the selected space. Choose again.");
                continue;
            }
            if (!(Board.validateColor(movingPlayer, initialSpace))){
                System.out.println("You cannot move that piece as it belongs to the opposing player");
                continue;
            }
            System.out.println("Enter the coordinates for where you wish to place your selected piece: ");
            String finalCoords = input.next();
            if (!(Board.validateCoords(finalCoords))){
                System.out.println("Inputted coordinates were formatted incorrectly. For example, it should resemble the following: \"E4\".");
                continue;
            }
            int[] finalIndexes = retrieveIndexes(finalCoords);
            Space finalSpace = checkersBoard.getSpaceOnBoard(finalIndexes);
            if (finalSpace.hasPieceAtSpace()){
                System.out.println("Cannot move piece to the selected space as there is already a piece there. Choose again.");
                continue;
            }
            if (!(Board.validMove(initialIndexes, finalIndexes, checkersBoard.getBoard(), initialSpace))){
                System.out.println("Illegal move. Try again.");
                continue;
            }
            checkersBoard.movePiece(initialIndexes, finalIndexes, opposingPlayer);
            checkersBoard.kingPiece(finalIndexes);
            checkersBoard.setBoardShown();
            break;
        }
    }

    public static int[] retrieveIndexes(String coordinates){
        coordinates = coordinates.trim();
        String column = String.valueOf(coordinates.charAt(0)).toUpperCase();
        int row = Integer.parseInt(String.valueOf(coordinates.charAt(1))) - 1;
        int columnNum = LetterCords.valueOf(column).getColIndex();
        return new int[]{row, columnNum};
    }

    public static boolean playerLost (Player player){
        return player.getNumOfPieces() < 0;
    }
}
