
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
    "groupId",
    "groupName",
    "permissionProfileId",
    "groupType"
})
public class GroupList {

    @JsonProperty("groupId")
    private String groupId;
    @JsonProperty("groupName")
    private String groupName;
    @JsonProperty("permissionProfileId")
    private String permissionProfileId;
    @JsonProperty("groupType")
    private String groupType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The groupId
     */
    @JsonProperty("groupId")
    public String getGroupId() {
        return groupId;
    }

    /**
     * 
     * @param groupId
     *     The groupId
     */
    @JsonProperty("groupId")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public GroupList withGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    /**
     * 
     * @return
     *     The groupName
     */
    @JsonProperty("groupName")
    public String getGroupName() {
        return groupName;
    }

    /**
     * 
     * @param groupName
     *     The groupName
     */
    @JsonProperty("groupName")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public GroupList withGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    /**
     * 
     * @return
     *     The permissionProfileId
     */
    @JsonProperty("permissionProfileId")
    public String getPermissionProfileId() {
        return permissionProfileId;
    }

    /**
     * 
     * @param permissionProfileId
     *     The permissionProfileId
     */
    @JsonProperty("permissionProfileId")
    public void setPermissionProfileId(String permissionProfileId) {
        this.permissionProfileId = permissionProfileId;
    }

    public GroupList withPermissionProfileId(String permissionProfileId) {
        this.permissionProfileId = permissionProfileId;
        return this;
    }

    /**
     * 
     * @return
     *     The groupType
     */
    @JsonProperty("groupType")
    public String getGroupType() {
        return groupType;
    }

    /**
     * 
     * @param groupType
     *     The groupType
     */
    @JsonProperty("groupType")
    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public GroupList withGroupType(String groupType) {
        this.groupType = groupType;
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

    public GroupList withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
