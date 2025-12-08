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
package de.meetingapps.meetingportal.meetingJob;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * The Class DemoJob1.
 */
public class DemoJob1 implements Job {

    /**
     * Execute the job.
     *
     * @param context the context
     * @throws JobExecutionException the job execution exception
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date date = new Date();
        System.out.println("[" + date.toString() + "] Demo Job1  is runing");
    }

}
