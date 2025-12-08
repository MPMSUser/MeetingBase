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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComBVerwaltung.BvMandanten;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEh.EhRedakteur;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclParameter;
import de.meetingapps.meetingportal.meetComKonst.KonstParameterIdent;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class URedakteur {

    
    @Inject
    private EclParamM eclParamM;
    @Inject
    private EclDbM eclDbM;
    @Inject
    private URedakteurSession uRedakteurSession;

    /**Open erfolgt in der Funktion.
     * 
     * Darf nur aufgerufen werden, wenn kein Mandant belegt!!!*/
    public void init() {
        eclDbM.openAllOhneParameterCheck();
        eclDbM.openWeitere();
        
        /*Holen der Liste aller Emittenten*/
        BvMandanten bvMandanten = new BvMandanten();
        List<EclEmittenten> lEmittentenListe = null;
        List<EhRedakteur> lRedakteurListe=new LinkedList<EhRedakteur>();
        
        int standardOderNurPOderAlle = 2;
        lEmittentenListe = bvMandanten.liefereMandanten(eclDbM.getDbBundle(), standardOderNurPOderAlle);
        if (lEmittentenListe != null) {
            for (int i = 0; i < lEmittentenListe.size(); i++) {
                EclEmittenten lEmittent=lEmittentenListe.get(i);
                if (lEmittent.portalStandard==1) {
                    eclParamM.getClGlobalVar().mandant=lEmittent.mandant;
                    eclParamM.getClGlobalVar().hvJahr=lEmittent.hvJahr;
                    eclParamM.getClGlobalVar().hvNummer=lEmittent.hvNummer;
                    eclParamM.getClGlobalVar().datenbereich=lEmittent.dbArt;
                    
                    EclParameter lParameter=new EclParameter();
                    lParameter.mandant=lEmittent.mandant;
                    lParameter.ident=KonstParameterIdent.FRAGEN_EXTERNER_ZUGRIFF;
                    eclDbM.getDbBundle().dbParameter.read(lParameter);
                    lParameter=eclDbM.getDbBundle().dbParameter.ergebnisPosition(0);
                    
                    if (lParameter!=null) {
                        if (lParameter.wert.equals("1")) {
                            EhRedakteur lRedakteur=new EhRedakteur(lEmittent);

                            /*Mail Consultant einlesen*/
                            lParameter=new EclParameter();
                            lParameter.mandant=lEmittent.mandant;
                            lParameter.ident=KonstParameterIdent.L_MAIL_CONSULTANT;
                            eclDbM.getDbBundle().dbParameter.readLang(lParameter);
                            lRedakteur.mailConsultant=eclDbM.getDbBundle().dbParameter.ergebnisPosition(0).wert;

                            /*Anzahl Fragen einlesen*/
                            eclDbM.getDbBundle().dbMitteilung.setzeFunktion(KonstPortalFunktionen.fragen);
                            eclDbM.getDbBundle().dbMitteilung.readAll_mitteilungen(0);
                            lRedakteur.anzahlFragen=eclDbM.getDbBundle().dbMitteilung.anzErgebnis();

                            lRedakteurListe.add(lRedakteur);
                        }
                    }
                }
            }
        }
        
        uRedakteurSession.setRedakteurListe(lRedakteurListe);
        
        eclDbM.closeAll();
    }
}
