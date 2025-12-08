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
package de.meetingapps.meetingportal.meetComBl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Precision;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclKoordinaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenprognose;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubBlPersonenprognose;
import de.meetingapps.meetingportal.meetComStub.WEStubBlPersonenprognoseRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

public class BlPersonenprognose extends StubRoot {

    public BlPersonenprognose(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    public EclKoordinaten hvKoordinaten;
    public int prediction = 0;
    public int presentPersons = 0;
    //	Doppelte Aktionäre gefiltert
    public int regShareholder = 0;
    public int shares = 0;

    //	Anzahl aller Meldungen
    public int anzRegMeldung = 0;

    //	Timestamp in UTC Time convert mit 
    //	EclPersonenprognose.getLocalDateTime(Timestamp);
    public Timestamp update = null;

    public EclMeldung[] rcRegMeldung = null;
    public EclMeldung[] rcPraesentMeldung = null;

    private int[] arrMax;
    private int[] arrRealCount;

    //	Doppelte Aktionäre gefiltert
    public List<EclMeldung> listRegMeldung = null;
    public List<EclMeldung> listPraesentMeldung = null;
    public List<EclPersonenprognose> listPp = null;
    public List<Double> distances = null;

    //	1
    public int intialisierePrognose() {

        if (verwendeWebService()) {

            WEStubBlPersonenprognose stub = new WEStubBlPersonenprognose(1, null, null);
            WEStubBlPersonenprognoseRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            listPp = stubRC.listPp;
            prediction = stubRC.prediction;
            presentPersons = stubRC.presentPersons;
            update = stubRC.update;
            listRegMeldung = stubRC.listRegMeldung;
            anzRegMeldung = stubRC.anzRegMeldung;
            shares = stubRC.shares;
            regShareholder = stubRC.regShareholder;

            return stubRC.getRc();

        } else {

            dbOpenUndWeitere();

            listPp = lDbBundle.dbPersonenprognose.readAll();
            prediction = lDbBundle.dbPersonenprognose.readSumPrediction();
            presentPersons = lDbBundle.dbPersonenprognose.readSumPresent();
            update = lDbBundle.dbPersonenprognose.readUpdateTime();
            createShareholderCount();

            dbClose();

            return 1;
        }
    }

    //	2
    public int save(List<EclPersonenprognose> list) {

        if (verwendeWebService()) {

            WEStubBlPersonenprognose stub = new WEStubBlPersonenprognose(2, null, list);
            WEStubBlPersonenprognoseRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1) {
                return stubRC.getRc();
            }

            return stubRC.getRc();
        } else {

            dbOpenUndWeitere();

            for (EclPersonenprognose pData : list) {
                if (pData.getIdent() == 0)
                    lDbBundle.dbPersonenprognose.insert(pData);
                else
                    lDbBundle.dbPersonenprognose.update(pData);
            }

            dbClose();

            return 1;
        }
    }

    //	3
    public int deletePrognose(EclPersonenprognose pData) {

        if (verwendeWebService()) {

            WEStubBlPersonenprognose stub = new WEStubBlPersonenprognose(3, pData, null);

            WEStubBlPersonenprognoseRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1) {
                return stubRC.getRc();
            }

            return stubRC.getRc();
        } else {

            dbOpenUndWeitere();
            lDbBundle.dbPersonenprognose.delete(pData);
            dbClose();

            return 1;
        }
    }

    //	4
    public int calculateChange(List<EclPersonenprognose> list, List<Double> distance) {

        this.listPp = list;
        distances = distance;
        listPp.sort(compareByDistance());

        if (verwendeWebService()) {

            WEStubBlPersonenprognose stub = new WEStubBlPersonenprognose(4, null, list);

            stub.setDistances(distances);

            WEStubBlPersonenprognoseRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1) {
                return stubRC.getRc();
            }

            listPp = stubRC.listPp;
            distances = stubRC.distances;
            prediction = stubRC.prediction;
            presentPersons = stubRC.presentPersons;
            update = stubRC.update;

            return stubRC.getRc();
        } else {

            //		System.out.println(!distances.isEmpty());

            if (distances != null) {

                arrMax = new int[listPp.size()];

                for (Double d : distances) {
                    int limit = 0;
                    for (int i = 0; i < listPp.size(); i++) {
                        EclPersonenprognose data = listPp.get(i);
                        if (d == (-1.00)) {
                            arrMax[listPp.size() - 1] += 1;
                            System.out.println(listPp.size() + " " + arrMax[listPp.size() - 1]);
                            break;
                        } else if (d >= limit && d <= data.distance) {
                            arrMax[i] += 1;
                            break;
                        } else {
                            limit = data.distance;
                        }
                    }
                }
                calcPrediction(false);
            }
            return 1;
        }
    }

    //	5
    public int calculation(List<EclPersonenprognose> list, String hvOrt, String hvPlz) {

        this.listPp = list;
        listPp.sort(compareByDistance());

        if (verwendeWebService()) {

            WEStubBlPersonenprognose stub = new WEStubBlPersonenprognose(5, null, list);

            stub.setHvOrt(hvOrt);
            stub.setHvPlz(hvPlz);

            WEStubBlPersonenprognoseRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1) {
                return stubRC.getRc();
            }

            listPp = stubRC.listPp;
            distances = stubRC.distances;
            prediction = stubRC.prediction;
            presentPersons = stubRC.presentPersons;
            update = stubRC.update;
            listRegMeldung = stubRC.listRegMeldung;
            anzRegMeldung = stubRC.anzRegMeldung;
            shares = stubRC.shares;
            regShareholder = stubRC.regShareholder;
            listPraesentMeldung = stubRC.listPraesentMeldung;
            hvKoordinaten = stubRC.hvKoordinaten;

            return stubRC.getRc();

        } else {

            dbOpenUndWeitere();

            hvKoordinaten = readKoordinaten(hvPlz, hvOrt, "");
            if (hvKoordinaten == null) {
                System.out.println("HV-Koordinaten wurden nicht gefunden");
            } else {
                distances = new ArrayList<>();

                //			Meldungen EKs
                listRegMeldung = createMeldelisteEks();

                //			Meldungen - Praesente EKs
                listPraesentMeldung = createPraesentGeweseneEinzelEKs();

                System.out.println("Präsent: " + listPraesentMeldung.size());

                arrMax = new int[listPp.size()];
                arrRealCount = new int[listPp.size()];

                distances.clear();

//              Prognose
                for (EclMeldung meldung : listRegMeldung) {

                    EclKoordinaten koord = readKoordinaten(meldung.plz, meldung.ort, meldung.land);

//                    if (koord == null)
//                        System.out.println("null");

                    if (koord != null) {

                        final double distance = formelDistance(koord);
                        int limit = 0;

                        distances.add(distance);

                        for (int i = 0; i < listPp.size(); i++) {
                            EclPersonenprognose row = listPp.get(i);
                            if (distance >= limit && distance <= row.distance) {
                                arrMax[i] += 1;
                                if (!listPraesentMeldung.isEmpty() && listPraesentMeldung.stream()
                                        .anyMatch(e -> e.meldungsIdent == meldung.meldungsIdent)) {
                                    arrRealCount[i] += 1;
                                }
                                break;
                            } else {
                                limit = row.distance;
                            }
                        }
                    } else {
                        // Ausland
                        arrMax[listPp.size() - 1] += 1;
                        distances.add(-1.00);
                    }
                }
                calcPrediction(true);

            }
            dbClose();
            return 1;
        }
    }

    public int calcPraesent(List<EclPersonenprognose> list, String hvOrt, String hvPlz) {

        return 1;
    }

    /*
     * true = Tatsächliche Prozent auswerten - false = Nur Prognose auswerten
     * Prognose Textfeld füllen
     */
    private void calcPrediction(Boolean real) {
        prediction = 0;
        presentPersons = 0;
        update = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));
        for (int i = 0; i < listPp.size(); i++) {
            EclPersonenprognose row = listPp.get(i);
            row.setMax(arrMax[i]);
            row.setForecast((int) (row.max * (row.percent / 100)));
            row.setUpdated(update);
            prediction += row.getForecast();
            if (real) {
                System.out.println(Double.valueOf(row.getRealCount()) + " / " + Double.valueOf(row.getMax()) + " / "
                        + Precision.round(Double.valueOf(row.getRealCount()) / Double.valueOf(row.getMax()), 3));
                row.setRealCount(arrRealCount[i]);
                presentPersons += row.getRealCount();
                row.setRealPercent(
                        Precision.round(Double.valueOf(row.getRealCount()) / Double.valueOf(row.getMax()) * 100.0, 0));
            }
        }
    }

    private List<EclMeldung> createMeldelisteEks() {
        return lDbBundle.dbJoined.readAllMeldungenEKs();
    }

    private List<EclMeldung> createPraesentGeweseneEinzelEKs() {
        lDbBundle.dbJoined.read_allePraesentGewesenenEinzelEKs();
        return filterPraesentMeldungen(Arrays.asList(lDbBundle.dbJoined.ergebnisMeldung()));
    }

    private void createShareholderCount() {
        shares = lDbBundle.dbJoined.read_SummeAktien();

        listRegMeldung = createMeldelisteEks();
        anzRegMeldung = listRegMeldung.size();
        regShareholder = filterRegMeldungen(listRegMeldung).size();
    }

    private List<EclMeldung> filterRegMeldungen(List<EclMeldung> meldeliste) {
        Set<String> nameSet = new HashSet<>();
        return meldeliste.stream().filter(e -> nameSet.add(e.kurzName)).collect(Collectors.toList());
    }

    private List<EclMeldung> filterPraesentMeldungen(List<EclMeldung> meldeliste) {
        Set<String> nameSet = new HashSet<>();
        return meldeliste.stream().filter(e -> nameSet.add(checkVertreter(e))).collect(Collectors.toList());
    }

    private String checkVertreter(EclMeldung meldung) {
        return meldung.vertreterIdent != 0 ? meldung.vertreterName + " " + meldung.vertreterVorname : meldung.kurzName;
    }

    /*
     * Postleitzahl
     */
    private EclKoordinaten readKoordinaten(String plz, String ort, String land) {

        EclKoordinaten koord = null;
        if (land.isEmpty() || land.equalsIgnoreCase("DE")) {
            if (!plz.isEmpty())
                koord = lDbBundle.dbKoordinaten.readByPlz(plz);
            if (koord == null) {
                if (!ort.isEmpty()) {
                    String[] plzOrt = ort.split(" ");
                    System.out.println(Arrays.toString(plzOrt));
                    System.out.println(plzOrt[0].matches("\\d+") + " - " + ort);

                    if (plzOrt.length > 1 && plzOrt[0].matches("\\d+")) {
                        koord = lDbBundle.dbKoordinaten.readByPlz(plzOrt[0]);
                    } else {
                        koord = lDbBundle.dbKoordinaten.readByOrt(ort);
                    }
                }
            }
            if (koord == null)
                return null;
        } else {
            return null;
        }
        return koord;
    }

    private double formelDistance(EclKoordinaten koord) {

        if (koord.equals(hvKoordinaten))
            return 0.00;

        double breiteHV = hvKoordinaten.breite * Math.PI / 180;
        double breiteAktionaer = koord.breite * Math.PI / 180;
        double laengeHV = hvKoordinaten.laenge * Math.PI / 180;
        double laengeAktionaer = koord.laenge * Math.PI / 180;

        //		Formel Exakte Entfernungsberechnung für die Kugeloberfläche
        return 6378.388 * Math.acos(Math.sin(breiteHV) * Math.sin(breiteAktionaer)
                + Math.cos(breiteHV) * Math.cos(breiteAktionaer) * Math.cos(laengeAktionaer - laengeHV));
    }

    private Comparator<EclPersonenprognose> compareByDistance() {
        return (EclPersonenprognose o1, EclPersonenprognose o2) -> o1.getDistance().compareTo(o2.getDistance());
    }

    private WEStubBlPersonenprognoseRC verifyLogin(WEStubBlPersonenprognose stub) {

        WELoginVerify weLoginVerify = new WELoginVerify();
        stub.setWeLoginVerify(weLoginVerify);

        return wsClient.stubBlPersonenprognose(stub);
    }
}
