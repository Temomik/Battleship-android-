package team2.shattlebip.Models;

//for each cell on gameboard

import java.io.Serializable;

public class Cell implements Serializable {
    private Status status;
    private Sprite sprite;
    private int X;
    private int Y;
    public  Cell(int x, int y) {
        X=x;
        Y=y;
        status=Status.VACANT;
    }
    public int getX(){return  X;}

    public int getY() {return Y;}

    public Status getStatus() {
        return status;
    }
    public Sprite getSprite() {
        return sprite;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public  boolean isReadyToInteraction() {
        if(this.status == Status.VACANT)
            return  true;
        else
            return  false;
    }

    public  enum Status {
        VACANT,
        HIT,
        MISSED,
        KILED,
        WIN
    }
    public  enum Sprite {
        VACANT,
        HIT,
        MISSED,
        HORIZONTAL_FRONT,
        HORIZONTAL_BACK,
        HORIZONTAL_BODY,
        HORIZONTAL_SINGLE,
        VERTICAL_FRONT,
        VERTICAL_BODY,
        VERTICAL_BACK,
        VERTICAL_SINGLE,
        HORIZONTAL_FRONT_HIT,
        HORIZONTAL_BACK_HIT,
        HORIZONTAL_BODY_HIT,
        HORIZONTAL_SINGLE_HIT,
        VERTICAL_FRONT_HIT,
        VERTICAL_BODY_HIT,
        VERTICAL_BACK_HIT,
        VERTICAL_SINGLE_HIT
    }
}
