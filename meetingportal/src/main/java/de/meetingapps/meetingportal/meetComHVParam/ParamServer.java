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
package de.meetingapps.meetingportal.meetComHVParam;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclServiceDeskSet;
import de.meetingapps.meetingportal.meetComEntities.EclServiceDeskWorkflow;

/**Parameter, die Server-abhängig aber Mandantenübergreifend sind*/
public class ParamServer implements Serializable {
    private static final long serialVersionUID = 6172646564539054298L;

    /**Wird von bvReload mitgeliefert und verwendet - nicht in Datenbank. reloadParameterServer*/
    public int reloadVersionParameterServer = 0;

    /**40 Stellen
     * Soll zukünftig auch dafür dienen, zu Speichern welcher Server gerade der "Master" eines Mandanten ist.*/
    public String serverBezeichnung = "";

    /**Serverart (siehe auch KonstServerart):
     * 1 = Produktionsserver Portal (ExternerHoster)
     * 2 = Einrichtungsserver ("Office")
     * 3 = HV-Server
     * 4 = Einzel-Rechner
     */
    public int serverArt = 4;

    /**"Durchnummerierung" aller Datenbankserver.
     * Verwendung: für Mandanten-übergreifende Tables wird der Key (zukünftig) aus lfd Ident
     * und dbServerIdent bestehen. Damit können die IDs einfach je Server laufend vergeben werden,
     * sind aber zusammen mit der dbServerIdent eindeutig über alle Server hinweg. D.h.
     * Übergreifende Tables mit mandantenspezifischen Inhalten (Mails ....) können problemlos
     * mit umkopiert werden.
     *
     * Allerdings funktioniert das nur dann, wenn beim Verweis auf diese Ident auch die dbServerIdent mit gespeichert wird.
     * Dementsprechend soll diese Ident nur dort gespeichert werden, wo ein gleichzeitiges Anlegen (bzg. parallel-Halten der Daten)
     * nicht möglich ist.
     *
     * Also z.B.:
     * > Mails können nicht Serverübergreifend parallel erzeugt werden, deshalb dbServerIdent verwenden.
     * > Benutzer-Ids können jeweils - nach Anlage - auf die anderen Rechner übertragen werden, deshalb hier dbServerIdent
     *      nicht verwenden.
     */
    public int dbServerIdent=1; //tfDbServerIdent, 108


    /**Nummer des GeräteSets, das auf diesem Server gerade aktiv ist (wird beim Einlesen
     * der Geräteabhängigen Parameter verwendet*/
    public int geraeteSetIdent = 0;

    /** Praefix Link vor /meetingportal */
    public String pLocalPraefixLink = "";

    /** Alternatives Praefix vor /meetingportal, z.B. aus internem Netz */
    public String pLocalPraefixLinkAlternativ = "";

    /** Präfix Link für Subdomain z.B.  https:// (Präfix+Mandantenkürzel+Suffix=Link)*/
    /*TODO Derzeit keine Verwendung - für was war das mal gedacht?*/
    public String pSubdomainPraefixLink = "";

    /** Suffix Link für Subdomain z.B.  .aktionaersservice.de */
    /*TODO Derzeit keine Verwendung - für was war das mal gedacht?*/
    public String pSubdomainSuffixLink = ""; //tfSubdomainSuffixLink
    
    public String domainLinkErsetzen ="portal02"; //tfDomainLinkErsetzen 128
    public String domainLinkErsetzenDurch ="bo02"; //tfDomainLinkErsetzenDurch 129

    /**1 => Passwort bei dLogin wird abgefragt; 0 = nein; bei 0 wird gleichzeitig Passwort-Aging und Trivial-Passwort-Prüfung abgeschaltet*/
    public int pLocalInternPasswort = 1;

    /**1 => Passwort bei Meeting wird abgefragt; 0 = nein*/
    public int pLocalInternPasswortMeeting = 1;

    /**0 = Passwort-Aging abgeschaltet*/
    public int passwortAgingEin = 1;

    /**Bezeichnungen der Auto-Drucker. Array der Länge 101. [0] nicht verwendet.
     *
     * Werte: siehe KonstAutoDrucker
     * -1 = PDF-AUsgabe
     * 0 = Drucker abfragen
     * 1 bis 100 = vorkonfigurierter Drucker
     * */
    public String[] autoDrucker=null;

    /**Alle Portale auf diesem Server werden gesperrt
     * =1 => es ist kein Login möglich.
     * Achtung - zusätzlich auch eine Phase aktivieren, bei der keine Willenserklärungen möglich sind - wg. bereits eingeloggter Usern!
     */
    public int loginGesperrt = 0; //cbLoginGesperrt
    /**LEN max. 200*/
    public String loginGesperrtTextDeutsch = "Der Internetservice steht voraussichtlich ab 03.04.2019, 18.00 Uhr wieder zur Verfügung. Wir bitten um Ihr Verständnis."; //tfLoginGesperrtTextDeutsch
    /**LEN max. 200*/
    public String loginGesperrtTextEnglisch = "This internet service is expected to be available again from 3 April 2019, 6 p.m. We ask for your understanding."; //tfLoginGesperrtTextEnglisch

    /**Alle Wildfly-Server, die in diesem Cluster (ansprechbar) sind. Wird z.B. für Web-Sockets-Geschichten gebraucht.
     * [servernummer-1]*/
    public boolean[] verbundServerAktiv= {false, false, false, false, false}; //cbVerbundServerAktiv, Server, 112 bis 116
    /**[servernummer-1]*/
    public String[] verbundServerAdresse={"", "", "", "", ""}; //tfVerbundServerAdresse, Server, 117 bis 121

    /**Default = Produktionsserver. ws://127.0.0.1/betterportal/wortmeldungSocket=Lokaler Entwicklungsrechner */
    public String webSocketsLocalHost="ws://127.0.0.1:8080/meetingportalportal/wortmeldungSocket"; //tfWebSocketsLocalHost 122

    
    /**0 = Standard-Portaltexte können nicht gepflegt werden
     * 1 = Standard-Portaltexte können gepflegt werden
     */
    public int standardPortaltextePflegbar=1; //cbStandardPortaltextePflegbar 123
    
    public int nummernformenPflegbar=1; //cbNummernformenPflegbar 124
    public int instisPflegbar=1; //cbInstisPflegbar 125

    /**Wird vor den eigentlichen Verzeichnisnamen, also ausdrucke, ausdruckeintern etc. gesetzt.
     * Vor Namensumstellung: better
     * Nach Namensumstellung: meeting
     */
    public String praefixPfadVerzeichnisse="better"; //tfPraefixPfadVerzeichnisse 126
    
    /**Millisekunden, die eine Willenserklärungssperre maximal aufrecht erhalten wird.
     * Standardwert: 5 Minuten * 60 Sekunden * 1000 Millisekunden = 300.000
     * 
     * ToDo Parameter timeoutSperrlogik wird derzeit nicht verwendet. entweder rausschmeißen, oder dann mal einbauen ...
     */
    public long timeoutSperrlogik=300000; //tfTimeoutSperrlogik 127
    
    /**************************Fachliche Parameter, die Mandantenübergreifend gespeichert werden**********************/
    public List<EclServiceDeskWorkflow> serviceDeskWorkflowList=null;
    public List<EclServiceDeskSet> serviceDeskSetList=null;

    public List<EclServiceDeskWorkflow> liefereServiceDeskWorkflowZuSet(int pSetIdent){
        List<EclServiceDeskWorkflow> rcServiceDeskWorkflowList=new LinkedList<EclServiceDeskWorkflow>();
        for (int i=0;i<serviceDeskWorkflowList.size();i++) {
            EclServiceDeskWorkflow lServiceDeskWorkflow=serviceDeskWorkflowList.get(i);
            if (lServiceDeskWorkflow.setNr==pSetIdent) {
                rcServiceDeskWorkflowList.add(lServiceDeskWorkflow);
            }
        }

        return rcServiceDeskWorkflowList;
    }
    
    
    public String portalInitialPasswortTestBestand=""; //tfPortalInitialPasswortTestBestand
    
    
    /********************Die folgenden Parameter sind noch nicht in Datenbank**********************/
    public int anzahlScanSRVHVproBuendel = 10;

    /***************Felder nicht in Datenbank******************/
    /**Enthält eine Kopie von pLocalPraefixLinkExtern, zur Abfrage ohne Änderungsmöglichkeit
     * in XHTML
     */
    public String pLocalPraefixLinkExtern = "";

    
    public ParamServer() {
        autoDrucker=new String[101];
        for (int i=0;i<101;i++) {
            autoDrucker[i]="";
        }
    }



    public String getpLocalPraefixLink() {
        return pLocalPraefixLink;
    }


    public void setpLocalPraefixLink(String pLocalPraefixLink) {
        this.pLocalPraefixLink = pLocalPraefixLink;
    }



    public String getpLocalPraefixLinkExtern() {
        return pLocalPraefixLinkExtern;
    }



    public void setpLocalPraefixLinkExtern(String pLocalPraefixLinkExtern) {
        this.pLocalPraefixLinkExtern = pLocalPraefixLinkExtern;
    }


}
