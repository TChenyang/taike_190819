package com.nandu.taike.config.quartz;

import com.nandu.taike.quartz.MyQuartzJob;
import org.quartz.*;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;

@Configuration
public class QuartzConfig {
    private static final Logger log = LoggerFactory.getLogger(QuartzConfig.class);

    @Bean(name = "SchedulerFactory")
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException{
        log.info("初始化SchedulerFactoryBean--------------------开始-----------------");
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        //用于quartz集群,QuartzScheduler启动时，更新已存在的Scheduler Job
        //这样就不用每次修改targetObject后删除quartz_job_details表的记录了
        factory.setOverwriteExistingJobs(true);
        //任务调度监听类
        //factory.setGlobalTriggerListeners(triggerListenerLogMonitor());
        log.info("初始化SchedulerFactoryBean--------------------完毕-----------------");
        return  factory;
    }

    /*
     * 初始化监听器
     * */
    public QuartzInitializerListener executorListener(){
        log.info("-------------------初始化QuartzInitializerListener监听器------------------");
        return new QuartzInitializerListener();
    }

    /*
     * 通过SchedulerFactoryBean获取Scheduler实列
     * */
    @Bean(name = "Scheduler")
    public Scheduler scheuler() throws IOException {
        log.info("-------------------通过SchedulerFactoryBean获取Scheduler实列------------------");
        Scheduler scheduler = schedulerFactoryBean().getScheduler();
        ;
        return scheduler;
    }

    /*
    * 新增一个定时任务(测试)
    * */
    private void addMyTestJob(Scheduler scheuler){
        String startJob = "true"; //是否开始
        String jobName = "DATAMART_SYNC";
        String jobGroup = "DATAMART_SYNC";
        String cron = "0 0/1 * * * ?";//定时的时间设置
        String className = MyQuartzJob.class.getName();
        if (startJob != null && "true".equals(startJob)){
            addCommonCronJob(jobName,jobGroup,cron,scheuler,className);
        }
    }

    private void deleteCommonJob(String jobName,String jobGroup,Scheduler scheduler){
        JobKey jobKey = JobKey.jobKey(jobName,jobGroup);
        try {
            scheduler.pauseJob(jobKey);//先暂停任务
            scheduler.deleteJob(jobKey);//再删除任务
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /*
     * 添加一些定时任务,如日志清理等任务
     * */
    public void addCommonCronJob(String jobName,String jobGroup,String cron,Scheduler scheduler,String className){
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName,jobGroup);
        try {
            //任务触发
            Trigger checkExist = (CronTrigger)scheduler.getTrigger(triggerKey);
            if (checkExist == null){
                JobDetail  jobDetail = null;
                jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(className))
                        .requestRecovery(true)//当Quartz服务中止后,再次启动或集群中其他机器接受任务尝试接手任务时会尝试回复执行之前未完成的所有任务
                        .withIdentity(jobName,jobGroup)
                        .build();
                jobDetail.getJobDataMap().put("jobName",jobName);
                jobDetail.getJobDataMap().put("jobGroup",jobGroup);
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                /*withMisfireHandlingInstructionDoNothing
                ——不触发立即执行
                ——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
                withMisfireHandlingInstructionIgnoreMisfires
                ——以错过的第一个频率时间立刻开始执行
                ——重做错过的所有频率周期后
                ——当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行
                withMisfireHandlingInstructionFireAndProceed
                ——以当前时间为触发频率立刻触发一次执行
                ——然后按照Cron频率依次执行*/
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(jobName,jobGroup)
                        .withSchedule(cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires())
                        .build();
                scheduler.scheduleJob(jobDetail,trigger);
            }else {
                scheduler.deleteJob(JobKey.jobKey(jobName,jobGroup));
                addCommonCronJob(jobName,jobGroup,cron,scheduler,className);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    //            注：在QuartzConfig中注入TriggerListenerLogMonitor，将上面的注释代码去掉即可。
    //            5.设置定时任务的时间
    //            在QuartzConfig的addmyTestJob方法中设置cron，有一个很不错的在线Cron表达式生成器，推荐给大家    点击打开
    //
    //            (*)星号：可以理解为每的意思，每秒，每分，每天，每月，每年...
    //            (?)问号：问号只能出现在日期和星期这两个位置，表示这个位置的值不确定，每天3点执行，所以第六位星期的位置，我们是不需要关注的，就是不确定的值。同时：日期和星期是两个相互排斥的元素，通过问号来表明不指定值。比如，1月10日，比如是星期1，如果在星期的位置是另指定星期二，就前后冲突矛盾了。
    //            (-)减号：表达一个范围，如在小时字段中使用“10-12”，则表示从10到12点，即10,11,12
    //            (,)逗号：表达一个列表值，如在星期字段中使用“1,2,4”，则表示星期一，星期二，星期四
    //            (/)斜杠：如：x/y，x是开始值，y是步长，比如在第一位（秒） 0/15就是，从0秒开始，每15秒，最后就是0，15，30，45，60    另：*/y，等同于0/y
    //
    //            0 0 1 * * ?	每天1点触发 
    //            0 0 12 * * ?	每天12点触发 
    //            0 15 10 ? * *	每天10点15分触发 
    //            0 15 10 * * ?	每天10点15分触发 
    //            0 15 10 * * ? *	每天10点15分触发
    //            0 15 10 L * ?	每月最后一天的10点15分触发 
    //            0 15 10 15 * ?	每月15号上午10点15分触发 
    //            0 0-5 14 * * ?	每天下午的 2点到2点05分每分触发 
    //            0 0 12 1/5 * ?	每月的第一个中午开始每隔5天触发一次 
    //            0 0/2 * * * ?	每两分钟触发一次

}
