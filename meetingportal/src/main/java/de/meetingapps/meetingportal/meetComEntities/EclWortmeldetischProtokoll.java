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
package de.meetingapps.meetingportal.meetComEntities;

/**Protokollierung von Änderungen von Wortmeldungen*/
public class EclWortmeldetischProtokoll {
    
    
    /**Bei sonstigeAktion 1 bis 100: wortmeldungsident.
     * Bei sonstigeAktion 101 bis 200: loginIde
     */
    public int identWortmeldung=0;
    
    /**LEN=19
     * JJJJ.MM.TT HH:MM:SS*/
    public String datumZeit="";
    
    /**LEN=60*/
    public String alterStatus="";
    
    /**LEN=60*/
    public String neuerStatus="";

    /**Vorgesehen z.B. für Popup durch Aktionär bestätigt - derzeit noch nicht vollständig implementiert
     * uLogin:
     * 1=Statuswechsel
     * 2=Bearbeitung, internerKommentar, externerKommentar
     * 
     * Aktionärssicht:
     * 101=stoppeKonferenz
     * 102=doKonferenzTestStarten
     * 103=doKonferenzTestStartenBackup
     * 104=doKonferenzTestBeenden
     * 105=doKonferenzInRederaumGehen
     * 106=doKonferenzRederaumStartenBackup
     * 107=doKonferenzRedeBeginnen
     * 108=doKonferenzRederaumVerlassen
     * 
     * */
    public int sonstigeAktion=0;
    
    /**LEN=200*/
    public String nameVornameOrt="";
    
    public int raumNr=0;
    
    public int lfdNrInListe=0;
    
    /**LEN=500*/
    public String kommentarIntern="";
    
    /**LEN=500*/
    public String kommentarVersammlungsleiter="";
}
