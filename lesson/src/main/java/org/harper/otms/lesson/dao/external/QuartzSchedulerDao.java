package org.harper.otms.lesson.dao.external;

import java.text.MessageFormat;
import java.util.TimeZone;

import org.harper.otms.common.dao.Entity;
import org.harper.otms.common.util.DateUtil;
import org.harper.otms.lesson.dao.SchedulerDao;
import org.harper.otms.lesson.entity.Lesson;
import org.harper.otms.lesson.entity.LessonItem;
import org.harper.otms.lesson.entity.OneoffEntry;
import org.harper.otms.lesson.entity.RepeatEntry;
import org.harper.otms.lesson.service.impl.TriggerLessonJobDetail;
import org.quartz.CronScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.InitializingBean;

public class QuartzSchedulerDao implements SchedulerDao, InitializingBean {

	static final String SCHEDULER_GROUP = "calendar";

	@Override
	public void afterPropertiesSet() throws Exception {
		getScheduler().addJob(new TriggerLessonJobDetail(), true);
	}

	private Scheduler scheduler;

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public void setupScheduler(Entity entity) {
		String triggerId = MessageFormat.format("trigger_{0}_{1}", entity.getClass().getSimpleName(),
				Integer.toString(entity.getId()));
		TriggerBuilder<Trigger> tBuilder = TriggerBuilder.newTrigger().withIdentity(triggerId, SCHEDULER_GROUP)
				.forJob("triggerLessonJob", "calendar");
		Trigger trigger = null;

		if (entity instanceof Lesson) {
			Lesson lesson = (Lesson) entity;
			if (lesson.getCalendar() instanceof OneoffEntry) {
				OneoffEntry oe = (OneoffEntry) lesson.getCalendar();
				trigger = tBuilder.startAt(oe.getFromTime()).build();
			} else {
				RepeatEntry re = (RepeatEntry) lesson.getCalendar();
				CronScheduleBuilder csbuilder = CronScheduleBuilder.cronSchedule(re.cronExp())
						.inTimeZone(TimeZone.getTimeZone("UTC"));
				trigger = tBuilder.startAt(re.getStartDate()).endAt(DateUtil.dayend(re.getStopDate()))
						.withSchedule(csbuilder).build();
			}
			trigger.getJobDataMap().put(TriggerLessonJobDetail.LESSON_ID, String.valueOf(lesson.getId()));
		} else if (entity instanceof LessonItem) {
			LessonItem item = (LessonItem) entity;
			trigger = tBuilder.startAt(item.getFromTime()).build();
			trigger.getJobDataMap().put(TriggerLessonJobDetail.LESSON_ITEM_ID, String.valueOf(item.getId()));
		}

		TriggerKey tkey = trigger.getKey();
		try {
			if (getScheduler().checkExists(tkey)) {
				getScheduler().rescheduleJob(tkey, trigger);
			} else {
				getScheduler().scheduleJob(trigger);
			}
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void cancelScheduler(Entity entity) {
		String triggerId = MessageFormat.format("trigger_{0}_{1}", entity.getClass().getSimpleName(),
				Integer.toString(entity.getId()));
		try {
			getScheduler().unscheduleJob(new TriggerKey(triggerId, SCHEDULER_GROUP));
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

}
