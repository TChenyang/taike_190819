package com.nandu.taike.config.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
* 添加任务调度的监听器
* */
@Component
public class TriggerListenerLogMonitor implements TriggerListener {
    private static final Logger log = LoggerFactory.getLogger(TriggerListenerLogMonitor.class);

    @Override
    public String getName() {
        return "TriggerListenerLogMonitor";
    }

    //当与监听相关联Trigger被触发,Job上的execute()方法要被执行时,Scheduler就调用这个方法
    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
        log.info("TriggerListenerLogMonitor类:"+jobExecutionContext.getTrigger().getKey().getName()+"被执行");
    }

    /*
    * 在Trigger触发后,Job将要被执行时由Scheduler调用这个方法
    * TriggerListener 给了一个选择去否决Job的执行
    * if this method return true,this job will not execute for this Trigger
    * 假如这个方法返回 true，这个 Job 将不会为此次 Trigger 触发而得到执行。
    * */
    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        return false;
    }

    /*
    * Scheduler调用这个方法是在Trigger错过触发时
    * 如这个方法的javaDoc所指出的,你应该关注此方法中持续时间长的逻辑
    *       在出现许多错过触发的Trigger时，
    *       长逻辑会导致骨牌效应
    *       你应当保持这上方法尽量的小
    * */
    @Override
    public void triggerMisfired(Trigger trigger) {
        log.info("Job错过触发");
    }

    /*多米诺骨牌效应(骨牌效应):该效应产生的能量是十分巨大的。
    这种效应的物理道理是：骨牌竖着时，重心较高，倒下时重心下降，倒下过程中，
    将其重力势能转化为动能，它倒在第二张牌上，这个动能就转移到第二张牌上，
    第二张牌将第一张牌转移来的动能和自已倒下过程中由本身具有的重力势能转化来的动能之和，
    再传到第三张牌上......所以每张牌倒下的时候，具有的动能都比前一块牌大，因此它们的速度一个比一个快，
    也就是说，它们依次推倒的能量一个比一个大*/

    /*
     *  Trigger 被触发并且完成了Job的执行时,Scheduler 调用这个方法
     *  这不是说这个Trigger将不再触发了,而仅仅是当前Trigger的触发(并且紧接这Job执行)结束时。
     *  这个Trigger也许还要在将来触发多次的
     * */
    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {
        log.info("Job执行完毕，Trigger完成");
    }
}
