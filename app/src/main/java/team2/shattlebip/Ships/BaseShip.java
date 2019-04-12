package team2.shattlebip.Ships;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import team2.shattlebip.R;
import team2.shattlebip.Resources.Cell;

public abstract class BaseShip {
    private int shipSize;
    private Rotation rotation;
    public int getShipSize(){return shipSize;}
    private ArrayList<Cell> cells;
    public ArrayList<Cell> getShipLoaction(){return cells;}
    public void takeDamage(Cell cell)
    {
        for (Cell cel:cells) {
            if(cel==cell)
                cells.remove(cel);
        }
    }
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
    public enum Rotation {HORIZONTAL, VERTICAL}
}
