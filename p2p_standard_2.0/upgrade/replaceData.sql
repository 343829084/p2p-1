-- ----------------------------
-- Records of apply_enterprise_loan
-- ----------------------------
update apply_enterprise_loan set status='waiting_verify' where status='等待审核';
update apply_enterprise_loan set status='verified' where status='已审核';
-- ----------------------------
-- Records of invest
-- ----------------------------
update invest set status='wait_affirm' where status='等待确认';
update invest set status='bid_success' where status='投标成功' or status='债权转让';
update invest set status='cancel' where status='撤标' or status='流标';
update invest set status='repaying' where status='还款中';
update invest set status='overdue' where status='逾期';
update invest set status='complete' where status='完成';
update invest set status='bad_debt' where status='坏账';
update invest set invest_money=money;
-- ----------------------------
-- Records of loan
-- ---------------------------
update loan set status='waiting_verify' where status='等待审核';
update loan set status='dqgs' where status='贷前公示';
update loan set status='verify_fail' where status='审核未通过';
update loan set status='raising' where status='筹款中';
update loan set status='recheck' where status='等待复核';
update loan set status='cancel' where status='撤标' or status='流标';
update loan set status='repaying' where status='还款中';
update loan set status='overdue' where status='逾期';
update loan set status='complete' where status='完成';
update loan set status='bad_debt' where status='坏账';
update loan set loan_money=money;
update loan set interest_begin_time=give_money_time;
update loan set contract_type='quartet_contract';
update loan set fee_on_repay=0;
update loan set cardinal_number=50;
update loan set investor_fee_rate=0;
update loan set business_type='个人借款' where type='一口价';
update loan set business_type='企业借款' where type='企业借款';
update loan set type='loan_type_2' where repay_type='等额本息';
update loan set type='loan_type_3' where repay_type='按月付息到期还本' or repay_type='先付利息后还本金';
update loan set type='loan_type_4' where repay_type='到期还本付息';
-- ----------------------------
-- Records of user_bill
-- ---------------------------
update user_bill set detail=concat(type_info, detail);
update user_bill set type_info='admin_operation' where type_info like '%管理员%';
update user_bill set type_info='normal_repay' where type_info like '%还款%';
update user_bill set type_info='apply_loan' where type_info like '%发起借款%';
update user_bill set type_info='give_money_to_borrower' where type_info like '%借款成功%' or type_info like '%借款到账%' or type_info like '%投资成功%';
update user_bill set type_info='invest_success' where type_info='投资';
update user_bill set type_info='cancel_loan' where type_info like '%流标，解冻保证金%';
update user_bill set type_info='cancel_invest' where type_info like '%流标，解冻投资金额%';
update user_bill set type_info='refuse_apply_loan' where type_info ='借款审核未通过，解冻保证金。';
update user_bill set type_info='apply_withdraw' where type_info ='申请提现，冻结提现金额';
update user_bill set type_info='withdraw_success' where type_info ='提现申请通过，取出冻结金额';
update user_bill set type_info='refuse_apply_withdraw' where type_info ='提现申请被拒绝，解冻';





