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
package de.meetingapps.meetingportal.meetComImport;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComBl.BlGastkarte;
import de.meetingapps.meetingportal.meetComDb.DbBasis;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;

/*Import-Datei-Format:
 * Spalten in folgender Reihenfolge, 1. Zeile = Überschrift!
 * Gastkartennummer
 * Name
 * Vorname
 * Ort
 */

public class ImportGastkarten {


    /**Gibt Dateiaufbau wieder:
     * 0:
     * Zutrittsident, Name, Vorname, Ort
     * 
     * 1:
     * ku1009-Format
     * 
     * 2=ku287
     * Zeile 1 = Anrede oder Firma (zusatz1)
     * Zeile 2 = Vorname, Name (name)
     * Zeile 3 = Straße (stra0e)
     * Zeile 4 = PLZ und Ort (ort)
     * Zeile 5 = Land (zusatz2)
     */
    public int importVariante=2;
    boolean ueberschriftVorhanden=true;

    /**Aus dieser Datei wird importiert - voller Pfadname, z.B.
     * D:\\XY.TXT */
    public String importDateiName = "";


    public ImportGastkarten(DbBasis datenbank) {
        //		verbindung = datenbank.verbindung;
    }

    private DbBundle dbBundle = null;
    private CaDateiWrite protokollDatei = null;

    private EclMeldung lMeldung = null;
    private String zutrittsIdent = "";

    private boolean lAltsatzVorhanden = false;

    /**Liest alten vorhandenen Satz ein und belegt lAltsatzVorhanden true oder false*/
    private void leseAltSatz() {
        int erg = 0;
        EclZutrittsIdent lZutrittsIdent = new EclZutrittsIdent();
        lZutrittsIdent.zutrittsIdent = zutrittsIdent;
        lZutrittsIdent.zutrittsIdentNeben = "00";
        erg = dbBundle.dbZutrittskarten.readGast(lZutrittsIdent, 1);
        if (erg >= 1) {
            lAltsatzVorhanden = true;
        } else {
            lAltsatzVorhanden = false;
        }
    }

    public void importiere(DbBundle aDbBundle) throws FileNotFoundException, IOException {

        int iii = 0;

        dbBundle = aDbBundle;
        String hString = "";

        lMeldung = new EclMeldung();
        protokollDatei = new CaDateiWrite();
        protokollDatei.oeffne(aDbBundle, "gaestekartenImport");
        protokollDatei.ausgabe(CaDatumZeit.DatumZeitStringFuerDatenbank());
        protokollDatei.newline();

        aDbBundle.dbBasis.endTransactionFinal();

        try {

            //			FileReader fr = new FileReader(importDateiName);
            //			BufferedReader br = new BufferedReader(fr);

            		      BufferedReader br = new BufferedReader(
            		              new InputStreamReader(new FileInputStream(importDateiName), "UTF-8"));
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(importDateiName)));

            try {

                String zeile = null;
                if (ueberschriftVorhanden) {
                    zeile = br.readLine(); /*Erste Zeile= Überschrift*/
                }

                while ((zeile = br.readLine()) != null /*&& iii<45000*/) {
                    aDbBundle.dbBasis.beginTransaction();
                    iii++;
                    if ((iii) % 100 == 0) {
                        System.out.println(iii + "Import " + CaDatumZeit.DatumZeitStringFuerDatenbank());
                    }

                    String[] zeileSplit = zeile.split(";");
                    for (int i=0;i<zeileSplit.length;i++) {
                        zeileSplit[i]=zeileSplit[i].trim();
                    }

                    if (zeileSplit.length >= 2) {
                        BlGastkarte blGastkarte=new BlGastkarte(aDbBundle);
                        lMeldung = new EclMeldung();

                        if (importVariante==0) {
                            hString = zeileSplit[0];
                            if (!hString.isEmpty()) {
                                zutrittsIdent = Integer.toString(Integer.parseInt(hString));
                                leseAltSatz();
                                if (lAltsatzVorhanden == false) {
                                    lMeldung.name = zeileSplit[1].trim();
                                    if (zeileSplit.length > 2) {
                                        lMeldung.vorname = zeileSplit[2].trim();
                                        if (zeileSplit.length > 3) {
                                            lMeldung.ort = zeileSplit[3].trim();
                                        }
                                    }
                                    blGastkarte.pGast=lMeldung;
                                    blGastkarte.pZutrittsIdent=zutrittsIdent;
                                    int rc=blGastkarte.ausstellen();
                                    if (rc<1) {
                                        protokollDatei.ausgabe("Fehler 002 bei " + zutrittsIdent + "="
                                                + rc);
                                        protokollDatei.newline();
                                    }
                                 } else {
                                    protokollDatei.ausgabe("Fehler: schon vorhanden bei " + zutrittsIdent);
                                    protokollDatei.newline();
                                }
                             
                             }
                        }
                        

                        if (importVariante==1) {
                            hString = zeileSplit[3];
                            if (!hString.isEmpty()) {
                                zutrittsIdent = Integer.toString(Integer.parseInt(hString));
                                leseAltSatz();
                                if (lAltsatzVorhanden == false) {
                                    lMeldung.name = zeileSplit[5].trim();
                                    if (zeileSplit.length > 4) {
                                        lMeldung.vorname = zeileSplit[4].trim();
                                    }
                                    if (zeileSplit.length > 10) {
                                        lMeldung.ort = zeileSplit[10].trim();
                                    }
                                    if (zeileSplit.length > 9) {
                                        lMeldung.plz = zeileSplit[9].trim();
                                    }
                                    if (zeileSplit.length > 8) {
                                        lMeldung.strasse = zeileSplit[9].trim();
                                    }
                                    if (zeileSplit.length > 7) {
                                        lMeldung.zusatz1 = zeileSplit[7].trim();
                                    }
                                    
                                    blGastkarte.pGast=lMeldung;
                                    blGastkarte.pZutrittsIdent=zutrittsIdent;
                                    int rc=blGastkarte.ausstellen();
                                    if (rc<1) {
                                        protokollDatei.ausgabe("Fehler 002 bei " + zutrittsIdent + "="
                                                + rc);
                                        protokollDatei.newline();
                                    }
                                 } else {
                                    protokollDatei.ausgabe("Fehler: schon vorhanden bei " + zutrittsIdent);
                                    protokollDatei.newline();
                                }
                             
                             }
                        }

                        if (importVariante==2) {
                            lMeldung.zusatz1=zeileSplit[0].trim();
                            lMeldung.name = zeileSplit[1].trim();
                            if (zeileSplit.length > 2) {
                                lMeldung.strasse = zeileSplit[2].trim();
                            }
                            if (zeileSplit.length > 3) {
                                lMeldung.ort = zeileSplit[3].trim();
                            }
                            if (zeileSplit.length > 4) {
                                lMeldung.zusatz2 = zeileSplit[4].trim();
                            }
                            blGastkarte.pGast=lMeldung;
                            int rc=blGastkarte.ausstellen();
                            if (rc<1) {
                                protokollDatei.ausgabe("Fehler 002 bei " + iii + "="
                                        + rc);
                                protokollDatei.newline();
                            }
                        }


                    }
                }

                br.close();
                //	fr.close();
            } catch (IOException e) {
                CaBug.drucke("002");
                System.out.print(e.getMessage());
            }

        } catch (FileNotFoundException ex) {
            CaBug.drucke("001");
            System.out.print(ex.getMessage());
        }

        protokollDatei.ausgabe(CaDatumZeit.DatumZeitStringFuerDatenbank());
        protokollDatei.newline();

        protokollDatei.schliessen();
        aDbBundle.dbBasis.beginTransaction(); /*Damits anschließend beim Close beendet werden kann*/

    }

}