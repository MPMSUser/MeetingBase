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

import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclAktienregisterErgaenzungM implements Serializable {
    private static final long serialVersionUID = 7542483257275527623L;

    /**Mandantennummer*/
    private int mandant = 0;

    /** eindeutiger Key für Aktienregistersatz (zusammen mit mandant und satzNummer), der unveränderlich ist. 
     * Wird in diesem Fall nicht neu vergeben, sondern muß bereits gefüllt sein,
     * da dieses Feld der "Foreign-Key" zu EclAktienregisterEintrag ist.
     */
    private int aktienregisterIdent = 0;

    /**0 => Originalsatz wie geliefert
     * >0 => Vom Mitglied geänderte Daten, fortlaufend numeriert
     */
    private int satzNummer = 0;

    /**Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung in DbMeldungen*/
    private long db_version;

    /**LEN=120*/
    private String[] ergaenzungLangString = null;;
    /**LEN=40*/
    private String[] ergaenzungKurzString = null;;

    private int[] ergaenzungKennzeichen = null;

    public void copyFrom(EclAktienregisterErgaenzung pAktienregisterErgaenzung) {
        mandant = pAktienregisterErgaenzung.mandant;
        aktienregisterIdent = pAktienregisterErgaenzung.aktienregisterIdent;
        satzNummer = pAktienregisterErgaenzung.satzNummer;
        db_version = pAktienregisterErgaenzung.db_version;
        ergaenzungLangString = new String[30];
        ergaenzungKurzString = new String[30];
        ergaenzungKennzeichen = new int[30];
        for (int i = 0; i < 30; i++) {
            ergaenzungLangString[i] = pAktienregisterErgaenzung.ergaenzungLangString[i];
            ergaenzungKurzString[i] = pAktienregisterErgaenzung.ergaenzungKurzString[i];
            ergaenzungKennzeichen[i] = pAktienregisterErgaenzung.ergaenzungKennzeichen[i];
        }
    }

    public EclAktienregisterErgaenzung copyTo() {
        EclAktienregisterErgaenzung pAktienregisterErgaenzung = new EclAktienregisterErgaenzung();
        pAktienregisterErgaenzung.mandant = mandant;
        pAktienregisterErgaenzung.aktienregisterIdent = aktienregisterIdent;
        pAktienregisterErgaenzung.satzNummer = satzNummer;
        pAktienregisterErgaenzung.db_version = db_version;
        pAktienregisterErgaenzung.ergaenzungLangString = new String[30];
        pAktienregisterErgaenzung.ergaenzungKurzString = new String[30];
        pAktienregisterErgaenzung.ergaenzungKennzeichen = new int[30];
        for (int i = 0; i < 30; i++) {
            pAktienregisterErgaenzung.ergaenzungLangString[i] = ergaenzungLangString[i];
            pAktienregisterErgaenzung.ergaenzungKurzString[i] = ergaenzungKurzString[i];
            pAktienregisterErgaenzung.ergaenzungKennzeichen[i] = ergaenzungKennzeichen[i];
        }

        return pAktienregisterErgaenzung;
    }

    /***************Standard getter und Setter****************************/

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

    public int getSatzNummer() {
        return satzNummer;
    }

    public void setSatzNummer(int satzNummer) {
        this.satzNummer = satzNummer;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public String[] getErgaenzungLangString() {
        return ergaenzungLangString;
    }

    public void setErgaenzungLangString(String[] ergaenzungLangString) {
        this.ergaenzungLangString = ergaenzungLangString;
    }

    public String[] getErgaenzungKurzString() {
        return ergaenzungKurzString;
    }

    public void setErgaenzungKurzString(String[] ergaenzungKurzString) {
        this.ergaenzungKurzString = ergaenzungKurzString;
    }

    public int[] getErgaenzungKennzeichen() {
        return ergaenzungKennzeichen;
    }

    public void setErgaenzungKennzeichen(int[] ergaenzungKennzeichen) {
        this.ergaenzungKennzeichen = ergaenzungKennzeichen;
    }

}
