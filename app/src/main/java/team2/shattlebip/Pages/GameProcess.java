package team2.shattlebip.Pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import java.util.ArrayList;

import team2.shattlebip.Game;
import team2.shattlebip.R;
import team2.shattlebip.Resources.Cell;
import team2.shattlebip.View.AdapterBoard;

public class GameProcess extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        gridViewBoard = (GridView) findViewById(R.id.gridViewBoard1);
        adapterBoard = new AdapterBoard(this, new ArrayList<Cell>());
        adapterBoard.createBattleField(gridViewBoard,1,10);
    }
    public GameProcess() {

    }
    private GridView gridViewBoard;
    private AdapterBoard adapterBoard;
    public GameProcess(GridView view, AdapterBoard board)
    {
        gridViewBoard=view;
        adapterBoard=board;
    }

}

