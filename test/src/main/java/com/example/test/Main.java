package com.example.test;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    /**
     * Meetod kuvab õpetaja jaoks menüü, mis hiljem töötab teiste klasside ja meetodite abil
     * @param klassid kõikide klasside ArrayList
     * @param õpetaja Õpetaja objekt
     * @throws Exception
     */
    public static void õpetajaRoll(ArrayList<Klass> klassid, Õpetaja õpetaja) throws Exception{
        Scanner õpetajaRolliSisend = new Scanner(System.in);
        while (true) {
            System.out.println("Valige tegevus:\n1. Lisa uus klass\n2. Lisa hinne\n3. Kuva klassi nimekiri\n4. Vali suvalist õpilast\n5. Vaata aine keskmist hinnet\n6. Hinded faili\n7. Salvesta kõik tulemused\n8. testi\nVäljumiseks sisestage 'q'");
            String tegevus = õpetajaRolliSisend.nextLine();

            if (tegevus.equals("1")){
                õpetaja.moodustaGrupp();
            } else if (tegevus.equals("2")){
                õpetaja.lisaHinne(klassid);
            } else if (tegevus.equals("3")) {
                õpetaja.kuvaNimekiri(klassid);
            } else if (tegevus.equals("4")) {
                õpetaja.tahvliJuurde(klassid);
            } else if (tegevus.equals("5")) {
                õpetaja.aineKeskmineHinne(klassid);
            } else if (tegevus.equals("6")) {
                õpetaja.hindedFaili(klassid);
            } else if (tegevus.equals("7")) {
                õpetaja.salvestaAndmed(klassid);
            } else if (tegevus.equals("8")) {
                õpetaja.loeAndmed("src/andmed.ser");
            } else if (tegevus.equals("q")) break;
        }
    }


    /**
     * Meetod kuvab õpilasele menüü, kus küsitakse tema perekonnanime ja klassi, mis edaspidi töötab teiste klasside ja meetodite abil.
     * @param klassid kõikide klasside ArrayList
     * @throws Exception
     */
    public static void õpilaseRoll(ArrayList<Klass> klassid) throws Exception {
        Scanner õpilaseRolliSisend = new Scanner(System.in);
        System.out.println("Sisestage oma perenimi");
        String õpilasePerenimi = õpilaseRolliSisend.nextLine();
        System.out.println("Sisestage oma klass");
        String õpilaseKlass = õpilaseRolliSisend.nextLine();

        //esmalt otsime sobilikku klassi ja seega õpilast
        stop:
        for (Klass klass : klassid) {
            if (klass.getKlassiNumber().equals(õpilaseKlass)){
                for (Õpilane õpilane : klass.getÕpilasteGrupp()) {
                    if (õpilane.getPerenimi().equals(õpilasePerenimi)){
                        while (true){
                            System.out.println("Valige tegevus:\n1. Vaata hindeid\n2. Hinded faili\n3. Vaata keskmist hinnet\nVäljumiseks sisestage 'q'");
                            String tegevus = õpilaseRolliSisend.nextLine();
                            if (tegevus.equals("q")) break stop;
                            if (tegevus.equals("2")) {
                                õpilane.hinnedFaili(klassid, õpilasePerenimi, õpilaseKlass);
                                continue;
                            }
                            System.out.println("Sisesta mis tunni hindeid soovid vaadata");
                            String valitudAine = õpilaseRolliSisend.nextLine();

                            if (tegevus.equals("1")) {
                                õpilane.vaataHindeid(klassid, õpilasePerenimi, õpilaseKlass, valitudAine);
                            } else if (tegevus.equals("3")) {
                                õpilane.õpilaseKeskmineHinne(klassid, õpilasePerenimi, õpilaseKlass, valitudAine);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner menüüSisend = new Scanner(System.in);
        System.out.println("Hetkel ei ole ühtegi õpetajat, seega sisenen õpetajana");
        Scanner õpetajaSisend = new Scanner(System.in);
        System.out.println("Sisestage oma eesnimi");
        String õpetajaEesnimi = õpetajaSisend.nextLine();
        System.out.println("Sisestage oma perenimi");
        String õpetajaPerenimi = õpetajaSisend.nextLine();
        Õpetaja õpetaja = new Õpetaja(õpetajaEesnimi, õpetajaPerenimi);
        System.out.println("Esmalt peate lisama õpilasi");
        ArrayList<Klass> klassid = õpetaja.moodustaGrupp();

        õpetajaRoll(klassid, õpetaja);

        while (true) {
            System.out.println("Valige sisenemise viis:\n1. Õpetaja\n2. Õpilane");
            String valik = menüüSisend.nextLine();
            if (valik.equals("1")) {
                õpetajaRoll(klassid, õpetaja);

            }
            else if (valik.equals("2")) {
                õpilaseRoll(klassid);
            }
            else
                System.exit(0);
        }
    }
}







