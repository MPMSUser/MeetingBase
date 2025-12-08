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

import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungSet;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

/**Dokumentation siehe EclAbstimmungSet*/
@SessionScoped
@Named
public class EclAbstimmungSetM implements Serializable {
    private static final long serialVersionUID = 7594843729812058229L;

    /**Wird zu Beginn eines Weisungs-/Abstimmungsvorgang gesetzt, und am Ende mit der aktuellen Version
     * verglichen. Wenn Start-Version != aktuelle Version, dann kein Abschließen mehr möglich!
     */
    private int versionAbstimmungenStart = 0;
    private int versionWeisungenStart = 0;

    /**Wird aus dem Puffer gefüllt*/
    private int versionAbstimmungenAktuell = 0;
    private int versionWeisungenAktuell = 0;
    private int versionAbstimmungenWeisungenOhneAbbruchAktuell = 0;

    private EclAbstimmungSet abstimmungSet = null;

    /*********Standard Getter und Setter*************************/

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

    public EclAbstimmungSet getAbstimmungSet() {
        return abstimmungSet;
    }

    public void setAbstimmungSet(EclAbstimmungSet abstimmungSet) {
        this.abstimmungSet = abstimmungSet;
    }

    public int getVersionAbstimmungenWeisungenOhneAbbruchAktuell() {
        return versionAbstimmungenWeisungenOhneAbbruchAktuell;
    }

    public void setVersionAbstimmungenWeisungenOhneAbbruchAktuell(int versionAbstimmungenWeisungenOhneAbbruchAktuell) {
        this.versionAbstimmungenWeisungenOhneAbbruchAktuell = versionAbstimmungenWeisungenOhneAbbruchAktuell;
    }

}
