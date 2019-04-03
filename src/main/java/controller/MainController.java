package controller;

import com.jfoenix.controls.JFXTabPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import model.ResolvMethods;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    GridPane paneTable;

    @FXML
    Button btnNumVariables, btnResolve;

    @FXML
    Spinner<Integer> spinNumVariable;

    @FXML
    private MenuItem mnuAbout;

    @FXML
    TextArea textAreaSolution;

    @FXML
    ComboBox<String> cmbProcedure;

    @FXML
    JFXTabPane tabPane;

    int numVariables;
    ResolvMethods solver;
    DecimalFormat formatter;

    public void initialize(URL location, ResourceBundle resources) {
        initGUI();
        solver = new ResolvMethods();
        formatter = new DecimalFormat("0.00");
    }

    public void initGUI() {
        numVariables = 3;
        setNumVariables(numVariables);

        btnNumVariables.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                numVariables = spinNumVariable.getValue();
                setNumVariables(numVariables);
            }
        });

        mnuAbout.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/fxml/layout_about.fxml"));
                    Scene scene = new Scene(root, 420, 360);
                    scene.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnResolve.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                btnResolveAction(cmbProcedure.getSelectionModel().getSelectedIndex());
            }
        });


        SpinnerValueFactory values = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10);
        values.setValue(numVariables);
        spinNumVariable.setValueFactory(values);

        cmbProcedure.getItems().add("Gauss");
        cmbProcedure.getItems().add("Gauss-Jordan");
        cmbProcedure.getItems().add("Jacobi");
    }

    /**
     * Genera los TextField necesarios para el numero de variables
     * @param numVariables
     */
    private void setNumVariables(int numVariables) {

        paneTable.getChildren().clear();
        paneTable.getColumnConstraints().clear();

        for (int i = 0; i < numVariables; i++) {
            Label lbl = new Label("X" + (i + 1));
            lbl.getStyleClass().add("lbl");
            lbl.getStyleClass().add("lbl-default");
            lbl.getStyleClass().add("h4");
            paneTable.add(lbl, i, 0);
        }

        for (int row = 1; row <= numVariables; row++)
            for (int col = 0; col <  numVariables + 1; col++) {
                TextField txt = new TextField();
                txt.getStyleClass().add("text-primary");
                paneTable.add(txt, col, row);
            }

        /*Set size to the columns
        one constraint for column, it is to say, I need add 3 constraint if I have 3 columns for example.
        paneTable know that a new constraint is for the next column*/
        for (int i = 0; i <= numVariables; i++) {
            ColumnConstraints constraint = new ColumnConstraints(50, 70, Double.MAX_VALUE);
            constraint.setHgrow(Priority.ALWAYS);
            paneTable.getColumnConstraints().add(constraint);
        }
    }

    /**
     * Obtiene todos los valores de las TextField y los convierte en una matriz
     * @return
     */
    private double[][] getTableData() {
        double data[][] = new double[numVariables][numVariables + 1];
        int numTxt = numVariables;
        double num;

        for (int row = 0; row < numVariables; row++)
            for (int col = 0; col < numVariables + 1; col++, numTxt++) {
                num = Double.valueOf(((TextField) paneTable.getChildren().get(numTxt)).getText());
                data[row][col] = num;
            }
        return data;
    }

    private void btnResolveAction(int type){
        double data[][];

        data = getTableData();
        solver.setMatrix(data);
        solver.setNumVariables(numVariables);

       if(type == 0)
          resolvGaussAction();

       else if(type == 1)
           resolvGaussJordanAction();

       else if(type == 2)
           resolJacobiAction();


       tabPane.getSelectionModel().selectNext();
    }

    private void resolvGaussAction(){
        double results[];

        solver.resolvByGauss();
        results = solver.getGaussResults();

        textAreaSolution.clear();
        textAreaSolution.setText(solver.getProcedure());
        solver.reestartProcedure();

        textAreaSolution.appendText("\nCon esto obtenemos la solución de la última incógnita. Usarla para formar expresiones con las " +
                "filas anteriores, sustituir y resolver. Cada fila dará una solución para una incógnita\n");

        for (int i = 0; i < results.length; i++)
            textAreaSolution.appendText("X"+(i+1)+" = "+formatter.format(results[i])+"\n");
    }

    private void resolvGaussJordanAction(){
        double results[];

        solver.resolvByGauss_Jordan();
        results = solver.getGaussJordanResults();

        textAreaSolution.clear();
        textAreaSolution.setText(solver.getProcedure());
        solver.reestartProcedure();

        textAreaSolution.appendText("\nCon esto obtenemos la solución de todas las incógnitas donde el valor de cada una" +
                "viene representado por el último valor de su respectiva fila en la matriz\n");

        for (int i = 0; i < results.length; i++)
            textAreaSolution.appendText("X"+(i+1)+" = "+formatter.format(results[i])+"\n");

    }

    private void resolJacobiAction(){

        double[][] values = {
                {6,-1,-1,4, 17},
                {1,-10,2,-1,-17},
                {3,-2,8,-1,19},
                {1,1,1,-5,-14}
        };

        solver.setNumVariables(4);
        solver.setErrorPermited(0.001);
        solver.setMatrix(values);
        solver.resolvByJacobi();

        textAreaSolution.clear();
        textAreaSolution.setText(solver.getProcedure());
        solver.reestartProcedure();
    }
}
