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

public class EclMailingVariablen {

    public int aktienregisterIdent;

    public String email = "";

    public String email2 = "";

    public String kennung = "";

    public String passwort = "";

    public String anrede = "";

    public String nameKomplett = "";

    public String titel = "";

    public String vorname = "";

    public String nachname = "";

    public String link = "";

    public EclMailingVariablen() {

    }

    public EclMailingVariablen(int aktienregisterIdent, String email, String email2, String kennung, String passwort,
            String anrede, String nameKomplett, String titel, String vorname, String nachname, String link) {
        super();
        this.aktienregisterIdent = aktienregisterIdent;
        this.email = email;
        this.email2 = email2;
        this.kennung = kennung;
        this.passwort = passwort;
        this.anrede = anrede;
        this.nameKomplett = nameKomplett;
        this.titel = titel;
        this.vorname = vorname;
        this.nachname = nachname;
        this.link = link;
    }

    public int getAktienregisterIdent() {
        return aktienregisterIdent;
    }

    public void setAktienregisterIdent(int aktienregisterIdent) {
        this.aktienregisterIdent = aktienregisterIdent;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getKennung() {
        return kennung;
    }

    public void setKennung(String kennung) {
        this.kennung = kennung;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getAnrede() {
        return anrede;
    }

    public void setAnrede(String anrede) {
        this.anrede = anrede;
    }

    public String getNameKomplett() {
        return nameKomplett;
    }

    public void setNameKomplett(String nameKomplett) {
        this.nameKomplett = nameKomplett;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "EclMailingVariablen [aktienregisterIdent=" + aktienregisterIdent + ", email=" + email + ", email2="
                + email2 + ", kennung=" + kennung + ", passwort=" + passwort + ", anrede=" + anrede + ", nameKomplett="
                + nameKomplett + ", titel=" + titel + ", vorname=" + vorname + ", nachname=" + nachname + ", link="
                + link + "]";
    }
}
