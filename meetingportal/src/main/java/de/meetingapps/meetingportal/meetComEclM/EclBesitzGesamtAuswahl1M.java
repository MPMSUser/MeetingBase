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
package de.meetingapps.meetingportal.meetComEclM;

import java.io.Serializable;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmachtEingabe;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmachtFuerAnzeige;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**********
 * Ergänzung zu EclBesitzGesamtM.
 * 
 * Wird verwendet in Auswahl 1 - z.B. für Generalversammlungen (freiweilliges Anmelden,
 * vorläufige Vollmacht etc.)
 *
 */
@SessionScoped
@Named
public class EclBesitzGesamtAuswahl1M implements Serializable {
    private static final long serialVersionUID = -5264513817395571227L;

  	private int logDrucken=3;

    private @Inject EclBesitzGesamtM eclBesitzGesamtM;
    private @Inject EclParamM eclParamM;

    private boolean eigenerAREintragVorhanden = false;
    private boolean erhalteneVollmachtenVorhanden = false;
    /**Vollmachten, die mit gesetzlichen Vollmachten vererbt wurden*/
    private boolean vererbteVollmachtenVorhanden = false;

    /******************An oder Abmeldung - für Mitglieder*******************************/
    /**Anmeldung ist generell möglich oder nicht möglich für diese Anmeldung 
     * (d.h. wenn keine Mitgliedskennung, dann false)*/
    private boolean anmeldungMoeglich = false;

    private boolean anOderAbgemeldet = false;

    private boolean angemeldet = false;
    private boolean abgemeldet = false;
    private boolean zweiPersonenAngemeldet = false;

    /**Anmeldung ist für einen Bevollmächtigten erfolgt*/
    private boolean vertreterAngemeldet = false;

    /**Sobald eine Vollmacht tatsächlich eingetragen ist, kann diese storniert werden.
     * Und muß diese storniert werden, bevor der Anmeldestatus geändert wird
     */
    private boolean vollmachtAnDritteEingetragen = false;
    /**Aktuell eingetragene Vollmacht*/
    private EclVorlaeufigeVollmachtFuerAnzeige vollmachtAnDritteEingetragenEcl = null;

    /**Gesetzliche Vollmacht. kann nicht storniert werden. Zumindest nicht vom Mitglied ...
     */
    private boolean vollmachtGesetzlichEingetragen = false;
    /**Aktuell eingetragene Vollmacht*/
    private EclVorlaeufigeVollmachtFuerAnzeige vollmachtGesetzlichEingetragenEcl = null;

    private boolean gesetzlVertreterEingetragenNichtGeprueft=false;
    private boolean vertreterEingetragenNichtGeprueft=false;
    
    /**Aktuell true, wenn keine Prüfung mehr erforderlich und nicht Eltern*/
    private boolean ekDruckWirdAngezeigt=false;
    
    /**Liste aller mit den gesetzlichen Vollmachten verbundene (Unter)Vollmachten*/
    private List<EclVorlaeufigeVollmachtFuerAnzeige> listeAllerMitGesetzlichenVollmachten = null;

    public boolean liefereListeAllerMitGesetzlichenVollmachtenVorhanden() {
        if (listeAllerMitGesetzlichenVollmachten.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**Liste aller eingegangenen Vollmachten (ohne die aktuell gültige!)*/
    private List<EclVorlaeufigeVollmachtFuerAnzeige> listeAllerVollmachten = null;

    public boolean liefereListeAllerVollmachtenVorhanden() {
        if (listeAllerVollmachten.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**Liste aller eingegangenen gesetzlichen Vollmachten (ohne die aktuell gültige!)*/
    private List<EclVorlaeufigeVollmachtFuerAnzeige> listeAllerGesetzlichenVollmachten = null;

    public boolean liefereListeAllerGesetzlichenVollmachtenVorhanden() {
        if (listeAllerGesetzlichenVollmachten.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**Liste aller eingegangenen an Dritte Vollmachten (ohne die aktuell gültige!)*/
    private List<EclVorlaeufigeVollmachtFuerAnzeige> listeAllerAnDritteVollmachten = null;

    public boolean liefereListeAllerAnDritteVollmachtenVorhanden() {
        if (listeAllerAnDritteVollmachten.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**SieheKonstGruppen; -1 für unbelegt*/
    private int gruppe=-1;
    
    /**iGeneralversammlungAnAbmelden
     * Bei An-/Abmeldung, nach Radiobuttons; bei 602*/
    private int gruppenTextStartAnAbmeldung=0;
    /**Select-Button für eine Person*/
    private int gruppenTextEinePerson=0;
    /**Select-Button für zwei Personen*/
    private int gruppenTextZweiPersonen=0;
    /**Select-Button Abmelden*/
    private int gruppenTextAbmelden=0;
    /**Select-Button Vertreter*/
    private int gruppenTextVertreter=0;

    /**Muster-Vollmachtsformular gesetzlich anzeigen*/
    private boolean vollmachtsFormularGesetzlichZulaessig=false;
    /**Select-Button Vertreter*/
    private int gruppenTextGesetzlVollmachtFehlt=0;

    /**Starttext für Vertreterdaten*/
    private int gruppenTextVertreter1nur1=0;
    private int gruppenTextVertreter1=0;
    private int gruppenTextVertreter2=0;
    
    
    /**iAuswahl1Generalversammlung
     * Nach erfolgter Anmeldung, wenn Vertreter angemeldet, aber noch keiner vorliegt. Bei 1004.
     * Wird angzeigt falls noch keine geltende Vollmacht eingetragen ist.*/
    private int gruppenTextAnmeldungMitVertreter=0;
    
    /**iAuswahl1Generalversammlung
     * Nach erfolgter Abmeldung, vor dem Button zur An-/Abmeldung, bei 707*/
    private int gruppenTextAbmeldungBestaetigung=0;
    
    /**iAuswahl1Generalversammlung
     *Nach erfolgter Anmeldung, vor Vollmachtsformularen, bei 1006
     * Wird angezeigt, wenn noch keine gesetzliche Vollmacht hinterlegt ist*/
    private int gruppenTextVorVollmachtsformular=0;
    
    /**iWeisung*/
    private int gruppenTextWeisung=0;
    /**iWeisungAendern*/
    private int gruppenTextWeisungAendern=0;
    /**iWeisungQuittung*/
    private int gruppenTextWeisungQuittung=0;
    
    
    /**iAuswahl1Generalversammlung, iAuswahl1GeneralversammlungBriefwahl*/
    /*ehemals 1007 und 1094*/
    private boolean gruppenTextVollmachtsButtonAnzeigen=false;
    private int gruppenTextVollmachtsButton=1007;
    private int gruppenTextVollmachtsFormularNr=0;

    /**iAuswahl1Generalversammlung*/
    private int gruppenTextVollmachtsFormularFuerVertreterButton=0; //früher nur 964; nun 964 für Rest und andere Nummer für Minderjährige
    private int gruppenTextVollmachtsFormularFuerVertreterNr=0;

    
    /**Für Gruppen (siehe KonstAktienregisterErgaenzung) auf true:
     * 2 - Eheleute
     * 3 - Minderjährige Einzelmitglieder
     * 4 - Eheleute Gesamthandsgemeinschaft
     */
    private boolean zweiPersonenZulaessig = false;

    private boolean gastkarteFuerMitgliedZulaessig = false;
    private boolean gastkarteFuerZweitePersonZulaessig = false;
    
    /**Für Kinder etc.: auf false. Muß gesetzlichen Vertreter benennen*/
    private boolean selbstAnmeldungOhneGesetzlichenVertreterMoeglich = false;

    private boolean eintrittskarteFuerGeneralversammlungZulaessig=false;
    
    /**Für SelectBox (in Klammer: Textnummer):
     * 1=1 Person angemeldet (600, wenn nur eine Person; 711 wenn auch zwei Personen) (Physisch=Standard)
     * 2=abgemeldet (601)
     * 3=2 Personen angemeldet (712)
     * 4=Bevollmächtigter angemeldet (988)
     * 5=1 Person angemeldet (1723) (Online)
     */
    private String anmeldung = "";

    
    /**Gesetzlicher Vertreter*/
//    private boolean gesetzVertreter1AktuellVorhanden=false;
    private String titelGesetzVertreter1="";
    private String nameGesetzVertreter1="";
    private String vornameGesetzVertreter1="";
    private String zusatzGesetzVertreter1="";
    private String strasseGesetzVertreter1="";
    private String plzGesetzVertreter1="";
    private String ortGesetzVertreter1="";
    private String mailGesetzVertreter1="";

    public void clearGesetzVertreter1() {
        titelGesetzVertreter1="";
        nameGesetzVertreter1="";
        vornameGesetzVertreter1="";
        zusatzGesetzVertreter1="";
        strasseGesetzVertreter1="";
        plzGesetzVertreter1="";
        ortGesetzVertreter1="";
        mailGesetzVertreter1="";
    }
    
    public boolean pruefeObGesetzVertreter1FelderLeer() {
        if (!titelGesetzVertreter1.isEmpty()) {return false;}
        if (!nameGesetzVertreter1.isEmpty()) {return false;}
        if (!vornameGesetzVertreter1.isEmpty()) {return false;}
        if (!zusatzGesetzVertreter1.isEmpty()) {return false;}
        if (!strasseGesetzVertreter1.isEmpty()) {return false;}
        if (!plzGesetzVertreter1.isEmpty()) {return false;}
        if (!ortGesetzVertreter1.isEmpty()) {return false;}
        if (!mailGesetzVertreter1.isEmpty()) {return false;}
        return true;
    }

    private boolean eingabeMaskeGesetzVertreterAnzeigen=false;
    private boolean listeGesetzlVertreterAnzeigen=false;
    private boolean listeGesetzlVertreterIstLeer=false;
    private List<EclVorlaeufigeVollmachtEingabe> listeGesetzlVertreter=null;
    /**True => es wurden gesetzliche Vertreter elektronisch eingereicht, die noch nicht geprüft sind*/
    private boolean ungepruefteGesetzlVertreterVorhanden=false;
    
    public boolean liefereListeGesetzlVertreterAnzeigen() {
        if (listeGesetzlVertreterAnzeigen==false || listeGesetzlVertreterIstLeer==true) {
            return false;
        }
        return true;
    }
    
    private boolean eingabeStornoGesetzVertreterAnzeigen=false;
    private EclVorlaeufigeVollmachtEingabe stornoGesetzVertreter=null;
    
    /**Vertreterdaten*/
    private boolean vertreter1AktuellVorhanden=false;
    private String titelVertreter1="";
    private String nameVertreter1="";
    private String vornameVertreter1="";
    private String zusatzVertreter1="";
    private String strasseVertreter1="";
    private String plzVertreter1="";
    private String ortVertreter1="";
    private String mailVertreter1="";
    
    /**1=Mitglied
     * 2=Sonstiges
     */
    private String artVertreter1="";
    private String sonstigeBeschreibungVertreter1="";
    
    private String aktienregisternummerVertreter1="";

    public void clearVertreter1() {
        titelVertreter1="";
        nameVertreter1="";
        vornameVertreter1="";
        zusatzVertreter1="";
        strasseVertreter1="";
        plzVertreter1="";
        ortVertreter1="";
        mailVertreter1="";
        artVertreter1="";
        sonstigeBeschreibungVertreter1="";
        aktienregisternummerVertreter1="";
            
    }

    public boolean pruefeObVertreter1FelderLeer() {
        if (!titelVertreter1.isEmpty()) {return false;}
        if (!nameVertreter1.isEmpty()) {return false;}
        if (!vornameVertreter1.isEmpty()) {return false;}
        if (!zusatzVertreter1.isEmpty()) {return false;}
        if (!strasseVertreter1.isEmpty()) {return false;}
        if (!plzVertreter1.isEmpty()) {return false;}
        if (!ortVertreter1.isEmpty()) {return false;}
        if (!mailVertreter1.isEmpty()) {return false;}
        return true;
    }
    
    private boolean eingabeMaskeBevollmaechtigterAnzeigen=false;
    private boolean listeBevollmaechtigterAnzeigen=false;
    private boolean listeBevollmaechtigterIstLeer=false;
    private List<EclVorlaeufigeVollmachtEingabe> listeBevollmaechtigte=null;
    /**True => es wurden Vertreter elektronisch eingereicht, die noch nicht geprüft sind*/
    private boolean ungepruefteBevollmaechtigterVorhanden=false;
   
    public boolean liefereListeBevollmaechtigteAnzeigen() {
        if (listeBevollmaechtigterAnzeigen==false || listeBevollmaechtigterIstLeer==true) {
            return false;
        }
        return true;
    }
    
    private boolean eingabeStornoBevollmaechtigterAnzeigen=false;
    private EclVorlaeufigeVollmachtEingabe stornoBevollmaechtigter=null;

    private boolean listeNachweiseIstLeer=false;
    private List<EclVorlaeufigeVollmachtEingabe> listeNachweise=null;
    /**True => es wurden Nachweise elektronisch eingereicht, die noch nicht geprüft sind*/
    private boolean ungepruefteNachweiseVorhanden=false;

    
    private boolean bestaetigtDassBerechtigt = false;

    
    private String nameVertreter2="";
    private String ortVertreter2="";
    
    private boolean gastkarteFuerMitglied=false;
    private boolean gastkarteFuerZweitePerson=false;
    
    /**Beim Aktionärsdialog immer auf false lassen. Nur beim Aufruf aus uLogin heraus auf true setzen*/
    private boolean vertreterAufGeprueftSetzen=false;
    
    /**Wie ohne Alt, nur für den Stand vor der Anmelde/Abmelde-Maske.
     * Wird verwendet, um beim Verlassen der Maske festzustellen,
     * ob eine Änderung durchgeführt wurde.
     */
    private String anmeldungAlt = "";
    private String nameVertreter1Alt="";
    private String ortVertreter1Alt="";
    private String nameVertreter2Alt="";
    private String ortVertreter2Alt="";
    private boolean gastkarteFuerMitgliedAlt=false;
    private boolean gastkarteFuerZweitePersonAlt=false;
    
    /**Zusatz-Info-Daten für An-/Abmeldung*/
    private int statusPraesenz=0;
    private int statusPruefung=0;
    
    
    public boolean liefereVollmachtenAberNichtAngemeldet() {
        if (angemeldet) {
            return false;
        }
        if (vollmachtGesetzlichEingetragen) {
            return true;
        }
        if (vollmachtAnDritteEingetragen) {
            return true;
        }
        return false;
    }

    public boolean liefereGesetzlichenVertreterEingeben() {
        if (selbstAnmeldungOhneGesetzlichenVertreterMoeglich==true) {
            return false;
        }
        if (anmeldung==null) {
            return false;
        }
        if (anmeldung.equals("1")
                || anmeldung.equals("3")
                || anmeldung.equals("4")
                || anmeldung.equals("5")
                ) {
            return true;
        }
        return false;
    }
    public boolean liefereBevollmaechtigterEingeben() {
        if (anmeldung==null) {
            return false;
        }
        if (anmeldung.equals("4")){
            return true;
        }
        return false;
    }
    public boolean liefereHochladenMoeglich() {
        if (liefereGesetzlichenVertreterEingeben()==true) {
            return true;
        }
        if (liefereBevollmaechtigterEingeben()==true
                && liefereAktienregisterNummerBeiBevollmaechtigtenAbfragen()==false) {
            return true;
        }
        return false;
    }
    
    public  boolean liefereAktienregisterNummerBeiBevollmaechtigtenAbfragen() {
        if (artVertreter1==null) {
            return false;
        }
        if (artVertreter1.equals("1")){
            return true;
        }
        return false;
    }
    public  boolean liefereSonstigeBeiBevollmaechtigtenAbfragen() {
        if (artVertreter1==null) {
            return false;
        }
        if (artVertreter1.equals("8")){
            return true;
        }
        return false;
    }
   
    /*******Buttons für EK-Druck anzeigen***************/
    public boolean liefereNurEinButton() {
        int anzButtons=0;
        if (gastkarteFuerMitglied) {anzButtons++;}
        if (anmeldung.equals("3")) {
            anzButtons+=2;
        }
        else {anzButtons++;}
        
        return (anzButtons==1);
        
    }
    
    public boolean liefereZweitePersonAngemeldet() {
        return (anmeldung.equals("3") || gastkarteFuerZweitePerson);
    }
    
    public boolean liefereMitgliedAlsGastAngemeldet() {
        return gastkarteFuerMitglied;
    }
    
    public boolean liefereVollmachtNormal() {
        return (anmeldung.equals("4"));
    }
    /*******Liefere Meldung zu eigenem Bestand****************/
    public EclZugeordneteMeldungNeu liefereMeldungEigenerBestand() {
        return eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe
                .get(0).zugeordneteMeldungenListe.get(0);
    }
    
    /************Gattungsabhängige Rechte für Dialogveranstaltung und FreiwilligeAnmeldung*****************/
    public boolean liefereDialogAnmeldungZulaessigFuerMeldung() {
        int gattung=eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe
                .get(0).zugeordneteMeldungenListe.get(0).getGattung();
        return eclParamM.getParam().paramPortal.veranstaltungenAktivFuerGattung[gattung-1];
    }
    public boolean liefereFreiwilligeAnmeldungZulaessigFuerMeldung() {
        if (eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe==null ||
                eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe.size()<1 ||
                eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe.get(0).zugeordneteMeldungenListe==null ||
                eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe.get(0).zugeordneteMeldungenListe.size()<1
                ) {
            return false;
        }
        int gattung=eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe
                .get(0).zugeordneteMeldungenListe.get(0).getGattung();
        CaBug.druckeLog("gattung="+gattung+" eclParamM.getParam().paramPortal.freiwilligeAnmeldungAktivFuerGattung[gattung-1]="+eclParamM.getParam().paramPortal.freiwilligeAnmeldungAktivFuerGattung[gattung-1], logDrucken, 10);
        return eclParamM.getParam().paramPortal.freiwilligeAnmeldungAktivFuerGattung[gattung-1];
    }
     
    /***********Briefwahl - für Mitglied*********************/
    public boolean liefereBriefwahlNeuMoeglich() {
        try {
            return eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe
                    .get(0).zugeordneteMeldungenListe.get(0).weitereBriefwahlMoeglich;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean liefereBriefwahlDurchAndereErteilt() {
        try {
            return eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe
                    .get(0).zugeordneteMeldungenListe.get(0).bereitsErteiltWeisungBriefwahlAllgemeinDurchAndere;
        } catch (Exception e) {
            return false;
        }
    }

    /***********Stimmrechtsvertreter - für Mitglied*********************/
    public boolean liefereSRVNeuMoeglich() {
        try {
            return eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe
                    .get(0).zugeordneteMeldungenListe.get(0).weitereSRVMoeglich;
        } catch (Exception e) {
            return false;
        }
    }

    /***************erhaltene Vollmachten***************************/
    public List<EclZugeordneteMeldungNeu> liefereVollmachtsListe() {
        try {
            return eclBesitzGesamtM.getBesitzJeKennungListe().get(0).getZugeordneteMeldungenBevollmaechtigtListe();
        } catch (Exception e) {
            return null;
        }

    }

    public List<EclZugeordneteMeldungNeu> liefereVererbteVollmachtsListe() {
        try {
            return eclBesitzGesamtM.getBesitzJeKennungListe().get(0)
                    .getZugeordneteMeldungenBevollmaechtigtMitGesetzlichListe();
        } catch (Exception e) {
            return null;
        }
    }

    /****************Werte zu Mitglied********************/

    public int liefereGattungMitglied() {
        try {
            return eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe
                    .get(0).zugeordneteMeldungenListe.get(0).gattung;
        } catch (Exception e) {
            return 0;
        }
    }

    /************Online-Teilnahme Werte *******************************/

    /**Es ist ein Bestand vorhanden, mit dem Online-Teilnahme durchgeführt werden kann
     * (unabhängig davon, ob der bereits präsent ist oder nicht - das wird dann
     * je Bestand angezeigt / ausgewertet)*/
    private boolean bestandFuerOTVorhanden = false;

    /**Eine Online-Teilnahme ist nicht möglich, weil kein entsprechender
     * Bestand vorliegt*/
    private boolean keinBestandFuerOTNormalesMitglied = false;
    private boolean keinBestandFuerOTGesetzlichZuVertretendesMitglied = false;
    private boolean keinBestandFuerOTSonstigePerson = false;

    /**Eine Online-Teilnahme für den eigenen Bestand ist nicht möglich, weil keine entsprechende
     * Anmeldungvorliegt*/
    private boolean keinEigenerBestandFuerOTNormalesMitglied = false;

    /**Eigener Bestand kann OT vertreten werden (=selbst angemeldet,
     * und keine gesetzliche Vertretung erforderlich)*/
    private boolean eigenerBestandOTVertretbar = false;

    /**Es sind Bestände zugeordnet, die noch vertreten werden können,
     * aber aktuell nicht präsent sind.
     * 0 = kein Bestand
     * 1 = 1 einziger Bestand
     * >1 = mehrere Bestände
     */
    private int anzNichtPraesenteVorhanden = 0;

    /**Es sind Bestände zugeordnet, die noch vertreten werden können,
     * aber aktuell nicht präsent sind.
     * 0 = kein Bestand
     * 1 = 1 einziger Bestand
     * >1 = mehrere Bestände
     */
    private int anzPraesenteVorhanden = 0;

    /*++++++++++Online-Teilnahme Gast+++++++++++++++++++++++*/

    private boolean gastPraesent = false;

    /**Gast ist noch nicht präsent, und Aktionär ist auch noch nicht präsent
     * (zweiter Teil eigentlich hier unnötig abzufragen, aber wg. Kompatibilität zu normalem Menü drin)*/
    public boolean liefereZugangAlsGastZulaessig() {
        if (gastPraesent) {
            return false;
        }
        if (anzPraesenteVorhanden > 0) {
            return false;
        }
        return true;
    }

    /**Kann als Gast nicht zugehen, weil auch als Aktionär möglich und entsprechende Parameterstellung*/
    public boolean liefereZugangNurAlsAktionaerMoeglich() {
        if (anzNichtPraesenteVorhanden > 0 && eclParamM.getParam().paramPortal.onlineTeilnahmeAktionaerAlsGast == 0) {
            return true;
        }
        return false;
    }

    /**Abgang möglich, da Gast bereits präsent*/
    public boolean liefereAbgangAlsGastZulaessig() {
        if (gastPraesent) {
            return true;
        }
        return false;
    }

    /*++++++++++++++Listen für Online-Teilnahme - nicht präsent+++++++++++++++++++++*/
    /*+++Eigene+++*/
    public boolean liefereEigeneNichtPraesentVorhanden() {
        try {
            int laenge = eclBesitzGesamtM.getBesitzJeKennungListeNichtPraesent().get(0).eigenerAREintragListe
                    .get(0).zugeordneteMeldungenListe.size();
            return (laenge > 0);
        } catch (Exception e) {
            return false;
        }
    }

    public List<EclZugeordneteMeldungNeu> liefereEigeneNichtPraesent() {
        try {
            return eclBesitzGesamtM.getBesitzJeKennungListeNichtPraesent().get(0).eigenerAREintragListe
                    .get(0).zugeordneteMeldungenListe;
        } catch (Exception e) {
            return null;
        }
    }

    /*+++Vertretene+++*/
    public boolean liefereVertreteneNichtPraesentVorhanden() {
        try {
            int laenge = eclBesitzGesamtM.getBesitzJeKennungListeNichtPraesent()
                    .get(0).zugeordneteMeldungenBevollmaechtigtListe.size();
            return (laenge > 0);
        } catch (Exception e) {
            return false;
        }
    }

    public List<EclZugeordneteMeldungNeu> liefereVertreteneNichtPraesent() {
        try {
            return eclBesitzGesamtM.getBesitzJeKennungListeNichtPraesent()
                    .get(0).zugeordneteMeldungenBevollmaechtigtListe;
        } catch (Exception e) {
            return null;
        }
    }

    /*+++Geerbte+++*/
    public boolean liefereGeerbteNichtPraesentVorhanden() {
        try {
            int laenge = eclBesitzGesamtM.getBesitzJeKennungListeNichtPraesent()
                    .get(0).zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe.size();
            return (laenge > 0);
        } catch (Exception e) {
            return false;
        }
    }

    public List<EclZugeordneteMeldungNeu> liefereGeerbteNichtPraesent() {
        try {
            return eclBesitzGesamtM.getBesitzJeKennungListeNichtPraesent()
                    .get(0).zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe;
        } catch (Exception e) {
            return null;
        }
    }

    /*++++++++++++++Listen für Online-Teilnahme - präsent+++++++++++++++++++++*/
    /*+++Eigene+++*/
    public boolean liefereEigenePraesentVorhanden() {
        int laenge = 0;
        try {
            laenge = eclBesitzGesamtM.getBesitzJeKennungListePraesent().get(0).eigenerAREintragListe
                    .get(0).zugeordneteMeldungenListe.size();
        } catch (Exception e) {
            return false;
        }
        return (laenge > 0);
    }

    public List<EclZugeordneteMeldungNeu> liefereEigenePraesent() {
        CaBug.druckeLog("", logDrucken, 10);
        try {
            if (CaBug.pruefeLog(logDrucken, 10)) {
                for (EclZugeordneteMeldungNeu iZugeordneteMeldung: eclBesitzGesamtM.getBesitzJeKennungListePraesent().get(0).eigenerAREintragListe
                        .get(0).zugeordneteMeldungenListe) {
                    System.out.println("iZugeordneteMeldung="+iZugeordneteMeldung.aktionaersnummerFuerAnzeige);
                }
            }
            return eclBesitzGesamtM.getBesitzJeKennungListePraesent().get(0).eigenerAREintragListe
                    .get(0).zugeordneteMeldungenListe;
        } catch (Exception e) {
            CaBug.druckeLog("return null", logDrucken, 10);
            return null;
        }
    }

    /*+++Vertretene+++*/
    public boolean liefereVertretenePraesentVorhanden() {
        int laenge = 0;
        try {
            laenge = eclBesitzGesamtM.getBesitzJeKennungListePraesent().get(0).zugeordneteMeldungenBevollmaechtigtListe
                    .size();
        } catch (Exception e) {
            return false;
        }
        return (laenge > 0);
    }

    public List<EclZugeordneteMeldungNeu> liefereVertretenePraesent() {
        try {
            return eclBesitzGesamtM.getBesitzJeKennungListePraesent().get(0).zugeordneteMeldungenBevollmaechtigtListe;
        } catch (Exception e) {
            return null;
        }
    }

    /*+++Geerbte+++*/
    public boolean liefereGeerbtePraesentVorhanden() {
        int laenge = 0;
        try {
            laenge = eclBesitzGesamtM.getBesitzJeKennungListePraesent()
                    .get(0).zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe.size();
        } catch (Exception e) {
            return false;
        }
        return (laenge > 0);
    }

    public List<EclZugeordneteMeldungNeu> liefereGeerbtePraesent() {
        try {
            return eclBesitzGesamtM.getBesitzJeKennungListePraesent()
                    .get(0).zugeordneteMeldungenBevollmaechtigtMitGesetzlichListe;
        } catch (Exception e) {
            return null;
        }
    }

    //	/************Online-Teilnahme Werte - Kombi aus Mitglied und Vollmachten*****/
    //	/*XXX?*/
    //	private boolean tteilnahmeMoeglich=false;

    public void clearMitgliedsdaten() {
        eigenerAREintragVorhanden = false;
        erhalteneVollmachtenVorhanden = false;

        gruppe=-1;
        
        gruppenTextStartAnAbmeldung=0;
        gruppenTextAnmeldungMitVertreter=0;
        gruppenTextAbmeldungBestaetigung=0;
        gruppenTextVorVollmachtsformular=0;

        gruppenTextWeisung=0;
        gruppenTextWeisungAendern=0;
        gruppenTextWeisungQuittung=0;
        gruppenTextVollmachtsButtonAnzeigen=false;
        gruppenTextVollmachtsButton=1007;
        gruppenTextVollmachtsFormularNr=0;
        
        anmeldungMoeglich = false;
        anOderAbgemeldet = false;
        angemeldet = false;
        abgemeldet = false;
        zweiPersonenAngemeldet = false;
        vertreterAngemeldet = false;
        vollmachtAnDritteEingetragen = false;
        vollmachtGesetzlichEingetragen = false;

        zweiPersonenZulaessig = false;
        selbstAnmeldungOhneGesetzlichenVertreterMoeglich = false;

        anmeldung = "";
        nameVertreter1="";
        ortVertreter1="";
        bestaetigtDassBerechtigt = false;
        nameVertreter2="";
        ortVertreter2="";
        gastkarteFuerMitglied=false;
        gastkarteFuerZweitePerson=false;

        vertreterAufGeprueftSetzen=false;
        
        anmeldungAlt = "";
        nameVertreter1Alt="";
        ortVertreter1Alt="";
        nameVertreter2Alt="";
        ortVertreter2Alt="";
        gastkarteFuerMitgliedAlt=false;
        gastkarteFuerZweitePersonAlt=false;
 
        statusPraesenz=0;
        statusPruefung=0;

        bestandFuerOTVorhanden = false;
        keinBestandFuerOTNormalesMitglied = false;
        keinBestandFuerOTGesetzlichZuVertretendesMitglied = false;
        keinBestandFuerOTSonstigePerson = false;
        keinEigenerBestandFuerOTNormalesMitglied = false;
        eigenerBestandOTVertretbar = false;
        anzNichtPraesenteVorhanden = 0;
        anzPraesenteVorhanden = 0;
    }

    /************************Standard getter und setter***************************************/

    public boolean isEigenerAREintragVorhanden() {
        return eigenerAREintragVorhanden;
    }

    public void setEigenerAREintragVorhanden(boolean eigenerAREintragVorhanden) {
        this.eigenerAREintragVorhanden = eigenerAREintragVorhanden;
    }

    public boolean isErhalteneVollmachtenVorhanden() {
        return erhalteneVollmachtenVorhanden;
    }

    public void setErhalteneVollmachtenVorhanden(boolean erhalteneVollmachtenVorhanden) {
        this.erhalteneVollmachtenVorhanden = erhalteneVollmachtenVorhanden;
    }

    public boolean isAnmeldungMoeglich() {
        return anmeldungMoeglich;
    }

    public void setAnmeldungMoeglich(boolean anmeldungMoeglich) {
        this.anmeldungMoeglich = anmeldungMoeglich;
    }

    public boolean isAnOderAbgemeldet() {
        return anOderAbgemeldet;
    }

    public void setAnOderAbgemeldet(boolean anOderAbgemeldet) {
        this.anOderAbgemeldet = anOderAbgemeldet;
    }

    public boolean isAngemeldet() {
        return angemeldet;
    }

    public void setAngemeldet(boolean angemeldet) {
        this.angemeldet = angemeldet;
    }

    public boolean isAbgemeldet() {
        return abgemeldet;
    }

    public void setAbgemeldet(boolean abgemeldet) {
        this.abgemeldet = abgemeldet;
    }

    public boolean isZweiPersonenZulaessig() {
        return zweiPersonenZulaessig;
    }

    public void setZweiPersonenZulaessig(boolean zweiPersonenZulaessig) {
        this.zweiPersonenZulaessig = zweiPersonenZulaessig;
    }

    public String getAnmeldung() {
        return anmeldung;
    }

    public void setAnmeldung(String anmeldung) {
        this.anmeldung = anmeldung;
    }

    public boolean isZweiPersonenAngemeldet() {
        return zweiPersonenAngemeldet;
    }

    public void setZweiPersonenAngemeldet(boolean zweiPersonenAngemeldet) {
        this.zweiPersonenAngemeldet = zweiPersonenAngemeldet;
    }

    public boolean isVertreterAngemeldet() {
        return vertreterAngemeldet;
    }

    public void setVertreterAngemeldet(boolean vertreterAngemeldet) {
        this.vertreterAngemeldet = vertreterAngemeldet;
    }

    public List<EclVorlaeufigeVollmachtFuerAnzeige> getListeAllerVollmachten() {
        return listeAllerVollmachten;
    }

    public void setListeAllerVollmachten(List<EclVorlaeufigeVollmachtFuerAnzeige> listeAllerVollmachten) {
        this.listeAllerVollmachten = listeAllerVollmachten;
    }

    public boolean isSelbstAnmeldungOhneGesetzlichenVertreterMoeglich() {
        return selbstAnmeldungOhneGesetzlichenVertreterMoeglich;
    }

    public void setSelbstAnmeldungOhneGesetzlichenVertreterMoeglich(
            boolean selbstAnmeldungOhneGesetzlichenVertreterMoeglich) {
        this.selbstAnmeldungOhneGesetzlichenVertreterMoeglich = selbstAnmeldungOhneGesetzlichenVertreterMoeglich;
    }

    public EclBesitzGesamtM getEclBesitzGesamtM() {
        return eclBesitzGesamtM;
    }

    public void setEclBesitzGesamtM(EclBesitzGesamtM eclBesitzGesamtM) {
        this.eclBesitzGesamtM = eclBesitzGesamtM;
    }

    public List<EclVorlaeufigeVollmachtFuerAnzeige> getListeAllerGesetzlichenVollmachten() {
        return listeAllerGesetzlichenVollmachten;
    }

    public void setListeAllerGesetzlichenVollmachten(
            List<EclVorlaeufigeVollmachtFuerAnzeige> listeAllerGesetzlichenVollmachten) {
        this.listeAllerGesetzlichenVollmachten = listeAllerGesetzlichenVollmachten;
    }

    public List<EclVorlaeufigeVollmachtFuerAnzeige> getListeAllerAnDritteVollmachten() {
        return listeAllerAnDritteVollmachten;
    }

    public void setListeAllerAnDritteVollmachten(
            List<EclVorlaeufigeVollmachtFuerAnzeige> listeAllerAnDritteVollmachten) {
        this.listeAllerAnDritteVollmachten = listeAllerAnDritteVollmachten;
    }

    public boolean isVollmachtAnDritteEingetragen() {
        return vollmachtAnDritteEingetragen;
    }

    public void setVollmachtAnDritteEingetragen(boolean vollmachtAnDritteEingetragen) {
        this.vollmachtAnDritteEingetragen = vollmachtAnDritteEingetragen;
    }

    public EclVorlaeufigeVollmachtFuerAnzeige getVollmachtAnDritteEingetragenEcl() {
        return vollmachtAnDritteEingetragenEcl;
    }

    public void setVollmachtAnDritteEingetragenEcl(EclVorlaeufigeVollmachtFuerAnzeige vollmachtAnDritteEingetragenEcl) {
        this.vollmachtAnDritteEingetragenEcl = vollmachtAnDritteEingetragenEcl;
    }

    public boolean isVollmachtGesetzlichEingetragen() {
        return vollmachtGesetzlichEingetragen;
    }

    public void setVollmachtGesetzlichEingetragen(boolean vollmachtGesetzlichEingetragen) {
        this.vollmachtGesetzlichEingetragen = vollmachtGesetzlichEingetragen;
    }

    public EclVorlaeufigeVollmachtFuerAnzeige getVollmachtGesetzlichEingetragenEcl() {
        return vollmachtGesetzlichEingetragenEcl;
    }

    public void setVollmachtGesetzlichEingetragenEcl(
            EclVorlaeufigeVollmachtFuerAnzeige vollmachtGesetzlichEingetragenEcl) {
        this.vollmachtGesetzlichEingetragenEcl = vollmachtGesetzlichEingetragenEcl;
    }

    public List<EclVorlaeufigeVollmachtFuerAnzeige> getListeAllerMitGesetzlichenVollmachten() {
        return listeAllerMitGesetzlichenVollmachten;
    }

    public void setListeAllerMitGesetzlichenVollmachten(
            List<EclVorlaeufigeVollmachtFuerAnzeige> listeAllerMitGesetzlichenVollmachten) {
        this.listeAllerMitGesetzlichenVollmachten = listeAllerMitGesetzlichenVollmachten;
    }

    public boolean isVererbteVollmachtenVorhanden() {
        return vererbteVollmachtenVorhanden;
    }

    public void setVererbteVollmachtenVorhanden(boolean vererbteVollmachtenVorhanden) {
        this.vererbteVollmachtenVorhanden = vererbteVollmachtenVorhanden;
    }

    public boolean isKeinBestandFuerOTNormalesMitglied() {
        return keinBestandFuerOTNormalesMitglied;
    }

    public void setKeinBestandFuerOTNormalesMitglied(boolean keinBestandFuerOTNormalesMitglied) {
        this.keinBestandFuerOTNormalesMitglied = keinBestandFuerOTNormalesMitglied;
    }

    public boolean isKeinBestandFuerOTGesetzlichZuVertretendesMitglied() {
        return keinBestandFuerOTGesetzlichZuVertretendesMitglied;
    }

    public void setKeinBestandFuerOTGesetzlichZuVertretendesMitglied(
            boolean keinBestandFuerOTGesetzlichZuVertretendesMitglied) {
        this.keinBestandFuerOTGesetzlichZuVertretendesMitglied = keinBestandFuerOTGesetzlichZuVertretendesMitglied;
    }

    public boolean isKeinBestandFuerOTSonstigePerson() {
        return keinBestandFuerOTSonstigePerson;
    }

    public void setKeinBestandFuerOTSonstigePerson(boolean keinBestandFuerOTSonstigePerson) {
        this.keinBestandFuerOTSonstigePerson = keinBestandFuerOTSonstigePerson;
    }

    public boolean isBestandFuerOTVorhanden() {
        return bestandFuerOTVorhanden;
    }

    public void setBestandFuerOTVorhanden(boolean bestandFuerOTVorhanden) {
        this.bestandFuerOTVorhanden = bestandFuerOTVorhanden;
    }

    public boolean isEigenerBestandOTVertretbar() {
        return eigenerBestandOTVertretbar;
    }

    public void setEigenerBestandOTVertretbar(boolean eigenerBestandOTVertretbar) {
        this.eigenerBestandOTVertretbar = eigenerBestandOTVertretbar;
    }

    public int getAnzNichtPraesenteVorhanden() {
        return anzNichtPraesenteVorhanden;
    }

    public void setAnzNichtPraesenteVorhanden(int anzNichtPraesenteVorhanden) {
        this.anzNichtPraesenteVorhanden = anzNichtPraesenteVorhanden;
    }

    public int getAnzPraesenteVorhanden() {
        return anzPraesenteVorhanden;
    }

    public void setAnzPraesenteVorhanden(int anzPraesenteVorhanden) {
        this.anzPraesenteVorhanden = anzPraesenteVorhanden;
    }

    public boolean isKeinEigenerBestandFuerOTNormalesMitglied() {
        return keinEigenerBestandFuerOTNormalesMitglied;
    }

    public void setKeinEigenerBestandFuerOTNormalesMitglied(boolean keinEigenerBestandFuerOTNormalesMitglied) {
        this.keinEigenerBestandFuerOTNormalesMitglied = keinEigenerBestandFuerOTNormalesMitglied;
    }

    public boolean isGastPraesent() {
        return gastPraesent;
    }

    public void setGastPraesent(boolean gastPraesent) {
        this.gastPraesent = gastPraesent;
    }


    public int getGruppe() {
        return gruppe;
    }


    public void setGruppe(int gruppe) {
        this.gruppe = gruppe;
    }


    public int getGruppenTextStartAnAbmeldung() {
        return gruppenTextStartAnAbmeldung;
    }


    public void setGruppenTextStartAnAbmeldung(int gruppenTextStartAnAbmeldung) {
        this.gruppenTextStartAnAbmeldung = gruppenTextStartAnAbmeldung;
    }


    public int getGruppenTextAnmeldungMitVertreter() {
        return gruppenTextAnmeldungMitVertreter;
    }


    public void setGruppenTextAnmeldungMitVertreter(int gruppenTextAnmeldungMitVertreter) {
        this.gruppenTextAnmeldungMitVertreter = gruppenTextAnmeldungMitVertreter;
    }


    public int getGruppenTextAbmeldungBestaetigung() {
        return gruppenTextAbmeldungBestaetigung;
    }


    public void setGruppenTextAbmeldungBestaetigung(int gruppenTextAbmeldungBestaetigung) {
        this.gruppenTextAbmeldungBestaetigung = gruppenTextAbmeldungBestaetigung;
    }


    public int getGruppenTextVorVollmachtsformular() {
        return gruppenTextVorVollmachtsformular;
    }


    public void setGruppenTextVorVollmachtsformular(int gruppenTextVorVollmachtsformular) {
        this.gruppenTextVorVollmachtsformular = gruppenTextVorVollmachtsformular;
    }


    public int getGruppenTextWeisung() {
        return gruppenTextWeisung;
    }


    public void setGruppenTextWeisung(int gruppenTextWeisung) {
        this.gruppenTextWeisung = gruppenTextWeisung;
    }


    public int getGruppenTextWeisungAendern() {
        return gruppenTextWeisungAendern;
    }


    public void setGruppenTextWeisungAendern(int gruppenTextWeisungAendern) {
        this.gruppenTextWeisungAendern = gruppenTextWeisungAendern;
    }


    public int getGruppenTextWeisungQuittung() {
        return gruppenTextWeisungQuittung;
    }


    public void setGruppenTextWeisungQuittung(int gruppenTextWeisungQuittung) {
        this.gruppenTextWeisungQuittung = gruppenTextWeisungQuittung;
    }


    public boolean isGruppenTextVollmachtsButtonAnzeigen() {
        return gruppenTextVollmachtsButtonAnzeigen;
    }


    public void setGruppenTextVollmachtsButtonAnzeigen(boolean gruppenTextVollmachtsButtonAnzeigen) {
        this.gruppenTextVollmachtsButtonAnzeigen = gruppenTextVollmachtsButtonAnzeigen;
    }


    public int getGruppenTextVollmachtsButton() {
        return gruppenTextVollmachtsButton;
    }


    public void setGruppenTextVollmachtsButton(int gruppenTextVollmachtsButton) {
        this.gruppenTextVollmachtsButton = gruppenTextVollmachtsButton;
    }


    public int getGruppenTextVollmachtsFormularNr() {
        return gruppenTextVollmachtsFormularNr;
    }


    public void setGruppenTextVollmachtsFormularNr(int gruppenTextVollmachtsFormularNr) {
        this.gruppenTextVollmachtsFormularNr = gruppenTextVollmachtsFormularNr;
    }


    public int getGruppenTextVollmachtsFormularFuerVertreterNr() {
        return gruppenTextVollmachtsFormularFuerVertreterNr;
    }


    public void setGruppenTextVollmachtsFormularFuerVertreterNr(int gruppenTextVollmachtsFormularFuerVertreterNr) {
        this.gruppenTextVollmachtsFormularFuerVertreterNr = gruppenTextVollmachtsFormularFuerVertreterNr;
    }


    public int getGruppenTextVollmachtsFormularFuerVertreterButton() {
        return gruppenTextVollmachtsFormularFuerVertreterButton;
    }


    public void setGruppenTextVollmachtsFormularFuerVertreterButton(int gruppenTextVollmachtsFormularFuerVertreterButton) {
        this.gruppenTextVollmachtsFormularFuerVertreterButton = gruppenTextVollmachtsFormularFuerVertreterButton;
    }


    public boolean isGastkarteFuerMitgliedZulaessig() {
        return gastkarteFuerMitgliedZulaessig;
    }


    public void setGastkarteFuerMitgliedZulaessig(boolean gastkarteFuerMitgliedZulaessig) {
        this.gastkarteFuerMitgliedZulaessig = gastkarteFuerMitgliedZulaessig;
    }


    public String getNameVertreter1() {
        return nameVertreter1;
    }


    public void setNameVertreter1(String nameVertreter1) {
        this.nameVertreter1 = nameVertreter1;
    }


    public String getOrtVertreter1() {
        return ortVertreter1;
    }


    public void setOrtVertreter1(String ortVertreter1) {
        this.ortVertreter1 = ortVertreter1;
    }


    public String getNameVertreter2() {
        return nameVertreter2;
    }


    public void setNameVertreter2(String nameVertreter2) {
        this.nameVertreter2 = nameVertreter2;
    }


    public String getOrtVertreter2() {
        return ortVertreter2;
    }


    public void setOrtVertreter2(String ortVertreter2) {
        this.ortVertreter2 = ortVertreter2;
    }


    public boolean isGastkarteFuerMitglied() {
        return gastkarteFuerMitglied;
    }


    public void setGastkarteFuerMitglied(boolean gastkarteFuerMitglied) {
        this.gastkarteFuerMitglied = gastkarteFuerMitglied;
    }


    public String getAnmeldungAlt() {
        return anmeldungAlt;
    }


    public void setAnmeldungAlt(String anmeldungAlt) {
        this.anmeldungAlt = anmeldungAlt;
    }


    public String getNameVertreter1Alt() {
        return nameVertreter1Alt;
    }


    public void setNameVertreter1Alt(String nameVertreter1Alt) {
        this.nameVertreter1Alt = nameVertreter1Alt;
    }


    public String getOrtVertreter1Alt() {
        return ortVertreter1Alt;
    }


    public void setOrtVertreter1Alt(String ortVertreter1Alt) {
        this.ortVertreter1Alt = ortVertreter1Alt;
    }


    public String getNameVertreter2Alt() {
        return nameVertreter2Alt;
    }


    public void setNameVertreter2Alt(String nameVertreter2Alt) {
        this.nameVertreter2Alt = nameVertreter2Alt;
    }


    public String getOrtVertreter2Alt() {
        return ortVertreter2Alt;
    }


    public void setOrtVertreter2Alt(String ortVertreter2Alt) {
        this.ortVertreter2Alt = ortVertreter2Alt;
    }


    public boolean isGastkarteFuerMitgliedAlt() {
        return gastkarteFuerMitgliedAlt;
    }


    public void setGastkarteFuerMitgliedAlt(boolean gastkarteFuerMitgliedAlt) {
        this.gastkarteFuerMitgliedAlt = gastkarteFuerMitgliedAlt;
    }


    public int getGruppenTextZweiPersonen() {
        return gruppenTextZweiPersonen;
    }


    public void setGruppenTextZweiPersonen(int gruppenTextZweiPersonen) {
        this.gruppenTextZweiPersonen = gruppenTextZweiPersonen;
    }


    public int getGruppenTextVertreter1() {
        return gruppenTextVertreter1;
    }


    public void setGruppenTextVertreter1(int gruppenTextVertreter1) {
        this.gruppenTextVertreter1 = gruppenTextVertreter1;
    }


    public int getGruppenTextVertreter2() {
        return gruppenTextVertreter2;
    }


    public void setGruppenTextVertreter2(int gruppenTextVertreter2) {
        this.gruppenTextVertreter2 = gruppenTextVertreter2;
    }


    public int getGruppenTextEinePerson() {
        return gruppenTextEinePerson;
    }


    public void setGruppenTextEinePerson(int gruppenTextEinePerson) {
        this.gruppenTextEinePerson = gruppenTextEinePerson;
    }


    public boolean isGastkarteFuerZweitePerson() {
        return gastkarteFuerZweitePerson;
    }


    public void setGastkarteFuerZweitePerson(boolean gastkarteFuerZweitePerson) {
        this.gastkarteFuerZweitePerson = gastkarteFuerZweitePerson;
    }


    public boolean isGastkarteFuerZweitePersonAlt() {
        return gastkarteFuerZweitePersonAlt;
    }


    public void setGastkarteFuerZweitePersonAlt(boolean gastkarteFuerZweitePersonAlt) {
        this.gastkarteFuerZweitePersonAlt = gastkarteFuerZweitePersonAlt;
    }


    public boolean isGastkarteFuerZweitePersonZulaessig() {
        CaBug.druckeLog("gastkarteFuerZweitePersonZulaessig="+gastkarteFuerZweitePersonZulaessig, logDrucken, 10);
        return gastkarteFuerZweitePersonZulaessig;
    }


    public void setGastkarteFuerZweitePersonZulaessig(boolean gastkarteFuerZweitePersonZulaessig) {
        this.gastkarteFuerZweitePersonZulaessig = gastkarteFuerZweitePersonZulaessig;
    }


    public int getGruppenTextAbmelden() {
        return gruppenTextAbmelden;
    }


    public void setGruppenTextAbmelden(int gruppenTextAbmelden) {
        this.gruppenTextAbmelden = gruppenTextAbmelden;
    }


    public int getGruppenTextVertreter() {
        return gruppenTextVertreter;
    }


    public void setGruppenTextVertreter(int gruppenTextVertreter) {
        this.gruppenTextVertreter = gruppenTextVertreter;
    }


    public boolean isVollmachtsFormularGesetzlichZulaessig() {
        return vollmachtsFormularGesetzlichZulaessig;
    }


    public void setVollmachtsFormularGesetzlichZulaessig(boolean vollmachtsFormularGesetzlichZulaessig) {
        this.vollmachtsFormularGesetzlichZulaessig = vollmachtsFormularGesetzlichZulaessig;
    }


    public int getGruppenTextGesetzlVollmachtFehlt() {
        return gruppenTextGesetzlVollmachtFehlt;
    }


    public void setGruppenTextGesetzlVollmachtFehlt(int gruppenTextGesetzlVollmachtFehlt) {
        this.gruppenTextGesetzlVollmachtFehlt = gruppenTextGesetzlVollmachtFehlt;
    }


    public int getGruppenTextVertreter1nur1() {
        return gruppenTextVertreter1nur1;
    }


    public void setGruppenTextVertreter1nur1(int gruppenTextVertreter1nur1) {
        this.gruppenTextVertreter1nur1 = gruppenTextVertreter1nur1;
    }


    public boolean isEkDruckWirdAngezeigt() {
        return ekDruckWirdAngezeigt;
    }


    public void setEkDruckWirdAngezeigt(boolean ekDruckWirdAngezeigt) {
        this.ekDruckWirdAngezeigt = ekDruckWirdAngezeigt;
    }


    public boolean isVertreterAufGeprueftSetzen() {
        return vertreterAufGeprueftSetzen;
    }


    public void setVertreterAufGeprueftSetzen(boolean vertreterAufGeprueftSetzen) {
        this.vertreterAufGeprueftSetzen = vertreterAufGeprueftSetzen;
    }


    public int getStatusPraesenz() {
        return statusPraesenz;
    }


    public void setStatusPraesenz(int statusPraesenz) {
        this.statusPraesenz = statusPraesenz;
    }


    public int getStatusPruefung() {
        return statusPruefung;
    }


    public void setStatusPruefung(int statusPruefung) {
        this.statusPruefung = statusPruefung;
    }


    public boolean isEintrittskarteFuerGeneralversammlungZulaessig() {
        return eintrittskarteFuerGeneralversammlungZulaessig;
    }


    public void setEintrittskarteFuerGeneralversammlungZulaessig(boolean eintrittskarteFuerGeneralversammlungZulaessig) {
        this.eintrittskarteFuerGeneralversammlungZulaessig = eintrittskarteFuerGeneralversammlungZulaessig;
    }


    public boolean isBestaetigtDassBerechtigt() {
        return bestaetigtDassBerechtigt;
    }


    public void setBestaetigtDassBerechtigt(boolean bestaetigtDassBerechtigt) {
        this.bestaetigtDassBerechtigt = bestaetigtDassBerechtigt;
    }


    public String getVornameVertreter1() {
        return vornameVertreter1;
    }


    public void setVornameVertreter1(String vornameVertreter1) {
        this.vornameVertreter1 = vornameVertreter1;
    }

    public String getArtVertreter1() {
        return artVertreter1;
    }

    public void setArtVertreter1(String artVertreter1) {
        this.artVertreter1 = artVertreter1;
    }

    public String getSonstigeBeschreibungVertreter1() {
        return sonstigeBeschreibungVertreter1;
    }

    public void setSonstigeBeschreibungVertreter1(String sonstigeBeschreibungVertreter1) {
        this.sonstigeBeschreibungVertreter1 = sonstigeBeschreibungVertreter1;
    }

    public String getAktienregisternummerVertreter1() {
        return aktienregisternummerVertreter1;
    }

    public void setAktienregisternummerVertreter1(String aktienregisternummerVertreter1) {
        this.aktienregisternummerVertreter1 = aktienregisternummerVertreter1;
    }

    public String getNameGesetzVertreter1() {
        return nameGesetzVertreter1;
    }

    public void setNameGesetzVertreter1(String nameGesetzVertreter1) {
        this.nameGesetzVertreter1 = nameGesetzVertreter1;
    }

    public String getVornameGesetzVertreter1() {
        return vornameGesetzVertreter1;
    }

    public void setVornameGesetzVertreter1(String vornameGesetzVertreter1) {
        this.vornameGesetzVertreter1 = vornameGesetzVertreter1;
    }

    public String getOrtGesetzVertreter1() {
        return ortGesetzVertreter1;
    }

    public void setOrtGesetzVertreter1(String ortGesetzVertreter1) {
        this.ortGesetzVertreter1 = ortGesetzVertreter1;
    }

    public String getTitelGesetzVertreter1() {
        return titelGesetzVertreter1;
    }

    public void setTitelGesetzVertreter1(String titelGesetzVertreter1) {
        this.titelGesetzVertreter1 = titelGesetzVertreter1;
    }

    public String getZusatzGesetzVertreter1() {
        return zusatzGesetzVertreter1;
    }

    public void setZusatzGesetzVertreter1(String zusatzGesetzVertreter1) {
        this.zusatzGesetzVertreter1 = zusatzGesetzVertreter1;
    }

    public String getStrasseGesetzVertreter1() {
        return strasseGesetzVertreter1;
    }

    public void setStrasseGesetzVertreter1(String strasseGesetzVertreter1) {
        this.strasseGesetzVertreter1 = strasseGesetzVertreter1;
    }

    public String getPlzGesetzVertreter1() {
        return plzGesetzVertreter1;
    }

    public void setPlzGesetzVertreter1(String plzGesetzVertreter1) {
        this.plzGesetzVertreter1 = plzGesetzVertreter1;
    }

    public String getMailGesetzVertreter1() {
        return mailGesetzVertreter1;
    }

    public void setMailGesetzVertreter1(String mailGesetzVertreter1) {
        this.mailGesetzVertreter1 = mailGesetzVertreter1;
    }

    public String getTitelVertreter1() {
        return titelVertreter1;
    }

    public void setTitelVertreter1(String titelVertreter1) {
        this.titelVertreter1 = titelVertreter1;
    }

    public String getZusatzVertreter1() {
        return zusatzVertreter1;
    }

    public void setZusatzVertreter1(String zusatzVertreter1) {
        this.zusatzVertreter1 = zusatzVertreter1;
    }

    public String getStrasseVertreter1() {
        return strasseVertreter1;
    }

    public void setStrasseVertreter1(String strasseVertreter1) {
        this.strasseVertreter1 = strasseVertreter1;
    }

    public String getPlzVertreter1() {
        return plzVertreter1;
    }

    public void setPlzVertreter1(String plzVertreter1) {
        this.plzVertreter1 = plzVertreter1;
    }

    public String getMailVertreter1() {
        return mailVertreter1;
    }

    public void setMailVertreter1(String mailVertreter1) {
        this.mailVertreter1 = mailVertreter1;
    }

//    public boolean isGesetzVertreter1AktuellVorhanden() {
//        return gesetzVertreter1AktuellVorhanden;
//    }
//
//    public void setGesetzVertreter1AktuellVorhanden(boolean gesetzVertreter1AktuellVorhanden) {
//        this.gesetzVertreter1AktuellVorhanden = gesetzVertreter1AktuellVorhanden;
//    }

    public boolean isVertreter1AktuellVorhanden() {
        return vertreter1AktuellVorhanden;
    }

    public void setVertreter1AktuellVorhanden(boolean vertreter1AktuellVorhanden) {
        this.vertreter1AktuellVorhanden = vertreter1AktuellVorhanden;
    }

    public boolean isEingabeMaskeGesetzVertreterAnzeigen() {
        return eingabeMaskeGesetzVertreterAnzeigen;
    }

    public void setEingabeMaskeGesetzVertreterAnzeigen(boolean eingabeMaskeGesetzVertreterAnzeigen) {
        this.eingabeMaskeGesetzVertreterAnzeigen = eingabeMaskeGesetzVertreterAnzeigen;
    }

    public List<EclVorlaeufigeVollmachtEingabe> getListeGesetzlVertreter() {
        return listeGesetzlVertreter;
    }

    public void setListeGesetzlVertreter(List<EclVorlaeufigeVollmachtEingabe> listeGesetzlVertreter) {
        this.listeGesetzlVertreter = listeGesetzlVertreter;
    }

    public boolean isListeGesetzlVertreterIstLeer() {
        return listeGesetzlVertreterIstLeer;
    }

    public void setListeGesetzlVertreterIstLeer(boolean listeGesetzlVertreterIstLeer) {
        this.listeGesetzlVertreterIstLeer = listeGesetzlVertreterIstLeer;
    }

    public boolean isEingabeStornoGesetzVertreterAnzeigen() {
        return eingabeStornoGesetzVertreterAnzeigen;
    }

    public void setEingabeStornoGesetzVertreterAnzeigen(boolean eingabeStornoGesetzVertreterAnzeigen) {
        this.eingabeStornoGesetzVertreterAnzeigen = eingabeStornoGesetzVertreterAnzeigen;
    }

    public boolean isListeGesetzlVertreterAnzeigen() {
        return listeGesetzlVertreterAnzeigen;
    }

    public void setListeGesetzlVertreterAnzeigen(boolean listeGesetzlVertreterAnzeigen) {
        this.listeGesetzlVertreterAnzeigen = listeGesetzlVertreterAnzeigen;
    }

    public EclVorlaeufigeVollmachtEingabe getStornoGesetzVertreter() {
        return stornoGesetzVertreter;
    }

    public void setStornoGesetzVertreter(EclVorlaeufigeVollmachtEingabe stornoGesetzVertreter) {
        this.stornoGesetzVertreter = stornoGesetzVertreter;
    }

    public boolean isEingabeMaskeBevollmaechtigterAnzeigen() {
        return eingabeMaskeBevollmaechtigterAnzeigen;
    }

    public void setEingabeMaskeBevollmaechtigterAnzeigen(boolean eingabeMaskeBevollmaechtigterAnzeigen) {
        this.eingabeMaskeBevollmaechtigterAnzeigen = eingabeMaskeBevollmaechtigterAnzeigen;
    }

    public boolean isListeBevollmaechtigterAnzeigen() {
        return listeBevollmaechtigterAnzeigen;
    }

    public void setListeBevollmaechtigterAnzeigen(boolean listeBevollmaechtigterAnzeigen) {
        this.listeBevollmaechtigterAnzeigen = listeBevollmaechtigterAnzeigen;
    }

    public boolean isListeBevollmaechtigterIstLeer() {
        return listeBevollmaechtigterIstLeer;
    }

    public void setListeBevollmaechtigterIstLeer(boolean listeBevollmaechtigterIstLeer) {
        this.listeBevollmaechtigterIstLeer = listeBevollmaechtigterIstLeer;
    }

    public List<EclVorlaeufigeVollmachtEingabe> getListeBevollmaechtigte() {
        return listeBevollmaechtigte;
    }

    public void setListeBevollmaechtigte(List<EclVorlaeufigeVollmachtEingabe> listeBevollmaechtigte) {
        this.listeBevollmaechtigte = listeBevollmaechtigte;
    }

    public boolean isEingabeStornoBevollmaechtigterAnzeigen() {
        return eingabeStornoBevollmaechtigterAnzeigen;
    }

    public void setEingabeStornoBevollmaechtigterAnzeigen(boolean eingabeStornoBevollmaechtigterAnzeigen) {
        this.eingabeStornoBevollmaechtigterAnzeigen = eingabeStornoBevollmaechtigterAnzeigen;
    }

    public EclVorlaeufigeVollmachtEingabe getStornoBevollmaechtigter() {
        return stornoBevollmaechtigter;
    }

    public void setStornoBevollmaechtigter(EclVorlaeufigeVollmachtEingabe stornoBevollmaechtigter) {
        this.stornoBevollmaechtigter = stornoBevollmaechtigter;
    }

    public List<EclVorlaeufigeVollmachtEingabe> getListeNachweise() {
        return listeNachweise;
    }

    public void setListeNachweise(List<EclVorlaeufigeVollmachtEingabe> listeNachweise) {
        this.listeNachweise = listeNachweise;
    }

    public boolean isListeNachweiseIstLeer() {
        return listeNachweiseIstLeer;
    }

    public void setListeNachweiseIstLeer(boolean listeNachweiseIstLeer) {
        this.listeNachweiseIstLeer = listeNachweiseIstLeer;
    }

    public boolean isGesetzlVertreterEingetragenNichtGeprueft() {
        return gesetzlVertreterEingetragenNichtGeprueft;
    }

    public void setGesetzlVertreterEingetragenNichtGeprueft(boolean gesetzlVertreterEingetragenNichtGeprueft) {
        this.gesetzlVertreterEingetragenNichtGeprueft = gesetzlVertreterEingetragenNichtGeprueft;
    }

    public boolean isVertreterEingetragenNichtGeprueft() {
        return vertreterEingetragenNichtGeprueft;
    }

    public void setVertreterEingetragenNichtGeprueft(boolean vertreterEingetragenNichtGeprueft) {
        this.vertreterEingetragenNichtGeprueft = vertreterEingetragenNichtGeprueft;
    }

    public boolean isUngepruefteGesetzlVertreterVorhanden() {
        return ungepruefteGesetzlVertreterVorhanden;
    }

    public void setUngepruefteGesetzlVertreterVorhanden(boolean ungepruefteGesetzlVertreterVorhanden) {
        this.ungepruefteGesetzlVertreterVorhanden = ungepruefteGesetzlVertreterVorhanden;
    }

    public boolean isUngepruefteBevollmaechtigterVorhanden() {
        return ungepruefteBevollmaechtigterVorhanden;
    }

    public void setUngepruefteBevollmaechtigterVorhanden(boolean ungepruefteBevollmaechtigeterVorhanden) {
        this.ungepruefteBevollmaechtigterVorhanden = ungepruefteBevollmaechtigeterVorhanden;
    }

    public boolean isUngepruefteNachweiseVorhanden() {
        return ungepruefteNachweiseVorhanden;
    }

    public void setUngepruefteNachweiseVorhanden(boolean ungepruefteNachweiseVorhanden) {
        this.ungepruefteNachweiseVorhanden = ungepruefteNachweiseVorhanden;
    }



}
