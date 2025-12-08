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
package de.meetingapps.meetingportal.meetComEh;

import java.io.Serializable;

/**Element für selectItem-Element in Liste für JSF-Anzeige*/
public class EhJsfSelectItem  implements Serializable {
    private static final long serialVersionUID = 4875480990324649282L;
     
    String itemLabel="";
    String itemValue="";
    
    public EhJsfSelectItem(String pItemLabel, String pItemValue){
        itemLabel=pItemLabel;
        itemValue=pItemValue;
    }
    
    public EhJsfSelectItem(String pText, int pStatus){
        itemLabel=pText;
        itemValue=Integer.toString(pStatus);
    }

    /************************************************Standard getter und setter*****************************************************/
    public String getItemLabel() {
        return itemLabel;
    }
    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }
    public String getItemValue() {
        return itemValue;
    }
    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }
    

}
