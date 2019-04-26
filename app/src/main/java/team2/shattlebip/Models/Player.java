package team2.shattlebip;

import android.widget.GridView;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import team2.shattlebip.Resources.Cell;
import team2.shattlebip.Ships.BaseShip;
import team2.shattlebip.View.AdapterBoard;

public class Player {
    private ArrayDeque<BaseShip> ships = new ArrayDeque<>();
    private ArrangeHandler arrangeHandler;
    private AdapterBoard board;
    /**
     * fleet:
     * - one_deck_ship: occupies 1 cells, 4 ships
     * - two_deck_ship: 2, three ships
     * - three_deck_ship: 3, two ships
     *  - four_deck_ship: 4 , one ship
     *
     */
    public Player(AdapterBoard _board) {
        board=_board;
        arrangeHandler=new ArrangeHandler();
    }

    public Deque<BaseShip> getShips() {
        return ships;
    }


    public void arrangeShips() {
        arrangeHandler.arrangeShipsRandomly(board);
        finishArranging();
    }
    public void finishArranging()
    {
        ships=arrangeHandler.getShips();
    }
    public ArrangeHandler getArrangeHandler()
    {
         return arrangeHandler;
    }
    public AdapterBoard getBoard()
    {
        return board;
    }

}
