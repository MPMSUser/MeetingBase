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

import de.meetingapps.meetingportal.meetComEntities.EclPublikation;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclPublikationM implements Serializable {
    private static final long serialVersionUID = -5085633043740460761L;

    /**Mandantennummer*/
    private int mandant = 0;

    /** eindeutiger Key für Publikation (zusammen mit mandant), der unveränderlich ist. 
     */
    private int ident = 0;

    /**Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung in DbMeldungen*/
    private long db_version;

    /**Reihenfolge, in der die Publikationen ausgewiesen werden (in Dialogen).
     * Niedrigster Wert=0, höchster Wert=9*/
    private int position = 0;

    /**Bezeichnung der Publikation
     * Länge=30*/
    private String bezeichnung = "";

    /**Versandweg, über den die Publikation vertrieben wird bzw. angefordert werden kann
     * =0 => Versandweg ist nicht zulässig
     * =1 => Versandweg ist zulässig*/
    private int[] publikationenZustellung = new int[10];
    private boolean[] publikationAngefordert = new boolean[10];

    /*publikationAngefordert wird auf false gesetzt - alles andere kopiert*/
    public void copyFrom(EclPublikation pPublikation) {
        this.mandant = pPublikation.mandant;
        this.ident = pPublikation.ident;
        this.db_version = pPublikation.db_version;
        this.position = pPublikation.position;
        this.bezeichnung = pPublikation.bezeichnung;
        int i;
        for (i = 0; i < 10; i++) {
            this.publikationenZustellung[i] = pPublikation.publikationenZustellung[i];
            publikationAngefordert[i] = false;
        }
    }

    /***********Standard Setter Getter*******************************/

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public int[] getPublikationenZustellung() {
        return publikationenZustellung;
    }

    public void setPublikationenZustellung(int[] publikationenZustellung) {
        this.publikationenZustellung = publikationenZustellung;
    }

    public boolean[] getPublikationAngefordert() {
        return publikationAngefordert;
    }

    public void setPublikationAngefordert(boolean[] publikationAngefordert) {
        this.publikationAngefordert = publikationAngefordert;
    }

}
