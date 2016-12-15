package com.fluidnotions.docusign.models;

import org.ofbiz.base.util.Debug;

public class DocuSignUser {
	
	private static final String module = DocuSignUser.class.getName();
	private String userLoginId;
	private String docuSignUserId;
	private String name;
	private String email;
	private boolean enabled;
	
	

	public DocuSignUser(String userLoginId, String docuSignUserId,
			String email, String name, boolean enabled) {
		super();
		this.userLoginId = userLoginId;
		this.docuSignUserId = docuSignUserId;
		this.email = email;
		this.enabled = enabled;
		this.name = name;
		Debug.logInfo("DocuSignUser: "+this.toString(), module);
	}
	/**
	 * @return the userLoginId
	 */
	public String getUserLoginId() {
		return userLoginId;
	}
	/**
	 * @param userLoginId the userLoginId to set
	 */
	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}
	/**
	 * @return the docuSignUserId
	 */
	public String getDocuSignUserId() {
		return docuSignUserId;
	}
	/**
	 * @param docuSignUserId the docuSignUserId to set
	 */
	public void setDocuSignUserId(String docuSignUserId) {
		this.docuSignUserId = docuSignUserId;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
	    return "DocuSignUser [userLoginId=" + userLoginId + ", name=" + name + ", email=" + email + ", enabled="
		    + enabled + "]";
	}
	
	
	
	
	
}
