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
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstNummerBasis;

/**NummernformUmwandlung der Aktionärsnummer von Eingabe zu Intern und umgekehrt*/
public class BlNummernformBasis {

    private static int logDrucken = 3;

    private DbBundle lDbBundle = null;

    public int rcNummernformBasis = 0;

    /**pDbBundle nur zur Parameterübergabe*/
    public BlNummernformBasis(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    /**
     * > eliminiert Null zu ""
     * > Schneidet Leerzeichen ab
     */
    @Deprecated
    public String wandleEingabeZuIntern(String pNummer) {
        String neuNummer = pNummer;
        if (neuNummer == null) {
            neuNummer = "";
        }
        neuNummer = neuNummer.trim();

        if (ParamSpezial.ku178(lDbBundle.clGlobalVar.mandant)) {
            if (!CaString.isNummern(neuNummer)) {
                return "";
            }
            String hString = Long.toString(Long.parseLong(neuNummer));
            neuNummer = CaString.fuelleLinksNull(hString, 10) + "1";
        }

        return neuNummer;
    }

    @Deprecated
    public String wandleScanZuIntern(String pNummer) {
        return wandleEingabeZuIntern(pNummer);
    }

//    @Deprecated
//    public String wandleZuExtern(String pNummer) {
//        String neuNummer = pNummer;
//        if (lDbBundle.param.paramBasis.ohneNullAktionaersnummer == 1) {
//            if (!CaString.isNummern(neuNummer)) {
//                return neuNummer;
//            }
//            neuNummer = Long.toString(Long.parseLong(neuNummer));
//        }
//        return neuNummer;
//    }

    /*****************************Aktienregister-Nummer bzw. Login-Kennung im Portal*******************************/

    /**Bereitet die als Aktionärsnummer eingegebene Nummer für die interne Verwendung auf.
     * Konkret:
     * > Leerzeichen werden entfernt
     * > Uppercase
     * 
     * > Wenn was anderes als Ziffern enthalten sind, dann ansonsten unverändert lassen
     * > Wenn Inhaberaktien aktiv: liefert Ziffer ohne führende Null und ohne angehängte 0 oder 1 (bzw. Original-String, falls Länge>8)
     * > Wenn String kürzer ist als Aktionärsnummerlänge, dann fülle rechtsbündig mit 0 auf bis zur Länge der Aktionärsnummer
     * 
     * D.h.: 0 bzw. 1 anhängen muß nach Aufruf dieser Funktion ggf. noch durchgeführt werden.
     * 
     * Rückgabe:
     * > Aufbereiteter String
     * > rcNummernformBasis gemäß KonstNummerBasis
     * 
     * Neue Funktion BlNummernformenBasis.loginKennungAufbereitenFuerIntern
     */
    public String loginKennungAufbereitenFuerIntern(String pEingegebeneNummer) {
        CaBug.druckeLog("Start", logDrucken, 10);
        rcNummernformBasis = 0;

        String hNummer = pEingegebeneNummer.replace(" ", "");
        hNummer = hNummer.toUpperCase();

        if (lDbBundle.param.paramPortal.kennungAufbereiten==2) {
            return hNummer;
        }
            
        
        hNummer=hNummer.replace("-","");

        if (hNummer.isEmpty()) {
            return hNummer;
        }

        if (hNummer.contains("@")) {
            rcNummernformBasis = KonstNummerBasis.formMail;
            return hNummer;
        }

        String hAnfang = hNummer.substring(0, 1);
        if (hAnfang.equals("S")) {
            rcNummernformBasis = KonstNummerBasis.formS;
            return hNummer;
        }

        if (hAnfang.equals("A")) {
            rcNummernformBasis = KonstNummerBasis.formA;
            return hNummer;
        }

        boolean nurNummernEnthalten=CaString.isNummern(hNummer);
        
        if ( nurNummernEnthalten== false && lDbBundle.param.paramPortal.kennungAufbereiten==0) {
            return hNummer;
        }

        rcNummernformBasis = KonstNummerBasis.nurNummer;

        if (lDbBundle.param.paramBasis.inhaberaktienAktiv) {
            if (hNummer.length() > 8 || nurNummernEnthalten==false) {
                return hNummer;
            }
            hNummer = Integer.toString(Integer.parseInt(hNummer));
            return hNummer;
        }

        if (ParamSpezial.ku097(lDbBundle.clGlobalVar.mandant)) {
            return hNummer;
        }

        if (hNummer.length() < lDbBundle.param.paramBasis.laengeAktionaersnummer) {
            hNummer = CaString.fuelleLinksNull(hNummer, lDbBundle.param.paramBasis.laengeAktionaersnummer);
        }

        CaBug.druckeLog("hNummer=" + hNummer, logDrucken, 10);
        return hNummer;
    }

    /**pDbBundle nur wg. Parametern benötigt*/
    public String aufbereitenKennungFuerRegisterzugriff(String pNummer) {
        return pNummer;
    }
    
    /**
     * Aufbereiten für GenossenschaftSys Webrequests
     */
    
    public String aufbereitenKennungFuerGenossenschaftSysWebrequest(String pNummer) {
    	if (pNummer.length() == 11) {
    		pNummer = pNummer.substring(0, 10);
    	}
    	while(pNummer.startsWith("0")) {
    		pNummer = pNummer.substring(1);
    	}
    	return pNummer;
    }

    
    
    /**********************Aufbereiten für Datenbankzugriff******************
     * pNummer muß vorher über loginKennungAufbereitenFuerIntern aufbereitet werden.
     * 
     * Diese Funktion setzt ggf. lediglich noch ne 0 dran
     * 
     * "-" wird entfernt, falls vorhanden
     */
    public static String aufbereitenFuerDatenbankZugriff(String pNummer, DbBundle pDbBundle) {
        int lLaengeSoll = pDbBundle.param.paramBasis.laengeAktionaersnummer;
        if (pDbBundle.param.paramBasis.namensaktienAktiv == false) {
            return pNummer;
        }
        if (lLaengeSoll == 0) {
            return pNummer;
        }

        if (ParamSpezial.ku097(pDbBundle.clGlobalVar.mandant) && pNummer.length() > 2
                && (pNummer.substring(0, 2).compareTo("80") == 0 || pNummer.substring(0, 2).compareTo("90") == 0)) {
            lLaengeSoll = 9;
        }

        String ergebnis = pNummer;
        if (pNummer.isEmpty()) {
            return "";
        }
        if (!pNummer.startsWith("S")) {
            int laengeIst = ergebnis.length();
            if (laengeIst == lLaengeSoll) {
                ergebnis = ergebnis + "0";
            }
        }

        return ergebnis;
    }

    /*************Interne Nummer aufbreiten für externe Anzeige******************/
    public static String aufbereitenInternFuerExtern(String pNummer, DbBundle pDbBundle) {
        String ergebnis = "";

        int lLaengeSoll = pDbBundle.param.paramBasis.laengeAktionaersnummer;
        if (pNummer.length() < lLaengeSoll + 1) {
            ergebnis = pNummer;
        } else {
            ergebnis = pNummer.substring(0, lLaengeSoll);
            String hinten = pNummer.substring(lLaengeSoll);
            CaBug.druckeLog("ergebnis=" + ergebnis, logDrucken, 10);
            CaBug.druckeLog("hinten=" + hinten, logDrucken, 10);
            if (pDbBundle.param.paramBasis.ohneNullAktionaersnummer == 1) {
                if (!CaString.isNummern(ergebnis)) {
                    return "";
                }
                ergebnis = Long.toString(Long.parseLong(ergebnis));
            } else {
                if (!hinten.equals("0")) {
                    ergebnis = ergebnis + "" + "-" + hinten;
                }
            }
        }
        return ergebnis;
    }
    
    
    /********************Ab hier neu und überarbeitet - zukünftig Stück für Stück auf diese umstellen - 10.04.2021*****************************/
    
    /**pDbBundle nur wg. Parametern benötigt*/
    public static String aufbereitenKennungFuerExtern(String pNummer, DbBundle pDbBundle) {
        String ergebnis="";
        
        if (pNummer==null || pNummer.isEmpty()) {
            /*Leerer Parameter nicht zulässig*/
            CaBug.drucke("001");
            return "";
        }
        
        /*Gastkennung bleibt unverändert*/
        if (pNummer.startsWith("S")) {
            return pNummer;
        }
        
        if (pDbBundle.param.paramBasis.namensaktienAktiv == false) {
            /*Nur Inhaberaktien aktiv*/
            return pNummer;
        }
        
        if (pDbBundle.param.paramBasis.inhaberaktienAktiv == false) {
            /*Nur Namensaktien aktiv*/
            int lLaengeSoll = pDbBundle.param.paramBasis.laengeAktionaersnummer;
            if (pNummer.length() < lLaengeSoll + 1) {
                ergebnis = pNummer;
            } else {
                ergebnis = pNummer.substring(0, lLaengeSoll);
                String hinten = pNummer.substring(lLaengeSoll);
                CaBug.druckeLog("ergebnis=" + ergebnis, logDrucken, 10);
                CaBug.druckeLog("hinten=" + hinten, logDrucken, 10);
                if (pDbBundle.param.paramBasis.ohneNullAktionaersnummer == 1) {
                    if (!CaString.isNummern(ergebnis)) {
                        return "";
                    }
                    ergebnis = Long.toString(Long.parseLong(ergebnis));
                } else {
                    if (!hinten.equals("0")) {
                        ergebnis = ergebnis + "" + "-" + hinten;
                    }
                }
            }
            return ergebnis;
        }
        
        /*Namensaktien und Inhaberaktien aktiv*/
        /*XXX*/
        /*Idee: Parameter einführen, bis zu welcher "Aktionärsnummer" es
         * Inhaberaktien sind, und das dann als Trennung verwenden.
         * 
         * Vorläufig das "alte" Prozedere
         */

        int lLaengeSoll = pDbBundle.param.paramBasis.laengeAktionaersnummer;
        if (pNummer.length() < lLaengeSoll + 1) {
            ergebnis = pNummer;
        } else {
            ergebnis = pNummer.substring(0, lLaengeSoll);
            String hinten = pNummer.substring(lLaengeSoll);
            CaBug.druckeLog("ergebnis=" + ergebnis, logDrucken, 10);
            CaBug.druckeLog("hinten=" + hinten, logDrucken, 10);
            if (pDbBundle.param.paramBasis.ohneNullAktionaersnummer == 1) {
                if (!CaString.isNummern(ergebnis)) {
                    return "";
                }
                ergebnis = Long.toString(Long.parseLong(ergebnis));
            } else {
                if (!hinten.equals("0")) {
                    ergebnis = ergebnis + "" + "-" + hinten;
                }
            }
        }

        return ergebnis;
    }
    
    
    
 
}
