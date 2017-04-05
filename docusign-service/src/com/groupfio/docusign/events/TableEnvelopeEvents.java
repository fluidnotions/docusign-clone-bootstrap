package com.groupfio.docusign.events;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.jdbc.SQLProcessor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.groupfio.docusign.DocuSignEnvelopeStatusInformation;
import com.groupfio.docusign.DocuSignEnvelopeStatusInformationResponse;
import com.groupfio.docusign.OTUtil;

public class TableEnvelopeEvents {

    private static final String module = TableEnvelopeEvents.class.getName();
    private static Gson gson = new GsonBuilder().create();

    public static String getEnvelopeStatusData(HttpServletRequest request, HttpServletResponse response) {
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	Debug.logInfo("should be using tenant delegator, current delegator name: " + delegator.getDelegatorName(),
		module);
	String tenantKey = request.getParameter("tenantKey");
	// FIXME for testing only
	if (tenantKey == null) {
	    tenantKey = "111111";
	}

	if (UtilValidate.isEmpty(delegator.getDelegatorTenantId())) {
	    delegator = OTUtil.getTenantDelegator(tenantKey);
	}

	Integer offset = new Integer(request.getParameter("offset"));
	Integer fetchSize = new Integer(request.getParameter("limit"));

	Debug.logInfo("pageIndex: " + offset + ",fetchSize: " + fetchSize, module);

	String sql = "SELECT SQL_CALC_FOUND_ROWS * FROM " + "DOCU_SIGN_ENVELOPE ORDER BY LAST_UPDATED_STAMP DESC "
		+ " LIMIT " + offset + ", " + fetchSize;

	Debug.logInfo(sql, module);
	SQLProcessor sqlProcessor = new SQLProcessor(delegator.getGroupHelperInfo("org.ofbiz"));
	List<DocuSignEnvelopeStatusInformation> ess = new ArrayList<DocuSignEnvelopeStatusInformation>();
	DocuSignEnvelopeStatusInformationResponse esr = new DocuSignEnvelopeStatusInformationResponse();
	String jsonArray  = null;
	ResultSet rs = null, rs2 = null, rs3 = null;
	try {
	    rs = sqlProcessor.executeQuery(sql);
	    while (rs.next()) {
	    	String envelopeId = rs.getString("ENVELOPE_ID");
	    	String  senderUserLoginId = rs.getString("SENDER_USER_LOGIN_ID");
	    	String  docuSignUserEmail= rs.getString("DOCU_SIGN_USER_EMAIL");
	    	String  senderDocuSignUserId= rs.getString("SENDER_DOCU_SIGN_USER_ID");
	    	String  envelopeEmailSubject= rs.getString("EMAIL_SUBJECT");
	    	String  envelopeStatus= rs.getString("STATUS");     
	    	String  statusChangedDateTime= rs.getString("STATUS_CHANGED_DATE_TIME");
	    	DocuSignEnvelopeStatusInformation es = new DocuSignEnvelopeStatusInformation();
	    	es.setEnvelopeId(envelopeId);
	    	es.setEnvelopeEmailSubject(envelopeEmailSubject);
	    	es.setSenderUserLoginId(senderUserLoginId);
	    	es.setDocuSignUserEmail(docuSignUserEmail);
	    	es.setSenderDocuSignUserId(senderDocuSignUserId);
	    	es.setEnvelopeStatus(envelopeStatus);
	    	es.setStatusChangedDateTime(statusChangedDateTime);
	    	
	    	String sql2 = "SELECT * FROM " + "DOCU_SIGN_ENVELOPE_SIGNER_STATUS WHERE ENVELOPE_ID = '"+envelopeId+"'";
	    	rs2 = sqlProcessor.executeQuery(sql2);
	    	boolean noRoutingFirstFilled = false;
	    	while (rs2.next()) {
	    		String routingOrder = rs2.getString("ROUTING_ORDER").trim();
	       		//in the case of custom mode if no routing is provides then both are 1
	    		if(routingOrder.equals("1") && !noRoutingFirstFilled){
	    			String  firstRecipientName= rs2.getString("NAME");
	    			String  firstRecipientEmail= rs2.getString("EMAIL");
		    		String  firstRecipientRoutingOrder= rs2.getString("ROUTING_ORDER");	    		
		    		String  firstRecipientStatus= rs2.getString("STATUS");
		    		String  firstRecipientSignedDateTime= rs2.getString("SIGNED_DATE_TIME");
		    		String  firstRecipientDeliveredDateTime= rs2.getString("DELIVERED_DATE_TIME");
		    		es.setFirstRecipientName(firstRecipientName);
		    		es.setFirstRecipientEmail(firstRecipientEmail);
		    		es.setFirstRecipientRoutingOrder(firstRecipientRoutingOrder);
		    		es.setFirstRecipientStatus(firstRecipientStatus);
		    		es.setFirstRecipientSignedDateTime(firstRecipientSignedDateTime);
		    		es.setFirstRecipientDeliveredDateTime(firstRecipientDeliveredDateTime);
		    		noRoutingFirstFilled = true;
	    		}else {
	    			String  secondRecipientName= rs2.getString("NAME");
	    			String  secondRecipientEmail= rs2.getString("EMAIL");
		    		String  secondRecipientRoutingOrder= rs2.getString("ROUTING_ORDER");	    		
		    		String  secondRecipientStatus= rs2.getString("STATUS");
		    		String  secondRecipientSignedDateTime= rs2.getString("SIGNED_DATE_TIME");
		    		String  secondRecipientDeliveredDateTime= rs2.getString("DELIVERED_DATE_TIME");
		    		es.setSecondRecipientName(secondRecipientName);
		    		es.setSecondRecipientEmail(secondRecipientEmail);
		    		es.setSecondRecipientRoutingOrder(secondRecipientRoutingOrder);
		    		es.setSecondRecipientStatus(secondRecipientStatus);
		    		es.setSecondRecipientSignedDateTime(secondRecipientSignedDateTime);
		    		es.setSecondRecipientDeliveredDateTime(secondRecipientDeliveredDateTime);
	    		}
	    	}
	    	ess.add(es);
	    }
	    String sql3 = "SELECT COUNT(*) FROM DOCU_SIGN_ENVELOPE;";
	    rs3 =sqlProcessor.executeQuery(sql3);			
	    Integer  total = null;
		while(rs3.next()){
			total = rs3.getInt("COUNT(*)");	
		}
		
		esr.setRows(ess);
		esr.setTotal(total);
	    jsonArray = gson.toJson(esr);
	    
		rs.close();
		rs2.close();
		rs3.close();

	} catch (GenericDataSourceException e) {
	    e.printStackTrace();
	} catch (GenericEntityException e) {
	    e.printStackTrace();
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}

	Debug.logInfo("result: " + jsonArray, module);
	return doJSONResponse(response, jsonArray);

    }

   

    public static String doJSONResponse(HttpServletResponse response, String jsonString) {
	String result = "success";

	response.setContentType("application/x-json");
	try {
	    response.setContentLength(jsonString.getBytes("UTF-8").length);
	} catch (UnsupportedEncodingException e) {
	    Debug.logWarning(
		    "Could not get the UTF-8 json string due to UnsupportedEncodingException: " + e.getMessage(), "");
	    response.setContentLength(jsonString.length());
	}

	Writer out;
	try {
	    out = response.getWriter();
	    out.write(jsonString);
	    out.flush();
	} catch (IOException e) {
	    Debug.logError(e, "Failed to get response writer", "");
	    result = "error";
	}
	return result;
    }

}
