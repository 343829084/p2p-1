package com.esoft.jdp2p.loan.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.loan.model.LoanType;

/**
 * Description: 借款类型查询相关 Copyright: Copyright (c)2013 Company: jdp2p
 * 
 * @author: wangzhi
 * @since 2.0
 */
@Component
@Scope(ScopeType.VIEW)
public class LoanTypeList extends EntityQuery<LoanType> {
	

	public LoanTypeList() {
//		setAllResultList(null);
//		List<LoanType> list=new ArrayList();
//		list.add(new LoanType("loan_type_2","等额本息，按月计息，按月还款，放款后生息","等额本息，按月计息，按月还款，放款后生息"));
//		setAllResultList(list);
		final String[] RESTRICTIONS = { "id like #{loanTypeList.example.id}"
				,"name like #{loanTypeList.example.name}" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	
	}
	
}
