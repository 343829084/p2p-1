package com.esoft.jdp2p.invest.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.esoft.archer.node.model.Node;
import com.esoft.archer.system.controller.DictUtil;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.jdp2p.invest.InvestConstants.InvestStatus;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;
import com.google.common.base.Strings;
import com.lowagie.text.pdf.BaseFont;

@Component
@Scope(ScopeType.VIEW)
public class ContractHome {

	@Resource
	private HibernateTemplate ht;

	@Resource
	private DictUtil dictUtil;

	private String contractContent;

	public void contractPdfDownload(String fileName) {
		String body = contractContent.replace("&nbsp;", "&#160;");
		body = "<html><head></head><body style=\"font-family:'SimSun';\">"
				+ body + "</body></html>";
		StringReader contentReader = new StringReader(body);
		InputSource source = new InputSource(contentReader);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = factory.newDocumentBuilder();
			org.w3c.dom.Document xhtmlContent = documentBuilder.parse(source);
			ITextRenderer renderer = new ITextRenderer();
			ITextFontResolver fontResolver = renderer.getFontResolver();
			fontResolver.addFont(FacesUtil.getRealPath("SIMSUN.TTC"),
					BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			renderer.setDocument(xhtmlContent, "");
			renderer.layout();
			FacesUtil.getHttpServletResponse().setHeader(
					"Content-Disposition",
					"attachment; filename=\""
							+ new String((fileName + ".pdf").getBytes("utf-8"),
									"iso8859-1") + "\"");
			FacesUtil.getHttpServletResponse().setContentType(
					"application/pdf;charset=UTF-8");
			ServletOutputStream sos = FacesUtil.getHttpServletResponse()
					.getOutputStream();
			renderer.createPDF(sos);
			FacesUtil.getCurrentInstance().responseComplete();
			sos.close();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (com.lowagie.text.DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 替换借款合同正文中的信息
	 * 
	 * @param loanId
	 * @return
	 */
	public String dealLoanContractContent(String loanId) {
		// FIXME： #{investTransferList}", "债权转让列表
		Loan loan = ht.get(Loan.class, loanId);
		// #{investList} 投资列表
		Element table = Jsoup
				.parseBodyFragment("<table border='1' style='margin: 0px auto; border-collapse: collapse; border: 1px solid rgb(0, 0, 0); width: 70%; '><tbody><tr class='firstRow'><td style='text-align:center;'>用户名</td><td style='text-align:center;'>投标来源</td><td style='text-align:center;'>借出金额</td><td style='text-align:center;'>借款期限</td><td style='text-align:center;'>应收利息</td></tr></tbody></table>");
		Element tbody = table.getElementsByTag("tbody").first();

		List<Invest> is = ht.find(
				"from Invest i where i.loan.id=? and i.status!=?",
				new String[] { loan.getId(), InvestStatus.CANCEL });
		for (Invest invest : is) {
			tbody.append("<tr><td style='text-align:center;'>"
					+ invest.getUser().getUsername()
					+ "</td><td style='text-align:center;'>"
					+ (invest.getIsAutoInvest() ? "自动投标" : "手动投标")
					+ "</td><td style='text-align:center;'>"
					+ invest.getRepayRoadmap().getRepayCorpus()
					+ "</td><td style='text-align:center;'>"
					+ loan.getDeadline()
					* loan.getType().getRepayTimePeriod()
					+ "("
					+ dictUtil.getValue("repay_unit", loan.getType()
							.getRepayTimeUnit()) + ")"
					+ "</td><td style='text-align:center;'>"
					+ invest.getRepayRoadmap().getRepayInterest()
					+ "</td></tr>");
		}
		tbody.append("<tr><td style='text-align:center;'>总计</td><td></td><td style='text-align:center;'>"
				+ loan.getRepayRoadmap().getRepayCorpus()
				+ "</td><td></td><td style='text-align:center;'>"
				+ loan.getRepayRoadmap().getRepayInterest() + "</td></tr>");

		Node node = ht.get(Node.class, loan.getContractType());
		if (contractContent == null) {
			contractContent = node.getNodeBody().getBody();
			contractContent = contractContent
					.replace("#{loanId}", Strings.nullToEmpty(loan.getId()))
					.replace("#{actualMoney}", loan.getMoney().toString())
					.replace("#{investList}", table.outerHtml())
					.replace(
							"#{interest}",
							loan.getRepayRoadmap().getRepayInterest()
									.toString())
					.replace("#{loanerRealname}",
							Strings.nullToEmpty(loan.getUser().getRealname()))
					.replace("#{loanerIdCard}",
							Strings.nullToEmpty(loan.getUser().getIdCard()))
					.replace("#{loanerUsername}", loan.getUser().getUsername())
					.replace("#{guaranteeCompanyName}",
							Strings.nullToEmpty(loan.getGuaranteeCompanyName()))
					.replace(
							"#{giveMoneyDate}",
							Strings.nullToEmpty(DateUtil.DateToString(
									loan.getGiveMoneyTime(),
									DateStyle.YYYY_MM_DD_CN)))
					.replace("#{rate}", loan.getRatePercent().toString())
					.replace("#{deadline}", loan.getDeadline().toString())
					.replace(
							"#{interestBeginTime}",
							Strings.nullToEmpty(DateUtil.DateToString(
									loan.getInterestBeginTime(),
									DateStyle.YYYY_MM_DD_CN)))
					.replace(
							"#{interestEndTime}",
							// FIXME:需要根据借款类型，来计算借款到期日
							Strings.nullToEmpty(DateUtil.DateToString(
									DateUtil.addMonth(
											loan.getInterestBeginTime(),
											loan.getDeadline()),
									DateStyle.YYYY_MM_DD_CN)))
					.replace("#{repayAllMoney}",
							loan.getRepayRoadmap().getRepayMoney().toString())
					// FIXME:需要根据借款类型，来显示还款日
					.replace(
							"#{repayDay}",
							String.valueOf(DateUtil.getDay(loan
									.getInterestBeginTime())))
					.replace("#{investTransferList}", "债权转让列表");
		}
		return contractContent;
	}
}
