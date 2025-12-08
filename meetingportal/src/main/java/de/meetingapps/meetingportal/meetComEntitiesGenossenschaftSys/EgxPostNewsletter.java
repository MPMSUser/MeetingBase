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

public class EgxPostNewsletter {
    
	public int personen_lfnr = 0;
    public String kein_newsletter = "";
    public String versandart = "";
    public String abw_email = "";
    
    
	public int getPersonen_lfnr() {
		return personen_lfnr;
	}
	public void setPersonen_lfnr(int personen_lfnr) {
		this.personen_lfnr = personen_lfnr;
	}
	public String getKein_newsletter() {
		return kein_newsletter;
	}
	public void setKein_newsletter(String kein_newsletter) {
		this.kein_newsletter = kein_newsletter;
	}
	public String getVersandart() {
		return versandart;
	}
	public void setVersandart(String versandart) {
		this.versandart = versandart;
	}
	public String getAbw_email() {
		return abw_email;
	}
	public void setAbw_email(String abw_email) {
		this.abw_email = abw_email;
	}
    
    
}
