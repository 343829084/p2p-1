/**
 * QueryValidatorServicesImplServiceStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
package com.esoft.zcxy;

/*
 *  QueryValidatorServicesImplServiceStub java implementation
 */

public class QueryValidatorServicesImplServiceStub extends
		org.apache.axis2.client.Stub {
	protected org.apache.axis2.description.AxisOperation[] _operations;

	// hashmaps to keep the fault mapping
	private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();
	private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();
	private java.util.HashMap faultMessageMap = new java.util.HashMap();

	private static int counter = 0;

	private static synchronized java.lang.String getUniqueSuffix() {
		// reset the counter if it is greater than 99999
		if (counter > 99999) {
			counter = 0;
		}
		counter = counter + 1;
		return java.lang.Long.toString(java.lang.System.currentTimeMillis())
				+ "_" + counter;
	}

	private void populateAxisService() throws org.apache.axis2.AxisFault {

		// creating the Service with a unique name
		_service = new org.apache.axis2.description.AxisService(
				"QueryValidatorServicesImplService" + getUniqueSuffix());
		addAnonymousOperations();

		// creating the operations
		org.apache.axis2.description.AxisOperation __operation;

		_operations = new org.apache.axis2.description.AxisOperation[2];

		__operation = new org.apache.axis2.description.OutInAxisOperation();

		__operation.setName(new javax.xml.namespace.QName(
				"http://zcxyws.ztzx.com/", "queryBatch"));
		_service.addOperation(__operation);

		_operations[0] = __operation;

		__operation = new org.apache.axis2.description.OutInAxisOperation();

		__operation.setName(new javax.xml.namespace.QName(
				"http://zcxyws.ztzx.com/", "querySingle"));
		_service.addOperation(__operation);

		_operations[1] = __operation;

	}

	// populates the faults
	private void populateFaults() {

	}

	/**
	 * Constructor that takes in a configContext
	 */

	public QueryValidatorServicesImplServiceStub(
			org.apache.axis2.context.ConfigurationContext configurationContext,
			java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
		this(configurationContext, targetEndpoint, false);
	}

	/**
	 * Constructor that takes in a configContext and useseperate listner
	 */
	public QueryValidatorServicesImplServiceStub(
			org.apache.axis2.context.ConfigurationContext configurationContext,
			java.lang.String targetEndpoint, boolean useSeparateListener)
			throws org.apache.axis2.AxisFault {
		// To populate AxisService
		populateAxisService();
		populateFaults();

		_serviceClient = new org.apache.axis2.client.ServiceClient(
				configurationContext, _service);

		_serviceClient.getOptions().setTo(
				new org.apache.axis2.addressing.EndpointReference(
						targetEndpoint));
		_serviceClient.getOptions().setUseSeparateListener(useSeparateListener);

	}

	/**
	 * Default Constructor
	 */
	public QueryValidatorServicesImplServiceStub(
			org.apache.axis2.context.ConfigurationContext configurationContext)
			throws org.apache.axis2.AxisFault {

		this(configurationContext, ZcxyConstants.Config.URL);

	}

	/**
	 * Default Constructor
	 */
	public QueryValidatorServicesImplServiceStub()
			throws org.apache.axis2.AxisFault {

		this(ZcxyConstants.Config.URL);

	}

	/**
	 * Constructor taking the target endpoint
	 */
	public QueryValidatorServicesImplServiceStub(java.lang.String targetEndpoint)
			throws org.apache.axis2.AxisFault {
		this(null, targetEndpoint);
	}

	/**
	 * Auto generated method signature
	 * 
	 * @see com.esoft.zcxy.QueryValidatorServicesImplService#queryBatch
	 * @param queryBatch0
	 */

	public com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchResponseE queryBatch(

	com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchE queryBatch0)

	throws java.rmi.RemoteException

	{
		org.apache.axis2.context.MessageContext _messageContext = null;
		try {
			org.apache.axis2.client.OperationClient _operationClient = _serviceClient
					.createClient(_operations[0].getName());
			_operationClient.getOptions().setAction(
					"http://zcxyws.ztzx.com/QueryValidatorServices/queryBatch");
			_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(
					true);

			addPropertyToOperationClient(
					_operationClient,
					org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
					"&");

			// create a message context
			_messageContext = new org.apache.axis2.context.MessageContext();

			// create SOAP envelope with that payload
			org.apache.axiom.soap.SOAPEnvelope env = null;

			env = toEnvelope(getFactory(_operationClient.getOptions()
					.getSoapVersionURI()), queryBatch0,
					optimizeContent(new javax.xml.namespace.QName(
							"http://zcxyws.ztzx.com/", "queryBatch")),
					new javax.xml.namespace.QName("http://zcxyws.ztzx.com/",
							"queryBatch"));

			// adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);

			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);

			// execute the operation client
			_operationClient.execute(true);

			org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
					.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
			org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext
					.getEnvelope();

			java.lang.Object object = fromOM(
					_returnEnv.getBody().getFirstElement(),
					com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchResponseE.class,
					getEnvelopeNamespaces(_returnEnv));

			return (com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchResponseE) object;

		} catch (org.apache.axis2.AxisFault f) {

			org.apache.axiom.om.OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap
						.containsKey(new org.apache.axis2.client.FaultMapKey(
								faultElt.getQName(), "queryBatch"))) {
					// make the fault by reflection
					try {
						java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
								.get(new org.apache.axis2.client.FaultMapKey(
										faultElt.getQName(), "queryBatch"));
						java.lang.Class exceptionClass = java.lang.Class
								.forName(exceptionClassName);
						java.lang.reflect.Constructor constructor = exceptionClass
								.getConstructor(String.class);
						java.lang.Exception ex = (java.lang.Exception) constructor
								.newInstance(f.getMessage());
						// message class
						java.lang.String messageClassName = (java.lang.String) faultMessageMap
								.get(new org.apache.axis2.client.FaultMapKey(
										faultElt.getQName(), "queryBatch"));
						java.lang.Class messageClass = java.lang.Class
								.forName(messageClassName);
						java.lang.Object messageObject = fromOM(faultElt,
								messageClass, null);
						java.lang.reflect.Method m = exceptionClass.getMethod(
								"setFaultMessage",
								new java.lang.Class[] { messageClass });
						m.invoke(ex, new java.lang.Object[] { messageObject });

						throw new java.rmi.RemoteException(ex.getMessage(), ex);
					} catch (java.lang.ClassCastException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.reflect.InvocationTargetException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.IllegalAccessException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.InstantiationException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		} finally {
			if (_messageContext.getTransportOut() != null) {
				_messageContext.getTransportOut().getSender()
						.cleanup(_messageContext);
			}
		}
	}

	/**
	 * Auto generated method signature for Asynchronous Invocations
	 * 
	 * @see com.esoft.zcxy.QueryValidatorServicesImplService#startqueryBatch
	 * @param queryBatch0
	 */
	public void startqueryBatch(

			com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchE queryBatch0,

			final com.esoft.zcxy.QueryValidatorServicesImplServiceCallbackHandler callback)

	throws java.rmi.RemoteException {

		org.apache.axis2.client.OperationClient _operationClient = _serviceClient
				.createClient(_operations[0].getName());
		_operationClient.getOptions().setAction(
				"http://zcxyws.ztzx.com/QueryValidatorServices/queryBatch");
		_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

		addPropertyToOperationClient(
				_operationClient,
				org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
				"&");

		// create SOAP envelope with that payload
		org.apache.axiom.soap.SOAPEnvelope env = null;
		final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

		// Style is Doc.

		env = toEnvelope(getFactory(_operationClient.getOptions()
				.getSoapVersionURI()), queryBatch0,
				optimizeContent(new javax.xml.namespace.QName(
						"http://zcxyws.ztzx.com/", "queryBatch")),
				new javax.xml.namespace.QName("http://zcxyws.ztzx.com/",
						"queryBatch"));

		// adding SOAP soap_headers
		_serviceClient.addHeadersToEnvelope(env);
		// create message context with that soap envelope
		_messageContext.setEnvelope(env);

		// add the message context to the operation client
		_operationClient.addMessageContext(_messageContext);

		_operationClient
				.setCallback(new org.apache.axis2.client.async.AxisCallback() {
					public void onMessage(
							org.apache.axis2.context.MessageContext resultContext) {
						try {
							org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext
									.getEnvelope();

							java.lang.Object object = fromOM(
									resultEnv.getBody().getFirstElement(),
									com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchResponseE.class,
									getEnvelopeNamespaces(resultEnv));
							callback.receiveResultqueryBatch((com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchResponseE) object);

						} catch (org.apache.axis2.AxisFault e) {
							callback.receiveErrorqueryBatch(e);
						}
					}

					public void onError(java.lang.Exception error) {
						if (error instanceof org.apache.axis2.AxisFault) {
							org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
							org.apache.axiom.om.OMElement faultElt = f
									.getDetail();
							if (faultElt != null) {
								if (faultExceptionNameMap
										.containsKey(new org.apache.axis2.client.FaultMapKey(
												faultElt.getQName(),
												"queryBatch"))) {
									// make the fault by reflection
									try {
										java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
												.get(new org.apache.axis2.client.FaultMapKey(
														faultElt.getQName(),
														"queryBatch"));
										java.lang.Class exceptionClass = java.lang.Class
												.forName(exceptionClassName);
										java.lang.reflect.Constructor constructor = exceptionClass
												.getConstructor(String.class);
										java.lang.Exception ex = (java.lang.Exception) constructor
												.newInstance(f.getMessage());
										// message class
										java.lang.String messageClassName = (java.lang.String) faultMessageMap
												.get(new org.apache.axis2.client.FaultMapKey(
														faultElt.getQName(),
														"queryBatch"));
										java.lang.Class messageClass = java.lang.Class
												.forName(messageClassName);
										java.lang.Object messageObject = fromOM(
												faultElt, messageClass, null);
										java.lang.reflect.Method m = exceptionClass
												.getMethod(
														"setFaultMessage",
														new java.lang.Class[] { messageClass });
										m.invoke(
												ex,
												new java.lang.Object[] { messageObject });

										callback.receiveErrorqueryBatch(new java.rmi.RemoteException(
												ex.getMessage(), ex));
									} catch (java.lang.ClassCastException e) {
										// we cannot intantiate the class -
										// throw the original Axis fault
										callback.receiveErrorqueryBatch(f);
									} catch (java.lang.ClassNotFoundException e) {
										// we cannot intantiate the class -
										// throw the original Axis fault
										callback.receiveErrorqueryBatch(f);
									} catch (java.lang.NoSuchMethodException e) {
										// we cannot intantiate the class -
										// throw the original Axis fault
										callback.receiveErrorqueryBatch(f);
									} catch (java.lang.reflect.InvocationTargetException e) {
										// we cannot intantiate the class -
										// throw the original Axis fault
										callback.receiveErrorqueryBatch(f);
									} catch (java.lang.IllegalAccessException e) {
										// we cannot intantiate the class -
										// throw the original Axis fault
										callback.receiveErrorqueryBatch(f);
									} catch (java.lang.InstantiationException e) {
										// we cannot intantiate the class -
										// throw the original Axis fault
										callback.receiveErrorqueryBatch(f);
									} catch (org.apache.axis2.AxisFault e) {
										// we cannot intantiate the class -
										// throw the original Axis fault
										callback.receiveErrorqueryBatch(f);
									}
								} else {
									callback.receiveErrorqueryBatch(f);
								}
							} else {
								callback.receiveErrorqueryBatch(f);
							}
						} else {
							callback.receiveErrorqueryBatch(error);
						}
					}

					public void onFault(
							org.apache.axis2.context.MessageContext faultContext) {
						org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils
								.getInboundFaultFromMessageContext(faultContext);
						onError(fault);
					}

					public void onComplete() {
						try {
							_messageContext.getTransportOut().getSender()
									.cleanup(_messageContext);
						} catch (org.apache.axis2.AxisFault axisFault) {
							callback.receiveErrorqueryBatch(axisFault);
						}
					}
				});

		org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
		if (_operations[0].getMessageReceiver() == null
				&& _operationClient.getOptions().isUseSeparateListener()) {
			_callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
			_operations[0].setMessageReceiver(_callbackReceiver);
		}

		// execute the operation client
		_operationClient.execute(false);

	}

	/**
	 * Auto generated method signature
	 * 
	 * @see com.esoft.zcxy.QueryValidatorServicesImplService#querySingle
	 * @param querySingle2
	 */

	public com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleResponseE querySingle(

			com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleE querySingle2)

	throws java.rmi.RemoteException

	{
		org.apache.axis2.context.MessageContext _messageContext = null;
		try {
			org.apache.axis2.client.OperationClient _operationClient = _serviceClient
					.createClient(_operations[1].getName());
			_operationClient
					.getOptions()
					.setAction(
							"http://zcxyws.ztzx.com/QueryValidatorServices/querySingle");
			_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(
					true);

			addPropertyToOperationClient(
					_operationClient,
					org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
					"&");

			// create a message context
			_messageContext = new org.apache.axis2.context.MessageContext();

			// create SOAP envelope with that payload
			org.apache.axiom.soap.SOAPEnvelope env = null;

			env = toEnvelope(getFactory(_operationClient.getOptions()
					.getSoapVersionURI()), querySingle2,
					optimizeContent(new javax.xml.namespace.QName(
							"http://zcxyws.ztzx.com/", "querySingle")),
					new javax.xml.namespace.QName("http://zcxyws.ztzx.com/",
							"querySingle"));

			// adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);

			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);

			// execute the operation client
			_operationClient.execute(true);

			org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
					.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
			org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext
					.getEnvelope();

			java.lang.Object object = fromOM(
					_returnEnv.getBody().getFirstElement(),
					com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleResponseE.class,
					getEnvelopeNamespaces(_returnEnv));

			return (com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleResponseE) object;

		} catch (org.apache.axis2.AxisFault f) {

			org.apache.axiom.om.OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap
						.containsKey(new org.apache.axis2.client.FaultMapKey(
								faultElt.getQName(), "querySingle"))) {
					// make the fault by reflection
					try {
						java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
								.get(new org.apache.axis2.client.FaultMapKey(
										faultElt.getQName(), "querySingle"));
						java.lang.Class exceptionClass = java.lang.Class
								.forName(exceptionClassName);
						java.lang.reflect.Constructor constructor = exceptionClass
								.getConstructor(String.class);
						java.lang.Exception ex = (java.lang.Exception) constructor
								.newInstance(f.getMessage());
						// message class
						java.lang.String messageClassName = (java.lang.String) faultMessageMap
								.get(new org.apache.axis2.client.FaultMapKey(
										faultElt.getQName(), "querySingle"));
						java.lang.Class messageClass = java.lang.Class
								.forName(messageClassName);
						java.lang.Object messageObject = fromOM(faultElt,
								messageClass, null);
						java.lang.reflect.Method m = exceptionClass.getMethod(
								"setFaultMessage",
								new java.lang.Class[] { messageClass });
						m.invoke(ex, new java.lang.Object[] { messageObject });

						throw new java.rmi.RemoteException(ex.getMessage(), ex);
					} catch (java.lang.ClassCastException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.reflect.InvocationTargetException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.IllegalAccessException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.InstantiationException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		} finally {
			if (_messageContext.getTransportOut() != null) {
				_messageContext.getTransportOut().getSender()
						.cleanup(_messageContext);
			}
		}
	}

	/**
	 * Auto generated method signature for Asynchronous Invocations
	 * 
	 * @see com.esoft.zcxy.QueryValidatorServicesImplService#startquerySingle
	 * @param querySingle2
	 */
	public void startquerySingle(

			com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleE querySingle2,

			final com.esoft.zcxy.QueryValidatorServicesImplServiceCallbackHandler callback)

	throws java.rmi.RemoteException {

		org.apache.axis2.client.OperationClient _operationClient = _serviceClient
				.createClient(_operations[1].getName());
		_operationClient.getOptions().setAction(
				"http://zcxyws.ztzx.com/QueryValidatorServices/querySingle");
		_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

		addPropertyToOperationClient(
				_operationClient,
				org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
				"&");

		// create SOAP envelope with that payload
		org.apache.axiom.soap.SOAPEnvelope env = null;
		final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

		// Style is Doc.

		env = toEnvelope(getFactory(_operationClient.getOptions()
				.getSoapVersionURI()), querySingle2,
				optimizeContent(new javax.xml.namespace.QName(
						"http://zcxyws.ztzx.com/", "querySingle")),
				new javax.xml.namespace.QName("http://zcxyws.ztzx.com/",
						"querySingle"));

		// adding SOAP soap_headers
		_serviceClient.addHeadersToEnvelope(env);
		// create message context with that soap envelope
		_messageContext.setEnvelope(env);

		// add the message context to the operation client
		_operationClient.addMessageContext(_messageContext);

		_operationClient
				.setCallback(new org.apache.axis2.client.async.AxisCallback() {
					public void onMessage(
							org.apache.axis2.context.MessageContext resultContext) {
						try {
							org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext
									.getEnvelope();

							java.lang.Object object = fromOM(
									resultEnv.getBody().getFirstElement(),
									com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleResponseE.class,
									getEnvelopeNamespaces(resultEnv));
							callback.receiveResultquerySingle((com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleResponseE) object);

						} catch (org.apache.axis2.AxisFault e) {
							callback.receiveErrorquerySingle(e);
						}
					}

					public void onError(java.lang.Exception error) {
						if (error instanceof org.apache.axis2.AxisFault) {
							org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
							org.apache.axiom.om.OMElement faultElt = f
									.getDetail();
							if (faultElt != null) {
								if (faultExceptionNameMap
										.containsKey(new org.apache.axis2.client.FaultMapKey(
												faultElt.getQName(),
												"querySingle"))) {
									// make the fault by reflection
									try {
										java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
												.get(new org.apache.axis2.client.FaultMapKey(
														faultElt.getQName(),
														"querySingle"));
										java.lang.Class exceptionClass = java.lang.Class
												.forName(exceptionClassName);
										java.lang.reflect.Constructor constructor = exceptionClass
												.getConstructor(String.class);
										java.lang.Exception ex = (java.lang.Exception) constructor
												.newInstance(f.getMessage());
										// message class
										java.lang.String messageClassName = (java.lang.String) faultMessageMap
												.get(new org.apache.axis2.client.FaultMapKey(
														faultElt.getQName(),
														"querySingle"));
										java.lang.Class messageClass = java.lang.Class
												.forName(messageClassName);
										java.lang.Object messageObject = fromOM(
												faultElt, messageClass, null);
										java.lang.reflect.Method m = exceptionClass
												.getMethod(
														"setFaultMessage",
														new java.lang.Class[] { messageClass });
										m.invoke(
												ex,
												new java.lang.Object[] { messageObject });

										callback.receiveErrorquerySingle(new java.rmi.RemoteException(
												ex.getMessage(), ex));
									} catch (java.lang.ClassCastException e) {
										// we cannot intantiate the class -
										// throw the original Axis fault
										callback.receiveErrorquerySingle(f);
									} catch (java.lang.ClassNotFoundException e) {
										// we cannot intantiate the class -
										// throw the original Axis fault
										callback.receiveErrorquerySingle(f);
									} catch (java.lang.NoSuchMethodException e) {
										// we cannot intantiate the class -
										// throw the original Axis fault
										callback.receiveErrorquerySingle(f);
									} catch (java.lang.reflect.InvocationTargetException e) {
										// we cannot intantiate the class -
										// throw the original Axis fault
										callback.receiveErrorquerySingle(f);
									} catch (java.lang.IllegalAccessException e) {
										// we cannot intantiate the class -
										// throw the original Axis fault
										callback.receiveErrorquerySingle(f);
									} catch (java.lang.InstantiationException e) {
										// we cannot intantiate the class -
										// throw the original Axis fault
										callback.receiveErrorquerySingle(f);
									} catch (org.apache.axis2.AxisFault e) {
										// we cannot intantiate the class -
										// throw the original Axis fault
										callback.receiveErrorquerySingle(f);
									}
								} else {
									callback.receiveErrorquerySingle(f);
								}
							} else {
								callback.receiveErrorquerySingle(f);
							}
						} else {
							callback.receiveErrorquerySingle(error);
						}
					}

					public void onFault(
							org.apache.axis2.context.MessageContext faultContext) {
						org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils
								.getInboundFaultFromMessageContext(faultContext);
						onError(fault);
					}

					public void onComplete() {
						try {
							_messageContext.getTransportOut().getSender()
									.cleanup(_messageContext);
						} catch (org.apache.axis2.AxisFault axisFault) {
							callback.receiveErrorquerySingle(axisFault);
						}
					}
				});

		org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
		if (_operations[1].getMessageReceiver() == null
				&& _operationClient.getOptions().isUseSeparateListener()) {
			_callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
			_operations[1].setMessageReceiver(_callbackReceiver);
		}

		// execute the operation client
		_operationClient.execute(false);

	}

	/**
	 * A utility method that copies the namepaces from the SOAPEnvelope
	 */
	private java.util.Map getEnvelopeNamespaces(
			org.apache.axiom.soap.SOAPEnvelope env) {
		java.util.Map returnMap = new java.util.HashMap();
		java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
		while (namespaceIterator.hasNext()) {
			org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator
					.next();
			returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
		}
		return returnMap;
	}

	private javax.xml.namespace.QName[] opNameArray = null;

	private boolean optimizeContent(javax.xml.namespace.QName opName) {

		if (opNameArray == null) {
			return false;
		}
		for (int i = 0; i < opNameArray.length; i++) {
			if (opName.equals(opNameArray[i])) {
				return true;
			}
		}
		return false;
	}

	// http://211.147.7.47/zcxy/services/QueryValidatorServices
	public static class QueryBatch implements
			org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * queryBatch Namespace URI = http://zcxyws.ztzx.com/ Namespace Prefix =
		 * ns1
		 */

		/**
		 * field for Arg0
		 */

		protected java.lang.String localArg0;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localArg0Tracker = false;

		public boolean isArg0Specified() {
			return localArg0Tracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getArg0() {
			return localArg0;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Arg0
		 */
		public void setArg0(java.lang.String param) {
			localArg0Tracker = param != null;

			this.localArg0 = param;

		}

		/**
		 * field for Arg1
		 */

		protected java.lang.String localArg1;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localArg1Tracker = false;

		public boolean isArg1Specified() {
			return localArg1Tracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getArg1() {
			return localArg1;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Arg1
		 */
		public void setArg1(java.lang.String param) {
			localArg1Tracker = param != null;

			this.localArg1 = param;

		}

		/**
		 * field for Arg2
		 */

		protected java.lang.String localArg2;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localArg2Tracker = false;

		public boolean isArg2Specified() {
			return localArg2Tracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getArg2() {
			return localArg2;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Arg2
		 */
		public void setArg2(java.lang.String param) {
			localArg2Tracker = param != null;

			this.localArg2 = param;

		}

		/**
		 * field for Arg3
		 */

		protected java.lang.String localArg3;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localArg3Tracker = false;

		public boolean isArg3Specified() {
			return localArg3Tracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getArg3() {
			return localArg3;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Arg3
		 */
		public void setArg3(java.lang.String param) {
			localArg3Tracker = param != null;

			this.localArg3 = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(
				final javax.xml.namespace.QName parentQName,
				final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(
					this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter,
				boolean serializeType)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {

			java.lang.String prefix = null;
			java.lang.String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(),
					xmlWriter);

			if (serializeType) {

				java.lang.String namespacePrefix = registerPrefix(xmlWriter,
						"http://zcxyws.ztzx.com/");
				if ((namespacePrefix != null)
						&& (namespacePrefix.trim().length() > 0)) {
					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance",
							"type", namespacePrefix + ":queryBatch", xmlWriter);
				} else {
					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance",
							"type", "queryBatch", xmlWriter);
				}

			}
			if (localArg0Tracker) {
				namespace = "";
				writeStartElement(null, namespace, "arg0", xmlWriter);

				if (localArg0 == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException(
							"arg0 cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localArg0);

				}

				xmlWriter.writeEndElement();
			}
			if (localArg1Tracker) {
				namespace = "";
				writeStartElement(null, namespace, "arg1", xmlWriter);

				if (localArg1 == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException(
							"arg1 cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localArg1);

				}

				xmlWriter.writeEndElement();
			}
			if (localArg2Tracker) {
				namespace = "";
				writeStartElement(null, namespace, "arg2", xmlWriter);

				if (localArg2 == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException(
							"arg2 cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localArg2);

				}

				xmlWriter.writeEndElement();
			}
			if (localArg3Tracker) {
				namespace = "";
				writeStartElement(null, namespace, "arg3", xmlWriter);

				if (localArg3 == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException(
							"arg3 cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localArg3);

				}

				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement();

		}

		private static java.lang.String generatePrefix(
				java.lang.String namespace) {
			if (namespace.equals("http://zcxyws.ztzx.com/")) {
				return "ns1";
			}
			return org.apache.axis2.databinding.utils.BeanUtil
					.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(java.lang.String prefix,
				java.lang.String namespace, java.lang.String localPart,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(java.lang.String prefix,
				java.lang.String namespace, java.lang.String attName,
				java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(java.lang.String namespace,
				java.lang.String attName, java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(java.lang.String namespace,
				java.lang.String attName, javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			java.lang.String attributeNamespace = qname.getNamespaceURI();
			java.lang.String attributePrefix = xmlWriter
					.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			java.lang.String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix
							+ ":"
							+ org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter
							.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				}

			} else {
				xmlWriter
						.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
				java.lang.String namespaceURI = null;
				java.lang.String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite
									.append(prefix)
									.append(":")
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						} else {
							stringToWrite
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						}
					} else {
						stringToWrite
								.append(org.apache.axis2.databinding.utils.ConverterUtil
										.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private java.lang.String registerPrefix(
				javax.xml.stream.XMLStreamWriter xmlWriter,
				java.lang.String namespace)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter
						.getNamespaceContext();
				while (true) {
					java.lang.String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil
							.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(
				javax.xml.namespace.QName qName)
				throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			if (localArg0Tracker) {
				elementList.add(new javax.xml.namespace.QName("", "arg0"));

				if (localArg0 != null) {
					elementList
							.add(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(localArg0));
				} else {
					throw new org.apache.axis2.databinding.ADBException(
							"arg0 cannot be null!!");
				}
			}
			if (localArg1Tracker) {
				elementList.add(new javax.xml.namespace.QName("", "arg1"));

				if (localArg1 != null) {
					elementList
							.add(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(localArg1));
				} else {
					throw new org.apache.axis2.databinding.ADBException(
							"arg1 cannot be null!!");
				}
			}
			if (localArg2Tracker) {
				elementList.add(new javax.xml.namespace.QName("", "arg2"));

				if (localArg2 != null) {
					elementList
							.add(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(localArg2));
				} else {
					throw new org.apache.axis2.databinding.ADBException(
							"arg2 cannot be null!!");
				}
			}
			if (localArg3Tracker) {
				elementList.add(new javax.xml.namespace.QName("", "arg3"));

				if (localArg3 != null) {
					elementList
							.add(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(localArg3));
				} else {
					throw new org.apache.axis2.databinding.ADBException(
							"arg3 cannot be null!!");
				}
			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(
					qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static QueryBatch parse(
					javax.xml.stream.XMLStreamReader reader)
					throws java.lang.Exception {
				QueryBatch object = new QueryBatch();

				int event;
				java.lang.String nillableValue = null;
				java.lang.String prefix = "";
				java.lang.String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader
							.getAttributeValue(
									"http://www.w3.org/2001/XMLSchema-instance",
									"type") != null) {
						java.lang.String fullTypeName = reader
								.getAttributeValue(
										"http://www.w3.org/2001/XMLSchema-instance",
										"type");
						if (fullTypeName != null) {
							java.lang.String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0,
										fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							java.lang.String type = fullTypeName
									.substring(fullTypeName.indexOf(":") + 1);

							if (!"queryBatch".equals(type)) {
								// find namespace for the prefix
								java.lang.String nsUri = reader
										.getNamespaceContext().getNamespaceURI(
												nsPrefix);
								return (QueryBatch) ExtensionMapper
										.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					java.util.Vector handledAttributes = new java.util.Vector();

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("", "arg0")
									.equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if ("true".equals(nillableValue)
								|| "1".equals(nillableValue)) {
							throw new org.apache.axis2.databinding.ADBException(
									"The element: " + "arg0"
											+ "  cannot be null");
						}

						java.lang.String content = reader.getElementText();

						object.setArg0(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("", "arg1")
									.equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if ("true".equals(nillableValue)
								|| "1".equals(nillableValue)) {
							throw new org.apache.axis2.databinding.ADBException(
									"The element: " + "arg1"
											+ "  cannot be null");
						}

						java.lang.String content = reader.getElementText();

						object.setArg1(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("", "arg2")
									.equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if ("true".equals(nillableValue)
								|| "1".equals(nillableValue)) {
							throw new org.apache.axis2.databinding.ADBException(
									"The element: " + "arg2"
											+ "  cannot be null");
						}

						java.lang.String content = reader.getElementText();

						object.setArg2(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("", "arg3")
									.equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if ("true".equals(nillableValue)
								|| "1".equals(nillableValue)) {
							throw new org.apache.axis2.databinding.ADBException(
									"The element: " + "arg3"
											+ "  cannot be null");
						}

						java.lang.String content = reader.getElementText();

						object.setArg3(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException(
								"Unexpected subelement " + reader.getName());

				} catch (javax.xml.stream.XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}

	public static class QueryBatchE implements
			org.apache.axis2.databinding.ADBBean {

		public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
				"http://zcxyws.ztzx.com/", "queryBatch", "ns1");

		/**
		 * field for QueryBatch
		 */

		protected QueryBatch localQueryBatch;

		/**
		 * Auto generated getter method
		 * 
		 * @return QueryBatch
		 */
		public QueryBatch getQueryBatch() {
			return localQueryBatch;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            QueryBatch
		 */
		public void setQueryBatch(QueryBatch param) {

			this.localQueryBatch = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(
				final javax.xml.namespace.QName parentQName,
				final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(
					this, MY_QNAME);
			return factory.createOMElement(dataSource, MY_QNAME);

		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter,
				boolean serializeType)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it

			if (localQueryBatch == null) {
				throw new org.apache.axis2.databinding.ADBException(
						"queryBatch cannot be null!");
			}
			localQueryBatch.serialize(MY_QNAME, xmlWriter);

		}

		private static java.lang.String generatePrefix(
				java.lang.String namespace) {
			if (namespace.equals("http://zcxyws.ztzx.com/")) {
				return "ns1";
			}
			return org.apache.axis2.databinding.utils.BeanUtil
					.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(java.lang.String prefix,
				java.lang.String namespace, java.lang.String localPart,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(java.lang.String prefix,
				java.lang.String namespace, java.lang.String attName,
				java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(java.lang.String namespace,
				java.lang.String attName, java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(java.lang.String namespace,
				java.lang.String attName, javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			java.lang.String attributeNamespace = qname.getNamespaceURI();
			java.lang.String attributePrefix = xmlWriter
					.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			java.lang.String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix
							+ ":"
							+ org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter
							.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				}

			} else {
				xmlWriter
						.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
				java.lang.String namespaceURI = null;
				java.lang.String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite
									.append(prefix)
									.append(":")
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						} else {
							stringToWrite
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						}
					} else {
						stringToWrite
								.append(org.apache.axis2.databinding.utils.ConverterUtil
										.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private java.lang.String registerPrefix(
				javax.xml.stream.XMLStreamWriter xmlWriter,
				java.lang.String namespace)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter
						.getNamespaceContext();
				while (true) {
					java.lang.String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil
							.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(
				javax.xml.namespace.QName qName)
				throws org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it
			return localQueryBatch.getPullParser(MY_QNAME);

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static QueryBatchE parse(
					javax.xml.stream.XMLStreamReader reader)
					throws java.lang.Exception {
				QueryBatchE object = new QueryBatchE();

				int event;
				java.lang.String nillableValue = null;
				java.lang.String prefix = "";
				java.lang.String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					java.util.Vector handledAttributes = new java.util.Vector();

					while (!reader.isEndElement()) {
						if (reader.isStartElement()) {

							if (reader.isStartElement()
									&& new javax.xml.namespace.QName(
											"http://zcxyws.ztzx.com/",
											"queryBatch").equals(reader
											.getName())) {

								object.setQueryBatch(QueryBatch.Factory
										.parse(reader));

							} // End of if for expected property start element

							else {
								// A start element we are not expecting
								// indicates an invalid parameter was passed
								throw new org.apache.axis2.databinding.ADBException(
										"Unexpected subelement "
												+ reader.getName());
							}

						} else {
							reader.next();
						}
					} // end of while loop

				} catch (javax.xml.stream.XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}

	public static class QuerySingleResponseE implements
			org.apache.axis2.databinding.ADBBean {

		public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
				"http://zcxyws.ztzx.com/", "querySingleResponse", "ns1");

		/**
		 * field for QuerySingleResponse
		 */

		protected QuerySingleResponse localQuerySingleResponse;

		/**
		 * Auto generated getter method
		 * 
		 * @return QuerySingleResponse
		 */
		public QuerySingleResponse getQuerySingleResponse() {
			return localQuerySingleResponse;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            QuerySingleResponse
		 */
		public void setQuerySingleResponse(QuerySingleResponse param) {

			this.localQuerySingleResponse = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(
				final javax.xml.namespace.QName parentQName,
				final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(
					this, MY_QNAME);
			return factory.createOMElement(dataSource, MY_QNAME);

		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter,
				boolean serializeType)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it

			if (localQuerySingleResponse == null) {
				throw new org.apache.axis2.databinding.ADBException(
						"querySingleResponse cannot be null!");
			}
			localQuerySingleResponse.serialize(MY_QNAME, xmlWriter);

		}

		private static java.lang.String generatePrefix(
				java.lang.String namespace) {
			if (namespace.equals("http://zcxyws.ztzx.com/")) {
				return "ns1";
			}
			return org.apache.axis2.databinding.utils.BeanUtil
					.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(java.lang.String prefix,
				java.lang.String namespace, java.lang.String localPart,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(java.lang.String prefix,
				java.lang.String namespace, java.lang.String attName,
				java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(java.lang.String namespace,
				java.lang.String attName, java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(java.lang.String namespace,
				java.lang.String attName, javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			java.lang.String attributeNamespace = qname.getNamespaceURI();
			java.lang.String attributePrefix = xmlWriter
					.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			java.lang.String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix
							+ ":"
							+ org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter
							.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				}

			} else {
				xmlWriter
						.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
				java.lang.String namespaceURI = null;
				java.lang.String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite
									.append(prefix)
									.append(":")
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						} else {
							stringToWrite
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						}
					} else {
						stringToWrite
								.append(org.apache.axis2.databinding.utils.ConverterUtil
										.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private java.lang.String registerPrefix(
				javax.xml.stream.XMLStreamWriter xmlWriter,
				java.lang.String namespace)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter
						.getNamespaceContext();
				while (true) {
					java.lang.String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil
							.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(
				javax.xml.namespace.QName qName)
				throws org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it
			return localQuerySingleResponse.getPullParser(MY_QNAME);

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static QuerySingleResponseE parse(
					javax.xml.stream.XMLStreamReader reader)
					throws java.lang.Exception {
				QuerySingleResponseE object = new QuerySingleResponseE();

				int event;
				java.lang.String nillableValue = null;
				java.lang.String prefix = "";
				java.lang.String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					java.util.Vector handledAttributes = new java.util.Vector();

					while (!reader.isEndElement()) {
						if (reader.isStartElement()) {

							if (reader.isStartElement()
									&& new javax.xml.namespace.QName(
											"http://zcxyws.ztzx.com/",
											"querySingleResponse")
											.equals(reader.getName())) {

								object.setQuerySingleResponse(QuerySingleResponse.Factory
										.parse(reader));

							} // End of if for expected property start element

							else {
								// A start element we are not expecting
								// indicates an invalid parameter was passed
								throw new org.apache.axis2.databinding.ADBException(
										"Unexpected subelement "
												+ reader.getName());
							}

						} else {
							reader.next();
						}
					} // end of while loop

				} catch (javax.xml.stream.XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}

	public static class QueryBatchResponseE implements
			org.apache.axis2.databinding.ADBBean {

		public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
				"http://zcxyws.ztzx.com/", "queryBatchResponse", "ns1");

		/**
		 * field for QueryBatchResponse
		 */

		protected QueryBatchResponse localQueryBatchResponse;

		/**
		 * Auto generated getter method
		 * 
		 * @return QueryBatchResponse
		 */
		public QueryBatchResponse getQueryBatchResponse() {
			return localQueryBatchResponse;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            QueryBatchResponse
		 */
		public void setQueryBatchResponse(QueryBatchResponse param) {

			this.localQueryBatchResponse = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(
				final javax.xml.namespace.QName parentQName,
				final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(
					this, MY_QNAME);
			return factory.createOMElement(dataSource, MY_QNAME);

		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter,
				boolean serializeType)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it

			if (localQueryBatchResponse == null) {
				throw new org.apache.axis2.databinding.ADBException(
						"queryBatchResponse cannot be null!");
			}
			localQueryBatchResponse.serialize(MY_QNAME, xmlWriter);

		}

		private static java.lang.String generatePrefix(
				java.lang.String namespace) {
			if (namespace.equals("http://zcxyws.ztzx.com/")) {
				return "ns1";
			}
			return org.apache.axis2.databinding.utils.BeanUtil
					.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(java.lang.String prefix,
				java.lang.String namespace, java.lang.String localPart,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(java.lang.String prefix,
				java.lang.String namespace, java.lang.String attName,
				java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(java.lang.String namespace,
				java.lang.String attName, java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(java.lang.String namespace,
				java.lang.String attName, javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			java.lang.String attributeNamespace = qname.getNamespaceURI();
			java.lang.String attributePrefix = xmlWriter
					.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			java.lang.String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix
							+ ":"
							+ org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter
							.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				}

			} else {
				xmlWriter
						.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
				java.lang.String namespaceURI = null;
				java.lang.String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite
									.append(prefix)
									.append(":")
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						} else {
							stringToWrite
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						}
					} else {
						stringToWrite
								.append(org.apache.axis2.databinding.utils.ConverterUtil
										.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private java.lang.String registerPrefix(
				javax.xml.stream.XMLStreamWriter xmlWriter,
				java.lang.String namespace)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter
						.getNamespaceContext();
				while (true) {
					java.lang.String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil
							.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(
				javax.xml.namespace.QName qName)
				throws org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it
			return localQueryBatchResponse.getPullParser(MY_QNAME);

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static QueryBatchResponseE parse(
					javax.xml.stream.XMLStreamReader reader)
					throws java.lang.Exception {
				QueryBatchResponseE object = new QueryBatchResponseE();

				int event;
				java.lang.String nillableValue = null;
				java.lang.String prefix = "";
				java.lang.String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					java.util.Vector handledAttributes = new java.util.Vector();

					while (!reader.isEndElement()) {
						if (reader.isStartElement()) {

							if (reader.isStartElement()
									&& new javax.xml.namespace.QName(
											"http://zcxyws.ztzx.com/",
											"queryBatchResponse").equals(reader
											.getName())) {

								object.setQueryBatchResponse(QueryBatchResponse.Factory
										.parse(reader));

							} // End of if for expected property start element

							else {
								// A start element we are not expecting
								// indicates an invalid parameter was passed
								throw new org.apache.axis2.databinding.ADBException(
										"Unexpected subelement "
												+ reader.getName());
							}

						} else {
							reader.next();
						}
					} // end of while loop

				} catch (javax.xml.stream.XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}

	public static class ExtensionMapper {

		public static java.lang.Object getTypeObject(
				java.lang.String namespaceURI, java.lang.String typeName,
				javax.xml.stream.XMLStreamReader reader)
				throws java.lang.Exception {

			if ("http://zcxyws.ztzx.com/".equals(namespaceURI)
					&& "queryBatchResponse".equals(typeName)) {

				return QueryBatchResponse.Factory.parse(reader);

			}

			if ("http://zcxyws.ztzx.com/".equals(namespaceURI)
					&& "queryBatch".equals(typeName)) {

				return QueryBatch.Factory.parse(reader);

			}

			if ("http://zcxyws.ztzx.com/".equals(namespaceURI)
					&& "querySingle".equals(typeName)) {

				return QuerySingle.Factory.parse(reader);

			}

			if ("http://zcxyws.ztzx.com/".equals(namespaceURI)
					&& "querySingleResponse".equals(typeName)) {

				return QuerySingleResponse.Factory.parse(reader);

			}

			throw new org.apache.axis2.databinding.ADBException(
					"Unsupported type " + namespaceURI + " " + typeName);
		}

	}

	public static class QuerySingle implements
			org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * querySingle Namespace URI = http://zcxyws.ztzx.com/ Namespace Prefix
		 * = ns1
		 */

		/**
		 * field for Arg0
		 */

		protected java.lang.String localArg0;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localArg0Tracker = false;

		public boolean isArg0Specified() {
			return localArg0Tracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getArg0() {
			return localArg0;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Arg0
		 */
		public void setArg0(java.lang.String param) {
			localArg0Tracker = param != null;

			this.localArg0 = param;

		}

		/**
		 * field for Arg1
		 */

		protected java.lang.String localArg1;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localArg1Tracker = false;

		public boolean isArg1Specified() {
			return localArg1Tracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getArg1() {
			return localArg1;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Arg1
		 */
		public void setArg1(java.lang.String param) {
			localArg1Tracker = param != null;

			this.localArg1 = param;

		}

		/**
		 * field for Arg2
		 */

		protected java.lang.String localArg2;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localArg2Tracker = false;

		public boolean isArg2Specified() {
			return localArg2Tracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getArg2() {
			return localArg2;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Arg2
		 */
		public void setArg2(java.lang.String param) {
			localArg2Tracker = param != null;

			this.localArg2 = param;

		}

		/**
		 * field for Arg3
		 */

		protected java.lang.String localArg3;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localArg3Tracker = false;

		public boolean isArg3Specified() {
			return localArg3Tracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getArg3() {
			return localArg3;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Arg3
		 */
		public void setArg3(java.lang.String param) {
			localArg3Tracker = param != null;

			this.localArg3 = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(
				final javax.xml.namespace.QName parentQName,
				final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(
					this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter,
				boolean serializeType)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {

			java.lang.String prefix = null;
			java.lang.String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(),
					xmlWriter);

			if (serializeType) {

				java.lang.String namespacePrefix = registerPrefix(xmlWriter,
						"http://zcxyws.ztzx.com/");
				if ((namespacePrefix != null)
						&& (namespacePrefix.trim().length() > 0)) {
					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance",
							"type", namespacePrefix + ":querySingle", xmlWriter);
				} else {
					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance",
							"type", "querySingle", xmlWriter);
				}

			}
			if (localArg0Tracker) {
				namespace = "";
				writeStartElement(null, namespace, "arg0", xmlWriter);

				if (localArg0 == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException(
							"arg0 cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localArg0);

				}

				xmlWriter.writeEndElement();
			}
			if (localArg1Tracker) {
				namespace = "";
				writeStartElement(null, namespace, "arg1", xmlWriter);

				if (localArg1 == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException(
							"arg1 cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localArg1);

				}

				xmlWriter.writeEndElement();
			}
			if (localArg2Tracker) {
				namespace = "";
				writeStartElement(null, namespace, "arg2", xmlWriter);

				if (localArg2 == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException(
							"arg2 cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localArg2);

				}

				xmlWriter.writeEndElement();
			}
			if (localArg3Tracker) {
				namespace = "";
				writeStartElement(null, namespace, "arg3", xmlWriter);

				if (localArg3 == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException(
							"arg3 cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localArg3);

				}

				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement();

		}

		private static java.lang.String generatePrefix(
				java.lang.String namespace) {
			if (namespace.equals("http://zcxyws.ztzx.com/")) {
				return "ns1";
			}
			return org.apache.axis2.databinding.utils.BeanUtil
					.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(java.lang.String prefix,
				java.lang.String namespace, java.lang.String localPart,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(java.lang.String prefix,
				java.lang.String namespace, java.lang.String attName,
				java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(java.lang.String namespace,
				java.lang.String attName, java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(java.lang.String namespace,
				java.lang.String attName, javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			java.lang.String attributeNamespace = qname.getNamespaceURI();
			java.lang.String attributePrefix = xmlWriter
					.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			java.lang.String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix
							+ ":"
							+ org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter
							.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				}

			} else {
				xmlWriter
						.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
				java.lang.String namespaceURI = null;
				java.lang.String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite
									.append(prefix)
									.append(":")
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						} else {
							stringToWrite
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						}
					} else {
						stringToWrite
								.append(org.apache.axis2.databinding.utils.ConverterUtil
										.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private java.lang.String registerPrefix(
				javax.xml.stream.XMLStreamWriter xmlWriter,
				java.lang.String namespace)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter
						.getNamespaceContext();
				while (true) {
					java.lang.String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil
							.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(
				javax.xml.namespace.QName qName)
				throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			if (localArg0Tracker) {
				elementList.add(new javax.xml.namespace.QName("", "arg0"));

				if (localArg0 != null) {
					elementList
							.add(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(localArg0));
				} else {
					throw new org.apache.axis2.databinding.ADBException(
							"arg0 cannot be null!!");
				}
			}
			if (localArg1Tracker) {
				elementList.add(new javax.xml.namespace.QName("", "arg1"));

				if (localArg1 != null) {
					elementList
							.add(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(localArg1));
				} else {
					throw new org.apache.axis2.databinding.ADBException(
							"arg1 cannot be null!!");
				}
			}
			if (localArg2Tracker) {
				elementList.add(new javax.xml.namespace.QName("", "arg2"));

				if (localArg2 != null) {
					elementList
							.add(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(localArg2));
				} else {
					throw new org.apache.axis2.databinding.ADBException(
							"arg2 cannot be null!!");
				}
			}
			if (localArg3Tracker) {
				elementList.add(new javax.xml.namespace.QName("", "arg3"));

				if (localArg3 != null) {
					elementList
							.add(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(localArg3));
				} else {
					throw new org.apache.axis2.databinding.ADBException(
							"arg3 cannot be null!!");
				}
			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(
					qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static QuerySingle parse(
					javax.xml.stream.XMLStreamReader reader)
					throws java.lang.Exception {
				QuerySingle object = new QuerySingle();

				int event;
				java.lang.String nillableValue = null;
				java.lang.String prefix = "";
				java.lang.String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader
							.getAttributeValue(
									"http://www.w3.org/2001/XMLSchema-instance",
									"type") != null) {
						java.lang.String fullTypeName = reader
								.getAttributeValue(
										"http://www.w3.org/2001/XMLSchema-instance",
										"type");
						if (fullTypeName != null) {
							java.lang.String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0,
										fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							java.lang.String type = fullTypeName
									.substring(fullTypeName.indexOf(":") + 1);

							if (!"querySingle".equals(type)) {
								// find namespace for the prefix
								java.lang.String nsUri = reader
										.getNamespaceContext().getNamespaceURI(
												nsPrefix);
								return (QuerySingle) ExtensionMapper
										.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					java.util.Vector handledAttributes = new java.util.Vector();

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("", "arg0")
									.equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if ("true".equals(nillableValue)
								|| "1".equals(nillableValue)) {
							throw new org.apache.axis2.databinding.ADBException(
									"The element: " + "arg0"
											+ "  cannot be null");
						}

						java.lang.String content = reader.getElementText();

						object.setArg0(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("", "arg1")
									.equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if ("true".equals(nillableValue)
								|| "1".equals(nillableValue)) {
							throw new org.apache.axis2.databinding.ADBException(
									"The element: " + "arg1"
											+ "  cannot be null");
						}

						java.lang.String content = reader.getElementText();

						object.setArg1(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("", "arg2")
									.equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if ("true".equals(nillableValue)
								|| "1".equals(nillableValue)) {
							throw new org.apache.axis2.databinding.ADBException(
									"The element: " + "arg2"
											+ "  cannot be null");
						}

						java.lang.String content = reader.getElementText();

						object.setArg2(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("", "arg3")
									.equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if ("true".equals(nillableValue)
								|| "1".equals(nillableValue)) {
							throw new org.apache.axis2.databinding.ADBException(
									"The element: " + "arg3"
											+ "  cannot be null");
						}

						java.lang.String content = reader.getElementText();

						object.setArg3(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException(
								"Unexpected subelement " + reader.getName());

				} catch (javax.xml.stream.XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}

	public static class QueryBatchResponse implements
			org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * queryBatchResponse Namespace URI = http://zcxyws.ztzx.com/ Namespace
		 * Prefix = ns1
		 */

		/**
		 * field for _return
		 */

		protected java.lang.String local_return;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean local_returnTracker = false;

		public boolean is_returnSpecified() {
			return local_returnTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String get_return() {
			return local_return;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            _return
		 */
		public void set_return(java.lang.String param) {
			local_returnTracker = param != null;

			this.local_return = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(
				final javax.xml.namespace.QName parentQName,
				final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(
					this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter,
				boolean serializeType)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {

			java.lang.String prefix = null;
			java.lang.String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(),
					xmlWriter);

			if (serializeType) {

				java.lang.String namespacePrefix = registerPrefix(xmlWriter,
						"http://zcxyws.ztzx.com/");
				if ((namespacePrefix != null)
						&& (namespacePrefix.trim().length() > 0)) {
					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance",
							"type", namespacePrefix + ":queryBatchResponse",
							xmlWriter);
				} else {
					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance",
							"type", "queryBatchResponse", xmlWriter);
				}

			}
			if (local_returnTracker) {
				namespace = "";
				writeStartElement(null, namespace, "return", xmlWriter);

				if (local_return == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException(
							"return cannot be null!!");

				} else {

					xmlWriter.writeCharacters(local_return);

				}

				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement();

		}

		private static java.lang.String generatePrefix(
				java.lang.String namespace) {
			if (namespace.equals("http://zcxyws.ztzx.com/")) {
				return "ns1";
			}
			return org.apache.axis2.databinding.utils.BeanUtil
					.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(java.lang.String prefix,
				java.lang.String namespace, java.lang.String localPart,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(java.lang.String prefix,
				java.lang.String namespace, java.lang.String attName,
				java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(java.lang.String namespace,
				java.lang.String attName, java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(java.lang.String namespace,
				java.lang.String attName, javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			java.lang.String attributeNamespace = qname.getNamespaceURI();
			java.lang.String attributePrefix = xmlWriter
					.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			java.lang.String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix
							+ ":"
							+ org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter
							.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				}

			} else {
				xmlWriter
						.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
				java.lang.String namespaceURI = null;
				java.lang.String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite
									.append(prefix)
									.append(":")
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						} else {
							stringToWrite
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						}
					} else {
						stringToWrite
								.append(org.apache.axis2.databinding.utils.ConverterUtil
										.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private java.lang.String registerPrefix(
				javax.xml.stream.XMLStreamWriter xmlWriter,
				java.lang.String namespace)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter
						.getNamespaceContext();
				while (true) {
					java.lang.String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil
							.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(
				javax.xml.namespace.QName qName)
				throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			if (local_returnTracker) {
				elementList.add(new javax.xml.namespace.QName("", "return"));

				if (local_return != null) {
					elementList
							.add(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(local_return));
				} else {
					throw new org.apache.axis2.databinding.ADBException(
							"return cannot be null!!");
				}
			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(
					qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static QueryBatchResponse parse(
					javax.xml.stream.XMLStreamReader reader)
					throws java.lang.Exception {
				QueryBatchResponse object = new QueryBatchResponse();

				int event;
				java.lang.String nillableValue = null;
				java.lang.String prefix = "";
				java.lang.String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader
							.getAttributeValue(
									"http://www.w3.org/2001/XMLSchema-instance",
									"type") != null) {
						java.lang.String fullTypeName = reader
								.getAttributeValue(
										"http://www.w3.org/2001/XMLSchema-instance",
										"type");
						if (fullTypeName != null) {
							java.lang.String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0,
										fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							java.lang.String type = fullTypeName
									.substring(fullTypeName.indexOf(":") + 1);

							if (!"queryBatchResponse".equals(type)) {
								// find namespace for the prefix
								java.lang.String nsUri = reader
										.getNamespaceContext().getNamespaceURI(
												nsPrefix);
								return (QueryBatchResponse) ExtensionMapper
										.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					java.util.Vector handledAttributes = new java.util.Vector();

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("", "return")
									.equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if ("true".equals(nillableValue)
								|| "1".equals(nillableValue)) {
							throw new org.apache.axis2.databinding.ADBException(
									"The element: " + "return"
											+ "  cannot be null");
						}

						java.lang.String content = reader.getElementText();

						object.set_return(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException(
								"Unexpected subelement " + reader.getName());

				} catch (javax.xml.stream.XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}

	public static class QuerySingleE implements
			org.apache.axis2.databinding.ADBBean {

		public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
				"http://zcxyws.ztzx.com/", "querySingle", "ns1");

		/**
		 * field for QuerySingle
		 */

		protected QuerySingle localQuerySingle;

		/**
		 * Auto generated getter method
		 * 
		 * @return QuerySingle
		 */
		public QuerySingle getQuerySingle() {
			return localQuerySingle;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            QuerySingle
		 */
		public void setQuerySingle(QuerySingle param) {

			this.localQuerySingle = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(
				final javax.xml.namespace.QName parentQName,
				final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(
					this, MY_QNAME);
			return factory.createOMElement(dataSource, MY_QNAME);

		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter,
				boolean serializeType)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it

			if (localQuerySingle == null) {
				throw new org.apache.axis2.databinding.ADBException(
						"querySingle cannot be null!");
			}
			localQuerySingle.serialize(MY_QNAME, xmlWriter);

		}

		private static java.lang.String generatePrefix(
				java.lang.String namespace) {
			if (namespace.equals("http://zcxyws.ztzx.com/")) {
				return "ns1";
			}
			return org.apache.axis2.databinding.utils.BeanUtil
					.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(java.lang.String prefix,
				java.lang.String namespace, java.lang.String localPart,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(java.lang.String prefix,
				java.lang.String namespace, java.lang.String attName,
				java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(java.lang.String namespace,
				java.lang.String attName, java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(java.lang.String namespace,
				java.lang.String attName, javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			java.lang.String attributeNamespace = qname.getNamespaceURI();
			java.lang.String attributePrefix = xmlWriter
					.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			java.lang.String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix
							+ ":"
							+ org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter
							.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				}

			} else {
				xmlWriter
						.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
				java.lang.String namespaceURI = null;
				java.lang.String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite
									.append(prefix)
									.append(":")
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						} else {
							stringToWrite
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						}
					} else {
						stringToWrite
								.append(org.apache.axis2.databinding.utils.ConverterUtil
										.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private java.lang.String registerPrefix(
				javax.xml.stream.XMLStreamWriter xmlWriter,
				java.lang.String namespace)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter
						.getNamespaceContext();
				while (true) {
					java.lang.String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil
							.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(
				javax.xml.namespace.QName qName)
				throws org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it
			return localQuerySingle.getPullParser(MY_QNAME);

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static QuerySingleE parse(
					javax.xml.stream.XMLStreamReader reader)
					throws java.lang.Exception {
				QuerySingleE object = new QuerySingleE();

				int event;
				java.lang.String nillableValue = null;
				java.lang.String prefix = "";
				java.lang.String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					java.util.Vector handledAttributes = new java.util.Vector();

					while (!reader.isEndElement()) {
						if (reader.isStartElement()) {

							if (reader.isStartElement()
									&& new javax.xml.namespace.QName(
											"http://zcxyws.ztzx.com/",
											"querySingle").equals(reader
											.getName())) {

								object.setQuerySingle(QuerySingle.Factory
										.parse(reader));

							} // End of if for expected property start element

							else {
								// A start element we are not expecting
								// indicates an invalid parameter was passed
								throw new org.apache.axis2.databinding.ADBException(
										"Unexpected subelement "
												+ reader.getName());
							}

						} else {
							reader.next();
						}
					} // end of while loop

				} catch (javax.xml.stream.XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}

	public static class QuerySingleResponse implements
			org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * querySingleResponse Namespace URI = http://zcxyws.ztzx.com/ Namespace
		 * Prefix = ns1
		 */

		/**
		 * field for _return
		 */

		protected java.lang.String local_return;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean local_returnTracker = false;

		public boolean is_returnSpecified() {
			return local_returnTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String get_return() {
			return local_return;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            _return
		 */
		public void set_return(java.lang.String param) {
			local_returnTracker = param != null;

			this.local_return = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(
				final javax.xml.namespace.QName parentQName,
				final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(
					this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName,
				javax.xml.stream.XMLStreamWriter xmlWriter,
				boolean serializeType)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {

			java.lang.String prefix = null;
			java.lang.String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(),
					xmlWriter);

			if (serializeType) {

				java.lang.String namespacePrefix = registerPrefix(xmlWriter,
						"http://zcxyws.ztzx.com/");
				if ((namespacePrefix != null)
						&& (namespacePrefix.trim().length() > 0)) {
					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance",
							"type", namespacePrefix + ":querySingleResponse",
							xmlWriter);
				} else {
					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance",
							"type", "querySingleResponse", xmlWriter);
				}

			}
			if (local_returnTracker) {
				namespace = "";
				writeStartElement(null, namespace, "return", xmlWriter);

				if (local_return == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException(
							"return cannot be null!!");

				} else {

					xmlWriter.writeCharacters(local_return);

				}

				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement();

		}

		private static java.lang.String generatePrefix(
				java.lang.String namespace) {
			if (namespace.equals("http://zcxyws.ztzx.com/")) {
				return "ns1";
			}
			return org.apache.axis2.databinding.utils.BeanUtil
					.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(java.lang.String prefix,
				java.lang.String namespace, java.lang.String localPart,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(java.lang.String prefix,
				java.lang.String namespace, java.lang.String attName,
				java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(java.lang.String namespace,
				java.lang.String attName, java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(java.lang.String namespace,
				java.lang.String attName, javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			java.lang.String attributeNamespace = qname.getNamespaceURI();
			java.lang.String attributePrefix = xmlWriter
					.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			java.lang.String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix
							+ ":"
							+ org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter
							.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				}

			} else {
				xmlWriter
						.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
				java.lang.String namespaceURI = null;
				java.lang.String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite
									.append(prefix)
									.append(":")
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						} else {
							stringToWrite
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						}
					} else {
						stringToWrite
								.append(org.apache.axis2.databinding.utils.ConverterUtil
										.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private java.lang.String registerPrefix(
				javax.xml.stream.XMLStreamWriter xmlWriter,
				java.lang.String namespace)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter
						.getNamespaceContext();
				while (true) {
					java.lang.String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil
							.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(
				javax.xml.namespace.QName qName)
				throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			if (local_returnTracker) {
				elementList.add(new javax.xml.namespace.QName("", "return"));

				if (local_return != null) {
					elementList
							.add(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(local_return));
				} else {
					throw new org.apache.axis2.databinding.ADBException(
							"return cannot be null!!");
				}
			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(
					qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static QuerySingleResponse parse(
					javax.xml.stream.XMLStreamReader reader)
					throws java.lang.Exception {
				QuerySingleResponse object = new QuerySingleResponse();

				int event;
				java.lang.String nillableValue = null;
				java.lang.String prefix = "";
				java.lang.String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader
							.getAttributeValue(
									"http://www.w3.org/2001/XMLSchema-instance",
									"type") != null) {
						java.lang.String fullTypeName = reader
								.getAttributeValue(
										"http://www.w3.org/2001/XMLSchema-instance",
										"type");
						if (fullTypeName != null) {
							java.lang.String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0,
										fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							java.lang.String type = fullTypeName
									.substring(fullTypeName.indexOf(":") + 1);

							if (!"querySingleResponse".equals(type)) {
								// find namespace for the prefix
								java.lang.String nsUri = reader
										.getNamespaceContext().getNamespaceURI(
												nsPrefix);
								return (QuerySingleResponse) ExtensionMapper
										.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					java.util.Vector handledAttributes = new java.util.Vector();

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("", "return")
									.equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if ("true".equals(nillableValue)
								|| "1".equals(nillableValue)) {
							throw new org.apache.axis2.databinding.ADBException(
									"The element: " + "return"
											+ "  cannot be null");
						}

						java.lang.String content = reader.getElementText();

						object.set_return(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException(
								"Unexpected subelement " + reader.getName());

				} catch (javax.xml.stream.XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}

	private org.apache.axiom.om.OMElement toOM(
			com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param
					.getOMElement(
							com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchE.MY_QNAME,
							org.apache.axiom.om.OMAbstractFactory
									.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(
			com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchResponseE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param
					.getOMElement(
							com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchResponseE.MY_QNAME,
							org.apache.axiom.om.OMAbstractFactory
									.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(
			com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param
					.getOMElement(
							com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleE.MY_QNAME,
							org.apache.axiom.om.OMAbstractFactory
									.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(
			com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleResponseE param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param
					.getOMElement(
							com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleResponseE.MY_QNAME,
							org.apache.axiom.om.OMAbstractFactory
									.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
			org.apache.axiom.soap.SOAPFactory factory,
			com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchE param,
			boolean optimizeContent, javax.xml.namespace.QName methodQName)
			throws org.apache.axis2.AxisFault {

		try {

			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
					.getDefaultEnvelope();
			emptyEnvelope
					.getBody()
					.addChild(
							param.getOMElement(
									com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchE.MY_QNAME,
									factory));
			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	/* methods to provide back word compatibility */

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
			org.apache.axiom.soap.SOAPFactory factory,
			com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleE param,
			boolean optimizeContent, javax.xml.namespace.QName methodQName)
			throws org.apache.axis2.AxisFault {

		try {

			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
					.getDefaultEnvelope();
			emptyEnvelope
					.getBody()
					.addChild(
							param.getOMElement(
									com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleE.MY_QNAME,
									factory));
			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	/* methods to provide back word compatibility */

	/**
	 * get the default envelope
	 */
	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
			org.apache.axiom.soap.SOAPFactory factory) {
		return factory.getDefaultEnvelope();
	}

	private java.lang.Object fromOM(org.apache.axiom.om.OMElement param,
			java.lang.Class type, java.util.Map extraNamespaces)
			throws org.apache.axis2.AxisFault {

		try {

			if (com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchE.class
					.equals(type)) {

				return com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchE.Factory
						.parse(param.getXMLStreamReaderWithoutCaching());

			}

			if (com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchResponseE.class
					.equals(type)) {

				return com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchResponseE.Factory
						.parse(param.getXMLStreamReaderWithoutCaching());

			}

			if (com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleE.class
					.equals(type)) {

				return com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleE.Factory
						.parse(param.getXMLStreamReaderWithoutCaching());

			}

			if (com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleResponseE.class
					.equals(type)) {

				return com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleResponseE.Factory
						.parse(param.getXMLStreamReaderWithoutCaching());

			}

		} catch (java.lang.Exception e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
		return null;
	}

}
