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

import java.util.LinkedList;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComKonst.KonstEingabeQuelle;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzBuchen;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzBuchenRC;

public class BlPraesenzGesamtabgang {

    private int logDrucken=10;
    
    private DbBundle lDbBundle=null;
    
    public BlPraesenzGesamtabgang(DbBundle pDbBundle) {
        lDbBundle=pDbBundle;
    }
    
    public int gesamtabgangAusfuehren() {
        
        lDbBundle.dbMeldungen.leseAlleMeldungen();
        EclMeldung[] meldungenArray=lDbBundle.dbMeldungen.meldungenArray;
        
        if (meldungenArray==null) {
            return 1;
        }
        
        for (int i=0;i<meldungenArray.length;i++) {
            EclMeldung lMeldung=meldungenArray[i];
            if (lMeldung.statusPraesenz==1 && (lMeldung.meldungstyp==0 /*Gastkarte*/ || lMeldung.meldungstyp==1 /*Einzelaktionär*/)) {
                
                WEPraesenzBuchen wePraesenzBuchen=new WEPraesenzBuchen();

                WELoginVerify weLoginVerify=new WELoginVerify();
                weLoginVerify.setEingabeQuelle(KonstEingabeQuelle.konventionell_aufHV);
                wePraesenzBuchen.weLoginVerify=weLoginVerify;



                if (lMeldung.meldungstyp==1) {
                    /*Aktionär*/
                    wePraesenzBuchen.zutrittsIdentAktionaerArt = new LinkedList<Integer>();
                    wePraesenzBuchen.zutrittsIdentAktionaerArt.add(2);

                    EclZutrittsIdent lZutrittsIdentAktionaer=new EclZutrittsIdent();
                    lZutrittsIdentAktionaer.zutrittsIdent=lMeldung.zutrittsIdent; 

                    wePraesenzBuchen.zutrittsIdentAktionaer = new LinkedList<EclZutrittsIdent>();
                    wePraesenzBuchen.zutrittsIdentAktionaer.add(lZutrittsIdentAktionaer);

                    wePraesenzBuchen.zutrittsIdentGastArt = new LinkedList<Integer>();
                    wePraesenzBuchen.zutrittsIdentGastArt.add(0);

                    EclZutrittsIdent lZutrittsIdentGast=new EclZutrittsIdent();

                    wePraesenzBuchen.zutrittsIdentGast = new LinkedList<EclZutrittsIdent>();
                    wePraesenzBuchen.zutrittsIdentGast.add(lZutrittsIdentGast);


                }
                else {
                    /*Gast*/
                    wePraesenzBuchen.zutrittsIdentAktionaerArt = new LinkedList<Integer>();
                    wePraesenzBuchen.zutrittsIdentAktionaerArt.add(0);

                    EclZutrittsIdent lZutrittsIdentAktionaer=new EclZutrittsIdent();

                    wePraesenzBuchen.zutrittsIdentAktionaer = new LinkedList<EclZutrittsIdent>();
                    wePraesenzBuchen.zutrittsIdentAktionaer.add(lZutrittsIdentAktionaer);

                    wePraesenzBuchen.zutrittsIdentGastArt = new LinkedList<Integer>();
                    wePraesenzBuchen.zutrittsIdentGastArt.add(2);

                    EclZutrittsIdent lZutrittsIdentGast=new EclZutrittsIdent();
                    lZutrittsIdentGast.zutrittsIdent=lMeldung.zutrittsIdent; 


                    wePraesenzBuchen.zutrittsIdentGast = new LinkedList<EclZutrittsIdent>();
                    wePraesenzBuchen.zutrittsIdentGast.add(lZutrittsIdentGast);

                }


                wePraesenzBuchen.stimmkartenArt = new LinkedList<int[]>();
                int[] lStimmkartenArt=new int[4];
                for (int i1=0;i1<4;i1++) {lStimmkartenArt[i1]=0;}
                wePraesenzBuchen.stimmkartenArt.add(lStimmkartenArt);

                wePraesenzBuchen.stimmkartenSecondArt = new LinkedList<Integer>();
                wePraesenzBuchen.stimmkartenSecondArt.add(0);
                
                wePraesenzBuchen.eclMeldung = new LinkedList<EclMeldung>();
                wePraesenzBuchen.eclMeldung.add(lMeldung);
               
                wePraesenzBuchen.vollmachtPersonenNatJurIdent = new LinkedList<Integer>();
                wePraesenzBuchen.vollmachtPersonenNatJurIdent.add(-1);

                wePraesenzBuchen.programmFunktion=1;
                
                wePraesenzBuchen.funktion = new LinkedList<Integer>();
                if (lMeldung.meldungstyp==0) {
                    /*Gast*/
                    wePraesenzBuchen.funktion.add(KonstWillenserklaerung.abgang);
               }
                else {
                    /*Aktionär*/
                    wePraesenzBuchen.funktion.add(KonstWillenserklaerung.abgang);
                }
                
                BlPraesenzAkkreditierung blPraesenzAkkreditierung=new BlPraesenzAkkreditierung(lDbBundle);
                WEPraesenzBuchenRC wePraesenzBuchenRC=blPraesenzAkkreditierung.buchen(wePraesenzBuchen);

                if (wePraesenzBuchenRC.rc==1) {
                    CaBug.druckeLog("Abgang gebucht für: "+lMeldung.zutrittsIdent, logDrucken, 10);
                }
                else {
                    CaBug.drucke("01 Fehler bei Abgangsbuchung "+wePraesenzBuchenRC.rc);
                }
            }
        }
        return 1;
    }
}
