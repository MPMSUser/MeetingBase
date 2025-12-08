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
package de.meetingapps.meetingportal.meetComAllg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CaLeseDatei {

    public byte[] alsBytes(String filename) {
        String pathToFile = filename;
        File file = new File(pathToFile);

        InputStream in;
        byte[] bytes = null;
        try {
            in = new FileInputStream(file);
            try {
                bytes = new byte[in.available()];
                in.read(bytes);
                in.close();
            } catch (IOException e) {
                CaBug.drucke("CaLeseDatei.alsBytes 001");
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            CaBug.drucke("CaLeseDatei.alsBytes 002 - File not found "+pathToFile);
//            e.printStackTrace();
        }
        return bytes;
    }

}
