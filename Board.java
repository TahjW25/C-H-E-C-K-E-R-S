package CheckersProject;
import java.util.ArrayList;

public class Board {
    private ArrayList<Space>[][] board = new ArrayList[8][8];
    private String[][] boardShown = new String[8][8];
    private static final String[][] coords = {{" ", "A", "B", "C", "D", "E", "F", "G", "H"},
            {"1"},
            {"2"},
            {"3"},
            {"4"},
            {"5"},
            {"6"},
            {"7"},
            {"8"},
    };

    public Board() {
        generateBoard();
    }

    public void generateBoard() {
        for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
            for (int colIndex = 0; colIndex < 8; colIndex++) {
                if ((colIndex + 1) % 2 != 0 && (rowIndex + 1) % 2 != 0) {
                    this.board[rowIndex][colIndex] = new ArrayList<>();
                    this.board[rowIndex][colIndex].add(new Space(Color.RED));
                }
                if ((colIndex + 1) % 2 == 0 && (rowIndex + 1) % 2 == 0) {
                    this.board[rowIndex][colIndex] = new ArrayList<>();
                    this.board[rowIndex][colIndex].add(new Space(Color.RED));
                }
            }
        }
        for (int rowIndex = 5; rowIndex < 8; rowIndex++) {
            for (int colIndex = 0; colIndex < 8; colIndex++) {
                if ((colIndex + 1) % 2 == 0 && (rowIndex + 1) % 2 == 0){
                    this.board[rowIndex][colIndex] = new ArrayList<>();
                    this.board[rowIndex][colIndex].add(new Space(Color.BLACK));
                }
                if ((colIndex + 1) % 2 != 0 && (rowIndex + 1) % 2 != 0){
                    this.board[rowIndex][colIndex] = new ArrayList<>();
                    this.board[rowIndex][colIndex].add(new Space(Color.BLACK));
                }
            }
        }
        for (int rowIndex = 0; rowIndex < 8; rowIndex++){
            for (int colIndex = 0; colIndex < 8; colIndex++){
                if (this.board[rowIndex][colIndex] == null){
                    this.board[rowIndex][colIndex] = new ArrayList<>();
                    this.board[rowIndex][colIndex].add(new Space());
                }
            }
        }
    }

    public ArrayList<Space>[][] getBoard() {
        return board;
    }

    public static boolean validateCoords (String coordinates) {
        coordinates = coordinates.trim();
        int coordinatesLength = coordinates.length();
        if (coordinatesLength > 2){
            return false;
        }
        String column = String.valueOf(coordinates.charAt(0));
        column = column.toUpperCase();
        int row;
        try {
            row = Integer.parseInt(String.valueOf(coordinates.charAt(1))) - 1;
            LetterCords.valueOf(column).getColIndex();
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return (row < 9);
    }

    public static boolean validateColor (Player player, Space space){
        return (player.getColor() == space.getColorOfPiece());
    }

    public static boolean validMove (int[] initialCoords, int[] finalCoords, ArrayList[][] board, Space movingPiece){
        //TODO: take difference of values of coords, absolute value must be 1 if its a regular move, 2 if its taking a
        // piece, 4 & 6 if performing a double/triple jump (use getter methods isKing and hasPiece for space being jumped
        // over).
        int rowDifference = finalCoords[0] - initialCoords[0];
        int columnDifference = finalCoords[1] - initialCoords[1];
        if (Math.abs(rowDifference) != Math.abs(columnDifference)){
            return false;
        }
        boolean verticalDirection = verticalDirectionOfMove(rowDifference);
        boolean horizontalDirection = horizontalDirectionOfMove(columnDifference);
        if (!(movingPiece.isKing())) {
            boolean isValidVerticalDirection = validDirection(verticalDirection, movingPiece);
            if (!(isValidVerticalDirection)){
                return false;
            }
            if (Math.abs(rowDifference) == 1){
                return true;
            }
            else {
                return validJump(verticalDirection, horizontalDirection, Math.abs(rowDifference), board, movingPiece, initialCoords);
            }

        }
        else{
            return validJump(verticalDirection, horizontalDirection, Math.abs(rowDifference), board, movingPiece, initialCoords);
        }
    }

    public static boolean validJump (boolean verticalDirection, boolean horizontalDirection, int difference, ArrayList[][] board, Space movingPiece, int[] initialCoords){
        //TODO: Check if the jump the piece is trying to make is valid.
        if (difference == 2){
            if (verticalDirection){
                // down and right
                if (horizontalDirection){
                    Space betweenSpace = (Space) board[initialCoords[0] + 1][initialCoords[1] + 1].get(0);
                    boolean isSameColor = (movingPiece.getColorOfPiece() == betweenSpace.getColorOfPiece());
                    return betweenSpace.hasPieceAtSpace() && !(isSameColor);
                }
                // down and left
                else{
                    Space betweenSpace = (Space) board[initialCoords[0] + 1][initialCoords[1] - 1].get(0);
                    boolean isSameColor = (movingPiece.getColorOfPiece() == betweenSpace.getColorOfPiece());
                    return betweenSpace.hasPieceAtSpace() && !(isSameColor);
                }
            }
            else{
                // up and right
                if (horizontalDirection){
                    Space betweenSpace = (Space) board[initialCoords[0] - 1][initialCoords[1] + 1].get(0);
                    boolean isSameColor = (movingPiece.getColorOfPiece() == betweenSpace.getColorOfPiece());
                    return betweenSpace.hasPieceAtSpace() && !(isSameColor);
                }
                // up and left
                else {
                    Space betweenSpace = (Space) board[initialCoords[0] - 1][initialCoords[1] - 1].get(0);
                    boolean isSameColor = (movingPiece.getColorOfPiece() == betweenSpace.getColorOfPiece());
                    return betweenSpace.hasPieceAtSpace() && !(isSameColor);
                }
            }
        }
        else if (difference == 4){
            if (verticalDirection){
                // down and right
                if (horizontalDirection){
                    Space firstBetweenSpace = (Space) board[initialCoords[0] + 1][initialCoords[1] + 1].get(0);
                    Space secondBetweenSpace = (Space) board[initialCoords[0] + 2][initialCoords[1] + 2].get(0);
                    Space thirdBetweenSpace = (Space) board[initialCoords[0] + 3][initialCoords[1] + 3].get(0);
                    boolean isSameColor = (movingPiece.getColorOfPiece() == firstBetweenSpace.getColorOfPiece()) || (movingPiece.getColorOfPiece() == thirdBetweenSpace.getColorOfPiece());
                    boolean hasPiece = (firstBetweenSpace.hasPieceAtSpace() && (thirdBetweenSpace.hasPieceAtSpace()));
                    return !(isSameColor) && hasPiece && !(secondBetweenSpace.hasPieceAtSpace());
                }
                // down and left
                else{
                    Space firstBetweenSpace = (Space) board[initialCoords[0] + 1][initialCoords[1] - 1].get(0);
                    Space secondBetweenSpace = (Space) board[initialCoords[0] + 2][initialCoords[1] - 2].get(0);
                    Space thirdBetweenSpace = (Space) board[initialCoords[0] + 3][initialCoords[1] - 3].get(0);
                    boolean isSameColor = (movingPiece.getColorOfPiece() == firstBetweenSpace.getColorOfPiece()) || (movingPiece.getColorOfPiece() == thirdBetweenSpace.getColorOfPiece());
                    boolean hasPiece = (firstBetweenSpace.hasPieceAtSpace() && (thirdBetweenSpace.hasPieceAtSpace()));
                    return !(isSameColor) && hasPiece && !(secondBetweenSpace.hasPieceAtSpace());
                }
            }
            else{
                // up and right
                if (horizontalDirection){
                    Space firstBetweenSpace = (Space) board[initialCoords[0] - 1][initialCoords[1] + 1].get(0);
                    Space secondBetweenSpace = (Space) board[initialCoords[0] - 2][initialCoords[1] + 2].get(0);
                    Space thirdBetweenSpace = (Space) board[initialCoords[0] - 3][initialCoords[1] + 3].get(0);
                    boolean isSameColor = (movingPiece.getColorOfPiece() == firstBetweenSpace.getColorOfPiece()) || (movingPiece.getColorOfPiece() == thirdBetweenSpace.getColorOfPiece());
                    boolean hasPiece = (firstBetweenSpace.hasPieceAtSpace() && (thirdBetweenSpace.hasPieceAtSpace()));
                    return !(isSameColor) && hasPiece && !(secondBetweenSpace.hasPieceAtSpace());
                }
                // up and left
                else {
                    Space firstBetweenSpace = (Space) board[initialCoords[0] - 1][initialCoords[1] - 1].get(0);
                    Space secondBetweenSpace = (Space) board[initialCoords[0] - 2][initialCoords[1] - 2].get(0);
                    Space thirdBetweenSpace = (Space) board[initialCoords[0] - 3][initialCoords[1] - 3].get(0);
                    boolean isSameColor = (movingPiece.getColorOfPiece() == firstBetweenSpace.getColorOfPiece()) || (movingPiece.getColorOfPiece() == thirdBetweenSpace.getColorOfPiece());
                    boolean hasPiece = (firstBetweenSpace.hasPieceAtSpace() && (thirdBetweenSpace.hasPieceAtSpace()));
                    return !(isSameColor) && hasPiece && !(secondBetweenSpace.hasPieceAtSpace());
                }
            }
        }
        else if (difference == 6){
            if (verticalDirection){
                // down and right
                if (horizontalDirection){
                    Space firstBetweenSpace = (Space) board[initialCoords[0] + 1][initialCoords[1] + 1].get(0);
                    Space secondBetweenSpace = (Space) board[initialCoords[0] + 2][initialCoords[1] + 2].get(0);
                    Space thirdBetweenSpace = (Space) board[initialCoords[0] + 3][initialCoords[1] + 3].get(0);
                    Space fourthBetweenSpace = (Space) board[initialCoords[0] + 4][initialCoords[1] + 4].get(0);
                    Space fifthBetweenSpace = (Space) board[initialCoords[0] + 5][initialCoords[1] + 5].get(0);
                    boolean isSameColor = (movingPiece.getColorOfPiece() == firstBetweenSpace.getColorOfPiece()) || (movingPiece.getColorOfPiece() == thirdBetweenSpace.getColorOfPiece()) || (movingPiece.getColorOfPiece() == fifthBetweenSpace.getColorOfPiece());
                    boolean hasPiece = (firstBetweenSpace.hasPieceAtSpace() && thirdBetweenSpace.hasPieceAtSpace() && fifthBetweenSpace.hasPieceAtSpace());
                    boolean hasPiece2 = (secondBetweenSpace.hasPieceAtSpace() && fourthBetweenSpace.hasPieceAtSpace());
                    return !(isSameColor) && hasPiece && !(hasPiece2);
                }
                // down and left
                else{
                    Space firstBetweenSpace = (Space) board[initialCoords[0] + 1][initialCoords[1] - 1].get(0);
                    Space secondBetweenSpace = (Space) board[initialCoords[0] + 2][initialCoords[1] - 2].get(0);
                    Space thirdBetweenSpace = (Space) board[initialCoords[0] + 3][initialCoords[1] - 3].get(0);
                    Space fourthBetweenSpace = (Space) board[initialCoords[0] + 4][initialCoords[1] - 4].get(0);
                    Space fifthBetweenSpace = (Space) board[initialCoords[0] + 5][initialCoords[1] - 5].get(0);
                    boolean isSameColor = (movingPiece.getColorOfPiece() == firstBetweenSpace.getColorOfPiece()) || (movingPiece.getColorOfPiece() == thirdBetweenSpace.getColorOfPiece()) || (movingPiece.getColorOfPiece() == fifthBetweenSpace.getColorOfPiece());
                    boolean hasPiece = (firstBetweenSpace.hasPieceAtSpace() && thirdBetweenSpace.hasPieceAtSpace() && fifthBetweenSpace.hasPieceAtSpace());
                    boolean hasPiece2 = (secondBetweenSpace.hasPieceAtSpace() && fourthBetweenSpace.hasPieceAtSpace());
                    return !(isSameColor) && hasPiece && !(hasPiece2);
                }
            }
            else{
                // up and right
                if (horizontalDirection){
                    Space firstBetweenSpace = (Space) board[initialCoords[0] - 1][initialCoords[1] + 1].get(0);
                    Space secondBetweenSpace = (Space) board[initialCoords[0] - 2][initialCoords[1] + 2].get(0);
                    Space thirdBetweenSpace = (Space) board[initialCoords[0] - 3][initialCoords[1] + 3].get(0);
                    Space fourthBetweenSpace = (Space) board[initialCoords[0] - 4][initialCoords[1] + 4].get(0);
                    Space fifthBetweenSpace = (Space) board[initialCoords[0] - 5][initialCoords[1] + 5].get(0);
                    boolean isSameColor = (movingPiece.getColorOfPiece() == firstBetweenSpace.getColorOfPiece()) || (movingPiece.getColorOfPiece() == thirdBetweenSpace.getColorOfPiece()) || (movingPiece.getColorOfPiece() == fifthBetweenSpace.getColorOfPiece());
                    boolean hasPiece = (firstBetweenSpace.hasPieceAtSpace() && thirdBetweenSpace.hasPieceAtSpace() && fifthBetweenSpace.hasPieceAtSpace());
                    boolean hasPiece2 = (secondBetweenSpace.hasPieceAtSpace() && fourthBetweenSpace.hasPieceAtSpace());
                    return !(isSameColor) && hasPiece && !(hasPiece2);
                }
                // up and left
                else {
                    Space firstBetweenSpace = (Space) board[initialCoords[0] - 1][initialCoords[1] - 1].get(0);
                    Space secondBetweenSpace = (Space) board[initialCoords[0] - 2][initialCoords[1] - 2].get(0);
                    Space thirdBetweenSpace = (Space) board[initialCoords[0] - 3][initialCoords[1] - 3].get(0);
                    Space fourthBetweenSpace = (Space) board[initialCoords[0] - 4][initialCoords[1] - 4].get(0);
                    Space fifthBetweenSpace = (Space) board[initialCoords[0] - 5][initialCoords[1] - 5].get(0);
                    boolean isSameColor = (movingPiece.getColorOfPiece() == firstBetweenSpace.getColorOfPiece()) || (movingPiece.getColorOfPiece() == thirdBetweenSpace.getColorOfPiece()) || (movingPiece.getColorOfPiece() == fifthBetweenSpace.getColorOfPiece());
                    boolean hasPiece = (firstBetweenSpace.hasPieceAtSpace() && thirdBetweenSpace.hasPieceAtSpace() && fifthBetweenSpace.hasPieceAtSpace());
                    boolean hasPiece2 = (secondBetweenSpace.hasPieceAtSpace() && fourthBetweenSpace.hasPieceAtSpace());
                    return !(isSameColor) && hasPiece && !(hasPiece2);
                }
            }
        }
        else {
            return false;
        }
    }

    public static boolean validDirection (boolean verticalDirection, Space movingPiece){
        //TODO: Check if the direction that the piece is moving in is valid as Man pieces can't move backwards and which
        // they can move depends on their color
        if (movingPiece.getColorOfPiece() == Color.RED){
            return verticalDirection;
        }
        else {
            return !(verticalDirection);
        }
    }

    public static boolean verticalDirectionOfMove(int rowDifference){
        // false = up, true = down
        return rowDifference > 0;
    }

    public static boolean horizontalDirectionOfMove (int columnDifference){
        // false = left, true = right
        return columnDifference > 0;
    }
    public void kingPiece (int[] finalCoords){
        String piece = this.board[finalCoords[0]][finalCoords[1]].get(0).getPiece().toUpperCase();
        if (this.board[finalCoords[0]][finalCoords[1]].get(0).isKing()){
            return;
        }
        if (this.board[finalCoords[0]][finalCoords[1]].get(0).getColorOfPiece() == Color.RED){
            if (finalCoords[0] == 7){
                this.board[finalCoords[0]][finalCoords[1]].get(0).setKing(piece);
            }
        }
        else {
            if (finalCoords[0] == 0){
                this.board[finalCoords[0]][finalCoords[1]].get(0).setKing(piece);
            }
        }
    }

    public void movePiece (int[] initialCoords, int[] finalCoords, Player opposingPlayer){
        //TODO: set space obj at finalCoords to the space obj at initialCoords, and then empty space obj at initialCoords using emptySpace method
        boolean verticalDirection = verticalDirectionOfMove(finalCoords[0] - initialCoords[0]);
        boolean horizontalDirection = horizontalDirectionOfMove(finalCoords[1] - initialCoords[1]);
        int difference = Math.abs(finalCoords[0] - initialCoords[0]);
        switch (difference){
            case 1:
                this.board[finalCoords[0]][finalCoords[1]].get(0).copySpace(this.board[initialCoords[0]][initialCoords[1]].get(0));
                this.board[initialCoords[0]][initialCoords[1]].get(0).emptySpace();
                break;
            case 2:
                if (verticalDirection){
                    // down and right
                    if (horizontalDirection){
                        this.board[initialCoords[0] + 1][initialCoords[1] + 1].get(0).emptySpace();
                    }
                    // down and left
                    else{
                        this.board[initialCoords[0] + 1][initialCoords[1] - 1].get(0).emptySpace();
                    }
                }
                else{
                    // up and right
                    if (horizontalDirection){
                        this.board[initialCoords[0] - 1][initialCoords[1] + 1].get(0).emptySpace();
                    }
                    // up and left
                    else{
                        this.board[initialCoords[0] - 1][initialCoords[1] - 1].get(0).emptySpace();
                    }
                }
                opposingPlayer.setNumOfPieces(opposingPlayer.getNumOfPieces() - 1);
                this.board[finalCoords[0]][finalCoords[1]].get(0).copySpace(this.board[initialCoords[0]][initialCoords[1]].get(0));
                this.board[initialCoords[0]][initialCoords[1]].get(0).emptySpace();
                break;
            case 4:
                if (verticalDirection){
                    // down and right
                    if (horizontalDirection){
                        this.board[initialCoords[0] + 1][initialCoords[1] + 1].get(0).emptySpace();
                        this.board[initialCoords[0] + 3][initialCoords[1] + 3].get(0).emptySpace();
                    }
                    // down and left
                    else{
                        this.board[initialCoords[0] + 1][initialCoords[1] - 1].get(0).emptySpace();
                        this.board[initialCoords[0] + 3][initialCoords[1] - 3].get(0).emptySpace();
                    }
                }
                else{
                    // up and right
                    if (horizontalDirection){
                        this.board[initialCoords[0] - 1][initialCoords[1] + 1].get(0).emptySpace();
                        this.board[initialCoords[0] - 3][initialCoords[1] + 3].get(0).emptySpace();
                    }
                    // up and left
                    else{
                        this.board[initialCoords[0] - 1][initialCoords[1] - 1].get(0).emptySpace();
                        this.board[initialCoords[0] - 3][initialCoords[1] - 3].get(0).emptySpace();
                    }
                }
                opposingPlayer.setNumOfPieces(opposingPlayer.getNumOfPieces() - 2);
                this.board[finalCoords[0]][finalCoords[1]].get(0).copySpace(this.board[initialCoords[0]][initialCoords[1]].get(0));
                this.board[initialCoords[0]][initialCoords[1]].get(0).emptySpace();
                break;
            case 6:
                if (verticalDirection){
                    // down and right
                    if (horizontalDirection){
                        this.board[initialCoords[0] + 1][initialCoords[1] + 1].get(0).emptySpace();
                        this.board[initialCoords[0] + 3][initialCoords[1] + 3].get(0).emptySpace();
                        this.board[initialCoords[0] + 5][initialCoords[1] + 5].get(0).emptySpace();
                    }
                    // down and left
                    else{
                        this.board[initialCoords[0] + 1][initialCoords[1] - 1].get(0).emptySpace();
                        this.board[initialCoords[0] + 3][initialCoords[1] - 3].get(0).emptySpace();
                        this.board[initialCoords[0] + 5][initialCoords[1] - 5].get(0).emptySpace();
                    }
                }
                else{
                    // up and right
                    if (horizontalDirection){
                        this.board[initialCoords[0] - 1][initialCoords[1] + 1].get(0).emptySpace();
                        this.board[initialCoords[0] - 3][initialCoords[1] + 3].get(0).emptySpace();
                        this.board[initialCoords[0] - 5][initialCoords[1] + 5].get(0).emptySpace();
                    }
                    // up and left
                    else{
                        this.board[initialCoords[0] - 1][initialCoords[1] - 1].get(0).emptySpace();
                        this.board[initialCoords[0] - 3][initialCoords[1] - 3].get(0).emptySpace();
                        this.board[initialCoords[0] - 5][initialCoords[1] - 5].get(0).emptySpace();
                    }
                }
                opposingPlayer.setNumOfPieces(opposingPlayer.getNumOfPieces() - 3);
                this.board[finalCoords[0]][finalCoords[1]].get(0).copySpace(this.board[initialCoords[0]][initialCoords[1]].get(0));
                this.board[initialCoords[0]][initialCoords[1]].get(0).emptySpace();
                break;
        }
    }

    public Space getSpaceOnBoard(int[] indexes){
        ArrayList<Space> obj = this.board[indexes[0]][indexes[1]];
        return obj.get(0);
    }

    public void setBoardShown(){
        for (int i = 0; i < this.boardShown.length; i++){
            for (int j = 0; j < this.boardShown[i].length; j++){
                this.boardShown[i][j] = this.board[i][j].get(0).getPiece();
            }
        }
    }

    public void printBoard(){
        for (String coordinate : coords[0]){
            System.out.print(coordinate + " ");
        }
        System.out.println();
        for (int i = 0; i < this.boardShown.length; i++){
            System.out.print(coords[i + 1][0] + " ");
            for (int j = 0; j < this.boardShown[i].length; j++){
                System.out.print(this.boardShown[i][j] + " ");
            }
            System.out.println();
        }
    }
//    public boolean isMoveAvailable(Player player){
//        //TODO: Checks whether a move is still available for the player, player loses if false
//    }


}
