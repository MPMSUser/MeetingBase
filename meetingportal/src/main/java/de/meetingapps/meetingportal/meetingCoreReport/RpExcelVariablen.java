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
package de.meetingapps.meetingportal.meetingCoreReport;

public class RpExcelVariablen {
    
    public Object obj = null;
    
    public int column = 0;
    
    public int format = RpExcel.TEXT;
    
    public int length = 0;
    
    public RpExcelVariablen() {
        
    }
    
    /*
     * Normale Funktion zum erstellen von Variablen
     */
    public RpExcelVariablen(Object obj, int column, int format) {
        super();
        this.obj = obj;
        this.column = column;
        this.format = format;
    }
    
    /*
     * Wird fuer Formeln verwendet um die Spaltenbreite richtig zu erstellen.
     */
    public RpExcelVariablen(Object obj, int column, int format, int length) {
        super();
        this.obj = obj;
        this.column = column;
        this.format = format;
        this.length = length;
    }
    
}
