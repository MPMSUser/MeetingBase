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
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungEinzelneWeisung;

/**Umkopieren aller Weisungen von einer Abstimmung in mehrere andere*/
public class BlWeisungenKopieren {

    private int logDrucken=10;
    
    /**Ident der Weisung, die als Quelle dient*/
    public int identWeisungssatzQuelle=0;
    
    /**Array mit den Weisungen, in die rein kopiert werden soll*/
    public List<Integer> identWeisungssatzZiel=new LinkedList<Integer>();
    
    /**[0..12, quellWeisung laut KonstStimmart wird umgesetzt auf Wert in diesem Array*/
    public int[] umsetzungWeisungen=null;
    
    
    private DbBundle lDbBundle=null;
    
    public BlWeisungenKopieren(DbBundle pDbBundle) {
        lDbBundle=pDbBundle;
        
        umsetzungWeisungen=new int[13];
        for (int i=0;i<=12;i++) {
            umsetzungWeisungen[i]=0;
        }
    }
    
    /**Nach dieser Funktion müssen Summen der Sammelkarten korrigiert werden!*/
    public int kopieren() {
        
        try {
        /*Alle Weisungen der Quell-Ident (und nur dieser) von Einzelaktionären einlesen*/
        lDbBundle.dbWeisungMeldung.leseAktionaersWeisungenZuWeisungsIdent_einzelneWeisung(identWeisungssatzQuelle);
        List<EclWeisungMeldungEinzelneWeisung> lWeisungMeldungQuelleListe=lDbBundle.dbWeisungMeldung.ergebnis_einzelneWeisung();
       
        lDbBundle.dbBasis.beginTransactionNeu();
        
        /*Schleife für alle Weisungen von Einzelaktionären*/
        for (int i=0;i<lWeisungMeldungQuelleListe.size();i++) {
            EclWeisungMeldungEinzelneWeisung lWeisungMeldungEinzelneWeisung=lWeisungMeldungQuelleListe.get(i);
            
            /*Schleife für alle Ziel-Weisungen*/
            for (int i1=0;i1<identWeisungssatzZiel.size();i1++) {
                /* Kopieren.
                 * Achtung - beim update in DB checken, ob wirklich nicht markiert
                 */
                lDbBundle.dbWeisungMeldung.updateAktionaersWeisungen_einzelneWeisung(lWeisungMeldungEinzelneWeisung.weisungIdent, identWeisungssatzZiel.get(i1), umsetzungWeisungen[lWeisungMeldungEinzelneWeisung.abgabe], true);
            }
            
            /*Commit durchführen, damit diese Weisung wieder freigegeben wird*/
            lDbBundle.dbBasis.zwischenCommitNeu();
            
            if (i % 500==0) {
                CaBug.druckeLog("Kopieren weisungIdent:"+lWeisungMeldungEinzelneWeisung.weisungIdent+" von "+i, logDrucken, 3);
            }
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        
        lDbBundle.dbBasis.endTransactionNeu();
        } catch (Exception e2) {
            CaBug.drucke("001");
            System.err.println(" " + e2.getMessage());
            lDbBundle.dbBasis.endTransactionNeu();
        }
        
        return 1;
    }
    
    
    /**Nach dieser Funktion müssen Summen der Sammelkarten korrigiert werden!
     * 
     * Es werden die Ziel-TOPs komplett mit Nicht-Markiert belegt.-*/
    public int initialisieren() {
        try {
        
        /*Alle Weisungen der Quell-Ident (und nur dieser) von Einzelaktionären einlesen*/
        lDbBundle.dbWeisungMeldung.leseAktionaersWeisungenZuWeisungsIdent_einzelneWeisung(identWeisungssatzQuelle);
        List<EclWeisungMeldungEinzelneWeisung> lWeisungMeldungQuelleListe=lDbBundle.dbWeisungMeldung.ergebnis_einzelneWeisung();
       
        lDbBundle.dbBasis.beginTransactionNeu();
        
        /*Schleife für alle Weisungen von Einzelaktionären*/
        for (int i=0;i<lWeisungMeldungQuelleListe.size();i++) {
            EclWeisungMeldungEinzelneWeisung lWeisungMeldungEinzelneWeisung=lWeisungMeldungQuelleListe.get(i);
            
            /*Schleife für alle Ziel-Weisungen*/
            for (int i1=0;i1<identWeisungssatzZiel.size();i1++) {
                /* Kopieren.
                 * Achtung - beim update in DB checken, ob wirklich nicht markiert
                 */
                lDbBundle.dbWeisungMeldung.updateAktionaersWeisungen_einzelneWeisung(lWeisungMeldungEinzelneWeisung.weisungIdent, identWeisungssatzZiel.get(i1), 0, false);
            }
            
            /*Commit durchführen, damit diese Weisung wieder freigegeben wird*/
            lDbBundle.dbBasis.zwischenCommitNeu();
            
            if (i % 500==0) {
                CaBug.druckeLog("Initialisieren weisungIdent:"+lWeisungMeldungEinzelneWeisung.weisungIdent+" von "+i, logDrucken, 3);
            }
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        
        lDbBundle.dbBasis.endTransactionNeu();
        } catch (Exception e2) {
            CaBug.drucke("001");
            System.err.println(" " + e2.getMessage());
            lDbBundle.dbBasis.endTransactionNeu();
        }
        
        return 1;
    }
    
    
}
