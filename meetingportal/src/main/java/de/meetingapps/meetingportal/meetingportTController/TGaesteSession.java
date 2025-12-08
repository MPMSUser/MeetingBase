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
package de.meetingapps.meetingportal.meetingportTController;

import java.io.Serializable;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungenMeldungen;
import de.meetingapps.meetingportal.meetComHVParam.ParamGaesteModul;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class TGaesteSession implements Serializable {
    private static final long serialVersionUID = -3018854798627617449L;

    /*+++++++++++++++Werte für Übersicht+++++++++++++++++++++*/
    
    /**true=Verändern ist grundsätzlich (Parameterbedingt) möglich
     * false=gesperrt*/
    private boolean veraendernIstZulaessig=false;
    
    /**Verändern ist grundsätzlich noch möglich - aber möglicherweise für diesen
     * konkreten Aktionär nicht mehr, weil Grenze erreicht
     */
    private boolean neuerGastZulaessig=false;
    
    private int anzGaesteInsgesamtZulaessig=0;
    private int anzGaesteNochZulaessig=0;

    /*Gäste liste*/
    private List<EclMeldungenMeldungen> gastkartenListe=null;

    private int anzahlVorhandeneGaeste=0;
    public int liefereAnzahlVorhandeneGaeste() {
        return anzahlVorhandeneGaeste;
    }

    public int liefereAnzahlGaesteInsgesamt() {
        if (gastkartenListe==null) {return 0;}
        return gastkartenListe.size();
    }

    
    /*++++++++++++++++++Felder für Eingabe++++++++++++++++++++++++++++*/
    
    private String feldAnrede = "";
    private String feldTitel = "";
    private String feldAdelstitel = "";
    private String feldName = "";
    private String feldVorname = "";
    private String feldZuHaenden = "";
    private String feldZusatz1 = "";
    private String feldZusatz2 = "";
    private String feldStrasse = "";
    private String feldLand = "";
    private String feldPLZ = "";
    private String feldOrt = "";
    private String feldMailadresse = "";
    private String feldKommunikationssprache = "";

    
    private int feldAnredeVerwendenPortal = 2;
    private int feldTitelVerwendenPortal = 1;
    private int feldAdelstitelVerwendenPortal = 0;
    private int feldNameVerwendenPortal = 2;
    private int feldVornameVerwendenPortal = 1;
    private int feldZuHaendenVerwendenPortal = 1;
    private int feldZusatz1VerwendenPortal = 0;
    private int feldZusatz2VerwendenPortal = 0;
    private int feldStrasseVerwendenPortal = 1;
    private int feldLandVerwendenPortal = 1;
    private int feldPLZVerwendenPortal = 1;
    private int feldOrtVerwendenPortal = 2;
    private int feldMailadresseVerwendenPortal = 1;
    private int feldKommunikationsspracheVerwendenPortal = 0;
    
    public boolean liefereEingebenFeldAnrede(){
        return feldAnredeVerwendenPortal>0;
    }
    public boolean liefereEingebenFeldTitel(){
        return feldTitelVerwendenPortal>0;
    }
    public boolean liefereEingebenFeldAdelstitel(){
        return feldAdelstitelVerwendenPortal>0;
    }
    public boolean liefereEingebenFeldName(){
        return feldNameVerwendenPortal>0;
    }
    public boolean liefereEingebenFeldVorname(){
        return feldVornameVerwendenPortal>0;
    }
    public boolean liefereEingebenFeldZuHaenden(){
        return feldZuHaendenVerwendenPortal>0;
    }
    public boolean liefereEingebenFeldZusatz1(){
        return feldZusatz1VerwendenPortal>0;
    }
    public boolean liefereEingebenFeldZusatz2(){
        return feldZusatz2VerwendenPortal>0;
    }
    public boolean liefereEingebenFeldStrasse(){
        return feldStrasseVerwendenPortal>0;
    }
    public boolean liefereEingebenFeldLand(){
        return feldLandVerwendenPortal>0;
    }
    public boolean liefereEingebenFeldPLZ(){
        return feldPLZVerwendenPortal>0;
    }
    public boolean liefereEingebenFeldOrt(){
        return feldOrtVerwendenPortal>0;
    }
    public boolean liefereEingebenFeldMailadresse(){
        return feldMailadresseVerwendenPortal>0;
    }
    public boolean liefereEingebenFeldKommunikationssprache(){
        return feldKommunikationsspracheVerwendenPortal>0;
    }
    
    public void initFelder(ParamGaesteModul pParamGaeste) {
        feldAnrede = "";
        feldAnredeVerwendenPortal=pParamGaeste.feldAnredeVerwendenPortal;

        feldTitel = "";
        feldTitelVerwendenPortal=pParamGaeste.feldTitelVerwendenPortal;

        feldAdelstitel = "";
        feldAdelstitelVerwendenPortal=pParamGaeste.feldAdelstitelVerwendenPortal;

        feldName = "";
        feldNameVerwendenPortal=pParamGaeste.feldNameVerwendenPortal;

        feldVorname = "";
        feldVornameVerwendenPortal=pParamGaeste.feldVornameVerwendenPortal;

        feldZuHaenden = "";
        feldZuHaendenVerwendenPortal=pParamGaeste.feldZuHaendenVerwendenPortal;

        feldZusatz1 = "";
        feldZusatz1VerwendenPortal=pParamGaeste.feldZusatz1VerwendenPortal;

        feldZusatz2 = "";
        feldZusatz2VerwendenPortal=pParamGaeste.feldZusatz2VerwendenPortal;

        feldStrasse = "";
        feldStrasseVerwendenPortal=pParamGaeste.feldStrasseVerwendenPortal;

        feldLand = "";
        feldLandVerwendenPortal=pParamGaeste.feldLandVerwendenPortal;

        feldPLZ = "";
        feldPLZVerwendenPortal=pParamGaeste.feldPLZVerwendenPortal;

        feldOrt = "";
        feldOrtVerwendenPortal=pParamGaeste.feldOrtVerwendenPortal;

        feldMailadresse = "";
        feldMailadresseVerwendenPortal=pParamGaeste.feldMailadresseVerwendenPortal;

        feldKommunikationssprache = "";
        feldKommunikationsspracheVerwendenPortal=pParamGaeste.feldKommunikationsspracheVerwendenPortal;
         
    }
    
    public int pruefeGasteingabe() {
        if (feldAnredeVerwendenPortal==2) {
            if (feldAnrede==null || feldAnrede.isEmpty()) {return CaFehler.afGastAnredeZwingend;}
        }
        if (feldTitelVerwendenPortal==2) {
            if (feldTitel==null || feldTitel.isEmpty()) {return CaFehler.afGastTitelZwingend;}
        }
        if (feldAdelstitelVerwendenPortal==2) {
            if (feldAdelstitel==null || feldAdelstitel.isEmpty()) {return CaFehler.afGastAdelstitelZwingend;}
        }
        if (feldNameVerwendenPortal==2) {
            if (feldName==null || feldName.isEmpty()) {return CaFehler.afGastNameZwingend;}
        }
        if (feldVornameVerwendenPortal==2) {
            if (feldVorname==null || feldVorname.isEmpty()) {return CaFehler.afGastVornameZwingend;}
        }
        if (feldZuHaendenVerwendenPortal==2) {
            if (feldZuHaenden==null || feldZuHaenden.isEmpty()) {return CaFehler.afGastZuHaendenZwingend;}
        }
        if (feldZusatz1VerwendenPortal==2) {
            if (feldZusatz1==null || feldZusatz1.isEmpty()) {return CaFehler.afGastZusatz1Zwingend;}
        }
        if (feldZusatz2VerwendenPortal==2) {
            if (feldZusatz2==null || feldZusatz2.isEmpty()) {return CaFehler.afGastZusatz2Zwingend;}
        }
        if (feldStrasseVerwendenPortal==2) {
            if (feldStrasse==null || feldStrasse.isEmpty()) {return CaFehler.afGastStrasseZwingend;}
        }
        if (feldLandVerwendenPortal==2) {
            if (feldLand==null || feldLand.isEmpty()) {return CaFehler.afGastLandZwingend;}
        }
        if (feldPLZVerwendenPortal==2) {
            if (feldPLZ==null || feldPLZ.isEmpty()) {return CaFehler.afGastPLZZwingend;}
        }
        if (feldOrtVerwendenPortal==2) {
            if (feldOrt==null || feldOrt.isEmpty()) {return CaFehler.afGastOrtZwingend;}
        }
        if (feldMailadresseVerwendenPortal==2) {
            if (feldMailadresse==null || feldMailadresse.isEmpty()) {return CaFehler.afGastMailadresseZwingend;}
        }
        if (feldMailadresseVerwendenPortal!=0) {
            if (feldMailadresse!=null && feldMailadresse.isEmpty()==false) {
                if (CaString.isMailadresse(feldMailadresse)==false) {
                    return CaFehler.afGastMailAdresseFalsch;
                }
            }
        }
        if (feldKommunikationsspracheVerwendenPortal==2) {
            if (feldKommunikationssprache==null || feldKommunikationssprache.isEmpty()) {return CaFehler.afGastKommunikationsspracheZwingend;}
        }
        return 1;
    }
    
    
    public void copyTo(EclMeldung pGast) {

        if (feldAnrede==null ||feldAnrede.isEmpty()) {
            pGast.anrede=0;
        }
        else {
            pGast.anrede = Integer.parseInt(feldAnrede);
        }
        pGast.titel = feldTitel;
        pGast.adelstitel = feldAdelstitel;
        pGast.name = feldName;
        pGast.vorname = feldVorname;
        pGast.zuHdCo = feldZuHaenden;
        pGast.zusatz1 = feldZusatz1;
        pGast.zusatz2 = feldZusatz2;
        pGast.strasse = feldStrasse;
        pGast.land = feldLand;
        pGast.plz = feldPLZ;
        pGast.ort = feldOrt;
        pGast.mailadresse = feldMailadresse;
//        pGast.kommunikationssprache = Integer.parseInt(feldKommunikationssprache);
        return;
    }

    
    
    /*+++++++++++++++++++++++++++++Standard getter und setter+++++++++++++++++++++++++++++++++++++++++*/
    public boolean isVeraendernIstZulaessig() {
        return veraendernIstZulaessig;
    }

    public void setVeraendernIstZulaessig(boolean veraendernIstZulaessig) {
        this.veraendernIstZulaessig = veraendernIstZulaessig;
    }

    public boolean isNeuerGastZulaessig() {
        return neuerGastZulaessig;
    }

    public void setNeuerGastZulaessig(boolean neuerGastZulaessig) {
        this.neuerGastZulaessig = neuerGastZulaessig;
    }

    public int getAnzGaesteInsgesamtZulaessig() {
        return anzGaesteInsgesamtZulaessig;
    }

    public void setAnzGaesteInsgesamtZulaessig(int anzGaesteInsgesamtZulaessig) {
        this.anzGaesteInsgesamtZulaessig = anzGaesteInsgesamtZulaessig;
    }


    public int getAnzGaesteNochZulaessig() {
        return anzGaesteNochZulaessig;
    }


    public void setAnzGaesteNochZulaessig(int anzGaesteNochZulaessig) {
        this.anzGaesteNochZulaessig = anzGaesteNochZulaessig;
    }
    public String getFeldAnrede() {
        return feldAnrede;
    }
    public void setFeldAnrede(String feldAnrede) {
        this.feldAnrede = feldAnrede;
    }
    public String getFeldTitel() {
        return feldTitel;
    }
    public void setFeldTitel(String feldTitel) {
        this.feldTitel = feldTitel;
    }
    public String getFeldAdelstitel() {
        return feldAdelstitel;
    }
    public void setFeldAdelstitel(String feldAdelstitel) {
        this.feldAdelstitel = feldAdelstitel;
    }
    public String getFeldName() {
        return feldName;
    }
    public void setFeldName(String feldName) {
        this.feldName = feldName;
    }
    public String getFeldVorname() {
        return feldVorname;
    }
    public void setFeldVorname(String feldVorname) {
        this.feldVorname = feldVorname;
    }
    public String getFeldZuHaenden() {
        return feldZuHaenden;
    }
    public void setFeldZuHaenden(String feldZuHaenden) {
        this.feldZuHaenden = feldZuHaenden;
    }
    public String getFeldZusatz1() {
        return feldZusatz1;
    }
    public void setFeldZusatz1(String feldZusatz1) {
        this.feldZusatz1 = feldZusatz1;
    }
    public String getFeldZusatz2() {
        return feldZusatz2;
    }
    public void setFeldZusatz2(String feldZusatz2) {
        this.feldZusatz2 = feldZusatz2;
    }
    public String getFeldStrasse() {
        return feldStrasse;
    }
    public void setFeldStrasse(String feldStrasse) {
        this.feldStrasse = feldStrasse;
    }
    public String getFeldLand() {
        return feldLand;
    }
    public void setFeldLand(String feldLand) {
        this.feldLand = feldLand;
    }
    public String getFeldPLZ() {
        return feldPLZ;
    }
    public void setFeldPLZ(String feldPLZ) {
        this.feldPLZ = feldPLZ;
    }
    public String getFeldOrt() {
        return feldOrt;
    }
    public void setFeldOrt(String feldOrt) {
        this.feldOrt = feldOrt;
    }
    public String getFeldMailadresse() {
        return feldMailadresse;
    }
    public void setFeldMailadresse(String feldMailadresse) {
        this.feldMailadresse = feldMailadresse;
    }
    public String getFeldKommunikationssprache() {
        return feldKommunikationssprache;
    }
    public void setFeldKommunikationssprache(String feldKommunikationssprache) {
        this.feldKommunikationssprache = feldKommunikationssprache;
    }
    public int getFeldAnredeVerwendenPortal() {
        return feldAnredeVerwendenPortal;
    }
    public void setFeldAnredeVerwendenPortal(int feldAnredeVerwendenPortal) {
        this.feldAnredeVerwendenPortal = feldAnredeVerwendenPortal;
    }
    public int getFeldTitelVerwendenPortal() {
        return feldTitelVerwendenPortal;
    }
    public void setFeldTitelVerwendenPortal(int feldTitelVerwendenPortal) {
        this.feldTitelVerwendenPortal = feldTitelVerwendenPortal;
    }
    public int getFeldAdelstitelVerwendenPortal() {
        return feldAdelstitelVerwendenPortal;
    }
    public void setFeldAdelstitelVerwendenPortal(int feldAdelstitelVerwendenPortal) {
        this.feldAdelstitelVerwendenPortal = feldAdelstitelVerwendenPortal;
    }
    public int getFeldNameVerwendenPortal() {
        return feldNameVerwendenPortal;
    }
    public void setFeldNameVerwendenPortal(int feldNameVerwendenPortal) {
        this.feldNameVerwendenPortal = feldNameVerwendenPortal;
    }
    public int getFeldVornameVerwendenPortal() {
        return feldVornameVerwendenPortal;
    }
    public void setFeldVornameVerwendenPortal(int feldVornameVerwendenPortal) {
        this.feldVornameVerwendenPortal = feldVornameVerwendenPortal;
    }
    public int getFeldZuHaendenVerwendenPortal() {
        return feldZuHaendenVerwendenPortal;
    }
    public void setFeldZuHaendenVerwendenPortal(int feldZuHaendenVerwendenPortal) {
        this.feldZuHaendenVerwendenPortal = feldZuHaendenVerwendenPortal;
    }
    public int getFeldZusatz1VerwendenPortal() {
        return feldZusatz1VerwendenPortal;
    }
    public void setFeldZusatz1VerwendenPortal(int feldZusatz1VerwendenPortal) {
        this.feldZusatz1VerwendenPortal = feldZusatz1VerwendenPortal;
    }
    public int getFeldZusatz2VerwendenPortal() {
        return feldZusatz2VerwendenPortal;
    }
    public void setFeldZusatz2VerwendenPortal(int feldZusatz2VerwendenPortal) {
        this.feldZusatz2VerwendenPortal = feldZusatz2VerwendenPortal;
    }
    public int getFeldStrasseVerwendenPortal() {
        return feldStrasseVerwendenPortal;
    }
    public void setFeldStrasseVerwendenPortal(int feldStrasseVerwendenPortal) {
        this.feldStrasseVerwendenPortal = feldStrasseVerwendenPortal;
    }
    public int getFeldLandVerwendenPortal() {
        return feldLandVerwendenPortal;
    }
    public void setFeldLandVerwendenPortal(int feldLandVerwendenPortal) {
        this.feldLandVerwendenPortal = feldLandVerwendenPortal;
    }
    public int getFeldPLZVerwendenPortal() {
        return feldPLZVerwendenPortal;
    }
    public void setFeldPLZVerwendenPortal(int feldPLZVerwendenPortal) {
        this.feldPLZVerwendenPortal = feldPLZVerwendenPortal;
    }
    public int getFeldOrtVerwendenPortal() {
        return feldOrtVerwendenPortal;
    }
    public void setFeldOrtVerwendenPortal(int feldOrtVerwendenPortal) {
        this.feldOrtVerwendenPortal = feldOrtVerwendenPortal;
    }
    public int getFeldMailadresseVerwendenPortal() {
        return feldMailadresseVerwendenPortal;
    }
    public void setFeldMailadresseVerwendenPortal(int feldMailadresseVerwendenPortal) {
        this.feldMailadresseVerwendenPortal = feldMailadresseVerwendenPortal;
    }
    public int getFeldKommunikationsspracheVerwendenPortal() {
        return feldKommunikationsspracheVerwendenPortal;
    }
    public void setFeldKommunikationsspracheVerwendenPortal(int feldKommunikationsspracheVerwendenPortal) {
        this.feldKommunikationsspracheVerwendenPortal = feldKommunikationsspracheVerwendenPortal;
    }
    public List<EclMeldungenMeldungen> getGastkartenListe() {
        return gastkartenListe;
    }
    public void setGastkartenListe(List<EclMeldungenMeldungen> gastkartenListe) {
        this.gastkartenListe = gastkartenListe;
    }
    public int getAnzahlVorhandeneGaeste() {
        return anzahlVorhandeneGaeste;
    }
    public void setAnzahlVorhandeneGaeste(int anzahlVorhandeneGaeste) {
        this.anzahlVorhandeneGaeste = anzahlVorhandeneGaeste;
    }
    
    
}
