package team2.shattlebip;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Random;

import team2.shattlebip.Models.Cell;
import team2.shattlebip.R;
import team2.shattlebip.Models.Ships.BaseShip;
import team2.shattlebip.Models.Ships.FourDeckShip;
import team2.shattlebip.Models.Ships.OneDeckShip;
import team2.shattlebip.Models.Ships.ThreeDeckShip;
import team2.shattlebip.Models.Ships.TwoDeckShip;
import team2.shattlebip.Controller.AdapterBoard;
import team2.shattlebip.databinding.MainActivityBinding;

public class ArrangeHandler {
    private ArrayDeque<BaseShip> arrangedShips;
    private int[] countShipsLeftToArrange;
    public int rotateVertical;
    public boolean isShipSelected;
    public MainActivityBinding binding;
    public ArrangeHandler()
    {
        arrangedShips =new ArrayDeque<>();
        countShipsLeftToArrange = new int[4];
        countShipsLeftToArrange[0] = 4;
        countShipsLeftToArrange[1] = 3;
        countShipsLeftToArrange[2] = 2;
        countShipsLeftToArrange[3] = 1;
        this.isShipSelected=false;

    }
    public String[] getCountShipsLeftToArrange() {
        String[] tmp = new String[4];
        for(int i = 0;i < 4;i++){
            tmp[i] = Integer.toHexString(countShipsLeftToArrange[i]);
        }
        return  tmp;
    }
    public void addShip(BaseShip ship)
    {
        if(rotateVertical == 1)
            ship.setRotation(BaseShip.Rotation.VERTICAL);
        else
            ship.setRotation(BaseShip.Rotation.HORIZONTAL);
        arrangedShips.addLast(ship);
        isShipSelected=true;
    }
    public boolean tryToPlaceShip(Cell cell, AdapterBoard board, BaseShip ship)
    {
        if(ship==null||getSpecificShipCount(ship)==0)
            return false;
        if(rotateVertical == 1)
            ship.setRotation(BaseShip.Rotation.VERTICAL);
        else
            ship.setRotation(BaseShip.Rotation.HORIZONTAL);
        if(isCollision(cell,ship))
            return false;
        if(ship.getRotation()== BaseShip.Rotation.HORIZONTAL) {
            placeShipHorizontally(ship,cell,board);
        }
        else
            placeShipVertically(ship,cell,board);
        decreaseShipCount(ship);
        board.update(cell,ship);
        isShipSelected=false;
        return true;
    }

    private void placeShipHorizontally(BaseShip ship, Cell cell, AdapterBoard board)
    {
        for (int i = 0; i < ship.getShipSize(); i++)
            ship.addCell(board.getItem(board.getPosition(cell) + i));
    }
    private void placeShipVertically(BaseShip ship, Cell cell, AdapterBoard board)
    {
        for (int i = 0; i < ship.getShipSize(); i++)
            ship.addCell(board.getItem(board.getPosition(cell) + (i*10)));
    }
    private boolean isCollision(Cell cell, BaseShip ship) {
        int x = cell.getX();
        int y = cell.getY();
        return ship.getRotation() == BaseShip.Rotation.HORIZONTAL?!canHorizontallyArrange(x,y,ship):
                !canVerticallyArrange(x,y,ship);
    }

    private void decreaseShipCount(BaseShip ship)
    {
        if(ship instanceof OneDeckShip)
            countShipsLeftToArrange[0]--;
        if(ship instanceof TwoDeckShip)
            countShipsLeftToArrange[1]--;
        if(ship instanceof ThreeDeckShip)
            countShipsLeftToArrange[2]--;
        if(ship instanceof FourDeckShip)
            countShipsLeftToArrange[3]--;
        if(binding!=null)
        binding.invalidateAll();
    }
    private void increaseShipCount(BaseShip ship)
    {
        if(ship instanceof OneDeckShip)
            countShipsLeftToArrange[0]++;
        if(ship instanceof TwoDeckShip)
            countShipsLeftToArrange[1]++;
        if(ship instanceof ThreeDeckShip)
            countShipsLeftToArrange[2]++;
        if(ship instanceof FourDeckShip)
            countShipsLeftToArrange[3]++;
        if(binding!=null)
        binding.invalidateAll();
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

    public int getShipCount() {return arrangedShips.size();}

    public BaseShip deleteShipByCell(Cell cell) {
        for (BaseShip ship:arrangedShips) {
            if(ship.getShipLoaction().contains(cell)) {
               increaseShipCount(ship);
               arrangedShips.remove(ship);
                return ship;
            }
        }
        return null;
    }

    private BaseShip getShipByLength(int i)
    {
        BaseShip ship;
        switch (i) {
            case 0:
                ship=new OneDeckShip();
                break;
            case 1:
                ship=new TwoDeckShip();
                break;
            case 2:
                ship= new ThreeDeckShip();
                break;
            case 3:
                ship =new FourDeckShip();
                break;
            default:
                ship=null;
                break;
        }
        return ship;
    }


    public void updateRotateImage(TextView rotateImage) {
        if(rotateVertical == 1)
            rotateImage.setBackgroundResource(R.drawable.vertical);
        else
            rotateImage.setBackgroundResource(R.drawable.horizontal);
    }

    public void arrangeShipsRandomly(AdapterBoard board) {
        for(int ii=3;ii>=0;ii--){
            while(countShipsLeftToArrange[ii]>0)
            {
                Random rand=new Random();
                int i,j,o;
                BaseShip.Rotation rotation;
                BaseShip ship;
                do {
                    if(isShipSelected)
                        arrangedShips.removeLast();
                   i=rand.nextInt(10);
                   j=rand.nextInt(10);
                   o = rand.nextInt(2);
                    arrangedShips.addLast(getShipByLength(ii));
                    isShipSelected=true;
                    if (o != 0) {
                        arrangedShips.getLast().rotate();
                        rotateVertical = o;
                    }

                }while(!tryToPlaceShip(board.getItem(i+10*j),board,arrangedShips.getLast()));
            }
        }
    }
}
