package com.esoft.jdp2p.schedule.job;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.esoft.jdp2p.trusteeship.service.TrusteeshipService;

/**
 * 主动查询发往第三方资金托管的请求的状态，并根据返回值做相应处理。
 * 
 * @author Administrator=
 * 
 */
@Component
public class RefreshTrusteeshipOperation implements Job {

	@Resource
	TrusteeshipService trusteeshipService;
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		trusteeshipService.handleSendedOperations();
	}
}
