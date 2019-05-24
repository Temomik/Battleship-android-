package team2.shattlebip.Models;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

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
import team2.shattlebip.R;

public class Client extends AsyncTask<Void, AdapterBoard, String> {

    private String dstAddress = "159.89.99.20";
    private int dstPort = 5057;
    private String response = "";
    private String receive = "";
    private Button turn;
    private String tosend = "";
    private Socket socket = null;
    private GridView hideViewBoard;
    List<BaseShip> HitShipsCells = new ArrayList();
    private AdapterBoard hideBoard;
    private DataOutputStream dos;
    private DataInputStream dis;
    private BattleStageHandler battleHandler = new BattleStageHandler();
    private GameData gameData = GameData.getInstance();

    public Client(GridView _hideViewBoard, AdapterBoard _hideBoard,Button turn) {
        hideBoard = _hideBoard;
        hideViewBoard = _hideViewBoard;
        hideViewBoard.setAdapter(hideBoard);
        this.turn = turn;
    }

    @Override
    protected void onProgressUpdate(AdapterBoard... values) {
        super.onProgressUpdate(values);
        values[0].notifyDataSetChanged();
        turn.setBackgroundColor(10);
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
                if (isNumber(receive)) {
                    // System.out.println("Answer was it Hit or Miss");
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
                        default:
                            break;
                    }
                    dos.writeUTF(tosend);
                    dos.flush();
                    publishProgress(gameData.getMe().getBoard());
                }
                if (receive.equals("Youbegin") || tosend.equals("Miss")) {
                    tosend = "";
                    while (!response.equals("Miss")) {
                       /* hideViewBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {*/
                                /*Cell cell = hideBoard.getItem(position);
                                tosend=String.valueOf(cell.getY()*10+cell.getX());*/
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
                        } catch (Exception e) {
                        }
                        publishProgress(hideBoard);/*
                            }
                       });*/
                        tosend = "";
                    }
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
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    static boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++)
            if (Character.isDigit(s.charAt(i))
                    == false)
                return false;

        return true;
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
            if (isHit == Cell.Status.MISSED) {
                cell.setStatus(Cell.Status.MISSED);
                cell.setSprite(Cell.Sprite.MISSED);
            }
            //board.notifyDataSetChanged();
            return isHit;
        }
        return Cell.Status.VACANT;
    }

}
