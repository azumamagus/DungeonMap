package externius.rdmg.helpers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import externius.rdmg.models.Treasures;

public final class Treasure {

    private static double treasureValue;
    private static int itemsRarity;
    private static int sumValue;

    private static final int[] treasureGP = {
            0, 300, 600, 900, 1200, 1600, 2000, 2600, 3400, 4500, 5800,
            7500, 9800, 13000, 17000, 22000, 28000, 36000, 47000, 61000, 80000
    };

    private Treasure() {

    }


    private static List<Treasures> getFiltered(List<Treasures> treasures) {
        List<Treasures> result = new ArrayList<>();
        if (Objects.equals(Utils.getMonsterType(), "any")) {
            for (Treasures treasure : treasures) {
                if (treasure.getRarity() <= itemsRarity && treasure.getCost() < sumValue) {
                    result.add(treasure);
                }
            }
        } else {
            for (Treasures treasure : treasures) {
                if (treasure.getRarity() <= itemsRarity && treasure.getCost() < sumValue && treasure.getTypes().contains(Utils.getMonsterType())) {
                    result.add(treasure);
                }
            }
        }
        return result;
    }

    static String getTreasure() {
        if (Math.floor(Math.random() * 100) > Utils.getPercentage()) {
            return "Treasures: Empty";
        }
        getAllCost();
        List<Treasures> filteredTreasures = getFiltered(Utils.getTreasureList());
        return "Treasures: " + calcTreasure(filteredTreasures);
    }


    private static String calcTreasure(List<Treasures> filteredTreasures) {
        StringBuilder sb = new StringBuilder();
        int currentValue = 0;
        int itemCount = getItemsCount();
        Treasures currentTreasure;
        List<Treasures> finalList = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            currentTreasure = filteredTreasures.get(Utils.getRandomInt(0, filteredTreasures.size())); // get random treasure
            if (currentValue + currentTreasure.getCost() < sumValue) { // if it's still affordable add to list
                currentValue += currentTreasure.getCost();
                finalList.add(currentTreasure);
            }
        }
        Map<Treasures, Integer> map = new HashMap<>();
        for (Treasures temp : finalList) { // get duplicated items count
            Integer count = map.get(temp);
            map.put(temp, (count == null) ? 1 : count + 1);
        }
        for (Map.Entry<Treasures, Integer> entry : map.entrySet()) { // add them to the stringbuilder
            if (entry.getValue() > 1) {
                sb.append(entry.getValue());
                sb.append("x ");
            }
            sb.append(entry.getKey().getName());
            sb.append(", ");
        }
        sb.append(sumValue - currentValue); // get the remaining value
        sb.append(" gp");
        return sb.toString();
    }

    private static int getItemsCount() {
        switch (Utils.getDungeonDifficulty()) {
            case 0:
                return Utils.getRandomInt(0, 6);
            case 1:
                return Utils.getRandomInt(2, 11);
            case 2:
                return Utils.getRandomInt(4, 16);
            case 3:
                return Utils.getRandomInt(6, 21);
            default:
                return 0;
        }
    }

    private static void getAllCost() {
        sumValue = (int) (treasureGP[Utils.getPartyLevel()] * treasureValue);
    }

    public static void setTreasureValue(double treasureValue) {
        Treasure.treasureValue = treasureValue;
    }

    public static void setItemsRarity(int itemsRarity) {
        Treasure.itemsRarity = itemsRarity;
    }

}
