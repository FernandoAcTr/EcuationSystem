package model;

import javafx.beans.property.SimpleSetProperty;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MultiVariableFuntion {
    private String function;
    private Expression expression;
    private int numVariables;

    public MultiVariableFuntion(String function, int numVariables) {
        this.function = function;
        this.numVariables = numVariables;

        expression = new ExpressionBuilder(function)
                .implicitMultiplication(true)
                .variables(getVariables())
                .build();
    }

    private Set<String> getVariables() {
        Set<String> variables = new TreeSet<String>();

        if (numVariables < 2)
            variables.add("x");
        else {
            for (int i = 1; i <= numVariables; i++)
                variables.add("x" + i);
        }

        return variables;
    }

    /**
     * Evalua la funcion dada para un cierto valor de cada variable
     *
     * @param values Los valores de cada variable para los cuales requiere elvaluar la función
     * @return double - El valor de la función para esos puntos
     * @throws Exception
     *
     * Si se crea una funcion donde no aparece alguna de las variables desde X1 hasta X-enesima. Entonces
     * el valor de esa variable en el vector de valores puede ser cualsea. Para efectos de diferenciar, se puede
     * poner -1 o 0. Lo importante es que el vector tenga tantos valores como numero de variables sea la funcion
     * en el orden desde X1 hasta X-enesima.
     */
    public double evaluateFrom(double[] values) throws Exception {
        double result = Double.NaN;

        if (numVariables < 2)
            expression.setVariable("x", values[0]);
        else
            for (int i = 0; i < values.length; i++) {
                expression.setVariable("x" + (i + 1), values[i]);
            }

        ValidationResult validation = expression.validate();
        if (!validation.isValid()) {
            throw new Exception("La expresión es inválida");
        }

        result = expression.evaluate();
        return result;
    }

    /**
     * Genera un rango de valores, desde un valor a otro, con los incrementos especificados
     *
     * @param from      valor inicial para el incremento
     * @param to        valor final para el incremento
     * @param increment incremento entre valores
     * @return conjunto de valores que conforman el rango
     */
    public double[] generateRange(double from, double to, double increment) {
        int numValues = (int) (Math.abs(to - from) / increment);
        double range[] = new double[numValues];
        for (int i = 0; i < numValues; i++) {
            range[i] = from;
            from += increment;
        }
        return range;
    }


    public String getFunction() {
        return function;
    }
}
