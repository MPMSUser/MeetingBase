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

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;


public class EclParameter {

    /** Our Logger. */
//    private static final Logger LOGGER = LogManager.getFormatterLogger(EclParameter.class.getName());

    /**Mandantennummer; mandant=0 => dieser Parameter gilt für alle Mandanten (soweit nicht "überschrieben"), d.h.:
     * z.B. Parameter 1 ist mit Mandant 0 Wert 50 und mit Mandant 54 mit Wert 51 gespeichert =>
     * Für den Mandant 54 hat der Parameter den Wert 51, für alle anderen den Wert 50*/
    public int mandant = 0;

    /** eindeutiger Key (zusammen mit mandant), der unveränderlich ist. */
    public int ident = 0;

    /**Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung in Db*/
    public long db_version;

    /**Wert des Parameters;
     * In normalen Tables: Länge=40
     * In tbl_parameterlang: Länge=200*/
    public String wert = "";

    /**Beschreibung des Parameters - nur für leichtere direkte Pflege in Datenbank; Länge=80*/
    public String beschreibung = "";

    public EclParameter() {
        super();
//        if (LOGGER.isTraceEnabled()) {
//            LOGGER.trace("EclParamter called:");
//        }
    }

    // protected void finalize() {
    //    if (LOGGER.isTraceEnabled()) {
    //        LOGGER.trace("EclParamter destructed");
    //    }
    //}

    /**Selektionskriterien für effizientes Einlesen der Parameter.
     * =1 => Parameter wird vom jeweiligen Modul benötigt*/
    //	public int selektionPortal=0;
    //	public int selektionApp=0;

}
