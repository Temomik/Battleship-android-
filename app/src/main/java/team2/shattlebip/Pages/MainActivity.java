package team2.shattlebip.Pages;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import team2.shattlebip.View.AdapterBoard;
import team2.shattlebip.Resources.Cell;
import team2.shattlebip.Game;
import team2.shattlebip.R;

public class MainActivity extends AppCompatActivity {
    private int numCellsBoardSide;
    private TextView textViewGameStage, textViewMessage;
    private Button  buttonRestart;
    private Button[] SwitchShipSize;
    private Button buttonRotate;
    private Button buttonDelete;
    private GridView gridViewBoard1;
    private AdapterBoard adapterBoard1;

    //temomik

    /**
     * passes variables to class Game and initializes game
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setFields();
        enableGame();
    }

    private void enableGame() {
        Game game = Game.getInstance();
        game.setFields(this, numCellsBoardSide, textViewGameStage, textViewMessage, buttonRestart,
                gridViewBoard1, adapterBoard1,SwitchShipSize, buttonDelete,buttonRotate);
        game.initialize();
    }

    private void setFields() {
        SwitchShipSize = new Button[10];
        numCellsBoardSide = getResources().getInteger(R.integer.num_cells_board_side);
        buttonRestart = (Button) findViewById(R.id.button_initialize);
        buttonRotate = (Button) findViewById(R.id.button_rotate);
        buttonDelete = (Button) findViewById(R.id.button_delete);
        SwitchShipSize[0] = (Button) findViewById(R.id.one_deck_ship);
        SwitchShipSize[1] = (Button) findViewById(R.id.two_deck_ship);
        SwitchShipSize[2] = (Button) findViewById(R.id.three_deck_ship);
        SwitchShipSize[3] = (Button) findViewById(R.id.four_deck_ship);
        gridViewBoard1 = (GridView) findViewById(R.id.gridViewBoard1);
        adapterBoard1 = new AdapterBoard(this, new ArrayList<Cell>());
    }
}
