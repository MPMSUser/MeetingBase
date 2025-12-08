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
package de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys;

import java.util.List;

public class EgxGetUmsaetzeRC {

	public List<EgxGetUmsaetzeResult> result;
    public EgxGetLinksRC _links;
    
    /*
     * Standard getter und Setter
     */
    
	public List<EgxGetUmsaetzeResult> getResult() {
		return result;
	}
	public void setResult(List<EgxGetUmsaetzeResult> result) {
		this.result = result;
	}
	public EgxGetLinksRC get_links() {
		return _links;
	}
	public void set_links(EgxGetLinksRC _links) {
		this._links = _links;
	}
	
    
}
