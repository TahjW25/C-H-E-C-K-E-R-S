package CheckersProject;

public class Player {
    private int numOfPieces = 12;
    private boolean hasPieces = true;
    private Color color;
    public Player(Color color1){
        if (color1 == Color.RED){
            this.color = Color.RED;
        }
        else {
            this.color = Color.BLACK;
        }
    }

    public int getNumOfPieces() {
        return numOfPieces;
    }

    public void setNumOfPieces(int numOfPieces) {
        this.numOfPieces = numOfPieces;
    }

    public boolean hasPieces() {
        return hasPieces;
    }

    public void setHasPieces(boolean hasPieces) {
        this.hasPieces = hasPieces;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
