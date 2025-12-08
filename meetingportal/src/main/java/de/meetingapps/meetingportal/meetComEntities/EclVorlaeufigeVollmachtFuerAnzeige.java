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
package de.meetingapps.meetingportal.meetComEntities;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComDb.DbBundle;

public class EclVorlaeufigeVollmachtFuerAnzeige implements Serializable {
    private static final long serialVersionUID = 1422014499741485602L;

    /**Beschreibung siehe EclVorlaeufigeVollmacht*, soweit identisch*/

    public int ident = 0;
    public int storniert = 0;

    public String liefereStorniertText() {
        switch (storniert) {
        case 0: {
            return "989";
        } //Kann eigentlich nicht auftreten, da diese nicht angezeigt werden
        case 1: {
            return "987";
        }
        case 2: {
            return "990";
        }
        case 3: {
            return "991";
        }
        case 4: {
            return "992";
        }
        }
        return "";
    }

    public String bevollmaechtigterTitel = "";
    public String bevollmaechtigterName = "";
    public String bevollmaechtigterVorname = "";

    public String liefereNameKomplett() {
        String hString = bevollmaechtigterTitel;
        if (!hString.isEmpty()) {
            hString = hString + " ";
        }
        hString = hString + bevollmaechtigterVorname;
        if (!bevollmaechtigterVorname.isEmpty()) {
            hString = hString + " ";
        }
        hString = hString + bevollmaechtigterName;
        return hString;
    }

    public String bevollmaechtigterStrasse = "";
    public String bevollmaechtigterPlz = "";
    public String bevollmaechtigterOrt = "";

    public String liefereOrtKomplett() {
        String hString = bevollmaechtigterPlz;
        if (!hString.isEmpty()) {
            hString = hString + " ";
        }
        hString = hString + bevollmaechtigterOrt;
        return hString;
    }

    public int pruefstatus = 0;

    public String lieferePruefstatusText() {
        switch (pruefstatus) {
        case 0: {
            return "967";
        }
        case 1: {
            return "968";
        }
        case 2: {
            return "969";
        }
        case 3: {
            return "970";
        }
        case 4: {
            return "971";
        }
        }
        return "";
    }

    public int abgelehntWeil = 0;

    public String liefereAbgelehntWeilText() {
        switch (abgelehntWeil) {
        case 0: {
            return "972";
        }
        case 1: {
            return "973";
        }
        case 2: {
            return "974";
        }
        case 3: {
            return "975";
        }
        case 4: {
            return "976";
        }
        case 5: {
            return "977";
        }
        case 6: {
            return "978";
        }
        case 7: {
            return "979";
        }
        case 8: {
            return "980";
        }
        }
        return "";
    }

    public String abgelehntWeilText = "";

    public boolean bevollmaechtigterIstGesetzlich = false;

    public boolean vollmachtIstFolgeVonGesetzlicher = false;

    /**Vollmachtgebender Aktionär*/
    public String aktionaerNummerFuerAnzeige = "";
    public String aktionaerTitel = "";
    public String aktionaerName = "";
    public String aktionaerVorname = "";

    public String liefereAktionaerNameKomplett() {
        String hString = aktionaerTitel;
        if (!hString.isEmpty()) {
            hString = hString + " ";
        }
        hString = hString + aktionaerVorname;
        if (!aktionaerVorname.isEmpty()) {
            hString = hString + " ";
        }
        hString = hString + aktionaerName;
        return hString;
    }

    public String aktionaerStrasse = "";
    public String aktionaerPlz = "";
    public String aktionaerOrt = "";

    public String liefereAktionaerOrtKomplett() {
        String hString = aktionaerPlz;
        if (!hString.isEmpty()) {
            hString = hString + " ";
        }
        hString = hString + aktionaerOrt;
        return hString;
    }

    
    /**Nur für ku216-Ablauf*/
    public int meldeIdent=0;
    
    
    public EclVorlaeufigeVollmachtFuerAnzeige() {
    }

    public EclVorlaeufigeVollmachtFuerAnzeige(EclVorlaeufigeVollmacht pEclVorlaeufigeVollmachtFuerAnzeige,
            DbBundle pDbBundle) {
        ident = pEclVorlaeufigeVollmachtFuerAnzeige.ident;
        storniert = pEclVorlaeufigeVollmachtFuerAnzeige.storniert;
        bevollmaechtigterTitel = pEclVorlaeufigeVollmachtFuerAnzeige.bevollmaechtigterTitel;
        bevollmaechtigterName = pEclVorlaeufigeVollmachtFuerAnzeige.bevollmaechtigterName;
        bevollmaechtigterVorname = pEclVorlaeufigeVollmachtFuerAnzeige.bevollmaechtigterVorname;
        bevollmaechtigterStrasse = pEclVorlaeufigeVollmachtFuerAnzeige.bevollmaechtigterStrasse;
        bevollmaechtigterPlz = pEclVorlaeufigeVollmachtFuerAnzeige.bevollmaechtigterPlz;
        bevollmaechtigterOrt = pEclVorlaeufigeVollmachtFuerAnzeige.bevollmaechtigterOrt;
        pruefstatus = pEclVorlaeufigeVollmachtFuerAnzeige.pruefstatus;
        abgelehntWeil = pEclVorlaeufigeVollmachtFuerAnzeige.abgelehntWeil;
        abgelehntWeilText = pEclVorlaeufigeVollmachtFuerAnzeige.abgelehntWeilText;
        bevollmaechtigterIstGesetzlich = (pEclVorlaeufigeVollmachtFuerAnzeige.bevollmaechtigterAusgefuehrtIstGesetzlich == 1);
        vollmachtIstFolgeVonGesetzlicher = pEclVorlaeufigeVollmachtFuerAnzeige.vollmachtIstFolgeVonGesetzlicher;

        if (pEclVorlaeufigeVollmachtFuerAnzeige.eclAktienregister != null) {
            aktionaerNummerFuerAnzeige = BlNummernformBasis.aufbereitenInternFuerExtern(
                    pEclVorlaeufigeVollmachtFuerAnzeige.eclAktienregister.aktionaersnummer, pDbBundle);
            aktionaerTitel = pEclVorlaeufigeVollmachtFuerAnzeige.eclAktienregister.titel;
            aktionaerName = pEclVorlaeufigeVollmachtFuerAnzeige.eclAktienregister.liefereName();
            aktionaerVorname = pEclVorlaeufigeVollmachtFuerAnzeige.eclAktienregister.vorname;
            aktionaerStrasse = pEclVorlaeufigeVollmachtFuerAnzeige.eclAktienregister.strasse;
            aktionaerPlz = pEclVorlaeufigeVollmachtFuerAnzeige.eclAktienregister.postleitzahl;
            aktionaerOrt = pEclVorlaeufigeVollmachtFuerAnzeige.eclAktienregister.ort;
        }

    }

    /**********************Standard getter und setter************************************/

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public int getStorniert() {
        return storniert;
    }

    public void setStorniert(int storniert) {
        this.storniert = storniert;
    }

    public String getBevollmaechtigterTitel() {
        return bevollmaechtigterTitel;
    }

    public void setBevollmaechtigterTitel(String bevollmaechtigterTitel) {
        this.bevollmaechtigterTitel = bevollmaechtigterTitel;
    }

    public String getBevollmaechtigterName() {
        return bevollmaechtigterName;
    }

    public void setBevollmaechtigterName(String bevollmaechtigterName) {
        this.bevollmaechtigterName = bevollmaechtigterName;
    }

    public String getBevollmaechtigterVorname() {
        return bevollmaechtigterVorname;
    }

    public void setBevollmaechtigterVorname(String bevollmaechtigterVorname) {
        this.bevollmaechtigterVorname = bevollmaechtigterVorname;
    }

    public String getBevollmaechtigterStrasse() {
        return bevollmaechtigterStrasse;
    }

    public void setBevollmaechtigterStrasse(String bevollmaechtigterStrasse) {
        this.bevollmaechtigterStrasse = bevollmaechtigterStrasse;
    }

    public String getBevollmaechtigterPlz() {
        return bevollmaechtigterPlz;
    }

    public void setBevollmaechtigterPlz(String bevollmaechtigterPlz) {
        this.bevollmaechtigterPlz = bevollmaechtigterPlz;
    }

    public String getBevollmaechtigterOrt() {
        return bevollmaechtigterOrt;
    }

    public void setBevollmaechtigterOrt(String bevollmaechtigterOrt) {
        this.bevollmaechtigterOrt = bevollmaechtigterOrt;
    }

    public int getPruefstatus() {
        return pruefstatus;
    }

    public void setPruefstatus(int pruefstatus) {
        this.pruefstatus = pruefstatus;
    }

    public int getAbgelehntWeil() {
        return abgelehntWeil;
    }

    public void setAbgelehntWeil(int abgelehntWeil) {
        this.abgelehntWeil = abgelehntWeil;
    }

    public String getAbgelehntWeilText() {
        return abgelehntWeilText;
    }

    public void setAbgelehntWeilText(String abgelehntWeilText) {
        this.abgelehntWeilText = abgelehntWeilText;
    }

    public boolean isBevollmaechtigterIstGesetzlich() {
        return bevollmaechtigterIstGesetzlich;
    }

    public void setBevollmaechtigterIstGesetzlich(boolean bevollmaechtigterIstGesetzlich) {
        this.bevollmaechtigterIstGesetzlich = bevollmaechtigterIstGesetzlich;
    }

    public boolean isVollmachtIstFolgeVonGesetzlicher() {
        return vollmachtIstFolgeVonGesetzlicher;
    }

    public void setVollmachtIstFolgeVonGesetzlicher(boolean vollmachtIstFolgeVonGesetzlicher) {
        this.vollmachtIstFolgeVonGesetzlicher = vollmachtIstFolgeVonGesetzlicher;
    }

    public String getAktionaerNummerFuerAnzeige() {
        return aktionaerNummerFuerAnzeige;
    }

    public void setAktionaerNummerFuerAnzeige(String aktionaerNummerFuerAnzeige) {
        this.aktionaerNummerFuerAnzeige = aktionaerNummerFuerAnzeige;
    }

    public String getAktionaerTitel() {
        return aktionaerTitel;
    }

    public void setAktionaerTitel(String aktionaerTitel) {
        this.aktionaerTitel = aktionaerTitel;
    }

    public String getAktionaerName() {
        return aktionaerName;
    }

    public void setAktionaerName(String aktionaerName) {
        this.aktionaerName = aktionaerName;
    }

    public String getAktionaerVorname() {
        return aktionaerVorname;
    }

    public void setAktionaerVorname(String aktionaerVorname) {
        this.aktionaerVorname = aktionaerVorname;
    }

    public String getAktionaerStrasse() {
        return aktionaerStrasse;
    }

    public void setAktionaerStrasse(String aktionaerStrasse) {
        this.aktionaerStrasse = aktionaerStrasse;
    }

    public String getAktionaerPlz() {
        return aktionaerPlz;
    }

    public void setAktionaerPlz(String aktionaerPlz) {
        this.aktionaerPlz = aktionaerPlz;
    }

    public String getAktionaerOrt() {
        return aktionaerOrt;
    }

    public void setAktionaerOrt(String aktionaerOrt) {
        this.aktionaerOrt = aktionaerOrt;
    }

    public int getMeldeIdent() {
        return meldeIdent;
    }

    public void setMeldeIdent(int meldeIdent) {
        this.meldeIdent = meldeIdent;
    }

}