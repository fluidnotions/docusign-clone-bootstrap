
package com.docusign.esignature.json;

import java.util.HashMap;
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
    "forgottenPasswordAnswer1",
    "forgottenPasswordAnswer2",
    "forgottenPasswordAnswer3",
    "forgottenPasswordAnswer4",
    "forgottenPasswordQuestion1",
    "forgottenPasswordQuestion2",
    "forgottenPasswordQuestion3",
    "forgottenPasswordQuestion4"
})
public class ForgottenPasswordInfo {

    @JsonProperty("forgottenPasswordAnswer1")
    private String forgottenPasswordAnswer1;
    @JsonProperty("forgottenPasswordAnswer2")
    private String forgottenPasswordAnswer2;
    @JsonProperty("forgottenPasswordAnswer3")
    private String forgottenPasswordAnswer3;
    @JsonProperty("forgottenPasswordAnswer4")
    private String forgottenPasswordAnswer4;
    @JsonProperty("forgottenPasswordQuestion1")
    private String forgottenPasswordQuestion1;
    @JsonProperty("forgottenPasswordQuestion2")
    private String forgottenPasswordQuestion2;
    @JsonProperty("forgottenPasswordQuestion3")
    private String forgottenPasswordQuestion3;
    @JsonProperty("forgottenPasswordQuestion4")
    private String forgottenPasswordQuestion4;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The forgottenPasswordAnswer1
     */
    @JsonProperty("forgottenPasswordAnswer1")
    public String getForgottenPasswordAnswer1() {
        return forgottenPasswordAnswer1;
    }

    /**
     * 
     * @param forgottenPasswordAnswer1
     *     The forgottenPasswordAnswer1
     */
    @JsonProperty("forgottenPasswordAnswer1")
    public void setForgottenPasswordAnswer1(String forgottenPasswordAnswer1) {
        this.forgottenPasswordAnswer1 = forgottenPasswordAnswer1;
    }

    public ForgottenPasswordInfo withForgottenPasswordAnswer1(String forgottenPasswordAnswer1) {
        this.forgottenPasswordAnswer1 = forgottenPasswordAnswer1;
        return this;
    }

    /**
     * 
     * @return
     *     The forgottenPasswordAnswer2
     */
    @JsonProperty("forgottenPasswordAnswer2")
    public String getForgottenPasswordAnswer2() {
        return forgottenPasswordAnswer2;
    }

    /**
     * 
     * @param forgottenPasswordAnswer2
     *     The forgottenPasswordAnswer2
     */
    @JsonProperty("forgottenPasswordAnswer2")
    public void setForgottenPasswordAnswer2(String forgottenPasswordAnswer2) {
        this.forgottenPasswordAnswer2 = forgottenPasswordAnswer2;
    }

    public ForgottenPasswordInfo withForgottenPasswordAnswer2(String forgottenPasswordAnswer2) {
        this.forgottenPasswordAnswer2 = forgottenPasswordAnswer2;
        return this;
    }

    /**
     * 
     * @return
     *     The forgottenPasswordAnswer3
     */
    @JsonProperty("forgottenPasswordAnswer3")
    public String getForgottenPasswordAnswer3() {
        return forgottenPasswordAnswer3;
    }

    /**
     * 
     * @param forgottenPasswordAnswer3
     *     The forgottenPasswordAnswer3
     */
    @JsonProperty("forgottenPasswordAnswer3")
    public void setForgottenPasswordAnswer3(String forgottenPasswordAnswer3) {
        this.forgottenPasswordAnswer3 = forgottenPasswordAnswer3;
    }

    public ForgottenPasswordInfo withForgottenPasswordAnswer3(String forgottenPasswordAnswer3) {
        this.forgottenPasswordAnswer3 = forgottenPasswordAnswer3;
        return this;
    }

    /**
     * 
     * @return
     *     The forgottenPasswordAnswer4
     */
    @JsonProperty("forgottenPasswordAnswer4")
    public String getForgottenPasswordAnswer4() {
        return forgottenPasswordAnswer4;
    }

    /**
     * 
     * @param forgottenPasswordAnswer4
     *     The forgottenPasswordAnswer4
     */
    @JsonProperty("forgottenPasswordAnswer4")
    public void setForgottenPasswordAnswer4(String forgottenPasswordAnswer4) {
        this.forgottenPasswordAnswer4 = forgottenPasswordAnswer4;
    }

    public ForgottenPasswordInfo withForgottenPasswordAnswer4(String forgottenPasswordAnswer4) {
        this.forgottenPasswordAnswer4 = forgottenPasswordAnswer4;
        return this;
    }

    /**
     * 
     * @return
     *     The forgottenPasswordQuestion1
     */
    @JsonProperty("forgottenPasswordQuestion1")
    public String getForgottenPasswordQuestion1() {
        return forgottenPasswordQuestion1;
    }

    /**
     * 
     * @param forgottenPasswordQuestion1
     *     The forgottenPasswordQuestion1
     */
    @JsonProperty("forgottenPasswordQuestion1")
    public void setForgottenPasswordQuestion1(String forgottenPasswordQuestion1) {
        this.forgottenPasswordQuestion1 = forgottenPasswordQuestion1;
    }

    public ForgottenPasswordInfo withForgottenPasswordQuestion1(String forgottenPasswordQuestion1) {
        this.forgottenPasswordQuestion1 = forgottenPasswordQuestion1;
        return this;
    }

    /**
     * 
     * @return
     *     The forgottenPasswordQuestion2
     */
    @JsonProperty("forgottenPasswordQuestion2")
    public String getForgottenPasswordQuestion2() {
        return forgottenPasswordQuestion2;
    }

    /**
     * 
     * @param forgottenPasswordQuestion2
     *     The forgottenPasswordQuestion2
     */
    @JsonProperty("forgottenPasswordQuestion2")
    public void setForgottenPasswordQuestion2(String forgottenPasswordQuestion2) {
        this.forgottenPasswordQuestion2 = forgottenPasswordQuestion2;
    }

    public ForgottenPasswordInfo withForgottenPasswordQuestion2(String forgottenPasswordQuestion2) {
        this.forgottenPasswordQuestion2 = forgottenPasswordQuestion2;
        return this;
    }

    /**
     * 
     * @return
     *     The forgottenPasswordQuestion3
     */
    @JsonProperty("forgottenPasswordQuestion3")
    public String getForgottenPasswordQuestion3() {
        return forgottenPasswordQuestion3;
    }

    /**
     * 
     * @param forgottenPasswordQuestion3
     *     The forgottenPasswordQuestion3
     */
    @JsonProperty("forgottenPasswordQuestion3")
    public void setForgottenPasswordQuestion3(String forgottenPasswordQuestion3) {
        this.forgottenPasswordQuestion3 = forgottenPasswordQuestion3;
    }

    public ForgottenPasswordInfo withForgottenPasswordQuestion3(String forgottenPasswordQuestion3) {
        this.forgottenPasswordQuestion3 = forgottenPasswordQuestion3;
        return this;
    }

    /**
     * 
     * @return
     *     The forgottenPasswordQuestion4
     */
    @JsonProperty("forgottenPasswordQuestion4")
    public String getForgottenPasswordQuestion4() {
        return forgottenPasswordQuestion4;
    }

    /**
     * 
     * @param forgottenPasswordQuestion4
     *     The forgottenPasswordQuestion4
     */
    @JsonProperty("forgottenPasswordQuestion4")
    public void setForgottenPasswordQuestion4(String forgottenPasswordQuestion4) {
        this.forgottenPasswordQuestion4 = forgottenPasswordQuestion4;
    }

    public ForgottenPasswordInfo withForgottenPasswordQuestion4(String forgottenPasswordQuestion4) {
        this.forgottenPasswordQuestion4 = forgottenPasswordQuestion4;
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

    public ForgottenPasswordInfo withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
