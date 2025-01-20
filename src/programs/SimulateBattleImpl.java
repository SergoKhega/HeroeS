package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {

    @Override
    public void simulate(Army playerArmy, Army opponent) throws InterruptedException {

        List<Unit> allUnits = new ArrayList<>(playerArmy.getUnits());
        allUnits.addAll(opponent.getUnits());

        while (hasAliveUnits(playerArmy) && hasAliveUnits(opponent)) {
            allUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            for (Unit unit : allUnits) {
                if (unit.isAlive()) {
                    Unit target = selectTarget(unit, playerArmy, opponent);
                    if (target != null) {
                        performAttack(unit);
                    }
                }
            }
        }
    }

    private boolean hasAliveUnits(Army army) {
        for (Unit unit : army.getUnits()) {
            if (unit.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private Unit selectTarget(Unit attacker, Army playerArmy, Army opponent) {
        Army targetArmy = playerArmy.getUnits().contains(attacker) ? opponent : playerArmy;
        for (Unit unit : targetArmy.getUnits()) {
            if (unit.isAlive()) {
                return unit;
            }
        }
        return null;
    }

    private void performAttack(Unit attacker) throws InterruptedException {
        if (attacker.getProgram() != null) {
            attacker.getProgram().attack();
        }
    }
}
