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

import java.util.ArrayList;
import java.util.UUID;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComBl.BlAuftragAnbindungAktienregister;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostIban;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostIbanRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostWeitereZeichnung;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostWeitereZeichnungRC;
import de.meetingapps.meetingportal.meetComEntitiesku178.EprAddress;
import de.meetingapps.meetingportal.meetComEntitiesku178.EprApplicant;
import de.meetingapps.meetingportal.meetComEntitiesku178.EprBankAccount;
import de.meetingapps.meetingportal.meetComEntitiesku178.EprContact;
import de.meetingapps.meetingportal.meetComEntitiesku178.EprDepositor;
import de.meetingapps.meetingportal.meetComEntitiesku178.EprPostBeteiligungserhoehung;
import de.meetingapps.meetingportal.meetComEntitiesku178.EprPostBeteiligungserhoehungRC;
import de.meetingapps.meetingportal.meetComEntitiesku178.EprShare;
import de.meetingapps.meetingportal.meetingportTController.PAktionaersdatenSession;
import de.meetingapps.meetingportal.meetingportTController.PBeteiligungserhoehungSession;
import de.meetingapps.meetingportal.meetingportTController.TPermanentSession;
import de.meetingapps.meetingportal.meetingportTController.TPublikationenSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BrMBeteiligungserhoehung {

    private int logDrucken = 10;

    private @Inject EclDbM eclDbM;
    private @Inject PBeteiligungserhoehungSession pBeteiligungserhoehungSession;
    private @Inject TPermanentSession tPermanentSession;
    private @Inject BrMku178Request brMku178Request;
    private @Inject PAktionaersdatenSession pAktionaersdatenSession;
    private @Inject BrMGenossenschaftCall brMGenossenschaftCall;
    private @Inject TPublikationenSession tPublikationenSession;
    private @Inject EclLoginDatenM eclLoginDatenM;

    public String antragErstellen(String pAktionaersnummer, String uuid) {

        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis
                .aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);

        EprPostBeteiligungserhoehung eprPostBeteiligungserhoehung = new EprPostBeteiligungserhoehung();

        EprContact eprContact = new EprContact();
        eprContact.setEmail(pAktionaersdatenSession.getAktionaerEmailPortal());
        eprContact.setPhone(pAktionaersdatenSession.getAllTelefon());
        eprContact.setMobile("");

        EprAddress eprAddressMitglied = new EprAddress();
        eprAddressMitglied.setStreet(pAktionaersdatenSession.getAktionaerStrasse());
        eprAddressMitglied.setAdditionalInfo(pAktionaersdatenSession.getAktionaerAdresszusatz());
        eprAddressMitglied.setPostalCode(pAktionaersdatenSession.getAktionaerPLZ());
        eprAddressMitglied.setCity(pAktionaersdatenSession.getAktionaerOrt());
        eprAddressMitglied.setCountry(pAktionaersdatenSession.getAktionaerLand());

        EprApplicant eprApplicant = new EprApplicant();
        eprApplicant.setMembershipID(Integer.parseInt(aktionaersnummerFuerGenossenschaftSysWebrequest));
        eprApplicant.setMember(true);
        eprApplicant.setName(pAktionaersdatenSession.getAktionaerNachname());
        eprApplicant.setForename(pAktionaersdatenSession.getAktionaerVorname());
        eprApplicant.setSalutation(pAktionaersdatenSession.getAktionaerAnrede());
        eprApplicant.setAddress(eprAddressMitglied);
        eprApplicant.setContact(eprContact);
        if (!pAktionaersdatenSession.getAktionaerNachname().contains("GbR")
                && !pAktionaersdatenSession.getAktionaerNachname().contains("Gesamthandsgemeinschaft")) {
            eprApplicant.setIdnr(pAktionaersdatenSession.getAllSteuerId());
            eprApplicant.setBirthday(pAktionaersdatenSession.getAllGeburtsdatum());
        }

        eprPostBeteiligungserhoehung.setApplicant(eprApplicant);

        EprShare eprShare = new EprShare();
        eprShare.setRequestedQuantity(pBeteiligungserhoehungSession.getWeitereAnteile());

        eprPostBeteiligungserhoehung.setShare(eprShare);

        ArrayList<EprBankAccount> bankAccounts = new ArrayList<EprBankAccount>();

        EprBankAccount eprBankAccount_einzahlung = new EprBankAccount();
        eprBankAccount_einzahlung.setSepaDirectDebitMandate(false);
        eprBankAccount_einzahlung.setIban(pAktionaersdatenSession.getBankIban());
        eprBankAccount_einzahlung.setBic(pAktionaersdatenSession.getBankBic());
        eprBankAccount_einzahlung.setBank(pAktionaersdatenSession.getBankBankname());
        if (pBeteiligungserhoehungSession.getZahlungsart().equals("1")) {
            eprBankAccount_einzahlung.setSepaDirectDebitMandate(true);
        }
        if (pAktionaersdatenSession.isBankverbindungFehlt()) {
            eprBankAccount_einzahlung.setIban(pBeteiligungserhoehungSession.getSepaKontoSelfTempIBAN());
            eprBankAccount_einzahlung.setBic(pBeteiligungserhoehungSession.getSepaKontoSelfTempBIC());
            eprBankAccount_einzahlung.setBank(pBeteiligungserhoehungSession.getSepaKontoSelfTempBank());
        }
        if (pBeteiligungserhoehungSession.getSepaKontoAbweichend()) {
            eprBankAccount_einzahlung.setIban(pBeteiligungserhoehungSession.getSepaKontoAbweichendIBAN());
            eprBankAccount_einzahlung.setBic(pBeteiligungserhoehungSession.getSepaKontoAbweichendBIC());
            eprBankAccount_einzahlung.setBank(pBeteiligungserhoehungSession.getSepaKontoAbweichendBank());
            EprDepositor eprDepositor_einzahlung = new EprDepositor();
            eprDepositor_einzahlung.setForename(pBeteiligungserhoehungSession.getSepaKontoAbweichendVorname());
            eprDepositor_einzahlung.setName(pBeteiligungserhoehungSession.getSepaKontoAbweichendNachname());
            if (eprBankAccount_einzahlung.isSepaDirectDebitMandate()) {
                EprAddress eprAddressEinzahlung = new EprAddress();
                eprAddressEinzahlung.setStreet(pBeteiligungserhoehungSession.getSepaKontoAbweichendStrasse());
                eprAddressEinzahlung.setPostalCode(pBeteiligungserhoehungSession.getSepaKontoAbweichendPLZ());
                eprAddressEinzahlung.setCity(pBeteiligungserhoehungSession.getSepaKontoAbweichendOrt());
                eprDepositor_einzahlung.setAddress(eprAddressEinzahlung);
            }
            eprBankAccount_einzahlung.setDepositor(eprDepositor_einzahlung);
        }

        if (pBeteiligungserhoehungSession.getZahlungsart().equals("1")) {
            bankAccounts.add(eprBankAccount_einzahlung);
        }

        EprBankAccount eprBankAccount_auszahlung = new EprBankAccount();
        eprBankAccount_auszahlung.setSepaDirectDebitMandate(false);
        eprBankAccount_auszahlung.setPayments(true);
        eprBankAccount_auszahlung.setIban(pAktionaersdatenSession.getBankIban());
        eprBankAccount_auszahlung.setBic(pAktionaersdatenSession.getBankBic());
        eprBankAccount_auszahlung.setBank(pAktionaersdatenSession.getBankBankname());

        if (!pAktionaersdatenSession.isBankverbindungFehlt()) {
            bankAccounts.add(eprBankAccount_auszahlung);
        }

        eprPostBeteiligungserhoehung.setBankAccount(bankAccounts);

        eprPostBeteiligungserhoehung.setPaymentMethod("banktransfer");
        if (pBeteiligungserhoehungSession.getZahlungsart().equals("1")) {
            eprPostBeteiligungserhoehung.setPaymentMethod("sepa");
        }
        eprPostBeteiligungserhoehung.setMagazineDelivery("none");
        if (tPublikationenSession.getVersandartNewsletter().equals("1")) {
            eprPostBeteiligungserhoehung.setMagazineDelivery("post");
        } else if (tPublikationenSession.getVersandartNewsletter().equals("2")) {
            eprPostBeteiligungserhoehung.setMagazineDelivery("digital");
        }
        eprPostBeteiligungserhoehung.setSigningType("analog");
        if (pBeteiligungserhoehungSession.getSignaturArt().equals("1")) {
            eprPostBeteiligungserhoehung.setSigningType("digital");
        }
        eprPostBeteiligungserhoehung.setConfirmPayment(pBeteiligungserhoehungSession.getConfirmPayment());
        eprPostBeteiligungserhoehung.setTransmissionID(uuid);

        EprPostBeteiligungserhoehungRC eprPostBeteiligungserhoehungRC = brMku178Request
                .doPostRequestBeteiligungserhoehung(eprPostBeteiligungserhoehung);

        return eprPostBeteiligungserhoehungRC.getPdfFile();
    }

    /*
     * Ticket bei ku178 erstellen
     */
    public String beteiligunbgserhoehungSpeichern(String pAktionaersnummer) {

        String uuid = "";

        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis
                .aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);

        /****************** Speichern *******************************/
        String lSchluessel = "";
        String lZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

        if (tPermanentSession.isTestModus()) {

        } else {
            uuid = UUID.randomUUID().toString();
            EgxPostWeitereZeichnung egxPostWeitereZeichnung = null;
            egxPostWeitereZeichnung = new EgxPostWeitereZeichnung();
            egxPostWeitereZeichnung.auftragsnummer = uuid;
            egxPostWeitereZeichnung.email_mitglied = pAktionaersdatenSession.getAktionaerEmail();
            egxPostWeitereZeichnung.anteile = pBeteiligungserhoehungSession.getWeitereAnteile();
            egxPostWeitereZeichnung.zustimmungsatzung = "J";
            egxPostWeitereZeichnung.erklaerungweiterezeichnung = "J";
            egxPostWeitereZeichnung.verpflichtungeinzahlung = "J";
            egxPostWeitereZeichnung.datenschutzklausel = "J";
            egxPostWeitereZeichnung.zeichnungzahlungspflichtigbeantragen = "J";
            egxPostWeitereZeichnung.unterschriftsart = "Digital";
            if (pBeteiligungserhoehungSession.getZahlungsart().equals("1")) {
                egxPostWeitereZeichnung.zahlungsart = "Lastschrift";
                egxPostWeitereZeichnung.ibanlast = pAktionaersdatenSession.getBankIban().replace(" ", "").trim();
                egxPostWeitereZeichnung.namelast = pAktionaersdatenSession.getBankAktionaersname();
                if(pAktionaersdatenSession.isBankverbindungFehlt()) {
                    egxPostWeitereZeichnung.ibanlast = pBeteiligungserhoehungSession.getSepaKontoSelfTempIBAN().replace(" ", "").trim();
                    egxPostWeitereZeichnung.namelast = (pBeteiligungserhoehungSession.getSepaKontoSelfTempVorname() + " "  + pBeteiligungserhoehungSession.getSepaKontoSelfTempNachname()).trim();
                }
            } else {
                egxPostWeitereZeichnung.zahlungsart = "Überweisung";
            }

            if (pBeteiligungserhoehungSession.getSepaKontoAbweichend()) {
                egxPostWeitereZeichnung.abw_kontoinhaber = "J";
                if (!pBeteiligungserhoehungSession.getSepaKontoAbweichendAnrede().equals("0")) {
                    egxPostWeitereZeichnung.anrede = getAnrede(
                            pBeteiligungserhoehungSession.getSepaKontoAbweichendAnrede());
                }
                egxPostWeitereZeichnung.titel = pBeteiligungserhoehungSession.getSepaKontoAbweichendTitel();
                egxPostWeitereZeichnung.nachname = pBeteiligungserhoehungSession.getSepaKontoAbweichendNachname();
                egxPostWeitereZeichnung.vorname = pBeteiligungserhoehungSession.getSepaKontoAbweichendVorname();
                egxPostWeitereZeichnung.namelast = (pBeteiligungserhoehungSession.getSepaKontoAbweichendTitel() + " "
                        + pBeteiligungserhoehungSession.getSepaKontoAbweichendVorname() + " "
                        + pBeteiligungserhoehungSession.getSepaKontoAbweichendNachname()).trim();
                egxPostWeitereZeichnung.ibanlast = pBeteiligungserhoehungSession.getSepaKontoAbweichendIBAN()
                        .replace(" ", "").trim();
                egxPostWeitereZeichnung.strasse = pBeteiligungserhoehungSession.getSepaKontoAbweichendStrasse();
                egxPostWeitereZeichnung.plz = pBeteiligungserhoehungSession.getSepaKontoAbweichendPLZ();
                egxPostWeitereZeichnung.ort = pBeteiligungserhoehungSession.getSepaKontoAbweichendOrt();
                egxPostWeitereZeichnung.land = pBeteiligungserhoehungSession.getSepaKontoAbweichendLand();
                egxPostWeitereZeichnung.email = pBeteiligungserhoehungSession.getSepaKontoAbweichendEmail();
            } else {
                egxPostWeitereZeichnung.abw_kontoinhaber = "N";
            }

            EgxPostWeitereZeichnungRC egxPostWeitereZeichnungRC = brMGenossenschaftCall
                    .doPostRequestWeitereZeichnung(aktionaersnummerFuerGenossenschaftSysWebrequest, egxPostWeitereZeichnung);

            if (egxPostWeitereZeichnungRC == null) {
                return null;
            }

            lSchluessel = egxPostWeitereZeichnungRC.lfnr + ";11110";
            CaBug.druckeLog(lSchluessel, logDrucken, 10);

            String art = "3";

            if (pBeteiligungserhoehungSession.getAuffuellBetragZuUeberweisen() > 0) {
                art = "1";
            }

            BlAuftragAnbindungAktienregister lAuftragAnbindungAktienregister = new BlAuftragAnbindungAktienregister(
                    eclDbM.getDbBundle());
            lAuftragAnbindungAktienregister.antragBeteiligungserhoehung(aktionaersnummerFuerGenossenschaftSysWebrequest,
                    eclLoginDatenM.getEclLoginDaten().ident * (-1), lSchluessel, lZeit,
                    pBeteiligungserhoehungSession.getVerwendbarerBetragAnteileString(),
                    pBeteiligungserhoehungSession.getAuffuellBetragZuUeberweisenString(),
                    pBeteiligungserhoehungSession.getWeitereAnteile(), art,
                    pBeteiligungserhoehungSession.getZahlungsart());
        }

        return uuid;

    }

    /*
     * Nur Auffüllbetrag
     */
    public int auffuellbetragSpeichern(String pAktionaersnummer) {

        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis
                .aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);

        String lSchluessel = "";
        String lZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

        String art = "2";

        BlAuftragAnbindungAktienregister lAuftragAnbindungAktienregister = new BlAuftragAnbindungAktienregister(
                eclDbM.getDbBundle());
        lAuftragAnbindungAktienregister.antragBeteiligungserhoehung(aktionaersnummerFuerGenossenschaftSysWebrequest,
                eclLoginDatenM.getEclLoginDaten().ident * (-1), lSchluessel, lZeit,
                pBeteiligungserhoehungSession.getVerwendbarerBetragAnteileString(),
                pBeteiligungserhoehungSession.getAuffuellBetragZuUeberweisenString(),
                pBeteiligungserhoehungSession.getWeitereAnteile(), art, pBeteiligungserhoehungSession.getZahlungsart());

        return 1;
    }

    /*
     * IBAN prüfen
     */
    public Boolean checkIBAN(String pAktionaersnummer, String iban) {
        EgxPostIban egxPostIban = new EgxPostIban();
        EgxPostIbanRC egxPostIbanRC = brMGenossenschaftCall.doPostRequestIban(iban, iban, egxPostIban);
        return egxPostIbanRC.success;
    }

    private String getAnrede(String anrede) {
        String anredeString = "";
        switch (anrede) {
        case "0":
            anredeString = "";
            break;
        case "1":
            anredeString = "Herr";
            break;
        case "2":
            anredeString = "Frau";
            break;
        case "3":
            anredeString = "divers";
            break;
        case "4":
            anredeString = "Firma";
            break;
        }
        return anredeString;
    }
}
