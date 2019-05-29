package team2.shattlebip.Controller;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.List;

import team2.shattlebip.ArrangeHandler;
import team2.shattlebip.R;
import team2.shattlebip.Models.Cell;
import team2.shattlebip.Models.Ships.BaseShip;


public class AdapterBoard extends ArrayAdapter<Cell> implements Serializable {
    private LayoutInflater inflater;
    private List<Cell> objects;
    private int width;
    private int height;

    public AdapterBoard(Context newContext, List<Cell> newObjects) {
        super(newContext, -1, newObjects);
        inflater = LayoutInflater.from(newContext);
        objects = newObjects;
        width = 100;
        height = 100;
    }

    public void setSize(int width,int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * now takes BaseShip as a parameter
     * @param ship that's need to be arranged
     */
    public void update(Cell cell, BaseShip ship) {
        if(ship.getRotation()== BaseShip.Rotation.HORIZONTAL) {
            for (int i = 0; i < ship.getShipSize(); i++) {
                if( ship.getShipSize() == 1)
                    this.getItem(this.getPosition(cell) + i).setSprite(Cell.Sprite.HORIZONTAL_SINGLE);
                else
                if(i == 0)
                    this.getItem(this.getPosition(cell) + i).setSprite(Cell.Sprite.HORIZONTAL_FRONT);
                else
                if(i == ship.getShipSize() -1)
                    this.getItem(this.getPosition(cell) + i).setSprite(Cell.Sprite.HORIZONTAL_BACK);
                else
                    this.getItem(this.getPosition(cell) + i).setSprite(Cell.Sprite.HORIZONTAL_BODY);
            }
        }
        else{
            for (int i = 0; i < ship.getShipSize(); i++) {
                if(ship.getShipSize() == 1)
                    this.getItem(this.getPosition(cell) + i*(10)).setSprite(Cell.Sprite.VERTICAL_SINGLE);
                else
                if(i == 0)
                    this.getItem(this.getPosition(cell) + i*(10)).setSprite(Cell.Sprite.VERTICAL_FRONT);
                else
                if(i == ship.getShipSize() -1 )
                    this.getItem(this.getPosition(cell) + i*(10)).setSprite(Cell.Sprite.VERTICAL_BACK);
                else
                    this.getItem(this.getPosition(cell) + i*(10)).setSprite(Cell.Sprite.VERTICAL_BODY);
            }
        }
    }

    public void delete(Cell cell, ArrangeHandler handler) {
        BaseShip ship=handler.deleteShipByCell(cell);
        if(ship!=null) {
            for (Cell cel:ship.getShipLoaction()) {
                this.getItem(this.getPosition(cel)).setSprite(Cell.Sprite.VACANT);
            }
        }
    }

    /**
     * sets appearance color according to cell.Sprite
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        d
//        RelativeLayout relativeLayout = inflater.inflate(R.layout.ce, parent, false);
//        relativeLayout.getLayoutParams().width = 20;
//        relativeLayout.getLayoutParams().height = 20;
//        relativeLayout.requestLayout();

//        RelativeLayout rl = (RelativeLayout)findViewById(R.id.buttonCell);
//        rl.getLayoutParams().height = 100;
//        rl.getLayoutParams().width = 100;
        View tmpview = inflater.inflate(R.layout.layout_cell, parent, false);
        tmpview.getLayoutParams().width = width;
        tmpview.getLayoutParams().height = height;
        tmpview.setPadding(0,0,0,0);
        View view = tmpview;
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();\
//        view.getLayoutParams().width = width;
//        view.getLayoutParams().height = height;
//        view.setPadding(0,0,0,0)    ;
//        view.requestLayout();
//        view.setLayoutParams(layoutParams);
        Cell cell = getItem(position);
        Button button = (Button) view.findViewById(R.id.button_board_cell);
        if (cell.getSprite() == Cell.Sprite.MISSED)
            button.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.splash));
        else if (cell.getSprite() == Cell.Sprite.VACANT) {
            //button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorVacant));
        }
        else if (cell.getSprite() == Cell.Sprite.HORIZONTAL_BODY) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.horizontal_body));
        }
        else if (cell.getSprite() == Cell.Sprite.HORIZONTAL_FRONT) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.horizontal_front));
        }
        else if (cell.getSprite() == Cell.Sprite.HORIZONTAL_BACK) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.horizontal_back));
        }
        else if (cell.getSprite() == Cell.Sprite.HORIZONTAL_SINGLE) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.horizontal_single));
        }
        else if (cell.getSprite() == Cell.Sprite.VERTICAL_BODY) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.vertical_body));
        }
        else if (cell.getSprite() == Cell.Sprite.VERTICAL_FRONT) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.vertical_front));
        }
        else if (cell.getSprite() == Cell.Sprite.VERTICAL_BACK) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.vertical_back));
        }
        else if (cell.getSprite() == Cell.Sprite.VERTICAL_SINGLE) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.vertical_single));
        }
        else if (cell.getSprite() == Cell.Sprite.HORIZONTAL_BODY_HIT) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.horizontal_body_hit));
        }
        else if (cell.getSprite() == Cell.Sprite.HORIZONTAL_FRONT_HIT) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.horizontal_front_hit));
        }
        else if (cell.getSprite() == Cell.Sprite.HORIZONTAL_BACK_HIT) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.horizontal_back_hit));
        }
        else if (cell.getSprite() == Cell.Sprite.HORIZONTAL_SINGLE_HIT) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.horizontal_single_hit));
        }
        else if (cell.getSprite() == Cell.Sprite.VERTICAL_BODY_HIT) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.vertical_body_hit));
        }
        else if (cell.getSprite() == Cell.Sprite.VERTICAL_FRONT_HIT) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.vertical_front_hit));
        }
        else if (cell.getSprite() == Cell.Sprite.VERTICAL_BACK_HIT) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.vertical_back_hit));
        }
        else if (cell.getSprite() == Cell.Sprite.VERTICAL_SINGLE_HIT) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.vertical_single_hit));
        }
        else if (cell.getSprite() == Cell.Sprite.HIT) {
            button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.hit));
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
