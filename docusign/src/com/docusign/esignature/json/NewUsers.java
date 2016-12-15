
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
    "newUsers"
})
public class NewUsers {

    @JsonProperty("newUsers")
    private List<NewUser> newUsers = new ArrayList<NewUser>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The newUsers
     */
    @JsonProperty("newUsers")
    public List<NewUser> getNewUsers() {
        return newUsers;
    }

    /**
     * 
     * @param newUsers
     *     The newUsers
     */
    @JsonProperty("newUsers")
    public void setNewUsers(List<NewUser> newUsers) {
        this.newUsers = newUsers;
    }

    public NewUsers withNewUsers(List<NewUser> newUsers) {
        this.newUsers = newUsers;
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

    public NewUsers withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
