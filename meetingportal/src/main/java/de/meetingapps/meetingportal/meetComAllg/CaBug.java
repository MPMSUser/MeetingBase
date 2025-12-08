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
 package de.meetingapps.meetingportal.meetComAllg;

/*****************************Ausgabe von Bugs********************************************************
 * Diese Klasse wird verwendet, um an Stellen, an denen "Fehler, die nicht auftreten dürfen",
 * abgefangen werden, einen entsprechenden Log-Eintrag zu erzeugen.
 *
 * In der derzeit einfachsten Variante erfolgt dies durch Ausgabe eines entsprechend markierten 
 * Textes in der Systemkonsole.
 * 
 * Stufen:
 * 1=elementare Log-Stufe - Nachverfolgung was ein Benutzer grundsätzlich getan hat
 * 2=Nachverfolgung was ein Benutzer im Detail gemacht hat
 * 3=Nachverfolgung wichtiger System-Funktionen (=normale Stufe für Aktivierung)
 * 5="Mittel-Wichtige Logs" - Nachverfolgung, welche Einzelfunktionen aufgerufen wurden
 * 10=Detail-Logs, Ausgabe einzelner Variablen etc.
 * 
 */

public class CaBug {
    //	private boolean logDrucken=true;

    /**Ab dieser Stufe werden alle Logs gedruckt, egal wie lokale Parameter stehen.
     * 11=keiner wird zwangsweise unterdrückt; 0=alle werden zwangsläufig unterdrückt
     */
    public static int druckenGlobalForceAbStufeAus = 11; //11

    /**Bis zu dieser Stufe werden alle Logs gedruckt, egal wie lokale Parameter stehen.
     * 0 = keiner wird zwangsweise gedruckt; 11=alle werden zwangsweise gedruckt.
     * Muß immer kleiner <= druckenGlobalForceAbStufeAus sein
     */
    public static int druckenGlobalForceBisStufeAn = 0;

    /**Wenn true, dann werden alle logs (boolean) unterdrückt, auch wenn lokal auf "true"*/
    public static boolean druckenGlobalForceAus = false; //false

    /**Wenn true, dann werden alle logs (boolean) gedruckt, auch wenn lokal auf "false"
     * In diesem Fall wird druckenGlobalForceAus ignoriert*/
    public static boolean druckenGlobalForceAn = false;

    static public void drucke(String text) {
        int laengeStackTrace = Thread.currentThread().getStackTrace().length;
        System.out.println(
                "**CaBug**********************************************************************************************************");
        if (laengeStackTrace > 6) {
            System.out.println(Thread.currentThread().getStackTrace()[6].getClassName() + "."
                    + Thread.currentThread().getStackTrace()[6].getMethodName());
        }
        if (laengeStackTrace > 5) {
            System.out.println(Thread.currentThread().getStackTrace()[5].getClassName() + "."
                    + Thread.currentThread().getStackTrace()[5].getMethodName());
        }
        if (laengeStackTrace > 4) {
            System.out.println(Thread.currentThread().getStackTrace()[4].getClassName() + "."
                    + Thread.currentThread().getStackTrace()[4].getMethodName());
        }
        if (laengeStackTrace > 3) {
            System.out.println(Thread.currentThread().getStackTrace()[3].getClassName() + "."
                    + Thread.currentThread().getStackTrace()[3].getMethodName());
        }

        System.out.println(Thread.currentThread().getStackTrace()[2].getClassName() + "."
                + Thread.currentThread().getStackTrace()[2].getMethodName() + ": " + text);
        System.out.println(
                "*****************************************************************************************************************");
    }

    static public void druckeProt(String text) {
        System.out.println("Prot: " + text);
    }

    static public void druckeInfo(String text) {
        System.out.println(text +" **Info: " + Thread.currentThread().getStackTrace()[2].getClassName() + "."
                + Thread.currentThread().getStackTrace()[2].getMethodName());
    }


    static public void druckeLog(String text, int logDrucken, int stufe) {
        if ((logDrucken >= stufe && druckenGlobalForceAbStufeAus >= stufe) || druckenGlobalForceBisStufeAn > stufe) {
            System.out.println("**BMLog "+Integer.toString(logDrucken)+": " + Thread.currentThread().getStackTrace()[2].getClassName() + "."
                    + Thread.currentThread().getStackTrace()[2].getMethodName() + ": " + text);
        }
    }

    static public void druckeLogAufrufsequenz(String text, int logDrucken, int stufe) {
        if ((logDrucken >= stufe && druckenGlobalForceAbStufeAus >= stufe) || druckenGlobalForceBisStufeAn > stufe) {
            System.out.println("**BMLog "+Integer.toString(logDrucken)+": " + Thread.currentThread().getStackTrace()[2].getClassName() + "."
                    + Thread.currentThread().getStackTrace()[2].getMethodName() + ": " + text);
            int laengeStackTrace = Thread.currentThread().getStackTrace().length;
            if (laengeStackTrace > 9) {
                System.out.println(Thread.currentThread().getStackTrace()[9].getClassName() + "."
                        + Thread.currentThread().getStackTrace()[8].getMethodName());
            }
            if (laengeStackTrace > 8) {
                System.out.println(Thread.currentThread().getStackTrace()[8].getClassName() + "."
                        + Thread.currentThread().getStackTrace()[8].getMethodName());
            }
            if (laengeStackTrace > 7) {
                System.out.println(Thread.currentThread().getStackTrace()[7].getClassName() + "."
                        + Thread.currentThread().getStackTrace()[7].getMethodName());
            }
            if (laengeStackTrace > 6) {
                System.out.println(Thread.currentThread().getStackTrace()[6].getClassName() + "."
                        + Thread.currentThread().getStackTrace()[6].getMethodName());
            }
            if (laengeStackTrace > 5) {
                System.out.println(Thread.currentThread().getStackTrace()[5].getClassName() + "."
                        + Thread.currentThread().getStackTrace()[5].getMethodName());
            }
            if (laengeStackTrace > 4) {
                System.out.println(Thread.currentThread().getStackTrace()[4].getClassName() + "."
                        + Thread.currentThread().getStackTrace()[4].getMethodName());
            }
            if (laengeStackTrace > 3) {
                System.out.println(Thread.currentThread().getStackTrace()[3].getClassName() + "."
                        + Thread.currentThread().getStackTrace()[3].getMethodName());
            }

            System.out.println(Thread.currentThread().getStackTrace()[2].getClassName() + "."
                    + Thread.currentThread().getStackTrace()[2].getMethodName() + ": " + text);
        }
    }

    /**Nur als Ersatz für System.out.println. Nach Möglichkeit nur temporär verwenden*/
    static public void out(String text ) {
        System.out.println("**BMOut: " + Thread.currentThread().getStackTrace()[2].getClassName() + "."
                + Thread.currentThread().getStackTrace()[2].getMethodName() + ": " + text);
        
    }
    static public void out(int text ) {
        System.out.println("**BMOut: " + Thread.currentThread().getStackTrace()[2].getClassName() + "."
                + Thread.currentThread().getStackTrace()[2].getMethodName() + ": " + text);
        
    }
   
    static public boolean pruefeLog(int logDrucken, int stufe) {
        if ((logDrucken >= stufe && druckenGlobalForceAbStufeAus >= stufe) || druckenGlobalForceBisStufeAn > stufe) {
            return true;
        }
        return false;
    }

}
