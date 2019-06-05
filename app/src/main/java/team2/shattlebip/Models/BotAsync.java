package team2.shattlebip.Models;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import team2.shattlebip.Controller.AdapterBoard;
import team2.shattlebip.GameHandlers.BattleStageHandler;
import team2.shattlebip.Models.Ships.BaseShip;
import team2.shattlebip.Models.Ships.FourDeckShip;
import team2.shattlebip.Models.Ships.OneDeckShip;
import team2.shattlebip.Models.Ships.ThreeDeckShip;
import team2.shattlebip.Models.Ships.TwoDeckShip;
import team2.shattlebip.Pages.ConnectionError;
import team2.shattlebip.Pages.FinalPage;
import team2.shattlebip.Pages.FinalPageLose;
import team2.shattlebip.R;

public class BotAsync extends AsyncTask<Void, AdapterBoard, String> {

    private ImageButton turn;
    private GridView hideViewBoard;
    private AdapterBoard hideBoard;
    private BattleStageHandler battleHandler = new BattleStageHandler();
    private GameData gameData = GameData.getInstance();
    private int onProgressUpdateNum = 0;
    private Context context;
    private Cell cell;
    private String result=null;
    private Stack<Cell> cellStack=new Stack<>();
    private Stack<Cell> hitStack = new Stack<>();
    private AdapterBoard myBoard;
    private AdapterBoard enemyBoard;
    private boolean isWin;
    private boolean isClick;
    private boolean isLose;

    public BotAsync(GridView _hideViewBoard, AdapterBoard _hideBoard,ImageButton turn, Context context) {
        hideBoard = _hideBoard;
        hideViewBoard = _hideViewBoard;
        hideViewBoard.setAdapter(hideBoard);
        enemyBoard=gameData.getEnemy().getBoard();
        myBoard=gameData.getMe().getBoard();
        this.turn = turn;
        this.context=context.getApplicationContext();
    }

    @Override
    protected void onProgressUpdate(AdapterBoard... values) {
        super.onProgressUpdate(values);
        myBoard.notifyDataSetChanged();
        hideBoard.notifyDataSetChanged();
        switch (onProgressUpdateNum) {
            case 0:
                break;
            case 1:
                turn.setImageResource(R.drawable.enemy);
                break;
            case 2:
                turn.setImageResource(R.drawable.you);
                break;
            default:
                break;
        }

    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result.equals("Win")) {
            Intent winintent = new Intent(context, FinalPage.class);
            winintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(winintent);
        }
        else
        {
            Intent loseintent = new Intent(context, FinalPageLose.class);
            loseintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(loseintent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPreExecute() {
        hideViewBoard.setOnItemClickListener((parent, view, position, id) -> {
            isLose=false;
            cell = enemyBoard.getItem(position);
            isClick=true;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected String doInBackground(Void... arg0)
    {
        isClick=false;
        while (true) {
            if (isClick == true) {
                if (shotHandler(enemyBoard, gameData.getEnemy().getShips(), cell) == Cell.Status.MISSED) {
                    onProgressUpdateNum=1;
                    botResponseShot();
                    publishProgress();
                    isLose = battleHandler.isWinCondition(gameData.getMe());
                    if (isLose)
                        return "Lose";
                }
                hideBoard.getItem(cell.getY() * 10 + cell.getX()).setStatus(cell.getStatus());
                hideBoard.getItem(cell.getY() * 10 + cell.getX()).setSprite(cell.getSprite());
                onProgressUpdateNum=2;
                for (BaseShip shipIter : gameData.getEnemy().getShips()) {
                    if (shipIter.getShipLoaction().contains(cell)) {
                        battleHandler.hitShip(cell);
                        if (!shipIter.isAlive()) {
                            battleHandler.blockAreaNearBy(hideBoard, shipIter);
                            isWin = battleHandler.isWinCondition(gameData.getEnemy());
                        }
                        break;
                    }
                }
                publishProgress();
                isClick=false;
                if (isWin)
                    return "Win";
            }
        }
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
            return  isHit;
        }
        return Cell.Status.NONE;
    }
    private void botResponseShot()
    {
        Cell.Status status=null;
        Cell cell=null;
        do {
            cell=getCellToShoot(cell,status);
            status=shotHandler(myBoard,gameData.getMe().getShips(),cell);
            try
            {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

}
