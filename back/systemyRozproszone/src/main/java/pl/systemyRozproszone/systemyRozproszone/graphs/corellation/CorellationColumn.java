package pl.systemyRozproszone.systemyRozproszone.graphs.corellation;

import java.util.List;

public class CorellationColumn {

    String title;
    List<Double> data;
    double sum;


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

        String title;
        List<Double> data;
        double sum;

        public Builder title(String title){
            this.title = title;
            return this;
        }

        public Builder data(List<Double> data){
            this.data = data;
            this.sum = calculateSum(data);
            return this;
        }

        private double calculateSum(List<Double> data) {
            double sum=0;
            for(Double item : data){
                sum+=item;
            }
            return sum;
        }

        public CorellationColumn build(){

            CorellationColumn column = new CorellationColumn();
            column.data = data;
            column.title = title;
            column.sum = sum;
            return column;
        }
    }

}
