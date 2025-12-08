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

public class EclVeranstaltungenVeranstaltung  implements Serializable {
    private static final long serialVersionUID = -8993246383547331232L;

    public int identVeranstaltung=0;
    
    /**Verweis auf Menü-Eintrag (>0).
     * 
     * <0 - Spezial-Verlinkung:
     * -1 => Anmeldung/Abmeldung Vereins-Portal
     * 
     * Eine Veranstaltung kann an bis zu 3 Menüpunkten aufgehängt werden.
     */
    public int menueNummer1=0;
    public int positionInMenue1=0;

    public int menueNummer2=0;
    public int positionInMenue2=0;

    public int menueNummer3=0;
    public int positionInMenue3=0;

    /**Len=1000*/
    public String beschreibung="";
    
    /**FÜr Reports.
     * LEN=100
     */
    public String textIntern="";
    
    /**Verschiedene Status-Werte, die in den Sub-Elementen abgefragt werden können.
     * Fixe Verwendung:
     * 0 = Veranstaltung wird derzeit nicht angezeigt.
     * >0 = Veranstaltung wird angezeigt
     * Denkbar z.B.:
     * 1= Derzeit keine Veranstaltung geplant
     * 2= Nächste Veranstaltung vorgesehen - aber noch keine Anmeldung möglich
     * 3= Anmeldung möglich
     * 4= Überbucht
     * 5= Veranstaltung vorbei.
     * 
     * Wie gesagt, nur als Anregungen.
     */
    public int aktivierungsStatus=0;
    
     
    /*********************Nicht in Datenbank*************************************/
    public List<EclVeranstaltungenElement> veranstaltungenElementListe=new LinkedList<EclVeranstaltungenElement>();
    public boolean pruefeObElementeVorhanden() {
        return (veranstaltungenElementListe!=null && veranstaltungenElementListe.size()>0);
    }
    
    public List<EclVeranstaltungenElement> veranstaltungenElementAnzeigenListe=new LinkedList<EclVeranstaltungenElement>();
    public boolean pruefeObElementeAnzeigenVorhanden() {
        return (veranstaltungenElementAnzeigenListe!=null && veranstaltungenElementAnzeigenListe.size()>0);
    }
  
    /**Nur für Simulation*/
    public String aktivierungsStatusEingabe="";
    
    public EclVeranstaltungenVeranstaltung() {
   
    }
    
    /**Übernimmt die nicht-variablen Felder (einschließlich veranstaltungenElementListe)*/
    public EclVeranstaltungenVeranstaltung(EclVeranstaltungenVeranstaltung pEclVeranstaltungenVeranstaltung, String[] defaultWerte){
        identVeranstaltung=pEclVeranstaltungenVeranstaltung.identVeranstaltung;
        menueNummer1=pEclVeranstaltungenVeranstaltung.menueNummer1;
        positionInMenue1=pEclVeranstaltungenVeranstaltung.positionInMenue1;
        menueNummer2=pEclVeranstaltungenVeranstaltung.menueNummer2;
        positionInMenue2=pEclVeranstaltungenVeranstaltung.positionInMenue2;
        menueNummer3=pEclVeranstaltungenVeranstaltung.menueNummer3;
        positionInMenue3=pEclVeranstaltungenVeranstaltung.positionInMenue3;
        beschreibung=pEclVeranstaltungenVeranstaltung.beschreibung;
        textIntern=pEclVeranstaltungenVeranstaltung.textIntern;
        aktivierungsStatus=pEclVeranstaltungenVeranstaltung.aktivierungsStatus;
        
        if (pEclVeranstaltungenVeranstaltung.veranstaltungenElementListe!=null && pEclVeranstaltungenVeranstaltung.veranstaltungenElementListe.size()>0) {
            for (int i=0;i<pEclVeranstaltungenVeranstaltung.veranstaltungenElementListe.size();i++) {
                veranstaltungenElementListe.add(new EclVeranstaltungenElement(pEclVeranstaltungenVeranstaltung.veranstaltungenElementListe.get(i), defaultWerte, this, null));
            }
        }
        
    }
    
    
    /********************************Standard getter und setter***********************************************/
    
    public int getIdentVeranstaltung() {
        return identVeranstaltung;
    }

    public void setIdentVeranstaltung(int identVeranstaltung) {
        this.identVeranstaltung = identVeranstaltung;
    }

    public int getMenueNummer1() {
        return menueNummer1;
    }

    public void setMenueNummer1(int menueNummer1) {
        this.menueNummer1 = menueNummer1;
    }

    public int getPositionInMenue1() {
        return positionInMenue1;
    }

    public void setPositionInMenue1(int positionInMenue1) {
        this.positionInMenue1 = positionInMenue1;
    }

    public int getMenueNummer2() {
        return menueNummer2;
    }

    public void setMenueNummer2(int menueNummer2) {
        this.menueNummer2 = menueNummer2;
    }

    public int getPositionInMenue2() {
        return positionInMenue2;
    }

    public void setPositionInMenue2(int positionInMenue2) {
        this.positionInMenue2 = positionInMenue2;
    }

    public int getMenueNummer3() {
        return menueNummer3;
    }

    public void setMenueNummer3(int menueNummer3) {
        this.menueNummer3 = menueNummer3;
    }

    public int getPositionInMenue3() {
        return positionInMenue3;
    }

    public void setPositionInMenue3(int positionInMenue3) {
        this.positionInMenue3 = positionInMenue3;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getTextIntern() {
        return textIntern;
    }

    public void setTextIntern(String textIntern) {
        this.textIntern = textIntern;
    }

    public int getAktivierungsStatus() {
        return aktivierungsStatus;
    }

    public void setAktivierungsStatus(int aktivierungsStatus) {
        this.aktivierungsStatus = aktivierungsStatus;
    }

    public List<EclVeranstaltungenElement> getVeranstaltungenElementListe() {
        return veranstaltungenElementListe;
    }

    public void setVeranstaltungenElementListe(List<EclVeranstaltungenElement> veranstaltungenElementListe) {
        this.veranstaltungenElementListe = veranstaltungenElementListe;
    }

    public List<EclVeranstaltungenElement> getVeranstaltungenElementAnzeigenListe() {
        return veranstaltungenElementAnzeigenListe;
    }

    public void setVeranstaltungenElementAnzeigenListe(List<EclVeranstaltungenElement> veranstaltungenElementAnzeigenListe) {
        this.veranstaltungenElementAnzeigenListe = veranstaltungenElementAnzeigenListe;
    }

    public String getAktivierungsStatusEingabe() {
        return Integer.toString(aktivierungsStatus);
    }

    public void setAktivierungsStatusEingabe(String aktivierungsStatusEingabe) {
        this.aktivierungsStatus = Integer.parseInt(aktivierungsStatusEingabe);
    }

     
}
