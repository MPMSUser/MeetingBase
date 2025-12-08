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
package de.meetingapps.meetingportal.meetComBrM;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetAdressenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetBankdatenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetKuendigungRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetLaenderRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetMitgliedJNRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersoenlicheDatenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersonendatenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPlzOrtRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetTicketUebersichtRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetUmsaetzeRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostAdressaenderung;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostAdressaenderungRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostBankverbindung;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostBankverbindungRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostEmailaenderung;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostEmailaenderungRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostGeburtsdatum;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostGeburtsdatumRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostIban;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostIbanRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostKontakt;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostKontaktRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostKuendigungsruecknahme;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostKuendigungsruecknahmeRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostNewsletter;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostNewsletterRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostSteuerid;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostSteueridRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostTelefon;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostTelefonRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostWeitereZeichnung;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostWeitereZeichnungRC;
import de.meetingapps.meetingportal.meetingportTController.TPermanentSession;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.InvalidKeyException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BrMGenossenschaftCall {

    /*HIer Online-Schnittstelle zu Mitgliederverzeichnis implementieren*/
    
    private int logDrucken = 3;

    private @Inject EclParamM eclParamM;
    private @Inject TPermanentSession tPermanentSession;

    private static final String GET_REQUEST_PFAD_MITGLIED_JN = "betterorange/v1/mitgliedjn";
    private static final String GET_REQUEST_PFAD_PERSOENLICHE_DATEN = "betterorange/v1/persoenliche_daten";
    private static final String GET_REQUEST_PFAD_KUENDIGUNG = "betterorange/v1/kuendigung";
    private static final String GET_REQUEST_PFAD_LAENDER = "betterorange/v1/laender";
    private static final String GET_REQUEST_PFAD_PERSONENDATEN = "betterorange/v1/personendaten";
    private static final String GET_REQUEST_PFAD_PLZ_ORT = "betterorange/v1/plz_ort";
    private static final String GET_REQUEST_PFAD_BANKDATEN = "betterorange/v1/bankdaten";
    private static final String GET_REQUEST_PFAD_TICKET_UEBERSICHT = "betterorange/v1/ticket_uebersicht";
    
    private static final String GET_REQUEST_PFAD_UMSAETZE_AJ = "betterorange/v1/umsaetze_aj";
    private static final String GET_REQUEST_PFAD_UMSAETZE_ALLE = "betterorange/v1/umsaetze_alle";
    private static final String GET_REQUEST_PFAD_UMSAETZE_ZEITRAUM = "betterorange/v1/umsaetze_zeitraum/{erstes_datum}/{zweites_datum}";
    
    private static final String GET_REQUEST_PFAD_ADRESSEN = "betterorange/v1/adressen";
    
    private static final String POST_REQUEST_PFAD_ADRESSAENDERUNG = "betterorange/v1/adressaenderung";
    private static final String POST_REQUEST_PFAD_BANKVERBINDUNG = "betterorange/v1/bankverbindung";
    private static final String POST_REQUEST_PFAD_EMAILAENDERUNG = "betterorange/v1/emailaenderung";
    private static final String POST_REQUEST_PFAD_GEBURTSDATUM = "betterorange/v1/geburtsdatum";
    private static final String POST_REQUEST_PFAD_NEWSLETTER = "betterorange/v1/newsletter";
    private static final String POST_REQUEST_PFAD_STEUERID = "betterorange/v1/steuerid";
    private static final String POST_REQUEST_PFAD_TELEFON = "betterorange/v1/telefon";
    private static final String POST_REQUEST_PFAD_KONTAKT = "betterorange/v1/kontakt";
    private static final String POST_REQUEST_PFAD_WEITERE_ZEICHNUNG = "betterorange/v1/weitere_zeichnung";
    private static final String POST_REQUEST_PFAD_IBAN = "/v1/iban/";
    private static final String POST_REQUEST_PFAD_KUENDIGUNGsRUECKNAHME = "betterorange/v1/kuendigungsruecknahme";

    private String doPostRequest(String requestPfad, String mitgliedsnummer, String requestBody)
            throws ClientProtocolException, IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
            return null;
    }

    private String doPostRequest(String requestPfad, String mitgliedsnummer, String urlParameter, String requestBody)
            throws ClientProtocolException, IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        return null;
    }

    private String doGetRequest(String requestPfad, String mitgliedsnummer) throws ClientProtocolException, IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        return null;
    }
    
    private String doGetRequest(String requestPfad) throws ClientProtocolException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        return null;
    }

    private String decodeResponse(HttpResponse httpResponse) {
        return null;
    }

    /*
     * Start JWT-Token
     */

    private void generateJwt(String mitgliedsnumer) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        return ;
    }

    private Date addSeconds(Date date, int seconds) {
        return null;
    }

    /*
     * Ende JWT-Token
     */

    /*
     * Start Get-Requests
     */

    public EgxGetKuendigungRC doGetRequestKuendigung(String mitgliedsnummer) {
        return null;
    }

    public EgxGetLaenderRC doGetRequestLaender(String mitgliedsnummer) {
        return null;
    }

    public EgxGetMitgliedJNRC doGetRequestMitgliedJN(String mitgliedsnummer) {
        return null;
    }

    private String emailTest(String pGelieferteEMail) {
        return null;
    }

    public EgxGetPersoenlicheDatenRC doGetRequestPersoenlicheDaten(String mitgliedsnummer) {
        return null;
    }

    public EgxGetPersonendatenRC doGetRequestPersonendaten(String mitgliedsnummer) {
        return null;
    }

    public EgxGetPlzOrtRC doGetRequestPlzOrt(String mitgliedsnummer) {
        return null;
    }

    public EgxGetPlzOrtRC doGetRequestPlzOrt(String mitgliedsnummer, String link) {
        return null;
    }

    public EgxGetBankdatenRC doGetRequestBankdaten(String mitgliedsnummer) {
        return null;
    }

    public EgxGetBankdatenRC doGetRequestBankdaten(String mitgliedsnummer, String link) {
        return null;
    }

    public EgxGetUmsaetzeRC doGetRequestUmsaetzeAj(String mitgliedsnummer) {
        return null;
    }
    
    public EgxGetUmsaetzeRC doGetRequestUmsaetzeAlle(String mitgliedsnummer) {
        return null;
    }
    
    public EgxGetUmsaetzeRC doGetRequestUmsaetzeZeitraum(String mitgliedsnummer, String erstes_datum, String zweites_datum) {
        return null;
    }
    
    public EgxGetTicketUebersichtRC doGetRequestTicketUebersicht(String mitgliedsnummer) {
        return null;
    }
    
    public EgxGetAdressenRC doGetRequestAdressen(String page) {
        return null;
    }

    /*
     * Ende Get-Requests
     */

    /*
     * Start Post-Requests
     */

    public EgxPostAdressaenderungRC doPostRequestAdressaenderung(String mitgliedsnummer, EgxPostAdressaenderung egxPostAdressaenderung) {
        return null;
    }

    public EgxPostBankverbindungRC doPostRequestBankverbindung(String mitgliedsnummer, EgxPostBankverbindung egxPostBankverbindung) {
        return null;
    }

    public EgxPostEmailaenderungRC doPostRequestEmailaenderung(String mitgliedsnummer, EgxPostEmailaenderung egxPostEmailaenderung) {
        return null;
    }

    public EgxPostGeburtsdatumRC doPostRequestGeburtsdatum(String mitgliedsnummer, EgxPostGeburtsdatum egxPostGeburtsdatum) {
        return null;
    }

    public EgxPostNewsletterRC doPostRequestNewsletter(String mitgliedsnummer, EgxPostNewsletter egxPostNewsletter) {
        return null;
    }

    public EgxPostSteueridRC doPostRequestSteuerid(String mitgliedsnummer, EgxPostSteuerid egxPostSteuerid) {
        return null;
    }

    public EgxPostTelefonRC doPostRequestTelefon(String mitgliedsnummer, EgxPostTelefon egxPostTelefon) {
        return null;
    }

    public EgxPostKontaktRC doPostRequestKontakt(String mitgliedsnummer, EgxPostKontakt egxPostKontakt) {
        return null;
    }

    public EgxPostIbanRC doPostRequestIban(String mitgliedsnummer, String iban, EgxPostIban egxPostIban) {
        return null;
    }
    
    public EgxPostWeitereZeichnungRC doPostRequestWeitereZeichnung(String mitgliedsnummer, EgxPostWeitereZeichnung egxPostWeitereZeichnung) {
        return null;
    }
    
    public EgxPostKuendigungsruecknahmeRC doPostKuendigungsruecknahme(String mitgliedsnummer, EgxPostKuendigungsruecknahme egxPostKuendigungsruecknahme) {
        return null;
        
    }

    /*
     * Ende Post-Requests
     */

    /*
     * Start ping
     */

    public Boolean sendPingRequest(String ipAddress) throws UnknownHostException, IOException {
        return null;
    }

    /*
     * Ende Ping
     */
    
    /*
     * Pfadoptionen
     */
    private String getUmsaetzeZeitraumUrl(String erstes_datum, String zweites_datum) {
        return null;
    }
}