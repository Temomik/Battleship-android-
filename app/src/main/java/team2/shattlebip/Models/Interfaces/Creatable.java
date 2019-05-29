package team2.shattlebip.Models.Interfaces;

import team2.shattlebip.Controller.AdapterBoard;
import team2.shattlebip.Models.Cell;

public interface Creatable {
    void createBattleField(int numCells, AdapterBoard board);
}
