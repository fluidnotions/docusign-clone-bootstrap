package com.fluidnotions.docusign.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * A user, known as the authenticating user in this circumstance, that wants to authenticate for other
 * users in the account must have the following DocuSign userSettings enabled:
 * apiAccountWideAccess
 * allowSendOnBehalfOf
 * 
 * Note: If you are setting user permissions through the DocuSign web console, these correspond to
 * the Account-Wide Rights and Send On Behalf Of Rights (API) settings.
 * 
 */
public class AuthenticatingUser {
	
	private String username;
	private String password;
	private String integratorKey;
	private String afterSendRedirectUrlBase;
	private String tenantKey;
	private boolean enabled;
	
	public AuthenticatingUser(String username, String password,
			String integratorKey, String afterSendRedirectUrlBase) {
		super();
		this.username = username;
		this.password = password;
		this.integratorKey = integratorKey;
		this.afterSendRedirectUrlBase = afterSendRedirectUrlBase;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the integratorKey
	 */
	public String getIntegratorKey() {
		return integratorKey;
	}
	/**
	 * @param integratorKey the integratorKey to set
	 */
	public void setIntegratorKey(String integratorKey) {
		this.integratorKey = integratorKey;
	}
	
	public String getAfterSendRedirectUrlBase() {
		return afterSendRedirectUrlBase;
	}
	public void setAfterSendRedirectUrlBase(String afterSendRedirectUrlBase) {
		this.afterSendRedirectUrlBase = afterSendRedirectUrlBase;
	}
	/**
	 * @return the tenantKey
	 */
	public String getTenantKey() {
		return tenantKey;
	}
	/**
	 * @param tenantKey the tenantKey to set
	 */
	public void setTenantKey(String tenantKey) {
		this.tenantKey = tenantKey;
	}
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	private static ObjectMapper om = new ObjectMapper();

	public String toJsonAuthenticationHeader(String sendOnBehalfOf) throws JsonProcessingException{
	    Map<String, String> authHeaderMap = new HashMap<String, String>();
	    authHeaderMap.put("Username", this.username);
	    authHeaderMap.put("Password", this.password);
	    authHeaderMap.put("IntegratorKey", this.integratorKey);
	    if(sendOnBehalfOf!=null)authHeaderMap.put("SendOnBehalfOf", sendOnBehalfOf);
	    return om.writeValueAsString(authHeaderMap);
	}
	

}
