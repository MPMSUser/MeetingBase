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

import java.util.List;

import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;

/**Siehe EclLoginDatenM*/
public class EclTLoginDatenM {

    /**Immer gefüllt - gemäß eingegebener Login-Kennung*/
    public EclLoginDaten eclLoginDaten = new EclLoginDaten();

    /**Gefüllt, je nachdem welche Kennungart verwendet wurde*/
    public EclAktienregister eclAktienregister = new EclAktienregister();
    public EclPersonenNatJur eclPersonenNatJur = new EclPersonenNatJur();
    public EclAktienregisterErgaenzung eclAktienregisterErgaenzung = new EclAktienregisterErgaenzung();

    public String anmeldeKennungFuerAnzeige = "";

    public String titelVornameName = "";
    public String ort = "";

    public String titel = "";
    public String name = "";
    public String vorname = "";
    public String strasse = "";
    public String plzOrt = "";

    public List<String> adresszeilen = null;

    public String testanzeige = "";

    public long stimmen = 0;
    public String stimmenDE = "";
    /**String formatiert im Deutschen Format*/
    public String stimmenEN = "";
    /**String formatiert im Englischen Format*/

    /**Wird durch pruefeErstregistrierung gefuellt - 
     * true => Erst-Registrierungsseite muß nach Login aufgerufen werden*/
    public boolean erstregistrierungAufrufen = false;

    /**Falls Passwort verändert wurde: Passwort verschlüsselt, für Rückgabe
     * an App
     */
    public String passwortVerschluesselt = "";

    /**1=Aktienregister-Nummer (Namensaktien)/ HV-Ticket-Nummer (Inhaberaktien)
     * 2=Kennung (Gäste, Bevollmächtigte, ...)
     */
    public int liefereKennungArt() {
        return eclLoginDaten.kennungArt;
    }

    public int lieferePersonNatJurIdent() {
        if (eclLoginDaten.kennungArt == KonstLoginKennungArt.aktienregister) {
            return eclAktienregister.personNatJur;
        } else {
            return eclPersonenNatJur.ident;
        }
    }

    public String liefereNameVornameTitel() {
        return name+" "+vorname+" "+titel;
    }

    public String liefereVornameNameTitel() {
        return vorname+" "+name+" "+titel;
    }
    

}
