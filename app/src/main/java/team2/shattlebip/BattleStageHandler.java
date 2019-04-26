package team2.shattlebip;

import team2.shattlebip.Controller.AdapterBoard;
import team2.shattlebip.Models.Cell;
import team2.shattlebip.Ships.BaseShip;

public class BattleStageHandler {
    private boolean win;
    public BattleStageHandler()
    {
        win=false;
    }

    public void blockAreaNearBy(AdapterBoard board, BaseShip ship)
    {
        int size=ship.getShipSize()+1;
        Cell cell=ship.getShipLoaction().get(0);
        int X=cell.getX();
        int Y=cell.getY();
        board.getItem((X+10*Y)-1).setStatus(Cell.Status.MISSED);
        board.getItem((X+10*Y)+ship.getShipSize()).setStatus(Cell.Status.MISSED);
            for (Cell cel : ship.getShipLoaction()) {
                int x=cel.getX();
                int y=cel.getY();
                for(int i=0;i<size;++i) {
                    board.getItem((x+10*y)-1+i+10).setStatus(Cell.Status.MISSED);
                    board.getItem((x+10*y)-1+i-10).setStatus(Cell.Status.MISSED);
                }
            }
    }
}
