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
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAppTexte;

/**Div. Funktionen zum Versionsabgleich mit der App (ist die App-Version zulässig, Übertragen von
 * aktuellen Texten etc.
 */
public class BlAppVersionsabgleich {

    /**Auf true setzen => Das Laden und der Zugriff auf die Puffer wird im Systemlog protokolliert*/
    private boolean logDrucken = true;

    /**Hier werden alle die App-Versionen eingetragen, die mit der aktuellen Serverversion einsatzfähig sind.
     * Setzt ein User eine andere App-Version ein, bekommt er die Aufforderung zum Updaten.
     */
    private int vonAppVersion = 61;//6
    private int bisAppVersion = 100;//10

    /******************Block: Ausgabewerte für holeTexteZumUpdaten************************************/
    public EclAppTexte[] updateTexte = null;
    /**Für die folgenden 3 Werte gilt:
     * =0 => kein Update der Texte enthalten
     * =1 => Update der Texte enthalten, Neubestätigung der Texte erforderlich 
     * =3 => nicht aktiv, d.h. kein Handlungsbedarf
     */
    public int disclaimerAppAktivNutzungsbedingungen = 0;
    public int disclaimerAppAktivDatenschutz = 0;
    public int disclaimerAppAktivVerwendungPersoenlicherDaten = 0;

    /***************Block: Eingabewerte für checkAppVersion*****************************************/
    /**In dieser Liste werden die Texte aller verwendeten Mandanten überprüft*/
    public int[] mandantPruefen = null;
    public int[] textVersionMandantDE = null;
    public int[] textVersionMandantEN = null;

    /**Aktuelle Version der App*/
    public int aktuelleVersionApp = 0;

    /***************Block: Ausgabewerte für checkAppVersion*****************************************/
    /**0 = kein Text-Update erforderlich; 1=Textupdate erforderlich*/
    public int[] mandantErgebnis = null;
    public int[] updateTextMandantDE = null;
    public int[] updateTextMandantEN = null;

    /**0 = keine neue Version vorhanden; 1=neue Version ist vorhanden, aber nicht zwingend;
     * 2 = Version nicht mehr kompatibel, Einsatz zwingend erforderlich
     */
    public int updateAppVersion = 0;

    public boolean pruefeObAppVersionKompatibel(int pAppVersion) {
        if (pAppVersion < vonAppVersion) {
            return false;
        }
        if (pAppVersion > bisAppVersion) {
            return false;
        }
        return true;
    }

    /**Mandant=-1 => aktueller Mandant wird geholt.
     * 		=0 => Mandantenübergreifende Texte
     * 		ansonsten: Nummer des Mandanten 
     */
    public int holeHoechsteTextVersionAppTexte(DbBundle pDbBundle, int pSprache, int pMandant) {
        int lMandant = pMandant;
        if (lMandant == -1) {
            lMandant = pDbBundle.clGlobalVar.mandant;
        }
        return pDbBundle.dbAppTexte.read_maxVersion(lMandant, pSprache);
    }

    /**Mandant=-1 => aktueller Mandant wird geholt.
     * 		=0 => Mandantenübergreifende Texte
     * 		ansonsten: Nummer des Mandanten 
     * pIstVersion: alle Texte, die eine höhere Versionsnummer haben als pIstVersion,
     * werden geliefert.
     * 
     * Ergebnisablage: in updateTexte und disclaimerAppAktiv*
     * 
     * Returnwert: Anzahl der upzudatenden Texte
     */
    public int holeTexteZumUpdaten(DbBundle pDbBundle, int pSprache, int pMandant, int pIstVersion) {
        int lMandant = pMandant;
        if (lMandant == -1) {
            lMandant = pDbBundle.clGlobalVar.mandant;
        }
        int anzahl = pDbBundle.dbAppTexte.read_updateTexte(lMandant, pSprache, pIstVersion + 1);
        updateTexte = pDbBundle.dbAppTexte.ergebnis();
        for (int i = 0; i < updateTexte.length; i++) {
            switch (updateTexte[i].seitennummer) {
            case 901:
                disclaimerAppAktivNutzungsbedingungen = 1;
                break;
            case 902:
                disclaimerAppAktivDatenschutz = 1;
                break;
            case 903:
                disclaimerAppAktivVerwendungPersoenlicherDaten = 1;
                break;
            }
        }
        if (pDbBundle.param.paramAppServer.disclaimerAppAktivNutzungsbedingungen != 1) {
            disclaimerAppAktivNutzungsbedingungen = 3;
        }
        if (pDbBundle.param.paramAppServer.disclaimerAppAktivDatenschutz != 1) {
            disclaimerAppAktivDatenschutz = 3;
        }
        if (pDbBundle.param.paramAppServer.disclaimerAppAktivVerwendungPersoenlicherDaten != 1) {
            disclaimerAppAktivVerwendungPersoenlicherDaten = 3;
        }
        return anzahl;

    }

    /**Eingabewerte: 
     * mandantPruefen, textVersionMandantDE, textVersionMandantEN, aktuelleVersionApp
     * Ausgabewerte:
     * mandantErgebnis, updateTextMandantDE, updateTextMandantEN, updateAppVersion
     */
    public void checkAppVersion(DbBundle pDbBundle) {
        if (pruefeObAppVersionKompatibel(aktuelleVersionApp)) {
            updateAppVersion = 0;
        } else {
            updateAppVersion = 2;
        }

        if (mandantPruefen == null) {
            mandantErgebnis = null;
            updateTextMandantDE = null;
            updateTextMandantEN = null;
            return;
        }

        int anzahl = mandantPruefen.length;
        if (anzahl == 0) {
            mandantErgebnis = null;
            updateTextMandantDE = null;
            updateTextMandantEN = null;
            return;
        }

        mandantErgebnis = mandantPruefen;
        updateTextMandantDE = new int[anzahl];
        updateTextMandantEN = new int[anzahl];
        for (int i = 0; i < anzahl; i++) {
            int hoechsteVersion = 0;

            /*DE*/
            hoechsteVersion = holeHoechsteTextVersionAppTexte(pDbBundle, 1, mandantPruefen[i]);
            if (logDrucken) {
                CaBug.druckeInfo("BlAppVersionsabgleich.checkAppVersion DE: Mandantentext überprüfen; mandant="
                        + mandantPruefen[i] + " höchste Version Server=" + hoechsteVersion + " höchste Version App="
                        + textVersionMandantDE[i]);
            }
            if (hoechsteVersion > textVersionMandantDE[i]) {
                updateTextMandantDE[i] = 1;
                if (logDrucken) {
                    CaBug.druckeInfo("deutsche Texte müssen upgedated werden");
                }
            } else {
                updateTextMandantDE[i] = 0;
            }

            /*EN*/
            hoechsteVersion = holeHoechsteTextVersionAppTexte(pDbBundle, 2, mandantPruefen[i]);
            if (logDrucken) {
                CaBug.druckeInfo("BlAppVersionsabgleich.checkAppVersion EN: Mandantentext überprüfen; mandant="
                        + mandantPruefen[i] + " höchste Version Server=" + hoechsteVersion + " höchste Version App="
                        + textVersionMandantEN[i]);
            }
            if (hoechsteVersion > textVersionMandantEN[i]) {
                updateTextMandantEN[i] = 1;
                if (logDrucken) {
                    CaBug.druckeInfo("englische Texte müssen upgedated werden");
                }
            } else {
                updateTextMandantEN[i] = 0;
            }

        }
    }

}
