package team2.shattlebip;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.io.File;
import java.util.List;



public class AdapterBoard extends ArrayAdapter<Cell> {
    private LayoutInflater inflater;
    private List<Cell> objects;
    private Context context;

    public List<Cell> getObjects() {
        return objects;
    }


    public AdapterBoard(Context newContext, List<Cell> newObjects) {
        super(newContext, -1, newObjects);
        inflater = LayoutInflater.from(newContext);
        objects = newObjects;
        context = newContext;
    }

    public void update(int playerNum, Cell cell, Cell.Status status,int size) {
        if(collisionCheck(cell, status,size))
            for(int i = 0; i < size; i++)
                this.getItem(this.getPosition(cell) + i).setStatus(status);
    }

    public void delete(Cell cell,Cell.Status status) {
        int cellPosition = this.getPosition(cell);
        if(this.getItem(cellPosition).getStatus() == status)
        while( this.getItem(this.getPosition(cell)).getStatus()== status) {
            this.getItem(this.getPosition(cell)).setStatus(Cell.Status.VACANT);
        }
    }

    public boolean collisionCheck(Cell cell, Cell.Status criticalStatus, int size){
//        int isNewRow = 0;
        size+=2;
        for(int i = 0; i < size; i++) {
//            isNewRow = this.getPosition(cell) + i;
//            isNewRow /= 10;
            if(this.getItem(this.getPosition(cell) + i -1).getStatus() == criticalStatus ||
                this.getItem(this.getPosition(cell) + i -1 + 10).getStatus() == criticalStatus ||
                this.getItem(this.getPosition(cell) + i -1 - 10).getStatus() == criticalStatus ) {
                return  false;
            }
        }
        return true;
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
            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorHit));
        else if (cell.getStatus() == Cell.Status.MISSED)
            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMissed));
        else if (cell.getPlayerNum() == 2)
            button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorUnknown));
        else if (cell.getStatus() == Cell.Status.VACANT) {//button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorVacant));
        }
        else
             button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.splash));
        return view;
    }

    /**
     * creates board with empty cells
     */
    public void createBattleField(GridView gridView, int playerNum, int numCells) {
            gridView.setAdapter(this);
            for (int i = 0; i < numCells; i++)
            this.add(new Cell(playerNum, Cell.Status.VACANT));
    }
}
