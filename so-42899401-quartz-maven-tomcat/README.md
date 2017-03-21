# Answering SO Question #42899401

[How to run java class in back ground of server in Maven / Vaadin project?](http://stackoverflow.com/questions/42899401/how-to-run-java-class-in-back-ground-of-server-in-maven-vaadin-project)
---
Using Quartz is, indeed, one of the most straightforward ways of doing this programmatically, since you will already have a server/app running.

Having said that, employing it in any Java web application is, obviously, independent of the UI technology you might use (Vaadin included) and IMHO, it's better to reason about it independently.

For the sake of completeness, I will go through all the steps involved in adding Quartz to a Maven-managed, Java web application below.

**Adding Quartz as a Maven Dependency**

Adding these in your *pom.xml* should suffice:

    <dependency>
        <groupId>org.quartz-scheduler</groupId>
        <artifactId>quartz</artifactId>
        <version>2.2.3</version>
    </dependency>

**Initializing a Quartz Scheduler in a Servlet Container**

A default Quartz scheduler is automatically created and initialized upon Servlet Context initialization (as illustrated at http://www.quartz-scheduler.org/documentation/quartz-2.x/cookbook/ServletInitScheduler.html) by declaring a listener and several context parameters in the web.xml descriptor:

    <context-param>
        <param-name>quartz:config-file</param-name>
        <param-value>/quartz.properties</param-value>
    </context-param>
    <context-param>
        <param-name>quartz:shutdown-on-unload</param-name>
        <param-value>true</param-value>
    </context-param>
    <context-param>
        <param-name>quartz:wait-on-shutdown</param-name>
        <param-value>false</param-value>
    </context-param>
    <context-param>
        <param-name>quartz:start-scheduler-on-load</param-name>
        <param-value>true</param-value>
    </context-param>
    <listener>
        <listener-class>
            org.quartz.ee.servlet.QuartzInitializerListener
        </listener-class>
    </listener>

You should also configure the scheduler by providing a basic *quartz.properties* file:

    org.quartz.scheduler.instanceName = LenartScheduler
    org.quartz.scheduler.instanceId = LenartScheduler.ID
    org.quartz.threadPool.threadCount = 3
    org.quartz.jobStore.class = org.quartz.simpl.RAMJobStore

At this point, after the application has been deployed / started, a Quartz scheduler instance can be obtained from a "standard" scheduler factory available to you under a default key in the web application's ServletContext object:

    StdSchedulerFactory factory = (StdSchedulerFactory)
      ctx.getAttribute("org.quartz.impl.StdSchedulerFactory.KEY");
    try {
        Scheduler scheduler = factory.getScheduler("LenartScheduler");
        // schedule Jobs here...
    } catch (SchedulerException e) {
        // properly handle the exception...
    }

Note that we've used the scheduler name (*LenartScheduler*) specified in the *quartz.properties* file above. As well, note that at this point, nothing is scheduled yet – all we have is a scheduler ready to be used.

**Creating the Job Class**

Easily done by implementing *org.quartz.Job*:

    import org.quartz.Job;
    import org.quartz.JobExecutionContext;
    import org.quartz.JobExecutionException;
    
    public class MainJob implements Job {
    
        @Override
        public void execute(JobExecutionContext jobExecutionContext)
          throws JobExecutionException {
    
            // Simulate job execution for 5 seconds...
            try {
                System.out.println("Executing job in background...");
                Thread.sleep(1000 * 5 /* secs */);
                System.out.println("Done executing job.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

**Scheduling the Job**

Given the available Scheduler, we need to define:

  1. the so-called "details" of a Job

**AND**

  2. a trigger for those details
and use them both to finally schedule the job:

    private void scheduleMainJob(Scheduler scheduler)
      throws SchedulerException {
        requireNonNull(scheduler);
    
        JobDetail jobDetail = newJob(MainJob.class).storeDurably()
                                                   .withIdentity("MAIN_JOB")
                                                   .withDescription("Main Job to Perform")
                                                   .build();
        Trigger trigger = newTrigger().forJob(jobDetail)
                                      .withIdentity("MAIN_JOB_TRIGG")
                                      .withDescription("Trigger for Main Job")
                     .withSchedule(simpleSchedule().withIntervalInSeconds(15).repeatForever())
                     .startNow().build();
    
        scheduler.scheduleJob(jobDetail, trigger);
    }

Note the simple way of specifying when to trigger the job (no *cron* expression involved for your simple case, at least not yet). For the sake of the example, I am triggering the job every 15 seconds and let it run for 5 -- if you want to trigger it every 24 hours (i.e. one a day) you can use *simpleSchedule().withIntervalInHours(24).repeatForever()*.

**Automatically Scheduling the Job**

Now, the astute will notice that we still haven’t called the scheduling functionality just yet. We could either do it ***"manually"***, by defining some sort of an admin servlet/UI and upon user interaction to call the scheduling method defined above or, if we are OK with using the predefined/hardcoded values, ***automatically***, upon servlet context start up, like we did it with the scheduler.

Let's say we want to automatically schedule the main job, upon servlet context start up. We again have at least 2 options:

  1. *implement a ServletContextListener* that does/calls the scheduling routine above and make sure it is called after the QuartzInitializerListener we've declared in order to have the scheduler created

**OR**

  2. *extend the QuartzInitializerListener* class to schedule our main job just after the scheduler gets created; this way, we don’t have to worry about the order in which the context listeners get called:

    public class LenartQuartzListener extends QuartzInitializerListener {
    
        @Override
        public void contextInitialized(ServletContextEvent evt) {
            super.contextInitialized(evt);
            // At this point, the default functionality
            // has been executed hence the scheduler has been created!
    
            ServletContext ctx = evt.getServletContext();
            StdSchedulerFactory factory = (StdSchedulerFactory)
              ctx.getAttribute("org.quartz.impl.StdSchedulerFactory.KEY");
            try {
                scheduleMainJob(factory.getScheduler("LenartScheduler"));
            } catch (SchedulerException e) {
                // properly handle the exception...
            }
        }
    }

However, if we're using the (better, IMHO) second option, we do need to specified in the *web.xml* file our new Quartz listener, instead if the old one:

    <listener>
        <listener-class>com.lenard.web.LenartQuartzListener</listener-class>
    </listener>

At this point, never mind the UI technology used (Vaadin, etc.), a Quartz scheduler is automatically initialized and a job scheduled upon (web) application start up.

**If using Vaadin**

These days, one can initialize a Vaadin-based web application without using a web.xml descriptor. If that's your case, note that you now need to add the web.xml file that specifies the Quartz initialization we've been talking about. But this does not clash with any Vaadin-specific stuff…
