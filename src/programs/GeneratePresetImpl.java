package programs;
import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    private static final int MIN_LIMIT = 11;
    private static final Random random = new Random();

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        Army army = new Army();
        List<Unit> units = new ArrayList<>();
        int points = 0;

        unitList.sort(Comparator.comparingDouble(unit -> -calculateEfficiency(unit)));

        for (Unit unit : unitList) {
            int count = (maxPoints - points) / unit.getCost();
            count = Math.min(count, MIN_LIMIT);
            units.addAll(createUnits(unit, count));
            points += count * unit.getCost();

            if (points >= maxPoints) break;
        }

        setCoordinates(units);

        army.setUnits(units);
        army.setPoints(points);
        return army;
    }

    private double calculateEfficiency(Unit unit) {
        return (double) (unit.getBaseAttack() + unit.getHealth()) / unit.getCost();
    }

    private List<Unit> createUnits(Unit unit, int count) {
        List<Unit> units = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Unit unitToAdd = getUnit(unit);
            unitToAdd.setName(unit.getUnitType() + " " + i); 
            units.add(unitToAdd);
        }
        return units;
    }

    private static Unit getUnit(Unit unit) {
        return new Unit(
                unit.getName(),
                unit.getUnitType(),
                unit.getHealth(),
                unit.getBaseAttack(),
                unit.getCost(),
                unit.getAttackType(),
                unit.getAttackBonuses(),
                unit.getDefenceBonuses(),
                -1, -1
        );
    }

    private void setCoordinates(List<Unit> units) {
        Set<String> coordinates = new HashSet<>();

        for (Unit unit : units) {
            int x = random.nextInt(3);;
            int y = random.nextInt(21);;

            while (!coordinates.add(x + "," + y)) { 
                unit.setxCoordinate(x);
                unit.setyCoordinate(y);
            }

        }
    }
}
