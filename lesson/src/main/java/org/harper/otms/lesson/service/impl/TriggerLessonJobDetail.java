package org.harper.otms.lesson.service.impl;

import org.harper.otms.common.spring.AppContextAccessor;
import org.harper.otms.lesson.service.LessonService;
import org.harper.otms.lesson.service.dto.TriggerLessonDto;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.impl.JobDetailImpl;

public class TriggerLessonJobDetail extends JobDetailImpl {

	public static final String LESSON_ID = "lessonId";

	public static final String LESSON_ITEM_ID = "lessonItemId";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1249874730373934596L;

	public TriggerLessonJobDetail() {
		super();
		setDurability(true);
	}

	@Override
	public Class<? extends Job> getJobClass() {
		return TriggerLessonJob.class;
	}

	@Override
	public JobKey getKey() {
		return new JobKey("triggerLessonJob", "calendar");
	}

	public static class TriggerLessonJob implements Job {
		@Override
		public void execute(JobExecutionContext context)
				throws JobExecutionException {

			TriggerLessonDto request = new TriggerLessonDto();
			// Retrieve lesson id from trigger data map
			Object lsnId = context.getTrigger().getJobDataMap().get(LESSON_ID);
			Object lsnItemId = context.getTrigger().getJobDataMap()
					.get(LESSON_ITEM_ID);
			if (lsnId != null) {
				request.setLessonId(Integer.parseInt(lsnId.toString()));
			}
			if (lsnItemId != null) {
				request.setLessonItemId(Integer.parseInt(lsnItemId.toString()));
			}
			if (null == context.getTrigger().getNextFireTime()) {
				// No more
				request.setLast(true);
			}

			getLessonService().triggerLesson(request);
		}

		public LessonService getLessonService() {
			return AppContextAccessor.getContext().getBean(LessonService.class);
		}
	}
}
