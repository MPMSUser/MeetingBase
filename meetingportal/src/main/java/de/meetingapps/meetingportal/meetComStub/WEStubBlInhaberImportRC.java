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
package de.meetingapps.meetingportal.meetComStub;

import java.io.Serializable;
import java.util.List;

import de.meetingapps.meetingportal.meetComBl.BlInhaberImport;
import de.meetingapps.meetingportal.meetComEh.EhGattung;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImportAnmeldedaten;
import de.meetingapps.meetingportal.meetComEntities.EclImportProfil;
import de.meetingapps.meetingportal.meetComEntities.EclImportProtokoll;
import de.meetingapps.meetingportal.meetComWE.WERootRC;

public class WEStubBlInhaberImportRC extends WERootRC implements Serializable {
    private static final long serialVersionUID = -4857976647848725239L;

    public List<EclInhaberImportAnmeldedaten> InhaberImportList = null;
    public List<EclImportProfil> InhaberImportProfiles = null;
    public List<EclImportProtokoll> importProtokoll = null;
    public List<EhGattung> gattungList = null;
    public int ekVon = 0;
    public int ekBis = 0;
    public int gattungen[] = { 0, 0, 0, 0, 0 };
    public int bankCount = 0;
    public String errorLines = null;
    public String errorColumns = null;
    public List<String> violationList = null;

    public List<EclInhaberImportAnmeldedaten> errorList = null;

    public List<EclInhaberImportAnmeldedaten> insertList = null;
    public List<EclInhaberImportAnmeldedaten> importList = null;

    public List<EclInhaberImportAnmeldedaten> InhaberImportListInsert = null;
    public List<EclInhaberImportAnmeldedaten> InhaberImportListUpdate = null;

    public List<EclInhaberImportAnmeldedaten> emptyAdressList = null;

    public void setParameter(BlInhaberImport blInhaberImport) {

        InhaberImportList = blInhaberImport.InhaberImportList;
        InhaberImportProfiles = blInhaberImport.InhaberImportProfiles;
        importProtokoll = blInhaberImport.importProtokoll;
        gattungList = blInhaberImport.gattungList;
        ekVon = blInhaberImport.ekVon;
        ekBis = blInhaberImport.ekBis;
        bankCount = blInhaberImport.bankCount;
        errorLines = blInhaberImport.errorLines;
        errorColumns = blInhaberImport.errorColumns;
        violationList = blInhaberImport.violationList;
        errorList = blInhaberImport.errorList;
        insertList = blInhaberImport.insertList;
        importList = blInhaberImport.importList;
        InhaberImportListInsert = blInhaberImport.InhaberImportListInsert;
        InhaberImportListUpdate = blInhaberImport.InhaberImportListUpdate;
        emptyAdressList = blInhaberImport.emptyAdressList;

    }
}
