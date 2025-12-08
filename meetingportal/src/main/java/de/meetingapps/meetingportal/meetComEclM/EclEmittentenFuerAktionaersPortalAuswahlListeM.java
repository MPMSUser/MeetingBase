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
package de.meetingapps.meetingportal.meetComEclM;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvMandanten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclEmittentenFuerAktionaersPortalAuswahlListeM implements Serializable {
    private static final long serialVersionUID = -4945479409331517295L;

    private List<EclEmittentenFuerAktionaersPortalAuswahlM> emittentenListe = null;

    public EclEmittentenFuerAktionaersPortalAuswahlListeM() {
        emittentenListe = new LinkedList<EclEmittentenFuerAktionaersPortalAuswahlM>();
    }

    public boolean fuelleEmittentenListe(DbBundle pDbBundle, String pBaseLink, boolean pTest) {
        emittentenListe = new LinkedList<EclEmittentenFuerAktionaersPortalAuswahlM>();

        BvMandanten bvMandanten = new BvMandanten();
        bvMandanten.liefereMandantenFuerAktionaersPortalauswahl(pDbBundle, "0");

        for (int i = 0; i < bvMandanten.rcEmittentenListe.size(); i++) {
            EclEmittenten lEmittenten = bvMandanten.rcEmittentenListe.get(i);
            EclEmittentenFuerAktionaersPortalAuswahlM lEclEmittentenFurAktionaersPortalAuswahlM = new EclEmittentenFuerAktionaersPortalAuswahlM();
            lEclEmittentenFurAktionaersPortalAuswahlM
                    .setMandant(CaString.fuelleLinksNull(Integer.toString(lEmittenten.mandant), 3));
            lEclEmittentenFurAktionaersPortalAuswahlM.setHvJahr(Integer.toString(lEmittenten.hvJahr));
            lEclEmittentenFurAktionaersPortalAuswahlM.setHvNummer(lEmittenten.hvNummer);
            lEclEmittentenFurAktionaersPortalAuswahlM.setDbArt(lEmittenten.dbArt);
            lEclEmittentenFurAktionaersPortalAuswahlM.setBezeichnungKurz(lEmittenten.bezeichnungKurz);
            lEclEmittentenFurAktionaersPortalAuswahlM.setHvDatum(lEmittenten.hvDatum);

            lEclEmittentenFurAktionaersPortalAuswahlM.setPortalAktuellAktiv(lEmittenten.portalAktuellAktiv == 1);

            lEclEmittentenFurAktionaersPortalAuswahlM.setPortalSpracheDeutsch((lEmittenten.portalSprache & 1) == 1);
            lEclEmittentenFurAktionaersPortalAuswahlM
                    .setLinkDeutsch(bvMandanten.linkAktionaersportalDeutsch(pDbBundle, lEmittenten, pBaseLink, pTest));

            lEclEmittentenFurAktionaersPortalAuswahlM.setPortalSpracheEnglisch((lEmittenten.portalSprache & 2) == 2);
            lEclEmittentenFurAktionaersPortalAuswahlM.setLinkEnglisch(
                    bvMandanten.linkAktionaersportalEnglisch(pDbBundle, lEmittenten, pBaseLink, pTest));

            lEclEmittentenFurAktionaersPortalAuswahlM.setDbGesperrt(lEmittenten.inAuswahl == 1);

            emittentenListe.add(lEclEmittentenFurAktionaersPortalAuswahlM);
        }
        return true;
    }

    public boolean fuelleEmittentenListeAdmin(DbBundle pDbBundle, String pBaseLink) {
        emittentenListe = new LinkedList<EclEmittentenFuerAktionaersPortalAuswahlM>();

        BvMandanten bvMandanten = new BvMandanten();
        bvMandanten.liefereMandantenFuerAdminAuswahl(pDbBundle);

        for (int i = 0; i < bvMandanten.rcEmittentenListe.size(); i++) {
            EclEmittenten lEmittenten = bvMandanten.rcEmittentenListe.get(i);
            EclEmittentenFuerAktionaersPortalAuswahlM lEclEmittentenFurAktionaersPortalAuswahlM = new EclEmittentenFuerAktionaersPortalAuswahlM();
            lEclEmittentenFurAktionaersPortalAuswahlM
                    .setMandant(CaString.fuelleLinksNull(Integer.toString(lEmittenten.mandant), 3));
            lEclEmittentenFurAktionaersPortalAuswahlM.setHvJahr(Integer.toString(lEmittenten.hvJahr));
            lEclEmittentenFurAktionaersPortalAuswahlM.setHvNummer(lEmittenten.hvNummer);
            lEclEmittentenFurAktionaersPortalAuswahlM.setDbArt(lEmittenten.dbArt);
            lEclEmittentenFurAktionaersPortalAuswahlM.setBezeichnungKurz(lEmittenten.bezeichnungKurz);
            lEclEmittentenFurAktionaersPortalAuswahlM.setHvDatum(lEmittenten.hvDatum);

            lEclEmittentenFurAktionaersPortalAuswahlM.setPortalAktuellAktiv(lEmittenten.portalAktuellAktiv == 1);

            lEclEmittentenFurAktionaersPortalAuswahlM.setPortalSpracheDeutsch((lEmittenten.portalSprache & 1) == 1);
            lEclEmittentenFurAktionaersPortalAuswahlM
                    .setLinkDeutsch(bvMandanten.linkAdmin(pDbBundle, lEmittenten, pBaseLink));

            lEclEmittentenFurAktionaersPortalAuswahlM.setDbGesperrt(lEmittenten.inAuswahl == 1);

            emittentenListe.add(lEclEmittentenFurAktionaersPortalAuswahlM);
        }
        return true;
    }

    /*************Standard getter und Setter*************************/

    public List<EclEmittentenFuerAktionaersPortalAuswahlM> getEmittentenListe() {
        return emittentenListe;
    }

    public void setEmittentenListe(List<EclEmittentenFuerAktionaersPortalAuswahlM> emittentenListe) {
        this.emittentenListe = emittentenListe;
    }

}
