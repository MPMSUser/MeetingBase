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

public class EclEmittentenFuerAktionaersPortalAuswahlM implements Serializable {
    private static final long serialVersionUID = 6650831588127673931L;

    private String mandant = "";
    private String hvJahr = "";
    private String hvNummer = "";
    private String dbArt = "";
    private String bezeichnungKurz = "";
    public String hvDatum = "";
    private boolean portalAktuellAktiv = false;
    private boolean portalSpracheDeutsch = false;
    private String textLinkDeutsch = "";
    private String linkDeutsch = "";
    private boolean portalSpracheEnglisch = false;
    private String textLinkEnglisch = "";
    private String linkEnglisch = "";
    private boolean dbGesperrt = false;

    /****************Standard Getter und Setter****************************/
    public String getMandant() {
        return mandant;
    }

    public void setMandant(String mandant) {
        this.mandant = mandant;
    }

    public String getHvJahr() {
        return hvJahr;
    }

    public void setHvJahr(String hvJahr) {
        this.hvJahr = hvJahr;
    }

    public String getHvNummer() {
        return hvNummer;
    }

    public void setHvNummer(String hvNummer) {
        this.hvNummer = hvNummer;
    }

    public String getDbArt() {
        return dbArt;
    }

    public void setDbArt(String dbArt) {
        this.dbArt = dbArt;
    }

    public String getBezeichnungKurz() {
        return bezeichnungKurz;
    }

    public void setBezeichnungKurz(String bezeichnungKurz) {
        this.bezeichnungKurz = bezeichnungKurz;
    }

    public String getTextLinkDeutsch() {
        return textLinkDeutsch;
    }

    public void setTextLinkDeutsch(String textLinkDeutsch) {
        this.textLinkDeutsch = textLinkDeutsch;
    }

    public String getLinkDeutsch() {
        return linkDeutsch;
    }

    public void setLinkDeutsch(String linkDeutsch) {
        this.linkDeutsch = linkDeutsch;
    }

    public String getTextLinkEnglisch() {
        return textLinkEnglisch;
    }

    public void setTextLinkEnglisch(String textLinkEnglisch) {
        this.textLinkEnglisch = textLinkEnglisch;
    }

    public String getLinkEnglisch() {
        return linkEnglisch;
    }

    public void setLinkEnglisch(String linkEnglisch) {
        this.linkEnglisch = linkEnglisch;
    }

    public String getHvDatum() {
        return hvDatum;
    }

    public void setHvDatum(String hvDatum) {
        this.hvDatum = hvDatum;
    }

    public boolean isPortalAktuellAktiv() {
        return portalAktuellAktiv;
    }

    public void setPortalAktuellAktiv(boolean portalAktuellAktiv) {
        this.portalAktuellAktiv = portalAktuellAktiv;
    }

    public boolean isPortalSpracheDeutsch() {
        return portalSpracheDeutsch;
    }

    public void setPortalSpracheDeutsch(boolean portalSpracheDeutsch) {
        this.portalSpracheDeutsch = portalSpracheDeutsch;
    }

    public boolean isPortalSpracheEnglisch() {
        return portalSpracheEnglisch;
    }

    public void setPortalSpracheEnglisch(boolean portalSpracheEnglisch) {
        this.portalSpracheEnglisch = portalSpracheEnglisch;
    }

    public boolean isDbGesperrt() {
        return dbGesperrt;
    }

    public void setDbGesperrt(boolean dbGesperrt) {
        this.dbGesperrt = dbGesperrt;
    }

}
