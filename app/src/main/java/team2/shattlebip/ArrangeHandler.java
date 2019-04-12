package team2.shattlebip;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Random;
import android.support.v7.app.AppCompatActivity;

import team2.shattlebip.Pages.MainActivity;
import team2.shattlebip.Resources.Cell;
import team2.shattlebip.Ships.BaseShip;
import team2.shattlebip.Ships.FourDeckShip;
import team2.shattlebip.Ships.OneDeckShip;
import team2.shattlebip.Ships.ThreeDeckShip;
import team2.shattlebip.Ships.TwoDeckShip;
import team2.shattlebip.View.AdapterBoard;
import team2.shattlebip.databinding.MainActivityBinding;

public class ArrangeHandler {
    private ArrayDeque<BaseShip> arrangedShips;
    public int[] countShipsLeftToArrange;
//    public ObservableArrayList<Integer> countShipsLeftToArrange;
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

        if(ship instanceof OneDeckShip)
            ship = new OneDeckShip();
        if(ship instanceof TwoDeckShip)
            ship = new ThreeDeckShip();
        if(ship instanceof ThreeDeckShip)
            ship = new ThreeDeckShip();
        if(ship instanceof FourDeckShip)
            ship = new FourDeckShip();
        isShipSelected=true;
    }
    public void deleteLastShip()
    {
        arrangedShips.removeLast();
    }
    public boolean tryToPlaceShip(Cell cell, AdapterBoard board)
    {
//        textViewMessage.setText("21");
        if(arrangedShips.getLast()==null||getSpecificShipCount(arrangedShips.getLast())==0)
            return false;
        if(isCollision(cell,arrangedShips.getLast()))
            return false;
        if(arrangedShips.getLast().getRotation()== BaseShip.Rotation.HORIZONTAL) {
            for (int i = 0; i < arrangedShips.getLast().getShipSize(); i++)
                arrangedShips.getLast().addCell(board.getItem(board.getPosition(cell) + i));
        }
        else
            for (int i = 0; i < arrangedShips.getLast().getShipSize(); i++)
                arrangedShips.getLast().addCell(board.getItem(board.getPosition(cell) + (i*10)));
        changeShipCount(arrangedShips.getLast());
        board.update(cell,this.getShips().getLast());
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
                    if (o != 0)
                        arrangedShips.getLast().rotate();
                }while(!tryToPlaceShip(board.getItem(i+10*j),board));
            }
        }
    }
}
