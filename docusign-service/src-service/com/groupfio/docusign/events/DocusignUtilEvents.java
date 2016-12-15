package com.groupfio.docusign.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.model.ModelEntity;
import org.ofbiz.entity.util.EntityUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.groupfio.docusign.OTUtil;


public class DocusignUtilEvents {
    
    private static final String UID_COOKIE = "uid-cookie";
    private static final String module = null;
    private static Gson gson = new GsonBuilder().create();
    
    private static boolean isThereCookieByName(HttpServletRequest request, String cookieName) {

	Cookie[] cookies = request.getCookies();
	if (cookies != null) {
	    for (int i = 0; i < cookies.length; i++) {
		if (cookies[i].getName().equals(cookieName)) {
		    return true;
		}

	    }
	}
	return false;
    }
    
    
//    public static String createUidCookie(HttpServletRequest request, HttpServletResponse response)
//	    throws RuntimeException {
//	
//	if(!isThereCookieByName(request, UID_COOKIE)){
//
//	String tenantId = request.getParameter("tenantId");
//	if(tenantId == null) tenantId = (String) request.getAttribute("tenantId");
//	String loginUrlKey = request.getParameter("loginUrlKey");
//	if(loginUrlKey == null) loginUrlKey = (String) request.getAttribute("loginUrlKey");
//	
//	if (UtilValidate.isEmpty(tenantId)) {
//	    tenantId = OTUtil.validateIsTenantKey(loginUrlKey);
//	}
//	if (UtilValidate.isEmpty(loginUrlKey)) {
//	    loginUrlKey = OTUtil.validateIsLoginUrlKey(tenantId);
//	}
//
//	if (UtilValidate.isEmpty(loginUrlKey) && UtilValidate.isEmpty(tenantId)) {
//	    throw new RuntimeException((UtilValidate.isEmpty(loginUrlKey) ? "loginUrlKey is null. " : "")
//		    + (UtilValidate.isEmpty(tenantId) ? "tenantId is null" : ""));
//	}
//
//	HttpSession session = request.getSession();
//	GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
//	String userLoginId = userLogin.getString("userLoginId");
//
//	Map<String, String> uidmap = new HashMap<String, String>();
//	uidmap.put("tenantId", tenantId);
//	uidmap.put("loginUrlKey", loginUrlKey);
//	uidmap.put("userLoginId", userLoginId);
//	
//	Map<String, String> info = retrieveUserInfo(tenantId, userLogin);
//	for(Map.Entry<String, String> entry: info.entrySet()){
//	    uidmap.put(entry.getKey(), entry.getValue());
//	}
//	String jsonUid = gson.toJson(uidmap);
//
//	// b64 encode
//	jsonUid = Base64.encodeBase64String(jsonUid.getBytes());
//
//	request.setAttribute("externalLoginKey", session.getAttribute("externalLoginKey"));
//	request.setAttribute("loginUrlKey", loginUrlKey);
//
//	Cookie userCookie = new LongLivedCookie(UID_COOKIE, jsonUid);
//	response.addCookie(userCookie);
//	
//	} 
//	
//	return "sucess";
//
//    }


    public static String lookupUidInfo(HttpServletRequest request, HttpServletResponse response)
	    throws RuntimeException {
	
	
	String loginUrlKey = request.getParameter("loginUrlKey");
	if(loginUrlKey == null) loginUrlKey = (String) request.getAttribute("loginUrlKey");
	
	String  tenantId = OTUtil.validateIsTenantKey(loginUrlKey);
	
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
	for(Map.Entry<String, String> entry: info.entrySet()){
	    uidmap.put(entry.getKey(), entry.getValue());
	}
	String jsonStringResult = gson.toJson(uidmap);

	return OTUtil.doJSONResponse(response, jsonStringResult);
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
		primaryEmail =  getPartyEmail( tenantId,  partyId);
		
	    } catch (GenericEntityException e) {
		Debug.logError(e, "Error getting person/partyGroup info, ignoring...", module);
	    }
	}
	String name = ((first != null) ? first + " " : "") + ((second != null) ? second : "");
	
	return UtilMisc.toMap("name", name, "email", primaryEmail);
    }

    private static String getPartyEmail(String tenantId, String partyId) throws GenericEntityException {
	String email = null;
	Delegator delegator = OTUtil.getTenantDelegator(tenantId);
	List<GenericValue> pcmps = delegator.findByAndCache("PartyContactMechPurpose",
		UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PRIMARY_EMAIL"),
		UtilMisc.toList("createdStamp DESC"));
	GenericValue partyContactMechPurpose = EntityUtil.getFirst(pcmps);
	String contactMechId = partyContactMechPurpose.getString("contactMechId");
	if (UtilValidate.isNotEmpty(contactMechId)) {       
            GenericValue contactMech = delegator.findByPrimaryKeyCache("ContactMech", UtilMisc.toMap("contactMechId", contactMechId));
		if (UtilValidate.isNotEmpty(contactMech)) {
		    email = contactMech.getString("infoString");

		}
	    }

	return email;
    }

}
