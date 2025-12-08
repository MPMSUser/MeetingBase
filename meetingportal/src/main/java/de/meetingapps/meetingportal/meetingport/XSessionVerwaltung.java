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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclIpTracking;
import de.meetingapps.meetingportal.meetingportTController.TSession;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@SessionScoped
@Named
public class XSessionVerwaltung implements Serializable {
    private static final long serialVersionUID = 7469071336519102136L;

    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    EclParamM eclParamM;

    @Inject
    USession uSession;
    @Inject
    TSession tSession;

    private String aktuelleMaske = "";
    private int startPruefen = 1;
    private int bereitsAufgerufen = 0;

    /*
    <session-config>
        <session-timeout>60</session-timeout>
    </session-config>
    
     */

    /*********************Time-Out-Verwaltung**************************************************/
    public void setzeTimeOut() {
        if (eclParamM.getParam().paramPortal.timeoutAufLang == 1) {
            setTimeoutLang();
        } else {
            setTimeoutNormal();
        }
    }

    public void setTimeoutLang() {
        liefereFacesContext();
        HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
        /**Stunden*60*60 = Sekunden*/
        session.setMaxInactiveInterval(10 * 60 * 60);
    }

    public void setTimeoutNormal() {
        liefereFacesContext();
        HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
        /**Stunden*60*60 = Sekunden*/
        session.setMaxInactiveInterval(3 * 60 * 60);
    }

    /*******************Start-Stop für Seiten********************************/

    /**=für Aktionärsportal (A)
     * pLogin: nur zur Protokollausgabe*/
    public boolean pruefeStart(String pAufrufMaske, String pLogin) {
        String[] aufrufMasken = new String[1];
        aufrufMasken[0] = pAufrufMaske;
        return pruefeAUStart(aufrufMasken, pLogin);
    }

    public boolean pruefeStart(String[] pAufrufMaske, String pLogin) {
        return pruefeAUStart(pAufrufMaske, pLogin);
    }

    /**=für Universalportal (U)
     * pLogin: nur zur Protokollausgabe*/
    public boolean pruefeUStart(String pAufrufMaske, String pLogin) {
        String[] aufrufMasken = new String[1];
        aufrufMasken[0] = pAufrufMaske;
        return pruefeAUStart(aufrufMasken, pLogin);
    }

    public boolean pruefeUStart(String[] pAufrufMaske, String pLogin) {
        return pruefeAUStart(pAufrufMaske, pLogin);
    }

    /**=für Teilnehmerportal (T)
     * pLogin: nur zur Protokollausgabe*/
    public boolean pruefeTStart(String pAufrufMaske, String pLogin) {
        String[] aufrufMasken = new String[1];
        aufrufMasken[0] = pAufrufMaske;
        return pruefeAUStart(aufrufMasken, pLogin);
    }

    public boolean pruefeTStart(String[] pAufrufMaske, String pLogin) {
        return pruefeAUStart(pAufrufMaske, pLogin);
    }

    private boolean pruefeAUStart(String[] pAufrufMaske, String pLogin) {

        if (startPruefen == 0) {
            return true;
        }

        uSession.clearFehlermeldung();
        
        liefereFacesContext();
        String sessionId = getSessionID();

        CaBug.druckeInfo(">>>>pruefeStart<<<< paufrufmaske=" + pAufrufMaske[0] + " aktuelleMaske=" + aktuelleMaske
                + " Mandant=" + eclParamM.getClGlobalVar().mandant + " Login=" + pLogin + " SessionId=" + sessionId);
        if (bereitsAufgerufen == 1) {
            CaBug.druckeInfo("DoppelKlick");
            return false;
        }
        bereitsAufgerufen = 1;

        //		System.out.println(">>>>pruefeStart<<<< paufrufmaske="+pAufrufMaske+" aktuelleMaske="+aktuelleMaske+" Mandant="+ClGlobalVar.mandant+" Login="+eclTeilnehmerLoginM.getAnmeldeKennung()+" SessionId="+sessionId);
        for (int i = 0; i < pAufrufMaske.length; i++) {
            if (pAufrufMaske[i].compareTo("aLogin") == 0 || pAufrufMaske[i].compareTo("eLogin") == 0
                    || pAufrufMaske[i].compareTo("uLogin") == 0) {
                return true;
            }
            /*aLogin ist immer zulässig - egal was vorher war*/
            if (pAufrufMaske[i].compareTo(aktuelleMaske) == 0) {
                return true;
            }
        }
        CaBug.druckeInfo("DlgFehler");
        return false;
    }

    public String setzeEnde() {
        return setzeAUEnde();
    }

    public String setzeUEnde() {
        return setzeAUEnde();
    }

    public String setzeTEnde() {
        return setzeAUEnde();
    }

    private String setzeAUEnde() {
        bereitsAufgerufen = 0;
        return "";
    }

    /**pLogin: nur zur Protokollausgabe*/
    public String setzeEnde(String pNaechsteMaske, boolean pClearFehlermeldung, boolean pClearDlgVariablen,
            String pLogin) {
        return setzeAUEnde(pNaechsteMaske, pClearFehlermeldung, pClearDlgVariablen, pLogin, 1);
    }

    public String setzeUEnde(String pNaechsteMaske, boolean pClearFehlermeldung, boolean pClearDlgVariablen,
            String pLogin) {
        return setzeAUEnde(pNaechsteMaske, pClearFehlermeldung, pClearDlgVariablen, pLogin, 2);
    }

    public String setzeTEnde(String pNaechsteMaske, boolean pClearFehlermeldung, boolean pClearDlgVariablen,
            String pLogin) {
        return setzeAUEnde(pNaechsteMaske, pClearFehlermeldung, pClearDlgVariablen, pLogin, 3);
    }

    private String setzeAUEnde(String pNaechsteMaske, boolean pClearFehlermeldung, boolean pClearDlgVariablen,
            String pLogin, int pAOderU) {

        if (startPruefen == 0) {
            return pNaechsteMaske;
        }
        bereitsAufgerufen = 0;
        FacesContext fCtx = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
        String sessionId = session.getId();
        CaBug.druckeInfo(">>>>>setzeEnde<<<<< naechsteMaske=" + pNaechsteMaske + " Mandant="
                + eclParamM.getClGlobalVar().mandant + " Login=" + pLogin + " SessionId=" + sessionId);
        if (pClearFehlermeldung) {
            switch (pAOderU) {
            case 1:
                aDlgVariablen.clearFehlerMeldung();
                break;
            case 2:
                uSession.clearRequest();
                break;
            case 3:
                tSession.clearRequest();
                break;
            }
        }

        if (pClearDlgVariablen) {
            switch (pAOderU) {
            case 1:
                aDlgVariablen.clearDlg();
                break;
            case 2:
                uSession.clearSession();
                break;
            case 3:
                tSession.clearSession();
                break;
            }
        }
        aktuelleMaske = pNaechsteMaske;

        return pNaechsteMaske;
    }

    /****************************Low-Level-Funktionen*********************************/
    private FacesContext fCtx = null;

    public void liefereFacesContext() {
        fCtx = FacesContext.getCurrentInstance();
    }

    /**Vorbedingung: liefereFacesContext() muß aufgerufen sein*/
    public String getSessionID() {
        HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
        String sessionId = session.getId();
        return sessionId;
    }

    private String ipXForwardedFor = "";
    private String ipProxyClientIP = "";
    private String ipWLProxyClientIP = "";
    private String ipHTTPXFORWARDEDFOR = "";
    private String ipRemoteAddr = "";

    public void liefereClientIpAddr(String pKennung, DbBundle pDbBundle) {

        HttpServletRequest request = (HttpServletRequest) fCtx.getExternalContext().getRequest();

        /*Original*/
        //        String ip = request.getHeader("X-Forwarded-For");
        //        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        //            ip = request.getHeader("Proxy-Client-IP");
        //        }
        //        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        //            ip = request.getHeader("WL-Proxy-Client-IP");
        //        }
        //        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        //            ip = request.getHeader("HTTP_CLIENT_IP");
        //        }
        //        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        //            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        //        }
        //        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        //            ip = request.getRemoteAddr();
        //        }

        ipXForwardedFor = request.getHeader("X-Forwarded-For");
        if (ipXForwardedFor == null) {
            ipXForwardedFor = "null";
        }

        ipProxyClientIP = request.getHeader("Proxy-Client-IP");
        if (ipProxyClientIP == null) {
            ipProxyClientIP = "null";
        }

        ipWLProxyClientIP = request.getHeader("WL-Proxy-Client-IP");
        if (ipWLProxyClientIP == null) {
            ipWLProxyClientIP = "null";
        }

        ipHTTPXFORWARDEDFOR = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ipHTTPXFORWARDEDFOR == null) {
            ipHTTPXFORWARDEDFOR = "null";
        }

        ipRemoteAddr = request.getRemoteAddr();
        if (ipRemoteAddr == null) {
            ipRemoteAddr = "null";
        }

        EclIpTracking lIpTracking = new EclIpTracking();

        lIpTracking.mandant = pDbBundle.clGlobalVar.mandant;
        lIpTracking.ipXForwardedFor = ipXForwardedFor;
        lIpTracking.ipProxyClientIP = ipProxyClientIP;
        lIpTracking.ipWLProxyClientIP = ipWLProxyClientIP;
        lIpTracking.ipHTTPXFORWARDEDFOR = ipHTTPXFORWARDEDFOR;
        lIpTracking.ipRemoteAddr = ipRemoteAddr;
        lIpTracking.loginKennung = pKennung;
        lIpTracking.datumZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

        pDbBundle.dbIpTracking.insert(lIpTracking);
        //        System.out.println("ipXForwardedFor="+ipXForwardedFor+
        //        		"ipProxyClientIP="+ipProxyClientIP+
        //        		"ipWLProxyClientIP="+ipWLProxyClientIP+
        //        		"ipHTTPXFORWARDEDFOR="+ipHTTPXFORWARDEDFOR+
        //        		"ipRemoteAddr="+ipRemoteAddr
        //        		);
    }

    /******************Standard Setter und Getter**************************/

    public String getAktuelleMaske() {
        return aktuelleMaske;
    }

    public void setAktuelleMaske(String aktuelleMaske) {
        this.aktuelleMaske = aktuelleMaske;
    }

    public int getStartPruefen() {
        return startPruefen;
    }

    public void setStartPruefen(int startPruefen) {
        this.startPruefen = startPruefen;
    }

}
