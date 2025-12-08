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
package de.meetingapps.meetingportal.meetComBrM;

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEntities.EclAuftrag;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetTicketUebersichtRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetTicketUebersichtResult;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragArt;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragModul;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragStatusAuftragsArt;
import de.meetingapps.meetingportal.meetingportTController.PAktionaersdatenSession;
import de.meetingapps.meetingportal.meetingportTController.TPublikationenSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BrMAuftraege {

    private int logDrucken = 3;

    private @Inject EclDbM eclDbM;

    private @Inject PAktionaersdatenSession pAktionaersdatenSession;
    private @Inject TPublikationenSession pPublikationenSession;

    private @Inject BrMGenossenschaftCall brMGenossenschaftCall;

    /**Holt vom Aktienregister den Status der Aufträge ab und überträgt diesen in die lokale Datenbank*/
    public int aktualisiereLokaleAuftraege(String pAktionaersnummer, int pLoginIdent) {
        
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
//        String aktionaersnummerFuerRegister = blNummernformBasis
//                .aufbereitenKennungFuerRegisterzugriff(pAktionaersnummer);
        String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis
                .aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);
        
        EgxGetTicketUebersichtRC egxGetTicketUebersichtRC = brMGenossenschaftCall
                .doGetRequestTicketUebersicht(aktionaersnummerFuerGenossenschaftSysWebrequest);
        if (egxGetTicketUebersichtRC==null) {
            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
        }

        List<EgxGetTicketUebersichtResult>  egxGetTicketUebersichtResultList=egxGetTicketUebersichtRC.result;
        
        List<EclAuftrag> lAuftragList=null;
        eclDbM.getDbBundle().dbAuftrag.read_allBenutzer(pLoginIdent);
        if (eclDbM.getDbBundle().dbAuftrag.anzErgebnis()!=0) {
            lAuftragList=eclDbM.getDbBundle().dbAuftrag.ergebnis();
        }
        
        if (egxGetTicketUebersichtResultList!=null && lAuftragList!=null) {
            for (EgxGetTicketUebersichtResult iEgxGetTicketUebersichtResult:egxGetTicketUebersichtResultList) {
               String lSchluesselRR = iEgxGetTicketUebersichtResult.rohdaten_lfnr+";"+iEgxGetTicketUebersichtResult.prozess_id;
               int lStatusRR=/*KonstAuftragStatusAuftragsArt.ANBINDUNG_AKTIENREGISTER_AENDERN_RUECKFRAGE*/KonstAuftragStatusAuftragsArt.liefereCodeZuStatusText_Aktienregister(iEgxGetTicketUebersichtResult.status);
               if (CaBug.pruefeLog(logDrucken, 10)) {
                    System.out.println("Ticket= "+iEgxGetTicketUebersichtResult.ticket+" geaendertdatum="+iEgxGetTicketUebersichtResult.geaendertdatum+" Anlagedatum="+iEgxGetTicketUebersichtResult.anlagedatum+" erledigtdatum="+iEgxGetTicketUebersichtResult.erledigtdatum+" status="+lStatusRR+" rohdaten_lfnr="+iEgxGetTicketUebersichtResult.rohdaten_lfnr+" prozess_id="+iEgxGetTicketUebersichtResult.prozess_id);
                    System.out.println("Schlüssel: " + lSchluesselRR);
//                    String datumTest=iEgxGetTicketUebersichtResult.geaendertdatum;
//                    String datumTestZwischen=CaDatumZeit.datumJJJJ_MM_TT_HH_MM_SSzuNormal(datumTest);
//                    String datumTestDB=CaDatumZeit.DatumZeitStringFuerDatenbank(datumTestZwischen);
//                    System.out.println("datumTest="+datumTest+" datumTestZwischen="+datumTestZwischen+" datumTestDB="+datumTestDB);
                }
                
               /*Lokalen Auftrag suchen*/
               for (EclAuftrag iEclAuftrag: lAuftragList) {
                   if (iEclAuftrag.schluessel.equals(lSchluesselRR)) {
                       if (iEclAuftrag.auftragsArt!=KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_EMAIL) {
                           /*E-Mail-Tickets werden ignoriert*/
                           
                           CaBug.druckeLog("Schlüssel übereinstimmung gefunden:"+lSchluesselRR, logDrucken, 10);
                           if (lStatusRR!=iEclAuftrag.statusAuftragsArt) {
                               CaBug.druckeLog("StatusRR="+lStatusRR+" iEclAuftrag.statusAuftragsArt="+iEclAuftrag.statusAuftragsArt, logDrucken, 10);
                               iEclAuftrag.statusAuftragsArt=lStatusRR;
                               
                               String neuesDatum=CaDatumZeit.DatumZeitStringFuerDatenbank();
                               
                               String zNeuesDatum="";
                               String geaendertDatum=iEgxGetTicketUebersichtResult.geaendertdatum.trim();
                               String erledigtDatum=iEgxGetTicketUebersichtResult.erledigtdatum.trim();
                               if (!geaendertDatum.isEmpty()) {
                                   zNeuesDatum=geaendertDatum; 
                               }
                               if (!erledigtDatum.isEmpty()) {
                                   zNeuesDatum=erledigtDatum; 
                               }
                               zNeuesDatum=zNeuesDatum.trim();
                               if (!zNeuesDatum.isEmpty()) {
                                   /*JJJJ-MM-TT_HH:MM:SS????? zu TT.MM.JJJJ HH:MM:SS*/
                                   zNeuesDatum=CaDatumZeit.datumJJJJ_MM_TT_HH_MM_SSzuNormal(zNeuesDatum);
                                   /*TT.MM.JJJJ HH:MM:SS zu JJJJ.MM.TT HH:MM:SS*/
                                   zNeuesDatum=CaDatumZeit.DatumZeitStringFuerDatenbank(zNeuesDatum);
                                   
                                   neuesDatum=zNeuesDatum;
                               }
                               
                               iEclAuftrag.zeitLetzterStatuswechsel=neuesDatum;
                               iEclAuftrag.statusAuftragsArtGelesen=0;
                               iEclAuftrag.statusAuftragsArtGeloescht=0;
                               EclAuftrag[] lAuftragArray=new EclAuftrag[1];
                               lAuftragArray[0]=iEclAuftrag;
                               eclDbM.getDbBundle().dbAuftrag.update(lAuftragArray);
                           }
                           else {
                               CaBug.druckeLog("Keine Statusänderung StatusRR="+lStatusRR+" iEclAuftrag.statusAuftragsArt="+iEclAuftrag.statusAuftragsArt, logDrucken, 10);
                           }
                       }
                   }
               }
                
            }
        }
        return 1;
    }
    
    /**Setzt in PAktionaersdatenSession und TPublikationenSession  die Warnhinweise für bereits vorhandene
     * Aufträge
     */
    public int setzeOberflaechenStatusLtAuftraege(int pLoginIdent) {
        
 
        eclDbM.getDbBundle().dbAuftrag.read_aenderungenInArbeit(pLoginIdent, KonstAuftragModul.ANBINDUNG_AKTIENREGISTER);
        int anz=eclDbM.getDbBundle().dbAuftrag.anzErgebnis();
        CaBug.druckeLog("Status Aufträge eingelesen anz="+anz, logDrucken, 10);
        for (int i=0;i<anz;i++) {
            EclAuftrag lAuftrag=eclDbM.getDbBundle().dbAuftrag.ergebnisPosition(i);
            if (KonstAuftragStatusAuftragsArt.aktienregisterInArbeit(lAuftrag.statusAuftragsArt)) {
                switch (lAuftrag.auftragsArt) {
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_ANSCHRIFT:
                    pAktionaersdatenSession.setAktionaersdatenAenderungInArbeit(true);
                    break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON1:
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON2:
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON3:
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON4:
                                         pAktionaersdatenSession.setTelefonAenderungInArbeit(true);
                   break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_EMAIL:
                    pAktionaersdatenSession.setEmailAenderungInArbeit(true);
                    pAktionaersdatenSession.setEmailAenderungVorhanden(true);
                 break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_GEBURTSDATUM:
                    pAktionaersdatenSession.setGeburtsdatumAenderungInArbeit(true);
                   break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_STEUERID:
                    pAktionaersdatenSession.setSteuerIdAenderungInArbeit(true);
                    break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_BANKVERBINDUNG:
                    pAktionaersdatenSession.setBankverbindungAenderungInArbeit(true);
                    break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_NEWSLETTER:
                    pPublikationenSession.setAenderungenInArbeit(true);
                    break;
                }
            }
            
            if (KonstAuftragStatusAuftragsArt.aktienregisterFehlerhaft(lAuftrag.statusAuftragsArt)) {
                switch (lAuftrag.auftragsArt) {
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_ANSCHRIFT:
                    pAktionaersdatenSession.setAktionaersdatenAenderungFehlerhaft(true);
                    break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON1:
                    pAktionaersdatenSession.setTelefon1AenderungFehlerhaft(true);
                    break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON2:
                    pAktionaersdatenSession.setTelefon2AenderungFehlerhaft(true);
                    break;
               case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON3:
                    pAktionaersdatenSession.setTelefon3AenderungFehlerhaft(true);
                    break;
               case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON4:
                   pAktionaersdatenSession.setTelefon4AenderungFehlerhaft(true);
                   break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_EMAIL:
                    pAktionaersdatenSession.setEmailAenderungFehlerhaft(true);
                    pAktionaersdatenSession.setEmailAenderungVorhanden(true);
                 break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_GEBURTSDATUM:
                    pAktionaersdatenSession.setGeburtsdatumAenderungFehlerhaft(true);
                   break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_STEUERID:
                    pAktionaersdatenSession.setSteuerIdAenderungFehlerhaft(true);
                    break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_BANKVERBINDUNG:
                    pAktionaersdatenSession.setBankverbindungAenderungFehlerhaft(true);
                    break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_NEWSLETTER:
                    pPublikationenSession.setAenderungenFehlerhaft(true);
                    break;
                }
            }
            
            /**Wenn eine "positiv erledigte" Aktion (=erledigt) vorliegt, dann werden ggf. frühere "Abgelehnte" und
             * "Rückfragen" nicht mehr mit Warnstatus bzw. in Arbeit angezeigt
             */
            if (KonstAuftragStatusAuftragsArt.aktienregisterPositivErledigt(lAuftrag.statusAuftragsArt)) {
                switch (lAuftrag.auftragsArt) {
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_ANSCHRIFT:
                    pAktionaersdatenSession.setAktionaersdatenAenderungFehlerhaft(false);
                   break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON1:
                    pAktionaersdatenSession.setTelefon1AenderungFehlerhaft(false);
                   break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON2:
                    pAktionaersdatenSession.setTelefon2AenderungFehlerhaft(false);
                   break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON3:
                    pAktionaersdatenSession.setTelefon3AenderungFehlerhaft(false);
                   break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON4:
                    pAktionaersdatenSession.setTelefon4AenderungFehlerhaft(false);
                   break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_EMAIL:
                   pAktionaersdatenSession.setEmailAenderungVorhanden(false);
                   pAktionaersdatenSession.setEmailAenderungFehlerhaft(false);
                  break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_GEBURTSDATUM:
                    pAktionaersdatenSession.setGeburtsdatumAenderungFehlerhaft(false);
                   break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_STEUERID:
                    pAktionaersdatenSession.setSteuerIdAenderungFehlerhaft(false);
                    break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_BANKVERBINDUNG:
                    pAktionaersdatenSession.setBankverbindungAenderungFehlerhaft(false);
                    break;
                case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_NEWSLETTER:
                    pPublikationenSession.setAenderungenFehlerhaft(false);
                    break;
                }
            }

        }
        
//        pAktionaersdatenSession.setEmailAenderungFehlerhaft(true); //==============================================================Nur zu Testzwecken

        
        
        return 1;
    }
}
