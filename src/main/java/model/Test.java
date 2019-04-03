package model;

public class Test {
    public static void main(String args[]){
        double[][] values = {
                {6,-1,-1,4, 17},
                {1,-10,2,-1,-17},
                {3,-2,8,-1,19},
                {1,1,1,-5,-14}
        };

        ResolvMethods resolv = new ResolvMethods();
        resolv.setMatrix(values);
        resolv.setNumVariables(4);

        String expresion1 = ResolvMethods.getExpressionCleared(values[0], 0);
        String expresion2 = ResolvMethods.getExpressionCleared(values[1], 1);
        String expresion3 = ResolvMethods.getExpressionCleared(values[2], 2);
        String expresion4 = ResolvMethods.getExpressionCleared(values[3], 3 );

        System.out.println(expresion1);
        System.out.println(expresion2);
        System.out.println(expresion3);
        System.out.println(expresion4);

        MultiVariableFuntion funtion1 = new MultiVariableFuntion(expresion1, 4);
        MultiVariableFuntion funtion2 = new MultiVariableFuntion(expresion2, 4);
        MultiVariableFuntion funtion3 = new MultiVariableFuntion(expresion3, 4);
        MultiVariableFuntion funtion4 = new MultiVariableFuntion(expresion4, 4);

        // --------------------------------RESULTS-------------------------------------------
        try {
            System.out.println(funtion1.evaluateFrom(new double[]{0,1.7,2.375,2.8}));
            System.out.println(funtion2.evaluateFrom(new double[]{2.833,0,2.375,2.8}));
            System.out.println(funtion3.evaluateFrom(new double[]{2.833,1.7,0,2.8}));
            System.out.println(funtion4.evaluateFrom(new double[]{2.833,1.7,2.375,0}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
