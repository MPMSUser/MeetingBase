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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComEntities.EclAnmeldestelle;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImport;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImportEkParameter;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImportGesellschaft;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImportHvParameter;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImportIsinDaten;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImportNummernkreise;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImportXmlHeader;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * The Class CtrlInhaberImportExport.
 */
public class CtrlInhaberImportExport extends CtrlRoot {

    /** The cb anforderungsstelle. */
    @FXML
    private ComboBox<EclAnmeldestelle> cbAnforderungsstelle;

    /** The btn close. */
    @FXML
    private Button btnClose;

    /** The btn export. */
    @FXML
    private Button btnExport;

    /** The Constant InhaberImport_XML. */
    private static final String InhaberImport_XML = "InhaberImportges.xml";

    /**
     * Initialize.
     */
    @FXML
    private void initialize() {

        EclInhaberImport InhaberImport = new EclInhaberImport();

        EclInhaberImportXmlHeader header = null;

        EclInhaberImportGesellschaft ges = null;

        InhaberImport.setEclInhaberImportXmlHeader(header);
        InhaberImport.setEclInhaberImportGesellschaft(ges);

        EclInhaberImportHvParameter hvParam = null;

        ges.setEclInhaberImportHvParamerter(hvParam);

        EclInhaberImportEkParameter ekParam = null;

        hvParam.setEclInhaberImportEkParameter(ekParam);

        List<EclInhaberImportIsinDaten> isinList = new ArrayList<>();
        EclInhaberImportIsinDaten isinDaten = new EclInhaberImportIsinDaten(ges.getKurzname(), hvParam.getHvDatum(), ges.getHstNr(),
                "DE0005088108", "508810", "1", "E", "I", "S", "N", ges.getGrundkaptial(), "", null);
        isinList.add(isinDaten);

        EclInhaberImportIsinDaten isinDaten2 = new EclInhaberImportIsinDaten(ges.getKurzname(), hvParam.getHvDatum(), ges.getHstNr(),
                "DE0005088108", "508810", "1", "E", "I", "S", "N", ges.getGrundkaptial(), "", null);
        isinList.add(isinDaten2);

        ekParam.setEclInhaberImportIsinDaten(isinList);

        List<EclInhaberImportNummernkreise> numList = new ArrayList<>();
        EclInhaberImportNummernkreise numKreis = new EclInhaberImportNummernkreise(ges.getKurzname(), hvParam.getHvDatum(),
                ges.getHstNr(), isinDaten.getIsin(), 1, 1, 1000, 0);
        numList.add(numKreis);

        isinDaten.setEclInhaberImportNummernkreise(numList);
        isinDaten2.setEclInhaberImportNummernkreise(numList);

        String currentDirectory = System.getProperty("user.home");
        System.out.println("user.dir: " + currentDirectory);

        final String path = currentDirectory + "\\Desktop\\" + ParamS.clGlobalVar.mandant + "_" + "test\\";

        try {
            JAXBContext context = JAXBContext.newInstance(EclInhaberImport.class);

            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            m.marshal(InhaberImport, System.out);

            File d = new File(path);
            d.mkdirs();

            m.marshal(InhaberImport, new File(path, InhaberImport_XML));

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        CaDateiWrite writer = new CaDateiWrite();
        writer.oeffneNameExplizitOhneBundle(path + "Dambektz.txt");
        writer.ausgabePlain("Bitte tauschen Sie diese Eintrittskarte an der                   ");
        writer.newline();
        writer.ausgabePlain("Eingangskontrolle gegen einen Stimmbeleg.                        ");
        writer.schliessen();

    }

    /**
     * On close.
     *
     * @param event the event
     */
    @FXML
    private void onClose(ActionEvent event) {
        eigeneStage.hide();
    }

    /**
     * On export.
     *
     * @param event the event
     */
    @FXML
    private void onExport(ActionEvent event) {

    }

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }
}