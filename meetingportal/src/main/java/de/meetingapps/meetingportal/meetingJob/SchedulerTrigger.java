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

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * The Class SchedulerTrigger.
 */
public class SchedulerTrigger {

    /**
     * Instantiates a new scheduler trigger.
     */
    public SchedulerTrigger() {
        super();
    }

    /**
     * Start quartz scheduler
     *
     * @throws SchedulerException the scheduler exception
     */
    public void start() throws SchedulerException {
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();

        /*
         * add a demo Job
         */
        JobKey demoJobKey1 = new JobKey("demoJob1", "meetingportal");
        JobDetail demoJob1 = JobBuilder.newJob(DemoJob1.class).withIdentity(demoJobKey1).build();

        /*
         * Trigger for DemoJob1 - runs every Day at 8 pm
         */
        Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("demoTrigger1", "meetingportal")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 20 * * ?")).build();

        scheduler.start();
        scheduler.scheduleJob(demoJob1, trigger1);
    }
}
