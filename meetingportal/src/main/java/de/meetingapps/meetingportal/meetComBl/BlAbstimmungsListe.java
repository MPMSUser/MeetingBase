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

import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungSet;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;

/**Funktionen zum Benutzen der Abstimmungslisten*/
public class BlAbstimmungsListe {

    
    public List<EclAbstimmung> rcAgenda=null;
    public List<EclAbstimmung> rcGegenantraege=null;
    
    /**Vorbereitung z.B. über:
     * pAbstimmungSet=eclAbstimmungSetM.getAbstimmungSet();
     */
    public void leseWeisungsliste(EclAbstimmungSet pAbstimmungSet, int gattungVorhanden[], int pSkIst, int pSicht, boolean pPreview) {

        /*Eigentliche Agenda*/
        EclAbstimmung[] lAbstimmungQuelleArray = null;
        if (pSicht==KonstWeisungserfassungSicht.portalWeisungserfassung) {
            if (pPreview) {
                lAbstimmungQuelleArray = pAbstimmungSet.agendaArrayPortalWeisungserfassungPreview[0];
            }
            else {
                lAbstimmungQuelleArray = pAbstimmungSet.agendaArrayPortalWeisungserfassung[0];
            }
        }
        if (pSicht==KonstWeisungserfassungSicht.interneWeisungserfassung) {
            lAbstimmungQuelleArray = pAbstimmungSet.agendaArrayInternWeisungserfassung[0];
        }
        
        rcAgenda=arbeiteAbstimmungenFuerWeisungenDurch(lAbstimmungQuelleArray, gattungVorhanden, pSkIst);

        /*Gegenanträge */
        if (pSicht==KonstWeisungserfassungSicht.portalWeisungserfassung) {
            if (pPreview) {
                lAbstimmungQuelleArray = pAbstimmungSet.gegenantraegeArrayPortalWeisungserfassungPreview[0];
            }
            else {
                lAbstimmungQuelleArray = pAbstimmungSet.gegenantraegeArrayPortalWeisungserfassung[0];
            }
        }
        if (pSicht==KonstWeisungserfassungSicht.interneWeisungserfassung) {
            lAbstimmungQuelleArray = pAbstimmungSet.gegenantraegeArrayInternWeisungserfassung[0];
        }
//        lAbstimmungQuelleArray = eclAbstimmungSetM.getAbstimmungSet().gegenantraegeArrayPortalWeisungserfassung[0];
        rcGegenantraege=arbeiteAbstimmungenFuerWeisungenDurch(lAbstimmungQuelleArray, gattungVorhanden, pSkIst);

    }

    /**Abstimmungen in EclAbstimmungM übertragen - nur die, für die gattungVorhanden, und Weisungsart (Briefwahl, SRV etc.) aktiv*/
    private List<EclAbstimmung> arbeiteAbstimmungenFuerWeisungenDurch(EclAbstimmung[] lAbstimmungQuelleArray,
            int gattungVorhanden[], int pSkIst) {

        List<EclAbstimmung> lAbstimmungenListe = new LinkedList<>();

        if (lAbstimmungQuelleArray!=null) {
            for (int i3 = 0; i3 < lAbstimmungQuelleArray.length; i3++) {
                EclAbstimmung lAbstimmung = lAbstimmungQuelleArray[i3];
                int gattungAktiv = -1;
                for (int i4 = 1; i4 <= 5; i4++) {
                    if (gattungVorhanden[i4] == 1 && lAbstimmung.stimmberechtigteGattungen[i4 - 1] == 1) {
                        gattungAktiv = 1;
                    }
                }
                int skIstAktiv = -1;
                if (lAbstimmung.aktivBeiSRV == 1 && pSkIst == KonstSkIst.srv) {
                    skIstAktiv = 1;
                }
                if (lAbstimmung.aktivBeiBriefwahl == 1 && pSkIst == KonstSkIst.briefwahl) {
                    skIstAktiv = 1;
                }
                if (lAbstimmung.aktivBeiKIAVDauer == 1 && (pSkIst == KonstSkIst.kiav || pSkIst == KonstSkIst.dauervollmacht
                        || pSkIst == KonstSkIst.organisatorisch)) {
                    skIstAktiv = 1;
                }

                if (lAbstimmung.aktivFragen == 1 && pSkIst == KonstSkIst.FRAGEN) {
                    skIstAktiv = 1;
                }
                if (lAbstimmung.aktivAntraege == 1 && pSkIst == KonstSkIst.ANTRAEGE) {
                    skIstAktiv = 1;
                }
                if (lAbstimmung.aktivWidersprueche == 1 && pSkIst == KonstSkIst.WIDERSPRUECHE) {
                    skIstAktiv = 1;
                }
                if (lAbstimmung.aktivWortmeldungen == 1 && pSkIst == KonstSkIst.WORTMELDUNGEN) {
                    skIstAktiv = 1;
                }
                if (lAbstimmung.aktivSonstMitteilungen == 1 && pSkIst == KonstSkIst.SONSTMITTEILUNGEN) {
                    skIstAktiv = 1;
                }
                if (lAbstimmung.aktivBotschaftenEinreichen == 1 && pSkIst == KonstSkIst.BOTSCHAFTEN_EINREICHEN) {
                    skIstAktiv = 1;
                }


                if (gattungAktiv == 1 && skIstAktiv == 1) {
                    lAbstimmungenListe.add(lAbstimmung);
                }
            }
        }
        return lAbstimmungenListe;
    }

}
