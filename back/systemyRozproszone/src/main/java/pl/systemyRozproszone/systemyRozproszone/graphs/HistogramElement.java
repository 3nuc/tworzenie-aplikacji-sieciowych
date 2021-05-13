package pl.systemyRozproszone.systemyRozproszone.graphs;

public class HistogramElement {

    String decission;
    double value;

    public HistogramElement(String decission, double value) {
        this.decission = decission;
        this.value = value;
    }

    public HistogramElement() {
    }

    public String getDecission() {
        return decission;
    }

    public void setDecission(String decission) {
        this.decission = decission;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
