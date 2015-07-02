package com.esoft.archer.node.controller;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.node.NodeConstants;
import com.esoft.archer.node.model.NodeAttr;
import com.esoft.archer.node.model.NodeBody;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;
@Component
@Scope(ScopeType.VIEW)
public class NodeBodyHome extends EntityHome<NodeBody>{

	
	@Logger
	static Log log;
	private static final StringManager sm = StringManager
			.getManager(NodeConstants.Package);


	
	public NodeBody getBodyById(){
		
		NodeBody body=getBaseService().get(NodeBody.class, "7d806664cd904a2c97e9d133e0b7b96f");
		return body;
	}
}
