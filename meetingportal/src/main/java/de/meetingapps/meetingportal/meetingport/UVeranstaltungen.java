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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBl.BlVeranstaltungen;
import de.meetingapps.meetingportal.meetComBlManaged.BlMFuelleEclMAusPufferOderDBEE;
import de.meetingapps.meetingportal.meetComBlManaged.BlMPuffer;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclVeranstaltungenM;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UVeranstaltungen {

    private int logDrucken=10;
    
    private @Inject UVeranstaltungenSession uVeranstaltungenSession;
    private @Inject EclVeranstaltungenM eclVeranstaltungenM;

    private @Inject BlMPuffer blMPuffer;
    private @Inject BlMFuelleEclMAusPufferOderDBEE blMFuelleEclMAusPufferOderDBEE;
    
    private @Inject EclParamM eclParamM;
    private @Inject EclDbM eclDbM;

    public void simulationInit(boolean pBereitsOffen) {

        /**Nur zum leichteren Testen. Kann aber möglicherweise als Blaupause fürs Parameter-RÜcksetzen verwendet werden!
         * Problem dürfte allerdings sein: möglicherweise fliegen gerade parallel-aktive User raus :-(
         * Deshalb sinnvoller: Parameter raus / rein*/
        CaBug.druckeLog("001", logDrucken, 10);
        blMPuffer.clearAllPuffer(); 
        CaBug.druckeLog("002", logDrucken, 10);
        eclDbM.openAllOhneParameterCheck();
        CaBug.druckeLog("003", logDrucken, 10);
        blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();
        CaBug.druckeLog("004", logDrucken, 10);
       eclDbM.closeAll();
       CaBug.druckeLog("005", logDrucken, 10);
       
        if (pBereitsOffen==false) {
            eclDbM.openAll();
            eclDbM.openWeitere();
            
        }

        BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
        blVeranstaltungen.defaultWerte=new String[3];
        blVeranstaltungen.defaultWerte[2]="n@n.n";
        
        /**ToDo Veranstaltungen - Menünummer dann noch ergänzen*/
        blVeranstaltungen.erzeugeVeranstaltungslisteFuerTeilnehmer(BlVeranstaltungen.LAUT_MENUE_NUMMER, 1); 
        
//        blVeranstaltungen.belegeVeranstaltungslisteMitElementeZumAnzeigenFuerTeilnehmer();
        
        eclVeranstaltungenM.setVeranstaltungenListe(blVeranstaltungen.rcVeranstaltungenListe);
        
        
        blVeranstaltungen.belegeWerteVeranstaltungsliste();
        
        if (pBereitsOffen==false) {
            eclDbM.closeAll();
        }

    }
    
    public String doReloadSimulation() {
        simulationInit(false);
        return "";
        
    }
    
    /**Leer-Funktion- dient nur dazu, Eingabefelder zu übernehmen*/
    public String doRefreshSimulation() {
        eclVeranstaltungenM.refreshStatusAenderung();
        return "";
    }

    
    private boolean zeigeFehlermeldungAn=false;
    private String fehlerText="";
    private boolean zeigeQuittungAn=false;
    
    public String doWeiterZuQuittung() {
        zeigeFehlermeldungAn=false;
        fehlerText="";
        zeigeQuittungAn=false;


        boolean brc=false;

        /*Veranstaltungsliste durchlaufen:
         * > inLetzterVerarbeitungEnthalten setzen
         * > Eingabewerte überprüfen 
         */
        eclDbM.openAll();
        
        BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
        blVeranstaltungen.rcVeranstaltungenListe=eclVeranstaltungenM.getVeranstaltungenListe();
        
        brc=blVeranstaltungen.pruefeVeranstaltungsliste(true);
        if (brc==true) {
            blVeranstaltungen.aktionenAusfuehren();
            blVeranstaltungen.speichereWerteVeranstaltungsliste();
            blVeranstaltungen.aufbereitenQuittung();
            zeigeQuittungAn=true;
            
        }
        else { /*Fehler aufgetreten*/
            zeigeFehlermeldungAn=true;
            fehlerText=blVeranstaltungen.rcFehlerText;
        }
        
        eclDbM.closeAll();
        
        CaBug.druckeLog("zeigeFehlermeldungAn="+zeigeFehlermeldungAn, logDrucken, 10);
        CaBug.druckeLog("fehlerText="+fehlerText, logDrucken, 10);
        uVeranstaltungenSession.setZeigeFehlermeldungAn(zeigeFehlermeldungAn);
        uVeranstaltungenSession.setFehlerText(fehlerText);
        uVeranstaltungenSession.setZeigeQuittungAn(zeigeQuittungAn);
        
        eclVeranstaltungenM.setVeranstaltungenQuittungListe(blVeranstaltungen.rcVeranstaltungenQuittungListe);
        
        return "";
    }
    
   
   
    

   

}
