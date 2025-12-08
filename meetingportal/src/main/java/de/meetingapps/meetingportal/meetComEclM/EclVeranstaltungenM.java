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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBl.BlVeranstaltungen;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenElement;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenQuittungElement;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenVeranstaltung;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named


public class EclVeranstaltungenM implements Serializable {
    private static final long serialVersionUID = 1682191317127335513L;

    private int logDrucken=10;
    
    
    private boolean zeigeInterneInfo=false;

    private boolean anmeldungDurchfuehren=false;
    
    private List<EclVeranstaltungenVeranstaltung> veranstaltungenListe=null;
    
    public boolean pruefeObVeranstaltungenVorhanden() {
        return (veranstaltungenListe!=null && veranstaltungenListe.size()>0);
    }
   
    private boolean weiterButtonAktiv=false;
    private String weiterButtonBeschriftung="";
    
    private List<EclVeranstaltungenQuittungElement> veranstaltungenQuittungListe=new LinkedList<EclVeranstaltungenQuittungElement>();

    
    public void clear() {
        zeigeInterneInfo=false;
        veranstaltungenListe=null;
        weiterButtonAktiv=false;
        weiterButtonBeschriftung="";
        veranstaltungenQuittungListe=new LinkedList<EclVeranstaltungenQuittungElement>();
        anmeldungDurchfuehren=false;
    }
    
    
    public void copyFromBlVeranstaltungen(BlVeranstaltungen pBlVeranstaltungen) {
        veranstaltungenListe=pBlVeranstaltungen.rcVeranstaltungenListe;
        weiterButtonAktiv=pBlVeranstaltungen.rcWeiterButtonAktiv;
        weiterButtonBeschriftung=pBlVeranstaltungen.rcWeiterButtonBeschriftung;
    }
    
    public void refreshStatusAenderung() {
        weiterButtonAktiv=false;
        for (EclVeranstaltungenVeranstaltung iVeranstaltung:veranstaltungenListe) {
            if (iVeranstaltung.veranstaltungenElementListe!=null) {
                for (EclVeranstaltungenElement iVeranstaltungenElement:iVeranstaltung.veranstaltungenElementListe) {
                    if (iVeranstaltungenElement.elementTyp==1001) {
                        if (iVeranstaltungenElement.wirdVerwendetWennVorgaengerAktivierungsStatusGleich==0 
                                || iVeranstaltungenElement.wirdVerwendetWennVorgaengerAktivierungsStatusGleich==iVeranstaltung.aktivierungsStatus) {
                            weiterButtonAktiv=true;
                            weiterButtonBeschriftung=iVeranstaltungenElement.textExtern;
                        }
                    }
                    
                }
            }
            
        }
        
    }
    /***********************Standard getter und setter***************************************/
    public List<EclVeranstaltungenVeranstaltung> getVeranstaltungenListe() {
        return veranstaltungenListe;
    }

    public void setVeranstaltungenListe(List<EclVeranstaltungenVeranstaltung> veranstaltungenListe) {
        this.veranstaltungenListe = veranstaltungenListe;
    }

    public boolean isWeiterButtonAktiv() {
        return weiterButtonAktiv;
    }

    public void setWeiterButtonAktiv(boolean weiterButtonAktiv) {
        this.weiterButtonAktiv = weiterButtonAktiv;
    }

    public String getWeiterButtonBeschriftung() {
        return weiterButtonBeschriftung;
    }

    public void setWeiterButtonBeschriftung(String weiterButtonBeschriftung) {
        this.weiterButtonBeschriftung = weiterButtonBeschriftung;
    }

    public List<EclVeranstaltungenQuittungElement> getVeranstaltungenQuittungListe() {
        CaBug.druckeLog("veranstaltungenQuittungListe länge="+veranstaltungenQuittungListe.size(), logDrucken, 10);
        return veranstaltungenQuittungListe;
    }

    public void setVeranstaltungenQuittungListe(List<EclVeranstaltungenQuittungElement> veranstaltungenQuittungListe) {
        this.veranstaltungenQuittungListe = veranstaltungenQuittungListe;
    }

    public boolean isZeigeInterneInfo() {
        return zeigeInterneInfo;
    }

    public void setZeigeInterneInfo(boolean zeigeInterneInfo) {
        this.zeigeInterneInfo = zeigeInterneInfo;
    }


    public boolean isAnmeldungDurchfuehren() {
        return anmeldungDurchfuehren;
    }


    public void setAnmeldungDurchfuehren(boolean anmeldungDurchfuehren) {
        this.anmeldungDurchfuehren = anmeldungDurchfuehren;
    }


 


}
