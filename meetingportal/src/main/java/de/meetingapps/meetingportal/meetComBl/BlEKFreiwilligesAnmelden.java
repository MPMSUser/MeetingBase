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
package de.meetingapps.meetingportal.meetComBl;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

public class BlEKFreiwilligesAnmelden {

    private int logDrucken=10;
    
    private DbBundle lDbBundle=null;
    
    public BlEKFreiwilligesAnmelden(DbBundle pDbBundle) {
        lDbBundle=pDbBundle;
    }
    
    
    public String rcDateiname;
    
    /**Button-Nr. = 
     *  1 (EK für Mitglied oder dessen Vertreter) 
     *  2 (Gastkarte für zweiten Vertreter) 
     *  3 (Gastkarte für Mitglied, z.B. Minderjährige)
     *  4 ("isolierte" Gastkarte (ohne Mitgliedsdaten) - für Aufruf aus uLogin heraus
     *  
     *  pAnmeldeKennungFuerAnzeige: wird für die (temporäre) PDF-Erzeugung) verwendet. Ist die Kennung des Mitglieds.
     *  
     *  pLanguage: Portalsprache. 1=Deutsch.
     *  
     *  lMeldung = zu verwendende Meldung (bei buttonNr 1 bis 3 des eigentlichen Mitglieds; bei 4 des Gastes)
     *  
     *  Füllt: rcDateiname = die erzeugte PDF, die anschließend im Browser angezeigt werden kann.
     */
    public void ausfuehrenEKAnzeigenAllgemein(int buttonNr, String pAnmeldeKennungFuerAnzeige, int pLanguage, EclMeldung lMeldung) {
        
        EclAktienregisterErgaenzung lAktienregisterErgaenzung=null;
        if (buttonNr!=4) {
            lDbBundle.dbAktienregisterErgaenzung.readZuident(lMeldung.aktienregisterIdent);
            lAktienregisterErgaenzung=lDbBundle.dbAktienregisterErgaenzung.ergebnisPosition(0);
        }
        
        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();
        rpDrucken.exportFormat = 8;
        rpDrucken.exportDatei = "zutrittsdokumentFWAM" + lDbBundle.clGlobalVar.getMandantString()+pAnmeldeKennungFuerAnzeige;
        rpDrucken.initFormular(lDbBundle);

        /*Variablen füllen - sowie Dokumentvorlage*/
        RpVariablen rpVariablen = new RpVariablen(lDbBundle);
        rpVariablen.ekFreiwilligeAnmeldung("01", rpDrucken);
        rpDrucken.startFormular();

        /*0=Aufruf aus Deutschem Portal, 1=Aufruf aus Englischem Portal*/
        if (pLanguage==1) {
            rpVariablen.fuelleVariable(rpDrucken, "IstEnglisch", "0");
            CaBug.druckeLog("IstEnglisch=0", logDrucken, 10);
        }
        else {
            rpVariablen.fuelleVariable(rpDrucken, "IstEnglisch", "1");
            CaBug.druckeLog("IstEnglisch=1", logDrucken, 3);
        }

        String lIstGastkarte="";
        switch (buttonNr) {
        case 1:lIstGastkarte="0";break;
        case 2:lIstGastkarte="1";break;
        case 3:lIstGastkarte="2";break;
        case 4:lIstGastkarte="3";break;
        }
        rpVariablen.fuelleVariable(rpDrucken, "IstGastkarte", lIstGastkarte);
        
        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);

        String lZutrittsIdent="";
        EclMeldung lMeldungGast=null;
        
        switch (buttonNr) {
        case 1:
            lZutrittsIdent=lMeldung.zutrittsIdent;
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.NameVorname", lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER1_NAME]);
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort", lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER1_ORT]);
            break;
        case 2:
            lDbBundle.dbMeldungen.leseZuMeldungsIdent(lAktienregisterErgaenzung.ergaenzungKennzeichen[26]);
            lMeldungGast=lDbBundle.dbMeldungen.meldungenArray[0];
            lZutrittsIdent=lMeldungGast.zutrittsIdent;
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.NameVorname", lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER2_NAME]);
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort", lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_VERTRETER2_ORT]);
            break;
        case 3:
            lDbBundle.dbMeldungen.leseZuMeldungsIdent(lAktienregisterErgaenzung.ergaenzungKennzeichen[25]);
            lMeldungGast=lDbBundle.dbMeldungen.meldungenArray[0];
            lZutrittsIdent=lMeldungGast.zutrittsIdent;
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.NameVorname", "");
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort", "");
            break;
        case 4:
            lZutrittsIdent=lMeldung.zutrittsIdent;
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.NameVorname", "");
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort", "");
            break;
        }

        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdent", BlNummernformen.verketteEKMitNeben(lZutrittsIdent, "00", 0));
        if (!lMeldung.zutrittsIdent.trim().isEmpty()) {
            rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKurz", lMeldung.zutrittsIdent);
            rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKomplett", blNummernformen.formatiereNrKomplett(lZutrittsIdent, "00",
                KonstKartenklasse.eintrittskartennummerNeben, KonstKartenart.erstzugang));
        }
        else {
            rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKurz", "");
            rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKomplett", "");
        }
        //              System.out.println("ZutrittsIdentKomplett="+blNummernformen.formatiereNrKomplett(zutrittsIdentAktionaer.zutrittsIdent, zutrittsIdentAktionaer.zutrittsIdentNeben, KonstKartenklasse.eintrittskartennummerNeben, KonstKartenart.erstzugang));
        //              rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKomplett", blNummernformen.formatiereEKNrKomplett(zutrittsIdentAktionaer.zutrittsIdent, zutrittsIdentAktionaer.zutrittsIdentNeben));
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.Aktionaersnummer",
                BlNummernformBasis.aufbereitenInternFuerExtern(lMeldung.aktionaersnummer, lDbBundle)
                );

        rpVariablen.fuelleVariable(rpDrucken, "Besitz.Stimmen", Long.toString(lMeldung.stimmen));
        rpVariablen.fuelleVariable(rpDrucken, "Besitz.StimmenDE", CaString.toStringDE(lMeldung.stimmen));
        rpVariablen.fuelleVariable(rpDrucken, "Besitz.StimmenEN", CaString.toStringEN(lMeldung.stimmen));

        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Titel", lMeldung.titel);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Name", lMeldung.name);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Vorname", lMeldung.vorname);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Plz", lMeldung.plz);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Ort", lMeldung.ort);
        if (!lMeldung.land.isEmpty()) {
            lDbBundle.dbStaaten.readCode(lMeldung.land);
            if (lDbBundle.dbStaaten.anzErgebnis() > 0) {
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Landeskuerzel",
                        lDbBundle.dbStaaten.ergebnisPosition(0).code);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Land",
                        lDbBundle.dbStaaten.ergebnisPosition(0).nameDE);
            }
        }
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Strasse", lMeldung.strasse);
        /*Anrede füllen*/
        int anredenNr = lMeldung.anrede;
        EclAnrede hAnrede = new EclAnrede();
        if (anredenNr != 0) {
            lDbBundle.dbAnreden.SetzeSprache(2, 0);
            lDbBundle.dbAnreden.ReadAnrede_Anredennr(anredenNr);
            hAnrede = new EclAnrede();
            if (lDbBundle.dbAnreden.AnzAnredenInReadArray > 0) {
                hAnrede = lDbBundle.dbAnreden.anredenreadarray[0];
            }
            rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.AnredeDE", hAnrede.anredentext);
            rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.AnredeEN", hAnrede.anredentextfremd);
            rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BriefanredeDE", hAnrede.anredenbrief);
            rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BriefanredeEN", hAnrede.anredenbrieffremd);
        }

        /*Kombi-Felder füllen*/
        String titelVornameName = "";
        if (lMeldung.titel.length() != 0) {
            titelVornameName = titelVornameName + lMeldung.titel + " ";
        }
        if (lMeldung.vorname.length() != 0) {
            titelVornameName = titelVornameName + lMeldung.vorname + " ";
        }
        titelVornameName = titelVornameName + lMeldung.name;
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.TitelVornameName", titelVornameName);

        String nameVornameTitel = "";
        nameVornameTitel = lMeldung.name;
        if (lMeldung.titel.length() != 0 || lMeldung.vorname.length() != 0) {
            nameVornameTitel = nameVornameTitel + ",";
        }
        if (lMeldung.titel.length() != 0) {
            nameVornameTitel = nameVornameTitel + " " + lMeldung.titel;
        }
        if (lMeldung.vorname.length() != 0) {
            nameVornameTitel = nameVornameTitel + " " + lMeldung.vorname;
        }
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.NameVornameTitel", nameVornameTitel);

        String kompletteAnredeDE = hAnrede.anredenbrief;
        String kompletteAnredeEN = hAnrede.anredenbrieffremd;
        if (hAnrede.istjuristischePerson != 1) {
            if (lMeldung.titel.length() != 0) {
                kompletteAnredeDE = kompletteAnredeDE + " " + lMeldung.titel;
                kompletteAnredeEN = kompletteAnredeEN + " " + lMeldung.titel;
            }
            if (lMeldung.name.length() != 0) {
                kompletteAnredeDE = kompletteAnredeDE + " " + lMeldung.name;
                kompletteAnredeEN = kompletteAnredeEN + " " + lMeldung.name;
            }
        }
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.KompletteAnredeDE", kompletteAnredeDE);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.KompletteAnredeEN", kompletteAnredeEN);

        String besitzArtKuerzel = lMeldung.besitzart;
        String besitzArt = "", besitzArtEN = "";
        switch (besitzArtKuerzel) {
        case "E": 
            besitzArt = "Eigenbesitz";
            besitzArtEN = "Proprietary Possession";
            break;
       case "F": 
            besitzArt = "Fremdbesitz";
            besitzArtEN = "Minority Interests";
            break;
         case "V": 
            besitzArt = "Vollmachtsbesitz";
            besitzArtEN = "Proxy Possession";
            break;
        }
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArtKuerzel", besitzArtKuerzel);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArt", besitzArt);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArtEN", besitzArtEN);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.GattungKurz",
                lDbBundle.param.paramBasis.getGattungBezeichnungKurz(lMeldung.liefereGattung()));


        //Start printing
        rpDrucken.druckenFormular();
        rpDrucken.endeFormular();
        
         rcDateiname =
                 lDbBundle.lieferePfadMeetingAusdrucke() + "\\zutrittsdokumentFWAM" + lDbBundle.clGlobalVar.getMandantString()
                + pAnmeldeKennungFuerAnzeige + ".pdf";

        
        
         return;
     }

    
    
}
