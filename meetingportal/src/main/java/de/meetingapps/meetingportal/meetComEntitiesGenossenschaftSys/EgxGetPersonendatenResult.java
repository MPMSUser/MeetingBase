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

public class EgxGetPersonendatenResult {

	public String art = "";
    public String anrede = "";
    public String nachname = "";
    public String vorname = "";
    public int lfnr = 0;
    public String status = "";
    public String email = "";
    public String fax = "";
    public String mobil = "";
    public String plz = "";
    public String ort = "";
    public String strasse = "";
    public String geburt = "";
    public String Land = "";
    public boolean postempfaenger = true;
    public String steuer = "";
    
    /*
     * Standard Getter und Setter
     */
    
	public String getArt() {
		return art;
	}
	public void setArt(String art) {
		this.art = art;
	}
	public String getAnrede() {
		return anrede;
	}
	public void setAnrede(String anrede) {
		this.anrede = anrede;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getMobil() {
		return mobil;
	}
	public void setMobil(String mobil) {
		this.mobil = mobil;
	}
	public String getPlz() {
		return plz;
	}
	public void setPlz(String plz) {
		this.plz = plz;
	}
	public String getOrt() {
		return ort;
	}
	public void setOrt(String ort) {
		this.ort = ort;
	}
	public String getStrasse() {
		return strasse;
	}
	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}
	public String getGeburt() {
		return geburt;
	}
	public void setGeburt(String geburt) {
		this.geburt = geburt;
	}
	public String getLand() {
		return Land;
	}
	public void setLand(String land) {
		Land = land;
	}
	public boolean isPostempfaenger() {
		return postempfaenger;
	}
	public void setPostempfaenger(boolean postempfaenger) {
		this.postempfaenger = postempfaenger;
	}
	public String getSteuer() {
		return steuer;
	}
	public void setSteuer(String steuer) {
		this.steuer = steuer;
	}
	public int getLfnr() {
		return lfnr;
	}
	public void setLfnr(int lfnr) {
		this.lfnr = lfnr;
	}
	
}
