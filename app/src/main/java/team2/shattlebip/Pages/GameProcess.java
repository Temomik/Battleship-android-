package team2.shattlebip.Pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import team2.shattlebip.BattleStageHandler;
import team2.shattlebip.GameData;
import team2.shattlebip.R;
import team2.shattlebip.Models.Cell;
import team2.shattlebip.Ships.BaseShip;
import team2.shattlebip.Controller.AdapterBoard;

public class GameProcess extends AppCompatActivity {

    private GridView myViewBoard;
    private GridView enemyViewBoard;
    private GridView hideViewBoard;
    private AdapterBoard myBoard;
    private AdapterBoard enemyBoard;
    private AdapterBoard hideBoard;
    private GameData gameData;
    private BattleStageHandler battleHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        battleHandler=new BattleStageHandler();
        gameData = GameData.getInstance();
        myViewBoard = findViewById(R.id.gridViewBoard2);
        myBoard = gameData.getMe().getBoard();
        myViewBoard.setAdapter(myBoard);
        enemyBoard = gameData.getEnemy().getBoard();
//        enemyViewBoard = findViewById(R.id.gridViewBoard1);
//        enemyViewBoard.setAdapter(enemyBoard);

        hideBoard = new AdapterBoard(this, new ArrayList<Cell>());
        hideBoard.clear();
        hideBoard.createBattleField(10);
        hideViewBoard=findViewById(R.id.gridViewBoard1);
        hideViewBoard.setAdapter(hideBoard);
        makeShot();
    }

    private void makeShot()
    {
//        enemyViewBoard.setOnTouchListener(new AdapterView.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                Cell tmp = view.get
//                Cell cell = (Cell) parent.getAdapter().getItem(position);
//                if(!shotHandler(enemyBoard,gameData.getEnemy().getShips(),cell))
//                    botResponseShot();
//                return false;
//            }
//        })
        hideViewBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cell cell = enemyBoard.getItem(position);
                if(!shotHandler(enemyBoard,gameData.getEnemy().getShips(),cell))
                    botResponseShot();
                hideBoard.getItem(cell.getY()*10 + cell.getX()).setStatus(cell.getStatus());
                hideBoard.getItem(cell.getY()*10 + cell.getX()).setSprite(cell.getSprite());

                for (BaseShip shipIter : gameData.getEnemy().getShips()) {
                    if (shipIter.getShipLoaction().contains(cell)) {
                        battleHandler.hitShip(cell);
                        if (!shipIter.isAlive()) {
                            battleHandler.blockAreaNearBy(hideBoard, shipIter);
                        }
                        break;
                    }
                }
                hideBoard.notifyDataSetChanged();
            }
        });
    }
    private void botResponseShot()
    {
        Random rand = new Random();
        Cell cell;
        do {
            int position = rand.nextInt(100);
            cell = myBoard.getItem(position);
        }while(shotHandler(myBoard,gameData.getMe().getShips(),cell));
    }

    private boolean shotHandler(AdapterBoard enemyBoard, Deque<BaseShip> ships, Cell cell) {
        boolean isHit = false;
        if (cell.isReadyToInteraction()) {
            for (BaseShip shipIter : ships) {
                if (shipIter.getShipLoaction().contains(cell)) {
                    battleHandler.hitShip(cell);
                    isHit = true;
                    if (!shipIter.isAlive())
                        battleHandler.blockAreaNearBy(enemyBoard, shipIter);
                    break;
                }
            }
            if (!isHit) {
                cell.setStatus(Cell.Status.MISSED);
                cell.setSprite(Cell.Sprite.MISSED);
            }
            enemyBoard.notifyDataSetChanged();
            return  isHit;
        }
        return  true;
    }

}

