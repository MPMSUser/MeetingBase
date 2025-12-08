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
package de.meetingapps.meetingportal.meetingCoreReport;

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclTermine;
import de.meetingapps.meetingportal.meetComKonst.KonstTermine;

public class RpVariablen {

    
    //	private boolean logDrucken=false;

    /**false -> aus dem Mandantenspezifischen Pfad verwenden
     * true -> aus dem Pfad mc verwenden
     */
    public boolean globalesFormularVerwenden = false;

    /**Dateiname, in dem LL-Quelle abgelegt ist */
    public StringBuffer dateiname = null;

    private DbBundle lDbBundle = null;

    /**Enthält den Namen der Datei (ohne Zusatz, ohne Pfad) des zuletzt
     * geladenen Formulars/Liste
     */
    private String vorlagenDateiName = "XXXXXX";

    /**Enthält die Nummer der Datei des zuletzt geladenen Formulars/Liste*/
    private String vorlagenNummer = "XX";

    public RpVariablen(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    /**Liefert den Pfad, in den die Report-Vorlagen abgelegt sind*/
    public String reportVorlagePfad() {
        if (globalesFormularVerwenden == false) {
            return lDbBundle.lieferePfadMeetingReports();
        }
        return lDbBundle.lieferePfadMeetingReportsAllgemein();
    }

    public String reportVorlagePfadNameOhneZusatz() {
        return reportVorlagePfad() + "\\" + vorlagenDateiName + vorlagenNummer;
    }

    /**Verwenden für Formular. Setzt automatisch vorlagenNummer und vorlagenDateiName für spätere Verwendung*/
    private void reportPfadDateiname(String pDateiname, String pNr, String pZusatz) {
        vorlagenDateiName = pDateiname;
        if (!pNr.isEmpty()) {
            pNr = CaString.fuelleLinksNull(pNr, 2);
        }
        vorlagenNummer = pNr;
        dateiname = new StringBuffer(reportVorlagePfad() + "\\" + pDateiname + pNr + pZusatz);
    }

    /**Verwenden für Logo etc.*/
    private String pfadDateiname(String pDateiname, String pNr, String pZusatz) {
        return reportVorlagePfad() + "\\" + pDateiname + pNr + pZusatz;
    }

    /**Variablen für Formulare bzw. Listenüberschriften (1x pro Seite)*/
    public void fuelleVariable(RpDrucken pRpDrucken, String pVariablenname, String pInhalt) {
     // add here your preferred DokumentGenerator
    }

    /**Felder für einzelen Listenelemente in Listen (1x pro Datensatz)*/
    public void fuelleFeld(RpDrucken pRpDrucken, String pVariablenname, String pInhalt) {
        // add here your preferred DokumentGenerator
    }

    /**Felder für einzelen Listenelemente in Listen (1x pro Datensatz)
     * feldOderVariable kann 1 (Feld), 2 (Variable) oder einen anderen Wert (dann wird nix gemacht!) haben.*/
    public void fuelleFeldOderVariable(RpDrucken pRpDrucken, String pVariablenname, String pInhalt,
            int feldOderVariable) {
        if (feldOderVariable == 1) {
            fuelleFeld(pRpDrucken, pVariablenname, pInhalt);
        }
        if (feldOderVariable == 2) {
            fuelleVariable(pRpDrucken, pVariablenname, pInhalt);
        }
    }

    public void fuelleAllgemeineVariablen(RpDrucken pRpDrucken) {
        // add here your preferred DokumentGenerator
        String logoPfad = pfadDateiname("LOGO", "", ".JPG");
        fuelleVariable(pRpDrucken, "Logo", logoPfad);
        fuelleVariable(pRpDrucken, "Emittent.Logo", logoPfad);
        
        fuelleVariable(pRpDrucken, "Gesellschaft", lDbBundle.eclEmittent.holeBezeichnungFormulare());
        fuelleVariable(pRpDrucken, "Emittent.Gesellschaft", lDbBundle.eclEmittent.holeBezeichnungFormulare());
        
        fuelleVariable(pRpDrucken, "HVDatum", lDbBundle.eclEmittent.hvDatum);
        fuelleVariable(pRpDrucken, "HVDaten.HVDatum", lDbBundle.eclEmittent.hvDatum);
        
        fuelleVariable(pRpDrucken, "HVOrt", lDbBundle.eclEmittent.hvOrt);
        fuelleVariable(pRpDrucken, "HVDaten.HVOrt", lDbBundle.eclEmittent.hvOrt);
        
        fuelleVariable(pRpDrucken, "Datum", CaDatumZeit.DatumStringFuerAnzeige());
        fuelleVariable(pRpDrucken, "Sonstiges.DruckDatum", CaDatumZeit.DatumStringFuerAnzeige());
        
        fuelleVariable(pRpDrucken, "Zeit", CaDatumZeit.StundenMinutenStringFuerAnzeige());
        fuelleVariable(pRpDrucken, "Sonstiges.DruckZeit", CaDatumZeit.StundenMinutenStringFuerAnzeige());
        
        if (lDbBundle.eclEmittent.liefereDatenbestandMusterAufFormularenAusgeblendet()) {
            fuelleVariable(pRpDrucken,"MusterAnzeigen", "0");
            fuelleVariable(pRpDrucken,"Sonstiges.MusterAnzeigen", "0");
        }
        else {
            fuelleVariable(pRpDrucken,"MusterAnzeigen", "1");
            fuelleVariable(pRpDrucken,"Sonstiges.MusterAnzeigen", "1");
       }
        
        fuelleVariable(pRpDrucken, "MandantNr", lDbBundle.getMandantString());
        fuelleVariable(pRpDrucken, "Sonstiges.MandantNr", lDbBundle.getMandantString());
        
        fuelleVariable(pRpDrucken, "Kurzlink", lDbBundle.param.paramPortal.kurzLinkPortal);
        fuelleVariable(pRpDrucken, "Sonstiges.Kurzlink", lDbBundle.param.paramPortal.kurzLinkPortal);

        fuelleVariable(pRpDrucken, "HVForm", Integer.toString(lDbBundle.param.paramModuleKonfigurierbar.hvForm));
        fuelleVariable(pRpDrucken, "HVDaten.HVForm", Integer.toString(lDbBundle.param.paramModuleKonfigurierbar.hvForm));
        
        fuelleVariable(pRpDrucken, "Emittent.Strasse", lDbBundle.eclEmittent.strasseGesellschaft);
        fuelleVariable(pRpDrucken, "Emittent.PLZ", lDbBundle.eclEmittent.postleitzahl);
        fuelleVariable(pRpDrucken, "Emittent.Ort", lDbBundle.eclEmittent.ort);
        fuelleVariable(pRpDrucken, "Emittent.Land", Integer.toString(lDbBundle.eclEmittent.staatId));
        
        fuelleVariable(pRpDrucken, "HVDaten.HVArtId", Integer.toString(lDbBundle.eclEmittent.hvArtSchluessel));
        fuelleVariable(pRpDrucken, "HVDaten.HVVeranstaltungsort", lDbBundle.eclEmittent.veranstaltungGebäude);
        fuelleVariable(pRpDrucken, "HVDaten.HVStrasse", lDbBundle.eclEmittent.veranstaltungStrasse);
        fuelleVariable(pRpDrucken, "HVDaten.HVPLZ", lDbBundle.eclEmittent.veranstaltungPostleitzahl);
        fuelleVariable(pRpDrucken, "HVDaten.HVLand", Integer.toString(lDbBundle.eclEmittent.veranstaltungStaatId));
       
        fuelleVariable(pRpDrucken, "HVDaten.HVBeginn","");
        fuelleVariable(pRpDrucken, "HVDaten.HVBeginnDE","");
        fuelleVariable(pRpDrucken, "HVDaten.HVBeginnEN","");
        EclTermine lTermin=lDbBundle.liefereTermin(KonstTermine.hvTag);
        if (lTermin!=null) {
            fuelleVariable(pRpDrucken, "HVDaten.HVBeginn",lTermin.terminZeit);
            fuelleVariable(pRpDrucken, "HVDaten.HVBeginnDE",lTermin.textDatumZeitFuerPortalDE);
            fuelleVariable(pRpDrucken, "HVDaten.HVBeginnEN",lTermin.textDatumZeitFuerPortalEN);
        }
        
        fuelleVariable(pRpDrucken, "HVDaten.HVEinlass","");
        fuelleVariable(pRpDrucken, "HVDaten.HVEinlassDE","");
        fuelleVariable(pRpDrucken, "HVDaten.HVEinlassEN","");
        lTermin=lDbBundle.liefereTermin(KonstTermine.hvTagEinlass);
        if (lTermin!=null) {
            fuelleVariable(pRpDrucken, "HVDaten.HVEinlass",lTermin.terminZeit);
            fuelleVariable(pRpDrucken, "HVDaten.HVEinlassDE",lTermin.textDatumZeitFuerPortalDE);
            fuelleVariable(pRpDrucken, "HVDaten.HVEinlassEN",lTermin.textDatumZeitFuerPortalEN);
        }

        fuelleVariable(pRpDrucken, "HVDaten.HVLetzterAnmeldetag","");
        lTermin=lDbBundle.liefereTermin(KonstTermine.letzterAnmeldetag);
        if (lTermin!=null) {
            String hDatum=lTermin.terminDatum;
            if (hDatum.length()==8){
                fuelleVariable(pRpDrucken, "HVDaten.HVLetzterAnmeldetag",CaDatumZeit.datumJJJJMMTTzuJJJJ_MM_TT(hDatum));
            }
        }
        
        for (int i=1;i<=4;i++) {
            fuelleVariable(pRpDrucken, "Sonstiges.ISIN"+Integer.toString(i), lDbBundle.param.paramBasis.getIsin(i));
        }

        if (lDbBundle.param.paramBasis.mehrereGattungenAktiv()) {
            fuelleVariable(pRpDrucken, "Sonstiges.MehrereGattungenVorhanden", "1");
        }
        else {
            fuelleVariable(pRpDrucken, "Sonstiges.MehrereGattungenVorhanden", "0");
        }
    }

    /**Eintrittskarte, im Portal nach freiwilliger Anmeldung selbst auszudrucken*/
    public void ekFreiwilligeAnmeldung(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Definitionen für pArt*/
    private final int EK_MAIL=1;
    private final int EK_PAPIER=2;
    private final int EK_SELBSTDRUCK=3;
    
    /**Eintrittskarte, im Portal selbst auszudrucken*/
    private void ekAllgemeindruck(RpDrucken rpDrucken, int pArt) {
        // add here your preferred DokumentGenerator
    }

    
    /**Eintrittskarte, im Portal selbst auszudrucken*/
    public void ekSelbstdruck(RpDrucken rpDrucken) {
        ekAllgemeindruck(rpDrucken, EK_SELBSTDRUCK);
    }

    /**Eintrittskarte, wie sie per Email versandt wird*/
    public void ekMail(RpDrucken rpDrucken) {
        ekAllgemeindruck(rpDrucken, EK_MAIL);
    }

    /**Eintrittskarte, wie sie per Email versandt wird*/
    public void ekPapier(RpDrucken rpDrucken) {
        ekAllgemeindruck(rpDrucken, EK_PAPIER);
    }


    /**Gastkarte, Papier*/
    public void gastkarteSelbstdruck(String pNr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Gastkarte, wie sie per Email versandt wird*/
    public void gastkarteMail(String pNr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Gastkarte, wie sie per Email versandt wird*/
    public void gastkarteDruckEmittent(String pNr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    public void gkStapel(String pNr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Gastkarte, im Portal selbst auszudrucken*/
    public void abstimmungAktionaer() {
        // add here your preferred DokumentGenerator
    }

    /**Meldeliste*/
    public void meldeliste(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Liste der Willenserkläerungen für Aktionäre*/
    public void listeWK(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Negativliste*/
    public void negativliste(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Negativliste*/
    public void stimmausschlussliste(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**EmailVersand*/
    public void emailVersandListe(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**TestBarcodes, im Portal selbst auszudrucken*/
    public void testBarcodes(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**TestEinladungen (mit Identifikationsbarcode für App), im Portal selbst auszudrucken*/
    public void testEinladungen(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Präsenzliste*/
    public void praesenzliste(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Präsenz-Zusammenstellung*/
    public void praesenzZusammenstellung(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Präsenzprotokoll*/
    public void praesenzprotokoll(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Statistik Meldung*/
    public void statistikMeldung(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Sammelkurzübersicht*/
    public void sammelKurzUebersicht(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Sammelübersicht mit Weisungen*/
    public void sammelUebersichtMitWeisungen(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Sammelübersicht mit Weisungen und Aktionären*/
    public void sammelUebersichtMitWeisungenAktionaeren(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**weisungsSummen*/
    public void weisungsSummen(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Sammelkurzübersicht*/
    public void weisungsKontrolle(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**GastkartenListe*/
    public void gastkartenListe(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Präsenzliste*/
    public void gastkartenUebersichtListe(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    private void abtimmungVariablenFuellen(RpDrucken rpDrucken, int listeOderBlatt) {
        // add here your preferred DokumentGenerator
    }

    /**Abstimmung - Verleseblatt*/
    public void abstimmungVerleseblatt(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Abstimmung - Ergebnisliste*/
    public void abstimmungErgebnisListe(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Präsenz-Zusammenstellung*/
    public void abstimmungZwischenblatt(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Sammelanmeldebogen*/
    public void sammelAnmeldebogen(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Datenpflege*/
    public void datenpflegeMeldung(String pNr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Anschreiben für Neues Passwort*/
    public void anschreibenNeuesPasswort(String pNr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Anschreiben für Neues Passwort*/
    public void mitteilung(String pNr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Anschreiben für Neues Passwort*/
    public void wortmeldung(String pNr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Bestätigung für Weisungsabgabe Briefwahl oder SRV - Formular*/
    public void weisungBestaetigung(String pNr, RpDrucken rpDrucken, boolean pMitTOP) {
        // add here your preferred DokumentGenerator
    }

    /**Bestätigung für Weisungsabgabe Briefwahl oder SRV - Formular*/
    public void vollmachtsFormular(String pNr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    public void feststellungVereinZusammenstellung(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    public void feststellungVereinliste(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Formular, das beim Erstzugang (analog zum Label) ausgedruckt werden kann*/
    public void formularErstzugang(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Testformular, wird beim Zuordnungen / Einstellen von Druckern verwendet*/
    public void testDruckerzuordnung(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    /**Negativliste*/
    public void protokoll(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    
    /**Stimmkartendruck*/
    public void stimmkarten(String pNr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    
    /**Meldeliste*/
    public void listeVeranstaltungsManagement(String nr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }

    
    /**Stimmkartendruck*/
    public void versammlungsbatch(String pNr, RpDrucken rpDrucken) {
        // add here your preferred DokumentGenerator
    }
}
