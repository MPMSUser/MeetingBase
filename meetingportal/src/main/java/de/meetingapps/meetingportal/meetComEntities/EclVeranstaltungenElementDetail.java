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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;

public class EclVeranstaltungenElementDetail  implements Serializable {
    private static final long serialVersionUID = -6429953506232673506L;

    int logDrucken=10;
    
    public int identDetail=0;
    
    public int gehoertZuElement=0;
    public int position=0;
    
    /**Text für Anzeige im Portal
     * LEN=500*/
    public String textExtern="";
    
    /**Text für Anzeige bei internen Reports
     * LEN=100*/
    public String textIntern="";
    
    /**Bei Radiobuttons: Wert, der im Wert des zugehörigen ELements eingetragen wird,
     * wenn dieser Button aktiviert wird.
     * LEN=5*/
    public String erzeugtWert="";
    

    /**Text für Anzeige in Quittung, wenn diese Option ausgewählt
     * LEN=1000*/
    public String textQuittung="";

    /*++++++++++nicht in Datenbank++++++++++++++++*/
    /**Für Checkboxen - aktuelle Selektion*/
    public boolean geradeSelektiert=false;
    public boolean geradeSelektiertAlterWert=false;
    
    public List<EclVeranstaltungenAktion> veranstaltungenAktionListe=new LinkedList<EclVeranstaltungenAktion>();

    public int summeUeberAlleTeilnehmer=0;
    public double summeUeberAlleTeilnehmerRelativiert=0;
    
    public EclVeranstaltungenElementDetail() {
        
    }
    
    public EclVeranstaltungenElementDetail(EclVeranstaltungenElementDetail pEclVeranstaltungenElementDetail) {
        identDetail=pEclVeranstaltungenElementDetail.identDetail;
        gehoertZuElement=pEclVeranstaltungenElementDetail.gehoertZuElement;
        position=pEclVeranstaltungenElementDetail.position;
        textExtern=pEclVeranstaltungenElementDetail.textExtern;
        textIntern=pEclVeranstaltungenElementDetail.textIntern;
        erzeugtWert=pEclVeranstaltungenElementDetail.erzeugtWert;
        textQuittung=pEclVeranstaltungenElementDetail.textQuittung;
        
        if (pEclVeranstaltungenElementDetail.veranstaltungenAktionListe!=null && pEclVeranstaltungenElementDetail.veranstaltungenAktionListe.size()>0) {
            for (int i=0;i<pEclVeranstaltungenElementDetail.veranstaltungenAktionListe.size();i++) {
                CaBug.druckeLog("Kopiere Aktion="+pEclVeranstaltungenElementDetail.veranstaltungenAktionListe.get(i).identAktion, logDrucken, 10);
                veranstaltungenAktionListe.add(new EclVeranstaltungenAktion(pEclVeranstaltungenElementDetail.veranstaltungenAktionListe.get(i)));
            }
        }

    }

    /**************************Standard getter und setter***************************/
    public int getIdentDetail() {
        return identDetail;
    }

    public void setIdentDetail(int identDetail) {
        this.identDetail = identDetail;
    }

    public int getGehoertZuElement() {
        return gehoertZuElement;
    }

    public void setGehoertZuElement(int gehoertZuElement) {
        this.gehoertZuElement = gehoertZuElement;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTextExtern() {
        return textExtern;
    }

    public void setTextExtern(String textExtern) {
        this.textExtern = textExtern;
    }

    public String getTextIntern() {
        return textIntern;
    }

    public void setTextIntern(String textIntern) {
        this.textIntern = textIntern;
    }

    public String getErzeugtWert() {
        return erzeugtWert;
    }

    public void setErzeugtWert(String erzeugtWert) {
        this.erzeugtWert = erzeugtWert;
    }

    public String getTextQuittung() {
        return textQuittung;
    }

    public void setTextQuittung(String textQuittung) {
        this.textQuittung = textQuittung;
    }

    public boolean isGeradeSelektiert() {
        return geradeSelektiert;
    }

    public void setGeradeSelektiert(boolean geradeSelektiert) {
        this.geradeSelektiert = geradeSelektiert;
    }
}
