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

/**Login-Daten (Kennung, Initialpasswort, eigenes Passwort) für Aktienregister-Einträge*/
@Deprecated
public class EclAktienregisterLoginDaten {

    public int mandant = 0;

    /**Gleiche Ident wie Aktienregister-Eintrag*/
    public int aktienregisterIdent = 0;

    public long db_version = 0;

    /**Kennung, mit dem sich Aktionär im Portal einloggen kann. Ist i.d.R. identisch mit
     * der Aktionärsnummer, kann aber - auf Kundenwunsch - auch abweichend sein.
     * LEN=20
     */
    public String loginKennung = "";

    /**Verschlüsseltes Passwort. Auch bereits das Initial-Passwort. 
     * 
     * Entschlüsselung nicht möglich.
     * LEN=64
     */
    public String passwortVerschluesselt = "";

    /**Initialpasswort. Verschlüsselt, aber umkehrbar.
     * Gefüllt, wenn noch kein eigenes Passwort vergeben. Leer, wenn eigenes Passwort vergeben
     * LEN=64*/
    public String passwortInitial = "";

    /**=1 => diese Benutzerkennung darf sich nicht am Portal anmelden*/
    public int anmeldenUnzulaessig = 0;

    /**=1 => diese Benutzerkennung darf sich nicht für elektronischen Einladungsversand und nicht für 
     * ein dauerhaftes Passwort registrieren
     */
    public int dauerhafteRegistrierungUnzulaessig = 0;

    /******************Standard-Getter und Setter**************************************************/

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getAktienregisterIdent() {
        return aktienregisterIdent;
    }

    public void setAktienregisterIdent(int aktienregisterIdent) {
        this.aktienregisterIdent = aktienregisterIdent;
    }

    public String getLoginKennung() {
        return loginKennung;
    }

    public void setLoginKennung(String loginKennung) {
        this.loginKennung = loginKennung;
    }

    public String getPasswortVerschluesselt() {
        return passwortVerschluesselt;
    }

    public void setPasswortVerschluesselt(String passwortVerschluesselt) {
        this.passwortVerschluesselt = passwortVerschluesselt;
    }

    public String getPasswortInitial() {
        return passwortInitial;
    }

    public void setPasswortInitial(String passwortInitial) {
        this.passwortInitial = passwortInitial;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public int getAnmeldenUnzulaessig() {
        return anmeldenUnzulaessig;
    }

    public void setAnmeldenUnzulaessig(int anmeldenUnzulaessig) {
        this.anmeldenUnzulaessig = anmeldenUnzulaessig;
    }

    public int getDauerhafteRegistrierungUnzulaessig() {
        return dauerhafteRegistrierungUnzulaessig;
    }

    public void setDauerhafteRegistrierungUnzulaessig(int dauerhafteRegistrierungUnzulaessig) {
        this.dauerhafteRegistrierungUnzulaessig = dauerhafteRegistrierungUnzulaessig;
    }

}
