package team2.shattlebip.Pages;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import team2.shattlebip.Controller.AdapterBoard;
import team2.shattlebip.Models.GameData;
import team2.shattlebip.Models.Cell;
import team2.shattlebip.R;
import team2.shattlebip.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private int numCellsBoardSide;
    private TextView[] shipCount = new TextView[4];
    private TextView rotateImage;
    private TextView deleteImage;
    private Button  buttonRestart;
    private Button[] SwitchShipSize;
    private Button buttonRotate;
    private Button buttonDelete;
    private Button buttonRandom;
    private Button buttonContinue;
    private GridView myViewBoard;
    private AdapterBoard myBoard;
    private AdapterBoard enemyBoard;
    private MainActivityBinding binding;


    //temomik
    /**
     * passes variables to class GameData and initializes game
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        setFields();
        enableGame();

    }

    private void enableGame() {
        GameData gameData = GameData.getInstance();
        gameData.setFields(this, numCellsBoardSide, buttonRestart,
                myViewBoard, myBoard,SwitchShipSize, buttonDelete,buttonRotate,
                buttonRandom,rotateImage, deleteImage,binding,enemyBoard);
        gameData.initialize();
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
        buttonContinue.setOnClickListener(this);
        SwitchShipSize[0] = (Button) findViewById(R.id.one);
        SwitchShipSize[1] = (Button) findViewById(R.id.two_deck_ship);
        SwitchShipSize[2] = (Button) findViewById(R.id.three_deck_ship);
        SwitchShipSize[3] = (Button) findViewById(R.id.four_deck_ship);
        myViewBoard = (GridView) findViewById(R.id.gridViewBoard1);
        myBoard = new AdapterBoard(this, new ArrayList<Cell>());
        enemyBoard=new AdapterBoard(this,new ArrayList<Cell>());
    }
    private void startGameButtonClick() {
        if(GameData.getInstance().getMe().getArrangeHandler().getShipCount()==10) {
            GameData.getInstance().getMe().finishArranging();
            startActivity(new Intent("team2.shattlebip.Pages.GameProcess"));
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button_continue)
            startGameButtonClick();
    }
}
