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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungSetM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class BlMAbstimmung {

    private int logDrucken = 3;

    private @Inject EclAbstimmungSetM eclAbstimmungSetM;
    private @Inject EclAbstimmungenListeM eclAbstimmungenListeM;

    /*************************************Live-Abstimmung*******************************************************/
    /**Benötigt kein EclDbM*/
    public void leseAbstimmungsliste(int pGattungen[]) {
        CaBug.druckeLog("", logDrucken, 1);

        /*Vorzubereitende Gattung bestimmen*/
        int gattungVorhanden[] = pGattungen;

        /*Start-Version setzen*/
        eclAbstimmungSetM.setVersionAbstimmungenStart(eclAbstimmungSetM.getVersionAbstimmungenAktuell());

        /*Abstimmungen in EclAbstimmungM übertragen - nur die, für die gattungVorhanden*/
        EclAbstimmung[] lAbstimmungQuelleArray = eclAbstimmungSetM.getAbstimmungSet().abstimmungen;
        List<EclAbstimmungM> lAbstimmungenListe = new LinkedList<>();
        EclAbstimmungM lAbstimmungM = null;

        for (int i3 = 0; i3 < lAbstimmungQuelleArray.length; i3++) {
            CaBug.druckeLog("i3=" + i3, logDrucken, 10);
            EclAbstimmung lAbstimmung = lAbstimmungQuelleArray[i3];
            int gattungAktiv = -1;
            for (int i4 = 1; i4 <= 5; i4++) {
                if (gattungVorhanden[i4 - 1] == 1 && lAbstimmung.stimmberechtigteGattungen[i4 - 1] == 1) {
                    gattungAktiv = 1;
                }
            }
            if (gattungAktiv == 1) {
                CaBug.druckeLog("aufgenommen", logDrucken, 10);
                lAbstimmungM = new EclAbstimmungM();
                lAbstimmungM.copyFrom(lAbstimmung);
                lAbstimmungenListe.add(lAbstimmungM);
            }
        }

        eclAbstimmungenListeM.setAbstimmungenListeM(lAbstimmungenListe);

        /*Gegenanträge - bei Abstimmungen nicht gesondert berücksichtigt*/
        List<EclAbstimmungM> lGegenantraegeListe = new LinkedList<>();
        eclAbstimmungenListeM.setGegenantraegeListeM(lGegenantraegeListe);
    }

    //	/**Benötigt EclDbM*/
    //	public void speichereAbstimmungen() {
    //		BlAbstimmung blAbstimmung=new BlAbstimmung(eclDbM.getDbBundle());
    //		EclAbstimmungSet lAbstimmungset=eclAbstimmungSetM.getAbstimmungSet();
    //		
    //		blAbstimmung.aktivenAbstimmungsblockSortierenNach=lAbstimmungset.aktivenAbstimmungsblockSortierenNach;
    //		blAbstimmung.aktiverAbstimmungsblock=lAbstimmungset.aktiverAbstimmungsblock;
    //		blAbstimmung.aktiverAbstimmungsblockIstElektronischAktiv=lAbstimmungset.aktiverAbstimmungsblockIstElektronischAktiv;
    //		blAbstimmung.abstimmungenZuAktivenBlock=lAbstimmungset.abstimmungenZuAktivenBlock;
    //		blAbstimmung.abstimmungen=lAbstimmungset.abstimmungen;
    //
    //		List<EclAbstimmungM> lAbstimmungenListe=eclAbstimmungenListeM.getAbstimmungenListeM();
    //
    //		if (eclParamM.getParam().paramPortal.nurRawLiveAbstimmung==1) {
    //
    //			blAbstimmung.starteSpeichernFuerMeldungsIdent(eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().get(0).getMeldungsIdent());
    //			for (int i=0;i<lAbstimmungenListe.size();i++) {
    //				EclAbstimmungM lAbstimmungM=lAbstimmungenListe.get(i);
    //				if (lAbstimmungM.getGewaehlt()==null) {
    //					lAbstimmungM.setGewaehlt("");
    //				}
    //				if (!lAbstimmungM.isUeberschrift() && lAbstimmungM.getGewaehlt()!=null){
    //					int abstimmungIdent=lAbstimmungM.getIdent();
    //					int abstimmungVerhalten=0;
    //					switch (lAbstimmungM.getGewaehlt()){
    //					case "J":abstimmungVerhalten=KonstStimmart.ja;break;
    //					case "N":abstimmungVerhalten=KonstStimmart.nein;break;
    //					case "E":abstimmungVerhalten=KonstStimmart.enthaltung;break;
    //					case "U":abstimmungVerhalten=KonstStimmart.ungueltig;break;
    //					}
    //					CaBug.druckeLog("abstimmungVerhalten="+abstimmungVerhalten, logDrucken, 10);
    //					blAbstimmung.setzeMarkierungZuAbstimmungsIdent(abstimmungVerhalten, abstimmungIdent);
    //				}
    //			}
    //			blAbstimmung.beendeSpeichernFuerMeldung();
    //		}
    //		
    //
    //	}

    /*************************************************Weisungen*************************************************************/
    /**Benötigt kein EclDbM
     * int gattungVorhanden[]=new int[6]; jeweils 1 falls die jeweilige Gattung in die Abstimmung mit aufgenommen werden soll
     * pSicht gemäß KonstWeisungserfassungSicht*/
    public void leseWeisungsliste(int gattungVorhanden[], int pSkIst, int pSicht) {
        leseWeisungsliste(gattungVorhanden, pSkIst, pSicht, false);
    }
        
    public void leseWeisungsliste(int gattungVorhanden[], int pSkIst, int pSicht, boolean pPreview) {

        /*Start-Version setzen*/
        eclAbstimmungSetM.setVersionWeisungenStart(eclAbstimmungSetM.getVersionWeisungenAktuell());
        CaBug.druckeLog("eclAbstimmungSetM.getVersionWeisungenAktuell()="+eclAbstimmungSetM.getVersionWeisungenAktuell(), logDrucken, 10);
        /*Eigentliche Agenda*/
        EclAbstimmung[] lAbstimmungQuelleArray = null;
        if (pSicht==KonstWeisungserfassungSicht.portalWeisungserfassung) {
            if (pPreview) {
                lAbstimmungQuelleArray = eclAbstimmungSetM
                        .getAbstimmungSet().agendaArrayPortalWeisungserfassungPreview[0];
            }
            else {
                lAbstimmungQuelleArray = eclAbstimmungSetM
                        .getAbstimmungSet().agendaArrayPortalWeisungserfassung[0];
            }
        }
        if (pSicht==KonstWeisungserfassungSicht.interneWeisungserfassung) {
            lAbstimmungQuelleArray = eclAbstimmungSetM
                    .getAbstimmungSet().agendaArrayInternWeisungserfassung[0];
        }
        
        eclAbstimmungenListeM.setAbstimmungenListeM(
                arbeiteAbstimmungenFuerWeisungenDurch(lAbstimmungQuelleArray, gattungVorhanden, pSkIst));

        /*Gegenanträge */
        CaBug.druckeLog("Start Gegenanträge pPreview="+pPreview, logDrucken, 10);
        if (pSicht==KonstWeisungserfassungSicht.portalWeisungserfassung) {
            CaBug.druckeLog("pSicht==portalWeisungserfassung", logDrucken, 10);
            if (pPreview) {
                lAbstimmungQuelleArray = eclAbstimmungSetM
                        .getAbstimmungSet().gegenantraegeArrayPortalWeisungserfassungPreview[0];
                CaBug.druckeLog("Gegenanträge lesen pPreview==true, Länge="+lAbstimmungQuelleArray.length, logDrucken, 10);
            }
            else {
                lAbstimmungQuelleArray = eclAbstimmungSetM
                        .getAbstimmungSet().gegenantraegeArrayPortalWeisungserfassung[0];
            }
        }
        if (pSicht==KonstWeisungserfassungSicht.interneWeisungserfassung) {
            lAbstimmungQuelleArray = eclAbstimmungSetM
                    .getAbstimmungSet().gegenantraegeArrayInternWeisungserfassung[0];
        }
//        lAbstimmungQuelleArray = eclAbstimmungSetM.getAbstimmungSet().gegenantraegeArrayPortalWeisungserfassung[0];
        eclAbstimmungenListeM.setGegenantraegeListeM(
                arbeiteAbstimmungenFuerWeisungenDurch(lAbstimmungQuelleArray, gattungVorhanden, pSkIst));

    }

    /**Abstimmungen in EclAbstimmungM übertragen - nur die, für die gattungVorhanden, und Weisungsart (Briefwahl, SRV etc.) aktiv*/
    private List<EclAbstimmungM> arbeiteAbstimmungenFuerWeisungenDurch(EclAbstimmung[] lAbstimmungQuelleArray,
            int gattungVorhanden[], int pSkIst) {

        List<EclAbstimmungM> lAbstimmungenListe = new LinkedList<>();
        EclAbstimmungM lAbstimmungM = null;

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
                    lAbstimmungM = new EclAbstimmungM();
                    lAbstimmungM.copyFrom(lAbstimmung);
                    lAbstimmungenListe.add(lAbstimmungM);
                }
            }
        }
        return lAbstimmungenListe;
    }

//    /**Benötigt EclDbM*/
//    public void speichereWeisungen() {
//        /*Unklar für was das mal war*/
//        BlAbstimmung blAbstimmung = new BlAbstimmung(eclDbM.getDbBundle());
//        EclAbstimmungSet lAbstimmungset = eclAbstimmungSetM.getAbstimmungSet();
//
//        blAbstimmung.aktivenAbstimmungsblockSortierenNach = lAbstimmungset.aktivenAbstimmungsblockSortierenNach;
//        blAbstimmung.aktiverAbstimmungsblock = lAbstimmungset.aktiverAbstimmungsblock;
//        blAbstimmung.aktiverAbstimmungsblockIstElektronischAktiv = lAbstimmungset.aktiverAbstimmungsblockIstElektronischAktiv;
//        blAbstimmung.abstimmungenZuAktivenBlock = lAbstimmungset.abstimmungenZuAktivenBlock;
//        blAbstimmung.abstimmungen = lAbstimmungset.abstimmungen;
//
//        List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();
//
//        if (eclParamM.getParam().paramPortal.nurRawLiveAbstimmung == 1) {
//
//            blAbstimmung.starteSpeichernFuerMeldungsIdent(
//                    eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().get(0).getMeldungsIdent());
//            for (int i = 0; i < lAbstimmungenListe.size(); i++) {
//                EclAbstimmungM lAbstimmungM = lAbstimmungenListe.get(i);
//                if (!lAbstimmungM.isUeberschrift() && lAbstimmungM.getGewaehlt() != null) {
//                    int abstimmungIdent = lAbstimmungM.getIdent();
//                    int abstimmungVerhalten = 0;
//                    switch (lAbstimmungM.getGewaehlt()) {
//                    case "J":
//                        abstimmungVerhalten = KonstStimmart.ja;
//                        break;
//                    case "N":
//                        abstimmungVerhalten = KonstStimmart.nein;
//                        break;
//                    case "E":
//                        abstimmungVerhalten = KonstStimmart.enthaltung;
//                        break;
//                    case "U":
//                        abstimmungVerhalten = KonstStimmart.ungueltig;
//                        break;
//                    }
//                    blAbstimmung.setzeMarkierungZuAbstimmungsIdent(abstimmungVerhalten, abstimmungIdent);
//                }
//            }
//            blAbstimmung.beendeSpeichernFuerMeldung();
//        }
//
//    }

}
