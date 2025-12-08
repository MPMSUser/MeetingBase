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
package de.meetingapps.meetingportal.meetingportTFunktionen;

import java.io.IOException;
import java.util.Map;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComEntities.EclIpTracking;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/** Low-Level-Funktionen für Verwalten der JSF-Session */

@RequestScoped
@Named
public class TJsfSession {

    private int logDrucken = 3;

    public FacesContext liefereFacesContext() {
        FacesContext fCtx = FacesContext.getCurrentInstance();
        return fCtx;
    }

    public String getSessionID() {
        HttpSession session = (HttpSession) liefereFacesContext().getExternalContext().getSession(false);
        String ergebnis = session.getId();
        CaBug.druckeLog(ergebnis, logDrucken, 10);
        return ergebnis;
    }

    /* http://127.0.0.1/meetingportal/T001/portal.xhtml */
    public String getUrl() {
        HttpServletRequest request = (HttpServletRequest) liefereFacesContext().getExternalContext().getRequest();
        String urlString = request.getRequestURL().toString();
        
        return urlString;
    }
    
    public String getKomplettenAufruf() {
        String kompletterAufruf=getUrl()+"?";
        Map<String, String> paramMap = liefereFacesContext().getExternalContext().getRequestParameterMap();
        wasAngehaengt=false;
        kompletterAufruf+=aufrufParameter(paramMap, "zeit");
        kompletterAufruf+=aufrufParameter(paramMap, "hvjahr");
        kompletterAufruf+=aufrufParameter(paramMap, "hvnummer");
        kompletterAufruf+=aufrufParameter(paramMap, "datenbereich");
        kompletterAufruf+=aufrufParameter(paramMap, "mandant");
        kompletterAufruf+=aufrufParameter(paramMap, "sprache");
        kompletterAufruf+=aufrufParameter(paramMap, "test");
        kompletterAufruf+=aufrufParameter(paramMap, "nummer");
        kompletterAufruf+=aufrufParameter(paramMap, "p");
        kompletterAufruf+=aufrufParameter(paramMap, "bestaetigungscode");
        
        CaBug.druckeLog("kompletterAufruf"+kompletterAufruf, logDrucken, 3);
        return kompletterAufruf;
    }

    
    private boolean wasAngehaengt=false;
    private String aufrufParameter(Map<String, String> paramMap, String pParameter) {
        String hString= paramMap.get(pParameter);
        if (hString==null) {return "";}
        
        String rcString="";
        if (wasAngehaengt) {
            rcString="&";
        }
        else {wasAngehaengt=true;}
        return rcString+pParameter+"="+hString;
        
    }
    
    /** Returnwert=="" => kein Kürzel enthalten */
    public String getDesignKuerzel() {
        String urlString = getUrl();
        int pos = urlString.indexOf("meetingportal");
        if (pos == -1) {
            return "";
        }
        
        // ToDo: das ist MEGA gefährlich!!!
        pos = pos + 14;
        if (pos > urlString.length()) {
            return "";
        }
        int posEnde = urlString.indexOf("/", pos);
        if (posEnde == -1) {
            return "";
        }
        String designKuerzel = urlString.substring(pos, posEnde);
        return designKuerzel;
    }

    /**Löscht aktuelle Session und ruft pZiel auf*/
    public void loescheAktuelleSession(String pZiel) {
        CaBug.druckeLog("pZiel=" + pZiel, logDrucken, 10);
        ExternalContext externalContext = liefereFacesContext().getExternalContext();
        externalContext.invalidateSession();
        CaBug.druckeLog("Vor Redirect", logDrucken, 10);
        try {
            externalContext.redirect(pZiel);
        } catch (IOException e) {
            CaBug.drucke("001");
            e.printStackTrace();
        }
        CaBug.druckeLog("Nach Redirect", logDrucken, 10);

    }

    public EclIpTracking liefereTrackingInfo() {

        String ipXForwardedFor = "";
        String ipProxyClientIP = "";
        String ipWLProxyClientIP = "";
        String ipHTTPXFORWARDEDFOR = "";
        String ipRemoteAddr = "";

        HttpServletRequest request = (HttpServletRequest) liefereFacesContext().getExternalContext().getRequest();

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

        lIpTracking.ipXForwardedFor = ipXForwardedFor;
        lIpTracking.ipProxyClientIP = ipProxyClientIP;
        lIpTracking.ipWLProxyClientIP = ipWLProxyClientIP;
        lIpTracking.ipHTTPXFORWARDEDFOR = ipHTTPXFORWARDEDFOR;
        lIpTracking.ipRemoteAddr = ipRemoteAddr;
        lIpTracking.datumZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

        //      System.out.println("ipXForwardedFor="+ipXForwardedFor+
        //		"ipProxyClientIP="+ipProxyClientIP+
        //		"ipWLProxyClientIP="+ipWLProxyClientIP+
        //		"ipHTTPXFORWARDEDFOR="+ipHTTPXFORWARDEDFOR+
        //		"ipRemoteAddr="+ipRemoteAddr
        //		);

        return lIpTracking;
    }

    /*
       <session-config>
            <session-timeout>60</session-timeout>
        </session-config>
    
    	 */

    public void setTimeoutLang() {
        HttpSession session = (HttpSession) liefereFacesContext().getExternalContext().getSession(false);
        /**Stunden*60*60 = Sekunden*/
        session.setMaxInactiveInterval(14 * 60 * 60);
    }

    public void setTimeoutNormal() {
        HttpSession session = (HttpSession) liefereFacesContext().getExternalContext().getSession(false);
        /**Stunden*60*60 = Sekunden*/
        session.setMaxInactiveInterval(3 * 60 * 60);
    }

}
