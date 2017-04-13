package com.fluidnotions.docusign.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "tenantKey", "dynamicDocUrl", "title", "emailSubject", "emailblurb", "RecipientModels", "docuSignUserEmail", "afterSendRedirectUrl",
		"fistRecipient", "secondRecipient" })
public class AutoPositionedRecipientModelRequest {

	@JsonProperty("tenantKey")
	private String tenantKey;
	@JsonProperty("title")
	private String title;
	@JsonProperty("emailSubject")
	private String emailSubject;
	@JsonProperty("emailblurb")
	private String emailblurb;
	@JsonProperty("firstRecipient")
	private String firstRecipient;
	@JsonProperty("secondRecipient")
	private String secondRecipient;
	@JsonProperty("docuSignUserEmail")
	private String docuSignUserEmail;
	@JsonProperty("dynamicDocUrl")
	private String dynamicDocUrl;
	@JsonProperty("mime")
	private String mime;
	@JsonProperty("afterSendRedirectUrl")
	private String afterSendRedirectUrl;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	@JsonIgnore
	private List<RecipientModel> recipientModels = new ArrayList<RecipientModel>();

	public AutoPositionedRecipientModelRequest() {
	}

	@JsonProperty("tenantKey")
	public String getTenantKey() {
		return tenantKey;
	}

	@JsonProperty("tenantKey")
	public void setTenantKey(String tenantKey) {
		this.tenantKey = tenantKey;
	}

	public String getFirstRecipient() {
		return firstRecipient;
	}

	public void setFirstRecipient(String firstRecipient) {
		this.firstRecipient = firstRecipient;
	}

	public List<RecipientModel> getRecipientModels() {
		return recipientModels;
	}

	public void setRecipientModels(List<RecipientModel> RecipientModels) {
		this.recipientModels = RecipientModels;
	}

	public String getSecondRecipient() {
		return secondRecipient;
	}

	public void setSecondRecipient(String secondRecipient) {
		this.secondRecipient = secondRecipient;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@JsonAnySetter
	public String getEmailSubject() {
		return emailSubject;
	}

	@JsonAnySetter
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	@JsonAnySetter
	public String getEmailblurb() {
		return emailblurb;
	}

	@JsonAnySetter
	public void setEmailblurb(String emailblurb) {
		this.emailblurb = emailblurb;
	}

	@JsonProperty("docuSignUserEmail")
	public String getDocuSignUserEmail() {
		return docuSignUserEmail;
	}

	@JsonProperty("docuSignUserEmail")
	public void setDocuSignUserEmail(String docuSignUserEmail) {
		this.docuSignUserEmail = docuSignUserEmail;
	}

	@JsonProperty("dynamicDocUrl")
	public String getDynamicDocUrl() {
		return dynamicDocUrl;
	}

	@JsonProperty("dynamicDocUrl")
	public void setDynamicDocUrl(String dynamicDocUrl) {
		this.dynamicDocUrl = dynamicDocUrl;
	}

	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("mime")
	public String getMime() {
		return mime;
	}

	@JsonProperty("mime")
	public void setMime(String mime) {
		this.mime = mime;
	}

	/**
	 * 
	 * @return The afterSendRedirectUrl
	 */
	@JsonProperty("afterSendRedirectUrl")
	public String getAfterSendRedirectUrl() {
		return afterSendRedirectUrl;
	}

	/**
	 * 
	 * @param afterSendRedirectUrl
	 *            The afterSendRedirectUrl
	 */
	@JsonProperty("afterSendRedirectUrl")
	public void setAfterSendRedirectUrl(String afterSendRedirectUrl) {
		this.afterSendRedirectUrl = afterSendRedirectUrl;
	}

}
