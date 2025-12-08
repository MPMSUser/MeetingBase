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
package de.meetingapps.meetingportal.meetingport;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**Konfigurationsdatei - ist erforderlich*/

/*Pfadname, an den dann die einzelnen Services angehängt werden
 * D.h. Pfad müßte dann z.B. sein:
 *
 * v1 ist quasi Versionsnummer
 * */
@ApplicationPath("/api/v1")
public class WAApplicationConfiguration extends Application {

}
