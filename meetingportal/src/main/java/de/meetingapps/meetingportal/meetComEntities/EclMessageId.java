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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class EclMessageId {

    private String messageId = "";

    public EclMessageId() {
    }

    public EclMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    public EclMessageId(int length) {
        createMessageId(length);
    }
    
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void createMessageId(int length) {

        final String start = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        final int leftLimit = 48; // numeral '0'
        final int rightLimit = 122; // letter 'z'
        final int targetStringLength = length - start.length();
        final Random random = new Random();

        this.messageId = start + random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }
}
