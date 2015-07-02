
/**
 * ServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.esoft.ipspay.trusteeship.service.impl;

    /**
     *  ServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ServiceCallbackNewHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ServiceCallbackNewHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ServiceCallbackNewHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for queryForAccBalance method
            * override this method for handling normal response from queryForAccBalance operation
            */
           public void receiveResultqueryForAccBalance(
        		   com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew.QueryForAccBalanceResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryForAccBalance operation
           */
            public void receiveErrorqueryForAccBalance(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for coDpTrade method
            * override this method for handling normal response from coDpTrade operation
            */
           public void receiveResultcoDpTrade(
        		   com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew.CoDpTradeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from coDpTrade operation
           */
            public void receiveErrorcoDpTrade(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for guaranteeUnfreeze method
            * override this method for handling normal response from guaranteeUnfreeze operation
            */
           public void receiveResultguaranteeUnfreeze(
        		   com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew.GuaranteeUnfreezeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from guaranteeUnfreeze operation
           */
            public void receiveErrorguaranteeUnfreeze(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryAllTrade method
            * override this method for handling normal response from queryAllTrade operation
            */
           public void receiveResultqueryAllTrade(
        		   com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew.QueryAllTradeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryAllTrade operation
           */
            public void receiveErrorqueryAllTrade(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for transfer method
            * override this method for handling normal response from transfer operation
            */
           public void receiveResulttransfer(
        		   com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew.TransferResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from transfer operation
           */
            public void receiveErrortransfer(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for auditTender method
            * override this method for handling normal response from auditTender operation
            */
           public void receiveResultauditTender(
        		   com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew.AuditTenderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from auditTender operation
           */
            public void receiveErrorauditTender(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getBankList method
            * override this method for handling normal response from getBankList operation
            */
           public void receiveResultgetBankList(
        		   com.esoft.ipspay.trusteeship.service.impl.ServiceStubNew.GetBankListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getBankList operation
           */
            public void receiveErrorgetBankList(java.lang.Exception e) {
            }
                


    }
    