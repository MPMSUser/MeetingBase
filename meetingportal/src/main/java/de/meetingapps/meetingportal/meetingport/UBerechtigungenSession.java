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

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

/*TODO: Bereinigung: aus eclGastM die entsprechenden Funktionen auch hierrein abbilden. Muß in Gastpflege angepaßt werden*/
@SessionScoped
@Named
public class UBerechtigungenSession  implements Serializable {
    private static final long serialVersionUID = 6032447769784598581L;
    
    /************Berechtigungen****************/
    private boolean darfStream=false;
    private boolean darfFragen=false;
    private boolean darfWortmeldungen=false;
    private boolean darfWidersprueche=false;
    private boolean darfAntraege=false;
    private boolean darfSonstigeMitteilungen=false;
    private boolean darfUnterlagenGruppe1=false;
    private boolean darfUnterlagenGruppe2=false;
    private boolean darfUnterlagenGruppe3=false;
    private boolean darfUnterlagenGruppe4=false;
    private boolean darfUnterlagenGruppe5=false;
    private boolean darfTeilnehmerverzeichnis=false;
    private boolean darfAbstimmungsergebnisse=false;
    private boolean darfBotschaftenEinreichen=false;
    private boolean darfBotschaftenAnzeige=false;
    private boolean darfRueckfragen=false;
    private boolean darfMonitoring=false;
    private boolean darfPreview=false;
    private boolean darfGast1=false;
    private boolean darfGast2=false;
    private boolean darfGast3=false;
    private boolean darfGast4=false;
    private boolean darfGast5=false;
    private boolean darfGast6=false;
    private boolean darfGast7=false;
    private boolean darfGast8=false;
    private boolean darfGast9=false;
    public boolean isDarfStream() {
        return darfStream;
    }
    
    public void belegeBerechtigungen(long pBerechtigungsWert) {
        darfStream= KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.stream, pBerechtigungsWert);
        darfFragen=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.fragen, pBerechtigungsWert);;
        darfWortmeldungen=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.wortmeldungen, pBerechtigungsWert);;
        darfWidersprueche=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.widersprueche, pBerechtigungsWert);;
        darfAntraege=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.antraege, pBerechtigungsWert);;
        darfSonstigeMitteilungen=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.sonstigeMitteilungen, pBerechtigungsWert);;
        darfUnterlagenGruppe1=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.unterlagenGruppe1, pBerechtigungsWert);;
        darfUnterlagenGruppe2=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.unterlagenGruppe2, pBerechtigungsWert);;
        darfUnterlagenGruppe3=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.unterlagenGruppe3, pBerechtigungsWert);;
        darfUnterlagenGruppe4=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.unterlagenGruppe4, pBerechtigungsWert);;
        darfUnterlagenGruppe5=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.unterlagenGruppe5, pBerechtigungsWert);;
        darfTeilnehmerverzeichnis=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.teilnehmerverzeichnis, pBerechtigungsWert);;
        darfAbstimmungsergebnisse=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.abstimmungsergebnisse, pBerechtigungsWert);;
        darfBotschaftenEinreichen=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.botschaftenEinreichen, pBerechtigungsWert);;
        darfBotschaftenAnzeige=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.botschaftenAnzeige, pBerechtigungsWert);;
        darfRueckfragen=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.rueckfragen, pBerechtigungsWert);;
        darfMonitoring=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.monitoring, pBerechtigungsWert);;
        darfPreview=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.preview, pBerechtigungsWert);;
        darfGast1=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast1, pBerechtigungsWert);;
        darfGast2=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast2, pBerechtigungsWert);;
        darfGast3=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast3, pBerechtigungsWert);;
        darfGast4=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast4, pBerechtigungsWert);;
        darfGast5=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast5, pBerechtigungsWert);;
        darfGast6=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast6, pBerechtigungsWert);;
        darfGast7=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast7, pBerechtigungsWert);;
        darfGast8=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast8, pBerechtigungsWert);;
        darfGast9=KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.gast9, pBerechtigungsWert);;
     }

    public long liefereBerechtigungsWert() {
        long berechtigungsWert=0;
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.stream, darfStream);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.fragen, darfFragen);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.wortmeldungen, darfWortmeldungen);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.widersprueche, darfWidersprueche);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.antraege, darfAntraege);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.sonstigeMitteilungen, darfSonstigeMitteilungen);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.unterlagenGruppe1, darfUnterlagenGruppe1);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.unterlagenGruppe2, darfUnterlagenGruppe2);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.unterlagenGruppe3, darfUnterlagenGruppe3);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.unterlagenGruppe4, darfUnterlagenGruppe4);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.unterlagenGruppe5, darfUnterlagenGruppe5);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.teilnehmerverzeichnis, darfTeilnehmerverzeichnis);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.abstimmungsergebnisse, darfAbstimmungsergebnisse);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.botschaftenEinreichen, darfBotschaftenEinreichen);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.botschaftenAnzeige, darfBotschaftenAnzeige);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.rueckfragen, darfRueckfragen);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.monitoring, darfMonitoring);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.preview, darfPreview);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast1, darfGast1);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast2, darfGast2);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast3, darfGast3);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast4, darfGast4);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast5, darfGast5);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast6, darfGast6);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast7, darfGast7);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast8, darfGast8);
        berechtigungsWert=KonstPortalFunktionen.setzeBerechtigung(berechtigungsWert, KonstPortalFunktionen.gast9, darfGast9);
        return berechtigungsWert;
    }

    
    /********************************Standard getter und setter*********************/
    
    public void setDarfStream(boolean darfStream) {
        this.darfStream = darfStream;
    }
    public boolean isDarfFragen() {
        return darfFragen;
    }
    public void setDarfFragen(boolean darfFragen) {
        this.darfFragen = darfFragen;
    }
    public boolean isDarfWortmeldungen() {
        return darfWortmeldungen;
    }
    public void setDarfWortmeldungen(boolean darfWortmeldungen) {
        this.darfWortmeldungen = darfWortmeldungen;
    }
    public boolean isDarfWidersprueche() {
        return darfWidersprueche;
    }
    public void setDarfWidersprueche(boolean darfWidersprueche) {
        this.darfWidersprueche = darfWidersprueche;
    }
    public boolean isDarfAntraege() {
        return darfAntraege;
    }
    public void setDarfAntraege(boolean darfAntraege) {
        this.darfAntraege = darfAntraege;
    }
    public boolean isDarfSonstigeMitteilungen() {
        return darfSonstigeMitteilungen;
    }
    public void setDarfSonstigeMitteilungen(boolean darfSonstigeMitteilungen) {
        this.darfSonstigeMitteilungen = darfSonstigeMitteilungen;
    }
    public boolean isDarfUnterlagenGruppe1() {
        return darfUnterlagenGruppe1;
    }
    public void setDarfUnterlagenGruppe1(boolean darfUnterlagenGruppe1) {
        this.darfUnterlagenGruppe1 = darfUnterlagenGruppe1;
    }
    public boolean isDarfUnterlagenGruppe2() {
        return darfUnterlagenGruppe2;
    }
    public void setDarfUnterlagenGruppe2(boolean darfUnterlagenGruppe2) {
        this.darfUnterlagenGruppe2 = darfUnterlagenGruppe2;
    }
    public boolean isDarfUnterlagenGruppe3() {
        return darfUnterlagenGruppe3;
    }
    public void setDarfUnterlagenGruppe3(boolean darfUnterlagenGruppe3) {
        this.darfUnterlagenGruppe3 = darfUnterlagenGruppe3;
    }
    public boolean isDarfUnterlagenGruppe4() {
        return darfUnterlagenGruppe4;
    }
    public void setDarfUnterlagenGruppe4(boolean darfUnterlagenGruppe4) {
        this.darfUnterlagenGruppe4 = darfUnterlagenGruppe4;
    }
    public boolean isDarfUnterlagenGruppe5() {
        return darfUnterlagenGruppe5;
    }
    public void setDarfUnterlagenGruppe5(boolean darfUnterlagenGruppe5) {
        this.darfUnterlagenGruppe5 = darfUnterlagenGruppe5;
    }
    public boolean isDarfTeilnehmerverzeichnis() {
        return darfTeilnehmerverzeichnis;
    }
    public void setDarfTeilnehmerverzeichnis(boolean darfTeilnehmerverzeichnis) {
        this.darfTeilnehmerverzeichnis = darfTeilnehmerverzeichnis;
    }
    public boolean isDarfAbstimmungsergebnisse() {
        return darfAbstimmungsergebnisse;
    }
    public void setDarfAbstimmungsergebnisse(boolean darfAbstimmungsergebnisse) {
        this.darfAbstimmungsergebnisse = darfAbstimmungsergebnisse;
    }
    public boolean isDarfBotschaftenEinreichen() {
        return darfBotschaftenEinreichen;
    }
    public void setDarfBotschaftenEinreichen(boolean darfBotschaftenEinreichen) {
        this.darfBotschaftenEinreichen = darfBotschaftenEinreichen;
    }
    public boolean isDarfBotschaftenAnzeige() {
        return darfBotschaftenAnzeige;
    }
    public void setDarfBotschaftenAnzeige(boolean darfBotschaftenAnzeige) {
        this.darfBotschaftenAnzeige = darfBotschaftenAnzeige;
    }
    public boolean isDarfRueckfragen() {
        return darfRueckfragen;
    }
    public void setDarfRueckfragen(boolean darfRueckfragen) {
        this.darfRueckfragen = darfRueckfragen;
    }
    public boolean isDarfMonitoring() {
        return darfMonitoring;
    }
    public void setDarfMonitoring(boolean darfMonitoring) {
        this.darfMonitoring = darfMonitoring;
    }
    public boolean isDarfPreview() {
        return darfPreview;
    }
    public void setDarfPreview(boolean darfPreview) {
        this.darfPreview = darfPreview;
    }
    public boolean isDarfGast1() {
        return darfGast1;
    }
    public void setDarfGast1(boolean darfGast1) {
        this.darfGast1 = darfGast1;
    }
    public boolean isDarfGast2() {
        return darfGast2;
    }
    public void setDarfGast2(boolean darfGast2) {
        this.darfGast2 = darfGast2;
    }
    public boolean isDarfGast3() {
        return darfGast3;
    }
    public void setDarfGast3(boolean darfGast3) {
        this.darfGast3 = darfGast3;
    }
    public boolean isDarfGast4() {
        return darfGast4;
    }
    public void setDarfGast4(boolean darfGast4) {
        this.darfGast4 = darfGast4;
    }
    public boolean isDarfGast5() {
        return darfGast5;
    }
    public void setDarfGast5(boolean darfGast5) {
        this.darfGast5 = darfGast5;
    }
    public boolean isDarfGast6() {
        return darfGast6;
    }
    public void setDarfGast6(boolean darfGast6) {
        this.darfGast6 = darfGast6;
    }
    public boolean isDarfGast7() {
        return darfGast7;
    }
    public void setDarfGast7(boolean darfGast7) {
        this.darfGast7 = darfGast7;
    }
    public boolean isDarfGast8() {
        return darfGast8;
    }
    public void setDarfGast8(boolean darfGast8) {
        this.darfGast8 = darfGast8;
    }
    public boolean isDarfGast9() {
        return darfGast9;
    }
    public void setDarfGast9(boolean darfGast9) {
        this.darfGast9 = darfGast9;
    }


    
}
