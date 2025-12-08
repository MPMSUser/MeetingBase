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
package de.meetingapps.meetingportal.meetingportTController;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class TFehlerViewSession implements Serializable {
    private static final long serialVersionUID = -1072228811792773733L;

    /**siehe KonstPortalFehlerView*/
    private int fehlerArt = 0;

    /**Falls fehlerArt=1: View, der anschließend aufgerufen werden muß*/
    private int nextView = 0;

    /*******************Standard getter und setter***************************/

    public int getFehlerArt() {
        return fehlerArt;
    }

    public void setFehlerArt(int fehlerArt) {
        this.fehlerArt = fehlerArt;
    }

    public int getNextView() {
        return nextView;
    }

    public void setNextView(int nextView) {
        this.nextView = nextView;
    }

}
