
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
    "resultSetSize",
    "totalSetSize",
    "startPosition",
    "endPosition",
    "nextUri",
    "previousUri",
    "envelopes"
})
public class EnvelopesStatusChanged {

    @JsonProperty("resultSetSize")
    private String resultSetSize;
    @JsonProperty("totalSetSize")
    private String totalSetSize;
    @JsonProperty("startPosition")
    private String startPosition;
    @JsonProperty("endPosition")
    private String endPosition;
    @JsonProperty("nextUri")
    private String nextUri;
    @JsonProperty("previousUri")
    private String previousUri;
    @JsonProperty("envelopes")
    private List<Envelope> envelopes = new ArrayList<Envelope>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The resultSetSize
     */
    @JsonProperty("resultSetSize")
    public String getResultSetSize() {
        return resultSetSize;
    }

    /**
     * 
     * @param resultSetSize
     *     The resultSetSize
     */
    @JsonProperty("resultSetSize")
    public void setResultSetSize(String resultSetSize) {
        this.resultSetSize = resultSetSize;
    }

    /**
     * 
     * @return
     *     The totalSetSize
     */
    @JsonProperty("totalSetSize")
    public String getTotalSetSize() {
        return totalSetSize;
    }

    /**
     * 
     * @param totalSetSize
     *     The totalSetSize
     */
    @JsonProperty("totalSetSize")
    public void setTotalSetSize(String totalSetSize) {
        this.totalSetSize = totalSetSize;
    }

    /**
     * 
     * @return
     *     The startPosition
     */
    @JsonProperty("startPosition")
    public String getStartPosition() {
        return startPosition;
    }

    /**
     * 
     * @param startPosition
     *     The startPosition
     */
    @JsonProperty("startPosition")
    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    /**
     * 
     * @return
     *     The endPosition
     */
    @JsonProperty("endPosition")
    public String getEndPosition() {
        return endPosition;
    }

    /**
     * 
     * @param endPosition
     *     The endPosition
     */
    @JsonProperty("endPosition")
    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    /**
     * 
     * @return
     *     The nextUri
     */
    @JsonProperty("nextUri")
    public String getNextUri() {
        return nextUri;
    }

    /**
     * 
     * @param nextUri
     *     The nextUri
     */
    @JsonProperty("nextUri")
    public void setNextUri(String nextUri) {
        this.nextUri = nextUri;
    }

    /**
     * 
     * @return
     *     The previousUri
     */
    @JsonProperty("previousUri")
    public String getPreviousUri() {
        return previousUri;
    }

    /**
     * 
     * @param previousUri
     *     The previousUri
     */
    @JsonProperty("previousUri")
    public void setPreviousUri(String previousUri) {
        this.previousUri = previousUri;
    }

    /**
     * 
     * @return
     *     The envelopes
     */
    @JsonProperty("envelopes")
    public List<Envelope> getEnvelopes() {
        return envelopes;
    }

    /**
     * 
     * @param envelopes
     *     The envelopes
     */
    @JsonProperty("envelopes")
    public void setEnvelopes(List<Envelope> envelopes) {
        this.envelopes = envelopes;
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

}
