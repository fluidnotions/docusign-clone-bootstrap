
package com.fluidnotions.models;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class AutocompleteAllFormBuilderSpec {

    @SerializedName("inputFieldList")
    @Expose
    private List<InputField> inputFieldList = new ArrayList<InputField>();
    @SerializedName("formId")
    @Expose
    private String formId;
    @SerializedName("formButtonId")
    @Expose
    private String formButtonId;
    @SerializedName("formButtonName")
    @Expose
    private String formButtonName;
    @SerializedName("formButtonClasses")
    @Expose
    private String formButtonClasses;
    @SerializedName("lookupUrl")
    @Expose
    private String lookupUrl;
    @SerializedName("targetSelector")
    @Expose
    private String targetSelector;
    @SerializedName("templatesFolderPath")
    @Expose
    private String templatesFolderPath;
    @SerializedName("otherFormGroupTypesHtml")
    @Expose
    private String otherFormGroupTypesHtml;
    @SerializedName("entityName")
    @Expose
    private String entityName;
    @SerializedName("fieldName")
    @Expose
    private String fieldName;
    @SerializedName("searchTerm")
    @Expose
    private String searchTerm;
    @SerializedName("whereConditionList")
    @Expose
    private List<WhereCondition> whereConditionList = new ArrayList<WhereCondition>();

    /**
     * 
     * @return
     *     The inputField
     */
    public List<InputField> getInputField() {
        return inputFieldList;
    }

    /**
     * 
     * @param inputField
     *     The inputField
     */
    public void setInputField(List<InputField> inputField) {
        this.inputFieldList = inputField;
    }

    public AutocompleteAllFormBuilderSpec withInputField(List<InputField> inputField) {
        this.inputFieldList = inputField;
        return this;
    }

    /**
     * 
     * @return
     *     The formId
     */
    public String getFormId() {
        return formId;
    }

    /**
     * 
     * @param formId
     *     The formId
     */
    public void setFormId(String formId) {
        this.formId = formId;
    }

    public AutocompleteAllFormBuilderSpec withFormId(String formId) {
        this.formId = formId;
        return this;
    }

    /**
     * 
     * @return
     *     The formButtonId
     */
    public String getFormButtonId() {
        return formButtonId;
    }

    /**
     * 
     * @param formButtonId
     *     The formButtonId
     */
    public void setFormButtonId(String formButtonId) {
        this.formButtonId = formButtonId;
    }

    public AutocompleteAllFormBuilderSpec withFormButtonId(String formButtonId) {
        this.formButtonId = formButtonId;
        return this;
    }

    /**
     * 
     * @return
     *     The formButtonName
     */
    public String getFormButtonName() {
        return formButtonName;
    }

    /**
     * 
     * @param formButtonName
     *     The formButtonName
     */
    public void setFormButtonName(String formButtonName) {
        this.formButtonName = formButtonName;
    }

    public AutocompleteAllFormBuilderSpec withFormButtonName(String formButtonName) {
        this.formButtonName = formButtonName;
        return this;
    }

    /**
     * 
     * @return
     *     The formButtonClasses
     */
    public String getFormButtonClasses() {
        return formButtonClasses;
    }

    /**
     * 
     * @param formButtonClasses
     *     The formButtonClasses
     */
    public void setFormButtonClasses(String formButtonClasses) {
        this.formButtonClasses = formButtonClasses;
    }

    public AutocompleteAllFormBuilderSpec withFormButtonClasses(String formButtonClasses) {
        this.formButtonClasses = formButtonClasses;
        return this;
    }

    /**
     * 
     * @return
     *     The lookupUrl
     */
    public String getLookupUrl() {
        return lookupUrl;
    }

    /**
     * 
     * @param lookupUrl
     *     The lookupUrl
     */
    public void setLookupUrl(String lookupUrl) {
        this.lookupUrl = lookupUrl;
    }

    public AutocompleteAllFormBuilderSpec withLookupUrl(String lookupUrl) {
        this.lookupUrl = lookupUrl;
        return this;
    }

    /**
     * 
     * @return
     *     The targetSelector
     */
    public String getTargetSelector() {
        return targetSelector;
    }

    /**
     * 
     * @param targetSelector
     *     The targetSelector
     */
    public void setTargetSelector(String targetSelector) {
        this.targetSelector = targetSelector;
    }

    public AutocompleteAllFormBuilderSpec withTargetSelector(String targetSelector) {
        this.targetSelector = targetSelector;
        return this;
    }

    /**
     * 
     * @return
     *     The templatesFolderPath
     */
    public String getTemplatesFolderPath() {
        return templatesFolderPath;
    }

    /**
     * 
     * @param templatesFolderPath
     *     The templatesFolderPath
     */
    public void setTemplatesFolderPath(String templatesFolderPath) {
        this.templatesFolderPath = templatesFolderPath;
    }

    public AutocompleteAllFormBuilderSpec withTemplatesFolderPath(String templatesFolderPath) {
        this.templatesFolderPath = templatesFolderPath;
        return this;
    }

    /**
     * 
     * @return
     *     The otherFormGroupTypesHtml
     */
    public String getOtherFormGroupTypesHtml() {
        return otherFormGroupTypesHtml;
    }

    /**
     * 
     * @param otherFormGroupTypesHtml
     *     The otherFormGroupTypesHtml
     */
    public void setOtherFormGroupTypesHtml(String otherFormGroupTypesHtml) {
        this.otherFormGroupTypesHtml = otherFormGroupTypesHtml;
    }

    public AutocompleteAllFormBuilderSpec withOtherFormGroupTypesHtml(String otherFormGroupTypesHtml) {
        this.otherFormGroupTypesHtml = otherFormGroupTypesHtml;
        return this;
    }

    /**
     * 
     * @return
     *     The entityName
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * 
     * @param entityName
     *     The entityName
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public AutocompleteAllFormBuilderSpec withEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    /**
     * 
     * @return
     *     The fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 
     * @param fieldName
     *     The fieldName
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public AutocompleteAllFormBuilderSpec withFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    /**
     * 
     * @return
     *     The searchTerm
     */
    public String getSearchTerm() {
        return searchTerm;
    }

    /**
     * 
     * @param searchTerm
     *     The searchTerm
     */
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public AutocompleteAllFormBuilderSpec withSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
        return this;
    }

    /**
     * 
     * @return
     *     The whereCondition
     */
    public List<WhereCondition> getWhereConditionList() {
        return whereConditionList;
    }

    /**
     * 
     * @param whereCondition
     *     The whereCondition
     */
    public void setWhereConditionList(List<WhereCondition> whereConditionList) {
        this.whereConditionList = whereConditionList;
    }

    public AutocompleteAllFormBuilderSpec withWhereCondition(List<WhereCondition> whereConditionList) {
        this.whereConditionList = whereConditionList;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
