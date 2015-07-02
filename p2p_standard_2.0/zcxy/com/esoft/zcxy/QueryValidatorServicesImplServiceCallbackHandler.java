
/**
 * QueryValidatorServicesImplServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.esoft.zcxy;

    /**
     *  QueryValidatorServicesImplServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class QueryValidatorServicesImplServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public QueryValidatorServicesImplServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public QueryValidatorServicesImplServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for queryBatch method
            * override this method for handling normal response from queryBatch operation
            */
           public void receiveResultqueryBatch(
                    com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QueryBatchResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryBatch operation
           */
            public void receiveErrorqueryBatch(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for querySingle method
            * override this method for handling normal response from querySingle operation
            */
           public void receiveResultquerySingle(
                    com.esoft.zcxy.QueryValidatorServicesImplServiceStub.QuerySingleResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from querySingle operation
           */
            public void receiveErrorquerySingle(java.lang.Exception e) {
            }
                


    }
    