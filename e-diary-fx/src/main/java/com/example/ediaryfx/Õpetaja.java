package com.example.ediaryfx;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Õpetaja implements Serializable{
    private String eesnimi;
    private String perenimi;

    //siin hoiame kõiki klasse
    private ArrayList<Klass> klassid = new ArrayList<>();
    public Õpetaja(String eesnimi, String perenimi) {
        this.eesnimi = eesnimi;
        this.perenimi = perenimi;
    }

    /**
     * Klasside loomine
     * @return klasside ArrayList
     */
    public ArrayList<Klass> moodustaGrupp(){
        Scanner õpetajaSisendGrupp = new Scanner(System.in);
        System.out.println("Sisestage klass: ");
        String valitudKlass = õpetajaSisendGrupp.nextLine();
        System.out.println("Mitu ainet te soovite lisada selle klassi jaoks? ");
        int aineteArv = õpetajaSisendGrupp.nextInt();
        String[] aineteKogum = new String[aineteArv];
        Scanner sisend = new Scanner(System.in);

        //lisame kõik ained eraldi massiivi
        for (int i = 0; i < aineteArv; i++) {
            System.out.println("Sisestage aine nimi: ");
            String aineNimi = sisend.nextLine();
            aineteKogum[i] = aineNimi;
        }

        //kahemõõtmeline ArrayList, kus hoitakse kõiki
        ArrayList<ArrayList<Õpilane>> õpilasteGrupp = new ArrayList<>(aineteArv);


        Scanner õpetajaSisendGrupp2 = new Scanner(System.in);
        int i = aineteArv-1;
        while (true){
            System.out.println("Sisestage õpilase eesnimi\nLõpetamiseks sisestage 'q'");
            String õpilaseEesnimi = õpetajaSisendGrupp2.nextLine();
            if (õpilaseEesnimi.equals("q")) break;
            System.out.println("Sisestage õpilase perenimi");
            String õpilasePerenimi = õpetajaSisendGrupp2.nextLine();
            if (õpilasePerenimi.equals("q")) break;

            while (i >= 0){
                õpilasteGrupp.add(new ArrayList<>());
                i--;
            }

            //lisame õpilast nii palju, kui on aineid (üks õpilane võib käia mitmel ainel)
            for (int j = 0; j < aineteArv; j++) {
                ArrayList<Integer> hinded = new ArrayList<>();
                //loome õpilase objekti kuhu lisame ka hinnete ArratListi, mis on hetkel tühi. Objekt on alati uus, kuid õpilane on sama peab tema objekt olema erinev iga aine jaoks
                Õpilane õpilane = new Õpilane(õpilaseEesnimi, õpilasePerenimi, hinded);
                õpilasteGrupp.get(j).add(õpilane);
            }
        }

        for (int j = 0; j < õpilasteGrupp.size(); j++) {
            //iga aine jaoks on erinev klass, kuid kõik õpilased on samad
            Klass klass  = new Klass(valitudKlass, õpilasteGrupp.get(j), aineteKogum[j]);
            klassid.add(klass);
        }

        return klassid;
    }

    /**
     * Kuvab klassi nimekirja
     * @param klassid kõikide klasside ArrayList
     */
    public void kuvaNimekiri(ArrayList<Klass> klassid){
        Scanner klassiValik = new Scanner(System.in);
        System.out.println("Valige klass");
        String valitudKlass = klassiValik.nextLine();

        //otsime sobilikku klassi
        for (Klass klass : klassid) {
            if (klass.getKlassiNumber().equals(valitudKlass)){
                int num = 1;
                System.out.println(klass.getKlassiNumber());
                //läbime kõiki õpilasi ja väljastame neid
                for (Õpilane õpilane : klass.getÕpilasteGrupp()) {
                    System.out.println(num + ". " + õpilane.getEesnimi() + " " + õpilane.getPerenimi());
                    num++;
                }
                System.out.println();
                break;
            }
        }
    }

    /**
     * Meetod võimaldab lisada hindeid
     * @param klassid kõikide klasside ArrayList
     */
    public void lisaHinne(ArrayList<Klass> klassid){
        Scanner õpetajaSisendHinne = new Scanner(System.in);

        System.out.println("Valige klass kuhu soovid hinnet panna");
        String valitudKlass = õpetajaSisendHinne.nextLine();
        System.out.println("Valige aine kuhu soovid hinnet panna");
        String valitudAine = õpetajaSisendHinne.nextLine();

        while (true){
            Scanner õpetajaSisendHinne1 = new Scanner(System.in);
            System.out.println("Valige õpilast kellele soovid hinnet panna (perenimi)\nLõpetamiseks sisestage 'q'");
            String valitudOpilane = õpetajaSisendHinne1.nextLine();
            if (valitudOpilane.equals("q")) break;
            System.out.println("Sisestage hinne");
            int hinne = õpetajaSisendHinne1.nextInt();

            stop:
            for (Klass klass : klassid) {
                //otsime sobilikku klassi
                if (klass.getAine().equals(valitudAine) && klass.getKlassiNumber().equals(valitudKlass)){
                    for (Õpilane õpilane : klass.getÕpilasteGrupp()) {
                        //kontrollime, et paneksime hiinet õigele õpilasele
                        if (õpilane.getPerenimi().equals(valitudOpilane)){
                            õpilane.getHinded().add(hinne);
                            //edasi otsima ei pea
                            break stop;
                        }
                    }
                }
            }
        }
    }


    /**
     * Meetod arvutab õpilaste keskmist hinnet aine eest
     * @param klassid kõikide klasside ArrayList
     */
    public void aineKeskmineHinne(ArrayList<Klass> klassid){
        Scanner õpetajaSisendHinne = new Scanner(System.in);

        System.out.println("Valige klass ");
        String valitudKlass = õpetajaSisendHinne.nextLine();
        System.out.println("Valige aine ");
        String valitudAine = õpetajaSisendHinne.nextLine();

        for (Klass klass : klassid) {
            //otsime sobilikku klassi
            if (klass.getAine().equals(valitudAine) && klass.getKlassiNumber().equals(valitudKlass)){
                int hinneteSumma = 0;
                int kokkuHindeid = 0;
                //arvutame keskmist hinnet
                for (Õpilane õpilane : klass.getÕpilasteGrupp()) {
                    //leiame mitu üldse on hinnet
                    kokkuHindeid += õpilane.getHinded().size();
                    for (int hinne : õpilane.getHinded()) {
                        //arvutame hinnete summat
                        hinneteSumma+=hinne;
                    }
                }
                double keskmineHinne = (double) hinneteSumma/kokkuHindeid;
                System.out.println(keskmineHinne);
                System.out.println();
                break;
            }
        }
    }


    /**
     * Meetod kuvab juhuslikku õpilast valitud klassist
     * @param klassid kõikide klasside ArrayList
     */
    public void tahvliJuurde(ArrayList<Klass> klassid){
        Scanner õpilaneTahvliJuurde = new Scanner(System.in);

        System.out.println("Valige klass millest õpilane tuleb tahvli juurde ");
        String valitudKlass = õpilaneTahvliJuurde.nextLine();

        stop:
        for (Klass klass : klassid) {
            //otsime sobilikku klassi
            if (klass.getKlassiNumber().equals(valitudKlass)){
                //genereerime suvalise indeksi
                int juhuslikOpilane = (int) ((Math.random() * (klass.getÕpilasteGrupp().size())));
                int loendur = 0;
                //leiame õpilast, kes on selle indeski kohal ja väljastame
                for (Õpilane õpilane : klass.getÕpilasteGrupp()) {
                    if (loendur == juhuslikOpilane){
                        System.out.println("Tahvli juurde läheb: " + õpilane.getEesnimi() + " " + õpilane.getPerenimi());
                        System.out.println();
                        break stop;
                    }
                    loendur++;
                }
            }
        }
    }

    /**
     * Meetod kirjutab valitud klassi kõikide õpilaste hindeid eraldi faili
     * @param klassid kõikide klasside ArrayList
     * @throws Exception
     */
    public void hindedFaili(ArrayList<Klass> klassid) throws Exception{
        Scanner klassiValik = new Scanner(System.in);
        System.out.println("Sisestage klass: ");
        String valitudKlass = klassiValik.nextLine();

        PrintWriter myWriter = new PrintWriter(valitudKlass + ".txt", "UTF-8");
        for (Klass klass : klassid) {
            //otsime sobilikku klassi
            if (klass.getKlassiNumber().equals(valitudKlass)) {
                myWriter.write(klass.getAine() + "\n");

                //läbime igat õpilast
                for (Õpilane õpilane : klass.getÕpilasteGrupp()) {
                    myWriter.write(õpilane.getEesnimi() + " " +õpilane.getPerenimi() + ": ");
                    //läbime igat hinnet
                    for (int hinne : õpilane.getHinded()) {
                        myWriter.write(hinne + ", ");
                    }
                    myWriter.write("\n");
                }
            }
        }
        myWriter.flush();
    }

    /**
     * meetod salvestab kõikide klasside andmed faili
     * @param klassid klasside list
     * @throws IOException
     */
    public void salvestaAndmed(ArrayList<Klass> klassid) throws IOException {
        FileOutputStream fos = new FileOutputStream("andmed.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(klassid);

        System.out.println("Salvestatud!");
        oos.close();
        fos.close();
    }
}

