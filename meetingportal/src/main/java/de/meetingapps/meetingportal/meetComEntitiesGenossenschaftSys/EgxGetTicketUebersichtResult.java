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

public class EgxGetTicketUebersichtResult {

	public int kunde = 0;
	public String ticket = "";
    public String anlagedatum = "";
    public String status = "";
    public int rohdaten_lfnr = 0;
    public int prozess_id = 0;
    public String geaendertdatum = "";
    public String erledigtdatum = "";
    
    /*
     * Standard Getter und Setter
     */
    
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getAnlagedatum() {
		return anlagedatum;
	}
	public void setAnlagedatum(String anlagedatum) {
		this.anlagedatum = anlagedatum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getKunde() {
		return kunde;
	}
	public void setKunde(int kunde) {
		this.kunde = kunde;
	}
	public int getRohdaten_lfnr() {
		return rohdaten_lfnr;
	}
	public void setRohdaten_lfnr(int rohdaten_lfnr) {
		this.rohdaten_lfnr = rohdaten_lfnr;
	}
	public int getProzess_id() {
		return prozess_id;
	}
	public void setProzess_id(int prozess_id) {
		this.prozess_id = prozess_id;
	}
	public String getGeaendertdatum() {
		return geaendertdatum;
	}
	public void setGeaendertdatum(String geaendertdatum) {
		this.geaendertdatum = geaendertdatum;
	}
	public String getErledigtdatum() {
		return erledigtdatum;
	}
	public void setErledigtdatum(String erledigtdatum) {
		this.erledigtdatum = erledigtdatum;
	}
    
    
    
    
    
}
