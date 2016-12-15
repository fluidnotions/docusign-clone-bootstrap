
package com.docusign.esignature.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "userId",
    "uri",
    "apiPassword",
    "errorDetails",
    "activationAccessCode",
    "email",
    "enableConnectForUser",
    "firstName",
    "forgottenPasswordInfo",
    "groupList",
    "lastName",
    "middleName",
    "password",
    "sendActivationOnInvalidLogin",
    "suffixName",
    "title",
    "userName",
    "userSettings"
})
public class NewUser {

    @JsonProperty("userId")
    private String userId;
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("apiPassword")
    private String apiPassword;
    @JsonProperty("errorDetails")
    private ErrorDetails errorDetails;
    @JsonProperty("activationAccessCode")
    private String activationAccessCode;
    @JsonProperty("email")
    private String email;
    @JsonProperty("enableConnectForUser")
    private String enableConnectForUser;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("forgottenPasswordInfo")
    private ForgottenPasswordInfo forgottenPasswordInfo;
    @JsonProperty("groupList")
    private List<GroupList> groupList = new ArrayList<GroupList>();
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("middleName")
    private String middleName;
    @JsonProperty("password")
    private String password;
    @JsonProperty("sendActivationOnInvalidLogin")
    private String sendActivationOnInvalidLogin;
    @JsonProperty("suffixName")
    private String suffixName;
    @JsonProperty("title")
    private String title;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("userSettings")
    private List<UserSetting> userSettings = new ArrayList<UserSetting>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The userId
     */
    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The userId
     */
    @JsonProperty("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public NewUser withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    /**
     * 
     * @return
     *     The uri
     */
    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    /**
     * 
     * @param uri
     *     The uri
     */
    @JsonProperty("uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    public NewUser withUri(String uri) {
        this.uri = uri;
        return this;
    }

    /**
     * 
     * @return
     *     The apiPassword
     */
    @JsonProperty("apiPassword")
    public String getApiPassword() {
        return apiPassword;
    }

    /**
     * 
     * @param apiPassword
     *     The apiPassword
     */
    @JsonProperty("apiPassword")
    public void setApiPassword(String apiPassword) {
        this.apiPassword = apiPassword;
    }

    public NewUser withApiPassword(String apiPassword) {
        this.apiPassword = apiPassword;
        return this;
    }

    /**
     * 
     * @return
     *     The errorDetails
     */
    @JsonProperty("errorDetails")
    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    /**
     * 
     * @param errorDetails
     *     The errorDetails
     */
    @JsonProperty("errorDetails")
    public void setErrorDetails(ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }

    public NewUser withErrorDetails(ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
        return this;
    }

    /**
     * 
     * @return
     *     The activationAccessCode
     */
    @JsonProperty("activationAccessCode")
    public String getActivationAccessCode() {
        return activationAccessCode;
    }

    /**
     * 
     * @param activationAccessCode
     *     The activationAccessCode
     */
    @JsonProperty("activationAccessCode")
    public void setActivationAccessCode(String activationAccessCode) {
        this.activationAccessCode = activationAccessCode;
    }

    public NewUser withActivationAccessCode(String activationAccessCode) {
        this.activationAccessCode = activationAccessCode;
        return this;
    }

    /**
     * 
     * @return
     *     The email
     */
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email
     *     The email
     */
    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    public NewUser withEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * 
     * @return
     *     The enableConnectForUser
     */
    @JsonProperty("enableConnectForUser")
    public String getEnableConnectForUser() {
        return enableConnectForUser;
    }

    /**
     * 
     * @param enableConnectForUser
     *     The enableConnectForUser
     */
    @JsonProperty("enableConnectForUser")
    public void setEnableConnectForUser(String enableConnectForUser) {
        this.enableConnectForUser = enableConnectForUser;
    }

    public NewUser withEnableConnectForUser(String enableConnectForUser) {
        this.enableConnectForUser = enableConnectForUser;
        return this;
    }

    /**
     * 
     * @return
     *     The firstName
     */
    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    /**
     * 
     * @param firstName
     *     The firstName
     */
    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public NewUser withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * 
     * @return
     *     The forgottenPasswordInfo
     */
    @JsonProperty("forgottenPasswordInfo")
    public ForgottenPasswordInfo getForgottenPasswordInfo() {
        return forgottenPasswordInfo;
    }

    /**
     * 
     * @param forgottenPasswordInfo
     *     The forgottenPasswordInfo
     */
    @JsonProperty("forgottenPasswordInfo")
    public void setForgottenPasswordInfo(ForgottenPasswordInfo forgottenPasswordInfo) {
        this.forgottenPasswordInfo = forgottenPasswordInfo;
    }

    public NewUser withForgottenPasswordInfo(ForgottenPasswordInfo forgottenPasswordInfo) {
        this.forgottenPasswordInfo = forgottenPasswordInfo;
        return this;
    }

    /**
     * 
     * @return
     *     The groupList
     */
    @JsonProperty("groupList")
    public List<GroupList> getGroupList() {
        return groupList;
    }

    /**
     * 
     * @param groupList
     *     The groupList
     */
    @JsonProperty("groupList")
    public void setGroupList(List<GroupList> groupList) {
        this.groupList = groupList;
    }

    public NewUser withGroupList(List<GroupList> groupList) {
        this.groupList = groupList;
        return this;
    }

    /**
     * 
     * @return
     *     The lastName
     */
    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    /**
     * 
     * @param lastName
     *     The lastName
     */
    @JsonProperty("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public NewUser withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * 
     * @return
     *     The middleName
     */
    @JsonProperty("middleName")
    public String getMiddleName() {
        return middleName;
    }

    /**
     * 
     * @param middleName
     *     The middleName
     */
    @JsonProperty("middleName")
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public NewUser withMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    /**
     * 
     * @return
     *     The password
     */
    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @param password
     *     The password
     */
    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    public NewUser withPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * 
     * @return
     *     The sendActivationOnInvalidLogin
     */
    @JsonProperty("sendActivationOnInvalidLogin")
    public String getSendActivationOnInvalidLogin() {
        return sendActivationOnInvalidLogin;
    }

    /**
     * 
     * @param sendActivationOnInvalidLogin
     *     The sendActivationOnInvalidLogin
     */
    @JsonProperty("sendActivationOnInvalidLogin")
    public void setSendActivationOnInvalidLogin(String sendActivationOnInvalidLogin) {
        this.sendActivationOnInvalidLogin = sendActivationOnInvalidLogin;
    }

    public NewUser withSendActivationOnInvalidLogin(String sendActivationOnInvalidLogin) {
        this.sendActivationOnInvalidLogin = sendActivationOnInvalidLogin;
        return this;
    }

    /**
     * 
     * @return
     *     The suffixName
     */
    @JsonProperty("suffixName")
    public String getSuffixName() {
        return suffixName;
    }

    /**
     * 
     * @param suffixName
     *     The suffixName
     */
    @JsonProperty("suffixName")
    public void setSuffixName(String suffixName) {
        this.suffixName = suffixName;
    }

    public NewUser withSuffixName(String suffixName) {
        this.suffixName = suffixName;
        return this;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public NewUser withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 
     * @return
     *     The userName
     */
    @JsonProperty("userName")
    public String getUserName() {
        return userName;
    }

    /**
     * 
     * @param userName
     *     The userName
     */
    @JsonProperty("userName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public NewUser withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * 
     * @return
     *     The userSettings
     */
    @JsonProperty("userSettings")
    public List<UserSetting> getUserSettings() {
        return userSettings;
    }

    /**
     * 
     * @param userSettings
     *     The userSettings
     */
    @JsonProperty("userSettings")
    public void setUserSettings(List<UserSetting> userSettings) {
        this.userSettings = userSettings;
    }

    public NewUser withUserSettings(List<UserSetting> userSettings) {
        this.userSettings = userSettings;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public NewUser withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
