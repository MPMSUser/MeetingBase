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
package de.meetingapps.meetingportal.meetingport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComBl.BlMitteilungen;
import de.meetingapps.meetingportal.meetComEh.EhJsfSelectItem;
import de.meetingapps.meetingportal.meetComEh.EhJsfSummeWortmeldeView;
import de.meetingapps.meetingportal.meetComEntities.EclInfo;
import de.meetingapps.meetingportal.meetComEntities.EclMitteilung;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischView;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UWortmeldungenSession implements Serializable {
    private static final long serialVersionUID = -6599895420858637724L;

    /**Wortmeldung die in Bearbeitung ist*/
    private EclMitteilung bWortmeldung = null;

    /**Nachricht an Versammlungsleiter, die in Bearbeitung ist*/
    private EclInfo bInfo=null;

    /**0= keine Bearbeitung
     * 1=Komplette Bearbeitung
     * 2=Kommentar Intern
     * 3=Kommentar Versammlungsleiter
     *
     * 4=Nachricht an Versammlungsleiter
     * 
     * 5=Reihenfolge Bearbeiten
     * 6=Redner Bearbeiten
     */
    private int inBearbeitung = 0;
    private String aktuellerStatus="";
    private String neuerStatus = "";
    private boolean praesenteVorhanden = false;

    /*********Anzeige der Links**********************/
    private String testraumLink="";
    private String rederaumLink="";
    
    
//    /**Alle, die gesprochen haben*/
//    private int alleGesprochenenWortmeldungen = 0;
//
//    /**Alle noch nicht gesprochenen Wortmeldungen in Liste*/
//    private int alleAufzurufendeWortmeldungen = 0;
//
//    /**Alle zu bearbeitenden Wortmeldungen*/
//    private int alleZuBearbeitendenWortmeldungen = 0;
//
//
//    private List<EclMitteilung> rednerListeTest = null;
//
//    private List<EclMitteilung> rednerListeTelefonie = null;
//
//    private List<EclMitteilung> rednerListeGesamt = null;;
//
//    private List<EclMitteilung> wortmeldungenZuBearbeitenDurchKoordination = null;;
//
//    private List<EclMitteilung> wortmeldungenErledigt = null;;
//


    /**Liste für select-Items bei "Status-Bearbeiten"*/
    private List<EhJsfSelectItem> bearbeitenSelectItemListe=null;

    /**Nr. der Telefon-Station*/
    private String telefonieNr="0";
    private String testNr="0";

    private String neueLfdNr="";
    
    /*===============================Websockets====================================*/
    private String jwtWsToken = "";

    /*===============================Parameter für View-Anzeige====================*/
    private int viewIdent=0;

    private EclWortmeldetischView[] wortmeldetischViewArray=null;

    /**Summen, die in der Versammlungsleiterview einzeln je Zeile angezeigt werden*/
    private List<EhJsfSummeWortmeldeView> summenVersammlungsleiterEinzeln=null;
    private List<EhJsfSummeWortmeldeView> summenVersammlungsleiterZusammen=null;

    /**Summen, die der eigentliche View sieht*/
    private List<EhJsfSummeWortmeldeView> summenView=null;
    private List<EhJsfSummeWortmeldeView> summenViewZusammen=null;

    private EclWortmeldetischView wortmeldetischView=null;
    private EclWortmeldetischView wortmeldetischViewVersammlungsleiter=null;

    private String rednerlisteVersammlungsleitung = "";
    private List<EclMitteilung> rednerlisteVersammlungsleitungAlsListe = null;
    private String infoVersammlungsleiter="";

    private ArrayList<LinkedList<EclMitteilung>> rednerlistenKoordination = null;


    public void copyFrom(BlMitteilungen blMitteilungen) {
//        alleGesprochenenWortmeldungen = blMitteilungen.alleGesprochenenWortmeldungen;
//        alleAufzurufendeWortmeldungen = blMitteilungen.alleAufzurufendeWortmeldungen;
//        alleZuBearbeitendenWortmeldungen = blMitteilungen.alleZuBearbeitendenWortmeldungen;
//        rednerListeTelefonie = blMitteilungen.rednerListeTelefonie;
//        rednerListeTest = blMitteilungen.rednerListeTest;
//        rednerListeGesamt = blMitteilungen.rednerListeGesamt;
//        wortmeldungenZuBearbeitenDurchKoordination = blMitteilungen.wortmeldungenZuBearbeitenDurchKoordination;
//        wortmeldungenErledigt = blMitteilungen.wortmeldungenErledigt;

        /**View-Anzeige*/
        summenVersammlungsleiterEinzeln = blMitteilungen.summenVersammlungsleiterEinzeln;
        summenVersammlungsleiterZusammen = blMitteilungen.summenVersammlungsleiterZusammen;
        summenView = blMitteilungen.summenView;
        summenViewZusammen = blMitteilungen.summenViewZusammen;

        wortmeldetischView = blMitteilungen.wortmeldetischView;
        wortmeldetischViewVersammlungsleiter = blMitteilungen.wortmeldetischViewVersammlungsleiter;

        rednerlisteVersammlungsleitung = blMitteilungen.rednerlisteVersammlungsleitung;
        rednerlisteVersammlungsleitungAlsListe = blMitteilungen.rednerlisteVersammlungsleitungAlsListe;
        infoVersammlungsleiter=blMitteilungen.infoVersammlungsleiter;

        rednerlistenKoordination = blMitteilungen.rednerlistenKoordination;

    }



//    public String liefereStatusText(int nr) {
//        return KonstMitteilungStatus.getTextIntern(nr);
//    }

    /*********************************Standard Getter und Setter**************************************/

    public String getRednerlisteVersammlungsleitung() {
        return rednerlisteVersammlungsleitung;
    }

    public void setRednerlisteVersammlungsleitung(String rednerlisteVersammlungsleitung) {
        this.rednerlisteVersammlungsleitung = rednerlisteVersammlungsleitung;
    }




    public String getNeuerStatus() {
        return neuerStatus;
    }

    public void setNeuerStatus(String neuerStatus) {
        this.neuerStatus = neuerStatus;
    }

    public boolean isPraesenteVorhanden() {
        return praesenteVorhanden;
    }

    public void setPraesenteVorhanden(boolean praesenteVorhanden) {
        this.praesenteVorhanden = praesenteVorhanden;
    }


    public EclMitteilung getbWortmeldung() {
        return bWortmeldung;
    }



    public void setbWortmeldung(EclMitteilung bWortmeldung) {
        this.bWortmeldung = bWortmeldung;
    }



    public String getTelefonieNr() {
        return telefonieNr;
    }



    public void setTelefonieNr(String telefonieNr) {
        this.telefonieNr = telefonieNr;
    }



    public List<EhJsfSelectItem> getBearbeitenSelectItemListe() {
        return bearbeitenSelectItemListe;
    }



    public void setBearbeitenSelectItemListe(List<EhJsfSelectItem> bearbeitenSelectItemListe) {
        this.bearbeitenSelectItemListe = bearbeitenSelectItemListe;
    }




    public String getTestNr() {
        return testNr;
    }



    public void setTestNr(String testNr) {
        this.testNr = testNr;
    }



    public String getNeueLfdNr() {
        return neueLfdNr;
    }



    public void setNeueLfdNr(String neueLfdNr) {
        this.neueLfdNr = neueLfdNr;
    }



    public int getViewIdent() {
        return viewIdent;
    }



    public void setViewIdent(int viewIdent) {
        this.viewIdent = viewIdent;
    }





    public List<EhJsfSummeWortmeldeView> getSummenVersammlungsleiterEinzeln() {
        return summenVersammlungsleiterEinzeln;
    }



    public void setSummenVersammlungsleiterEinzeln(List<EhJsfSummeWortmeldeView> summenVersammlungsleiterEinzeln) {
        this.summenVersammlungsleiterEinzeln = summenVersammlungsleiterEinzeln;
    }



    public List<EhJsfSummeWortmeldeView> getSummenVersammlungsleiterZusammen() {
        return summenVersammlungsleiterZusammen;
    }



    public void setSummenVersammlungsleiterZusammen(List<EhJsfSummeWortmeldeView> summenVersammlungsleiterZusammen) {
        this.summenVersammlungsleiterZusammen = summenVersammlungsleiterZusammen;
    }



    public List<EhJsfSummeWortmeldeView> getSummenView() {
        return summenView;
    }



    public void setSummenView(List<EhJsfSummeWortmeldeView> summenView) {
        this.summenView = summenView;
    }



    public List<EhJsfSummeWortmeldeView> getSummenViewZusammen() {
        return summenViewZusammen;
    }



    public void setSummenViewZusammen(List<EhJsfSummeWortmeldeView> summenViewZusammen) {
        this.summenViewZusammen = summenViewZusammen;
    }



    public List<EclMitteilung> getRednerlisteVersammlungsleitungAlsListe() {
        return rednerlisteVersammlungsleitungAlsListe;
    }



    public void setRednerlisteVersammlungsleitungAlsListe(List<EclMitteilung> rednerlisteVersammlungsleitungAlsListe) {
        this.rednerlisteVersammlungsleitungAlsListe = rednerlisteVersammlungsleitungAlsListe;
    }






    public EclWortmeldetischView getWortmeldetischView() {
        return wortmeldetischView;
    }



    public void setWortmeldetischView(EclWortmeldetischView wortmeldetischView) {
        this.wortmeldetischView = wortmeldetischView;
    }



    public EclWortmeldetischView getWortmeldetischViewVersammlungsleiter() {
        return wortmeldetischViewVersammlungsleiter;
    }



    public void setWortmeldetischViewVersammlungsleiter(EclWortmeldetischView wortmeldetischViewVersammlungsleiter) {
        this.wortmeldetischViewVersammlungsleiter = wortmeldetischViewVersammlungsleiter;
    }



    public ArrayList<LinkedList<EclMitteilung>> getRednerlistenKoordination() {
        return rednerlistenKoordination;
    }



    public void setRednerlistenKoordination(ArrayList<LinkedList<EclMitteilung>> rednerlistenKoordination) {
        this.rednerlistenKoordination = rednerlistenKoordination;
    }



    public EclWortmeldetischView[] getWortmeldetischViewArray() {
        return wortmeldetischViewArray;
    }



    public void setWortmeldetischViewArray(EclWortmeldetischView[] wortmeldetischViewArray) {
        this.wortmeldetischViewArray = wortmeldetischViewArray;
    }



    public int getInBearbeitung() {
        return inBearbeitung;
    }



    public void setInBearbeitung(int inBearbeitung) {
        this.inBearbeitung = inBearbeitung;
    }



    public String getAktuellerStatus() {
        return aktuellerStatus;
    }



    public void setAktuellerStatus(String aktuellerStatus) {
        this.aktuellerStatus = aktuellerStatus;
    }



    public String getInfoVersammlungsleiter() {
        return infoVersammlungsleiter;
    }



    public void setInfoVersammlungsleiter(String infoVersammlungsleiter) {
        this.infoVersammlungsleiter = infoVersammlungsleiter;
    }



    public EclInfo getbInfo() {
        return bInfo;
    }



    public void setbInfo(EclInfo bInfo) {
        this.bInfo = bInfo;
    }



    public String getJwtWsToken() {
        return jwtWsToken;
    }



    public void setJwtWsToken(String jwtWsToken) {
        this.jwtWsToken = jwtWsToken;
    }



    public String getTestraumLink() {
        return testraumLink;
    }



    public void setTestraumLink(String testraumLink) {
        this.testraumLink = testraumLink;
    }



    public String getRederaumLink() {
        return rederaumLink;
    }



    public void setRederaumLink(String rederaumLink) {
        this.rederaumLink = rederaumLink;
    }


}
