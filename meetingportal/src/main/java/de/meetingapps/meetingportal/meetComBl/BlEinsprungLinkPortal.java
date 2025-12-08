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
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComKonst.KonstLinkArt;

public class BlEinsprungLinkPortal {

    private int logDrucken = 1;

    /* TODO _Portal: "Kunden-Link" auch in Email berücksichtigen! */

    private DbBundle dbBundle = null;

    /**DbBundle wird nur wg. Parametern benötigt, muß nicht open sein*/
    public BlEinsprungLinkPortal(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    /** pEmailNummer = 1 oder 2 (erste oder zweite Email) */
    public String linkFuerEmailBestaetigung(long pZeitDif, int pHvJahr, String pHvNummer, String pDatenbereich, String pSprache, String pTest, String pEmailBestaetigenCode, int pEmailNummer) {
        return linkFuerEmailBestaetigungIntern(pZeitDif, pHvJahr, pHvNummer, pDatenbereich, pSprache, pTest, pEmailBestaetigenCode, pEmailNummer, false);
    }

    /** pEmailNummer = 1 oder 2 (erste oder zweite Email) */
    public String linkFuerEmailBestaetigungApp(long pZeitDif, int pHvJahr, String pHvNummer, String pDatenbereich, String pSprache, String pTest, String pEmailBestaetigenCode, int pEmailNummer) {
        return linkFuerEmailBestaetigungIntern(pZeitDif, pHvJahr, pHvNummer, pDatenbereich, pSprache, pTest, pEmailBestaetigenCode, pEmailNummer, true);
    }

    public String linkFuerPortalStart(long pZeitDif, int pHvJahr, String pHvNummer, String pDatenbereich, String pSprache, String pTest, boolean pPermanentPortal) {

        boolean lAlternativ = false;
        if (!dbBundle.paramServer.pLocalPraefixLinkAlternativ.isEmpty()) {
            lAlternativ = true;
        }

        return linkAllgemeinerTeil(pZeitDif, pHvJahr, pHvNummer, pDatenbereich, pSprache, pTest, lAlternativ, pPermanentPortal);
    }

    public String linkKurzFuerPortalStart() {

        if (dbBundle.param.paramPortal.kurzLinkPortal.trim().isEmpty()) {return "";}
        
        return linkKurzAllgemeinerTeil();
    }

    /**
     * pZeitDif=Differenzzeit in long für zeit=; falls =0, dann kein zeit=
     * pHVJahr!=0 => HvJahr, HvNummer und Datenbereich werden in Link mit aufgenommen
     * 
     * pAlternativ=true => pLocalPraefixLinkAlternativ wird verwendet
     */
    private String linkKurzAllgemeinerTeil() {
        String lLink = "";
        lLink=dbBundle.param.paramPortal.kurzLinkPortal.trim();
        return lLink;
    }

    /**
     * pZeitDif=Differenzzeit in long für zeit=; falls =0, dann kein zeit=
     * pHVJahr!=0 => HvJahr, HvNummer und Datenbereich werden in Link mit aufgenommen
     * 
     * pAlternativ=true => pLocalPraefixLinkAlternativ wird verwendet
     */
    private String linkAllgemeinerTeil(long pZeitDif, int pHvJahr, String pHvNummer, String pDatenbereich, String pSprache, String pTest, boolean pAlternativ, boolean pPermanentPortal) {
        String lLink = "";

        boolean vorgaenger = false;

        String lStart = "";
        if (pAlternativ == false) {
            lStart = domainLink() /*dbBundle.paramServer.pLocalPraefixLink*/;
        } else {
            lStart = dbBundle.paramServer.pLocalPraefixLinkAlternativ;
        }

        if (pPermanentPortal) {
            lLink = lStart + "/meetingportal/" + dbBundle.param.paramPortal.designKuerzel + "/mitgliederportal.xhtml?";
        } else {
            lLink = lStart + "/meetingportal/" + dbBundle.param.paramPortal.designKuerzel + "/portal.xhtml?";
        }

        if (pZeitDif != 0) {
            if (vorgaenger == true) {
                lLink += "&";
            }
            vorgaenger = true;
            lLink += "zeit=" + CaDatumZeit.DatumZeitStringFromLong(CaDatumZeit.zeitStempelMS() + pZeitDif);
        }

        if (pHvJahr != 0) {
            if (vorgaenger == true) {
                lLink += "&";
            }
            vorgaenger = true;
            lLink += "hvjahr=" + Integer.toString(pHvJahr) + "&hvnummer=" + pHvNummer + "&datenbereich=" + pDatenbereich;
        }

        if (vorgaenger == true) {
            lLink += "&";
        }
        vorgaenger = true;
        lLink += "mandant=" + dbBundle.clGlobalVar.getMandantString();

        lLink += "&sprache=" + pSprache;

        if (pTest.equals("1")) {
            lLink += "&test=1";
        }
        if (pTest.equals("2")) {
            lLink += "&test=2";
        }
        if (pPermanentPortal) {
            lLink += "&per=1";
        }
        return lLink;
    }

    private String linkFuerEmailBestaetigungIntern(long pZeitDif, int pHvJahr, String pHvNummer, String pDatenbereich, String pSprache, String pTest, String pEmailBestaetigenCode, int pEmailNummer,
            boolean appVerwendet) {

        String linkArt = "";
        switch (pEmailNummer) {
        case 1:
            linkArt = Integer.toString(KonstLinkArt.EMAILBESTAETIGENfuerEMAIL1);
            break;
        case 2:
            linkArt = Integer.toString(KonstLinkArt.EMAILBESTAETIGENfuerEMAIL2);
            break;
        }

        String lRestLink = "&la=" + linkArt + "&bestaetigungscode=" + pEmailBestaetigenCode;

        String lLink = linkAllgemeinerTeil(pZeitDif, pHvJahr, pHvNummer, pDatenbereich, pSprache, pTest, false, false) + lRestLink;

        if (!dbBundle.paramServer.pLocalPraefixLinkAlternativ.isEmpty()) {
            lLink += "\n\n" + linkAllgemeinerTeil(pZeitDif, pHvJahr, pHvNummer, pDatenbereich, pSprache, pTest, true, false) + lRestLink;
        }

        CaBug.druckeLog("iLink=" + lLink, logDrucken, 1);
        return lLink;
    }

    public String linkFuerPasswortVergessen(long pZeitDif, int pHvJahr, String pHvNummer, String pDatenbereich, String pSprache, String pTest, String pEmailBestaetigenCode, boolean pPermanentPortal) {
        return linkFuerPasswortVergessenIntern(pZeitDif, pHvJahr, pHvNummer, pDatenbereich, pSprache, pTest, pEmailBestaetigenCode, false, pPermanentPortal);
    }

    public String linkFuerPasswortVergessenApp(long pZeitDif, int pHvJahr, String pHvNummer, String pDatenbereich, String pSprache, String pTest, String pEmailBestaetigenCode) {
        return linkFuerPasswortVergessenIntern(pZeitDif, pHvJahr, pHvNummer, pDatenbereich, pSprache, pTest, pEmailBestaetigenCode, true, false);
    }

    private String linkFuerPasswortVergessenIntern(long pZeitDif, int pHvJahr, String pHvNummer, String pDatenbereich, String pSprache, String pTest, String pEmailBestaetigenCode,
            boolean appVerwendet, boolean pPermanentPortal) {

        String linkArt = Integer.toString(KonstLinkArt.PASSWORTVERGESSEN);
        if (appVerwendet) {
            linkArt = Integer.toString(KonstLinkArt.PASSWORTVERGESSENAPP);
        }

        String lRestLink = "&la=" + linkArt + "&bestaetigungscode=" + pEmailBestaetigenCode;

        String lLink = linkAllgemeinerTeil(pZeitDif, pHvJahr, pHvNummer, pDatenbereich, pSprache, pTest, false, pPermanentPortal) + lRestLink;

        if (!dbBundle.paramServer.pLocalPraefixLinkAlternativ.isEmpty()) {
            lLink += "\n\n" + linkAllgemeinerTeil(pZeitDif, pHvJahr, pHvNummer, pDatenbereich, pSprache, pTest, true, false) + lRestLink;
        }

        CaBug.druckeLog("iLink=" + lLink, logDrucken, 1);
        return lLink;
    }

    public String linkFuerPDFAnzeigen(String pEinmalkey, long pIdent) {
        String hLink = /*dbBundle.paramServer.pLocalPraefixLink*/ domainLink() + "/meetingport/aPdfshow.xhtml?&ident=" + Long.toString(pIdent) + "&key=" + pEinmalkey;
        return hLink;
    }

    public String domainLink() {
        String hDomain = dbBundle.paramServer.pLocalPraefixLink;
        if (!dbBundle.param.paramPortal.subdomainPortal.isEmpty()) {
            String subDomain = dbBundle.param.paramPortal.subdomainPortal;
            hDomain = hDomain.replace(dbBundle.paramServer.domainLinkErsetzen, dbBundle.paramServer.domainLinkErsetzenDurch + subDomain);
//            hDomain = hDomain.replace("portal03", "bo03" + subDomain);
        }
        return hDomain;
    }
}
