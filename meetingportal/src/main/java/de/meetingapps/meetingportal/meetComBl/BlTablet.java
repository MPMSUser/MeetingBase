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

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclTablet;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;

public class BlTablet {

    private DbBundle lDbBundle = null;

    public boolean tabletIstPersoenlichesTablet=false;
    
    /**+++++++++++++++++++++Die folgenden Werte sind nur belegt, wenn tabletIstPersoenlichesTablet==true++++++++++++*/
    /**Zordnung ist bereits erfolgt*/
    public boolean tabletIstBereitsZugordnet=false;
    
    /**Nummer, die für die Abgabe der Abstimmung verwendet wird
     * Kann leer sein, dann noch nicht zugeordnet*/
    public String nummerIdentifikation="";
    
    /**Anzeige (für Teilnehmername)*/
    public String anzeigeName="";

    
    public void init(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    public void setzeStatusTabletAbstimmungOffen(int pTablet) {
        setzeStatusTablet(pTablet, 2);
        
        if (lDbBundle.paramGeraet.abstimmungTabletPersoenlichZugeordnet) {
            tabletIstPersoenlichesTablet=true;
            
            BlNummernformen blNummernformen=new BlNummernformen(lDbBundle);
            String sNr=blNummernformen.formatiereNr(Integer.toString(pTablet), KonstKartenklasse.stimmkartennummer);
            lDbBundle.dbStimmkarten.read(sNr);
            if (lDbBundle.dbStimmkarten.anzErgebnis()!=0) {
                tabletIstBereitsZugordnet=true;
                nummerIdentifikation=sNr;
                EclStimmkarten lStimmkarte=lDbBundle.dbStimmkarten.ergebnisPosition(0);
                int meldeIdent=lStimmkarte.meldungsIdentAktionaer;
                lDbBundle.dbMeldungen.leseZuIdent(meldeIdent);
                EclMeldung lMeldung=lDbBundle.dbMeldungen.meldungenArray[0];
                anzeigeName=lMeldung.name+" "+lMeldung.vorname +" "+lMeldung.ort;
            }
            
        }
    }

    public void setzeStatusTabletAbstimmungGeschlossen(int pTablet) {
        setzeStatusTablet(pTablet, 3);
    }

    private void setzeStatusTablet(int pTablet, int pStatus) {

        EclTablet lTablet = null;

        int anz = lDbBundle.dbTablet.lese_Ident(pTablet);
        if (anz < 1) {
            lTablet = new EclTablet();
            lTablet.arbeitsplatzNr = pTablet;
            lTablet.status = 0;
            lDbBundle.dbTablet.insert(lTablet);
        } else {
            lTablet = lDbBundle.dbTablet.ergebnisGefunden(0);
        }

        lTablet.status = pStatus;
        lDbBundle.dbTablet.update(lTablet);

    }

    public EclTablet[] liefereGesamtStatus() {

        EclTablet[] tablets = null;
        /*int anz=*/lDbBundle.dbTablet.lese_all();
        tablets = lDbBundle.dbTablet.ergebnisArray;

        return tablets;
    }

}
