package team2.shattlebip;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.support.v4.content.ContextCompat;
import android.widget.GridView;
import android.widget.TextView;


import java.util.Random;

public class Game {
    private static Game instance;
    private Stage stage;
    private Context context;
    private int numCells1side;
    private TextView textViewGameStage, textViewMessage;
    private Button buttonRestart;
    private Button[] SwitchShipSize;
    private GridView gridViewBoard1;
    private AdapterBoard adapterBoard1;
    private Player player1;
    private int ShipSize;
    private Button buttonRotate;
    private Button buttonDelte;
    private boolean isDeleteButtonPressed;

    private Game() {
    }

    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    /**
     * passes variables from MainActivity
     */
    public void setFields(Context context, int numCells1side,
                          TextView textViewGameStage, TextView textViewMessage,
                          Button buttonRestart,
                          GridView gridViewBoard1,
                          AdapterBoard adapterBoard1,
                          Button[] SwitchShipSize,
                          Button buttonDelete,
                          Button buttonRotate) {
        this.context = context;
        this.numCells1side = numCells1side;
        this.textViewGameStage = textViewGameStage;
        this.textViewMessage = textViewMessage;
        this.buttonRestart = buttonRestart;
        this.SwitchShipSize = SwitchShipSize;
        this.gridViewBoard1 = gridViewBoard1;
        this.adapterBoard1 = adapterBoard1;
        this.buttonRotate = buttonRotate;
        this.buttonDelte = buttonDelete;
        this.ShipSize = 0;
        this.isDeleteButtonPressed = false;
    }

    /**
     * [re]starts game by clearing boards and letting bot secretly arrange its fleet
     */
    public void initialize() {
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteButtoReleased();
                initialize();
            }
        });
        SwitchShipSize[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteButtoReleased();
                setShipSize(1);
                unmarkCurrentShip();
                SwitchShipSize[0].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.noun_ship_s));

            }
        });
        SwitchShipSize[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteButtoReleased();
                setShipSize(2);
                unmarkCurrentShip();
                SwitchShipSize[1].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.noun_battleship_s));
            }
        });
        SwitchShipSize[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteButtoReleased();
                setShipSize(3);
                unmarkCurrentShip();
                SwitchShipSize[2].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.noun_military_ship_s));

            }
        });
        SwitchShipSize[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteButtoReleased();
                setShipSize(4);
                unmarkCurrentShip();
                SwitchShipSize[3].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.noun_warship_s));

            }
        });
        buttonDelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteButtonPressed();
            }
        });

        disableClicking();
        adapterBoard1.clear();
        adapterBoard1.createBattleField(gridViewBoard1, 1, getNumCellsBoardArea());
        player1 = new Player(1);
        enableGameStageArranging();
    }

    public void setShipSize(int size) {
        this.ShipSize = size;
    }

    private void enableGameStageArranging() {
       // putGameStage(Stage.ARRANGING);
        letP1arrange();
    }

    public Context getContext() {
        return context;
    }

    private void letP1arrange() {
        gridViewBoard1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cell cell = (Cell) parent.getAdapter().getItem(position);
                if(!isDeleteButtonPressed) {
                    adapterBoard1.update(1,cell, Cell.Status.MISSED,ShipSize);
                } else {
                    adapterBoard1.delete(cell, Cell.Status.MISSED);
                }
//                player1.addCell(cell);
                adapterBoard1.notifyDataSetChanged();
            }
        });
    }

    public void unmarkCurrentShip() {
        SwitchShipSize[0].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.noun_ship));
        SwitchShipSize[1].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.noun_battleship));
        SwitchShipSize[2].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.noun_military_ship));
        SwitchShipSize[3].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.noun_warship));
    }

    private void putGameStage(Stage stage) {
        this.stage = stage;
        String msg = "Game stage: " + stage;
        textViewGameStage.setText(msg);
    }

    private void setMessage(String msg) {
        textViewMessage.setText("Message: " + msg);
    }

    private void disableClicking() {
        gridViewBoard1.setOnItemClickListener(null);
    }

    private int getNumCellsBoardArea() {
        return (int) Math.pow(numCells1side, 2);
    }

    public void deleteButtonPressed() {
        this.isDeleteButtonPressed = true;
    }

    public void deleteButtoReleased() {
        this.isDeleteButtonPressed = false;
    }


    private enum Stage {
        ARRANGING, BATTLING, ATTACKING
    }
}
