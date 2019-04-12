package team2.shattlebip.Resources;

//for each cell on gameboard

public class Cell {
    private int playerNum;
    private Status status;
    private int X;
    private int Y;
    public Cell(int x, int y) {
        X=x;
        Y=y;
        status=Status.VACANT;
    }
    public int getX(){return  X;}

    public int getY() {return Y;}

    public int getPlayerNum() {
        return playerNum;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public  enum Status {
        VACANT, OCCUPIED, HIT , MISSED, HORIZONTAL_FRONT,HORIZONTAL_BACK,HORIZONTAL_BODY,HORIZONTAL_SINGLE,VERTICAL_FRONT,VERTICAL_BODY,VERTICAL_BACK,VERTICAL_SINGLE
    }
}
