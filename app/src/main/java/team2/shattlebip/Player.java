package team2.shattlebip;

import java.util.ArrayList;
import java.util.List;

import team2.shattlebip.Resources.Cell;

public class Player {
    private int numShipsArranged = 0;
    private int numShips = 100;
    private int playerNum;
    private List<Ship> ships = new ArrayList<>(numShips);

    /**
     * fleet:
     * - one_deck_ship: occupies 1 cells, 4 ships
     * - two_deck_ship: 2, three ships
     * - three_deck_ship: 3, two ships
     *  - four_deck_ship: 4 , one ship
     *
     */
    public Player(int playerNum) {
        this.playerNum = playerNum;

        Ship ship = new Ship(playerNum, Ship.ShipType.ONE_DECK_SHIP);
        ships.add(ship);
        ship = new Ship(playerNum, Ship.ShipType.TWO_DECK_SHIP);
        ships.add(ship);
        ship = new Ship(playerNum, Ship.ShipType.THREE_DECK_SHIP);
        ships.add(ship);
        ship = new Ship(playerNum, Ship.ShipType.THREE_DECK_SHIP);
        ships.add(ship);
        ship = new Ship(playerNum, Ship.ShipType.FOUR_DECK_SHIP);
        ships.add(ship);
    }

    public int getNumShipsArranged() {
        return numShipsArranged;
    }

    public int getNumShips() {
        return numShips;
    }

    public List<Ship> getShips() {
        return ships;
    }

    private int getNumShipsToArrange() {
        return numShips - numShipsArranged;
    }


    /**
     * add cell to the ship being arranged
     */
    public void addCell(Cell cell) {
        Ship ship = ships.get(numShipsArranged);

        ship.addCell(cell);
        if (!ship.canAddCells())
            numShipsArranged++;
    }


    /**
     * @return how many ships in fleet are still alive?
     */
    public int getNumShipsAlive() {
        int numShipsAlive = 0;
        for (Ship ship : ships)
            if (ship.isAlive())
                numShipsAlive++;
        return numShipsAlive;
    }

    /**
     * @return >= 1 ship still alive
     */
    public boolean isAlive() {
        return getNumShipsAlive() > 0;
    }
}
