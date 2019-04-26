package team2.shattlebip.Ships;

import java.io.Serializable;
import java.util.ArrayList;

import team2.shattlebip.Models.Cell;

public abstract class BaseShip implements Serializable {
    private int shipSize;
    private Rotation rotation;
    private ArrayList<Cell> cells;

    public int getShipSize(){return shipSize;}
    public ArrayList<Cell> getShipLoaction(){return cells;}

    public BaseShip(int size) {
        shipSize=size;
        rotation=Rotation.HORIZONTAL;
        cells=new ArrayList<>();
    }
    public Rotation getRotation(){return rotation;}

    public void setRotation(Rotation rotation){
        this.rotation = rotation;
    }

    public void addCell(Cell cel)
    {
        cells.add(cel);
    }

    public void rotate(){
        if(rotation==Rotation.HORIZONTAL) {
            rotation=Rotation.VERTICAL;
        }
        else {
            rotation=Rotation.HORIZONTAL;
        }
    }
    public boolean isAlive()
    {
        boolean alive=false;
        for (Cell cell:cells) {
            if(cell.getStatus() != Cell.Status.HIT)
                alive=true;
        }
        return alive;
    }
    public enum Rotation {HORIZONTAL, VERTICAL}

}
