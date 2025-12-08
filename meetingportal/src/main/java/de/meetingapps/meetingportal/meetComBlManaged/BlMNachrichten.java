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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaLeseDatei;
import de.meetingapps.meetingportal.meetComBl.BlNachrichten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtAnhangM;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtEmpfaengerUserM;
import de.meetingapps.meetingportal.meetComEclM.EclNachrichtM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@SessionScoped
@Named
public class BlMNachrichten implements Serializable {
    private static final long serialVersionUID = -7796276974953671759L;

    private int logDrucken = 3;

    @Inject
    EclUserLoginM eclUserLoginM;
    @Inject
    EclNachrichtM eclNachrichtM;

    /**Nachrichtenliste (Empfangene)*/
    private boolean nachrichtenEmpfangsbereichAnzeigen = true;

    private boolean nurUnbearbeiteteNachrichtenAnzeigen = true;
    private List<EclNachrichtM> empfangeneNachrichtenAlleListe = null;
    private List<EclNachrichtM> empfangeneNachrichtenUnbearbeiteteListe = null;

    /**
     * Beinhaltet die "technischen" Mails mit verwendungscode>100000
     * 
     * Wenn mandant!=0, dann werden nur die Mails zu diesem Mandanten angezeigt,
     * sonst alle*/
    private boolean nurUnbearbeiteteNachrichtenTechnischAnzeigen = true;
    private List<EclNachrichtM> empfangeneNachrichtenTechnischAlleListe = null;
    private List<EclNachrichtM> empfangeneNachrichtenTechnischUnbearbeiteteListe = null;

    /**
     * Beinhaltet die gesendeten Mails
     * 
     * wenn mandant!=0, dann werden nur die Mails zu diesem Mandanten angezeigt, sonst alle
     */
    private boolean nachrichtenSendebereichAnzeigen = true;

    private boolean nurUnbearbeiteteGesendeteNachrichtenAnzeigen = true;
    private List<EclNachrichtM> gesendeteNachrichtenAlleListe = null;
    private List<EclNachrichtM> gesendeteNachrichtenUnbearbeiteteListe = null;

    /**Nachricht - Detailansicht*/
    private boolean detailSichtVerfuegbar = false;
    private List<EclNachrichtEmpfaengerUserM> nachrichtenEmpfaengerListe = null;
    private List<EclNachrichtAnhangM> nachrichtAnhangListe = null;

    public boolean nachrichtenAnhangListeVorhanden() {
        if (nachrichtAnhangListe == null || nachrichtAnhangListe.size() == 0) {
            return false;
        }
        return true;
    }

    public void vorbereitenNachrichtenEmpfangsliste(DbBundle pDbBundle) {
        CaBug.druckeLog("BlMNachrichten.vorbereitenNachrichtenEmpfangsliste", logDrucken, 10);
        BlNachrichten blNachrichten = new BlNachrichten(true, pDbBundle);

        einlesenEmpfangeneNachrichten(pDbBundle, blNachrichten);
        einlesenGesendeteNachrichten(pDbBundle, blNachrichten);
        //		if (logDrucken) {
        //			System.out.println("Alle");
        //			for (int i=0;i<empfangeneNachrichtenAlleListe.size();i++) {
        //				System.out.println(empfangeneNachrichtenAlleListe.get(i).getBetreff());
        //			}
        //			System.out.println("Unbearbeitete");
        //			for (int i=0;i<empfangeneNachrichtenUnbearbeiteteListe.size();i++) {
        //				System.out.println(empfangeneNachrichtenAlleListe.get(i).getBetreff());
        //			}
        //		}
        nachrichtenEmpfangsbereichAnzeigen = false;
        nurUnbearbeiteteNachrichtenAnzeigen = true;

        nurUnbearbeiteteNachrichtenTechnischAnzeigen = true;

        nachrichtenSendebereichAnzeigen = false;
        nurUnbearbeiteteGesendeteNachrichtenAnzeigen = true;

        detailSichtVerfuegbar = false;
    }

    public void vorbereitenNachrichtenEmpfangslisteRefresh(DbBundle pDbBundle) {
        CaBug.druckeLog("BlMNachrichten.vorbereitenNachrichtenEmpfangslisteRefresh", logDrucken, 10);
        BlNachrichten blNachrichten = new BlNachrichten(true, pDbBundle);

        einlesenEmpfangeneNachrichten(pDbBundle, blNachrichten);
        einlesenGesendeteNachrichten(pDbBundle, blNachrichten);
    }

    private int einlesenEmpfangeneNachrichten(DbBundle pDbBundle, BlNachrichten blNachrichten) {
        blNachrichten.leseAlleEmpfangenenNachrichten(eclUserLoginM.getUserLoginIdent());

        empfangeneNachrichtenAlleListe = new LinkedList<EclNachrichtM>();
        empfangeneNachrichtenUnbearbeiteteListe = new LinkedList<EclNachrichtM>();

        empfangeneNachrichtenTechnischAlleListe = new LinkedList<EclNachrichtM>();
        empfangeneNachrichtenTechnischUnbearbeiteteListe = new LinkedList<EclNachrichtM>();

        int anz = blNachrichten.rcEmpfangeneNachrichtenList.size();
        CaBug.druckeLog("anz=" + Integer.toString(anz), logDrucken, 10);
        for (int i = 0; i < anz; i++) {
            EclNachrichtM lNachrichtM = new EclNachrichtM(blNachrichten.rcEmpfangeneNachrichtenList.get(i));
            if (!lNachrichtM.getParameter2().isEmpty()) {
                /*Emittent ergänzen*/
                pDbBundle.setzeRCMandantAusSchemaMandant(lNachrichtM.getParameter2());
                pDbBundle.dbEmittenten.readEmittentHV(pDbBundle.rcMandant, pDbBundle.rcHVJahr, pDbBundle.rcHVNummer,
                        pDbBundle.rcDatenbereich);
                lNachrichtM.setEmittentenName(pDbBundle.dbEmittenten.ergebnisArray[0].bezeichnungKurz);
            }
            empfangeneNachrichtenAlleListe.add(lNachrichtM);
            if (lNachrichtM.isAnzeigeBeimEmpfaengerAusblenden() == false
                    && lNachrichtM.isMailIstBearbeitetVomEmpfaengerGesetzt() == false) {
                empfangeneNachrichtenUnbearbeiteteListe.add(lNachrichtM);
            }
            if (lNachrichtM.getVerwendungsCode() > 100000) {
                if (pDbBundle.clGlobalVar.mandant == 0 || lNachrichtM.getParameter2().isEmpty()
                        || lNachrichtM.getParameter2().equals(pDbBundle.getSchemaMandant())) {
                    empfangeneNachrichtenTechnischAlleListe.add(lNachrichtM);
                    if (lNachrichtM.isAnzeigeBeimEmpfaengerAusblenden() == false
                            && lNachrichtM.isMailIstBearbeitetVomEmpfaengerGesetzt() == false) {
                        empfangeneNachrichtenTechnischUnbearbeiteteListe.add(lNachrichtM);
                    }

                }
            }
        }

        return 1;
    }

    private int einlesenGesendeteNachrichten(DbBundle pDbBundle, BlNachrichten blNachrichten) {
        blNachrichten.leseAlleGesendetenNachrichten(eclUserLoginM.getUserLoginIdent());

        gesendeteNachrichtenAlleListe = new LinkedList<EclNachrichtM>();
        gesendeteNachrichtenUnbearbeiteteListe = new LinkedList<EclNachrichtM>();

        int anz = blNachrichten.rcGesendeteNachrichtenList.size();
        CaBug.druckeLog("anz=" + Integer.toString(anz), logDrucken, 10);
        for (int i = 0; i < anz; i++) {
            EclNachrichtM lNachrichtM = new EclNachrichtM(blNachrichten.rcGesendeteNachrichtenList.get(i));
            if (!lNachrichtM.getParameter2().isEmpty()) {
                /*Emittent ergänzen*/
                pDbBundle.setzeRCMandantAusSchemaMandant(lNachrichtM.getParameter2());
                pDbBundle.dbEmittenten.readEmittentHV(pDbBundle.rcMandant, pDbBundle.rcHVJahr, pDbBundle.rcHVNummer,
                        pDbBundle.rcDatenbereich);
                lNachrichtM.setEmittentenName(pDbBundle.dbEmittenten.ergebnisArray[0].bezeichnungKurz);
            }
            gesendeteNachrichtenAlleListe.add(lNachrichtM);
            if (lNachrichtM.isAnzeigeBeimEmpfaengerAusblenden() == false
                    && lNachrichtM.isMailIstBearbeitetVomEmpfaengerGesetzt() == false) {
                gesendeteNachrichtenUnbearbeiteteListe.add(lNachrichtM);
            }
        }

        return 1;
    }

    public boolean liefereNachrichtenTechnischVorhanden() {
        if (empfangeneNachrichtenTechnischAlleListe == null || empfangeneNachrichtenTechnischAlleListe.size() == 0) {
            return false;
        }
        return true;
    }

    public boolean liefereNachrichtenUnbearbeitetTechnischVorhanden() {
        if (empfangeneNachrichtenTechnischUnbearbeiteteListe == null
                || empfangeneNachrichtenTechnischUnbearbeiteteListe.size() == 0) {
            return false;
        }
        return true;
    }

    public boolean liefereNachrichtenVorhanden() {
        if (empfangeneNachrichtenAlleListe == null || empfangeneNachrichtenAlleListe.size() == 0) {
            return false;
        }
        return true;
    }

    public int liefereNachrichtenAnzahl() {
        if (empfangeneNachrichtenAlleListe == null || empfangeneNachrichtenAlleListe.size() == 0) {
            return 0;
        }
        return empfangeneNachrichtenAlleListe.size();
    }

    public int liefereGesendeteNachrichtenAnzahl() {
        if (gesendeteNachrichtenAlleListe == null || gesendeteNachrichtenAlleListe.size() == 0) {
            return 0;
        }
        return gesendeteNachrichtenAlleListe.size();
    }

    public boolean liefereGesendeteNachrichtenVorhanden() {
        if (gesendeteNachrichtenAlleListe == null || gesendeteNachrichtenAlleListe.size() == 0) {
            return false;
        }
        return true;
    }

    public boolean liefereGesendeteNachrichtenUnbearbeitetVorhanden() {
        if (gesendeteNachrichtenUnbearbeiteteListe == null || gesendeteNachrichtenUnbearbeiteteListe.size() == 0) {
            return false;
        }
        return true;
    }

    public int liefereGesendeteNachrichtenUnbearbeitetAnzahl() {
        if (gesendeteNachrichtenUnbearbeiteteListe == null || gesendeteNachrichtenUnbearbeiteteListe.size() == 0) {
            return 0;
        }
        return gesendeteNachrichtenUnbearbeiteteListe.size();
    }

    public boolean liefereNachrichtenUnbearbeitetVorhanden() {
        if (empfangeneNachrichtenUnbearbeiteteListe == null || empfangeneNachrichtenUnbearbeiteteListe.size() == 0) {
            return false;
        }
        return true;
    }

    public int liefereNachrichtenUnbearbeitetAnzahl() {
        if (empfangeneNachrichtenUnbearbeiteteListe == null || empfangeneNachrichtenUnbearbeiteteListe.size() == 0) {
            return 0;
        }
        return empfangeneNachrichtenUnbearbeiteteListe.size();
    }

    public int nachrichtEinblendenBeimEmpfaenger(DbBundle pDbBundle, int pIdent, int pDbServerIdent) {
        BlNachrichten blNachrichten = new BlNachrichten(true, pDbBundle);
        blNachrichten.nachrichtEinblendenBeimEmpfaenger(pIdent, pDbServerIdent);
        einlesenEmpfangeneNachrichten(pDbBundle, blNachrichten);
        return 1;
    }

    public int nachrichtAusblendenBeimEmpfaenger(DbBundle pDbBundle, int pIdent, int pDbServerIdent) {
        BlNachrichten blNachrichten = new BlNachrichten(true, pDbBundle);
        blNachrichten.nachrichtAusblendenBeimEmpfaenger(pIdent, pDbServerIdent);
        einlesenEmpfangeneNachrichten(pDbBundle, blNachrichten);
        return 1;
    }

    public int nachrichtSetzeBearbeitetBeimEmpfaenger(DbBundle pDbBundle, int pIdent, int pDbServerIdent) {
        BlNachrichten blNachrichten = new BlNachrichten(true, pDbBundle);
        blNachrichten.nachrichtSetzeBearbeitetBeimEmpfaenger(pIdent, pDbServerIdent);
        einlesenEmpfangeneNachrichten(pDbBundle, blNachrichten);
        return 1;
    }

    public int nachrichtSetzeUnbearbeitetBeimEmpfaenger(DbBundle pDbBundle, int pIdent, int pDbServerIdent) {
        BlNachrichten blNachrichten = new BlNachrichten(true, pDbBundle);
        blNachrichten.nachrichtSetzeUnbearbeitetBeimEmpfaenger(pIdent, pDbServerIdent);
        einlesenEmpfangeneNachrichten(pDbBundle, blNachrichten);
        return 1;
    }

    /**Ergebnis ist in Bean EclNachrichtM, sowie in
     * nachrichtenEmpfaengerListe und nachrichtAnhangListe.
     * @return
     */
    public int nachrichtDetailEinlesen(DbBundle pDbBundle, int pIdent, int pDbServerIdent) {
        BlNachrichten blNachrichten = new BlNachrichten(true, pDbBundle);
        blNachrichten.leseDetailsZuNachricht(pIdent, pDbServerIdent);
        eclNachrichtM.copyFrom(blNachrichten.rcNachrichtDetail);

        nachrichtenEmpfaengerListe = new LinkedList<EclNachrichtEmpfaengerUserM>();
        if (blNachrichten.rcAlleEmpfaengerArray != null) {
            for (int i = 0; i < blNachrichten.rcAlleEmpfaengerArray.length; i++) {
                nachrichtenEmpfaengerListe.add(new EclNachrichtEmpfaengerUserM(blNachrichten.rcAlleEmpfaengerArray[i]));
            }
        }

        nachrichtAnhangListe = new LinkedList<EclNachrichtAnhangM>();
        if (blNachrichten.rcNachrichtAnhangArray != null) {
            for (int i = 0; i < blNachrichten.rcNachrichtAnhangArray.size(); i++) {
                nachrichtAnhangListe.add(new EclNachrichtAnhangM(blNachrichten.rcNachrichtAnhangArray.get(i)));
            }
        }
        detailSichtVerfuegbar = true;

        return 1;
    }

    public void zeigeAnhangAn(DbBundle pDbBundle, EclNachrichtAnhangM lEclNachrichtAnhangM) {
        BlNachrichten blNachrichten = new BlNachrichten(true, pDbBundle);
        String dateiname = blNachrichten.lieferePfadDateiname(lEclNachrichtAnhangM.getIdent(),
                lEclNachrichtAnhangM.getDateiname());
        byte[] bytes = null;
        CaLeseDatei caLeseDatei = new CaLeseDatei();
        bytes = caLeseDatei.alsBytes(dateiname);

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

        response.setHeader("Content-Disposition", "attachment; filename=" + lEclNachrichtAnhangM.getDateiname());
        faces.responseComplete();

        return;
    }

    /****************Standard getter und Setter*********************************/

    public boolean isNachrichtenEmpfangsbereichAnzeigen() {
        CaBug.druckeLog("BlmNachrichten.isNachrichtenEmpfangsbereichAnzeigen", logDrucken, 10);
        return nachrichtenEmpfangsbereichAnzeigen;
    }

    public void setNachrichtenEmpfangsbereichAnzeigen(boolean nachrichtenEmpfangsbereichAnzeigen) {
        this.nachrichtenEmpfangsbereichAnzeigen = nachrichtenEmpfangsbereichAnzeigen;
    }

    public boolean isNurUnbearbeiteteNachrichtenAnzeigen() {
        return nurUnbearbeiteteNachrichtenAnzeigen;
    }

    public void setNurUnbearbeiteteNachrichtenAnzeigen(boolean nurUnbearbeiteteNachrichtenAnzeigen) {
        this.nurUnbearbeiteteNachrichtenAnzeigen = nurUnbearbeiteteNachrichtenAnzeigen;
    }

    public List<EclNachrichtM> getEmpfangeneNachrichtenAlleListe() {
        CaBug.druckeInfo("BlmNachrichten.getEmpfangeneNachrichtenAlleListe");
        return empfangeneNachrichtenAlleListe;
    }

    public void setEmpfangeneNachrichtenAlleListe(List<EclNachrichtM> empfangeneNachrichtenAlleListe) {
        this.empfangeneNachrichtenAlleListe = empfangeneNachrichtenAlleListe;
    }

    public List<EclNachrichtM> getEmpfangeneNachrichtenUnbearbeiteteListe() {
        return empfangeneNachrichtenUnbearbeiteteListe;
    }

    public void setEmpfangeneNachrichtenUnbearbeiteteListe(
            List<EclNachrichtM> empfangeneNachrichtenUnbearbeiteteListe) {
        this.empfangeneNachrichtenUnbearbeiteteListe = empfangeneNachrichtenUnbearbeiteteListe;
    }

    public List<EclNachrichtEmpfaengerUserM> getNachrichtenEmpfaengerListe() {
        return nachrichtenEmpfaengerListe;
    }

    public void setNachrichtenEmpfaengerListe(List<EclNachrichtEmpfaengerUserM> nachrichtenEmpfaengerListe) {
        this.nachrichtenEmpfaengerListe = nachrichtenEmpfaengerListe;
    }

    public List<EclNachrichtAnhangM> getNachrichtAnhangListe() {
        return nachrichtAnhangListe;
    }

    public void setNachrichtAnhangListe(List<EclNachrichtAnhangM> nachrichtAnhangListe) {
        this.nachrichtAnhangListe = nachrichtAnhangListe;
    }

    public boolean isDetailSichtVerfuegbar() {
        return detailSichtVerfuegbar;
    }

    public void setDetailSichtVerfuegbar(boolean detailSichtVerfuegbar) {
        this.detailSichtVerfuegbar = detailSichtVerfuegbar;
    }

    public boolean isNurUnbearbeiteteNachrichtenTechnischAnzeigen() {
        return nurUnbearbeiteteNachrichtenTechnischAnzeigen;
    }

    public void setNurUnbearbeiteteNachrichtenTechnischAnzeigen(boolean nurUnbearbeiteteNachrichtenTechnischAnzeigen) {
        this.nurUnbearbeiteteNachrichtenTechnischAnzeigen = nurUnbearbeiteteNachrichtenTechnischAnzeigen;
    }

    public List<EclNachrichtM> getEmpfangeneNachrichtenTechnischAlleListe() {
        return empfangeneNachrichtenTechnischAlleListe;
    }

    public void setEmpfangeneNachrichtenTechnischAlleListe(
            List<EclNachrichtM> empfangeneNachrichtenTechnischAlleListe) {
        this.empfangeneNachrichtenTechnischAlleListe = empfangeneNachrichtenTechnischAlleListe;
    }

    public List<EclNachrichtM> getEmpfangeneNachrichtenTechnischUnbearbeiteteListe() {
        return empfangeneNachrichtenTechnischUnbearbeiteteListe;
    }

    public void setEmpfangeneNachrichtenTechnischUnbearbeiteteListe(
            List<EclNachrichtM> empfangeneNachrichtenTechnischUnbearbeiteteListe) {
        this.empfangeneNachrichtenTechnischUnbearbeiteteListe = empfangeneNachrichtenTechnischUnbearbeiteteListe;
    }

    public List<EclNachrichtM> getGesendeteNachrichten() {
        return gesendeteNachrichtenAlleListe;
    }

    public void setGesendeteNachrichten(List<EclNachrichtM> gesendeteNachrichten) {
        this.gesendeteNachrichtenAlleListe = gesendeteNachrichten;
    }

    public boolean isNachrichtenSendebereichAnzeigen() {
        return nachrichtenSendebereichAnzeigen;
    }

    public void setNachrichtenSendebereichAnzeigen(boolean nachrichtenSendebereichAnzeigen) {
        this.nachrichtenSendebereichAnzeigen = nachrichtenSendebereichAnzeigen;
    }

    public List<EclNachrichtM> getGesendeteNachrichtenListe() {
        return gesendeteNachrichtenAlleListe;
    }

    public void setGesendeteNachrichtenListe(List<EclNachrichtM> gesendeteNachrichtenListe) {
        this.gesendeteNachrichtenAlleListe = gesendeteNachrichtenListe;
    }

    public boolean isNurUnbearbeiteteGesendeteNachrichtenAnzeigen() {
        return nurUnbearbeiteteGesendeteNachrichtenAnzeigen;
    }

    public void setNurUnbearbeiteteGesendeteNachrichtenAnzeigen(boolean nurUnbearbeiteteGesendeteNachrichtenAnzeigen) {
        this.nurUnbearbeiteteGesendeteNachrichtenAnzeigen = nurUnbearbeiteteGesendeteNachrichtenAnzeigen;
    }

    public List<EclNachrichtM> getGesendeteNachrichtenAlleListe() {
        return gesendeteNachrichtenAlleListe;
    }

    public void setGesendeteNachrichtenAlleListe(List<EclNachrichtM> gesendeteNachrichtenAlleListe) {
        this.gesendeteNachrichtenAlleListe = gesendeteNachrichtenAlleListe;
    }

    public List<EclNachrichtM> getGesendeteNachrichtenUnbearbeiteteListe() {
        return gesendeteNachrichtenUnbearbeiteteListe;
    }

    public void setGesendeteNachrichtenUnbearbeiteteListe(List<EclNachrichtM> gesendeteNachrichtenUnbearbeiteteListe) {
        this.gesendeteNachrichtenUnbearbeiteteListe = gesendeteNachrichtenUnbearbeiteteListe;
    }

}
