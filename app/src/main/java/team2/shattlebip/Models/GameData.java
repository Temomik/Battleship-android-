package team2.shattlebip.Models;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.support.v4.content.ContextCompat;
import android.widget.GridView;
import android.widget.TextView;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import team2.shattlebip.Models.Ships.BaseShip;
import team2.shattlebip.Models.Ships.FourDeckShip;
import team2.shattlebip.Models.Ships.OneDeckShip;
import team2.shattlebip.Models.Ships.ThreeDeckShip;
import team2.shattlebip.Models.Ships.TwoDeckShip;
import team2.shattlebip.Controller.AdapterBoard;
import team2.shattlebip.R;
import team2.shattlebip.databinding.MainActivityBinding;


//tixon


public class GameData {
    private static GameData instance;
    private Context context;
    private TextView rotateImage;
    private TextView deleteImage;
    private boolean isShipButtonPressed;
    private boolean isDeleteButtonPressed;
    private int numCells1side;

    private Button buttonRestart;
    private Button[] SwitchShipSize;
    private Button buttonRotate;
    private Button buttonDelete;
    private Button buttonRandom;

    private GridView myViewBoard;
    private AdapterBoard myBoard;
    private AdapterBoard enemyBoard;
    private MainActivityBinding binding;
    private BaseShip currentShip;
    private Player me;
    private Player enemy;

    private GameData() {
    }

    public Player getEnemy(){return enemy;}
    public Player getMe(){return me;}

    public static GameData getInstance() {
        if (instance == null)
            instance = new GameData();
        return instance;
    }
    /**
     * passes variables from MainActivity
     */
    public void setFields(Context context, int numCells1side,
                          Button buttonRestart,
                          GridView gridViewBoard1,
                          AdapterBoard myBoard,
                          Button[] SwitchShipSize,
                          Button buttonDelete,
                          Button buttonRotate,
                          Button buttonRandom,
                          TextView rotateImage,
                          TextView deleteImage,
                          MainActivityBinding binding,
                          AdapterBoard enemyBoard) {
        this.context = context;
        this.rotateImage = rotateImage;
        this.numCells1side = numCells1side;
        this.buttonRestart = buttonRestart;
        this.deleteImage = deleteImage;
        this.SwitchShipSize = SwitchShipSize;
        this.myViewBoard = gridViewBoard1;
        this.myBoard = myBoard;
        this.enemyBoard=enemyBoard;
        this.buttonRotate = buttonRotate;
        this.buttonDelete = buttonDelete;
        this.buttonRandom=buttonRandom;
        this.binding = binding;
        this.isShipButtonPressed = false;
    }

    /**
     * [re]starts game by clearing boards and letting bot secretly arrange its fleet
     */
    public void initialize() {
        myBoard.clear();
        myBoard.createBattleField(numCells1side);
        me=new Player(myBoard);
        binding.setArrangeHandler(me.getArrangeHandler());
        me.getArrangeHandler().binding = this.binding;
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteButtonReleased();
                initialize();
                isShipButtonPressed = false;
                me.getArrangeHandler().rotateVertical=0;
                me.getArrangeHandler().updateRotateImage(rotateImage);
                unmarkCurrentShip();
            }
        });
        buttonRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                me.getArrangeHandler().rotateVertical^=1;
                me.getArrangeHandler().updateRotateImage(rotateImage);
            }
        });
        buttonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialize();
                isShipButtonPressed = false;
                me.getArrangeHandler().arrangeShipsRandomly(myBoard);
            }
        });
        SwitchShipSize[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unmarkCurrentShip();
                deleteButtonReleased();
                SwitchShipSize[0].setBackground(ContextCompat.getDrawable(getContext(), R.drawable.noun_ship_s));
                isShipButtonPressed = true;
                currentShip = new OneDeckShip();
            }
        });
        SwitchShipSize[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unmarkCurrentShip();
                deleteButtonReleased();
                SwitchShipSize[1].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.noun_battleship_s));
                isShipButtonPressed = true;
                currentShip = new TwoDeckShip();
            }
        });
        SwitchShipSize[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unmarkCurrentShip();
                deleteButtonReleased();
                SwitchShipSize[2].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.noun_military_ship_s));
                isShipButtonPressed = true;
                currentShip = new ThreeDeckShip();
            }
        });
        SwitchShipSize[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unmarkCurrentShip();
                deleteButtonReleased();
                SwitchShipSize[3].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.noun_warship_s));
                isShipButtonPressed = true;
                currentShip = new FourDeckShip();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unmarkCurrentShip();
                isShipButtonPressed = false;
                if(isDeleteButtonPressed == false) {
                    deleteButtonPressed();
                    deleteImage.setText("Delete mode!!!");
                } else {
                    isDeleteButtonPressed = false;
                    deleteImage.setText("");
                }
            }
        });
        disableClicking();
        enemyBoard.clear();
        enemyBoard.createBattleField(numCells1side);
        enemy=new Player(enemyBoard);
        myViewBoard.setAdapter(myBoard);
        enableGameStageArranging();
    }

    private void enableGameStageArranging() {
        letP1arrange();
    }

    public Context getContext() {
        return context;
    }

    /**
     * method to arrange ships
     */
    private void letP1arrange() {
        myViewBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cell cell = (Cell) parent.getAdapter().getItem(position);
                me.getArrangeHandler().isShipSelected=isShipButtonPressed;
                if(me.getArrangeHandler().isShipSelected) {
                    placeShipOnBoard(cell);
                }
                if(isDeleteButtonPressed) {
                    myBoard.delete(cell,me.getArrangeHandler());
                }
                myBoard.notifyDataSetChanged();
            }
        });
        enemy.arrangeShips();
    }

    private void placeShipOnBoard(Cell cell)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try(ObjectOutputStream ous=new ObjectOutputStream(baos)) {
            ous.writeObject(currentShip);
        }
        catch (IOException e)
        {}
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try(ObjectInputStream ois = new ObjectInputStream(bais)) {
            BaseShip cloneShip = (BaseShip) ois.readObject();
            if(me.getArrangeHandler().tryToPlaceShip(cell, me.getBoard(),cloneShip))
                me.getArrangeHandler().addShip(cloneShip);
        }
        catch (ClassNotFoundException e)
        {}
        catch (IOException e)
        {}
    }
    public void unmarkCurrentShip() {
        SwitchShipSize[0].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.noun_ship));
        SwitchShipSize[1].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.noun_battleship));
        SwitchShipSize[2].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.noun_military_ship));
        SwitchShipSize[3].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.noun_warship));
        deleteImage.setText("");
        isDeleteButtonPressed = false;
    }

    private void disableClicking() {
        myViewBoard.setOnItemClickListener(null);
    }

    public void deleteButtonPressed() {
        this.isDeleteButtonPressed = true;
    }

    public void deleteButtonReleased() {
        this.isDeleteButtonPressed = false;
    }
}
