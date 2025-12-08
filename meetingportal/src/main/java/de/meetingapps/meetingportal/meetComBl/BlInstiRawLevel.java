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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclInsti;
import de.meetingapps.meetingportal.meetComEntities.EclInstiBestandsZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;

public class BlInstiRawLevel {

    private int logDrucken=10;
    
    private DbBundle lDbBundle=null;
    
    public BlInstiRawLevel(DbBundle pDbBundle) {
        lDbBundle=pDbBundle;
    }
    
   
    /**Wird temporär für das Eintragen neuer Zuordnungen im Speicher geführt. Enthält alle bereits erfolgte
     * Zuordnungen zu allen Instis 
     */
    private List<EclInstiBestandsZuordnung> lBereitsVorhandeneZuordnungen=null;
    private EclInstiBestandsZuordnung neueInstiBestandsZuordnung = null;

    private boolean bereitsDiesemInstiZugeordnet=false;
    private boolean bereitsAnderemInstiZugeordnet=false;
    
    private long bereitsZugeordnetAktienAnzahl=0;
    
    public String meldungsText="";
    
    public int wurdeVerarbeitet=0;
    
    /**Initialisiert die Insti-Bestände, um weitere Zuordnungen einzutragen*/
    public void initEintragen() {
        /*Alle bereits vorhandenen Zuordnungen einlesen  - zum Abgleich, ob bereits
         * zugeordnet (die neu verarbeiteten werden jeweils ergänzt)
         */
        lDbBundle.dbInstiBestandsZuordnung.readAlle();
        lBereitsVorhandeneZuordnungen = new LinkedList<EclInstiBestandsZuordnung>();
        for (int i = 0; i < lDbBundle.dbInstiBestandsZuordnung.anzErgebnis(); i++) {
            lBereitsVorhandeneZuordnungen.add(lDbBundle.dbInstiBestandsZuordnung.ergebnisPosition(i));
        }

    }
    
    
    
    /**Schreibt weitere Zuordnungen - Userabhängig - in die Datenbank und trägt diese in rcpBereitsVorhandeneZuordnungen ein,
     * 
     * Benötigt rcpBereitsVorhandeneZuordnungen als ein und Ausgabe.
     * */
    
    public void erzeugeUserInstiBestandsZuordnung(EclInstiBestandsZuordnung neueInstiBestandsZuordnung, EclUserLogin[] lpUserLoginZuInsti, boolean[] lpUserLoginZugeordnet) {
        for (int i1 = 0; i1 < lpUserLoginZuInsti.length; i1++) {
            if (lpUserLoginZugeordnet[i1] == true) {
                neueInstiBestandsZuordnung.identUserLogin = lpUserLoginZuInsti[i1].userLoginIdent;
                lDbBundle.dbInstiBestandsZuordnung.insert(neueInstiBestandsZuordnung, false);
                lBereitsVorhandeneZuordnungen.add(neueInstiBestandsZuordnung);
            }
        }
        
    }
    
    /**Belegt neueInstiBestandsZuordnung
     * 
     * pMitTeilBestand: steuert "übergeordnet", d.h. unabhängig von lpTeilBestand, ob eine Teilbestandszuordnung
     * überhaupt möglich ist. Bei Aktienregister-Zuordnung immer true, bei Meldebestand-Zuordnung immer false 
     * (Meldebestandszuordnung immer ganz oder gar nicht)*/
    public void erzeugeUndSpeichereNeueInstiBestandsZuordnung(EclInsti lpEclInsti, int pAktienregisterIdent, int pMeldeIdent,
            boolean pMitTeilBestand, String lpBeschreibung, boolean lpTeilbestand, long lpAktienTeilbestand) {
        neueInstiBestandsZuordnung = new EclInstiBestandsZuordnung();
        neueInstiBestandsZuordnung.mandant = lDbBundle.clGlobalVar.mandant;
        neueInstiBestandsZuordnung.beschreibung = lpBeschreibung;
        if (pAktienregisterIdent != 0) {
            neueInstiBestandsZuordnung.zugeordnetRegisterOderMeldungen = 1;
        } else {
            neueInstiBestandsZuordnung.zugeordnetRegisterOderMeldungen = 2;
        }
        neueInstiBestandsZuordnung.identAktienregister = pAktienregisterIdent;
        neueInstiBestandsZuordnung.identMeldung = pMeldeIdent;
        if (lpTeilbestand && pMitTeilBestand) {
            neueInstiBestandsZuordnung.zugeordneteStimmen = lpAktienTeilbestand;
        } else {
            neueInstiBestandsZuordnung.zugeordneteStimmen = -1;
        }
        neueInstiBestandsZuordnung.identInsti = lpEclInsti.ident;
        neueInstiBestandsZuordnung.identUserLogin = 0;
        lDbBundle.dbInstiBestandsZuordnung.insert(neueInstiBestandsZuordnung, true);
        lBereitsVorhandeneZuordnungen.add(neueInstiBestandsZuordnung);

    }


    
    /**Generelle Zuordnung des Bestands zu Insti prüfen und ggf. ausführen
     * return=true => wurde ausgeführt; false=> nicht ausgeführt
     * 
     * Verwendet:
     * 
     * lBereitsVorhandeneZuordnungen (input und output)
     * 
     * meldungsText (output)
     * 
     * wurdeVerarbeitet (output)
   * */
    public boolean verarbeiteHauptZuordnungAktienregister(EclInsti lpEclInsti, EclAktienregister lAktienregister, String lpBeschreibung, boolean lpTeilbestand, long lpAktienTeilbestand,
            EclUserLogin[] lpUserLoginZuInsti, boolean[] lpUserLoginZugeordnet) {
        CaBug.druckeLog("", logDrucken, 10);
        /*Prüfen: ist bereits eine Verarbeitung / Zuordnung generell zu dieser Insti erfolgt?
         */
        bereitsDiesemInstiZugeordnet = false;
        bereitsAnderemInstiZugeordnet = false;
        bereitsZugeordnetAktienAnzahl = 0;
        wurdeVerarbeitet=0;
        meldungsText="";

        for (int i1 = 0; i1 < lBereitsVorhandeneZuordnungen.size(); i1++) {
            EclInstiBestandsZuordnung lInstiBestandsZuordnung = lBereitsVorhandeneZuordnungen.get(i1);
            CaBug.druckeLog("i=" + Integer.toString(i1), logDrucken, 10);
            if (lInstiBestandsZuordnung.identAktienregister == lAktienregister.aktienregisterIdent) {
                CaBug.druckeLog("identAktienregister==aktienregisterIdent", logDrucken, 10);
                /*Anzahl zugeordneter Aktien korrigieren*/
                if (lInstiBestandsZuordnung.zugeordneteStimmen == -1) {
                    bereitsZugeordnetAktienAnzahl = -1;
                } else {
                    bereitsZugeordnetAktienAnzahl += lInstiBestandsZuordnung.zugeordneteStimmen;
                }
                /*Diesem oder anderem Insti zugeordnet?*/
                if (lInstiBestandsZuordnung.identInsti == lpEclInsti.ident) {
                    bereitsDiesemInstiZugeordnet = true;
                } else {
                    bereitsAnderemInstiZugeordnet = true;
                }
            }
        }

        CaBug.druckeLog("lpTeilbestand=" + lpTeilbestand, logDrucken, 10);
        if (lpTeilbestand) {
            if (bereitsZugeordnetAktienAnzahl + lpAktienTeilbestand > lAktienregister.stueckAktien) {
                meldungsText += "Teilbestand kann nicht zugeordnet werden, da nicht mehr genug Aktien verfügbar;";
                return false;
            }
        }
        if (!lpTeilbestand) {
            if (bereitsDiesemInstiZugeordnet) {
                meldungsText += "Zuordnung nicht möglich - bereits diesem Insti zugeordnet;";
                wurdeVerarbeitet=1;
                return false;
            }
            if (bereitsAnderemInstiZugeordnet) {
                meldungsText += "Zuordnung nicht möglich - bereits anderem Insti zugeordnet;";
                return false;
            }
        }

//        erzeugeUndSpeichereNeueInstiBestandsZuordnung(lAktienregister.aktienregisterIdent, 0, true);
        erzeugeUndSpeichereNeueInstiBestandsZuordnung(lpEclInsti, lAktienregister.aktienregisterIdent, 0,
                true, lpBeschreibung, lpTeilbestand, lpAktienTeilbestand);

        /**User-Zuordnung durchführen*/
        erzeugeUserInstiBestandsZuordnung(neueInstiBestandsZuordnung, lpUserLoginZuInsti, lpUserLoginZugeordnet);
        
        wurdeVerarbeitet = 1;

        return true;
    }

    
    
    /**Generelle Zuordnung einer Meldung zu Insti prüfen und ggf. ausführen
     * return=true => wurde ausgeführt; false=> nicht ausgeführt
     * 
     * Verwendet:
     * 
     * lBereitsVorhandeneZuordnungen (input und output)
     * 
      * meldungsText (output)
     * 
     * wurdeVerarbeitet (output)
     * */
    public boolean verarbeiteHauptZuordnungMeldung(EclInsti lpEclInsti, EclMeldung lMeldung, String lpBeschreibung, boolean lpTeilbestand, long lpAktienTeilbestand,
            EclUserLogin[] lpUserLoginZuInsti, boolean[] lpUserLoginZugeordnet) {
        /*Prüfen: ist bereits eine Verarbeitung / Zuordnung generell zu dieser Insti erfolgt?
         */
        bereitsDiesemInstiZugeordnet = false;
        bereitsAnderemInstiZugeordnet = false;
        wurdeVerarbeitet=0;
        meldungsText="";

        for (int i1 = 0; i1 < lBereitsVorhandeneZuordnungen.size(); i1++) {
            EclInstiBestandsZuordnung lInstiBestandsZuordnung = lBereitsVorhandeneZuordnungen.get(i1);
            if (lInstiBestandsZuordnung.identMeldung == lMeldung.meldungsIdent) {
                /*Diesem oder anderem Insti zugeordnet?*/
                if (lInstiBestandsZuordnung.identInsti == lpEclInsti.ident) {
                    bereitsDiesemInstiZugeordnet = true;
                } else {
                    bereitsAnderemInstiZugeordnet = true;
                }
            }
        }

        if (bereitsDiesemInstiZugeordnet) {
            meldungsText += "Zuordnung nicht möglich - bereits diesem Insti zugeordnet;";
            wurdeVerarbeitet=1;
            return false;
        }
        if (bereitsAnderemInstiZugeordnet) {
            meldungsText += "Zuordnung nicht möglich - bereits anderem Insti zugeordnet;";
            return false;
        }

//        erzeugeUndSpeichereNeueInstiBestandsZuordnung(0, lMeldung.meldungsIdent, false);
        erzeugeUndSpeichereNeueInstiBestandsZuordnung(lpEclInsti, 0, lMeldung.meldungsIdent,
                false, lpBeschreibung, lpTeilbestand, lpAktienTeilbestand);

        /**User-Zuordnung durchführen*/
        erzeugeUserInstiBestandsZuordnung(neueInstiBestandsZuordnung, lpUserLoginZuInsti, lpUserLoginZugeordnet);
        
        wurdeVerarbeitet = 1;

        return true;
    }

    
    
    
}
