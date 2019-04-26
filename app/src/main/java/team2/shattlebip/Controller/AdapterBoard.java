package team2.shattlebip.View;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.io.Serializable;
import java.util.List;

import team2.shattlebip.ArrangeHandler;
import team2.shattlebip.R;
import team2.shattlebip.Models.Cell;
import team2.shattlebip.Ships.BaseShip;


public class AdapterBoard extends ArrayAdapter<Cell> implements Serializable {
    private LayoutInflater inflater;
    private List<Cell> objects;

    public List<Cell> getObjects() {
        return objects;
    }


    public AdapterBoard(Context newContext, List<Cell> newObjects) {
        super(newContext, -1, newObjects);
        inflater = LayoutInflater.from(newContext);
        objects = newObjects;
    }
    /*public void setInflater(Context context, GridView viewBoard)
    {
        inflater=LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_cell, viewBoard, false);
    }*/

    /**
     * now takes BaseShip as a parameter
     * @param ship that's need to be arranged
     */
    public void update(Cell cell, BaseShip ship) {
        if(ship.getRotation()== BaseShip.Rotation.HORIZONTAL) {
            for (int i = 0; i < ship.getShipSize(); i++) {
                if( ship.getShipSize() == 1)
                    this.getItem(this.getPosition(cell) + i).setStatus(Cell.Status.HORIZONTAL_SINGLE);
                else
                if(i == 0)
                    this.getItem(this.getPosition(cell) + i).setStatus(Cell.Status.HORIZONTAL_FRONT);
                else
                if(i == ship.getShipSize() -1)
                    this.getItem(this.getPosition(cell) + i).setStatus(Cell.Status.HORIZONTAL_BACK);
                else
                    this.getItem(this.getPosition(cell) + i).setStatus(Cell.Status.HORIZONTAL_BODY);
            }
        }
        else{
            for (int i = 0; i < ship.getShipSize(); i++) {
                if(ship.getShipSize() == 1)
                    this.getItem(this.getPosition(cell) + i*(10)).setStatus(Cell.Status.VERTICAL_SINGLE);
                else
                if(i == 0)
                    this.getItem(this.getPosition(cell) + i*(10)).setStatus(Cell.Status.VERTICAL_FRONT);
                else
                if(i == ship.getShipSize() -1 )
                    this.getItem(this.getPosition(cell) + i*(10)).setStatus(Cell.Status.VERTICAL_BACK);
                else
                    this.getItem(this.getPosition(cell) + i*(10)).setStatus(Cell.Status.VERTICAL_BODY);
            }
        }
    }

    public void delete(Cell cell, ArrangeHandler handler) {
        BaseShip ship=handler.deleteShipByCell(cell);
        if(ship!=null) {
            for (Cell cel:ship.getShipLoaction()) {
                this.getItem(this.getPosition(cel)).setStatus(Cell.Status.VACANT);
            }
        }
    }

    /**
     * sets appearance color according to cell.status
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.layout_cell, parent, false);
        Cell cell = getItem(position);
        Button button = (Button) view.findViewById(R.id.button_board_cell);

        if (cell.getStatus() == Cell.Status.HIT)
            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        else if (cell.getStatus() == Cell.Status.MISSED)
            button.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.splash));
        else if (cell.getStatus() == Cell.Status.VACANT) {
            //button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorVacant));
        }
        else if (cell.getStatus() == Cell.Status.HORIZONTAL_BODY) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.horizontal_body));
        }
        else if (cell.getStatus() == Cell.Status.HORIZONTAL_FRONT) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.horizontal_front));
        }
        else if (cell.getStatus() == Cell.Status.HORIZONTAL_BACK) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.horizontal_back));
        }
        else if (cell.getStatus() == Cell.Status.HORIZONTAL_SINGLE) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.horizontal_single));
        }
        else if (cell.getStatus() == Cell.Status.VERTICAL_BODY) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.vertical_body));
        }
        else if (cell.getStatus() == Cell.Status.VERTICAL_FRONT) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.vertical_front));
        }
        else if (cell.getStatus() == Cell.Status.VERTICAL_BACK) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.vertical_back));
        }
        else if (cell.getStatus() == Cell.Status.VERTICAL_SINGLE) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.vertical_single));
        }
        return view;
    }

    /**
     * creates board with empty cells
     */
    public void createBattleField(int numCells) {
            for (int y = 0; y < numCells; y++)
                for(int x=0;x<numCells;x++)
            this.add(new Cell(x,y));
    }
}
