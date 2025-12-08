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
package de.meetingapps.meetingportal.meetComEntitiesXml;

public class Vote20 {

    private String issuerLabel;

    private String resolutionStatus;

    private String forShares;

    private String againstShares;

    private String abstainShares;

    public Vote20(String issuerLabel, String resolutionStatus, String forShares, String againstShares,
            String abstainShares) {
        super();
        this.issuerLabel = issuerLabel;
        this.resolutionStatus = resolutionStatus;
        this.forShares = forShares;
        this.againstShares = againstShares;
        this.abstainShares = abstainShares;
    }

    public String getIssuerLabel() {
        return issuerLabel;
    }

    public void setIssuerLabel(String issuerLabel) {
        this.issuerLabel = issuerLabel;
    }

    public String getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(String resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }

    public String getForShares() {
        return forShares;
    }

    public void setForShares(String forShares) {
        this.forShares = forShares;
    }

    public String getAgainstShares() {
        return againstShares;
    }

    public void setAgainstShares(String againstShares) {
        this.againstShares = againstShares;
    }

    public String getAbstainShares() {
        return abstainShares;
    }

    public void setAbstainShares(String abstainShares) {
        this.abstainShares = abstainShares;
    }

    @Override
    public String toString() {
        return "Vote20 [issuerLabel=" + issuerLabel + ", resolutionStatus=" + resolutionStatus + ", forShares="
                + forShares + ", againstShares=" + againstShares + ", abstainShares=" + abstainShares + "]";
    }
}