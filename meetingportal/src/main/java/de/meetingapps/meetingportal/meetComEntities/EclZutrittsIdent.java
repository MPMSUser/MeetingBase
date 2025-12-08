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

public class EclZutrittsIdent {

    public String zutrittsIdent = "";
    public String zutrittsIdentNeben = "";

    public int compareTo(EclZutrittsIdent pZutrittsnummer) {
        String dieseNummer = this.zutrittsIdent + this.zutrittsIdentNeben;
        return dieseNummer.compareTo(pZutrittsnummer.zutrittsIdent + pZutrittsnummer.zutrittsIdentNeben);
    }

    @Override
    public String toString() {
        String hString = this.zutrittsIdent;
        hString = hString + "-" + zutrittsIdentNeben;
        return hString;
    }

    public String toString(int immerNeben) {
        String hString = this.zutrittsIdent;
        if (!zutrittsIdentNeben.isEmpty() || immerNeben == 1) {
            hString = hString + "-" + zutrittsIdentNeben;
        }
        return hString;
    }

}