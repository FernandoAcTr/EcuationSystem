package model;

import javafx.scene.control.TextArea;

import java.text.DecimalFormat;

public class ResolvMethods {
    private double[][] matrix;
    private int numVariables;
    private DecimalFormat formatter;
    private String procedure;
    private String separator;

    public ResolvMethods() {
        procedure = "";
        separator = "";
        formatter = new DecimalFormat("0.00");
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setNumVariables(int numVariables) {
        this.numVariables = numVariables;
        separator = "";
        for (int i = 0; i < numVariables * 11; i++) {
            separator += "-";
        }
    }

    public void resolvByGauss() {
        int indexPivot, row, col;
        double pivot, factor;

        for (indexPivot = 0; indexPivot < numVariables; indexPivot++) { //recorre la diagonal de pivotes
            pivot = matrix[indexPivot][indexPivot];

            procedure += "Hacer 1 el pivote " + formatter.format(pivot) + " dividiendo la fila " + (indexPivot + 1) + " sobre si mismo";
            concatProcedure();

            for (col = indexPivot; col < numVariables + 1; col++) { //recorre la fila del pivote para dividirla sobre el pivote
                matrix[indexPivot][col] = matrix[indexPivot][col] / pivot;
            }

            concatProcedure();
            if (indexPivot + 1 < numVariables)
                procedure += "Hacer 0 los índices bajo el pivote: \n";

            for (row = indexPivot + 1; row < numVariables; row++) {  //recorre la columna del pivote para hacer 0, los indices inferiores
                factor = matrix[row][indexPivot];

                procedure += "Hacer 0 el indice de la fila " + (row + 1) + " haciendo: R" + (row + 1) + " - " + formatter.format(factor) + "*R" + (indexPivot + 1);

                for (col = indexPivot; col < numVariables + 1; col++) {
                    matrix[row][col] = matrix[row][col] - factor * matrix[indexPivot][col];
                }

                concatProcedure();
            }
        }

    }

    public double[] getGaussResults() {
        double results[] = new double[numVariables];
        int pivotRow, col;
        double tempResult;

        results[numVariables - 1] = matrix[numVariables - 1][numVariables]; //el primer resultado se sabe de facto

        //comenzamos a despejar desde una fila antes que el ultimo pivote que ya se sabe el resultado

        for (pivotRow = numVariables - 2; pivotRow >= 0; pivotRow--) {
            tempResult = matrix[pivotRow][numVariables];

            for (col = numVariables - 1; col > pivotRow; col--) {
                tempResult += (-1) * matrix[pivotRow][col] * results[col];
            }

            results[pivotRow] = tempResult;
        }

        return results;
    }

    public void resolvByGauss_Jordan() {
        int indexPivot, row, col;
        double pivot, factor;

        for (indexPivot = 0; indexPivot < numVariables; indexPivot++) { //recorre la diagonal de pivotes
            pivot = matrix[indexPivot][indexPivot];

            procedure += "Hacer 1 el pivote " + formatter.format(pivot) + " dividiendo la fila " + (indexPivot + 1) + " sobre si mismo";
            concatProcedure();

            for (col = indexPivot; col < numVariables + 1; col++) { //recorre la fila del pivote para dividirla sobre el pivote
                matrix[indexPivot][col] = matrix[indexPivot][col] / pivot;
            }

            concatProcedure();
            if (indexPivot + 1 < numVariables)
                procedure += "Hacer 0 los índices sobre y debajo del pivote: \n";

            for (row = 0; row < numVariables; row++) {  //recorre la columna del pivote para hacer 0, los indices inferiores y superiores

                if (row != indexPivot) {

                    factor = matrix[row][indexPivot];

                    procedure += "Hacer 0 el indice de la fila " + (row + 1) + " haciendo: R" + (row + 1) + " - " + formatter.format(factor) + "*R" + (indexPivot + 1);

                    for (col = indexPivot; col < numVariables + 1; col++) {
                        matrix[row][col] = matrix[row][col] - factor * matrix[indexPivot][col];
                    }

                    concatProcedure();
                }
            }
        }
    }

    public double[] getGaussJordanResults() {
        double results[] = new double[numVariables];
        int pivotRow, col;

        for (int row = 0; row < numVariables; row++) {
            results[row] = matrix[row][numVariables];
        }

        return results;
    }

    private void concatProcedure() {
        procedure += "\n" + printMatrix() + "\n";
    }


    public String printMatrix() {
        String mat = "";
        String aux;
        for (int i = 0; i < numVariables; i++) {
            for (int j = 0; j < numVariables + 1; j++) {
                aux = matrix[i][j] < 0 ? formatter.format(matrix[i][j]) : " " + formatter.format(matrix[i][j]);
                mat += " | " + aux;
            }
            mat += "\n";
            mat += separator;
            mat += "\n";
        }

        return mat;
    }

    public String getProcedure() {
        return procedure;
    }

    public void reestartProcedure() {
        procedure = "";
    }
}
