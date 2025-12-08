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

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class CronJobSocketTrigger {

    public static Scheduler scheduler = null;

    public static void startJob() throws SchedulerException {

        JobKey cronJobSocketKey = new JobKey("cronJobSocket", "group1");
        JobDetail cronJobSocketDetail = JobBuilder.newJob(CronJobSocket.class).withIdentity(cronJobSocketKey).build();

        Trigger cronJobSocketTrigger = TriggerBuilder.newTrigger().withIdentity("cronJobSocketTrigger", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0/30 * * * * ?")).build();

        if (CronJobSocketTrigger.scheduler == null) {

            CronJobSocketTrigger.scheduler = new StdSchedulerFactory().getScheduler();

        }

        CronJobSocketTrigger.scheduler.clear();
        CronJobSocketTrigger.scheduler.start();
        CronJobSocketTrigger.scheduler.deleteJob(cronJobSocketKey);
        CronJobSocketTrigger.scheduler.scheduleJob(cronJobSocketDetail, cronJobSocketTrigger);

    }

    public static void stopJob() throws SchedulerException {
        CronJobSocketTrigger.scheduler.shutdown();
    }
}
