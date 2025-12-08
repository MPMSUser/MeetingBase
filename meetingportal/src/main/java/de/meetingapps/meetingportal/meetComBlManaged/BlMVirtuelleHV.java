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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEntities.EclMitteilung;
import de.meetingapps.meetingportal.meetComKonst.KonstMitteilungStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalTexte;
import de.meetingapps.meetingportal.meetingportTController.TMitteilungSession;
import de.meetingapps.meetingportal.meetingportTFunktionen.TBestandDurcharbeiten;
import de.meetingapps.meetingportal.meetingportTFunktionen.TQuittungen;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BlMVirtuelleHV {

    private int logDrucken=10;
    
    private @Inject EclParamM eclParamM;
    private @Inject EclDbM eclDbM;
    private @Inject BaMailM baMailm;
    private @Inject EclPortalTexteM eclPortalTexteM;
    private @Inject TMitteilungSession tMitteilungSession;
    private @Inject TBestandDurcharbeiten tBestandDurcharbeiten;
    private @Inject TQuittungen tQuittungen;
    
    
    public void schickeMailFuerMitteilung(EclMitteilung lMitteilung) {
        CaBug.druckeLog("1", logDrucken, 10);
       if (tMitteilungSession.getMailBeiEingang() == 1) {
            String hMailText = "", hBetreff = "";
            CaBug.druckeLog("tMitteilungSession.getTextNrMailBetreff()="+tMitteilungSession.getTextNrMailBetreff(), logDrucken, 10);
           
            hBetreff = 
                    eclPortalTexteM.holeText(tMitteilungSession.getTextNrMailBetreff())
                    +eclParamM.getEmittentName();
           
            
            String kennungExtern=BlNummernformBasis.aufbereitenKennungFuerExtern(lMitteilung.identString, eclDbM.getDbBundle());
            
            hMailText = 
                    eclPortalTexteM.holeText(tMitteilungSession.getTextNrMailLfdIdent())
                    + Integer.toString(lMitteilung.mitteilungIdent) + "\n\r"
                    
                    + eclPortalTexteM.holeText(tMitteilungSession.getTextNrMailKennung())
                    + kennungExtern

                    + eclPortalTexteM.holeText(tMitteilungSession.getTextNrMailSteller())
                    + "\n\r"
                    + lMitteilung.nameVornameOrtKennung+"\n\r";
            
            if (tMitteilungSession.getNameAbfragen()>0) {
                hMailText=hMailText
                        + eclPortalTexteM.holeText(tMitteilungSession.getTextNrMailNameAbgefragt())
                        +"\n\r"
                        +lMitteilung.nameVornameOrt+"\n\r";
            }
 
            if (tMitteilungSession.getKontaktdatenAbfragen()>0) {
                hMailText=hMailText
                        + eclPortalTexteM.holeText(tMitteilungSession.getTextNrMailKontaktdaten())
                        +"\n\r"
                        +lMitteilung.kontaktDaten+"\n\r";
            }
            
            if (tMitteilungSession.getKontaktdatenTelefonAbfragen()>0) {
                hMailText=hMailText
                        + eclPortalTexteM.holeText(tMitteilungSession.getTextNrMailKontaktdatenTelefon())
                        +"\n\r"
                        +lMitteilung.kontaktDatenTelefon+"\n\r";
            }

            String textVorAktien="";
            for (int i=1;i<=5;i++) {
                if (lMitteilung.gattungen[i]==1) {
                    if (!textVorAktien.isEmpty()) {
                        textVorAktien+=" ";
                    }
                    switch (i) {
                    case 1:textVorAktien+=eclPortalTexteM.holeIText(KonstPortalTexte.ALLGEMEIN_GATTUNG1_BESITZ_VOR_AKTIEN);break;
                    case 2:textVorAktien+=eclPortalTexteM.holeIText(KonstPortalTexte.ALLGEMEIN_GATTUNG2_BESITZ_VOR_AKTIEN);break;
                    case 3:textVorAktien+=eclPortalTexteM.holeIText(KonstPortalTexte.ALLGEMEIN_GATTUNG3_BESITZ_VOR_AKTIEN);break;
                    case 4:textVorAktien+=eclPortalTexteM.holeIText(KonstPortalTexte.ALLGEMEIN_GATTUNG4_BESITZ_VOR_AKTIEN);break;
                    case 5:textVorAktien+=eclPortalTexteM.holeIText(KonstPortalTexte.ALLGEMEIN_GATTUNG5_BESITZ_VOR_AKTIEN);break;
                    }
                    
                }
            }
            if (textVorAktien.isEmpty()) {
                textVorAktien=eclPortalTexteM.holeText(tMitteilungSession.getTextNrMailAktien());
            }
            hMailText=hMailText
                    + textVorAktien+" "
                    +CaString.toStringDE(lMitteilung.anzahlAktienZumZeitpunktderMitteilung)+"\n\r";
            
            if (tMitteilungSession.getHinweisGelesen()>0) {
               String lInhalt="";
               if (lMitteilung.hinweisWurdeBestaetigt==1) {
                   lInhalt="Ja";
               }
               else {
                   lInhalt="Nein";
               }
               hMailText=hMailText
                       + eclPortalTexteM.holeText(tMitteilungSession.getTextNrMailHinweisBestaetigt())
                       +lInhalt+"\n\r";
            }

            if (tMitteilungSession.getTopListeAnbieten()>0) {
                hMailText=hMailText+
                        eclPortalTexteM.holeIText(1889)+
                        lMitteilung.zuTOPListe+"\n\r";
            }
            
            for (int i=0;i<10;i++) {
                if (lMitteilung.inhaltsHinweis[i]) {
                    hMailText=hMailText+eclParamM.getParam().paramPortal.inhaltsHinweiseTextDE[i]+"\n\r";
                }
            }
            
            if (tMitteilungSession.getKurztextAbfragen()>0) {
                hMailText=hMailText
                        +                    eclPortalTexteM.holeText(tMitteilungSession.getTextNrMailKurztext())

                        +"\n\r"
                        +lMitteilung.mitteilungKurztext+"\n\r";
            }

            if (tMitteilungSession.getLangtextAbfragen()>0) {
                hMailText=hMailText
                        +                    eclPortalTexteM.holeText(tMitteilungSession.getTextNrMailLangtext())

                        +"\n\r"
                        +lMitteilung.mitteilungLangtext+"\n\r";
            }
            CaBug.druckeLog("50", logDrucken, 10);
           
            hMailText=hMailText
                    +                    eclPortalTexteM.holeText(tMitteilungSession.getTextNrMailZeitpunktErteilt())
                    + CaDatumZeit.DatumZeitStringFuerAnzeige(lMitteilung.zeitpunktDerMitteilung)+"\n\r";

            if (lMitteilung.status==KonstMitteilungStatus.ZURUECKGEZOGEN) {
                hMailText=hMailText
                        +                    eclPortalTexteM.holeText(tMitteilungSession.getTextNrMailZurueckgezogen())
                        +"\n\r";
                
                hMailText=hMailText
                        +                    eclPortalTexteM.holeText(tMitteilungSession.getTextNrMailZeitpuntZurueckgezogen())
                        +CaDatumZeit.DatumZeitStringFuerAnzeige(lMitteilung.zeitpunktDesRueckzugs)+"\n\r";

            }

            if (lMitteilung.artDerMitteilung==KonstPortalFunktionen.widersprueche) {
                hMailText+="\n\r";
                hMailText=hMailText
                        + eclPortalTexteM.holeIText(1493)
                        +"\n\r";
                for (int i=0;i<tBestandDurcharbeiten.getRcAktienregisternummer().size();i++) {
                    hMailText=hMailText
                            + eclPortalTexteM.holeIText(1494)+tBestandDurcharbeiten.getRcAktienregisternummer().get(i)+" ";
                    int meldeIdent=tBestandDurcharbeiten.getRcMeldungsIdent().get(i);
                    if (meldeIdent!=0) {
                        hMailText=hMailText
                                + eclPortalTexteM.holeIText(1495)+Integer.toString(meldeIdent);
                    }
                    hMailText=hMailText
                            +"\n\r";
                }
                
            }

//            String empfaengerListe = tMitteilungSession.getMailVerteiler1() + ";"
//                    + tMitteilungSession.getMailVerteiler2() + ";"
//                    + tMitteilungSession.getMailVerteiler3() + ";";
//            String[] zeileSplit = empfaengerListe.split(";");
//            for (int i = 0; i < zeileSplit.length; i++) {
//                String[] zeileSplitKomma=zeileSplit[i].split(",");
//                for (int i1=0;i1<zeileSplitKomma.length;i1++) {
//                    String empfaenger = zeileSplitKomma[i1].trim();
//                    if (!empfaenger.isEmpty()) {
//                        System.out.println("Mitteilung senden an:" + empfaenger);
//                        baMailm.senden(empfaenger, hBetreff, hMailText);
//                    }
//                }
//            }
            
            String empfaengerListe = tMitteilungSession.getMailVerteiler1() + ","
                    + tMitteilungSession.getMailVerteiler2() + ","
                    + tMitteilungSession.getMailVerteiler3() + ",";
            String[] zeileSplitKomma=empfaengerListe.split(";");
            for (int i1=0;i1<zeileSplitKomma.length;i1++) {
                String empfaenger = zeileSplitKomma[i1].trim();
                if (!empfaenger.isEmpty()) {
                    CaBug.druckeLog("Mitteilung senden an:" + empfaenger, logDrucken, 1);
                    baMailm.senden(empfaenger, hBetreff, hMailText);
                }
            }

        }
       tQuittungen.bestaetigungMitteilung(lMitteilung);

    }

    
}
