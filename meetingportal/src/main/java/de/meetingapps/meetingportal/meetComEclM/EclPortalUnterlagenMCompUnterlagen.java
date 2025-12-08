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
package de.meetingapps.meetingportal.meetComEclM;

import java.util.Comparator;

public class EclPortalUnterlagenMCompUnterlagen implements Comparator<EclPortalUnterlagenM> {

    @Override
    public int compare(EclPortalUnterlagenM o1, EclPortalUnterlagenM o2) {

        if (o1.getReihenfolgeUnterlagen() < o2.getReihenfolgeUnterlagen()) {
            return -1;
        }
        if (o1.getReihenfolgeUnterlagen() > o2.getReihenfolgeUnterlagen()) {
            return 1;
        }

        return 0;
    }
}
