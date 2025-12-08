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
package de.meetingapps.meetingportal.meetingportTController;

import java.io.Serializable;
import java.util.Date;

import de.meetingapps.meetingportal.meetComAllg.CaString;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

/**Enthält Request-übergreifende Variablen für das Permanentportal.
 * 
 * Verarbeitungslogiken in BrM*
 */
@SessionScoped
@Named
public class TPermanentSession implements Serializable {
    private static final long serialVersionUID = 5758822358005488846L;

    /**true => die Schnittstelle arbeitet im Testmodus ohne reale Kommunikation
     * zum Aktienregister.
     * 
     * Folgende Fälle werden derzeit unterstützt:
     * Aktionärsnummer 
     * 1: nicht vorhanden im Register
     * 2: vorhanden im Register, aber nicht registriert
     * 3: vorhanden im Register, registriert 
     * 
     * 
     * alle anderen: nicht vorhanden im Register
     */
    private boolean testModus=false;

     /**Die folgenden Felder werden durch die Anbindung ans Aktienregister gesetzt.
     * Sie müssen dann - nach dem Login-Prozess-Abschluß
     */
    private String anmeldeKennungFuerAnzeige = "";

    private String titelVornameName = "";
    private String ort = "";

    
    /**Für Vereine u.ä. im Zusammenhang mit Permanent-Portal, wenn Stimmen immer =1, aber
     * Anteile für Anzeige o.ä. benötigt wird.
     */
    private int anteile=0;

    /**JWT-Token Parameter*/
    private String jwt_token = null;
	private Date jwt_exp_date = null;
	
	/***********************Spezial Getter und Setter*******************************************/
	public double getCo2Ersparnis() {
        return this.anteile * 0.2;
    }

    public String getCo2ErsparnisDE() {
        return CaString.doubleToStringDEKurz(this.anteile * 0.2);
    }
    
    /***********************Standard getter und setter******************************************/
    public boolean isTestModus() {
        return testModus;
    }

    public void setTestModus(boolean testModus) {
        this.testModus = testModus;
    }


	public String getJwt_token() {
		return jwt_token;
	}

	public void setJwt_token(String jwt_token) {
		this.jwt_token = jwt_token;
	}

	public Date getJwt_exp_date() {
		return jwt_exp_date;
	}

	public void setJwt_exp_date(Date jwt_exp_date) {
		this.jwt_exp_date = jwt_exp_date;
	}

    public int getAnteile() {
        return anteile;
    }

    public void setAnteile(int anteile) {
        this.anteile = anteile;
    }

    public String getAnmeldeKennungFuerAnzeige() {
        return anmeldeKennungFuerAnzeige;
    }

    public void setAnmeldeKennungFuerAnzeige(String anmeldeKennungFuerAnzeige) {
        this.anmeldeKennungFuerAnzeige = anmeldeKennungFuerAnzeige;
    }

    public String getTitelVornameName() {
        return titelVornameName;
    }

    public void setTitelVornameName(String titelVornameName) {
        this.titelVornameName = titelVornameName;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }
    
}
