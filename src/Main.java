import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        var item1 = new Item(10, 5);
        var item2 = new Item(40, 4);
        var item3 = new Item(30, 6);
        var item4 = new Item(50, 3);
        var items = Arrays.asList(item1, item2, item3, item4);

        var W = 10;

        //--------Dynamic programming---------//
        System.out.println("\n##################Dynamic programming##################\n");

        System.out.println("\n-------------Table-------------");
        var arr = knapsackMaxValue(W, items); // result table
        for (int[] i: arr) {
            for (int j: i) {
                System.out.print(j + ", ");
            }
            System.out.println();
        }

        System.out.println("\n-------------Result-------------");
        var DPResult = getResult(arr); // Max value that can contain knapsack
        System.out.println(DPResult);

        System.out.println("\n-------------List of involved items (Max value)-------------");
        var DPInvolvedItemsValue = involvedItemsValue(W, items); // List of involved items (Max value)
        DPInvolvedItemsValue.forEach(System.out::println);

        System.out.println("\n-------------List of involved items (Max items)-------------");
        var DPInvolvedItems = involvedItems(W, items); // List of involved items (Max items)
        DPInvolvedItems.forEach(System.out::println);

        System.out.println("\n-------------Max number of items in the knapsack-------------");
        System.out.println(DPInvolvedItems.size());// Max number of items in the knapsack

        System.out.println("\n-------------Intersection of the results-------------");
        DPInvolvedItemsValue.stream()
                .filter(DPInvolvedItems::contains)
                .forEach(System.out::println);// Intersection of the results

        //--------Greedy algorithm---------//
        System.out.println("\n\n##################Greedy algorithm##################\n");

        System.out.println("\n-------------List of involved items (Max value)-------------");
        var sortedItemsByRatio = items.stream()
                .sorted(Comparator.reverseOrder())
                .toList();
        var GAInvolvedItemsValue = GAMaxyList(W, sortedItemsByRatio); // List of involved items (Max value)
        GAInvolvedItemsValue.forEach(System.out::println);

        System.out.println("\n-------------Result-------------");
        var GAMaxValue = GAMaxyResult(GAInvolvedItemsValue); // Max value that can contains knapsack
        System.out.println(GAMaxValue);

        System.out.println("\n-------------List of involved items (Max items)-------------");
        var sortedItemsByWeight = items.stream()
                .sorted(Comparator.comparingInt(Item::getWeight))
                .toList();
        var GAInvolvedItems = GAMaxyList(W, sortedItemsByWeight); // List of involved items (Max items)
        GAInvolvedItems.forEach(System.out::println);

        System.out.println("\n-------------Max number of items in the knapsack-------------");
        var GAMaxItems = GAInvolvedItems.size(); // Max items that can contains knapsack
        System.out.println(GAMaxItems);

        System.out.println("\n-------------Intersection of the results-------------");
        GAInvolvedItemsValue.stream()
                .filter(GAInvolvedItems::contains)
                .forEach(System.out::println);// Intersection of the result
    }

    //--------Dynamic programming---------//

    // Return table of result
    static int[][] knapsackMaxValue(int W, List<Item> items) {
        var size = items.size();
        var table = new int[size + 1][W + 1]; //Items are in rows and weight at in columns +1 on each side

        // Populate base cases
        for (var i = 0; i <= W; i++) //if the knapsack's capacity is 0
            table[0][i] = 0;
        for (var i = 0; i <= size; i++) //if there are no items
            table[i][0] = 0;

        // Main logic
        for (var item = 1; item <= size; item++) {
            // fill the values row by row
            for (var weight = 1; weight <= W; weight++) {
                var notTakingItem = table[item - 1][weight];
                var takingItem = 0;

                if (weight >= items.get(item - 1).getWeight())
                    takingItem = items.get(item - 1).getValue() + table[item - 1][weight - items.get(item - 1).getWeight()]; // if take the item

                table[item][weight] = Math.max(notTakingItem, takingItem); // Pick the larger of the two
            }
        }
        return table;
    }

    // Return max value of table
    static int getResult(int[][] arr) {
        return arr[arr.length - 1][arr[arr.length - 1].length - 1];
    }

    // Return list of involved items (Max value)
    static List<Item> involvedItemsValue(int W, List<Item> items) {
        var table = knapsackMaxValue(W, items);
        var size = items.size();
        var result = new ArrayList<Item>();

        while (size > 0 && W > 0) {
            if (table[size][W] != table[size - 1][W]) {
                result.add(items.get(size - 1));
                W = W - items.get(size - 1).getWeight();
                size--;
            } else
                size--;
        }
        return result;
    }

    // Return list of involved items (Max items)
    static List<Item> involvedItems(int W, List<Item> items) {
        var table = knapsackMaxValue(W, items);
        var size = items.size();
        var result = new ArrayList<Item>();

        var CurWeight = 0;
        var weight = W;

        int curValue = 0;
        while (weight >= CurWeight) {
            for (int i = 1; i < size; i++) {
                for (int j = 1; j < W; j++) {
                    CurWeight = items.get(i - 1).getWeight();
                    if (table[i][j] == (curValue + items.get(i - 1).getValue())) {
                        result.add(items.get(i - 1));
                        weight -= CurWeight;
                        curValue += items.get(i - 1).getValue();
                        i++;
                    }
                }
            }
        }
        return result;
    }

    //--------Greedy algorithm---------//

    // Return list of involved items
    static List<Item> GAMaxyList(int W, List<Item> sortedItems) {
        var result = new ArrayList<Item>();

        var size = sortedItems.size();
        var i = 0;
        var weight = 0;

        while (i < size && weight <= W) {
            var curWeight = sortedItems.get(i).getWeight();
            if (weight + curWeight <= W) {
                weight += curWeight;
                result.add(sortedItems.get(i));
            }
            i++;
        }
        return result;
    }

    // Return max result
    static int GAMaxyResult(List<Item> items) {
        return items.stream()
                .mapToInt(Item::getValue)
                .sum();
    }

}
