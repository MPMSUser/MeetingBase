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

import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclEmittentenFuerAktionaersPortalAuswahlListeM;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerAuswahl {

    /**Hinweis: auch testModus muß zwingend im Link mit übergeben werden - sonst wird
     * die Auswahlliste nicht gefüllt!
     */
    private String kennung = "";
    private String p = "";
    private String dbArt = "";
    private String baseLink = "";
    private String testModus = "";

    @Inject
    EclDbM eclDbM;

    @Inject
    EclEmittentenFuerAktionaersPortalAuswahlListeM eclEmittentenFuerAktionaersPortalAuswahlListeM;

    /**Wird auf 1 gesetzt, sobald gültiges Systempasswort/Systemkennung übergeben wurde*/
    private int zulaessig = 0;

    /*********Individuelle getter und setter**********************************/

    public void setTestModus(String testModus) {
        this.testModus = testModus;

        if (zulaessig == 1) {
            eclDbM.openAll();
            Boolean test = false;
            if (testModus.compareTo("1") == 0) {
                test = true;
            }
            eclEmittentenFuerAktionaersPortalAuswahlListeM.fuelleEmittentenListe(eclDbM.getDbBundle(), baseLink, test);
            eclDbM.closeAll();
        }
    }

    public void setP(String p) {
        this.p = p;
        /*TODO _Benutzerverwaltung: aControllerAuswahl-Kennung verallgemeinern*/
        if (kennung.compareTo("daAuswahlGuru") == 0 && p.compareTo("sy21RTjHUk") == 0) {
            zulaessig = 1;
        }
    }

    /*******************Ab hier: Standard Getter/Setter********************************************/

    public String getKennung() {
        return kennung;
    }

    public void setKennung(String kennung) {
        this.kennung = kennung;
    }

    public String getP() {
        return p;
    }

    public String getDbArt() {
        return dbArt;
    }

    public void setDbArt(String dbArt) {
        this.dbArt = dbArt;
    }

    public String getBaseLink() {
        return baseLink;
    }

    public void setBaseLink(String baseLink) {
        this.baseLink = baseLink;
    }

    public String getTestModus() {

        return testModus;
    }

}
