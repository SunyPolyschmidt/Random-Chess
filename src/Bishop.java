public class Bishop implements Piece {

    private String color;
    private int value;

    public Bishop(String c, int v) {
        color = c;
        value = v;
    }

    public void move(Space a[][], int currentX, int currentY, int newX, int newY)
    //Still need to program to only move piece if legal move. Also will need to make it so you can only move a piece if it is that color's turn.
    {
        a[currentX][currentY] = null;
        a[newX][newY] = new Space(this);
    }

    public int getvalue() {
        return value;
    }

    public String getColor(){return color;}
}
