package pl.systemyRozproszone.systemyRozproszone.graphs.corellation;

import java.util.List;

public class ColumnData {

    String title;
    List<Double> data;

    public ColumnData(String title, List<Double> data) {
        this.title = title;
        this.data = data;
    }

    public ColumnData() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }

    public double getMin(){
        double min = Double.MAX_VALUE;
        for(Double current : data){
            if(current<min){
                min = current;
            }
        }
        return min;
    }

    public double getMax(){
        double max = Double.MIN_VALUE;
        for(Double current : data){
            if(current>max){
                max = current;
            }
        }
        return max;
    }

    public static final class Builder{
        private String title;
        private List<Double> data;

        public Builder title(String title){
            this.title = title;
            return this;
        }

        public Builder data(List<Double> data){
            this.data = data;
            return this;
        }

        public ColumnData build() throws IllegalAccessException {
            if(data == null){
                throw new IllegalStateException("data mustnt be null");
            }
            if(title == null){
                throw new IllegalStateException("title mustnt be null");
            }
            ColumnData column = new ColumnData();

            column.setData(data);
            column.setTitle(title);
            return column;
        }
    }

}
