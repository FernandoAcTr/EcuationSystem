package controller;

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

import java.io.IOException;
import java.net.URL;
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

    int numVariables;

    public void initialize(URL location, ResourceBundle resources) {
        initGUI();
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
                double data[][] = getTableData();
            }
        });


        SpinnerValueFactory values = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10);
        values.setValue(3);
        spinNumVariable.setValueFactory(values);
    }

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
            for (int col = 0; col <= numVariables; col++) {
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
}
