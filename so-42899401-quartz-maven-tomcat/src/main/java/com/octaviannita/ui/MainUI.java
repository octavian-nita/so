package com.octaviannita.ui;

import com.octaviannita.core.MainJob;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletContext;

import static com.vaadin.server.Sizeable.Unit.PIXELS;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Mar 20, 2017
 */
@Title("Main UI")
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout content = new VerticalLayout();
        setContent(content);

        TextArea logTA = new TextArea();
        logTA.setWidth(800, PIXELS);
        logTA.setRows(20);

        ServletContext ctx = VaadinServlet.getCurrent().getServletContext();
        StdSchedulerFactory factory = (StdSchedulerFactory) ctx.getAttribute("org.quartz.impl.StdSchedulerFactory.KEY");
        try {
            Scheduler scheduler = factory.getScheduler("LenartScheduler");

            content.addComponents(new HorizontalLayout(new Button("Schedule Main Job", (ClickListener) clickEvent -> {
                try {
                    scheduleMainJob(scheduler);
                    log(logTA, "Main Job scheduled.");
                } catch (SchedulerException e) {
                    log(logTA, "ERROR Cannot schedule Main Job: %s", e.getMessage());
                }
            }), new Button("Unschedule Main Job", (ClickListener) clickEvent -> {
                try {
                    unscheduleMainJob(scheduler);
                    log(logTA, "Main Job unscheduled.");
                } catch (SchedulerException e) {
                    log(logTA, "ERROR Cannot unschedule Main Job: %s", e.getMessage());
                }
            })), logTA);

            log(logTA, "Scheduler %s: %s initialized.", scheduler.getSchedulerInstanceId(),
                scheduler.getSchedulerName());

        } catch (SchedulerException e) {
            log(logTA, "ERROR Cannot obtain/initialize scheduler: %s", e.getMessage());
        }
    }

    private static void log(TextArea logTA, String fmt, Object... args) {
        if (logTA != null && fmt != null) {
            String currentValue = logTA.getValue();
            currentValue = currentValue == null || currentValue.length() == 0 ? " " : currentValue + "\n ";
            logTA.setValue(currentValue + format(fmt, args));
        }
    }

    private static void scheduleMainJob(Scheduler scheduler) throws SchedulerException {
        requireNonNull(scheduler);

        JobDetail jobDetail =
            newJob(MainJob.class).storeDurably().withIdentity("MAIN_JOB").withDescription("Main Job to Perform")
                                 .build();

        Trigger trigger =
            newTrigger().forJob(jobDetail).withIdentity("MAIN_JOB_TRIGG").withDescription("Trigger for Main Job")
                        .withSchedule(simpleSchedule().withIntervalInSeconds(15).repeatForever()).startNow().build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    private static void unscheduleMainJob(Scheduler scheduler) throws SchedulerException {
        requireNonNull(scheduler).deleteJob(JobKey.jobKey("MAIN_JOB"));
    }
}
