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
package de.meetingapps.meetingportal.meetComBl;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungZuSammelkarte;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;

/**Für ku302_303 entwickelt. Gibt keine Oberfläche dafür!*/
public class BlWIllenserklaerungStapel {

    private DbBundle lDbBundle=null;
    private int logDrucken=10;
    
    public BlWIllenserklaerungStapel(DbBundle pDbBundle) {
        lDbBundle=pDbBundle;
    }
    
    public void widerrufeInaktiveVollmachtenWeisungen(){
    
        lDbBundle.dbWeisungMeldung.leseInaktive();
        int anz = lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden();
//        if (anz>10) {
//            anz=10;
//        }
        EclWeisungMeldung[] lWeisungMeldungArray = lDbBundle.dbWeisungMeldung.weisungMeldungArray;
        for (int i = 0; i < anz; i++) {
            CaBug.druckeLog("Inaktive Weisung "+i+" von "+anz, logDrucken, 10);
 
            int meldeIdent = lWeisungMeldungArray[i].meldungsIdent;
            lDbBundle.dbMeldungen.leseZuIdent(meldeIdent);
            EclMeldung lMeldung=lDbBundle.dbMeldungen.meldungenArray[0];
 
            
            lDbBundle.dbMeldungZuSammelkarte.leseZuMeldung(lMeldung);
            for (int i1=0;i1<lDbBundle.dbMeldungZuSammelkarte.anzMeldungZuSammelkarteGefunden();i1++) {
                CaBug.druckeLog("i1="+i1+" "+lMeldung.aktionaersnummer, logDrucken, 10);
                EclMeldungZuSammelkarte lMeldungZuSammelkarte=lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteArray[i1];

                if (lMeldungZuSammelkarte.aktiv==0) {
                    CaBug.druckeLog("Inaktiv", logDrucken, 10);

                    lDbBundle.dbWillenserklaerung.leseZuIdent(lMeldungZuSammelkarte.willenserklaerungIdent);
                    EclWillenserklaerung lWillenserklaerung=lDbBundle.dbWillenserklaerung.willenserklaerungArray[0];
                    int lWillenserklaerungArt=lWillenserklaerung.willenserklaerung;

                    BlWillenserklaerung blWillenserklaerung=new BlWillenserklaerung();
                    blWillenserklaerung.pEclMeldungZuSammelkarte=lMeldungZuSammelkarte;
                    blWillenserklaerung.piEclMeldungAktionaer=lMeldung;
                    blWillenserklaerung.pAufnehmendeSammelkarteIdent=lMeldungZuSammelkarte.sammelIdent;
                    
                    switch (lWillenserklaerungArt) {
                    case KonstWillenserklaerung.vollmachtUndWeisungAnSRV:
                        blWillenserklaerung.widerrufVollmachtUndWeisungAnSRV(lDbBundle);
                        break;
                    case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
                        blWillenserklaerung.widerrufVollmachtUndWeisungAnKIAV(lDbBundle);
                        break;
                    case KonstWillenserklaerung.dauervollmachtAnKIAV:
                        blWillenserklaerung.widerrufDauervollmachtAnKIAV(lDbBundle);
                        break;
                    case KonstWillenserklaerung.briefwahl:
                        blWillenserklaerung.widerrufBriefwahl(lDbBundle);
                        break;
                    default:
                        CaBug.drucke("Falsche Willenserklaerung");
                    }
                    if (blWillenserklaerung.rcIstZulaessig==false) {
                        CaBug.drucke("Grund für Unzulässig="+blWillenserklaerung.rcGrundFuerUnzulaessig);
                    }
                }


            }





        }




    }


}
