package com.fluidnotions.events;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.model.ModelEntity;
import org.ofbiz.entity.util.EntityUtil;

import com.fluidnotions.helper.FluidUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class UidEvents {

    private static final String module = null;
    private static Gson gson = new GsonBuilder().create();

    public static String lookupUidInfo(HttpServletRequest request, HttpServletResponse response)
	    throws RuntimeException {

	String loginUrlKey = request.getParameter("loginUrlKey");
	if (loginUrlKey == null)
	    loginUrlKey = (String) request.getAttribute("loginUrlKey");

	String tenantId = FluidUtil.validateIsTenantKey(loginUrlKey);

	if (UtilValidate.isEmpty(loginUrlKey) && UtilValidate.isEmpty(tenantId)) {
	    throw new RuntimeException((UtilValidate.isEmpty(loginUrlKey) ? "loginUrlKey is null. " : "")
		    + (UtilValidate.isEmpty(tenantId) ? "tenantId is null" : ""));
	}

	HttpSession session = request.getSession();
	GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
	String userLoginId = userLogin.getString("userLoginId");

	Map<String, String> uidmap = new HashMap<String, String>();
	uidmap.put("tenantId", tenantId);
	uidmap.put("loginUrlKey", loginUrlKey);
	uidmap.put("userLoginId", userLoginId);

	Map<String, String> info = retrieveUserInfo(tenantId, userLogin);
	for (Map.Entry<String, String> entry : info.entrySet()) {
	    uidmap.put(entry.getKey(), entry.getValue());
	}
	String jsonStringResult = gson.toJson(uidmap);

	return doJSONResponse(response, jsonStringResult);
    }

    public static Map<String, String> retrieveUserInfo(String tenantId, GenericValue userLogin) {
	String first = null, second = null, primaryEmail = null;
	ModelEntity modelUserLogin = userLogin.getModelEntity();
	if (modelUserLogin.isField("partyId")) {
	    // if partyId is a field, then we should have these relations
	    // defined
	    try {
		GenericValue person = userLogin.getRelatedOne("Person");
		first = person.getString("firstName");
		second = person.getString("lastName");

		String partyId = userLogin.getString("partyId");
		primaryEmail = getPartyEmail(tenantId, partyId);

	    } catch (GenericEntityException e) {
		Debug.logError(e, "Error getting person/partyGroup info, ignoring...", module);
	    }
	}
	String name = ((first != null) ? first + " " : "") + ((second != null) ? second : "");

	return UtilMisc.toMap("name", name, "email", primaryEmail);
    }

    private static String getPartyEmail(String tenantId, String partyId) throws GenericEntityException {
	String email = null;
	Delegator delegator = FluidUtil.getTenantDelegator(tenantId);
	List<GenericValue> pcmps = delegator.findByAndCache("PartyContactMechPurpose",
		UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PRIMARY_EMAIL"),
		UtilMisc.toList("createdStamp DESC"));
	GenericValue partyContactMechPurpose = EntityUtil.getFirst(pcmps);
	String contactMechId = partyContactMechPurpose.getString("contactMechId");
	if (UtilValidate.isNotEmpty(contactMechId)) {
	    GenericValue contactMech = delegator.findByPrimaryKeyCache("ContactMech",
		    UtilMisc.toMap("contactMechId", contactMechId));
	    if (UtilValidate.isNotEmpty(contactMech)) {
		email = contactMech.getString("infoString");

	    }
	}

	return email;
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
