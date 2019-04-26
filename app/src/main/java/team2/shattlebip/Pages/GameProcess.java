package team2.shattlebip.Pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.Random;

import team2.shattlebip.BattleStageHandler;
import team2.shattlebip.GameData;
import team2.shattlebip.R;
import team2.shattlebip.Models.Cell;
import team2.shattlebip.Ships.BaseShip;
import team2.shattlebip.Controller.AdapterBoard;

public class GameProcess extends AppCompatActivity {

    private GridView myViewBoard;
    private GridView enemyViewBoard;
    private AdapterBoard myBoard;
    private AdapterBoard enemyBoard;
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
        enemyBoard= gameData.getEnemy().getBoard();
        myViewBoard.setAdapter(myBoard);
        enemyViewBoard=findViewById(R.id.gridViewBoard1);
        enemyViewBoard.setAdapter(enemyBoard);
        makeShot();
    }

    private void makeShot()
    {
        enemyViewBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isHit=false;
                Cell cell = (Cell) parent.getAdapter().getItem(position);
                for (BaseShip ship: gameData.getEnemy().getShips()){
                    if(ship.getShipLoaction().contains(cell)) {
                        cell.setStatus(Cell.Status.HIT);
                        isHit=true;
                        if(!ship.isAlive())
                            battleHandler.blockAreaNearBy(enemyBoard,ship);
                    }
                }
                if(!isHit) {
                    cell.setStatus(Cell.Status.MISSED);
                    botResponseShot();
                }
                enemyBoard.notifyDataSetChanged();
            }
        });
    }
    private void botResponseShot()
    {
        boolean isHit=true;
        Cell cell;
        Random rand=new Random();
        while(isHit) {
            isHit=false;
            do {
                int position = rand.nextInt(100);
                cell = myBoard.getItem(position);
            } while (cell.getStatus() == Cell.Status.HIT || cell.getStatus() == Cell.Status.MISSED);

            for (BaseShip ship : gameData.getMe().getShips()) {
                if (ship.getShipLoaction().contains(cell)) {
                    cell.setStatus(Cell.Status.HIT);
                    isHit = true;
                }
            }
            if (!isHit)
                cell.setStatus(Cell.Status.MISSED);
            myBoard.notifyDataSetChanged();
        }
    }


}

