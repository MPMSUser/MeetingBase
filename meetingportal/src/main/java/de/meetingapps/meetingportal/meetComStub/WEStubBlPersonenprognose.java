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

import de.meetingapps.meetingportal.meetComEntities.EclPersonenprognose;
import de.meetingapps.meetingportal.meetComWE.WERoot;

public class WEStubBlPersonenprognose extends WERoot {

    public int stubFunktion = -1;

    public EclPersonenprognose data = null;

    public List<EclPersonenprognose> list = null;

    public List<Double> distances = null;

    public String hvOrt = null;

    public String hvPlz = null;

    public WEStubBlPersonenprognose() {

    }

    public WEStubBlPersonenprognose(int stubFunktion) {
        this.stubFunktion = stubFunktion;
    }

    public WEStubBlPersonenprognose(int stubFunktion, EclPersonenprognose data, List<EclPersonenprognose> list) {
        this.stubFunktion = stubFunktion;
        this.data = data;
        this.list = list;
    }

    public int getStubFunktion() {
        return stubFunktion;
    }

    public void setStubFunktion(int stubFunktion) {
        this.stubFunktion = stubFunktion;
    }

    public EclPersonenprognose getData() {
        return data;
    }

    public void setData(EclPersonenprognose data) {
        this.data = data;
    }

    public List<EclPersonenprognose> getList() {
        return list;
    }

    public void setList(List<EclPersonenprognose> list) {
        this.list = list;
    }

    public List<Double> getDistances() {
        return distances;
    }

    public void setDistances(List<Double> distances) {
        this.distances = distances;
    }

    public String getHvOrt() {
        return hvOrt;
    }

    public void setHvOrt(String hvOrt) {
        this.hvOrt = hvOrt;
    }

    public String getHvPlz() {
        return hvPlz;
    }

    public void setHvPlz(String hvPlz) {
        this.hvPlz = hvPlz;
    }

}
