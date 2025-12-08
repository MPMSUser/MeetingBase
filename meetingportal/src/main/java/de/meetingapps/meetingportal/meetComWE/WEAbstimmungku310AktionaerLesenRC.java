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
package de.meetingapps.meetingportal.meetComWE;

import java.util.List;

import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungSet;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;

public class WEAbstimmungku310AktionaerLesenRC extends WERootRC {

    /*TODO #KONS
     * rc in dieser Funktion nur temporär verwendet - Codierung noch zu konsolidieren!
     * 
     *-2=nicht vorhanden
     *-4=nicht präsent
     */

    /*Return-Wert von startStimmabgabe()*/
    public int rcFachlich=0;
    
    public EclAktienregister aktienregister=null;
    
    /*+++++++++Aus EclAbstimmungSetM+++++++*/
    
    public int versionAbstimmungenStart = 0;
    public int versionWeisungenStart = 0;

    /**Wird aus dem Puffer gefüllt*/
    public int versionAbstimmungenAktuell = 0;
    public int versionWeisungenAktuell = 0;
    public int versionAbstimmungenWeisungenOhneAbbruchAktuell = 0;

    public EclAbstimmungSet abstimmungSet = null;

    /*+++++++++++++++Aus EclAbstimmungenListeM++++++++++++++++*/
    public List<EclAbstimmungM> abstimmungenListeM;
    public List<EclAbstimmungM> gegenantraegeListeM;

    public boolean gegenantraegeVorhanden = true;

    public int alternative = 1;

    public EclWeisungMeldung weisungMeldung = null;
    public EclWeisungMeldungRaw weisungMeldungRaw = null;

    
    /*+++++++++++++++Aus TStimmabgabeku310Session+++++++++++++++++++++++*/
    public boolean warnungBeendenBereitsAngezeigt=false;

    
    /*Alt*/
    
    public int meldungsIdent = 0;
    public String name = "";
    public String vorname = "";
    public String ort = "";
    public int gattungId=0;

    /*alle 0 => noch keine Abstimmung vorhanden*/
    public int[] abstimmart = null;
    
    /**Liefert die aktuelle Abstimmungsversion, zum Abgleich der bereits auf dem Tablet
     * lokal gespeicherten Version*/
     
    public int abstimmungsVersion=0;

    public int getRcFachlich() {
        return rcFachlich;
    }

    public void setRcFachlich(int rcFachlich) {
        this.rcFachlich = rcFachlich;
    }

    public EclAktienregister getAktienregister() {
        return aktienregister;
    }

    public void setAktienregister(EclAktienregister aktienregister) {
        this.aktienregister = aktienregister;
    }

    public int getVersionAbstimmungenStart() {
        return versionAbstimmungenStart;
    }

    public void setVersionAbstimmungenStart(int versionAbstimmungenStart) {
        this.versionAbstimmungenStart = versionAbstimmungenStart;
    }

    public int getVersionWeisungenStart() {
        return versionWeisungenStart;
    }

    public void setVersionWeisungenStart(int versionWeisungenStart) {
        this.versionWeisungenStart = versionWeisungenStart;
    }

    public int getVersionAbstimmungenAktuell() {
        return versionAbstimmungenAktuell;
    }

    public void setVersionAbstimmungenAktuell(int versionAbstimmungenAktuell) {
        this.versionAbstimmungenAktuell = versionAbstimmungenAktuell;
    }

    public int getVersionWeisungenAktuell() {
        return versionWeisungenAktuell;
    }

    public void setVersionWeisungenAktuell(int versionWeisungenAktuell) {
        this.versionWeisungenAktuell = versionWeisungenAktuell;
    }

    public int getVersionAbstimmungenWeisungenOhneAbbruchAktuell() {
        return versionAbstimmungenWeisungenOhneAbbruchAktuell;
    }

    public void setVersionAbstimmungenWeisungenOhneAbbruchAktuell(int versionAbstimmungenWeisungenOhneAbbruchAktuell) {
        this.versionAbstimmungenWeisungenOhneAbbruchAktuell = versionAbstimmungenWeisungenOhneAbbruchAktuell;
    }

    public EclAbstimmungSet getAbstimmungSet() {
        return abstimmungSet;
    }

    public void setAbstimmungSet(EclAbstimmungSet abstimmungSet) {
        this.abstimmungSet = abstimmungSet;
    }

    public List<EclAbstimmungM> getAbstimmungenListeM() {
        return abstimmungenListeM;
    }

    public void setAbstimmungenListeM(List<EclAbstimmungM> abstimmungenListeM) {
        this.abstimmungenListeM = abstimmungenListeM;
    }

    public List<EclAbstimmungM> getGegenantraegeListeM() {
        return gegenantraegeListeM;
    }

    public void setGegenantraegeListeM(List<EclAbstimmungM> gegenantraegeListeM) {
        this.gegenantraegeListeM = gegenantraegeListeM;
    }

    public boolean isGegenantraegeVorhanden() {
        return gegenantraegeVorhanden;
    }

    public void setGegenantraegeVorhanden(boolean gegenantraegeVorhanden) {
        this.gegenantraegeVorhanden = gegenantraegeVorhanden;
    }

    public int getAlternative() {
        return alternative;
    }

    public void setAlternative(int alternative) {
        this.alternative = alternative;
    }

    public EclWeisungMeldung getWeisungMeldung() {
        return weisungMeldung;
    }

    public void setWeisungMeldung(EclWeisungMeldung weisungMeldung) {
        this.weisungMeldung = weisungMeldung;
    }

    public EclWeisungMeldungRaw getWeisungMeldungRaw() {
        return weisungMeldungRaw;
    }

    public void setWeisungMeldungRaw(EclWeisungMeldungRaw weisungMeldungRaw) {
        this.weisungMeldungRaw = weisungMeldungRaw;
    }

    public boolean isWarnungBeendenBereitsAngezeigt() {
        return warnungBeendenBereitsAngezeigt;
    }

    public void setWarnungBeendenBereitsAngezeigt(boolean warnungBeendenBereitsAngezeigt) {
        this.warnungBeendenBereitsAngezeigt = warnungBeendenBereitsAngezeigt;
    }

    public int getMeldungsIdent() {
        return meldungsIdent;
    }

    public void setMeldungsIdent(int meldungsIdent) {
        this.meldungsIdent = meldungsIdent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public int getGattungId() {
        return gattungId;
    }

    public void setGattungId(int gattungId) {
        this.gattungId = gattungId;
    }

    public int[] getAbstimmart() {
        return abstimmart;
    }

    public void setAbstimmart(int[] abstimmart) {
        this.abstimmart = abstimmart;
    }

    public int getAbstimmungsVersion() {
        return abstimmungsVersion;
    }

    public void setAbstimmungsVersion(int abstimmungsVersion) {
        this.abstimmungsVersion = abstimmungsVersion;
    }

}
