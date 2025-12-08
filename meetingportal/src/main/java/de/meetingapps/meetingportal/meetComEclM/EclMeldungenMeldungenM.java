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

import de.meetingapps.meetingportal.meetComEntities.EclMeldungenMeldungen;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclMeldungenMeldungenM implements Serializable {
    private static final long serialVersionUID = 13434130248480493L;

    private int mandant = 0;
    private String vonMeldungsIdent = "";
    private String zuMeldungsIdent = "";
    private int verwendung = 0;

    /*Nicht Bestandteil der Tabelle, aber beim Einlesen mit zugeordnet: Daten der zugeordneten Karte*/
    private String zutrittsIdent = "";
    private String name = "";
    private String vorname = "";
    private String ort = "";

    public void copyFrom(EclMeldungenMeldungen pMeldungenMeldungen) {
        mandant = pMeldungenMeldungen.mandant;
        vonMeldungsIdent = Integer.toString(pMeldungenMeldungen.vonMeldungsIdent);
        zuMeldungsIdent = Integer.toString(pMeldungenMeldungen.zuMeldungsIdent);
        verwendung = pMeldungenMeldungen.verwendung;
        zutrittsIdent = pMeldungenMeldungen.zutrittsIdent;
        name = pMeldungenMeldungen.name;
        vorname = pMeldungenMeldungen.vorname;
        ort = pMeldungenMeldungen.ort;
    }

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public String getVonMeldungsIdent() {
        return vonMeldungsIdent;
    }

    public void setVonMeldungsIdent(String vonMeldungsIdent) {
        this.vonMeldungsIdent = vonMeldungsIdent;
    }

    public String getZuMeldungsIdent() {
        return zuMeldungsIdent;
    }

    public void setZuMeldungsIdent(String zuMeldungsIdent) {
        this.zuMeldungsIdent = zuMeldungsIdent;
    }

    public int getVerwendung() {
        return verwendung;
    }

    public void setVerwendung(int verwendung) {
        this.verwendung = verwendung;
    }

    public String getZutrittsIdent() {
        return zutrittsIdent;
    }

    public void setZutrittsIdent(String zutrittsIdent) {
        this.zutrittsIdent = zutrittsIdent;
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

}
