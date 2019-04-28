package team2.shattlebip.GameHandlers;

import team2.shattlebip.Controller.AdapterBoard;
import team2.shattlebip.Models.Cell;
import team2.shattlebip.Models.Player;
import team2.shattlebip.Models.Ships.BaseShip;

public class BattleStageHandler {
    private boolean win;
    public BattleStageHandler()
    {
        win=false;
    }

    public void blockAreaNearBy(AdapterBoard board, BaseShip ship)
    {
        int size=ship.getShipSize();
        Cell cell=ship.getShipLoaction().get(0);
        int matrixIndex = cell.getX()+10*cell.getY();
        if(ship.getRotation() == BaseShip.Rotation.HORIZONTAL)
            horizontalBlock(size,matrixIndex,cell,board);
        else
            verticalBlock(size,matrixIndex,cell,board);
    }
    public boolean isWinCondition(Player player) {
        boolean isWin=true;
        for (BaseShip ship:player.getShips()) {
            if(ship.isAlive()) {
                isWin=false;
                break;
            }
        }
        return isWin;
    }

    private void horizontalBlock(int size,int matrixIndex,Cell cell,AdapterBoard board) {
        matrixIndex--;
        for (int i = 0; i <= size + 1; i++) {
            int tmpMatrixIndex = matrixIndex - 10;
            if((cell.getX() != 0 && i == 0) || (cell.getX() + i <= 10 && i != 0) ) {
                for (int j = 0; j < 3; j++) {
                    if (tmpMatrixIndex < 100 && tmpMatrixIndex >= 0 && board.getItem(tmpMatrixIndex).getStatus() == Cell.Status.VACANT) {
                        board.getItem(tmpMatrixIndex).setStatus(Cell.Status.MISSED);
                        board.getItem(tmpMatrixIndex).setSprite(Cell.Sprite.MISSED);
                    }
                    tmpMatrixIndex += 10;
                }
            }
            matrixIndex++;
        }
    }

    private void verticalBlock(int size,int matrixIndex,Cell cell,AdapterBoard board) {
        matrixIndex-=10;
        int width = 3;
        for (int i = 0; i <= size + 1; i++) {
            int tmpMatrixIndex = matrixIndex;
            if(cell.getX() != 0) {
                tmpMatrixIndex--;
                width = 3;
            } else {
                width = 2;
            }
            if(cell.getX()==9)
            width = 2;
                for (int j = 0; j < width; j++) {
                if (tmpMatrixIndex < 100 && tmpMatrixIndex >= 0 && board.getItem(tmpMatrixIndex).getStatus() == Cell.Status.VACANT) {
                    board.getItem(tmpMatrixIndex).setStatus(Cell.Status.MISSED);
                    board.getItem(tmpMatrixIndex).setSprite(Cell.Sprite.MISSED);
                }
                    tmpMatrixIndex += 1;
            }
            matrixIndex+=10;
        }
    }

    public void hitShip(Cell cell) {
        cell.setStatus(Cell.Status.HIT);
        switch (cell.getSprite()) {
            case HORIZONTAL_FRONT:
                cell.setSprite(Cell.Sprite.HORIZONTAL_FRONT_HIT);
                break;
            case HORIZONTAL_BACK:
                cell.setSprite(Cell.Sprite.HORIZONTAL_BACK_HIT);
                break;
            case HORIZONTAL_BODY:
                cell.setSprite(Cell.Sprite.HORIZONTAL_BODY_HIT);
                break;
            case HORIZONTAL_SINGLE:
                cell.setSprite(Cell.Sprite.HORIZONTAL_SINGLE_HIT);
                break;
            case VERTICAL_FRONT:
                cell.setSprite(Cell.Sprite.VERTICAL_FRONT_HIT);
                break;
            case VERTICAL_BODY:
                cell.setSprite(Cell.Sprite.VERTICAL_BODY_HIT);
                break;
            case VERTICAL_BACK:
                cell.setSprite(Cell.Sprite.VERTICAL_BACK_HIT);
                break;
            case VERTICAL_SINGLE:
                cell.setSprite(Cell.Sprite.VERTICAL_SINGLE_HIT);
                break;
        }
    }
}