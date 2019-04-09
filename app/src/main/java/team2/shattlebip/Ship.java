package team2.shattlebip;

import java.util.ArrayList;
import java.util.List;

import team2.shattlebip.Resources.Cell;

public class Ship {
    private int numCellsAdded = 0;
    private int playerNum;
    private ShipType shipType;
    private int numCells;
    private List<Cell> cells;


    /**
     * creates ship according to ship type
     */
    public Ship(int playerNum, ShipType shipType) {
        this.playerNum = playerNum;
        this.shipType = shipType;
        setShipTypeDependentFields();
        cells = new ArrayList<>(numCells);
    }

    private void setShipTypeDependentFields() {
        if (shipType == ShipType.ONE_DECK_SHIP) {
            numCells = 1;
        } else if (shipType == ShipType.TWO_DECK_SHIP) {
            numCells = 2;
        }
        else if (shipType == ShipType.THREE_DECK_SHIP) {
            numCells = 3;
        }else {
            numCells = 4;
        }
    }
    /**
     * how many cells left to arrange this ship
     */
    public int getNumCellsToAdd() {
        return numCells - numCellsAdded;
    }

    /**
     * not done arranging this ship yet
     */
    public boolean canAddCells() {
        return getNumCellsToAdd() > 0;
    }

    /**
     * add 1 cell to ship during arrangement
     */
    public void addCell(Cell cell) {
        cell.setStatus(Cell.Status.OCCUPIED);
        cells.add(cell);
        numCellsAdded++;
    }

    /**
     * this ship now attacks 1 cell
     */
    public void attackCell(Cell cell) {
        if (cell.getStatus() == Cell.Status.VACANT)
            cell.setStatus(Cell.Status.MISSED);
        if (cell.getStatus() == Cell.Status.OCCUPIED)
            cell.setStatus(Cell.Status.HIT);
    }

    /**
     * @return some cell hasn't been hit
     */
    public boolean isAlive() {
        for (Cell cell : cells)
            if (cell.getStatus() != Cell.Status.HIT)
                return true;
        return false;
    }

    public enum ShipType {
        ONE_DECK_SHIP, TWO_DECK_SHIP, THREE_DECK_SHIP,FOUR_DECK_SHIP
    }
}
