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
package de.meetingapps.meetingportal.cron;

import java.io.IOException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import de.meetingapps.meetingportal.meetComHVParam.ParamServerStatic;
import de.meetingapps.meetingportal.meetingSocket.BsClient;


public class CronJobSocket implements Job {

    private static BsClient bsClient = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        if (bsClient == null) {
            bsClient = new BsClient(ParamServerStatic.webSocketsLocalHost, "cron");    
        }

        try {
            bsClient.sendPingToAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
