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
package de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys;

public class EgxGetKuendigungResult {

	public String leistung = "";
	public long id = 0;
    public int anteile = 0;
    public String wirksamkeit = "";
    public Boolean laufende_ruecknahme = false;
    
    /*
     * Standard getter und Setter
     */
    
	public String getLeistung() {
		return leistung;
	}
	public void setLeistung(String leistung) {
		this.leistung = leistung;
	}
	public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getAnteile() {
		return anteile;
	}
	public void setAnteile(int anteile) {
		this.anteile = anteile;
	}
	public String getWirksamkeit() {
		return wirksamkeit;
	}
	public void setWirksamkeit(String wirksamkeit) {
		this.wirksamkeit = wirksamkeit;
	}
    public Boolean getLaufende_ruecknahme() {
        return laufende_ruecknahme;
    }
    public void setLaufende_ruecknahme(Boolean laufende_ruecknahme) {
        this.laufende_ruecknahme = laufende_ruecknahme;
    }
    
    
}
