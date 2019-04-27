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
        int size=ship.getShipSize();
        Cell cell=ship.getShipLoaction().get(0);
        int matrixIndex = cell.getX()+10*cell.getY() - 1;
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
                cell.setSprite(Cell.Sprite.HORIZONTAL_FRONT_HIT);
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