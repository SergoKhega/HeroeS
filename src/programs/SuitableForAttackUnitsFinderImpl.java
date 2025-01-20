package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        for (List<Unit> row : unitsByRow) {
            Optional<Unit> candidateUnit = findEdgeUnit(row, isLeftArmyTarget);
            candidateUnit.ifPresent(suitableUnits::add);
        }

        return suitableUnits;
    }

    private Optional<Unit> findEdgeUnit(List<Unit> row, boolean isLeftArmyTarget) {
        Unit edgeUnit = null;
        int extremeCoordinate = isLeftArmyTarget ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Unit unit : row) {
            if (unit != null && unit.isAlive() && !isUnitBlocked(unit, row, isLeftArmyTarget)) {
                int xCoordinate = unit.getxCoordinate();
                if (isLeftArmyTarget ? xCoordinate > extremeCoordinate : xCoordinate < extremeCoordinate) {
                    extremeCoordinate = xCoordinate;
                    edgeUnit = unit;
                }
            }
        }

        return Optional.ofNullable(edgeUnit);
    }

    private boolean isUnitBlocked(Unit unit, List<Unit> row, boolean isLeftArmyTarget) {
        int index = row.indexOf(unit);
        int step = isLeftArmyTarget ? 1 : -1;

        for (int i = index + step; i >= 0 && i < row.size(); i += step) {
            Unit blockingUnit = row.get(i);
            if (blockingUnit != null && blockingUnit.isAlive()) {
                return true;
            }
        }

        return false;
    }
}
