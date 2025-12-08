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
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;

/**Routinen zum Aufbereiten des Eintrittskartenstapeldrucks*/
@Deprecated
public class BlEintrittskartenStapelDruck {

    private int logDrucken=10;
    private DbBundle dbBundle=null;
    
    public EclWillenserklaerung ergebnisArrayWillenserklaerung[] = null;
    public EclWillenserklaerungZusatz ergebnisArrayWillenserklaerungZusatz[] = null;

    private EclMeldung ergebnisArrayMeldung[] = null;
    /**p Gehört zu EclMeldung*/
    private EclPersonenNatJur ergebnisArrayPersonenNatJur[] = null;
    private EclAnrede ergebnisArrayAnrede[] = null;
    private EclStaaten ergebnisArrayStaaten[] = null;
    
    private EclAktienregister ergebnisArrayAktienregisterEintrag[] = null;
    /*are Gehört zu Aktienregister*/
    private EclLoginDaten ergebnisArrayLoginDaten[] = null;
    
    /*Vertreter*/
    private EclPersonenNatJur ergebnisArrayPersonenNatJur1[] = null;
     private EclLoginDaten ergebnisArrayLoginDaten1[] = null;

    public BlEintrittskartenStapelDruck(DbBundle pDbBundle) {
        dbBundle=pDbBundle;
    }
    
    public int read_eintrittskartenDruck(boolean erstDruck, int drucklauf, boolean nurGepruefte,
            String ekNummer, String ekNummerNeben, int pGastOderAktionaer,
            String pVonZutrittsIdent, String pBisVonZutrittsIdent) {
        
        
        /*Basis über willenserklärungen einlesen*/
        int anzInArray=dbBundle.dbJoined.read_eintrittskartenDruckNurWillenserklaerungen(erstDruck, drucklauf, nurGepruefte, ekNummer, ekNummerNeben, pGastOderAktionaer, pVonZutrittsIdent, pBisVonZutrittsIdent);
        
        ergebnisArrayWillenserklaerung=dbBundle.dbJoined.ergebnisWillenserklaerung();
        ergebnisArrayWillenserklaerungZusatz=dbBundle.dbJoined.ergebnisWillenserklaerungZusatz();
        
        CaBug.druckeLog("A anzInArray="+anzInArray, logDrucken, 10);
        
        ergebnisArrayMeldung = new EclMeldung[anzInArray];
        ergebnisArrayPersonenNatJur = new EclPersonenNatJur[anzInArray];
        ergebnisArrayAnrede = new EclAnrede[anzInArray];
        ergebnisArrayStaaten = new EclStaaten[anzInArray];
        ergebnisArrayPersonenNatJur1 = new EclPersonenNatJur[anzInArray];
        ergebnisArrayAktienregisterEintrag = new EclAktienregister[anzInArray];
        ergebnisArrayLoginDaten=new EclLoginDaten[anzInArray];
        ergebnisArrayLoginDaten1=new EclLoginDaten[anzInArray];


        
        
        
        return anzInArray;
    }
    
}
