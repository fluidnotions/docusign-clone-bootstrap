package com.groupfio.docusign;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.DelegatorFactory;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class OTUtil {

	private static final String module = null;
	private static Gson gson = new GsonBuilder().create();
	
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

	public static String validateIsLoginUrlKey(String tenantIdOrLoginUrlKey) {
		Delegator delegator = DelegatorFactory.getDelegator("default");
		GenericValue tenant = null;
		try {
			tenant = delegator.findByPrimaryKey("Tenant", UtilMisc.toMap("tenantId", tenantIdOrLoginUrlKey));
		} catch (GenericEntityException e) {
		}
		return tenant != null ? tenant.getString("loginUrlKey") : tenantIdOrLoginUrlKey;
	}
	
	public static String validateIsTenantKey(String loginUrlKeyOrTenantKey) {
		Delegator delegator = DelegatorFactory.getDelegator("default");
		String tenantId = null;
		try {
			List<GenericValue> tenants = delegator.findByAnd("Tenant", UtilMisc.toMap("loginUrlKey", loginUrlKeyOrTenantKey));
			if(tenants.size()>0){
				assert(tenants.size()==1);
				GenericValue tenant = tenants.get(0);
				tenantId = tenant.getString("tenantId");
						
			}else{
				tenantId = loginUrlKeyOrTenantKey;
			}
		} catch (GenericEntityException e) {
		}
		return tenantId;
	}
	
	public static String getTenantdynamicDocDownloadSuffix(String tenantKey) {
		Delegator delegator = DelegatorFactory.getDelegator("default");
		String userLoginId = "", password = "", key = "";
		boolean isTenantId = true;
		try {
			String tenantId = validateIsTenantKey(tenantKey);		
			 GenericValue tenant = delegator.findByPrimaryKey("DynamicDocumentDownloadTenantCredentials", UtilMisc.toMap("tenantKey", tenantId));
			 String loginUrlKey = null;
			if(tenant == null || UtilValidate.isEmpty(tenant.getAllFields())){
				loginUrlKey = validateIsLoginUrlKey(tenantKey);
				tenant = delegator.findByPrimaryKey("DynamicDocumentDownloadTenantCredentials", UtilMisc.toMap("tenantKey", tenantId));
				isTenantId = false;
			}
			userLoginId = "USERNAME="+tenant.getString("userLoginId");
			password = "PASSWORD="+tenant.getString("password");
			key = isTenantId? "tenantId="+tenantId:"loginUrlKey="+loginUrlKey;
		} catch (GenericEntityException e) {
			e.printStackTrace();
			
		}
		return "&"+userLoginId+"&"+password+"&"+key;
	}
	
	public static String getHostBaseUrl(String tenantKey) {
		Delegator delegator = DelegatorFactory.getDelegator("default");
		String hostBaseUrl = "http://localhost:7611";
		try {
			String tenantId = validateIsTenantKey(tenantKey);		
			 GenericValue tenant = delegator.findByPrimaryKey("DynamicDocumentDownloadTenantCredentials", UtilMisc.toMap("tenantKey", tenantId));
			 
			 if(tenant == null || UtilValidate.isEmpty(tenant.getAllFields())){
					String loginUrlKey = validateIsLoginUrlKey(tenantKey);
					tenant = delegator.findByPrimaryKey("DynamicDocumentDownloadTenantCredentials", UtilMisc.toMap("tenantKey", loginUrlKey));
					if(tenant==null)Debug.logInfo("notion found with: "+loginUrlKey, module);
					 else Debug.logInfo("found with: "+loginUrlKey+", tenant: "+tenant.toString(), module);
				}
			 if(tenant!=null){
				 hostBaseUrl = tenant.getString("hostBaseUrl");
				 Debug.logInfo("hostBaseUrl: "+hostBaseUrl, module);
			 }
			 
		} catch (GenericEntityException e) {
			e.printStackTrace();
		
		}
		return hostBaseUrl;
	}

	public static Delegator getTenantDelegator(String tenantKey) {

		Delegator delegator = DelegatorFactory.getDelegator("default");
		Delegator tenantDelegator = null;

		if (tenantKey != null) {
            try {

                List<GenericValue> gvs = delegator.findByAnd("Tenant", "loginUrlKey", tenantKey.trim());
                if (gvs.size() > 0) {
                    String tenantId = gvs.get(0).getString("tenantId");
                    tenantDelegator = DelegatorFactory.getDelegator("default#" + tenantId);
                } else {
                    tenantDelegator = DelegatorFactory.getDelegator("default#" + tenantKey);
                }

            } catch (GenericEntityException e) {
                Debug.logError(e, OTUtil.class.getName());
            } catch (Exception e) {
                Debug.logError(e, OTUtil.class.getName());
            }
        }
        return tenantDelegator != null ? tenantDelegator : delegator;
	}
	
	
	
	


}
