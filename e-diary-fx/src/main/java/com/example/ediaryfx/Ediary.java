package com.example.ediaryfx;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.io.*;
import java.util.ArrayList;


public class Ediary extends Application {

    ArrayList<Klass> klassid = loeKlassid("andmed.ser");
    private Stage mainStage;
    private String opilaseNimi;
    private String opilaseKlass;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Stage Algne = Algne();
        this.mainStage = primaryStage();
        Algne.show();
    }


    /**
     * Meetod, mis vastutab üldtutvustava lühiinfo akna eest
     * @return lühiinfo stseen
     */
    private Stage Algne() {
        Stage Algne = new Stage();
        Algne.setTitle("E-päevik");

        BorderPane borderPane = new BorderPane();

        Text text = new Text("Tere tulemast e-päevikusse!");
        Font font = Font.font("Segoe UI", 24);
        text.setFont(font);

        Text infoText = new Text("Selle rakendusega saate vaadata oma tulemusi. "
                + "Võite teada saada ainete keskmist ja salvestada oma andmeid eraldi faili. "
                + "Sisselogimislehele minemiseks klõpsake allolevat nuppu. "
                + "Seal tuleb täpselt sisestada oma perekonnanimi ja klass. "
                + "Päevikus nupp \"file\" kirjutab andmed faili ja nupp \"avg\" arvutab soovitud aine keskmise hinde");
        infoText.setFont(Font.font("Segoe UI", 14));
        infoText.setTextAlignment(TextAlignment.CENTER);
        infoText.setWrappingWidth(400);

        VBox textContainer = new VBox(text, infoText);
        textContainer.setAlignment(Pos.CENTER);
        textContainer.setSpacing(20);
        BorderPane.setMargin(textContainer, new Insets(0, 0, 150, 0));
        borderPane.setCenter(textContainer);

        Button button = new Button("Alustada");

        //vahetame stseeni
        button.setOnAction(e -> {
            Stage loginStage = Login();
            this.mainStage = primaryStage();
            loginStage.show();
            Algne.close();
        });

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(button);

        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 500, 500);

        Algne.setScene(scene);
        Algne.show();

        return Algne;
    }


    /**
     * Meetod, mis vastutab pea akna eest
     * @return pea stseen
     */
    private Stage primaryStage(){
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Hinded");

        TableView<ObservableList<SimpleStringProperty>> tabel = new TableView<>();
        tabel.setEditable(false);

        //hinnete salvestamine faili
        Button andmedFailiBtn = new Button("Salvesta faili");
        andmedFailiBtn.setOnAction(event -> {
            stop:
            for (Klass klass : klassid) {
                if (klass.getKlassiNumber().equals(opilaseKlass)) {
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

        //keskmise hinne arvutamine valitud aines
        Button keskmineHinneBtn = new Button("Keskmine hinne");
        TextField aineNimetus = new TextField();
        aineNimetus.setPromptText("Aine nimetus");
        keskmineHinneBtn.setOnAction(e -> {

            double keskmine = 0;
            String aine = aineNimetus.getText();

            try {
                if (AineCheck(aine, "andmed.ser")){

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
                }
                else{
                    Alert alertKeskmine = new Alert(Alert.AlertType.INFORMATION);
                    alertKeskmine.setTitle(null);
                    alertKeskmine.setHeaderText(null);
                    alertKeskmine.setContentText("Sellist ainet ei leidu!");
                    alertKeskmine.showAndWait();
                }
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
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
        return primaryStage;
    }


    /**
     * Meetod, mis salvestab hindeid sobivas formaadis, et neid oleks võimalik tabelis kuvada
     * @return hinnete list
     */
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


    /**
     * Meetod, mis vastutab sisselogimise akna eest
     * @return sisselogimise stseen
     */
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

            try {
                //vahetame stseeni, kui kobtroll on läbitud
                if (LoginCheck(nimi, klass, "andmed.ser")){
                    this.mainStage = primaryStage();
                    mainStage.show();
                    loginStage.close();
                }
                else {
                    Alert alertLogin = new Alert(Alert.AlertType.INFORMATION);
                    alertLogin.setTitle(null);
                    alertLogin.setHeaderText(null);
                    alertLogin.setContentText("Sellist õpilast ei leidu!");
                    alertLogin.showAndWait();
                }
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }


        });

        grid.add(btn, 1, 2);
        Scene scene = new Scene(grid);
        loginStage.setScene(scene);
        loginStage.show();
        return loginStage;
    }


    /**
     * meetod, mis loeb klasse failist
     * @param failinimi vastav fail
     * @return klasside list
     */
    private ArrayList<Klass> loeKlassid(String failinimi) {
        File fail = new File(failinimi);
        if (fail.exists()) {
            try {
                FileInputStream is = new FileInputStream(failinimi);
                ObjectInputStream ois = new ObjectInputStream(is);
                ArrayList<Klass> klassid = (ArrayList<Klass>) ois.readObject();
                ois.close();
                is.close();
                return klassid;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alertFail = new Alert(Alert.AlertType.INFORMATION);
            alertFail.setTitle(null);
            alertFail.setHeaderText(null);
            alertFail.setContentText("Andmete faili ei leidu!");
            alertFail.showAndWait();
            return null;
        }
    }


    /**
     * mettod kontrollib, kas on olemas sisestatud õpilane
     * @param nimi õpilase nimi
     * @param klassinumber õpilase klassi number
     * @param failinimi klasside fail
     * @return vastav tõeväärtus
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean LoginCheck(String nimi, String klassinumber, String failinimi) throws IOException, ClassNotFoundException {

        FileInputStream fis = new FileInputStream(failinimi);
        ObjectInputStream ois = new ObjectInputStream(fis);
        ArrayList<Klass> klassid = (ArrayList<Klass>) ois.readObject();
        ois.close();
        fis.close();

        for (Klass klass : klassid) {
            if (klass.getKlassiNumber().equals(klassinumber)) {
                for (Õpilane õpilane : klass.getÕpilasteGrupp()) {
                    if (õpilane.getPerenimi().equals(nimi)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * meetod kontrollib, kas on olemas sisestatud aine
     * @param aine aine nimetus
     * @param failinimi klasside fail
     * @return vastav tõeväärtus
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean AineCheck(String aine, String failinimi) throws IOException, ClassNotFoundException {

        FileInputStream fis = new FileInputStream(failinimi);
        ObjectInputStream ois = new ObjectInputStream(fis);
        ArrayList<Klass> klassid = (ArrayList<Klass>) ois.readObject();
        ois.close();
        fis.close();

        for (Klass klass : klassid) {
            if (klass.getAine().equals(aine)){
                return true;}
        }
        return false;
    }
}