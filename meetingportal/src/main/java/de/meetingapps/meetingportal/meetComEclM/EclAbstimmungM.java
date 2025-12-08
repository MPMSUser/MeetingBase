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
import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungku310;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

/**
 * @author N.N
 *
 */
@SessionScoped
@Named
public class EclAbstimmungM implements Serializable {
    private static final long serialVersionUID = -1783898382339870569L;

//    private int logDrucken=10;
    
    /*Achtung - bildet EclAbstimmung nicht vollständig ab!*/

    private int ident = 0;

    private String nummer = "";
    private String nummerEN = "";
    private String nummerindex = "";
    private String nummerindexEN = "";
    private boolean nummerUnterdruecken=false;
    private String anzeigeBezeichnungKurz = "";
    private String anzeigeBezeichnungKurzEN = "";
    private String anzeigeBezeichnungLang = "";
    private String anzeigeBezeichnungLangEN = "";

    private int externJa = 0;
    private int externNein = 0;
    private int externEnthaltung = 0;
    private int externUngueltig = 0;
    private int externNichtTeilnahme = 0;
//    private int externSonstiges1 = 0;
//    private int externSonstiges2 = 0;
//    private int externSonstiges3 = 0;
    private int externLoeschen = 0;
//    private int externFrei = 0;
//    private String externFreiText = "";

//    /** elStiBlo*/
//    private int elStiBloJa = 0;
//    private int elStiBloNein = 0;
//    private int elStiBloEnthaltung = 0;
//    private int elStiBloUngueltig = 0;
//    private int elStiBloNichtTeilnahme = 0;
//    private int elStiBloSonstiges1 = 0;
//    private int elStiBloSonstiges2 = 0;
//    private int elStiBloSonstiges3 = 0;
//    /**elStiBloLoeschen ist nicht als eigenständiger Abstimmungspunkt, sondern für Portal als "Anzeige eines Lösch-Buttons" gedacht*/
//    private int elStiBloLoeschen = 0;
//    /**elStiBlo frei = keine Standardstimmart, sondern freiText wird als Stimmart angezeigt*/
//    private int elStiBloFrei = 0;
//    private String elStiBloFreiText = "";

//    /**tablet*/
//    private int tabletJa = 0;
//    private int tabletNein = 0;
//    private int tabletEnthaltung = 0;
//    private int tabletUngueltig = 0;
//    private int tabletNichtTeilnahme = 0;
//    private int tabletSonstiges1 = 0;
//    private int tabletSonstiges2 = 0;
//    private int tabletSonstiges3 = 0;
//    /**elStiBloLoeschen ist nicht als eigenständiger Abstimmungspunkt, sondern für Portal als "Anzeige eines Lösch-Buttons" gedacht*/
//    private int tabletLoeschen = 0;
//    /**elStiBlo frei = keine Standardstimmart, sondern freiText wird als Stimmart angezeigt*/
//    private int tabletFrei = 0;
//    private String tabletFreiText = "";

    private boolean ueberschrift = false;
    private int identWeisungssatz = 0;
    private int gegenantrag = 0;

    /**stimmberechtigte Gattungen*/
    private int[] stimmberechtigteGattungen = { 0, 0, 0, 0, 0 };

    private int ergaenzungsantrag = 0;

    /**Abstimmung gehört zur Abstimmungsgruppe (z.B.: Wahl)*/
    private int zuAbstimmungsgruppe = 0;

    /**Verbindung zu Buttons
     * J, N, E.
     * leer, null oder blank = nichtmarkiert.
     * Wird in getGewaehlt entsprechend umgesetzt auf ' '*/
    private String gewaehlt = "";

    /**Abstimmungsvorschlag - für KIAV aktuell */
    private String abstimmungsvorschlag = "";

    /**Abstimmungsvorschlag der Gesellschaft*/
    private String abstimmungsvorschlagGesellschaft = "";

    /**Für Gegenanträge, sowie für Fragen/Widersprüche etc.*/
    private boolean markiert = false;

    private List<EclAbstimmungku310> abstimmungku310List=null;
    
    public boolean pruefeObAbstimmungku310Gefuellt() {
        if (abstimmungku310List==null) {return false;}
        if (abstimmungku310List.size()==0) {return false;}
        return true;
    }
    
    public void copyFrom(EclAbstimmung pAbstimmung) {
        ident = pAbstimmung.ident;
        nummer = pAbstimmung.nummer;
        nummerEN = pAbstimmung.nummerEN;
        nummerindex = pAbstimmung.nummerindex;
        nummerindexEN = pAbstimmung.nummerindexEN;
        nummerUnterdruecken=(pAbstimmung.nummerUnterdruecken==1);
        
        /*TODO Portal Agenda: im Portal wird nun immer nur die Langebezeichnung zur Verfügung gestellt*/
        //		anzeigeBezeichnungKurz=pAbstimmung.liefereAnzeigeBezeichnungKurz();
        //		anzeigeBezeichnungKurzEN=pAbstimmung.liefereAnzeigeBezeichnungKurzEN();
        anzeigeBezeichnungKurz = pAbstimmung.anzeigeBezeichnungKurz;
        if (anzeigeBezeichnungKurz.isEmpty()) {
            anzeigeBezeichnungKurz = pAbstimmung.anzeigeBezeichnungLang; 
        }   
        anzeigeBezeichnungKurzEN = pAbstimmung.anzeigeBezeichnungKurzEN;
        if (anzeigeBezeichnungKurzEN.isEmpty()) {
            anzeigeBezeichnungKurzEN = pAbstimmung.anzeigeBezeichnungLangEN; 
        }   
        anzeigeBezeichnungLang = pAbstimmung.anzeigeBezeichnungLang;
        anzeigeBezeichnungLangEN = pAbstimmung.anzeigeBezeichnungLangEN;
        externJa = pAbstimmung.externJa;
        externNein = pAbstimmung.externNein;
        externEnthaltung = pAbstimmung.externEnthaltung;
        externUngueltig = pAbstimmung.externUngueltig;
        externNichtTeilnahme = pAbstimmung.externNichtTeilnahme;
//        externSonstiges1 = pAbstimmung.externSonstiges1;
//        externSonstiges2 = pAbstimmung.externSonstiges2;
//        externSonstiges3 = pAbstimmung.externSonstiges3;
        externLoeschen = pAbstimmung.externLoeschen;
//        externFrei = pAbstimmung.externFrei;
//        externFreiText = pAbstimmung.externFreiText;

//        elStiBloJa = pAbstimmung.elStiBloJa;
//        elStiBloNein = pAbstimmung.elStiBloNein;
//        elStiBloEnthaltung = pAbstimmung.elStiBloEnthaltung;
//        elStiBloUngueltig = pAbstimmung.elStiBloUngueltig;
//        elStiBloNichtTeilnahme = pAbstimmung.elStiBloNichtTeilnahme;
//        elStiBloSonstiges1 = pAbstimmung.elStiBloSonstiges1;
//        elStiBloSonstiges2 = pAbstimmung.elStiBloSonstiges2;
//        elStiBloSonstiges3 = pAbstimmung.elStiBloSonstiges3;
//        elStiBloLoeschen = pAbstimmung.elStiBloLoeschen;
//        elStiBloFrei = pAbstimmung.elStiBloFrei;
//        elStiBloFreiText = pAbstimmung.elStiBloFreiText;

//        tabletJa = pAbstimmung.tabletJa;
//        tabletNein = pAbstimmung.tabletNein;
//        tabletEnthaltung = pAbstimmung.tabletEnthaltung;
//        tabletUngueltig = pAbstimmung.tabletUngueltig;
//        tabletNichtTeilnahme = pAbstimmung.tabletNichtTeilnahme;
//        tabletSonstiges1 = pAbstimmung.tabletSonstiges1;
//        tabletSonstiges2 = pAbstimmung.tabletSonstiges2;
//        tabletSonstiges3 = pAbstimmung.tabletSonstiges3;
//        tabletLoeschen = pAbstimmung.tabletLoeschen;
//        tabletFrei = pAbstimmung.tabletFrei;
//        tabletFreiText = pAbstimmung.tabletFreiText;

        if (pAbstimmung.identWeisungssatz == -1) {
            ueberschrift = true;
        } else {
            ueberschrift = false;
        }
        identWeisungssatz = pAbstimmung.identWeisungssatz;
        gegenantrag = pAbstimmung.gegenantrag;
        for (int i = 0; i < 5; i++) {
            stimmberechtigteGattungen[i] = pAbstimmung.stimmberechtigteGattungen[i];
        }
        ergaenzungsantrag = pAbstimmung.ergaenzungsantrag;
        zuAbstimmungsgruppe = pAbstimmung.zuAbstimmungsgruppe;
        gewaehlt = "";
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getAnzeigeBezeichnungKurz() {
        return anzeigeBezeichnungKurz;
    }

    public void setAnzeigeBezeichnungKurz(String anzeigeBezeichnungKurz) {
        this.anzeigeBezeichnungKurz = anzeigeBezeichnungKurz;
    }

    public String getAnzeigeBezeichnungLang() {
        return anzeigeBezeichnungLang;
    }

    public void setAnzeigeBezeichnungLang(String anzeigeBezeichnungLang) {
        this.anzeigeBezeichnungLang = anzeigeBezeichnungLang;
    }

    public int getExternJa() {
        return externJa;
    }

    public void setExternJa(int externJa) {
        this.externJa = externJa;
    }

    public int getExternNein() {
        return externNein;
    }

    public void setExternNein(int externNein) {
        this.externNein = externNein;
    }

    public int getExternEnthaltung() {
        return externEnthaltung;
    }

    public void setExternEnthaltung(int externEnthaltung) {
        this.externEnthaltung = externEnthaltung;
    }

    public int getExternUngueltig() {
        return externUngueltig;
    }

    public void setExternUngueltig(int externUngueltig) {
        this.externUngueltig = externUngueltig;
    }

    public int getExternNichtTeilnahme() {
        return externNichtTeilnahme;
    }

    public void setExternNichtTeilnahme(int externNichtTeilnahme) {
        this.externNichtTeilnahme = externNichtTeilnahme;
    }

//    public int getExternSonstiges1() {
//        return externSonstiges1;
//    }
//
//    public void setExternSonstiges1(int externSonstiges1) {
//        this.externSonstiges1 = externSonstiges1;
//    }
//
//    public int getExternSonstiges2() {
//        return externSonstiges2;
//    }
//
//    public void setExternSonstiges2(int externSonstiges2) {
//        this.externSonstiges2 = externSonstiges2;
//    }
//
//    public int getExternSonstiges3() {
//        return externSonstiges3;
//    }
//
//    public void setExternSonstiges3(int externSonstiges3) {
//        this.externSonstiges3 = externSonstiges3;
//    }

    public int getExternLoeschen() {
        return externLoeschen;
    }

    public void setExternLoeschen(int externLoeschen) {
        this.externLoeschen = externLoeschen;
    }

//    public int getExternFrei() {
//        return externFrei;
//    }
//
//    public void setExternFrei(int externFrei) {
//        this.externFrei = externFrei;
//    }
//
//    public String getExternFreiText() {
//        return externFreiText;
//    }
//
//    public void setExternFreiText(String externFreiText) {
//        this.externFreiText = externFreiText;
//    }

    public boolean isUeberschrift() {
        return ueberschrift;
    }

    public void setUeberschrift(boolean ueberschrift) {
        this.ueberschrift = ueberschrift;
    }

    public int getIdentWeisungssatz() {
        return identWeisungssatz;
    }

    public void setIdentWeisungssatz(int identWeisungssatz) {
        this.identWeisungssatz = identWeisungssatz;
    }

    public int getGegenantrag() {
        return gegenantrag;
    }

    public void setGegenantrag(int gegenantrag) {
        this.gegenantrag = gegenantrag;
    }

    public int getErgaenzungsantrag() {
        return ergaenzungsantrag;
    }

    public void setErgaenzungsantrag(int ergaenzungsantrag) {
        this.ergaenzungsantrag = ergaenzungsantrag;
    }

    public boolean isMarkiert() {
        return markiert;
    }

    public void setMarkiert(boolean markiert) {
        this.markiert = markiert;
    }

    public String getGewaehlt() {
        
        if (gewaehlt==null) {
//            CaBug.druckeLog("gewaehlt==null", logDrucken, 10);
            return " ";
        }
        if (gewaehlt.isEmpty()) {
//            CaBug.druckeLog("gewaehlt==leer", logDrucken, 10);
            return " ";
        }
//        CaBug.druckeLog("**", logDrucken, 10);
        return gewaehlt;
    }

    public void setGewaehlt(String gewaehlt) {
        this.gewaehlt = gewaehlt;
    }

    public String getAbstimmungsvorschlag() {
        return abstimmungsvorschlag;
    }

    public void setAbstimmungsvorschlag(String abstimmungsvorschlag) {
        this.abstimmungsvorschlag = abstimmungsvorschlag;
    }

    public String getAbstimmungsvorschlagGesellschaft() {
        return abstimmungsvorschlagGesellschaft;
    }

    public void setAbstimmungsvorschlagGesellschaft(String abstimmungsvorschlagGesellschaft) {
        this.abstimmungsvorschlagGesellschaft = abstimmungsvorschlagGesellschaft;
    }

    public String getNummer() {
        return nummer;
    }

    public void setNummer(String nummer) {
        this.nummer = nummer;
    }

    public String getNummerEN() {
        return nummerEN;
    }

    public void setNummerEN(String nummerEN) {
        this.nummerEN = nummerEN;
    }

    public String getAnzeigeBezeichnungKurzEN() {
        return anzeigeBezeichnungKurzEN;
    }

    public void setAnzeigeBezeichnungKurzEN(String anzeigeBezeichnungKurzEN) {
        this.anzeigeBezeichnungKurzEN = anzeigeBezeichnungKurzEN;
    }

    public String getAnzeigeBezeichnungLangEN() {
        return anzeigeBezeichnungLangEN;
    }

    public void setAnzeigeBezeichnungLangEN(String anzeigeBezeichnungLangEN) {
        this.anzeigeBezeichnungLangEN = anzeigeBezeichnungLangEN;
    }

    public String getNummerindex() {
        return nummerindex;
    }

    public void setNummerindex(String nummerindex) {
        this.nummerindex = nummerindex;
    }

    public String getNummerindexEN() {
        return nummerindexEN;
    }

    public void setNummerindexEN(String nummerindexEN) {
        this.nummerindexEN = nummerindexEN;
    }

//    public int getElStiBloJa() {
//        return elStiBloJa;
//    }
//
//    public void setElStiBloJa(int elStiBloJa) {
//        this.elStiBloJa = elStiBloJa;
//    }
//
//    public int getElStiBloNein() {
//        return elStiBloNein;
//    }
//
//    public void setElStiBloNein(int elStiBloNein) {
//        this.elStiBloNein = elStiBloNein;
//    }
//
//    public int getElStiBloEnthaltung() {
//        return elStiBloEnthaltung;
//    }
//
//    public void setElStiBloEnthaltung(int elStiBloEnthaltung) {
//        this.elStiBloEnthaltung = elStiBloEnthaltung;
//    }
//
//    public int getElStiBloUngueltig() {
//        return elStiBloUngueltig;
//    }
//
//    public void setElStiBloUngueltig(int elStiBloUngueltig) {
//        this.elStiBloUngueltig = elStiBloUngueltig;
//    }
//
//    public int getElStiBloNichtTeilnahme() {
//        return elStiBloNichtTeilnahme;
//    }
//
//    public void setElStiBloNichtTeilnahme(int elStiBloNichtTeilnahme) {
//        this.elStiBloNichtTeilnahme = elStiBloNichtTeilnahme;
//    }
//
//    public int getElStiBloSonstiges1() {
//        return elStiBloSonstiges1;
//    }
//
//    public void setElStiBloSonstiges1(int elStiBloSonstiges1) {
//        this.elStiBloSonstiges1 = elStiBloSonstiges1;
//    }
//
//    public int getElStiBloSonstiges2() {
//        return elStiBloSonstiges2;
//    }
//
//    public void setElStiBloSonstiges2(int elStiBloSonstiges2) {
//        this.elStiBloSonstiges2 = elStiBloSonstiges2;
//    }
//
//    public int getElStiBloSonstiges3() {
//        return elStiBloSonstiges3;
//    }
//
//    public void setElStiBloSonstiges3(int elStiBloSonstiges3) {
//        this.elStiBloSonstiges3 = elStiBloSonstiges3;
//    }
//
//    public int getElStiBloLoeschen() {
//        return elStiBloLoeschen;
//    }
//
//    public void setElStiBloLoeschen(int elStiBloLoeschen) {
//        this.elStiBloLoeschen = elStiBloLoeschen;
//    }
//
//    public int getElStiBloFrei() {
//        return elStiBloFrei;
//    }
//
//    public void setElStiBloFrei(int elStiBloFrei) {
//        this.elStiBloFrei = elStiBloFrei;
//    }
//
//    public String getElStiBloFreiText() {
//        return elStiBloFreiText;
//    }
//
//    public void setElStiBloFreiText(String elStiBloFreiText) {
//        this.elStiBloFreiText = elStiBloFreiText;
//    }

//    public int getTabletJa() {
//        return tabletJa;
//    }
//
//    public void setTabletJa(int tabletJa) {
//        this.tabletJa = tabletJa;
//    }
//
//    public int getTabletNein() {
//        return tabletNein;
//    }
//
//    public void setTabletNein(int tabletNein) {
//        this.tabletNein = tabletNein;
//    }
//
//    public int getTabletEnthaltung() {
//        return tabletEnthaltung;
//    }
//
//    public void setTabletEnthaltung(int tabletEnthaltung) {
//        this.tabletEnthaltung = tabletEnthaltung;
//    }
//
//    public int getTabletUngueltig() {
//        return tabletUngueltig;
//    }
//
//    public void setTabletUngueltig(int tabletUngueltig) {
//        this.tabletUngueltig = tabletUngueltig;
//    }
//
//    public int getTabletNichtTeilnahme() {
//        return tabletNichtTeilnahme;
//    }
//
//    public void setTabletNichtTeilnahme(int tabletNichtTeilnahme) {
//        this.tabletNichtTeilnahme = tabletNichtTeilnahme;
//    }
//
//    public int getTabletSonstiges1() {
//        return tabletSonstiges1;
//    }
//
//    public void setTabletSonstiges1(int tabletSonstiges1) {
//        this.tabletSonstiges1 = tabletSonstiges1;
//    }
//
//    public int getTabletSonstiges2() {
//        return tabletSonstiges2;
//    }
//
//    public void setTabletSonstiges2(int tabletSonstiges2) {
//        this.tabletSonstiges2 = tabletSonstiges2;
//    }
//
//    public int getTabletSonstiges3() {
//        return tabletSonstiges3;
//    }
//
//    public void setTabletSonstiges3(int tabletSonstiges3) {
//        this.tabletSonstiges3 = tabletSonstiges3;
//    }
//
//    public int getTabletLoeschen() {
//        return tabletLoeschen;
//    }
//
//    public void setTabletLoeschen(int tabletLoeschen) {
//        this.tabletLoeschen = tabletLoeschen;
//    }
//
//    public int getTabletFrei() {
//        return tabletFrei;
//    }
//
//    public void setTabletFrei(int tabletFrei) {
//        this.tabletFrei = tabletFrei;
//    }
//
//    public String getTabletFreiText() {
//        return tabletFreiText;
//    }
//
//    public void setTabletFreiText(String tabletFreiText) {
//        this.tabletFreiText = tabletFreiText;
//    }

    public int[] getStimmberechtigteGattungen() {
        return stimmberechtigteGattungen;
    }

    public void setStimmberechtigteGattungen(int[] stimmberechtigteGattungen) {
        this.stimmberechtigteGattungen = stimmberechtigteGattungen;
    }

    public int getZuAbstimmungsgruppe() {
        return zuAbstimmungsgruppe;
    }

    public void setZuAbstimmungsgruppe(int zuAbstimmungsgruppe) {
        this.zuAbstimmungsgruppe = zuAbstimmungsgruppe;
    }

    public boolean isNummerUnterdruecken() {
        return nummerUnterdruecken;
    }

    public void setNummerUnterdruecken(boolean nummerUnterdruecken) {
        this.nummerUnterdruecken = nummerUnterdruecken;
    }

    public List<EclAbstimmungku310> getAbstimmungku310List() {
        return abstimmungku310List;
    }

    public void setAbstimmungku310List(List<EclAbstimmungku310> abstimmungku310List) {
        this.abstimmungku310List = abstimmungku310List;
    }

}
