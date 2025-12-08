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
package de.meetingapps.meetingportal.meetingport;

import de.meetingapps.meetingportal.meetComAllg.CaLeseDatei;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComEclM.EclAnredeListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclGastM;
import de.meetingapps.meetingportal.meetComEclM.EclMeldungenMeldungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@RequestScoped
@Named
@Deprecated
public class AControllerGGNeuerGastQuittung {

    @Inject
    BaMailM baMailm;
    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    private EclDbM eclDbM;
    @Inject
    EclAnredeListeM lAnredeListeM;
    @Inject
    EclGastM lGastM;
    @Inject
    EclMeldungenMeldungenListeM lMeldungenMeldungenListeM;
    @Inject
    EclParamM eclParamM;

    public String doDrucken() {
        if (!aFunktionen.pruefeStart("aGGNeuerGastQuittung")) {
            return "aDlgFehler";
        }

        String dateinr = aDlgVariablen.getGastkartePdfNrS();

        byte[] bytes = null;
        CaLeseDatei caLeseDatei = new CaLeseDatei();
        bytes = caLeseDatei.alsBytes(
                eclParamM.getClGlobalVar().lwPfadAllgemein + "\\meetingausdrucke\\zutrittsdokument" + dateinr + ".pdf");

        FacesContext faces = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();

        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(bytes);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println("Fehler: Attachment anzeigen: " + e);
        }

        response.setHeader("Content-Disposition", "attachment; filename=zutrittsdokument.pdf");
        faces.responseComplete();

        aFunktionen.setzeEnde("aGGNeuerGastQuittung", true, false);

        return "";

    }

    public String doWeiter() {
        int i;

        if (!aFunktionen.pruefeStart("aGGNeuerGastQuittung")) {
            return "aDlgFehler";
        }

        /*MasterGast wieder einlesen für Anzeige*/
        /*TODO _Gastverwaltung: Berücksichtigen, dass sich die Gastdaten möglicherweise mittlerweile verändert haben !!!*/
        EclMeldung lMeldung = new EclMeldung();

        eclDbM.openAll();

        lMeldung.meldungsIdent = aDlgVariablen.getIdentMasterGast();
        eclDbM.getDbBundle().dbMeldungen.leseZuMeldungsIdent(lMeldung);

        lMeldung = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];

        /*Achtung: noch nicht berücksichtigt, dass storniert, mehrere ZutrittsIdents, etc. ....*/
        eclDbM.getDbBundle().dbZutrittskarten.readZuMeldungsIdentGast(lMeldung.meldungsIdent);
        lMeldung.zutrittsIdent = eclDbM.getDbBundle().dbZutrittskarten.ergebnisPosition(0).zutrittsIdent;

        lGastM.init();
        lGastM.copyFrom(eclDbM.getDbBundle().dbMeldungen.meldungenArray[0]);

        /*Anrede*/
        lGastM.setAnredeText("");
        if (lGastM.getAnrede().compareTo("0") != 0) {
            for (i = 0; i < lAnredeListeM.getAnredeListeM().size(); i++) {
                if (lAnredeListeM.getAnredeListeM().get(i).getAnredennr().equals(lGastM.getAnrede())) {
                    lGastM.setAnredeText(lAnredeListeM.getAnredeListeM().get(i).getAnredentext());
                }
            }
        }

        /*Zugeordnete einlesen - Überprüfen ob "Gruppen-Gastanmeldung*/
        lMeldungenMeldungenListeM.fuelleListe(eclDbM.getDbBundle(), lMeldung);
        lGastM.setMeldungenMeldungenListe(lMeldungenMeldungenListeM.getMeldungenMeldungenListeM());
        if (lGastM.getMeldungenMeldungenListe().size() > 0) {
            aDlgVariablen.setGruppenausstellung(true);
        } else {
            aDlgVariablen.setGruppenausstellung(false);
        }

        eclDbM.closeAll();
        return aFunktionen.setzeEnde("aGGGastStatus", true, false);
    }

    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

}
