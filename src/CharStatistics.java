public class CharStatistics implements Comparable<CharStatistics>{
    private final char symbol;
    private int count;

    CharStatistics(char symbol, int count) {
        this.symbol = symbol;
        this.count = count;
    }
    CharStatistics(char symbol) {
        this(symbol, 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CharStatistics that = (CharStatistics) o;

        return symbol == that.symbol;
    }

    @Override
    public int compareTo(CharStatistics charStatistics) {
        return this.symbol - charStatistics.symbol;
    }

    @Override
    public int hashCode() {
        return symbol;
    }

    public void increaseCount() {
        this.count++;
    }
    public char getSymbol() {
        return symbol;
    }
    public int getCount() {
        return count;
    }
}
