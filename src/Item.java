public class Item implements Comparable<Item>{
    private int value;
    private int weight;

    public Item(int value, int weight) {
        this.value = value;
        this.weight = weight;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getRatio() {
        return (double) value / weight;
    }

    @Override
    public String toString() {
        return "Item{" +
                "price=" + value +
                ", weight=" + weight +
                '}';
    }

    @Override
    public int compareTo(Item o) {
        return (int) (this.getRatio() - o.getRatio());
    }
}
