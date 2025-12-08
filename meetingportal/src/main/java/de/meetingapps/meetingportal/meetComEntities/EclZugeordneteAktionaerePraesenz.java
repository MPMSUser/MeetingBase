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


/**Passend zu ku178-Akkreditierung:
 * Ausdruck von Batches, auf denen alle zugeordneten Aktionär stehen.
 * Zuordnung erfolgt manuell, nach entsprechender Vollmachtsprüfung
 * 
 * Hinweis: es kann eine Zuordnung mehrfach drin sein - nämlich z.B. einmal direkt, und einmal über Kind. Und auch mit
 * unterschiedlichen Vertretungsarten. Kann aber immer nur einer
 * aktiv sein. D.h. Primäry Key=anwesendePersonLoginIdent, zugeordneterAktionaer, geerbtVonZugeordnetemAktionaer, zuordnungIstAktiv, zuordnungsArt
 */
public class EclZugeordneteAktionaerePraesenz {

    /**Dieser Person sind weitere Meldungen zugeordnet.
     * Kann sowohl Aktionärsnummer als auch Gastnummer sein.
     * LEN=11;
     */
    public String anwesendePersonLoginIdent="";
    
    /**Aktionärsnummer, die der teilnehmenden Person Vollmacht erteilt hat  (oder sie selbst ist)
     * 
     * Kann auch - bei zuordnungsArt=99999, eine Begleitperson-Kennung sein
     * LEN=11;*/
    public String zugeordneterAktionaer="";
    
    public String geerbtVonZugeordnetemAktionaer="";
    
    /**
     * -1=Selbst
     * 0=Bevollmächtigter
     * 1=Gesetzlicher
     * 2=Bevollmächtigter - Geerbter Bevollmächtigter
     * 3=Bevollmächtigter - Geerbter Gesetzlicher Bevollmächtigter
     * 4=Gesetzlicher - Geerbter Bevollmächtigter
     * 5=Gesetzlicher - Geerbter Gesetzlicher Bevollmächtigter
     * 
     * 9999=Begleitperson
     */
    public int zuordnungsArt=0;
    
    /**0 oder 1*/
    public int zuordnungIstAktiv=0;
    
    /**LEN=19*/
    public String zuordnungAktiviertZeitpunkt="";
    
    /**LEN=19*/
    public String zuordnungDeaktiviertZeitpunkt="";
    
    
    /**********************Nicht in Datenbank*************************************/
    public EclAktienregister eclAktienregister=null;
    
    public EclPersonenNatJur eclPersonenNatJur=null;
    
    /**Dient dazu, um die Position in der ursprünglichen Mitgliederliste beim Übertragen in die Kinderliste
     * festzuhalten, um dann wieder zurückübertragen zu können.
     */
    public int quellOffset=0;
    
    /**Wie zuordnungIstAktiv, wird beim Read in db automatisch gesetzt.
     * -1 => Eintrag war noch nicht in Datenbank
     */
    public int statusInDatenbank=-1;
    
    /**Nur für Begleitpersonenen - Bearbeitung gesperrt, da Präsent*/
    public boolean zurBearbeitungGesperrt=false;
    
    /**Sonderprogrammierung für ku178 2024 - Ausgabe der Bändchen*/
    public int anmeldungenEmpfang=0;
}
