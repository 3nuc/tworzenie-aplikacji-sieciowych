package pl.systemyRozproszone.systemyRozproszone.graphs.corellation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class PearsonCorellationDataGenerator {



    public static String generateData(List<String> xColumn, List<String> yColumn) throws IllegalAccessException {

        ColumnData xCol = new ColumnData.Builder().data(createListOfDouble(xColumn)).title(xColumn.get(0)).build();
        ColumnData yCol = new ColumnData.Builder().data(createListOfDouble(yColumn)).title(yColumn.get(0)).build();
        
        List<CorellationColumn> corellationTable = calculateCorellationData(xCol, yCol);
        double corellation = calculateCorellation(corellationTable);
        List<Point> linearRegressionPoints = calculateLinearRegression(corellationTable);
        String formula = createFormula(corellationTable);


        //generate string
        JsonObject responseObject = new JsonObject();

        JsonArray fullArray = new JsonArray();
        JsonArray dataArray = new JsonArray();
        JsonArray titleArray = new JsonArray();
        //add titles
        for(int i=0; i<corellationTable.size();i++){
            titleArray.add(corellationTable.get(i).title);
        }
        //rows
        for(int i=0; i<corellationTable.get(0).data.size(); i++){

            JsonArray row = new JsonArray();
            //cols
            for(int j=0; j<corellationTable.size(); j++){
                row.add(corellationTable.get(j).data.get(i));
            }
            dataArray.add(row);

        }
        fullArray.add(titleArray);
        fullArray.add(dataArray);
        responseObject.add("corellationData", fullArray);

        JsonObject datasetInfo = new JsonObject();
        datasetInfo.addProperty("rows", corellationTable.get(0).data.size());
        datasetInfo.addProperty("minX", xCol.getMin());
        datasetInfo.addProperty("maxX", xCol.getMax());
        datasetInfo.addProperty("minY", yCol.getMin());
        datasetInfo.addProperty("maxY", yCol.getMax());
        responseObject.add("datasetInfo", datasetInfo);

        JsonObject corellationInfo = new JsonObject();
        corellationInfo.addProperty("corellation", corellation);
        corellationInfo.addProperty("linearRegressionFormula", formula);
        corellationInfo.addProperty("linearRegressionStartPointX", linearRegressionPoints.get(0).x);
        corellationInfo.addProperty("linearRegressionStartPointY", linearRegressionPoints.get(0).y);
        corellationInfo.addProperty("linearRegressionEndPointX", linearRegressionPoints.get(1).x);
        corellationInfo.addProperty("linearRegressionEndPointY", linearRegressionPoints.get(1).y);
        responseObject.add("corellationInfo", corellationInfo);



        return responseObject.toString();
    }

    private static String createFormula(List<CorellationColumn> corellationTable) {
        //formula is = y + a+bx
        // b= (multiplied - (n*meanX*meanY)) / (xSquared- (n*meanXSquared)
        // a = meanY - b*meanX;
        double xColMin = corellationTable.get(0).getMin();
        double xColMax = corellationTable.get(0).getMax();
        double yColMin = corellationTable.get(1).getMin();
        double yColMax = corellationTable.get(1).getMax();
        double meanX = calculateMean(corellationTable.get(0));
        double meanY = calculateMean(corellationTable.get(1));
        double n = corellationTable.get(0).data.size();
        double x = corellationTable.get(0).sum;
        double y = corellationTable.get(1).sum;
        double multiplied = corellationTable.get(2).sum;
        double xSquared  = corellationTable.get(3).sum;
        double ySquared = corellationTable.get(4).sum;

        double b = (multiplied - (n*meanX*meanY))/(xSquared-(n*Math.pow(meanX,2)));
        double a = meanY - (b*meanX);
        String toReturn = "y = "+a+" + "+b+"x";
        return toReturn;
    }

    private static List<Point> calculateLinearRegression(List<CorellationColumn> corellationTable) {

        //formula is = y + a+bx
        // b= (multiplied - (n*meanX*meanY)) / (xSquared- (n*meanXSquared)
        // a = meanY - b*meanX;
        double xColMin = corellationTable.get(0).getMin();
        double xColMax = corellationTable.get(0).getMax();
        double yColMin = corellationTable.get(1).getMin();
        double yColMax = corellationTable.get(1).getMax();
        double meanX = calculateMean(corellationTable.get(0));
        double meanY = calculateMean(corellationTable.get(1));
        double n = corellationTable.get(0).data.size();
        double x = corellationTable.get(0).sum;
        double y = corellationTable.get(1).sum;
        double multiplied = corellationTable.get(2).sum;
        double xSquared  = corellationTable.get(3).sum;
        double ySquared = corellationTable.get(4).sum;

        double b = (multiplied - (n*meanX*meanY))/(xSquared-(n*Math.pow(meanX,2)));
        double a = meanY - (b*meanX);
        Point start = new Point(xColMin, calculateY(a,b,xColMin));
        Point end = new Point(xColMax, calculateY(a,b,xColMax));
        List<Point> lineEnds = new ArrayList<>();
        lineEnds.add(start);
        lineEnds.add(end);
        return lineEnds;

    }

    private static double calculateY(double a, double b, double xVal) {

        double result = a + (b*xVal);
        return result;
    }

    private static double calculateMean(CorellationColumn corellationColumn) {
        int size = corellationColumn.data.size();
        double sum = 0;

        for(Double value : corellationColumn.data){
            sum+=value;
        }
        return sum/size;
    }

    private static double calculateCorellation(List<CorellationColumn> corellationTable) {

        //formula is (n*multiplied - x*y) / sqrt([(n*xSquared)- x^2] * [(n*ySquared) - y^2])

        double n = corellationTable.get(0).data.size();
        double x = corellationTable.get(0).sum;
        double y = corellationTable.get(1).sum;
        double multiplied = corellationTable.get(2).sum;
        double xSquared  = corellationTable.get(3).sum;
        double ySquared = corellationTable.get(4).sum;

        double result = (n*multiplied - x*y) / Math.sqrt(((n*xSquared)- Math.pow(x,2)) * ((n*ySquared) - Math.pow(y,2)));
        return result;

    }

    private static List<CorellationColumn> calculateCorellationData(ColumnData xCol, ColumnData yCol) {

        List<CorellationColumn> table = new ArrayList<>();
        CorellationColumn xColumn = new CorellationColumn.Builder().data(xCol.data).title(xCol.title).build();
        CorellationColumn yColumn = new CorellationColumn.Builder().data(yCol.data).title(yCol.title).build();
        CorellationColumn multiplyColumn = new CorellationColumn.Builder().data(multiply(xCol,yCol)).title("multiplied").build();
        CorellationColumn xSquared = new CorellationColumn.Builder().data(square(xCol)).title("xSquare").build();
        CorellationColumn ySquared = new CorellationColumn.Builder().data(square(yCol)).title("ySquare").build();

        table.add(xColumn);
        table.add(yColumn);
        table.add(multiplyColumn);
        table.add(xSquared);
        table.add(ySquared);

        return table;
    }

    private static List<Double> square(ColumnData columnData) {
        List<Double> square = new ArrayList<>();

        for(int i=0; i<columnData.getData().size(); i++){
            double current = columnData.getData().get(i);
            square.add(Math.pow(current,2));
        }
        return square;
    }

    private static List<Double> multiply(ColumnData xCol, ColumnData yCol) {
        List<Double> multiplied = new ArrayList<>();

        for(int i=0; i<xCol.getData().size(); i++){

            double currentX = xCol.getData().get(i);
            double currentY = yCol.getData().get(i);
            multiplied.add(currentX*currentY);
        }

        return multiplied;
    }

    private static List<Double> createListOfDouble(List<String> xColumn) {

        List<Double> data = new ArrayList<>();

        for(int i=1; i<xColumn.size(); i++){
            data.add(Double.valueOf(xColumn.get(i)));
        }
        return data;
    }
}
