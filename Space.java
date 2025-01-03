package CheckersProject;

public class Space {
    private String piece;
    private boolean hasPiece;
    private Color colorOfPiece;
    private boolean isKing;
    public Space(){
        this.piece = "O";
        this.hasPiece = false;
        this.colorOfPiece = Color.NONE;
        this.isKing = false;
    }
    public Space(Color ColorOfPiece){
        if (ColorOfPiece == Color.RED){
            this.piece = "r";
            this.colorOfPiece = Color.RED;
        }
        else {
            this.piece = "b";
            this.colorOfPiece = Color.BLACK;
        }
        this.isKing = false;
        this.hasPiece = true;
    }

    public void copySpace (Space anotherSpace){
        this.piece = anotherSpace.getPiece();
        this.hasPiece = anotherSpace.hasPieceAtSpace();
        this.colorOfPiece = anotherSpace.getColorOfPiece();
        this.isKing = anotherSpace.isKing();
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public boolean hasPieceAtSpace() {
        return hasPiece;
    }

    public void setHasPiece(boolean hasPiece) {
        this.hasPiece = hasPiece;
    }

    public Color getColorOfPiece() {
        return colorOfPiece;
    }

    public void setColorOfPiece(Color colorOfPiece) {
        this.colorOfPiece = colorOfPiece;
    }

    public boolean isKing() {
        return isKing;
    }

    public void setKing(String piece) {
        this.isKing = true;
        this.piece = piece;

    }

    public void emptySpace(){
        this.setPiece("O");
        this.setHasPiece(false);
        this.setColorOfPiece(Color.NONE);
        this.setKing(this.getPiece().toUpperCase());
    }
}
