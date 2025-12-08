/*
 *  Copyright 2025 Better Orange IR & HV AG
 *
 *  Licensed under the Meetingbase License (the "License");
 *  Vou may not use this file except in compliance with the License.
 *  You may obtain a copy of the License in the root directory (MEETINGBASE_LICENSE).
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package de.meetingapps.meetingportal.meetComEntities;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class EclInhaberImportAnmeldedaten implements Comparable<EclInhaberImportAnmeldedaten>, Serializable {

    private static final long serialVersionUID = -2236838730243210804L;
    /**
     * 
     */
    private int ident;
    @Max(99999)
    private int ekNr; // int
    @Size(min = 0, max = 1, message = "PreufKennz muss zwischen 0 und 1 Zeichen enthalten")
    private String pruefKennz = "";
    @Max(9)
    private int anredeKzf; // int
    @Max(999)
    private int fadrNr; // int
    @Size(min = 0, max = 30, message = "AkadGrad muss zwischen 1 und 30 Zeichen enthalten")
    private String akadGrad = "";
    @Size(min = 0, max = 30, message = "Adelstitel1 muss zwischen 1 und 30 Zeichen enthalten")
    private String adelstitel1 = "";
    @Size(min = 0, max = 10, message = "Adelstitel2 muss zwischen 1 und 10 Zeichen enthalten")
    private String adelstitel2 = "";
    @Size(min = 0, max = 50, message = "Vorname muss zwischen 0 und 50 Zeichen enthalten")
    private String vorname = "";
    @Size(min = 0, max = 80, message = "Nachname muss zwischen 0 und 80 Zeichen enthalten")
    private String nachname = "";
    @Size(min = 0, max = 80, message = "Anmeldung muss zwischen 0 und 80 Zeichen enthalten")
    private String anmeldung = "";
    @Size(min = 0, max = 255, message = "Adresse muss zwischen 0 und 255 Zeichen enthalten")
    private String adresse = "";
    @Size(min = 0, max = 30, message = "Strasse muss zwischen 0 und 30 Zeichen lang sein") //
    private String strasse = "";
    @Size(min = 0, max = 80, message = "Adresszusatz muss zwischen 0 und 80 Zeichen enthalten")
    private String adresszusatz = "";
    @Size(min = 0, max = 10, message = "PLZ muss zwischen 0 und 10 Zeichen enthalten")
    private String plz = "";
    @Size(min = 0, max = 80, message = "Ort muss zwischen 0 und 80 Zeichen enthalten")
    private String ort = "";
    @Size(min = 0, max = 3, message = "Version muss zwischen 0 und 3 Zeichen enthalten")
    private String version = "";
    @Size(min = 0, max = 3, message = "LandKzf muss zwischen 0 und 3 Zeichen enthalten")
    private String landKzf = "";
    @Size(min = 0, max = 20, message = "Land muss zwischen 0 und 20 Zeichen enthalten")
    private String land = "";
    @Size(min = 0, max = 1, message = "VertreterKzf muss zwischen 0 und 1 Zeichen enthalten")
    private String vertreterKzf = "";
    @Positive(message = "Stueck müssen positiv sein")
    private int stueck; // int
    //	Mehr als Max
    private int nominal; // int
    @Max(999999999)
    private int stimmenanteil; // int
    @Size(min = 0, max = 1, message = "BesitzMm muss zwischen 0 und 1 Zeichen enthalten")
    private String besitzMm = "V";
    @Size(min = 0, max = 1, message = "Aktiengattung muss zwischen 0 und 1 Zeichen enthalten")
    private String aktiengattung = "";
    @Size(min = 0, max = 1, message = "ARVS muss zwischen 0 und 1 Zeichen enthalten")
    private String arvs = "";
    @Size(min = 0, max = 2, message = "StatistikMm muss zwischen 0 und 2 Zeichen enthalten")
    private String statistikMm = "";
    @Size(min = 0, max = 20, message = "DepotNr muss zwischen 0 und 20 Zeichen enthalten")
    private String depotNr = "";
    @Size(min = 0, max = 6, message = "WKN muss zwischen 0 und 6 Zeichen enthalten")
    private String wkn = "";
    @Max(999)
    private int nennwert; // int
    @Size(min = 0, max = 1, message = "VersandKzf muss zwischen 0 und 1 Zeichen enthalten")
    private String versandKzf = "";
    @Size(min = 0, max = 2, message = "Satzart muss zwischen 0 und 2 Zeichen enthalten")
    private String satzart = "";
    @Size(min = 0, max = 8, message = "ErfDat muss zwischen 0 und 8 Zeichen enthalten")
    private String erfDat = "";
    @Size(min = 0, max = 3, message = "ErfBea muss zwischen 1 und 3 Zeichen enthalten")
    private String erfBea = "";
    @Size(min = 0, max = 3, message = "DruckBea muss zwischen 1 und 3 Zeichen enthalten")
    private String druckBea = "";
    @Max(99)
    private int weitergabeNr1; // int
    @Max(99)
    private int weitergabeNr2; // int
    @Size(min = 0, max = 1, message = "DruckKzf muss zwischen 0 und 1 Zeichen enthalten")
    private String druckKzf = "";
    @Size(min = 0, max = 1, message = "Teilnahmeart muss zwischen 0 und 1 Zeichen enthalten")
    private String teilnahmeart = "";
    @Size(min = 0, max = 1, message = "SatzKzf muss zwischen 0 und 1 Zeichen enthalten")
    private String satzKzf = "";
    @Max(999)
    private int hstNr = 0; // int
    @Max(99999)
    private int referenzEKNr = 0; // int
    @Size(min = 0, max = 20, message = "Hinweis muss zwischen 1 und 20 Zeichen enthalten")
    private String hinweis = "";
    @Size(min = 0, max = 12, message = "ISIN muss zwischen 0 und 12 Zeichen enthalten")
    private String isin = "";
    @Size(min = 0, max = 1, message = "InternetKzf muss zwischen 1 und 1 Zeichen enthalten")
    private String internetKzf = "";
    @Size(min = 0, max = 100, message = "InternetAdr muss zwischen 1 und 100 Zeichen enthalten")
    private String internetAdr = "";
    @Size(min = 0, max = 100, message = "HinweisWeitergabe muss zwischen 0 und 100 Zeichen enthalten")
    private String hinweisWeitergabe = "";
    @Size(min = 0, max = 30, message = "Reserve muss zwischen 0 und 30 Zeichen enthalten")
    private String reserve = "";

    /*
     * Empfaenger Daten nur in Excel enthalten
     */
    @Size(min = 0, max = 1, message = "EmpfAnrede muss zwischen 1 und 15 Zeichen enthalten")
    private String empfAnrede = "";
    @Size(min = 0, max = 1, message = "EmpfKzf muss zwischen 0 und 1 Zeichen enthalten")
    private String empfKzf = "";
    @Size(min = 0, max = 50, message = "EmpfNachname muss zwischen 0 und 50 Zeichen enthalten")
    private String empfNachname = "";
    @Size(min = 0, max = 50, message = "EmpfVorname muss zwischen 0 und 50 Zeichen enthalten")
    private String empfVorname = "";
    @Size(min = 0, max = 30, message = "EmpfAkadGrad muss zwischen 0 und 30 Zeichen enthalten")
    private String empfAkadGrad = "";
    @Size(min = 0, max = 30, message = "EmpfAdelstitel muss zwischen 0 und 30 Zeichen enthalten")
    private String empfAdelstitel = "";
    @Size(min = 0, max = 30, message = "EmpfStrasse muss zwischen 0 und 30 Zeichen enthalten")
    private String empfStrasse = "";
    @Size(min = 0, max = 10, message = "EmpfPLZ muss zwischen01 und 10 Zeichen enthalten")
    private String empfPLZ = "";
    @Size(min = 0, max = 80, message = "EmpfOrt muss zwischen 0 und 80 Zeichen enthalten")
    private String empfOrt = "";
    @Size(min = 0, max = 3, message = "EmpfLandKzf muss zwischen 0 und 3 Zeichen enthalten")
    private String empfLandKzf = "";

    private int gattungId;
    @Size(min = 1, max = 80, message = "Dateiname muss zwischen 1 und 80 Zeichen enthalten")
    private String datei;
    @Size(min = 1, max = 15, message = "Dateikürzel muss zwischen 1 und 15 Zeichen enthalten")
    private String dateikuerzel;

    private String[] abgabe = new String[200];

    private String abstimmung = "";

    public EclInhaberImportAnmeldedaten() {

    }

    public EclInhaberImportAnmeldedaten(String line) throws NumberFormatException {

        this.ekNr = Integer.parseInt(line.substring(0, 5).trim());
        this.pruefKennz = line.substring(5, 6).trim();
        this.anredeKzf = Integer.parseInt((line.substring(6, 7).trim().isBlank()) ? "0" : line.substring(6, 7).trim());
        this.fadrNr = Integer.parseInt((line.substring(7, 10).trim().isBlank()) ? "0" : line.substring(7, 10).trim());
        this.akadGrad = line.substring(10, 20).trim();
        this.adelstitel1 = line.substring(20, 30).trim();
        this.adelstitel2 = line.substring(30, 40).trim();
        this.vorname = line.substring(40, 55).trim();
        this.nachname = line.substring(55, 85).trim();
        this.anmeldung = line.substring(85, 130).trim();
        this.strasse = line.substring(130, 155).trim();
        this.plz = line.substring(155, 160).trim();
        this.ort = line.substring(160, 185).trim();
        this.version = line.substring(185, 188).trim();
        this.landKzf = line.substring(188, 191).trim();
        this.land = line.substring(191, 211).trim();
        this.vertreterKzf = line.substring(211, 212).trim();
        this.stueck = Integer
                .parseInt((line.substring(212, 221).trim().isBlank()) ? "0" : line.substring(212, 221).trim());
        this.nominal = Integer
                .parseInt((line.substring(221, 232).trim().isBlank()) ? "0" : line.substring(221, 232).trim());
        this.stimmenanteil = Integer
                .parseInt((line.substring(232, 241).trim().isBlank()) ? "0" : line.substring(232, 241).trim());
        this.besitzMm = line.substring(241, 242).trim();
        this.aktiengattung = line.substring(242, 243).trim();
        this.arvs = line.substring(243, 244).trim();
        this.statistikMm = line.substring(244, 246).trim();
        this.depotNr = line.substring(246, 266).trim();
        this.wkn = line.substring(266, 272).trim();
        this.nennwert = Integer
                .parseInt((line.substring(272, 275).trim().isBlank()) ? "0" : line.substring(272, 275).trim());
        this.versandKzf = line.substring(275, 276).trim();
        this.satzart = line.substring(276, 278).trim();
        this.erfDat = line.substring(278, 286).trim();
        this.erfBea = line.substring(286, 289).trim();
        this.druckBea = line.substring(289, 292).trim();
        this.weitergabeNr1 = Integer
                .parseInt((line.substring(292, 294).trim().isBlank()) ? "0" : line.substring(292, 294).trim());
        this.weitergabeNr2 = Integer
                .parseInt((line.substring(294, 296).trim().isBlank()) ? "0" : line.substring(294, 296).trim());
        this.druckKzf = line.substring(296, 297).trim();
        this.teilnahmeart = line.substring(297, 298).trim();
        this.satzKzf = line.substring(298, 299).trim();
        this.hstNr = Integer
                .parseInt((line.substring(299, 302).trim().isBlank()) ? "0" : line.substring(299, 302).trim());
        this.referenzEKNr = Integer
                .parseInt((line.substring(302, 307).trim().isBlank()) ? "0" : line.substring(302, 307).trim());
        this.hinweis = line.substring(307, 327).trim();
        this.isin = line.substring(327, 339).trim();
        this.internetKzf = line.substring(339, 340).trim();
        this.internetAdr = line.substring(340, 440).trim();
        this.hinweisWeitergabe = line.substring(440, 470).trim();
        this.reserve = line.substring(470, 500).trim();

    }

    public EclInhaberImportAnmeldedaten(int ident, @Max(99999) int ekNr, @Size(min = 0, max = 1) String pruefKennz,
            @Max(9) int anredeKzf, @Max(999) int fadrNr, @Size(min = 0, max = 30) String akadGrad,
            @Size(min = 0, max = 30) String adelstitel1, @Size(min = 0, max = 10) String adelstitel2,
            @Size(min = 0, max = 50) String vorname, @Size(min = 0, max = 80) String nachname,
            @Size(min = 0, max = 45) String anmeldung, @Size(min = 0, max = 255) String adresse,
            @Size(min = 0, max = 30) String strasse, @Size(min = 0, max = 80) String adresszusatz,
            @Size(min = 0, max = 10) String plz, @Size(min = 0, max = 50) String ort,
            @Size(min = 0, max = 3) String version, @Size(min = 0, max = 3) String landKzf,
            @Size(min = 0, max = 20) String land, @Size(min = 0, max = 1) String vertreterKzf, @Positive int stueck,
            int nominal, @Max(999999999) int stimmenanteil, @Size(min = 0, max = 1) String besitzMm,
            @Size(min = 0, max = 1) String aktiengattung, @Size(min = 0, max = 1) String arvs,
            @Size(min = 0, max = 2) String statistikMm, @Size(min = 0, max = 20) String depotNr,
            @Size(min = 0, max = 6) String wKN, @Max(999) int nennwert, @Size(min = 0, max = 1) String versandKzf,
            @Size(min = 0, max = 2) String satzart, String erfDat, @Size(min = 0, max = 3) String erfBea,
            @Size(min = 0, max = 3) String druckBea, @Max(99) int weitergabeNr1, @Max(99) int weitergabeNr2,
            @Size(min = 0, max = 1) String druckKzf, @Size(min = 0, max = 1) String teilnahmeart,
            @Size(min = 0, max = 1) String satzKzf, @Max(999) int hstNr, @Max(99999) int referenzEKNr,
            @Size(min = 0, max = 20) String hinweis, @Size(min = 0, max = 12) String isin,
            @Size(min = 0, max = 1) String internetKzf, @Size(min = 0, max = 100) String internetAdr,
            @Size(min = 0, max = 100) String hinweisWeitergabe, @Size(min = 0, max = 30) String reserve,
            @Size(min = 0, max = 1) String empfAnrede, @Size(min = 0, max = 1) String empfKzf,
            @Size(min = 0, max = 50) String empfNachname, @Size(min = 0, max = 50) String empfVorname,
            @Size(min = 0, max = 30) String empfAkadGrad, @Size(min = 0, max = 30) String empfAdelstitel,
            @Size(min = 0, max = 30) String empfStrasse, @Size(min = 0, max = 10) String empfPLZ,
            @Size(min = 0, max = 50) String empfOrt, @Size(min = 0, max = 3) String empfLandKzf, int gattungId,
            String datei, String dateikuerzel, String[] abgabe) {
        super();
        this.ident = ident;
        this.ekNr = ekNr;
        this.pruefKennz = pruefKennz;
        this.anredeKzf = anredeKzf;
        this.fadrNr = fadrNr;
        this.akadGrad = akadGrad;
        this.adelstitel1 = adelstitel1;
        this.adelstitel2 = adelstitel2;
        this.vorname = vorname;
        this.nachname = nachname;
        this.anmeldung = anmeldung;
        this.adresse = adresse;
        this.strasse = strasse;
        this.adresszusatz = adresszusatz;
        this.plz = plz;
        this.ort = ort;
        this.version = version;
        this.landKzf = landKzf;
        this.land = land;
        this.vertreterKzf = vertreterKzf;
        this.stueck = stueck;
        this.nominal = nominal;
        this.stimmenanteil = stimmenanteil;
        this.besitzMm = besitzMm;
        this.aktiengattung = aktiengattung;
        this.arvs = arvs;
        this.statistikMm = statistikMm;
        this.depotNr = depotNr;
        this.wkn = wKN;
        this.nennwert = nennwert;
        this.versandKzf = versandKzf;
        this.satzart = satzart;
        this.erfDat = erfDat;
        this.erfBea = erfBea;
        this.druckBea = druckBea;
        this.weitergabeNr1 = weitergabeNr1;
        this.weitergabeNr2 = weitergabeNr2;
        this.druckKzf = druckKzf;
        this.teilnahmeart = teilnahmeart;
        this.satzKzf = satzKzf;
        this.hstNr = hstNr;
        this.referenzEKNr = referenzEKNr;
        this.hinweis = hinweis;
        this.isin = isin;
        this.internetKzf = internetKzf;
        this.internetAdr = internetAdr;
        this.hinweisWeitergabe = hinweisWeitergabe;
        this.reserve = reserve;
        this.empfAnrede = empfAnrede;
        this.empfKzf = empfKzf;
        this.empfNachname = empfNachname;
        this.empfVorname = empfVorname;
        this.empfAkadGrad = empfAkadGrad;
        this.empfAdelstitel = empfAdelstitel;
        this.empfStrasse = empfStrasse;
        this.empfPLZ = empfPLZ;
        this.empfOrt = empfOrt;
        this.empfLandKzf = empfLandKzf;
        this.gattungId = gattungId;
        this.datei = datei;
        this.dateikuerzel = dateikuerzel;
        this.abgabe = abgabe;
        this.abstimmung = abgabeToAnzeige();
    }

    public static EclInhaberImportAnmeldedaten createAnmeldedaten(Map<String, String> map) {

        EclInhaberImportAnmeldedaten anm = new EclInhaberImportAnmeldedaten();

        for (var entry : map.entrySet()) {
            final int length = entry.getValue().length();
            final String value = entry.getKey().contains("abgabe") ? "abgabe" : entry.getKey().trim();
            final int x = entry.getKey().contains("abgabe") ? Integer.valueOf(entry.getKey().replace("abgabe", "")) : 0;

            //            System.out.println(value + " / " + entry.getValue());

            switch (value) {
            case "ekNr":
                anm.ekNr = Integer.parseInt(entry.getValue());
                break;
            case "pruefKennz":
                anm.pruefKennz = entry.getValue();
                break;
            case "anredeKzf":
                anm.anredeKzf = Integer.parseInt(entry.getValue());
                break;
            case "fadrNr":
                anm.fadrNr = Integer.parseInt(entry.getValue());
                break;
            case "akadGrad":
                anm.akadGrad = entry.getValue();
                break;
            case "adelstitel1":
                anm.adelstitel1 = entry.getValue();
                break;
            case "adelstitel2":
                anm.adelstitel2 = entry.getValue();
                break;
            case "vorname":
                anm.vorname = entry.getValue();
                break;
            case "nachname":
                anm.nachname = entry.getValue();
                break;
            case "anmeldung":
                anm.anmeldung = entry.getValue();
                break;
            case "adresse":
                anm.adresse = entry.getValue();
                break;
            case "strasse":
                anm.strasse = entry.getValue();
                break;
            case "adresszusatz":
                anm.adresszusatz = entry.getValue();
                break;
            case "plz":
                anm.plz = entry.getValue();
                break;
            case "ort":
                anm.ort = length > 80 ? entry.getValue().substring(length - 80, length) : entry.getValue();
                break;
            case "version":
                anm.version = entry.getValue();
                break;
            case "landKzf":
                anm.landKzf = entry.getValue();
                break;
            case "land":
                anm.land = entry.getValue();
                break;
            case "vertreterKzf":
                anm.versandKzf = entry.getValue();
                break;
            case "stueck":
                anm.stueck = Integer.parseInt(entry.getValue());
                break;
            case "nominal":
                anm.nominal = Integer.parseInt(entry.getValue());
                break;
            case "stimmenanteil":
                anm.stimmenanteil = Integer.parseInt(entry.getValue());
                break;
            case "besitzMm":
                anm.besitzMm = entry.getValue();
                break;
            case "aktiengattung":
                anm.aktiengattung = entry.getValue();
                break;
            case "arvs":
                anm.arvs = entry.getValue();
                break;
            case "statistikMm":
                anm.statistikMm = entry.getValue();
                break;
            case "depotNr":
                anm.depotNr = entry.getValue();
                break;
            case "wkn":
                anm.wkn = entry.getValue();
                break;
            case "nennwert":
                anm.nennwert = Integer.parseInt(entry.getValue());
                break;
            case "versandKzf":
                anm.versandKzf = entry.getValue();
                break;
            case "satzart":
                anm.satzart = entry.getValue();
                break;
            case "erfDat":
                anm.erfDat = entry.getValue();
                break;
            case "erfBea":
                anm.erfBea = entry.getValue();
                break;
            case "druckBea":
                anm.druckBea = entry.getValue();
                break;
            case "weitergabeNr1":
                anm.weitergabeNr1 = Integer.parseInt(entry.getValue());
                break;
            case "weitergabeNr2":
                anm.weitergabeNr2 = Integer.parseInt(entry.getValue());
                break;
            case "druckKzf":
                anm.druckKzf = entry.getValue();
                break;
            case "teilnahmeart":
                anm.teilnahmeart = entry.getValue();
                break;
            case "satzKzf":
                anm.satzKzf = entry.getValue();
                break;
            case "hstNr":
                anm.hstNr = Integer.parseInt(entry.getValue());
                break;
            case "referenzEKNr":
                anm.referenzEKNr = Integer.parseInt(entry.getValue());
                break;
            case "hinweis":
                anm.hinweis = entry.getValue();
                break;
            case "isin":
                anm.isin = entry.getValue();
                break;
            case "internetKzf":
                anm.internetKzf = entry.getValue();
                break;
            case "internetAdr":
                anm.internetAdr = entry.getValue();
                break;
            case "hinweisWeitergabe":
                anm.hinweisWeitergabe = entry.getValue();
                break;
            case "reserve":
                anm.reserve = entry.getValue();
                break;
            case "empfAnrede":
                anm.empfAnrede = entry.getValue();
                break;
            case "empfKzf":
                anm.empfKzf = entry.getValue();
                break;
            case "empfNachname":
                anm.empfNachname = entry.getValue();
                break;
            case "empfVorname":
                anm.empfVorname = entry.getValue();
                break;
            case "empfAkadGrad":
                anm.empfAkadGrad = entry.getValue();
                break;
            case "empfAdelstitel":
                anm.empfAdelstitel = entry.getValue();
                break;
            case "empfStrasse":
                anm.empfStrasse = entry.getValue();
                break;
            case "empfPLZ":
                anm.empfStrasse = entry.getValue();
                break;
            case "empfOrt":
                anm.empfOrt = length > 80 ? entry.getValue().substring(length - 80, length) : entry.getValue();
                break;
            case "empfLandKzf":
                anm.empfLandKzf = entry.getValue();
                break;
            case "abgabe":
                anm.abgabe[x] = convertAbgabe(entry.getValue());
            default:
                break;
            }

        }
        anm.abstimmung = anm.abgabeToAnzeige();

        //        System.out.println(anm.toString());        
        return anm;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public int getEkNr() {
        return ekNr;
    }

    public void setEkNr(int ekNr) {
        this.ekNr = ekNr;
    }

    public String getPruefKennz() {
        return pruefKennz;
    }

    public void setPruefKennz(String pruefKennz) {
        this.pruefKennz = pruefKennz;
    }

    public int getAnredeKzf() {
        return anredeKzf;
    }

    public void setAnredeKzf(int anredeKzf) {
        this.anredeKzf = anredeKzf;
    }

    public int getFadrNr() {
        return fadrNr;
    }

    public void setFadrNr(int fadrNr) {
        this.fadrNr = fadrNr;
    }

    public String getAkadGrad() {
        return akadGrad;
    }

    public void setAkadGrad(String akadGrad) {
        this.akadGrad = akadGrad;
    }

    public String getAdelstitel1() {
        return adelstitel1;
    }

    public void setAdelstitel1(String adelstitel1) {
        this.adelstitel1 = adelstitel1;
    }

    public String getAdelstitel2() {
        return adelstitel2;
    }

    public void setAdelstitel2(String adelstitel2) {
        this.adelstitel2 = adelstitel2;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getAnmeldung() {
        return anmeldung;
    }

    public void setAnmeldung(String anmeldung) {
        this.anmeldung = anmeldung;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getAdresszusatz() {
        return adresszusatz;
    }

    public void setAdresszusatz(String adresszusatz) {
        this.adresszusatz = adresszusatz;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLandKzf() {
        return landKzf;
    }

    public void setLandKzf(String landKzf) {
        this.landKzf = landKzf;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getVertreterKzf() {
        return vertreterKzf;
    }

    public void setVertreterKzf(String vertreterKzf) {
        this.vertreterKzf = vertreterKzf;
    }

    public int getStueck() {
        return stueck;
    }

    public void setStueck(int stueck) {
        this.stueck = stueck;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public int getStimmenanteil() {
        return stimmenanteil;
    }

    public void setStimmenanteil(int stimmenanteil) {
        this.stimmenanteil = stimmenanteil;
    }

    public String getBesitzMm() {
        return besitzMm;
    }

    public void setBesitzMm(String besitzMm) {
        this.besitzMm = besitzMm;
    }

    public String getAktiengattung() {
        return aktiengattung;
    }

    public void setAktiengattung(String aktiengattung) {
        this.aktiengattung = aktiengattung;
    }

    public String getArvs() {
        return arvs;
    }

    public void setArvs(String arvs) {
        this.arvs = arvs;
    }

    public String getStatistikMm() {
        return statistikMm;
    }

    public void setStatistikMm(String statistikMm) {
        this.statistikMm = statistikMm;
    }

    public String getDepotNr() {
        return depotNr;
    }

    public void setDepotNr(String depotNr) {
        this.depotNr = depotNr;
    }

    public String getWkn() {
        return wkn;
    }

    public void setWkn(String wkn) {
        this.wkn = wkn;
    }

    public int getNennwert() {
        return nennwert;
    }

    public void setNennwert(int nennwert) {
        this.nennwert = nennwert;
    }

    public String getVersandKzf() {
        return versandKzf;
    }

    public void setVersandKzf(String versandKzf) {
        this.versandKzf = versandKzf;
    }

    public String getSatzart() {
        return satzart;
    }

    public void setSatzart(String satzart) {
        this.satzart = satzart;
    }

    public String getErfDat() {
        return erfDat;
    }

    public void setErfDat(String erfDat) {
        this.erfDat = erfDat;
    }

    public String getErfBea() {
        return erfBea;
    }

    public void setErfBea(String erfBea) {
        this.erfBea = erfBea;
    }

    public String getDruckBea() {
        return druckBea;
    }

    public void setDruckBea(String druckBea) {
        this.druckBea = druckBea;
    }

    public int getWeitergabeNr1() {
        return weitergabeNr1;
    }

    public void setWeitergabeNr1(int weitergabeNr1) {
        this.weitergabeNr1 = weitergabeNr1;
    }

    public int getWeitergabeNr2() {
        return weitergabeNr2;
    }

    public void setWeitergabeNr2(int weitergabeNr2) {
        this.weitergabeNr2 = weitergabeNr2;
    }

    public String getDruckKzf() {
        return druckKzf;
    }

    public void setDruckKzf(String druckKzf) {
        this.druckKzf = druckKzf;
    }

    public String getTeilnahmeart() {
        return teilnahmeart;
    }

    public void setTeilnahmeart(String teilnahmeart) {
        this.teilnahmeart = teilnahmeart;
    }

    public String getSatzKzf() {
        return satzKzf;
    }

    public void setSatzKzf(String satzKzf) {
        this.satzKzf = satzKzf;
    }

    public int getHstNr() {
        return hstNr;
    }

    public void setHstNr(int hstNr) {
        this.hstNr = hstNr;
    }

    public int getReferenzEKNr() {
        return referenzEKNr;
    }

    public void setReferenzEKNr(int referenzEKNr) {
        this.referenzEKNr = referenzEKNr;
    }

    public String getHinweis() {
        return hinweis;
    }

    public void setHinweis(String hinweis) {
        this.hinweis = hinweis;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getInternetKzf() {
        return internetKzf;
    }

    public void setInternetKzf(String internetKzf) {
        this.internetKzf = internetKzf;
    }

    public String getInternetAdr() {
        return internetAdr;
    }

    public void setInternetAdr(String internetAdr) {
        this.internetAdr = internetAdr;
    }

    public String getHinweisWeitergabe() {
        return hinweisWeitergabe;
    }

    public void setHinweisWeitergabe(String hinweisWeitergabe) {
        this.hinweisWeitergabe = hinweisWeitergabe;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getEmpfAnrede() {
        return empfAnrede;
    }

    public void setEmpfAnrede(String empfAnrede) {
        this.empfAnrede = empfAnrede;
    }

    public String getEmpfKzf() {
        return empfKzf;
    }

    public void setEmpfKzf(String empfKzf) {
        this.empfKzf = empfKzf;
    }

    public String getEmpfNachname() {
        return empfNachname;
    }

    public void setEmpfNachname(String empfNachname) {
        this.empfNachname = empfNachname;
    }

    public String getEmpfVorname() {
        return empfVorname;
    }

    public void setEmpfVorname(String empfVorname) {
        this.empfVorname = empfVorname;
    }

    public String getEmpfAkadGrad() {
        return empfAkadGrad;
    }

    public void setEmpfAkadGrad(String empfAkadGrad) {
        this.empfAkadGrad = empfAkadGrad;
    }

    public String getEmpfAdelstitel() {
        return empfAdelstitel;
    }

    public void setEmpfAdelstitel(String empfAdelstitel) {
        this.empfAdelstitel = empfAdelstitel;
    }

    public String getEmpfStrasse() {
        return empfStrasse;
    }

    public void setEmpfStrasse(String empfStrasse) {
        this.empfStrasse = empfStrasse;
    }

    public String getEmpfPLZ() {
        return empfPLZ;
    }

    public void setEmpfPLZ(String empfPLZ) {
        this.empfPLZ = empfPLZ;
    }

    public String getEmpfOrt() {
        return empfOrt;
    }

    public void setEmpfOrt(String empfOrt) {
        this.empfOrt = empfOrt;
    }

    public String getEmpfLandKzf() {
        return empfLandKzf;
    }

    public void setEmpfLandKzf(String empfLandKzf) {
        this.empfLandKzf = empfLandKzf;
    }

    public int getGattungId() {
        return gattungId;
    }

    public void setGattungId(int gattungId) {
        this.gattungId = gattungId;
    }

    public String getDatei() {
        return datei;
    }

    public void setDatei(String datei) {
        this.datei = datei;
    }

    public String getDateikuerzel() {
        return dateikuerzel;
    }

    public void setDateikuerzel(String dateikuerzel) {
        this.dateikuerzel = dateikuerzel;
    }

    public String[] getAbgabe() {
        return abgabe;
    }

    public void setAbgabe(String[] abgabe) {
        this.abgabe = abgabe;
    }

    public String getAbstimmung() {
        return abstimmung;
    }

    public void setAbstimmung(String anzeigeAbgabe) {
        this.abstimmung = anzeigeAbgabe;
    }

    public static String convertAbgabe(String stimmart) {

        return switch (stimmart.toUpperCase()) {
        case "J", "Y", "F", "JA", "YES", "FOR", "IN FAVOUR", "VOTEOPTION - FOR" -> "J";
        case "N", "NEIN", "NO", "AGAINST", "VOTEOPTION - AGAINST" -> "N";
        case "E", "A", "ENTHALTUNG", "ABSTAIN", "VOTEOPTION - ABSTAIN" -> "E";
        default -> "U";
        };
    }

    public String abgabeToString() {
        String erg = "";
        int i = 0;

        for (String str : abgabe) {
            if (str != null)
                erg += str;
            else if (i > 0)
                return erg;
            else
                i++;
        }
        return erg;
    }

    public String abgabeToAnzeige() {
        String erg = "";
        int i = 0;

        for (String str : abgabe) {
            if (str != null)
                erg += str + " - ";
            else if (i > 0)
                return erg.endsWith(" - ") ? erg.substring(0, erg.length() - 3) : erg;
            else
                i++;

        }
        return erg;
    }

    @Override
    public String toString() {
        return "EclInhaberImportAnmeldedaten [ident=" + ident + ", ekNr=" + ekNr + ", pruefKennz=" + pruefKennz + ", anredeKzf="
                + anredeKzf + ", fadrNr=" + fadrNr + ", akadGrad=" + akadGrad + ", adelstitel1=" + adelstitel1
                + ", adelstitel2=" + adelstitel2 + ", vorname=" + vorname + ", nachname=" + nachname + ", anmeldung="
                + anmeldung + ", adresse=" + adresse + ", strasse=" + strasse + ", adresszusatz=" + adresszusatz
                + ", plz=" + plz + ", ort=" + ort + ", version=" + version + ", landKzf=" + landKzf + ", land=" + land
                + ", vertreterKzf=" + vertreterKzf + ", stueck=" + stueck + ", nominal=" + nominal + ", stimmenanteil="
                + stimmenanteil + ", besitzMm=" + besitzMm + ", aktiengattung=" + aktiengattung + ", arvs=" + arvs
                + ", statistikMm=" + statistikMm + ", depotNr=" + depotNr + ", wkn=" + wkn + ", nennwert=" + nennwert
                + ", versandKzf=" + versandKzf + ", satzart=" + satzart + ", erfDat=" + erfDat + ", erfBea=" + erfBea
                + ", druckBea=" + druckBea + ", weitergabeNr1=" + weitergabeNr1 + ", weitergabeNr2=" + weitergabeNr2
                + ", druckKzf=" + druckKzf + ", teilnahmeart=" + teilnahmeart + ", satzKzf=" + satzKzf + ", hstNr="
                + hstNr + ", referenzEKNr=" + referenzEKNr + ", hinweis=" + hinweis + ", isin=" + isin
                + ", internetKzf=" + internetKzf + ", internetAdr=" + internetAdr + ", hinweisWeitergabe="
                + hinweisWeitergabe + ", reserve=" + reserve + ", empfAnrede=" + empfAnrede + ", empfKzf=" + empfKzf
                + ", empfNachname=" + empfNachname + ", empfVorname=" + empfVorname + ", empfAkadGrad=" + empfAkadGrad
                + ", empfAdelstitel=" + empfAdelstitel + ", empfStrasse=" + empfStrasse + ", empfPLZ=" + empfPLZ
                + ", empfOrt=" + empfOrt + ", empfLandKzf=" + empfLandKzf + ", gattungId=" + gattungId + ", datei="
                + datei + ", dateikuerzel=" + dateikuerzel + "]";
    }

    public String searchString() {
        return ident + " " + ekNr + " " + pruefKennz + " " + anredeKzf + " " + fadrNr + " " + akadGrad + " "
                + adelstitel1 + " " + adelstitel2 + " " + vorname + " " + nachname + " " + anmeldung + " " + strasse
                + " " + plz + " " + ort + " " + version + " " + landKzf + " " + land + " " + vertreterKzf + " " + stueck
                + " " + nominal + " " + stimmenanteil + " " + besitzMm + " " + aktiengattung + " " + arvs + " "
                + statistikMm + " " + depotNr + " " + wkn + " " + nennwert + " " + versandKzf + " " + satzart + " "
                + erfDat + " " + erfBea + " " + druckBea + " " + weitergabeNr1 + " " + weitergabeNr2 + " " + druckKzf
                + " " + teilnahmeart + " " + satzKzf + " " + hstNr + " " + referenzEKNr + " " + hinweis + " " + isin
                + " " + internetKzf + " " + internetAdr + " " + hinweisWeitergabe + " " + reserve + " " + empfAnrede
                + " " + empfKzf + " " + empfNachname + " " + empfVorname + " " + empfAkadGrad + " " + empfAdelstitel
                + " " + empfStrasse + " " + empfPLZ + " " + empfOrt + " " + empfLandKzf + " " + datei + " "
                + dateikuerzel;
    }

    @Override
    public int compareTo(EclInhaberImportAnmeldedaten o) {
        return Integer.compare(this.ekNr, o.ekNr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adresse, adresszusatz, aktiengattung, anmeldung, depotNr, ekNr, empfNachname, empfOrt,
                empfPLZ, empfStrasse, empfVorname, gattungId, isin, nachname, ort, plz, strasse, stueck, vorname, wkn);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EclInhaberImportAnmeldedaten other = (EclInhaberImportAnmeldedaten) obj;
        return Objects.equals(adresse, other.adresse) && Objects.equals(adresszusatz, other.adresszusatz)
                && Objects.equals(aktiengattung, other.aktiengattung) && Objects.equals(anmeldung, other.anmeldung)
                && Objects.equals(depotNr, other.depotNr) && ekNr == other.ekNr
                && Objects.equals(empfNachname, other.empfNachname) && Objects.equals(empfOrt, other.empfOrt)
                && Objects.equals(empfPLZ, other.empfPLZ) && Objects.equals(empfStrasse, other.empfStrasse)
                && Objects.equals(empfVorname, other.empfVorname) && gattungId == other.gattungId
                && Objects.equals(isin, other.isin) && Objects.equals(nachname, other.nachname)
                && Objects.equals(ort, other.ort) && Objects.equals(plz, other.plz)
                && Objects.equals(strasse, other.strasse) && stueck == other.stueck
                && Objects.equals(vorname, other.vorname) && Objects.equals(wkn, other.wkn);
    }

}
