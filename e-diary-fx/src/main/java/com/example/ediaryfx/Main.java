package com.example.ediaryfx;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    /**
     * Meetod kuvab õpetaja jaoks menüü, mis hiljem töötab teiste klasside ja meetodite abil
     * @param klassid kõikide klasside ArrayList
     * @param õpetaja Õpetaja objekt
     * @throws Exception
     */
    public static int õpetajaRoll(ArrayList<Klass> klassid, Õpetaja õpetaja) throws Exception{
        Scanner õpetajaRolliSisend = new Scanner(System.in);
        while (true) {
            System.out.println("Valige tegevus:\n1. Lisa uus klass\n2. Lisa hinne\n3. Kuva klassi nimekiri\n4. Vali suvalist õpilast\n5. Vaata aine keskmist hinnet\n6. Hinded faili\n7. Salvesta kõik tulemused\nVäljumiseks sisestage 'q'");
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
            } else if (tegevus.equals("q")) return 0;
        }
    }


    public static void main(String[] args) throws Exception {
        Scanner õpetajaSisend = new Scanner(System.in);
        System.out.println("Sisestage oma eesnimi");
        String õpetajaEesnimi = õpetajaSisend.nextLine();
        System.out.println("Sisestage oma perenimi");
        String õpetajaPerenimi = õpetajaSisend.nextLine();
        Õpetaja õpetaja = new Õpetaja(õpetajaEesnimi, õpetajaPerenimi);
        System.out.println("Esmalt peate lisama õpilasi");
        ArrayList<Klass> klassid = õpetaja.moodustaGrupp();


        while (õpetajaRoll(klassid, õpetaja) != 0) {
            õpetajaRoll(klassid, õpetaja);
        }
    }
}







