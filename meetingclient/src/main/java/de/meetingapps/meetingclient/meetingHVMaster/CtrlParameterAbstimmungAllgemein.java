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
package de.meetingapps.meetingclient.meetingHVMaster;

import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungZuAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarteInhalt;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstDBAbstimmungen;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import javafx.scene.control.ComboBox;

/**
 * The Class CtrlParameterAbstimmungAllgemein.
 */
public class CtrlParameterAbstimmungAllgemein {

    /** *************Belegen mit****************************. */
    public static String stimmenBelegenMit[] = { ".", "J", "N", "E" };

    /**
     * Fuelle cb stimmen belegen mit.
     *
     * @param cbStimmenBelegen the cb stimmen belegen
     * @param wert             the wert
     */
    public static void fuelleCbStimmenBelegenMit(ComboBox<String> cbStimmenBelegen, int wert) {
        cbStimmenBelegen.getItems().clear();
        for (int i1 = 0; i1 < stimmenBelegenMit.length; i1++) {
            cbStimmenBelegen.getItems().addAll(stimmenBelegenMit[i1]);
            if (i1 == wert) {
                cbStimmenBelegen.setValue(stimmenBelegenMit[i1]);
            }
        }
    }

    /**
     * Liefere auswahl cb stimmen belegen.
     *
     * @param cbStimmenBelegen the cb stimmen belegen
     * @return the int
     */
    public static int liefereAuswahlCbStimmenBelegen(ComboBox<String> cbStimmenBelegen) {
        String hString = cbStimmenBelegen.getValue();
        return KonstStimmart.getIntVonTextKurz(hString);
    }

    /** *************Belegen mit JNEU****************************. */
    public static String stimmenBelegenMitJNEU[] = { "J", "N", "E", "U" };

    /**
     * Fuelle cb stimmen belegen mit JNEU.
     *
     * @param cbStimmenBelegen the cb stimmen belegen
     * @param wert             the wert
     */
    public static void fuelleCbStimmenBelegenMitJNEU(ComboBox<String> cbStimmenBelegen, int wert) {
        cbStimmenBelegen.getItems().clear();
        for (int i1 = 0; i1 < stimmenBelegenMit.length; i1++) {
            cbStimmenBelegen.getItems().addAll(stimmenBelegenMit[i1]);
            if (stimmenBelegenMitJNEU[i1].equals(KonstStimmart.getTextKurz(wert))) {
                cbStimmenBelegen.setValue(stimmenBelegenMitJNEU[i1]);
            }
        }
    }

    /**
     * Liefere auswahl cb stimmen belegen mit JNEU.
     *
     * @param cbStimmenBelegen the cb stimmen belegen
     * @return the int
     */
    public static int liefereAuswahlCbStimmenBelegenMitJNEU(ComboBox<String> cbStimmenBelegen) {
        String hString = cbStimmenBelegen.getValue();
        return KonstStimmart.getIntVonTextKurz(hString);
    }

    /** *************Stimmen auswerten******************. */
    public static String stimmenAuswertenMoeglichkeiten[] = { "A", "J", "N", "E" };

    /**
     * wert=0 oder -1 => Additionsverfahren.
     *
     * @param cbStimmenAuswerten the cb stimmen auswerten
     * @param wert               the wert
     */
    public static void fuelleCbStimmenAuswerten(ComboBox<String> cbStimmenAuswerten, int wert) {
        cbStimmenAuswerten.getItems().clear();
        for (int i1 = 0; i1 < stimmenAuswertenMoeglichkeiten.length; i1++) {
            cbStimmenAuswerten.getItems().addAll(stimmenAuswertenMoeglichkeiten[i1]);
            if (i1 == wert) {
                cbStimmenAuswerten.setValue(stimmenAuswertenMoeglichkeiten[i1]);
            }
        }
        if (wert == -1) {
            cbStimmenAuswerten.setValue(stimmenAuswertenMoeglichkeiten[0]);
        }
    }

    /**
     * Additionsverfahren => -1 wird geliefert.
     *
     * @param cbStimmenAuswerten the cb stimmen auswerten
     * @return the int
     */
    public static int liefereAuswahlCbStimmenAuswerten(ComboBox<String> cbStimmenAuswerten) {
        String hString = cbStimmenAuswerten.getValue();
        if (hString.equals("A")) {
            return -1;
        }
        return KonstStimmart.getIntVonTextKurz(hString);
    }
    
//  @formatter:off
    /** ***********Erforderliche Mehrheit***********************. */
    public static String cbErforderlicheMehrheitMoeglichkeiten[] = {
            "(0) undefiniert",
            "(1) einf.Stimmen(>50%)",
            "(2) einf.Kapital(>50%)+einf.Stimmen(>50%)",
            "(4) qual.Kapital(75%)",
            "(5) qual.Kapital(75%)+einf.Stimmen(>50%)",
            "(6) qual.Stimmen(75%)",
            "(7) qual.Stimmen+qual.Kapital(75%)",
            "(8) (Kapital >75%)",
            "(9) (Kapital >75% +einf.Stimmen)",
            "(10) (Stimmen >75%)",
            "(11) (Stimmen+Kapital>75%)",
            "(101) (je Gat.einf.Stimmen(>50%))",
            "(102) (je Gat.einf.Kapital(>50%)+einf.Stimmen(>50%))",
            "(104) (je Gat.qual.Kapital(75%))",
            "(105) (je Gat.qual.Kapital(75%)+einf.Stimmen(>50%))",
            "(106) (je Gat.qual.Stimmen(75%))",
            "(107) (je Gat.qual.Stimmen+qual.Kapital(75%))",
            "(108) (je Gat.Kapital >75%))",
            "(109) (je Gat.Kapital >75% +einf.Stimmen))",
            "(110) (je Gat.Stimmen >75%))",
            "(111) (je Gat.Stimmen+Kapital>75%))"
 //            "(0) undefiniert", 
//            "(1) einfache Stimmen-Mehrheit",
//            "(2) einfache Kapital-Mehrheit", 
//            "(3) einfache Stimmen-Mehrheit (je Gattung)",
//            "(4) einfache Kapital-Mehrheit (je Gattung)", 
//            "(5) 3/4 Stimmen-Mehrheit", 
//            "(6) 3/4 Kapital-Mehrheit",
//            "(7) 3/4 Stimmen-Mehrheit (je Gattung)", 
//            "(8) 3/4 Kapital-Mehrheit (je Gattung)",
//            "(9) > 3/4 Stimmen-Mehrheit", 
//            "(10) > 3/4 Kapital-Mehrheit", 
//            "(11) > 3/4 Stimmen-Mehrheit (je Gattung)",
//            "(12) > 3/4 Kapital-Mehrheit (je Gattung)" 
            };
    public static boolean cbErforderlicheMehrheitMoeglichkeitenVerwendenBei1Gattung[] = {
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false
    };

//  @formatter:on

    /**
     * Fuelle cb erforderliche mehrheit.
     *
     * @param cbErforderlicheMehrheit the cb erforderliche mehrheit
     * @param wert                    the wert
     */
    public static void fuelleCbErforderlicheMehrheit(ComboBox<String> cbErforderlicheMehrheit, int wert) {
        cbErforderlicheMehrheit.getItems().clear();
        for (int i1 = 0; i1 < cbErforderlicheMehrheitMoeglichkeiten.length; i1++) {
            if (cbErforderlicheMehrheitMoeglichkeitenVerwendenBei1Gattung[i1]==true ||
                    ParamS.param.paramModuleKonfigurierbar.mehrereGattungen) {
                cbErforderlicheMehrheit.getItems().addAll(cbErforderlicheMehrheitMoeglichkeiten[i1]);
                if (wert == CaString.liefereKlammerZahlAmAnfang(cbErforderlicheMehrheitMoeglichkeiten[i1])) {
                    cbErforderlicheMehrheit
                    .setValue(CtrlParameterAbstimmungAllgemein.cbErforderlicheMehrheitMoeglichkeiten[i1]);
                }
            }
        }
    }

    /**
     * Liefere auswahl cb erforderliche mehrheit.
     *
     * @param cbErforderlicheMehrheit the cb erforderliche mehrheit
     * @return the int
     */
    public static int liefereAuswahlCbErforderlicheMehrheit(ComboBox<String> cbErforderlicheMehrheit) {
        int ident = 0;
        String hString = cbErforderlicheMehrheit.getValue();
        if (hString != null && !hString.isEmpty()) {
            ident = CaString.liefereKlammerZahlAmAnfang(hString);
        }
        return ident;
    }

    /**
     * ***************Beschlußvorschlag von **************************************.
     */
    public static String cbBeschlussvorschlagVonMoeglichkeiten[] = { "(0) undefiniert", "(1) Vorstand",
            "(2) Aufsichtsrat", "(3) VorstandAufsichtsrat", "(4) Sonstige (dann separater Text)" };

    /**
     * Fuelle cb beschlussvorschlag von.
     *
     * @param cbBeschlussvorschlagVon the cb beschlussvorschlag von
     * @param wert                    the wert
     */
    public static void fuelleCbBeschlussvorschlagVon(ComboBox<String> cbBeschlussvorschlagVon, int wert) {
        cbBeschlussvorschlagVon.getItems().clear();
        for (int i1 = 0; i1 < cbBeschlussvorschlagVonMoeglichkeiten.length; i1++) {
            cbBeschlussvorschlagVon.getItems().addAll(cbBeschlussvorschlagVonMoeglichkeiten[i1]);
            if (i1 == wert) {
                cbBeschlussvorschlagVon
                        .setValue(CtrlParameterAbstimmungAllgemein.cbBeschlussvorschlagVonMoeglichkeiten[i1]);
            }
        }
    }

    /**
     * Liefere auswahl cb beschlussvorschlag von moeglichkeiten.
     *
     * @param cbBeschlussvorschlagVonMoeglichkeiten the cb beschlussvorschlag von
     *                                              moeglichkeiten
     * @return the int
     */
    public static int liefereAuswahlCbBeschlussvorschlagVonMoeglichkeiten(
            ComboBox<String> cbBeschlussvorschlagVonMoeglichkeiten) {
        int ident = 0;
        String hString = cbBeschlussvorschlagVonMoeglichkeiten.getValue();
        if (hString != null && !hString.isEmpty()) {
            ident = CaString.liefereKlammerZahlAmAnfang(hString);
        }
        return ident;
    }

    /******************Array aus EclAbstimmung - Einlesen und Belegen etc.*******************************************/
    public static EclAbstimmung[] abstimmungArray = null;

    /** The anz abstimmung array. */
    public static int anzAbstimmungArray = 0;

    /**
     * Liest abstimmungArray aus Datenbank, und zwar alle, sortiert nach ident (also
     * z.B. für Combo-Boxen mit allen Abstimmungen) . Muß noch überarbeitet werden
     * (Holen über Webservice, aus Puffer o.ä.)
     */
    public static void belegeAbstimmungArray() {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        anzAbstimmungArray = lDbBundle.dbAbstimmungen.leseAlleAbstimmungen(KonstDBAbstimmungen.sortierung_interneIdent,
                KonstDBAbstimmungen.selektion_alle, false);
        abstimmungArray = lDbBundle.dbAbstimmungen.abstimmungenArray;
        lDbBundle.closeAll();
    }

    /**
     * Hole ident zu ident gesamtweisung.
     *
     * @param pWert the wert
     * @return the int
     */
    public static int holeIdentZuIdentGesamtweisung(int pWert) {
        if (pWert == -1) {
            return -1;
        }
        for (int i = 0; i < anzAbstimmungArray; i++) {
            if (abstimmungArray[i].identWeisungssatz == pWert) {
                return abstimmungArray[i].ident;
            }
        }
        return -1;
    }

    /**
     * Hole ident gesamtweisung zu ident.
     *
     * @param pWert the wert
     * @return the int
     */
    public static int holeIdentGesamtweisungZuIdent(int pWert) {
        for (int i = 0; i < anzAbstimmungArray; i++) {
            if (abstimmungArray[i].ident == pWert) {
                return abstimmungArray[i].identWeisungssatz;
            }
        }
        return -1;
    }

    /**
     * textFuerDefault: empty, dann keine Default-Text.
     *
     * @param cbVonIdentGesamtweisung the cb von ident gesamtweisung
     * @param wert                    the wert
     * @param mitUeberschrift         the mit ueberschrift
     * @param textFuerDefault         the text fuer default
     */
    public static void belegeMitAbstimmungArray(ComboBox<String> cbVonIdentGesamtweisung, int wert,
            boolean mitUeberschrift, String textFuerDefault) {
        cbVonIdentGesamtweisung.getItems().clear();
        if (!textFuerDefault.isEmpty()) {
            cbVonIdentGesamtweisung.getItems().addAll(textFuerDefault);
            if (wert == -1) {
                cbVonIdentGesamtweisung.setValue(textFuerDefault);
            }
        }
        if (anzAbstimmungArray > 0) {
            for (int i1 = 0; i1 < anzAbstimmungArray; i1++) {
                EclAbstimmung lAbstimmung = abstimmungArray[i1];
                if (lAbstimmung.identWeisungssatz != -1 || mitUeberschrift) {
                    String hString = lAbstimmung.nummerKey + " " + lAbstimmung.nummerindexKey + " "
                            + lAbstimmung.kurzBezeichnung + " (" + Integer.toString(lAbstimmung.ident) + ")";
                    cbVonIdentGesamtweisung.getItems().addAll(hString);
                    if (lAbstimmung.ident == wert) {
                        cbVonIdentGesamtweisung.setValue(hString);
                    }
                }
            }
        }
    }

    /**
     * Liefert -1, falls Value leer.
     *
     * @param cbAbstimmungArray the cb abstimmung array
     * @return the int
     */
    public static int liefereAuswahlAusCbAbstimmungArray(ComboBox<String> cbAbstimmungArray) {
        int ident = -1;
        String hString = cbAbstimmungArray.getValue();
        if (hString != null && !hString.isEmpty()) {
            ident = CaString.liefereKlammerZahlAmEnde(hString);
        }
        return ident;
    }

    /********************Abstimmungsvorgänge***************************************/
    public static EclAbstimmungsblock[] abstimmungsblockArray = null;

    /** The anz abstimmungsblock array. */
    public static int anzAbstimmungsblockArray = 0;

    /**
     * Belege abstimmungsblock array.
     */
    public static void belegeAbstimmungsblockArray() {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbAbstimmungsblock.read_all();
        abstimmungsblockArray = lDbBundle.dbAbstimmungsblock.ergebnisArray;
        if (abstimmungsblockArray != null) {
            anzAbstimmungsblockArray = abstimmungsblockArray.length;
        } else {
            anzAbstimmungsblockArray = 0;
        }
        lDbBundle.closeAll();
        return;
    }

    /**
     * Belege mit abstimmungblock array.
     *
     * @param cbAbstimmungBloecke the cb abstimmung bloecke
     * @param wert                the wert
     */
    public static void belegeMitAbstimmungblockArray(ComboBox<String> cbAbstimmungBloecke, int wert) {
        cbAbstimmungBloecke.getItems().clear();
        if (anzAbstimmungsblockArray > 0) {
            for (int i1 = 0; i1 < anzAbstimmungsblockArray; i1++) {
                EclAbstimmungsblock lAbstimmungsblock = abstimmungsblockArray[i1];
                String hString = lAbstimmungsblock.kurzBeschreibung + " (" + Integer.toString(lAbstimmungsblock.ident)
                        + ")";
                cbAbstimmungBloecke.getItems().addAll(hString);
                if (lAbstimmungsblock.ident == wert) {
                    cbAbstimmungBloecke.setValue(hString);
                }
            }
        }
    }

    /**
     * Liefert -1, falls Value leer.
     *
     * @param cbAbstimmungBloecke the cb abstimmung bloecke
     * @return the int
     */
    public static int liefereAuswahlAusAbstimmungblockArray(ComboBox<String> cbAbstimmungBloecke) {
        int ident = -1;
        String hString = cbAbstimmungBloecke.getValue();
        if (hString != null && !hString.isEmpty()) {
            ident = CaString.liefereKlammerZahlAmEnde(hString);
        }
        return ident;
    }

    /**************Abstimmungen zu Abstimmungsvorgang*************************************/
    public static EclAbstimmungZuAbstimmungsblock[] abstimmungZuAbstimmungsblockArray = null;

    /** The anz abstimmung zu abstimmungsblock array. */
    public static int anzAbstimmungZuAbstimmungsblockArray = 0;

    /**
     * Belege abstimmung zu abstimmungsblock array.
     *
     * @param pAbstimmungsBlock the abstimmungs block
     */
    public static void belegeAbstimmungZuAbstimmungsblockArray(int pAbstimmungsBlock) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbAbstimmungZuAbstimmungsblock.read_zuAbstimmungsblock(pAbstimmungsBlock, 1);
        abstimmungZuAbstimmungsblockArray = lDbBundle.dbAbstimmungZuAbstimmungsblock.ergebnisArray;
        if (abstimmungZuAbstimmungsblockArray != null) {
            anzAbstimmungZuAbstimmungsblockArray = abstimmungZuAbstimmungsblockArray.length;
        } else {
            anzAbstimmungZuAbstimmungsblockArray = 0;
        }
        lDbBundle.closeAll();
        return;
    }

    /**
     * Pruefe vorhanden stimmkarte stimmzettel in abstimmungsblock.
     *
     * @param pStimmkarte  the stimmkarte
     * @param pStimmzettel the stimmzettel
     * @return true, if successful
     */
    public static boolean pruefeVorhandenStimmkarteStimmzettelInAbstimmungsblock(int pStimmkarte, int pStimmzettel) {
        if (pStimmkarte == 0 && pStimmzettel == 0) {
            return false;
        }
        for (int i = 0; i < anzAbstimmungZuAbstimmungsblockArray; i++) {
            if (abstimmungZuAbstimmungsblockArray[i].nummerDerStimmkarte == pStimmkarte
                    && abstimmungZuAbstimmungsblockArray[i].positionAufStimmkarte == pStimmzettel) {
                return true;
            }
        }
        return false;
    }

    /**
     * Pruefe vorhanden tablet seite position in abstimmungsblock.
     *
     * @param pSeite    the seite
     * @param pPosition the position
     * @return true, if successful
     */
    public static boolean pruefeVorhandenTabletSeitePositionInAbstimmungsblock(int pSeite, int pPosition) {
        if (pSeite == 0 && pPosition == 0) {
            return false;
        }
        for (int i = 0; i < anzAbstimmungZuAbstimmungsblockArray; i++) {
            if (abstimmungZuAbstimmungsblockArray[i].seite == pSeite
                    && abstimmungZuAbstimmungsblockArray[i].position == pPosition) {
                return true;
            }
        }
        return false;
    }

    /********************DBStimmkarte***************************************/
    public static EclStimmkarteInhalt[] stimmkarteInhaltArray = null;

    /** The anz stimmkarte inhalt array. */
    public static int anzStimmkarteInhaltArray = 0;

    /**
     * Belege stimmkarte inhalt array.
     */
    public static void belegeStimmkarteInhaltArray() {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbStimmkarteInhalt.readAll();
        stimmkarteInhaltArray = lDbBundle.dbStimmkarteInhalt.ergebnis();
        if (stimmkarteInhaltArray != null) {
            anzStimmkarteInhaltArray = stimmkarteInhaltArray.length;
        } else {
            anzStimmkarteInhaltArray = 0;
        }
        lDbBundle.closeAll();
        return;
    }

    /*************************Weisungen vorbelegen bei Erstanlage****************************/
    public static void belegeWeisungBelegenMit(ComboBox<String> pComboBox) {
        pComboBox.getItems().clear();
        pComboBox.getItems().addAll("J");
        pComboBox.getItems().addAll("N");
        pComboBox.getItems().addAll("E");
        pComboBox.getItems().addAll(".");
        pComboBox.setValue("E");
    }

}
