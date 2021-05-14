package pl.systemyRozproszone.systemyRozproszone.graphs.histogram;

import java.util.ArrayList;
import java.util.List;

public class HistogramColumnHolder {

    double sectorMin;
    double sectorMax;
    List<HistogramElement> values;

    public HistogramColumnHolder(double sectorMin, double sectorMax, List<HistogramElement> values) {
        this.sectorMin = sectorMin;
        this.sectorMax = sectorMax;
        this.values = values;
    }

    public HistogramColumnHolder() {
        values = new ArrayList<>();
    }

    public double getSectorMin() {
        return sectorMin;
    }

    public void setSectorMin(double sectorMin) {
        this.sectorMin = sectorMin;
    }

    public double getSectorMax() {
        return sectorMax;
    }

    public void setSectorMax(double sectorMax) {
        this.sectorMax = sectorMax;
    }

    public List<HistogramElement> getValues() {
        return values;
    }

    public void setValues(List<HistogramElement> values) {
        this.values = values;
    }
}
