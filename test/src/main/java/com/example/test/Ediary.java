package com.example.test;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class Ediary extends Application  {

    ArrayList<Klass> klassid = loeKlassid("andmed.ser");
    private Stage mainStage;
    private String opilaseNimi;
    private String opilaseKlass;




    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Stage loginStage = Login();
        this.mainStage = primaryStage();
        loginStage.show();

    }


    private Stage primaryStage(){
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Hinded");

        TableView<ObservableList<SimpleStringProperty>> tabel = new TableView<>();
        tabel.setEditable(false);


        Button andmedFailiBtn = new Button("file");
        andmedFailiBtn.setOnAction(event -> {
            stop:
            for (Klass klass : klassid) {
                if (klass.getKlassiNumber().equals(klass)) {
                    for (Õpilane õpilane : klass.getÕpilasteGrupp()) {
                        if (õpilane.getPerenimi().equals(opilaseNimi)) {
                            try {
                                õpilane.hinnedFaili(klassid, opilaseNimi, opilaseKlass);
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle(null);
                                alert.setHeaderText(null);
                                alert.setContentText("Andmed on salvestatud");
                                alert.showAndWait();
                                break stop;

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        });


        Button keskmineHinneBtn = new Button("Avg");
        TextField aineNimetus = new TextField();
        aineNimetus.setPromptText("Aine nimetus");
        keskmineHinneBtn.setOnAction(e -> {

            double keskmine = 0;
            String aine = aineNimetus.getText();

            for (Klass klass : klassid) {
                if (klass.getKlassiNumber().equals(opilaseKlass)) {
                    for (Õpilane õpilane : klass.getÕpilasteGrupp()) {
                        if (õpilane.getPerenimi().equals(opilaseNimi)) {
                            keskmine = õpilane.õpilaseKeskmineHinne(klassid, opilaseNimi, opilaseKlass, aine);
                        }
                    }
                }
            }


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Keskmine hinne: " + keskmine);
            alert.showAndWait();
        });

        HBox paigutaNuppud = new HBox();
        paigutaNuppud.setSpacing(5);
        paigutaNuppud.getChildren().addAll(andmedFailiBtn, aineNimetus, keskmineHinneBtn);

        //Õpilase nimega veerg
        TableColumn<ObservableList<SimpleStringProperty>, String> opilaseNimegaVeerg = new TableColumn<>("Nimi");
        opilaseNimegaVeerg.setCellValueFactory(param -> param.getValue().get(0));
        opilaseNimegaVeerg.setMinWidth(150);
        tabel.getColumns().add(opilaseNimegaVeerg);

        // Aine nimetusega veerg
        TableColumn<ObservableList<SimpleStringProperty>, String> aineNimetusegaVeerg = new TableColumn<>("Aine");
        aineNimetusegaVeerg.setCellValueFactory(param -> param.getValue().get(1));
        aineNimetusegaVeerg.setMinWidth(150);
        tabel.getColumns().add(aineNimetusegaVeerg);

        ObservableList<ObservableList<SimpleStringProperty>> hinded = getHinded();


        int maxHinneteArv = 0;
        for (ObservableList<SimpleStringProperty> hinne : hinded) {
            maxHinneteArv = Math.max(maxHinneteArv, hinne.size() - 2);
        }

        // Hinnete veerud
        for (int i = 0; i < maxHinneteArv; i++) {
            int gradeIndex = i + 2;
            TableColumn<ObservableList<SimpleStringProperty>, String> hinnetegaVeerg = new TableColumn<>("Hinne " + (i + 1));
            hinnetegaVeerg.setCellValueFactory(param -> {
                if (param.getValue().size() > gradeIndex) {
                    return param.getValue().get(gradeIndex);
                } else {
                    return new SimpleStringProperty("");
                }
            });
            hinnetegaVeerg.setMinWidth(75);
            tabel.getColumns().add(hinnetegaVeerg);
        }


        tabel.setItems(hinded);

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(tabel);
        vbox.getChildren().add(paigutaNuppud);


        primaryStage.setScene(new Scene(vbox));
//        primaryStage.show();
        return primaryStage;
    }





    private ObservableList<ObservableList<SimpleStringProperty>> getHinded() {
        ObservableList<ObservableList<SimpleStringProperty>> hinded = FXCollections.observableArrayList();


        for (Klass klass : klassid) {
            if (klass.getKlassiNumber().equals(opilaseKlass)) {
                for (Õpilane õpilane : klass.getÕpilasteGrupp()) {
                    if (õpilane.getPerenimi().equals(opilaseNimi)) {
                        ObservableList<SimpleStringProperty> rida = FXCollections.observableArrayList();
                        rida.add(new SimpleStringProperty(õpilane.getPerenimi()));
                        rida.add(new SimpleStringProperty(klass.getAine()));

                        for (int hinne : õpilane.getHinded()) {
                            rida.add(new SimpleStringProperty(String.valueOf(hinne)));
                        }

                        hinded.add(rida);
                    }
                }
            }
        }

        return hinded;
    }


    private Stage Login() {
        Stage loginStage = new Stage();
        loginStage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextField nimiF = new TextField();
        TextField klassF = new TextField();
        grid.add(new Label("Perenimi:"), 0, 0);
        grid.add(nimiF, 1, 0);
        grid.add(new Label("Klass:"), 0, 1);
        grid.add(klassF, 1, 1);

        Button btn = new Button("Login");
        btn.setOnAction(e -> {
            String nimi = nimiF.getText();
            this.opilaseNimi = nimi;
            String klass = klassF.getText();
            this.opilaseKlass = klass;

            if (!nimi.isEmpty() && !klass.isEmpty()) {
                this.mainStage = primaryStage();
                mainStage.show();
                loginStage.close();
            }
        });

        grid.add(btn, 1, 2);

        Scene scene = new Scene(grid);
        loginStage.setScene(scene);
        loginStage.show();
        return loginStage;
    }




    private ArrayList<Klass> loeKlassid(String failinimi) {
        try {
            FileInputStream is = new FileInputStream(failinimi);
            ObjectInputStream ois = new ObjectInputStream(is);
            ArrayList<Klass> klassid = (ArrayList<Klass>) ois.readObject();
            return klassid;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}