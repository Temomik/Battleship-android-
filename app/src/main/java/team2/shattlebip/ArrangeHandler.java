package team2.shattlebip;

import java.util.ArrayDeque;

import team2.shattlebip.Resources.Cell;
import team2.shattlebip.Ships.BaseShip;
import team2.shattlebip.Ships.FourDeckShip;
import team2.shattlebip.Ships.OneDeckShip;
import team2.shattlebip.Ships.ThreeDeckShip;
import team2.shattlebip.Ships.TwoDeckShip;

public class ArrangeHandler {
    private ArrayDeque<BaseShip> arrangedShips;
    public int[] countShipsLeftToArrange;
    public boolean isShipSelected;
    public ArrangeHandler()
    {
        arrangedShips =new ArrayDeque<>();
        countShipsLeftToArrange=new int[4];
        countShipsLeftToArrange[0]=4;
        countShipsLeftToArrange[1]=3;
        countShipsLeftToArrange[2]=2;
        countShipsLeftToArrange[3]=1;
        this.isShipSelected=false;

    }
    public void addShip(BaseShip ship)
    {
        arrangedShips.addLast(ship);
        isShipSelected=true;
    }
    public void deleteLastShip()
    {
        arrangedShips.removeLast();
    }
    public boolean canPlaceShip(Cell cell)
    {
        if(arrangedShips.getLast()==null||getSpecificShipCount(arrangedShips.getLast())==0)
            return false;
        if(isCollision(cell,arrangedShips.getLast()))
            return false;
        changeShipCount(arrangedShips.getLast());
        isShipSelected=false;
        return true;
    }
    private boolean isCollision(Cell cell, BaseShip ship) {

        int x = cell.getX();
        int y = cell.getY();

        return ship.getRotation() == BaseShip.Rotation.HORIZONTAL?!canHorizontallyArrange(x,y,ship):
                !canVerticallyArrange(x,y,ship);
    }

    private void changeShipCount(BaseShip ship)
    {
        if(ship instanceof OneDeckShip)
            countShipsLeftToArrange[0]--;
        if(ship instanceof TwoDeckShip)
            countShipsLeftToArrange[1]--;
        if(ship instanceof ThreeDeckShip)
            countShipsLeftToArrange[2]--;
        if(ship instanceof FourDeckShip)
            countShipsLeftToArrange[3]--;
    }

    private boolean canHorizontallyArrange(int x,int y, BaseShip ship)
    {
        int size = ship.getShipSize() + 2;

        if (x + ship.getShipSize() > 10)
            return false;
        for (BaseShip arShip : arrangedShips) {
            for (Cell cel : arShip.getShipLoaction()) {
                for (int i = 0; i < size; i++) {
                    if (x + i - 1 == cel.getX() && y == cel.getY() ||
                            x + i - 1 == cel.getX() && y - 1 == cel.getY() ||
                            x + i - 1 == cel.getX() && y + 1 == cel.getY()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    private boolean canVerticallyArrange(int x,int y, BaseShip ship)
    {
        int size = ship.getShipSize() + 2;

        if (y + ship.getShipSize() > 10)
            return false;
        for (BaseShip arShip : arrangedShips) {
            for (Cell cel : arShip.getShipLoaction()) {
                for (int i = 0; i < size; i++) {
                    if (y + i - 1 == cel.getY() && x == cel.getX() ||
                            y + i - 1 == cel.getY() && x - 1 == cel.getX() ||
                            y + i - 1 == cel.getY() && x + 1 == cel.getX()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    private int getSpecificShipCount(BaseShip ship)
    {
        int count=0;
        if(ship instanceof OneDeckShip)
            count=countShipsLeftToArrange[0];
        if(ship instanceof TwoDeckShip)
            count=countShipsLeftToArrange[1];
        if(ship instanceof ThreeDeckShip)
            count=countShipsLeftToArrange[2];
        if(ship instanceof FourDeckShip)
            count=countShipsLeftToArrange[3];
        return count;
    }

    public ArrayDeque<BaseShip> getShips(){return arrangedShips;}
}
