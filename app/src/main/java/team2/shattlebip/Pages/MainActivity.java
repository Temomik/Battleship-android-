package team2.shattlebip.Pages;
import android.databinding.DataBindingUtil;
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
import team2.shattlebip.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {
    private int numCellsBoardSide;
    private TextView[] shipCount = new TextView[4];
    private TextView rotateImage;
    private TextView deleteImage;
    private Button  buttonRestart;
    private Button[] SwitchShipSize;
    private Button buttonRotate;
    private Button buttonDelete;
    private Button buttonRandom;
    Button buttonContinue;
    private GridView gridViewBoard1;
    private AdapterBoard adapterBoard1;
    private MainActivityBinding binding;


    //temomik
    /**
     * passes variables to class Game and initializes game
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
//        MainActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        setFields();
        enableGame();

    }

    private void enableGame() {
        Game game = Game.getInstance();
        game.setFields(this, numCellsBoardSide, shipCount, buttonRestart,
                gridViewBoard1, adapterBoard1,SwitchShipSize, buttonDelete,buttonRotate,buttonRandom,buttonContinue,rotateImage, deleteImage,binding);
        game.initialize();
    }

    private void setFields() {
        SwitchShipSize = new Button[10];
        numCellsBoardSide = getResources().getInteger(R.integer.num_cells_board_side);
        shipCount[0] =(TextView) findViewById(R.id.one_deck_count);
        shipCount[1] =(TextView) findViewById(R.id.two_deck_count);
        shipCount[2] =(TextView) findViewById(R.id.three_deck_count);
        shipCount[3] =(TextView) findViewById(R.id.four_deck_count);
        deleteImage = (TextView) findViewById(R.id.layoutе_delete);
        rotateImage = (TextView) findViewById(R.id.layoutе_rotation);
        buttonRestart = (Button) findViewById(R.id.button_initialize);
        buttonRotate = (Button) findViewById(R.id.button_rotate);
        buttonDelete = (Button) findViewById(R.id.button_delete);
        buttonRandom=(Button) findViewById(R.id.button_random);
        buttonContinue=(Button) findViewById(R.id.button_continue);
        SwitchShipSize[0] = (Button) findViewById(R.id.one);
        SwitchShipSize[1] = (Button) findViewById(R.id.two_deck_ship);
        SwitchShipSize[2] = (Button) findViewById(R.id.three_deck_ship);
        SwitchShipSize[3] = (Button) findViewById(R.id.four_deck_ship);
        gridViewBoard1 = (GridView) findViewById(R.id.gridViewBoard1);
        adapterBoard1 = new AdapterBoard(this, new ArrayList<Cell>());
    }
}
