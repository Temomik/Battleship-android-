package team2.shattlebip.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;
import java.util.Stack;

import team2.shattlebip.GameHandlers.BattleStageHandler;
import team2.shattlebip.Models.BotAsync;
import team2.shattlebip.Models.Client;
import team2.shattlebip.Models.GameData;
import team2.shattlebip.R;
import team2.shattlebip.Models.Cell;
import team2.shattlebip.Models.Ships.BaseShip;
import team2.shattlebip.Controller.AdapterBoard;

public class GameProcess extends AppCompatActivity {

    private GridView myViewBoard;
    private GridView hideViewBoard;
    private AdapterBoard myBoard;
    private AdapterBoard enemyBoard;
    private AdapterBoard hideBoard;
    private ImageButton turn;
    private GameData gameData;
    private BattleStageHandler battleHandler;
    private Stack<Cell> cellStack=new Stack<>();
    private Stack<Cell> hitStack = new Stack<>();
    private boolean isWin;
    private boolean isLose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        battleHandler=new BattleStageHandler();
        gameData = GameData.getInstance();
        myViewBoard = findViewById(R.id.gridViewBoard2);
        myBoard = gameData.getMe().getBoard();
        myBoard.setSize(80,80);
        myViewBoard.setColumnWidth(80);
        myViewBoard.setAdapter(myBoard);
        if(MainMenu.isOnline==0)
        enemyBoard = gameData.getEnemy().getBoard();

        hideBoard = new AdapterBoard(this, new ArrayList<Cell>());
        hideBoard.setSize(90,90);

        hideBoard.clear();
        hideBoard.createBattleField(10);
        hideViewBoard=findViewById(R.id.gridViewBoard1);
        hideViewBoard.setColumnWidth(90);
        hideViewBoard.setAdapter(hideBoard);
        turn = findViewById(R.id.turnButton);
//        turn.setImageResource(R.drawable.you);
        if(MainMenu.isOnline==0)
        makeShot();
        else if(MainMenu.isOnline==1)
        makeOnlineShot();
    }

    /*private void makeShot()
    {
        hideViewBoard.setOnItemClickListener((parent, view, position, id) -> {
            isLose=false;
            Cell cell = enemyBoard.getItem(position);
            if(shotHandler(enemyBoard,gameData.getEnemy().getShips(),cell)== Cell.Status.MISSED) {
                turn.setImageResource(R.drawable.enemy);
                botResponseShot();
                isLose=battleHandler.isWinCondition(gameData.getMe());
                if(isLose)
                    setFinalLoseStage();
            }
            hideBoard.getItem(cell.getY()*10 + cell.getX()).setStatus(cell.getStatus());
            hideBoard.getItem(cell.getY()*10 + cell.getX()).setSprite(cell.getSprite());
            turn.setImageResource(R.drawable.you);
            long tStart = System.currentTimeMillis();
            long tDelta = 0;
            for (BaseShip shipIter : gameData.getEnemy().getShips()) {
                if (shipIter.getShipLoaction().contains(cell)) {
                    battleHandler.hitShip(cell);
                    if (!shipIter.isAlive()) {
                        battleHandler.blockAreaNearBy(hideBoard, shipIter);
                        isWin=battleHandler.isWinCondition(gameData.getEnemy());
                    }
                    break;
                }
            }
            hideBoard.notifyDataSetChanged();
            if(isWin)
                setFinalStage();;
        });
    }*/


    private void addNearCells(Cell cel) {
        if(cel.getX()+1<10)
        cellStack.push(myBoard.getItem(cel.getY()*10+cel.getX()+1));
        if(cel.getX()-1>-1)
        cellStack.push(myBoard.getItem(cel.getY()*10+cel.getX()-1));
        if(cel.getY()+1<10)
        cellStack.push(myBoard.getItem(cel.getY()*10+cel.getX()+10));
        if(cel.getY()-1>-1)
        cellStack.push(myBoard.getItem(cel.getY()*10+cel.getX()-10));
    }

    private void botResponseShot()
    {
        Cell.Status status=null;
        Cell cell=null;
        do {
            cell=getCellToShoot(cell,status);
            status=shotHandler(myBoard,gameData.getMe().getShips(),cell);
//            try
//            {
//                Thread.sleep(400);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }while(status== Cell.Status.HIT||status== Cell.Status.NONE);
    }
    private Cell getCellToShoot(Cell cel, Cell.Status status)
    {
        Cell cell;
        if(((status==null&&hitStack.size()==0))||status== Cell.Status.NONE) {
            Random rand = new Random();
            int position = rand.nextInt(100);
            cell = myBoard.getItem(position);
        }
        else
        {
            if(status==Cell.Status.HIT) {
                hitStack.add(cel);
                addNearCells(cel);
            }
            if(!cellStack.empty())
            cell = cellStack.pop();
            else
            {
                Random rand = new Random();
                int position = rand.nextInt(100);
                cell = myBoard.getItem(position);
            }
        }
        for(BaseShip ship: gameData.getMe().getShips()) {
            if (ship.getShipLoaction().contains(cel)&&!ship.isAlive()) {
                cellStack.clear();
                hitStack.clear();
                Random rand = new Random();
                int position = rand.nextInt(100);
                cell = myBoard.getItem(position);
            }
        }
        return cell;
    }
    private Cell.Status shotHandler(AdapterBoard board, Deque<BaseShip> ships, Cell cell) {
        Cell.Status isHit = Cell.Status.MISSED;
        if (cell.isReadyToInteraction()) {
            for (BaseShip ship : ships) {
                if (ship.getShipLoaction().contains(cell)) {
                    battleHandler.hitShip(cell);
                    isHit = Cell.Status.HIT;
                    if (!ship.isAlive()) {
                        battleHandler.blockAreaNearBy(board, ship);
                        isHit = Cell.Status.KILED;
                    }
                    break;
                }
            }
            if (isHit== Cell.Status.MISSED ) {
                cell.setStatus(Cell.Status.MISSED);
                cell.setSprite(Cell.Sprite.MISSED);
            }
            board.notifyDataSetChanged();
            return  isHit;
        }
        return Cell.Status.NONE;
    }


    private void setFinalStage() {
        startActivity(new Intent("team2.shattlebip.Pages.FinalPage"));
    }
    private void setFinalLoseStage() {
        startActivity(new Intent("team2.shattlebip.Pages.FinalPageLose"));
    }
    private void makeShot()
    {
        BotAsync bot= new BotAsync(hideViewBoard,hideBoard,turn,this);
        bot.execute();
    }
    private void makeOnlineShot()
    {
        Client client=new Client(hideViewBoard,hideBoard,turn,this);
        client.execute();
    }
}

