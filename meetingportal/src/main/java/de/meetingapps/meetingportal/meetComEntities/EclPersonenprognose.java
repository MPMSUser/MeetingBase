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

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EclPersonenprognose implements Serializable {
    private static final long serialVersionUID = 2909088375057655041L;

    public int ident = 0;

    public String description = "NEU";

    public Integer distance = 0;

    public double percent = 0;

    public int max = 0;

    public int forecast = 0;

    public double realPercent = 0;

    public int realCount = 0;

    public Timestamp updated;

    public EclPersonenprognose() {

    }

    public EclPersonenprognose(int ident, String description, int distance, double percent) {
        this.ident = ident;
        this.description = description;
        this.distance = distance;
        this.percent = percent;
    }

    public EclPersonenprognose(int ident, String description, Integer distance, double percent, int max, int forecast,
            double realPercent, int realCount, Timestamp updated) {
        super();
        this.ident = ident;
        this.description = description;
        this.distance = distance;
        this.percent = percent;
        this.max = max;
        this.forecast = forecast;
        this.realPercent = realPercent;
        this.realCount = realCount;
        this.updated = updated;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getForecast() {
        return forecast;
    }

    public void setForecast(int forecast) {
        this.forecast = forecast;
    }

    public double getRealPercent() {
        return realPercent;
    }

    public void setRealPercent(double realPercent) {
        this.realPercent = realPercent;
    }

    public int getRealCount() {
        return realCount;
    }

    public void setRealCount(int realCount) {
        this.realCount = realCount;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public static String getLocalDateTime(Timestamp stamp) {

        if (stamp == null)
            return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        final ZonedDateTime timeInUTC = stamp.toLocalDateTime().atZone(ZoneId.of("UTC"));
        final LocalDateTime timeInGermany = LocalDateTime.ofInstant(timeInUTC.toInstant(), ZoneId.of("CET"));

        return timeInGermany.format(formatter);
    }

    @Override
    public String toString() {
        return "EclPersonenprognose [ident=" + ident + ", description=" + description + ", distance=" + distance
                + ", percent=" + percent + ", max=" + max + ", forecast=" + forecast + ", realPercent=" + realPercent
                + ", realCount=" + realCount + ", updated=" + updated + "]";
    }

}
