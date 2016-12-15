package com.fluidnotions.docusign.models;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "docUrl",
    "docName",
    "emailSubject",
    "emailBody",
    "docuSignUserEmail",
    "tenantKey",
    "isDynamic",
    "afterSendRedirectUrl",
    "mode",
    "recipientSignUserEmail",
    "recipientSignUserName",
    "docuSignUserId",
    "mime"
})
public class  SetupDocumentRequest{

    @JsonProperty("docUrl")
    private String docUrl;
    @JsonProperty("docName")
    private String docName;
    @JsonProperty("emailSubject")
    private String emailSubject;
    @JsonProperty("emailBody")
    private String emailBody;
    @JsonProperty("docuSignUserEmail")
    private String docuSignUserEmail;
    @JsonProperty("tenantKey")
    private String tenantKey;
    @JsonProperty("isDynamic")
    private String isDynamic;
    @JsonProperty("afterSendRedirectUrl")
    private String afterSendRedirectUrl;
    @JsonProperty("mode")
    private String mode;
    @JsonProperty("recipientSignUserEmail")
    private String recipientSignUserEmail;
    @JsonProperty("recipientSignUserName")
    private String recipientSignUserName;
    @JsonProperty("docuSignUserId")
    private String docuSignUserId;
    @JsonProperty("mime")
    private String mime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public SetupDocumentRequest() {
    }

    /**
     * 
     * @param docName
     * @param emailBody
     * @param afterSendRedirectUrl
     * @param docUrl
     * @param docuSignUserId
     * @param recipientSignUserEmail
     * @param isDynamic
     * @param docuSignUserEmail
     * @param tenantKey
     * @param recipientSignUserName
     * @param mode
     */
//    public SetupDocumentRequest(String docUrl, String docName, String emailBody, String docuSignUserEmail, String tenantKey, String isDynamic, String afterSendRedirectUrl, String mode, String recipientSignUserEmail, String recipientSignUserName, String docuSignUserId, String mime) {
//        this.docUrl = docUrl;
//        this.docName = docName;
//        this.emailBody = emailBody;
//        this.docuSignUserEmail = docuSignUserEmail;
//        this.tenantKey = tenantKey;
//        this.isDynamic = isDynamic;
//        this.afterSendRedirectUrl = afterSendRedirectUrl;
//        this.mode = mode;
//        this.recipientSignUserEmail = recipientSignUserEmail;
//        this.recipientSignUserName = recipientSignUserName;
//        this.docuSignUserId = docuSignUserId;
//        this.mime = mime;
//    }

    /**
     * 
     * @return
     *     The docUrl
     */
    @JsonProperty("docUrl")
    public String getDocUrl() {
        return docUrl;
    }

    /**
     * 
     * @param docUrl
     *     The docUrl
     */
    @JsonProperty("docUrl")
    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    /**
     * 
     * @return
     *     The docName
     */
    @JsonProperty("docName")
    public String getDocName() {
        return docName;
    }

    /**
     * 
     * @param docName
     *     The docName
     */
    @JsonProperty("docName")
    public void setDocName(String docName) {
        this.docName = docName;
    }

    /**
     * 
     * @return
     *     The emailBody
     */
    @JsonProperty("emailBody")
    public String getEmailBody() {
        return emailBody;
    }

    /**
     * 
     * @param emailBody
     *     The emailBody
     */
    @JsonProperty("emailBody")
    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    /**
     * 
     * @return
     *     The docuSignUserEmail
     */
    @JsonProperty("docuSignUserEmail")
    public String getDocuSignUserEmail() {
        return docuSignUserEmail;
    }

    /**
     * 
     * @param docuSignUserEmail
     *     The docuSignUserEmail
     */
    @JsonProperty("docuSignUserEmail")
    public void setDocuSignUserEmail(String docuSignUserEmail) {
        this.docuSignUserEmail = docuSignUserEmail;
    }

    /**
     * 
     * @return
     *     The tenantKey
     */
    @JsonProperty("tenantKey")
    public String getTenantKey() {
        return tenantKey;
    }

    /**
     * 
     * @param tenantKey
     *     The tenantKey
     */
    @JsonProperty("tenantKey")
    public void setTenantKey(String tenantKey) {
        this.tenantKey = tenantKey;
    }

    /**
     * 
     * @return
     *     The isDynamic
     */
    @JsonProperty("isDynamic")
    public String getIsDynamic() {
        return isDynamic;
    }

    /**
     * 
     * @param isDynamic
     *     The isDynamic
     */
    @JsonProperty("isDynamic")
    public void setIsDynamic(String isDynamic) {
        this.isDynamic = isDynamic;
    }

    /**
     * 
     * @return
     *     The afterSendRedirectUrl
     */
    @JsonProperty("afterSendRedirectUrl")
    public String getAfterSendRedirectUrl() {
        return afterSendRedirectUrl;
    }

    /**
     * 
     * @param afterSendRedirectUrl
     *     The afterSendRedirectUrl
     */
    @JsonProperty("afterSendRedirectUrl")
    public void setAfterSendRedirectUrl(String afterSendRedirectUrl) {
        this.afterSendRedirectUrl = afterSendRedirectUrl;
    }

    /**
     * 
     * @return
     *     The mode
     */
    @JsonProperty("mode")
    public String getMode() {
        return mode;
    }

    /**
     * 
     * @param mode
     *     The mode
     */
    @JsonProperty("mode")
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * 
     * @return
     *     The recipientSignUserEmail
     */
    @JsonProperty("recipientSignUserEmail")
    public String getRecipientSignUserEmail() {
        return recipientSignUserEmail;
    }

    /**
     * 
     * @param recipientSignUserEmail
     *     The recipientSignUserEmail
     */
    @JsonProperty("recipientSignUserEmail")
    public void setRecipientSignUserEmail(String recipientSignUserEmail) {
        this.recipientSignUserEmail = recipientSignUserEmail;
    }

    /**
     * 
     * @return
     *     The recipientSignUserName
     */
    @JsonProperty("recipientSignUserName")
    public String getRecipientSignUserName() {
        return recipientSignUserName;
    }

    /**
     * 
     * @param recipientSignUserName
     *     The recipientSignUserName
     */
    @JsonProperty("recipientSignUserName")
    public void setRecipientSignUserName(String recipientSignUserName) {
        this.recipientSignUserName = recipientSignUserName;
    }

    /**
     * 
     * @return
     *     The docuSignUserId
     */
    @JsonProperty("docuSignUserId")
    public String getDocuSignUserId() {
        return docuSignUserId;
    }

    /**
     * 
     * @param docuSignUserId
     *     The docuSignUserId
     */
    @JsonProperty("docuSignUserId")
    public void setDocuSignUserId(String docuSignUserId) {
        this.docuSignUserId = docuSignUserId;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonProperty("mime")
	public String getMime() {
		return mime;
	}

    @JsonProperty("mime")
	public void setMime(String mime) {
		this.mime = mime;
	}

    @JsonProperty("emailSubject")
	public String getEmailSubject() {
		return emailSubject;
	}

    @JsonProperty("emailSubject")
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

}
