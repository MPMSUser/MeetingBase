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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclKIAVM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import de.meetingapps.meetingportal.meetingport.AControllerKIAVBestaetigung;
import de.meetingapps.meetingportal.meetingport.AControllerWeisungBestaetigung;
import de.meetingapps.meetingportal.meetingport.ADlgVariablen;
import de.meetingapps.meetingportal.meetingport.AFunktionen;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class BlMMassenWillenserklaerungen {

    @Inject
    private EclDbM eclDbM;
    @Inject
    private AFunktionen aFunktionen;
    @Inject
    private EclKIAVM eclKIAVM;
    @Inject
    private EclAbstimmungenListeM eclAbstimmungenListeM;
    @Inject
    private EclTeilnehmerLoginM eclTeilnehmerLoginM;
    @Inject
    private ADlgVariablen aDlgVariablen;
    @Inject
    private EclParamM eclParamM;
    @Inject
    private AControllerKIAVBestaetigung aControllerKIAVBestaetigung;
    @Inject
    private AControllerWeisungBestaetigung aControllerWeisungBestaetigung;

    
    /**Für ku168 AG:
     * Alle Treuhänder (=Gattung 2) werden
     * > Angemeldet
     * > eine EK ausgestellt
     * > auf die SammelIdent mit Vollmacht KIAV gebucht
     */
    public void anmeldenku168(int sammelIdent) {

        eclDbM.openAll();

        eclDbM.getDbBundle().dbMeldungen.leseZuIdent(sammelIdent);
        EclMeldung sammelMeldung = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];
        eclKIAVM.copyFrom(sammelMeldung);
        System.out.println("gattung sammelkarte=" + eclKIAVM.getGattung());

        aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), 2, KonstWeisungserfassungSicht.interneWeisungserfassung,
                KonstSkIst.kiav);

        for (int i = 0; i < eclAbstimmungenListeM.getAbstimmungenListeM().size(); i++) {
            eclAbstimmungenListeM.getAbstimmungenListeM().get(i).setGewaehlt("");
        }
        for (int i = 0; i < eclAbstimmungenListeM.getGegenantraegeListeM().size(); i++) {
            eclAbstimmungenListeM.getGegenantraegeListeM().get(i).setMarkiert(false);
            eclAbstimmungenListeM.getGegenantraegeListeM().get(i).setGewaehlt("");
        }

        EclAktienregister lAktienregister = new EclAktienregister();
        lAktienregister.gattungId = 2;
        eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(lAktienregister);

        EclAktienregister[] alleAktienregister = eclDbM.getDbBundle().dbAktienregister.ergebnisArray;
        int anzAktienregister = alleAktienregister.length;

        eclDbM.closeAll();

        for (int i = 0; i < anzAktienregister; i++) {

            EclAktienregister aktuellAktienregister = alleAktienregister[i];
            String aktionaersnummer = aktuellAktienregister.aktionaersnummer;

            System.out.println("i=" + i + " aktionaersnummer=" + aktionaersnummer);

            eclTeilnehmerLoginM.setAnmeldeIdentPersonenNatJur(0);
            eclTeilnehmerLoginM.setAnmeldeAktionaersnummer(aktionaersnummer);

            aDlgVariablen.clearDlg();

            aFunktionen.setStartPruefen(0);

            aDlgVariablen.setAusgewaehlteHauptAktion("1");
            aDlgVariablen.setAusgewaehlteAktion("7");

            System.out.println("A");
            aControllerKIAVBestaetigung.doErteilen();
            System.out.println("B");
            if (aDlgVariablen.getFehlerNr() < 0) {
                CaBug.drucke("DControllerMenue anmeldenku168 001 Fehler=" + aDlgVariablen.getFehlerNr());
            }

        }

        eclDbM.openAll();
        eclDbM.getDbBundle().dbMeldungen.leseAlleMeldungenGattung(2);
        EclMeldung[] alleMeldungen = eclDbM.getDbBundle().dbMeldungen.meldungenArray;
        int anzMeldungen = alleMeldungen.length;

        int neueZutrittsIdentNr = eclParamM.getParam().paramNummernkreise.vonSubEintrittskartennummer[2][1];
        /*2=Gattung, 1=Manuell*/
        System.out.println("neueZutrittsIdentNr=" + neueZutrittsIdentNr);

        for (int i = 0; i < anzMeldungen; i++) {
            EclMeldung aktuelleMeldung = alleMeldungen[i];
            if (aktuelleMeldung.meldungstyp == 3 && aktuelleMeldung.zutrittsIdent.isEmpty()
                    && aktuelleMeldung.meldungEnthaltenInSammelkarte == 4) {

                String neueZutrittsIdentString = Integer.toString(neueZutrittsIdentNr);
                EclZutrittsIdent eclZutrittsIdent = new EclZutrittsIdent();
                BlNummernformen blNummernformen = new BlNummernformen(eclDbM.getDbBundle());
                eclZutrittsIdent.zutrittsIdent = blNummernformen.formatiereEKNr(neueZutrittsIdentString);
                eclZutrittsIdent.zutrittsIdentNeben = "00";

                BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
                lWillenserklaerung.piEclMeldungAktionaer = aktuelleMeldung;
                lWillenserklaerung.pWillenserklaerungGeberIdent = -1;

                lWillenserklaerung.pZutrittsIdent = eclZutrittsIdent;
                lWillenserklaerung.pVersandartEK = 0;

                lWillenserklaerung.neueZutrittsIdentZuMeldung(eclDbM.getDbBundle());
                System.out.println(lWillenserklaerung.rcIstZulaessig + " " + lWillenserklaerung.rcGrundFuerUnzulaessig);
                neueZutrittsIdentNr++;
            }
        }
        eclDbM.closeAll();
        //
        //		/*Massentest*/
        //		if (anzahl!=0){
        //			int stufe=100;
        //			System.out.println("Start="+CaDatumZeit.DatumZeitStringFuerDatenbank());
        //
        //			eclDbM.openAll();
        //			for (i=10000;i<anzahl+10000;i++){
        //
        //				String aktionaersnummer=Integer.toString(i);
        //				while (aktionaersnummer.length()<3){aktionaersnummer="0"+aktionaersnummer;}
        //
        //				eclTeilnehmerLoginM.setAnmeldeIdentPersonenNatJur(0);
        //				eclTeilnehmerLoginM.setAnmeldeAktionaersnummer(aktionaersnummer);
        //				aDlgVariablen.clearDlg();
        //				aDlgVariablen.setAusgewaehlteHauptAktion("1"); //Neuanmeldung
        //				aDlgVariablen.setAusgewaehlteAktion("1"); //1 EK
        //				aDlgVariablen.setEintrittskarteVersandart("3");
        //				aControllerEintrittskarte.anlegenAktionaerEK(eclDbM.getDbBundle());
        //
        //				if ((i-100) % stufe ==0){
        //					System.out.println((i-100-offset)+"angemeldet "+CaDatumZeit.DatumZeitStringFuerDatenbank());
        //				}
        //			}
        //			eclDbM.closeAll();
        //		}

        aFunktionen.setStartPruefen(1);
        System.out.println("Ende=" + CaDatumZeit.DatumZeitStringFuerDatenbank());
    }

 


    /**Für Governance & Values: meldet alle noch nicht angemeldeten Bestände an und bucht sie
     * mit Weisung . auf den Stimmrechtsvertreter Internet.
     */
    public void anmeldenGovVal() {

        eclDbM.openAll();

        aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), 1, KonstWeisungserfassungSicht.portalWeisungserfassung,
                KonstSkIst.srv);

        for (int i = 0; i < eclAbstimmungenListeM.getAbstimmungenListeM().size(); i++) {
            eclAbstimmungenListeM.getAbstimmungenListeM().get(i).setGewaehlt(".");
        }
        for (int i = 0; i < eclAbstimmungenListeM.getGegenantraegeListeM().size(); i++) {
            eclAbstimmungenListeM.getGegenantraegeListeM().get(i).setMarkiert(false);
            eclAbstimmungenListeM.getGegenantraegeListeM().get(i).setGewaehlt("");
        }

        eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(null);

        EclAktienregister[] alleAktienregister = eclDbM.getDbBundle().dbAktienregister.ergebnisArray;
        int anzAktienregister = alleAktienregister.length;

        eclDbM.closeAll();

        for (int i = 0; i < anzAktienregister; i++) {

            EclAktienregister aktuellAktienregister = alleAktienregister[i];
            String aktionaersnummer = aktuellAktienregister.aktionaersnummer;

            System.out.println("i=" + i + " aktionaersnummer=" + aktionaersnummer);

            eclTeilnehmerLoginM.setAnmeldeIdentPersonenNatJur(0);
            eclTeilnehmerLoginM.setAnmeldeAktionaersnummer(aktionaersnummer);

            aDlgVariablen.clearDlg();

            aFunktionen.setStartPruefen(0);

            aDlgVariablen.setAusgewaehlteHauptAktion("1");
            aDlgVariablen.setAusgewaehlteAktion("4");

            System.out.println("A");
            aControllerWeisungBestaetigung.doErteilen();
            System.out.println("B");
            if (aDlgVariablen.getFehlerNr() < 0) {
                CaBug.drucke("DControllerMenue anmeldenGovVal 001 Fehler=" + aDlgVariablen.getFehlerNr());
            }

        }

        aFunktionen.setStartPruefen(1);
        System.out.println("Ende=" + CaDatumZeit.DatumZeitStringFuerDatenbank());
    }

    
    
    
}
