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
package de.meetingapps.meetingportal.meetComEntities;

import java.io.Serializable;

/**Zuordnungen von VipKZ zu einzelnen Meldungen*/
public class EclMeldungVipKZ implements Serializable {
    private static final long serialVersionUID = 8592752073242254376L;

    public int mandant = 0;
    public int meldungsIdent = 0;
    public String vipKZKuerzel = "";
    public String parameter = "";
    /**Beschreibung nicht in zugehöriger Datenbank - wird von vipKZ zugeladen*/
    public String beschreibung = "";

}
