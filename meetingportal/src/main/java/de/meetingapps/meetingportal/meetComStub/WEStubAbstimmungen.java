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

import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungZuAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungenZuStimmkarte;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarteInhalt;
import de.meetingapps.meetingportal.meetComWE.WERoot;

public class WEStubAbstimmungen extends WERoot {

    /**Bisher vergebener Max-Wert = 17*/
    public int stubFunktion = -1;

    public int filterTyp = 0;
    public int filterIdent = 0;
    public int sortierung = 0;
    public EclAbstimmung[] abstimmungenArray = null;
    public EclAbstimmung abstimmung;
    public EclAbstimmungsblock[] abstimmungsblockArray = null;
    public EclAbstimmungsblock abstimmungsvorgang;
    public EclStimmkarteInhalt[] elekStimmkarteArray = null;
    public EclStimmkarteInhalt elekStimmkarte = null;
    public EclAbstimmungZuAbstimmungsblock[] angezeigteAbstimmungZuAbstimmungsblock = null;
    public EclAbstimmungZuAbstimmungsblock abstimmungZuAbstimmungsblock;
    public List<EclAbstimmungZuAbstimmungsblock> listAbstimmungZuAbstimmungsblock;
    public List<EclAbstimmungenZuStimmkarte> listAbstimmungZuStimmkarte;
    public EclAbstimmungenZuStimmkarte[] angezeigteAbstimmungZuStimmkarte = null;
    public int ident = 0;
    public boolean[] abstimmungWurdeVeraendert = null;
    public boolean reloadWeisungenAbbruch = false;
    public boolean reloadAbstimmungenAbbruch = false;
    public boolean reload = false;

    /***********Standard Getter und Setter********************/

    public int getStubFunktion() {
        return stubFunktion;
    }

    public void setStubFunktion(int stubFunktion) {
        this.stubFunktion = stubFunktion;
    }

    public int getFilterTyp() {
        return filterTyp;
    }

    public void setFilterTyp(int filterTyp) {
        this.filterTyp = filterTyp;
    }

    public int getFilterIdent() {
        return filterIdent;
    }

    public void setFilterIdent(int filterIdent) {
        this.filterIdent = filterIdent;
    }

    public int getSortierung() {
        return sortierung;
    }

    public void setSortierung(int sortierung) {
        this.sortierung = sortierung;
    }

    public EclAbstimmung[] getAbstimmungenArray() {
        return abstimmungenArray;
    }

    public void setAbstimmungenArray(EclAbstimmung[] abstimmungenArray) {
        this.abstimmungenArray = abstimmungenArray;
    }

    public EclAbstimmung getAbstimmung() {
        return abstimmung;
    }

    public void setAbstimmung(EclAbstimmung abstimmung) {
        this.abstimmung = abstimmung;
    }

    public EclAbstimmungsblock[] getAbstimmungsblockArray() {
        return abstimmungsblockArray;
    }

    public void setAbstimmungsblockArray(EclAbstimmungsblock[] abstimmungsblockArray) {
        this.abstimmungsblockArray = abstimmungsblockArray;
    }

    public EclAbstimmungsblock getAbstimmungsvorgang() {
        return abstimmungsvorgang;
    }

    public void setAbstimmungsvorgang(EclAbstimmungsblock abstimmungsvorgang) {
        this.abstimmungsvorgang = abstimmungsvorgang;
    }

    public EclStimmkarteInhalt[] getElekStimmkarteArray() {
        return elekStimmkarteArray;
    }

    public void setElekStimmkarteArray(EclStimmkarteInhalt[] elekStimmkarteArray) {
        this.elekStimmkarteArray = elekStimmkarteArray;
    }

    public EclStimmkarteInhalt getElekStimmkarte() {
        return elekStimmkarte;
    }

    public void setElekStimmkarte(EclStimmkarteInhalt elekStimmkarte) {
        this.elekStimmkarte = elekStimmkarte;
    }

    public EclAbstimmungZuAbstimmungsblock[] getAngezeigteAbstimmungZuAbstimmungsblock() {
        return angezeigteAbstimmungZuAbstimmungsblock;
    }

    public void setAngezeigteAbstimmungZuAbstimmungsblock(
            EclAbstimmungZuAbstimmungsblock[] angezeigteAbstimmungZuAbstimmungsblock) {
        this.angezeigteAbstimmungZuAbstimmungsblock = angezeigteAbstimmungZuAbstimmungsblock;
    }

    public EclAbstimmungZuAbstimmungsblock getAbstimmungZuAbstimmungsblock() {
        return abstimmungZuAbstimmungsblock;
    }

    public void setAbstimmungZuAbstimmungsblock(EclAbstimmungZuAbstimmungsblock abstimmungZuAbstimmungsblock) {
        this.abstimmungZuAbstimmungsblock = abstimmungZuAbstimmungsblock;
    }

    public List<EclAbstimmungZuAbstimmungsblock> getListAbstimmungZuAbstimmungsblock() {
        return listAbstimmungZuAbstimmungsblock;
    }

    public void setListAbstimmungZuAbstimmungsblock(
            List<EclAbstimmungZuAbstimmungsblock> listAbstimmungZuAbstimmungsblock) {
        this.listAbstimmungZuAbstimmungsblock = listAbstimmungZuAbstimmungsblock;
    }

    public List<EclAbstimmungenZuStimmkarte> getListAbstimmungZuStimmkarte() {
        return listAbstimmungZuStimmkarte;
    }

    public void setListAbstimmungZuStimmkarte(List<EclAbstimmungenZuStimmkarte> listAbstimmungZuStimmkarte) {
        this.listAbstimmungZuStimmkarte = listAbstimmungZuStimmkarte;
    }

    public EclAbstimmungenZuStimmkarte[] getAngezeigteAbstimmungZuStimmkarte() {
        return angezeigteAbstimmungZuStimmkarte;
    }

    public void setAngezeigteAbstimmungZuStimmkarte(EclAbstimmungenZuStimmkarte[] angezeigteAbstimmungZuStimmkarte) {
        this.angezeigteAbstimmungZuStimmkarte = angezeigteAbstimmungZuStimmkarte;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public boolean[] getAbstimmungWurdeVeraendert() {
        return abstimmungWurdeVeraendert;
    }

    public void setAbstimmungWurdeVeraendert(boolean[] abstimmungWurdeVeraendert) {
        this.abstimmungWurdeVeraendert = abstimmungWurdeVeraendert;
    }

    public boolean isReloadWeisungenAbbruch() {
        return reloadWeisungenAbbruch;
    }

    public void setReloadWeisungenAbbruch(boolean reloadWeisungenAbbruch) {
        this.reloadWeisungenAbbruch = reloadWeisungenAbbruch;
    }

    public boolean isReloadAbstimmungenAbbruch() {
        return reloadAbstimmungenAbbruch;
    }

    public void setReloadAbstimmungenAbbruch(boolean reloadAbstimmungenAbbruch) {
        this.reloadAbstimmungenAbbruch = reloadAbstimmungenAbbruch;
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

}
