package com.example.test;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
        primaryStage.setTitle("Grades Table");

        TableView<ObservableList<SimpleStringProperty>> table = new TableView<>();
        table.setEditable(false);

        Button button1 = new Button("Button 1");
        Button button2 = new Button("Button 2");
        TextField textField = new TextField();

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(5);
        buttonBox.getChildren().addAll(button1, textField, button2);

        // Add student name column
        TableColumn<ObservableList<SimpleStringProperty>, String> studentNameCol = new TableColumn<>("Student Name");
        studentNameCol.setCellValueFactory(param -> param.getValue().get(0));
        studentNameCol.setMinWidth(150);
        table.getColumns().add(studentNameCol);

        // Add lesson name column
        TableColumn<ObservableList<SimpleStringProperty>, String> lessonNameCol = new TableColumn<>("Lesson Name");
        lessonNameCol.setCellValueFactory(param -> param.getValue().get(1));
        lessonNameCol.setMinWidth(150);
        table.getColumns().add(lessonNameCol);

        ObservableList<ObservableList<SimpleStringProperty>> entries = getGradeEntries();

        // Determine the maximum number of grades
        int maxGrades = 0;
        for (ObservableList<SimpleStringProperty> entry : entries) {
            maxGrades = Math.max(maxGrades, entry.size() - 2);
        }

        // Add grade columns
        for (int i = 0; i < maxGrades; i++) {
            int gradeIndex = i + 2;
            TableColumn<ObservableList<SimpleStringProperty>, String> gradeCol = new TableColumn<>("Grade " + (i + 1));
            gradeCol.setCellValueFactory(param -> param.getValue().size() > gradeIndex ? param.getValue().get(gradeIndex) : new SimpleStringProperty(""));
            gradeCol.setMinWidth(75);
            table.getColumns().add(gradeCol);
        }

        // Add data to the table
        table.setItems(entries);

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(table);
        vbox.getChildren().add(buttonBox);


        primaryStage.setScene(new Scene(vbox));
        primaryStage.show();
    }

    private ObservableList<ObservableList<SimpleStringProperty>> getGradeEntries() throws ClassNotFoundException, IOException {
        ObservableList<ObservableList<SimpleStringProperty>> entries = FXCollections.observableArrayList();
        ArrayList<Klass> klassid = loeKlassid("andmed.ser");
        String õpilaseKlass = "1a"; // Replace with the actual class
        String õpilasePerenimi = "filin"; // Replace with the actual student's last name

        for (Klass klass : klassid) {
            if (klass.getKlassiNumber().equals(õpilaseKlass)) {
                for (Õpilane õpilane : klass.getÕpilasteGrupp()) {
                    if (õpilane.getPerenimi().equals(õpilasePerenimi)) {
                        ObservableList<SimpleStringProperty> row = FXCollections.observableArrayList();
                        row.add(new SimpleStringProperty(õpilane.getPerenimi()));
                        row.add(new SimpleStringProperty(klass.getAine()));

                        for (int hinne : õpilane.getHinded()) {
                            row.add(new SimpleStringProperty(String.valueOf(hinne)));
                        }

                        entries.add(row);
                    }
                }
            }
        }

        return entries;
    }


    private ArrayList<Klass> loeKlassid(String failinimi) throws IOException, ClassNotFoundException {
        try (FileInputStream is = new FileInputStream(failinimi);
             ObjectInputStream ois = new ObjectInputStream(is)) {

            ArrayList<Klass> klassid = (ArrayList<Klass>) ois.readObject();

            return klassid;
        }
    }
}

