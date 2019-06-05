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

public class Client extends AsyncTask<Void, AdapterBoard, String> {

    private String dstAddress = "159.89.99.20";
    private int dstPort = 5057;
    private String response = "";
    private String receive = "";
    private ImageButton turn;
    private String tosend = "";
    private Socket socket = null;
    private GridView hideViewBoard;
    List<BaseShip> HitShipsCells = new ArrayList();
    private AdapterBoard hideBoard;
    private DataOutputStream dos;
    private DataInputStream dis;
    private BattleStageHandler battleHandler = new BattleStageHandler();
    private GameData gameData = GameData.getInstance();
    private int onProgressUpdateNum = 0;
    private Context context;

    public Client(GridView _hideViewBoard, AdapterBoard _hideBoard,ImageButton turn, Context context) {
        hideBoard = _hideBoard;
        hideViewBoard = _hideViewBoard;
        hideViewBoard.setAdapter(hideBoard);
        this.turn = turn;
        this.context=context.getApplicationContext();
    }

    @Override
    protected void onProgressUpdate(AdapterBoard... values) {
        super.onProgressUpdate(values);
        gameData.getMe().getBoard().notifyDataSetChanged();
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

    @Override
    protected void onPreExecute() {
        hideViewBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cell cell = hideBoard.getItem(position);
                tosend = String.valueOf(cell.getY() * 10 + cell.getX());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected String doInBackground(Void... arg0)
    {

        try {
            /*
             * notice: inputStream.read() will block if no data return
             */
            socket = new Socket(dstAddress, dstPort);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            receive = dis.readUTF();
            Cell.Status status;

            while (true) {
                receive = dis.readUTF();
                if(receive.equals("ERROR"))
                    return "Error";
                if (isNumber(receive)) {
                    Cell cel = gameData.getMe().getBoard().getItem(Integer.parseInt(receive));
                    status=shotHandler(gameData.getMe().getBoard(), gameData.getMe().getShips(), cel);
                    switch (status)
                    {
                        case HIT:
                            tosend = "Hit";
                            break;
                        case MISSED:
                            tosend = "Miss";
                            break;
                        case VACANT:
                           tosend = "Vacant";
                        case KILED:
                            tosend = "Kill";
                            break;
                        case WIN:
                            tosend="Win";
                            break;
                        default:
                            break;
                    }
                    dos.writeUTF(tosend);
                    dos.flush();
                    onProgressUpdateNum = 0;
                    publishProgress();
                    if(tosend.equals("Win"))
                        return "Lose";
                }
                if (receive.equals("Youbegin") || tosend.equals("Miss")) {
                    onProgressUpdateNum = 2;
                    publishProgress();
                    tosend = "";
                    while (!response.equals("Miss")) {
                        try {
                            while (tosend.equals("")) {
                            }
                            dos.writeUTF(tosend);
                            response = dis.readUTF();
                            //System.out.println(response);
                            if (response.equals("Miss")) {
                                hideBoard.getItem(Integer.parseInt(tosend)).setStatus(Cell.Status.MISSED);
                                hideBoard.getItem(Integer.parseInt(tosend)).setSprite(Cell.Sprite.MISSED);
                            }
                            else if(response.equals("Vacant")){}
                            else if(response.equals("Hit")) {
                                hideBoard.getItem(Integer.parseInt(tosend)).setStatus(Cell.Status.HIT);
                                hideBoard.getItem(Integer.parseInt(tosend)).setSprite(Cell.Sprite.HIT);
                                BaseShip hitShip = new OneDeckShip();
                                hitShip.addCell( hideBoard.getItem(Integer.parseInt(tosend)));
                                HitShipsCells.add(hitShip);
                            }
                            else if(response.equals("Kill")) {
                                if(hideBoard.getItem(Integer.parseInt(tosend)).getStatus() == Cell.Status.VACANT) {
                                    hideBoard.getItem(Integer.parseInt(tosend)).setStatus(Cell.Status.HIT);
                                    hideBoard.getItem(Integer.parseInt(tosend)).setSprite(Cell.Sprite.HIT);
                                    BaseShip hitShip = new OneDeckShip();
                                    hitShip.addCell( hideBoard.getItem(Integer.parseInt(tosend)));
                                    HitShipsCells.add(hitShip);
                                    for(int i = 0; i < HitShipsCells.size();i++) {
                                                battleHandler.blockAreaNearBy(hideBoard, HitShipsCells.get(i));
                                    }
                                }
                            }
                            else if(response.equals("ERROR"))
                                return "Error";
                            else if(response.equals("Win"))
                                return "Win";
                        } catch (Exception e) {
                            Intent winintent = new Intent(context, ConnectionError.class);
                            winintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(winintent);
                        }
                        onProgressUpdateNum = 0;
                        publishProgress(hideBoard);
                        /*
                            }
                       });*/
                        tosend = "";

                        onProgressUpdateNum = 0;
                        publishProgress();
                    }
                        onProgressUpdateNum = 1;
                        publishProgress();
                    response = "";
                    tosend = "";
                }
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                    Intent winintent = new Intent(context, ConnectionError.class);
                    winintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(winintent);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return response;
    }
    public boolean isWinCondition(Deque<BaseShip> ships)
    {
        boolean isWin=true;
        for(BaseShip ship:ships)
        {
            if(ship.isAlive())
            {
                isWin=false;
                break;
            }
        }
        return isWin;
    }
    static boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++)
            if (Character.isDigit(s.charAt(i))
                    == false)
                return false;

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Cell.Status shotHandler(AdapterBoard board, Deque<BaseShip> ships, Cell cell) {
        if (cell.isReadyToInteraction()) {
            Cell.Status isHit = Cell.Status.MISSED;
            for(BaseShip ship : ships)
            {
                if (ship.getShipLoaction().contains(cell)) {
                    battleHandler.hitShip(cell);
                    isHit = Cell.Status.HIT;
                    if (!ship.isAlive()) {
                        battleHandler.blockAreaNearBy(board, ship);
                        isHit = Cell.Status.KILED;
                        if (isWinCondition(ships))
                            isHit = Cell.Status.WIN;
                    }
                }
            }
            if (isHit == Cell.Status.MISSED) {
                cell.setStatus(Cell.Status.MISSED);
                cell.setSprite(Cell.Sprite.MISSED);
            }
            return isHit;
        }
        return Cell.Status.VACANT;
    }

}
