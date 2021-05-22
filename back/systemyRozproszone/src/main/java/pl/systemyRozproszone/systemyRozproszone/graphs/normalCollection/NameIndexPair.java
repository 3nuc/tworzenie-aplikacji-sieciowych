package pl.systemyRozproszone.systemyRozproszone.graphs.normalCollection;

public class NameIndexPair {

    String value;
    int valueNumerized;

    public NameIndexPair() {
    }

    public NameIndexPair(String value, int valueNumerized) {
        this.value = value;
        this.valueNumerized = valueNumerized;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getValueNumerized() {
        return valueNumerized;
    }

    public void setValueNumerized(int valueNumerized) {
        this.valueNumerized = valueNumerized;
    }
}
