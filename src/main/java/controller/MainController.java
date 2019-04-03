package controller;

import com.jfoenix.controls.JFXTabPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    @FXML
    TextField txtError;

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
                Stage stage = (Stage) btnNumVariables.getParent().getScene().getWindow();
                int diference = Math.abs(numVariables - spinNumVariable.getValue());

                if (spinNumVariable.getValue() > numVariables) {
                    stage.setHeight(stage.getHeight() + diference * 30);
                    stage.setWidth(stage.getWidth() + diference * 30);
                } else {
                    stage.setHeight(stage.getHeight() - +diference * 30);
                    stage.setWidth(stage.getWidth() - +diference * 30);
                }

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

        cmbProcedure.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                switch (cmbProcedure.getSelectionModel().getSelectedIndex()){
                    case 0:
                    case 1:
                        txtError.setVisible(false);
                        break;
                    case 2:
                    case 3:
                        txtError.setVisible(true);
                        break;
                }
            }
        });


        SpinnerValueFactory values = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10);
        values.setValue(numVariables);
        spinNumVariable.setValueFactory(values);

        cmbProcedure.getItems().add("Gauss");
        cmbProcedure.getItems().add("Gauss-Jordan");
        cmbProcedure.getItems().add("Jacobi");
        cmbProcedure.getItems().add("Gauss-Seidel");

        txtError.setVisible(false);
    }

    /**
     * Genera los TextField necesarios para el numero de variables
     *
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
            for (int col = 0; col < numVariables + 1; col++) {
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
     *
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

    private void btnResolveAction(int type) {
        double data[][];

        data = getTableData();
        solver.setMatrix(data);
        solver.setNumVariables(numVariables);

        if (type == 0)
            resolvGaussAction();

        else if (type == 1)
            resolvGaussJordanAction();


        else if (type == 2)
            if(txtError.getText().length() == 0) {
                showAlert("Advertencia", "Asegurate de ingresar el error permitido");
                return;
            }
            else
                resolvJacobiAction(Double.parseDouble(txtError.getText()));

        else if (type == 3)
            if (txtError.getText().length() == 0) {
                showAlert("Advertencia", "Asegurate de ingresar el error permitido");
                return;
            }
            else
                resolveGaussSeidelAction(Double.parseDouble(txtError.getText()));


        tabPane.getSelectionModel().selectNext();
    }

    private void resolvGaussAction() {
        double results[];

        solver.resolvByGauss();
        results = solver.getGaussResults();

        textAreaSolution.clear();
        textAreaSolution.setText(solver.getProcedure());
        solver.reestartProcedure();

        textAreaSolution.appendText("\nCon esto obtenemos la solución de la última incógnita. Usarla para formar expresiones con las " +
                "filas anteriores, sustituir y resolver. Cada fila dará una solución para una incógnita\n");

        printResults(results);
    }

    private void resolvGaussJordanAction() {
        double results[];

        solver.resolvByGauss_Jordan();
        results = solver.getGaussJordanResults();

        textAreaSolution.clear();
        textAreaSolution.setText(solver.getProcedure());
        solver.reestartProcedure();

        textAreaSolution.appendText("\nCon esto obtenemos la solución de todas las incógnitas donde el valor de cada una" +
                "viene representado por el último valor de su respectiva fila en la matriz\n");

        printResults(results);

    }

    private void resolvJacobiAction(double error) {

        solver.setErrorPermited(error);
        double results[] = solver.resolvByJacobi();

        textAreaSolution.clear();
        textAreaSolution.setText(solver.getProcedure());
        solver.reestartProcedure();

        printResults(results);
    }

    private void resolveGaussSeidelAction(double error) {

        solver.setErrorPermited(error);
        double results[] = solver.resolvByGauss_Seidel();

        textAreaSolution.clear();
        textAreaSolution.setText(solver.getProcedure());
        printResults(results);
        solver.reestartProcedure();
    }

    private void printResults(double[] results) {
        for (int i = 0; i < results.length; i++)
            textAreaSolution.appendText("X" + (i + 1) + " = " + formatter.format(results[i]) + "\n");
    }

    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.show();
    }
}
