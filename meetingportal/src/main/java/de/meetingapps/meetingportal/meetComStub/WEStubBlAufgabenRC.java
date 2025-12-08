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

import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAufgaben;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComWE.WERootRC;

public class WEStubBlAufgabenRC extends WERootRC {

    public List<EclAufgaben> rcAufgabenListe = null;
    public List<EclAktienregister> rcAktienregisterListe = null;
    public List<EclPersonenNatJur> rcPersonNatJurListe = null;
    public List<String> rcPasswortListe = null;
    public List<String> rcKennungListe = null;

    public List<Integer> rcMandantListe = null;
    public List<Integer> rcHvJahrListe = null;
    public List<String> rcHvNummer = null;
    public List<String> rcDatenbereich = null;
    public List<String> rcEmittent = null;

    public List<Integer> rcFehlercode = null;

}
