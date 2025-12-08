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

import java.util.List;

import de.meetingapps.meetingportal.meetComBl.BlInhaberImport;
import de.meetingapps.meetingportal.meetComEh.EhGattung;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImportAnmeldedaten;
import de.meetingapps.meetingportal.meetComEntities.EclImportProfil;
import de.meetingapps.meetingportal.meetComEntities.EclImportProtokoll;
import de.meetingapps.meetingportal.meetComWE.WERoot;

public class WEStubBlInhaberImport extends WERoot {

    public int stubFunktion = -1;

    public List<EclInhaberImportAnmeldedaten> InhaberImportList = null;
    public List<EclImportProfil> InhaberImportProfiles = null;
    public List<EclImportProtokoll> importProtokoll = null;
    public byte[] byteFile = null;
    public String fileName = null;
    public List<EhGattung> gattungList = null;
    public int gattungen[] = { 0, 0, 0, 0, 0 };
    public int bankCount = 0;
    public int pSammelIdent = 0;
    public EclImportProfil importProfile = null;

    public List<EclInhaberImportAnmeldedaten> errorList = null;
    public List<EclInhaberImportAnmeldedaten> insertList = null;

    public List<EclInhaberImportAnmeldedaten> importList = null;

    public List<EclInhaberImportAnmeldedaten> InhaberImportListInsert = null;
    public List<EclInhaberImportAnmeldedaten> InhaberImportListUpdate = null;

    public WEStubBlInhaberImport() {

    }

    public WEStubBlInhaberImport(int stubFunktion) {
        this.stubFunktion = stubFunktion;
    }

    public void setParameter(BlInhaberImport blInhaberImport) {

        InhaberImportList = blInhaberImport.InhaberImportList;
        InhaberImportProfiles = blInhaberImport.InhaberImportProfiles;
        importProtokoll = blInhaberImport.importProtokoll;
        fileName = blInhaberImport.fileName;
        byteFile = blInhaberImport.byteFile;
        importProfile = blInhaberImport.importProfile;
        gattungList = blInhaberImport.gattungList;
        bankCount = blInhaberImport.bankCount;
        errorList = blInhaberImport.errorList;
        insertList = blInhaberImport.insertList;
        importList = blInhaberImport.importList;
        InhaberImportListInsert = blInhaberImport.InhaberImportListInsert;
        pSammelIdent = blInhaberImport.pSammelIdent;

    }

}
