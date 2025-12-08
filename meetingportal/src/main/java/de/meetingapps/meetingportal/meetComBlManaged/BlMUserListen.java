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
package de.meetingapps.meetingportal.meetComBlManaged;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclSocketJsfToUser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

/**User-Listen zum Verwalten der angemeldeten User - je Server*/
@ApplicationScoped
@Named
public class BlMUserListen implements Serializable {
    private static final long serialVersionUID = 6130570382421394656L;

    private int logDrucken = 3;

    private static HashMap<String, String> userToJsf = new HashMap<String, String>();
    private static HashMap<String, String> jsfToUser = new HashMap<String, String>();

    /**Beim Login: der User wird unter dieser Session eingetragen.
     * Sollte der User bereits unter einer anderen Session eingetragen sein, wird diese ausgetragen (was bei einer späteren Abfrage
     * bedeutet: diese Session ist dann disabled)
     */
    synchronized public boolean trageUserEin(String userKennung, int mandant, int hvJahr, String hvNummer,
            String datenbereich, String neueJsfSessionId) {
        CaBug.druckeLog("*********************************************userKennung=" + userKennung + " mandant="
                + mandant + " neueJsfSessionId=" + neueJsfSessionId, logDrucken, 10);
        ausgabe();
        EclSocketJsfToUser eclSocketJsfToUser = new EclSocketJsfToUser();
        eclSocketJsfToUser.userKennung = userKennung;
        eclSocketJsfToUser.mandant = mandant;
        eclSocketJsfToUser.hvJahr = hvJahr;
        eclSocketJsfToUser.hvNummer = hvNummer;
        eclSocketJsfToUser.datenbereich = datenbereich;

        /*Suchen, ob User bereits in userToJsf enthalten ist.
         * Wenn ja, dann aus userToJsf und jsfToUser austragen
         */
        if (userToJsf.containsKey(eclSocketJsfToUser.toString())) {
            String alteJsfSessionId = userToJsf.get(eclSocketJsfToUser.toString());
            CaBug.druckeLog("Austragen alte SessionID=" + alteJsfSessionId, logDrucken, 10);
            String sessionRemoved = userToJsf.remove(eclSocketJsfToUser.toString());
            if (sessionRemoved == null) {
                CaBug.drucke("001");
            }
            String eclSocketJsfToUserRemoved = jsfToUser.remove(alteJsfSessionId);
            if (eclSocketJsfToUserRemoved == null) {
                CaBug.drucke("002");
            }
        }

        /*Nun neuen User eintragen*/
        userToJsf.put(eclSocketJsfToUser.toString(), neueJsfSessionId);
        jsfToUser.put(neueJsfSessionId, eclSocketJsfToUser.toString());
        ausgabe();
        return true;
    }

    /**Beim Logout: der User wird unter dieser Session ausgetragen. Falls nicht mehr vorhanden, wird das ignoriert*/
    synchronized public boolean trageUserAus(String userKennung, int mandant, int hvJahr, String hvNummer,
            String datenbereich, String jsfSessionId) {
        CaBug.druckeLog("*********************************************userKennung=" + userKennung + " mandant="
                + mandant + " hvJahr=" + hvJahr + " hvNummer=" + hvNummer + " datenbereich=" + datenbereich
                + " jsfSessionId=" + jsfSessionId, logDrucken, 10);
        ausgabe();
        EclSocketJsfToUser eclSocketJsfToUser = new EclSocketJsfToUser();
        eclSocketJsfToUser.userKennung = userKennung;
        eclSocketJsfToUser.mandant = mandant;
        eclSocketJsfToUser.hvJahr = hvJahr;
        eclSocketJsfToUser.hvNummer = hvNummer;
        eclSocketJsfToUser.datenbereich = datenbereich;

        if (userToJsf.containsKey(eclSocketJsfToUser.toString())) {
            String sessionRemove = userToJsf.get(eclSocketJsfToUser.toString());
            if (sessionRemove == null) {
                CaBug.drucke("001");
            } else {
                if (sessionRemove.equals(jsfSessionId)) {
                    String sessionRemoved = userToJsf.remove(eclSocketJsfToUser.toString());
                    if (sessionRemoved == null) {
                        CaBug.drucke("002");
                    }
                    String eclSocketJsfToUserRemoved = jsfToUser.remove(jsfSessionId);
                    if (eclSocketJsfToUserRemoved == null) {
                        CaBug.drucke("003");
                    }

                } else {
                    CaBug.drucke("004");
                }
            }

        } else {
            CaBug.druckeInfo("User userKennung=" + userKennung + " mandant=" + mandant + " nicht gefunden");
        }
        ausgabe();
        return true;
    }

    /**Es wird geprüft, ob der User unter dieser Session noch eingetragen ist. Wenn nein wurde die Session deaktiviert,
     * d.h. der User wurde über einen zweiten Login deaktiviert*/
    synchronized public boolean pruefeObUserEingetragen(String userKennung, int mandant, int hvJahr, String hvNummer,
            String datenbereich, String jsfSessionId) {
        CaBug.druckeLog("*********************************************userKennung=" + userKennung + " mandant="
                + mandant + " jsfSessionId=" + jsfSessionId, logDrucken, 10);
        ausgabe();
        EclSocketJsfToUser eclSocketJsfToUser = new EclSocketJsfToUser();
        eclSocketJsfToUser.userKennung = userKennung;
        eclSocketJsfToUser.mandant = mandant;
        eclSocketJsfToUser.hvJahr = hvJahr;
        eclSocketJsfToUser.hvNummer = hvNummer;
        eclSocketJsfToUser.datenbereich = datenbereich;

        if (userToJsf.containsKey(eclSocketJsfToUser.toString())) {
            String sessionGet = userToJsf.get(eclSocketJsfToUser.toString());
            if (sessionGet == null) {
                CaBug.drucke("001");
            } else {
                if (sessionGet.equals(jsfSessionId)) {
                    CaBug.druckeLog("return true", logDrucken, 10);
                    return true;
                }
            }

        }
        CaBug.druckeLog("return false", logDrucken, 10);
        return false;
    }

    private void ausgabe() {
        if (logDrucken != 10) {
            return;
        }
        System.out.println("====Anfang==========userToJsf========================");
        for (@SuppressWarnings("rawtypes")
        Map.Entry wert : userToJsf.entrySet()) {
            String eclSocketJsfToUser = (String) wert.getKey();
            String jsfSession = (String) wert.getValue();
            System.out.println("userKennung=" + eclSocketJsfToUser + " jsfSession=" + jsfSession);
        }
        System.out.println("==================Ende========================");

        System.out.println("==================jsfToUser========================");
        for (@SuppressWarnings("rawtypes")
        Map.Entry wert : jsfToUser.entrySet()) {
            String eclSocketJsfToUser = (String) wert.getValue();
            String jsfSession = (String) wert.getKey();
            System.out.println("key=" + jsfSession + " userKennung=" + eclSocketJsfToUser);
        }
        System.out.println("====Ende===========Ende========================");

    }

}
