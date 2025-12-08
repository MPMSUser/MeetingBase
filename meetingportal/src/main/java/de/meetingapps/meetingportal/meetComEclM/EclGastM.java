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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclGastM implements Serializable {
    private static final long serialVersionUID = -5085633043740460761L;

    @Inject
    EclKommunikationsSpracheListeM kommunikationsSpracheListeM;

    private int meldeIdent = 0;

    /**Benötigt als Zwischenspeicher fürs Ändern*/
    private long db_version = 0;

    /**Wird für die Anzeige verwendet*/
    private String nummer = "";

    /**Aktuelle, aktive Nummer - für Stornieren, und Drucken*/
    private String nummerOhneNeben = "";
    private String nummerNeben = "";

    private String anrede = "";
    private String anredeText = ""; /*Nur zur Anzeige bei Quittung*/
    private String anredeKomplett = ""; /*Sehr geehrter Herr Mustermann etc. - je nach Anredenschlüssel. Nur für Mailaufbereitung bei Neuausstellung*/
    private String titel = "";
    private String adelstitel = "";
    private String name = "";
    private String vorname = "";
    private String zuHdCo = "";
    private String zusatz1 = "";
    private String zusatz2 = "";
    private String strasse = "";
    private String land = "";
    private String plz = "";
    private String ort = "";
    private String mailadresse = "";
    private String kommunikationssprache = "-1";
    private String kommunikationsspracheText = ""; /*Nur zur Anzeige bei Quittung*/
    private String gruppe = "";
    private String gruppeText = ""; /*Nur zur Anzeige bei Quittung*/
    private String neuerAusstellungsgrund = "";
    private String neuerAusstellungsgrundKommentar = "";
    private List<EclMeldungAusstellungsgrundM> ausstellungsgrundListe = null;
    private String neuesVipKZ = "";
    private String neuesVipKZKommentar = "";
    private List<EclMeldungVipKZM> vipKZListe = null;
    private String anzahlZugeordneteEK = "";
    private List<EclMeldungenMeldungenM> meldungenMeldungenListe = null;

    private List<EclZutrittsIdentAnzeigeM> alleZutrittsIdents = null;

    /************Berechtigungen****************/
    private boolean darfStream=false;
    private boolean darfFragen=false;
    private boolean darfWortmeldungen=false;
    private boolean darfWidersprueche=false;
    private boolean darfAntraege=false;
    private boolean darfSonstigeMitteilungen=false;
    private boolean darfUnterlagenGruppe1=false;
    private boolean darfUnterlagenGruppe2=false;
    private boolean darfUnterlagenGruppe3=false;
    private boolean darfUnterlagenGruppe4=false;
    private boolean darfUnterlagenGruppe5=false;
    private boolean darfTeilnehmerverzeichnis=false;
    private boolean darfAbstimmungsergebnisse=false;
    private boolean darfBotschaftenEinreichen=false;
    private boolean darfBotschaftenAnzeige=false;
    private boolean darfRueckfragen=false;
    private boolean darfMonitoring=false;
    private boolean darfPreview=false;
    private boolean darfGast1=false;
    private boolean darfGast2=false;
    private boolean darfGast3=false;
    private boolean darfGast4=false;
    private boolean darfGast5=false;
    private boolean darfGast6=false;
    private boolean darfGast7=false;
    private boolean darfGast8=false;
    private boolean darfGast9=false;

    public EclGastM() {
        init();
    }

    public void init() {
        meldeIdent = 0;
        db_version = 0;
        nummer = "";
        nummerOhneNeben = "";
        nummerNeben = "";
        anrede = "0";
        anredeText = "";
        anredeKomplett = "";
        titel = "";
        adelstitel = "";
        name = "";
        vorname = "";
        zuHdCo = "";
        zusatz1 = "";
        zusatz2 = "";
        strasse = "";
        land = "";
        plz = "";
        ort = "";
        mailadresse = "";
        kommunikationssprache = "-1";
        kommunikationsspracheText = "";
        gruppe = "0";
        gruppeText = "";
        neuerAusstellungsgrund = "_0";
        neuerAusstellungsgrundKommentar = "";
        ausstellungsgrundListe = new LinkedList<>();
        neuesVipKZ = "0";
        neuesVipKZKommentar = "";
        vipKZListe = new LinkedList<>();
        anzahlZugeordneteEK = "";
        alleZutrittsIdents = new LinkedList<EclZutrittsIdentAnzeigeM>();

        darfStream=false;
        darfFragen=false;
        darfWortmeldungen=false;
        darfWidersprueche=false;
        darfAntraege=false;
        darfSonstigeMitteilungen=false;
        darfUnterlagenGruppe1=false;
        darfUnterlagenGruppe2=false;
        darfUnterlagenGruppe3=false;
        darfUnterlagenGruppe4=false;
        darfUnterlagenGruppe5=false;
        darfTeilnehmerverzeichnis=false;
        darfAbstimmungsergebnisse=false;
        darfBotschaftenEinreichen=false;
        darfBotschaftenAnzeige=false;
        darfRueckfragen=false;
        darfMonitoring=false;
        darfPreview=false;
        darfGast1=false;
        darfGast2=false;
        darfGast3=false;
        darfGast4=false;
        darfGast5=false;
        darfGast6=false;
        darfGast7=false;
        darfGast8=false;
        darfGast9=false;

        
        
        //		Temporär lahmgelegt, wegen Verwendung für Insti
        //		if (kommunikationsSpracheListeM!=null && kommunikationsSpracheListeM.getKommunikationsSpracheListeM()!=null){
        //			
        //			int i;
        //			for (i=0;i<kommunikationsSpracheListeM.getKommunikationsSpracheListeM().size();i++){
        //				if (kommunikationsSpracheListeM.getKommunikationsSpracheListeM().get(i).getIststandard()==1){
        //					kommunikationssprache=kommunikationsSpracheListeM.getKommunikationsSpracheListeM().get(i).getSprachennr();
        //				}
        //			}
        //		}

    }

    /**Folgende Felder müssen anschließend außerhab dieser Funktion noch gefüllt werden
     * (sie werden hier nur initialisiert):
     * nummerOhneNeben, nummerNeben, ausstellungsgrundListe, vipKZListe,
     * alleZutrittsIdents
     */
    public void copyFrom(EclMeldung pGast) {
        meldeIdent = pGast.meldungsIdent;
        db_version = pGast.db_version;
        nummer = pGast.zutrittsIdent;
        nummerOhneNeben = "";
        nummerNeben = "";
        anrede = Integer.toString(pGast.anrede);
        titel = pGast.titel;
        adelstitel = pGast.adelstitel;
        name = pGast.name;
        vorname = pGast.vorname;
        zuHdCo = pGast.zuHdCo;
        zusatz1 = pGast.zusatz1;
        zusatz2 = pGast.zusatz2;
        strasse = pGast.strasse;
        land = pGast.land;
        plz = pGast.plz;
        ort = pGast.ort;
        mailadresse = pGast.mailadresse;
        kommunikationssprache = Integer.toString(pGast.kommunikationssprache);
        gruppe = Integer.toString(pGast.gruppe);
        neuerAusstellungsgrund = "";
        neuerAusstellungsgrundKommentar = "";
        ausstellungsgrundListe = new LinkedList<>();
        neuesVipKZ = "";
        neuesVipKZKommentar = "";
        vipKZListe = new LinkedList<>();
        anzahlZugeordneteEK = "";
        alleZutrittsIdents = new LinkedList<EclZutrittsIdentAnzeigeM>();

    }

    public void belegeBerechtigungen(long pBerechtigungsWert) {
        darfStream= KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.stream, pBerechtigungsWert);
        darfFragen=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.fragen, pBerechtigungsWert);;
        darfWortmeldungen=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.wortmeldungen, pBerechtigungsWert);;
        darfWidersprueche=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.widersprueche, pBerechtigungsWert);;
        darfAntraege=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.antraege, pBerechtigungsWert);;
        darfSonstigeMitteilungen=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.sonstigeMitteilungen, pBerechtigungsWert);;
        darfUnterlagenGruppe1=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.unterlagenGruppe1, pBerechtigungsWert);;
        darfUnterlagenGruppe2=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.unterlagenGruppe2, pBerechtigungsWert);;
        darfUnterlagenGruppe3=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.unterlagenGruppe3, pBerechtigungsWert);;
        darfUnterlagenGruppe4=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.unterlagenGruppe4, pBerechtigungsWert);;
        darfUnterlagenGruppe5=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.unterlagenGruppe5, pBerechtigungsWert);;
        darfTeilnehmerverzeichnis=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.teilnehmerverzeichnis, pBerechtigungsWert);;
        darfAbstimmungsergebnisse=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.abstimmungsergebnisse, pBerechtigungsWert);;
        darfBotschaftenEinreichen=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.botschaftenEinreichen, pBerechtigungsWert);;
        darfBotschaftenAnzeige=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.botschaftenAnzeige, pBerechtigungsWert);;
        darfRueckfragen=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.rueckfragen, pBerechtigungsWert);;
        darfMonitoring=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.monitoring, pBerechtigungsWert);;
        darfPreview=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.preview, pBerechtigungsWert);;
        darfGast1=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast1, pBerechtigungsWert);;
        darfGast2=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast2, pBerechtigungsWert);;
        darfGast3=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast3, pBerechtigungsWert);;
        darfGast4=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast4, pBerechtigungsWert);;
        darfGast5=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast5, pBerechtigungsWert);;
        darfGast6=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast6, pBerechtigungsWert);;
        darfGast7=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast7, pBerechtigungsWert);;
        darfGast8=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast8, pBerechtigungsWert);;
        darfGast9=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast9, pBerechtigungsWert);;
     }

    
    
    /**Folgende Felder werden NICHT mitkopiert
     * (sie werden hier nur initialisiert):
     * ausstellungsgrundListe, vipKZListe,
     * alleZutrittsIdents
     */
    public void copyFromM(EclGastM pGast) {
        meldeIdent = pGast.meldeIdent;
        db_version = pGast.db_version;
        nummer = pGast.nummer;
        nummerOhneNeben = pGast.nummerOhneNeben;
        nummerNeben = pGast.nummerNeben;
        anrede = pGast.anrede;
        titel = pGast.titel;
        adelstitel = pGast.adelstitel;
        name = pGast.name;
        vorname = pGast.vorname;
        zuHdCo = pGast.zuHdCo;
        zusatz1 = pGast.zusatz1;
        zusatz2 = pGast.zusatz2;
        strasse = pGast.strasse;
        land = pGast.land;
        plz = pGast.plz;
        ort = pGast.ort;
        mailadresse = pGast.mailadresse;
        kommunikationssprache = pGast.kommunikationssprache;
        gruppe = pGast.gruppe;
        neuerAusstellungsgrund = "";
        neuerAusstellungsgrundKommentar = "";
        ausstellungsgrundListe = new LinkedList<>();
        neuesVipKZ = "";
        neuesVipKZKommentar = "";
        vipKZListe = new LinkedList<>();
        anzahlZugeordneteEK = "";
        alleZutrittsIdents = new LinkedList<EclZutrittsIdentAnzeigeM>();
    }

    /**Folgende Felder müssen anschließend außerhab dieser Funktion noch gefüllt werden
     * (sie werden hier nur initialisiert):
     * nummerOhneNeben, nummerNeben, ausstellungsgrundListe, vipKZListe,
     * alleZutrittsIdents
     */
    public void copyTo(EclMeldung pGast) {

        pGast.meldungsIdent = meldeIdent;
        pGast.db_version = db_version;
        pGast.zutrittsIdent = nummer;
        pGast.anrede = Integer.parseInt(anrede);
        pGast.titel = titel;
        pGast.adelstitel = adelstitel;
        pGast.name = name;
        pGast.vorname = vorname;
        pGast.zuHdCo = zuHdCo;
        pGast.zusatz1 = zusatz1;
        pGast.zusatz2 = zusatz2;
        pGast.strasse = strasse;
        pGast.land = land;
        pGast.plz = plz;
        pGast.ort = ort;
        pGast.mailadresse = mailadresse;
        pGast.kommunikationssprache = Integer.parseInt(kommunikationssprache);
        pGast.gruppe = Integer.parseInt(gruppe);
        return;
    }

    public long liefereBerechtigungsWert() {
        long berechtigungsWert=0;
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.stream, darfStream);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.fragen, darfFragen);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.wortmeldungen, darfWortmeldungen);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.widersprueche, darfWidersprueche);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.antraege, darfAntraege);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.sonstigeMitteilungen, darfSonstigeMitteilungen);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.unterlagenGruppe1, darfUnterlagenGruppe1);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.unterlagenGruppe2, darfUnterlagenGruppe2);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.unterlagenGruppe3, darfUnterlagenGruppe3);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.unterlagenGruppe4, darfUnterlagenGruppe4);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.unterlagenGruppe5, darfUnterlagenGruppe5);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.teilnehmerverzeichnis, darfTeilnehmerverzeichnis);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.abstimmungsergebnisse, darfAbstimmungsergebnisse);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.botschaftenEinreichen, darfBotschaftenEinreichen);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.botschaftenAnzeige, darfBotschaftenAnzeige);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.rueckfragen, darfRueckfragen);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.monitoring, darfMonitoring);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.preview, darfPreview);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast1, darfGast1);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast2, darfGast2);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast3, darfGast3);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast4, darfGast4);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast5, darfGast5);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast6, darfGast6);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast7, darfGast7);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast8, darfGast8);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast9, darfGast9);
        return berechtigungsWert;
    }

    public boolean isAlleZutrittsIdentsVorhanden() {
        if (alleZutrittsIdents == null || alleZutrittsIdents.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /*****************Standard getter/setter**************************/

    public String getNummer() {
        return nummer;
    }

    public void setNummer(String nummer) {
        this.nummer = nummer;
    }

    public String getAnrede() {
        return anrede;
    }

    public void setAnrede(String anrede) {
        this.anrede = anrede;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getAdelstitel() {
        return adelstitel;
    }

    public void setAdelstitel(String adelstitel) {
        this.adelstitel = adelstitel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getZuHdCo() {
        return zuHdCo;
    }

    public void setZuHdCo(String zuHdCo) {
        this.zuHdCo = zuHdCo;
    }

    public String getZusatz1() {
        return zusatz1;
    }

    public void setZusatz1(String zusatz1) {
        this.zusatz1 = zusatz1;
    }

    public String getZusatz2() {
        return zusatz2;
    }

    public void setZusatz2(String zusatz2) {
        this.zusatz2 = zusatz2;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
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

    public String getMailadresse() {
        return mailadresse;
    }

    public void setMailadresse(String mailadresse) {
        this.mailadresse = mailadresse;
    }

    public String getKommunikationssprache() {
        return kommunikationssprache;
    }

    public void setKommunikationssprache(String kommunikationssprache) {
        this.kommunikationssprache = kommunikationssprache;
    }

    public String getGruppe() {
        return gruppe;
    }

    public void setGruppe(String gruppe) {
        this.gruppe = gruppe;
    }

    public String getNeuerAusstellungsgrund() {
        return neuerAusstellungsgrund;
    }

    public void setNeuerAusstellungsgrund(String neuerAusstellungsgrund) {
        this.neuerAusstellungsgrund = neuerAusstellungsgrund;
    }

    public String getNeuerAusstellungsgrundKommentar() {
        return neuerAusstellungsgrundKommentar;
    }

    public void setNeuerAusstellungsgrundKommentar(String neuerAusstellungsgrundKommentar) {
        this.neuerAusstellungsgrundKommentar = neuerAusstellungsgrundKommentar;
    }

    public String getNeuesVipKZ() {
        return neuesVipKZ;
    }

    public void setNeuesVipKZ(String neuesVipKZ) {
        this.neuesVipKZ = neuesVipKZ;
    }

    public String getNeuesVipKZKommentar() {
        return neuesVipKZKommentar;
    }

    public void setNeuesVipKZKommentar(String neuesVipKZKommentar) {
        this.neuesVipKZKommentar = neuesVipKZKommentar;
    }

    public List<EclMeldungAusstellungsgrundM> getAusstellungsgrundListe() {
        return ausstellungsgrundListe;
    }

    public void setAusstellungsgrundListe(List<EclMeldungAusstellungsgrundM> ausstellungsgrundListe) {
        this.ausstellungsgrundListe = ausstellungsgrundListe;
    }

    public List<EclMeldungVipKZM> getVipKZListe() {
        return vipKZListe;
    }

    public void setVipKZListe(List<EclMeldungVipKZM> vipKZListe) {
        this.vipKZListe = vipKZListe;
    }

    public String getAnredeText() {
        return anredeText;
    }

    public void setAnredeText(String anredeText) {
        this.anredeText = anredeText;
    }

    public String getGruppeText() {
        return gruppeText;
    }

    public void setGruppeText(String gruppeText) {
        this.gruppeText = gruppeText;
    }

    public String getKommunikationsspracheText() {
        return kommunikationsspracheText;
    }

    public void setKommunikationsspracheText(String kommunikationsspracheText) {
        this.kommunikationsspracheText = kommunikationsspracheText;
    }

    public int getMeldeIdent() {
        return meldeIdent;
    }

    public void setMeldeIdent(int meldeIdent) {
        this.meldeIdent = meldeIdent;
    }

    public String getAnzahlZugeordneteEK() {
        return anzahlZugeordneteEK;
    }

    public void setAnzahlZugeordneteEK(String anzahlZugeordneteEK) {
        this.anzahlZugeordneteEK = anzahlZugeordneteEK;
    }

    public List<EclMeldungenMeldungenM> getMeldungenMeldungenListe() {
        return meldungenMeldungenListe;
    }

    public void setMeldungenMeldungenListe(List<EclMeldungenMeldungenM> meldungenMeldungenListe) {
        this.meldungenMeldungenListe = meldungenMeldungenListe;
    }

    public String getNummerOhneNeben() {
        return nummerOhneNeben;
    }

    public void setNummerOhneNeben(String nummerOhneNeben) {
        this.nummerOhneNeben = nummerOhneNeben;
    }

    public String getNummerNeben() {
        return nummerNeben;
    }

    public void setNummerNeben(String nummerNeben) {
        this.nummerNeben = nummerNeben;
    }

    public List<EclZutrittsIdentAnzeigeM> getAlleZutrittsIdents() {
        return alleZutrittsIdents;
    }

    public void setAlleZutrittsIdents(List<EclZutrittsIdentAnzeigeM> alleZutrittsIdents) {
        this.alleZutrittsIdents = alleZutrittsIdents;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public String getAnredeKomplett() {
        return anredeKomplett;
    }

    public void setAnredeKomplett(String anredeKomplett) {
        this.anredeKomplett = anredeKomplett;
    }

    public boolean isDarfStream() {
        return darfStream;
    }

    public void setDarfStream(boolean darfStream) {
        this.darfStream = darfStream;
    }

    public boolean isDarfFragen() {
        return darfFragen;
    }

    public void setDarfFragen(boolean darfFragen) {
        this.darfFragen = darfFragen;
    }

    public boolean isDarfWortmeldungen() {
        return darfWortmeldungen;
    }

    public void setDarfWortmeldungen(boolean darfWortmeldungen) {
        this.darfWortmeldungen = darfWortmeldungen;
    }

    public boolean isDarfWidersprueche() {
        return darfWidersprueche;
    }

    public void setDarfWidersprueche(boolean darfWidersprueche) {
        this.darfWidersprueche = darfWidersprueche;
    }

    public boolean isDarfAntraege() {
        return darfAntraege;
    }

    public void setDarfAntraege(boolean darfAntraege) {
        this.darfAntraege = darfAntraege;
    }

    public boolean isDarfSonstigeMitteilungen() {
        return darfSonstigeMitteilungen;
    }

    public void setDarfSonstigeMitteilungen(boolean darfSonstigeMitteilungen) {
        this.darfSonstigeMitteilungen = darfSonstigeMitteilungen;
    }

    public boolean isDarfUnterlagenGruppe1() {
        return darfUnterlagenGruppe1;
    }

    public void setDarfUnterlagenGruppe1(boolean darfUnterlagenGruppe1) {
        this.darfUnterlagenGruppe1 = darfUnterlagenGruppe1;
    }

    public boolean isDarfUnterlagenGruppe2() {
        return darfUnterlagenGruppe2;
    }

    public void setDarfUnterlagenGruppe2(boolean darfUnterlagenGruppe2) {
        this.darfUnterlagenGruppe2 = darfUnterlagenGruppe2;
    }

    public boolean isDarfUnterlagenGruppe3() {
        return darfUnterlagenGruppe3;
    }

    public void setDarfUnterlagenGruppe3(boolean darfUnterlagenGruppe3) {
        this.darfUnterlagenGruppe3 = darfUnterlagenGruppe3;
    }

    public boolean isDarfUnterlagenGruppe4() {
        return darfUnterlagenGruppe4;
    }

    public void setDarfUnterlagenGruppe4(boolean darfUnterlagenGruppe4) {
        this.darfUnterlagenGruppe4 = darfUnterlagenGruppe4;
    }

    public boolean isDarfUnterlagenGruppe5() {
        return darfUnterlagenGruppe5;
    }

    public void setDarfUnterlagenGruppe5(boolean darfUnterlagenGruppe5) {
        this.darfUnterlagenGruppe5 = darfUnterlagenGruppe5;
    }

    public boolean isDarfTeilnehmerverzeichnis() {
        return darfTeilnehmerverzeichnis;
    }

    public void setDarfTeilnehmerverzeichnis(boolean darfTeilnehmerverzeichnis) {
        this.darfTeilnehmerverzeichnis = darfTeilnehmerverzeichnis;
    }

    public boolean isDarfAbstimmungsergebnisse() {
        return darfAbstimmungsergebnisse;
    }

    public void setDarfAbstimmungsergebnisse(boolean darfAbstimmungsergebnisse) {
        this.darfAbstimmungsergebnisse = darfAbstimmungsergebnisse;
    }

    public boolean isDarfMonitoring() {
        return darfMonitoring;
    }

    public void setDarfMonitoring(boolean darfMonitoring) {
        this.darfMonitoring = darfMonitoring;
    }

    public boolean isDarfGast1() {
        return darfGast1;
    }

    public void setDarfGast1(boolean darfGast1) {
        this.darfGast1 = darfGast1;
    }

    public boolean isDarfGast2() {
        return darfGast2;
    }

    public void setDarfGast2(boolean darfGast2) {
        this.darfGast2 = darfGast2;
    }

    public boolean isDarfGast3() {
        return darfGast3;
    }

    public void setDarfGast3(boolean darfGast3) {
        this.darfGast3 = darfGast3;
    }

    public boolean isDarfGast4() {
        return darfGast4;
    }

    public void setDarfGast4(boolean darfGast4) {
        this.darfGast4 = darfGast4;
    }

    public boolean isDarfGast5() {
        return darfGast5;
    }

    public void setDarfGast5(boolean darfGast5) {
        this.darfGast5 = darfGast5;
    }

    public boolean isDarfGast6() {
        return darfGast6;
    }

    public void setDarfGast6(boolean darfGast6) {
        this.darfGast6 = darfGast6;
    }

    public boolean isDarfGast7() {
        return darfGast7;
    }

    public void setDarfGast7(boolean darfGast7) {
        this.darfGast7 = darfGast7;
    }

    public boolean isDarfGast8() {
        return darfGast8;
    }

    public void setDarfGast8(boolean darfGast8) {
        this.darfGast8 = darfGast8;
    }

    public boolean isDarfGast9() {
        return darfGast9;
    }

    public void setDarfGast9(boolean darfGast9) {
        this.darfGast9 = darfGast9;
    }

    public boolean isDarfBotschaftenEinreichen() {
        return darfBotschaftenEinreichen;
    }

    public void setDarfBotschaftenEinreichen(boolean darfBotschaftenEinreichen) {
        this.darfBotschaftenEinreichen = darfBotschaftenEinreichen;
    }

    public boolean isDarfBotschaftenAnzeige() {
        return darfBotschaftenAnzeige;
    }

    public void setDarfBotschaftenAnzeige(boolean darfBotschaftenAnzeige) {
        this.darfBotschaftenAnzeige = darfBotschaftenAnzeige;
    }

    public boolean isDarfRueckfragen() {
        return darfRueckfragen;
    }

    public void setDarfRueckfragen(boolean darfRueckfragen) {
        this.darfRueckfragen = darfRueckfragen;
    }

    public boolean isDarfPreview() {
        return darfPreview;
    }

    public void setDarfPreview(boolean darfPreview) {
        this.darfPreview = darfPreview;
    }

}