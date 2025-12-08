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
package de.meetingapps.meetingclient.meetingBestand;

import de.meetingapps.meetingportal.meetComBl.BlInsti;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclInsti;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/** Übergreifende Funktionen für die div. Sammelkarten-Controller */
public class CtrlInstiPflegeUebergreifend {

    /** The rc list insti. */
    ObservableList<MInsti> rcListInsti = null;

    /** The rc insti. */
    public EclInsti[] rcInsti = null;

    //    public EclZutrittskarten[][] rcZutrittskartenArray=null;
    //    public EclStimmkarten[][] rcStimmkartenArray=null;
    //    public EclWillensErklVollmachtenAnDritte[][] rcWillensErklVollmachtenAnDritteArray=null;
    //	
    //	
    /************************Funktionen für Insti-Table*************************************/
    /**Bereitet "das Gerüst" (d.h. einen leeren TableView mit entsprechenden Feldern)
     * für einen TableView für die Insti, basierend auf MInsti, auf.
     * Anschließend noch z.B.
     *      tableInsti.setPrefHeight(676);
     *      tableInsti.setPrefWidth(1464);
     *  setzen,
     *  sowie die observable List zuordnen, z.B.
     *      tableInsti.setItems(listInsti);
     */
    public TableView<MInsti> vorbereitenTableViewInsti() {

        TableView<MInsti> tableInsti = new TableView<MInsti>();

        TableColumn<MInsti, Integer> colIdent = new TableColumn<MInsti, Integer>("Ident");
        colIdent.setCellValueFactory(new PropertyValueFactory<>("ident"));
        colIdent.setSortType(TableColumn.SortType.DESCENDING);
        tableInsti.getColumns().add(colIdent);

        TableColumn<MInsti, String> colKurzBezeichnung = new TableColumn<MInsti, String>("Kurzbezeichnung");
        colKurzBezeichnung.setCellValueFactory(new PropertyValueFactory<>("kurzBezeichnung"));
        colKurzBezeichnung.setSortType(TableColumn.SortType.DESCENDING);
        tableInsti.getColumns().add(colKurzBezeichnung);

        TableColumn<MInsti, Integer> colStandardSammelkarteGruppennummer = new TableColumn<MInsti, Integer>(
                "Sam.Grup.Nr.");
        colStandardSammelkarteGruppennummer
                .setCellValueFactory(new PropertyValueFactory<>("standardSammelkarteGruppennummer"));
        colStandardSammelkarteGruppennummer.setSortType(TableColumn.SortType.DESCENDING);
        tableInsti.getColumns().add(colStandardSammelkarteGruppennummer);

        TableColumn<MInsti, String> colStandardMeldeName = new TableColumn<MInsti, String>("Standard Meldename");
        colStandardMeldeName.setCellValueFactory(new PropertyValueFactory<>("standardMeldeName"));
        colStandardMeldeName.setSortType(TableColumn.SortType.DESCENDING);
        tableInsti.getColumns().add(colStandardMeldeName);

        TableColumn<MInsti, String> colStandardMeldeOrt = new TableColumn<MInsti, String>("Standard Meldeort");
        colStandardMeldeOrt.setCellValueFactory(new PropertyValueFactory<>("standardMeldeOrt"));
        colStandardMeldeOrt.setSortType(TableColumn.SortType.DESCENDING);
        tableInsti.getColumns().add(colStandardMeldeOrt);

        TableColumn<MInsti, Integer> colFestadressenNummer = new TableColumn<MInsti, Integer>("Festadr.Nr.");
        colFestadressenNummer.setCellValueFactory(new PropertyValueFactory<>("festadressenNummer"));
        colFestadressenNummer.setSortType(TableColumn.SortType.DESCENDING);
        tableInsti.getColumns().add(colFestadressenNummer);

        return tableInsti;

    }

    /**
     * Holt die Instidaten.
     * 
     * pInstiIdent=0 => alle, sonst übergebene Ident.
     * 
     * Rückgabe = Anzahl der gelesenen Instis (0 = keine gelesen!)
     * 
     * Ablage erfolgt in: rcListInsti rcInsti
     * 
     * rcZutrittskartenArray rcStimmkartenArray
     * rcWillensErklVollmachtenAnDritteArray
     *
     * @param pInstiIdent the insti ident
     * @return the int
     */
    public int holeInstiDaten(int pInstiIdent) {

        /** Schritt 1: alles von Datenbank laden. */

        DbBundle lDbBundle = new DbBundle();

        BlInsti blInsti = new BlInsti(false, lDbBundle);
        int anzInsti = blInsti.holeInstiDaten(pInstiIdent);
        rcInsti = blInsti.rcInsti;

        /*Schritt 2: rcSammelMeldung füllen*/
        rcListInsti = FXCollections.observableArrayList();

        for (int i = 0; i < anzInsti; i++) {
            EclInsti aktuelleInsti = rcInsti[i];
            MInsti aktuelleMInsti = new MInsti(aktuelleInsti);
            rcListInsti.add(aktuelleMInsti);

            //	    	/*Zutrittskarten zusätzlich zuordnen*/
            //	    	anz=rcZutrittskartenArray[i].length;
            //	    	for (int i1=0;i1<anz;i1++){
            //	    		if (!rcZutrittskartenArray[i][i1].zutrittsIdentWurdeGesperrt()) {
            //	    			aktuelleMSammelkarte.addZutrittsIdent(rcZutrittskartenArray[i][i1].zutrittsIdent);
            //	    		}
            //	    	}
            //
            //	    	/*stimmkarten zusätzlich zuordnen*/
            //	    	anz=rcStimmkartenArray[i].length;
            //	    	for (int i1=0;i1<anz;i1++){
            //	    		if (rcStimmkartenArray[i][i1].stimmkarteWurdeGesperrt()) {
            //	    			aktuelleMSammelkarte.addStimmkarte(rcStimmkartenArray[i][i1].stimmkarte);
            //	    		}
            //	    	}
            //
            //	    	/*Bevollmächtigte hinzufügen (ohne stornierte!)*/
            //	    	if (rcWillensErklVollmachtenAnDritteArray[i]!=null){
            //	    		for (int i1=0;i1<rcWillensErklVollmachtenAnDritteArray[i].length;i1++){
            //	    			if (!rcWillensErklVollmachtenAnDritteArray[i][i1].wurdeStorniert){
            //	    				aktuelleMSammelkarte.addVollmacht(rcWillensErklVollmachtenAnDritteArray[i][i1]);
            //	    			}
            //	    		}
            //	    	}

        }
        return anzInsti;
    }

    //	
    //	/****************************Funktionen für Aktionärs-Table-View**************************************/
    //	/**Bereitet "das Gerüst" (d.h. einen leeren TableView mit entsprechenden Feldern)
    //	 * für einen TableView für die Aktionäre, basierend auf EclMeldungMitAktionaersWeisung, auf.
    //	 * Anschließend noch z.B.
    //	 *    	tableAktionaer.setPrefHeight(676);
    //	 *    	tableAktionaere.setPrefWidth(1464);
    //	 *  setzen,
    //	 *  sowie die observable List zuordnen, z.B.
    //	 *		tableAktionaere.setItems(listAktionaere);
    //	 */
    //	public TableView<EclMeldungMitAktionaersWeisung> vorbereitenTableViewAktionaere(int pGattung){
    //	
    //		TableView<EclMeldungMitAktionaersWeisung> tableAktionaere=new TableView<EclMeldungMitAktionaersWeisung>();
    //	
    //        TableColumn<EclMeldungMitAktionaersWeisung, Integer> colMeldungsIdent= 
    //        		new TableColumn<EclMeldungMitAktionaersWeisung, Integer>("Ident");
    //        colMeldungsIdent.setCellValueFactory(new PropertyValueFactory<>("meldungsIdent"));
    //        colMeldungsIdent.setSortType(TableColumn.SortType.DESCENDING);
    //        tableAktionaere.getColumns().add(colMeldungsIdent);
    //
    //        TableColumn<EclMeldungMitAktionaersWeisung, String> colAktionaersnummer= 
    //        		new TableColumn<EclMeldungMitAktionaersWeisung, String>("AktionärsNr");
    //        colAktionaersnummer.setCellValueFactory(new PropertyValueFactory<>("aktionaersnummer"));
    //        tableAktionaere.getColumns().add(colAktionaersnummer);
    //
    //        TableColumn<EclMeldungMitAktionaersWeisung, String> colZutrittsIdent= 
    //        		new TableColumn<EclMeldungMitAktionaersWeisung, String>("EK-Nr");
    //        colZutrittsIdent.setCellValueFactory(new PropertyValueFactory<>("zutrittsIdent"));
    //        tableAktionaere.getColumns().add(colZutrittsIdent);
    //
    //        TableColumn<EclMeldungMitAktionaersWeisung, String> colName= 
    //        		new TableColumn<EclMeldungMitAktionaersWeisung, String>("Name");
    //        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    //        tableAktionaere.getColumns().add(colName);
    //        
    //        TableColumn<EclMeldungMitAktionaersWeisung, String> colAktien= 
    //        		new TableColumn<EclMeldungMitAktionaersWeisung, String>("Aktien");
    //        colAktien.setCellValueFactory(new PropertyValueFactory<>("stueckAktien"));
    //        colAktien.setCellFactory(column->{
    //        	return new TableCellLongMitPunkt<EclMeldungMitAktionaersWeisung, String>();});
    ////        colAktienStimmen.setSortable(false);
    //        tableAktionaere.getColumns().add(colAktien);
    //         
    //       List<EclAbstimmungM> abstimmungenListeM=CInjects.weisungsAgenda[pGattung].getAbstimmungenListeM();
    //       int anzahlAbstimmungen=abstimmungenListeM.size();
    //       for (int i=0;i<anzahlAbstimmungen;i++){
    //    	   String hUeberschrift=abstimmungenListeM.get(i).getNummer()+abstimmungenListeM.get(i).getNummerindex();
    //    	   int abstimmungsPosition=abstimmungenListeM.get(i).getIdentWeisungssatz();
    //    	   if (abstimmungsPosition==-1) {abstimmungsPosition=200;}
    //    	   TableColumn<EclMeldungMitAktionaersWeisung, String> colWeisung= 
    //    			   new TableColumn<EclMeldungMitAktionaersWeisung, String>(hUeberschrift);
    //    	   colWeisung.setCellValueFactory(createArrayValueFactory(EclMeldungMitAktionaersWeisung::getAbgabeText, abstimmungsPosition));
    //    	   tableAktionaere.getColumns().add(colWeisung);
    //       }
    //
    //       abstimmungenListeM=CInjects.weisungsAgenda[pGattung].getGegenantraegeListeM();
    //       anzahlAbstimmungen=abstimmungenListeM.size();
    //       for (int i=0;i<anzahlAbstimmungen;i++){
    //    	   String hUeberschrift=abstimmungenListeM.get(i).getNummer()+abstimmungenListeM.get(i).getNummerindex();
    //    	   int abstimmungsPosition=abstimmungenListeM.get(i).getIdentWeisungssatz();
    //    	   if (abstimmungsPosition==-1) {abstimmungsPosition=200;}
    //    	   TableColumn<EclMeldungMitAktionaersWeisung, String> colWeisung= 
    //    			   new TableColumn<EclMeldungMitAktionaersWeisung, String>(hUeberschrift);
    //    	   colWeisung.setCellValueFactory(createArrayValueFactory(EclMeldungMitAktionaersWeisung::getAbgabeText, abstimmungsPosition));
    //    	   tableAktionaere.getColumns().add(colWeisung);
    //       }
    //
    //       TableColumn<EclMeldungMitAktionaersWeisung, String> colStimmausschluss= 
    //       		new TableColumn<EclMeldungMitAktionaersWeisung, String>("StimmAusschl.");
    //       colStimmausschluss.setCellValueFactory(new PropertyValueFactory<>("stimmausschluss"));
    //       tableAktionaere.getColumns().add(colStimmausschluss);
    //
    //		return tableAktionaere;
    //
    //	}
    //
    //	
    //	static <S, T> Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> createArrayValueFactory(Function<S, T[]> arrayExtractor, final int index) {
    //	    if (index < 0) {
    //	        return cd -> null;
    //	    }
    //	    return cd -> {
    //	        T[] array = arrayExtractor.apply(cd.getValue());
    //	        return array == null || array.length <= index ? null : new SimpleObjectProperty<>(array[index]);
    //	    };
    //	}

}
