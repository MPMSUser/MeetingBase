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

public class EclMeldungZuSammelkarte {

    /**Mandant**/
    public int mandant = 0;

    /**Ident der Meldung, die der Sammelkarte zugeordnet ist**/
    public int meldungsIdent = 0;

    /**Ident der Sammelkarte, der die Meldung zugeordnet ist**/
    public int sammelIdent = 0;

    /**Ident der Willenserklärung, die diese Zuordnung ausgelöst hat**/
    public int willenserklaerungIdent = 0;

    /**Ident der "normalen" Weisung (und falls vorhanden: auch der Split-Weisung; RAW-Weisung sowieso**/
    public int weisungIdent = 0;

    /**Status der Zuordnung
     * =1 => aktiv
     * =0 => vorhanden, aber nicht aktiv (z.B. auch wegen Pending)
     * =-1 => widerrufen
     * =-2 => mittlerweile geändert
     */
    public int aktiv = 0;

    /**************Die folgenden Felder sind nicht in der Datenbank - können bei bedarf aus EclMeldung gefüllt werden***********/
    /**Bezeichnung der zugeordneten Sammelkarte*/
    public String name = "";
    /**Ort der zugeordneten Sammelkarte*/
    public String ort = "";
    /**falls Meldungstyp=2 Sammelkarte:
     *1=KIAV
     *2=SRV
     *3=organisatorisch
     *4=Briefwahl
     *5=Dauervollmacht*/
    public int skIst = 0;

    /**D.h. keine geänderte oder widerrufene Zuordnung*/
    public boolean zuordnungIstNochGueltig() {
        if (aktiv == 1 || aktiv == 0) {
            return true;
        } else {
            return false;
        }
    }
}
