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
package de.meetingapps.meetingclient.meetingClientAllg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.meetingapps.meetingportal.meetComEntities.EclMailing;
import de.meetingapps.meetingportal.meetComEntities.EclMailingVariablen;

public class CaMailing {

    private int logDrucken = 3;

    public void sendMails(EclMailing eclMailing, ArrayList<EclMailingVariablen> versandliste,
            ArrayList<String> anhang) {
        // add your Mailing Server here
    }

    public static Map<String, String> createPersonalization(EclMailingVariablen eintrag) {

        Map<String, String> personalization = new HashMap<String, String>();

        personalization.put("kennung", eintrag.getKennung());
        personalization.put("passwort", eintrag.getPasswort());
        personalization.put("anrede", eintrag.getAnrede());
        personalization.put("nameKomplett", eintrag.getNameKomplett());
        personalization.put("vorname", eintrag.getVorname());
        personalization.put("nachname", eintrag.getNachname());
        personalization.put("link", eintrag.getLink());

        return personalization;
    }

}
