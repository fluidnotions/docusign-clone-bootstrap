
package com.fluidnotions.models;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class WhereCondition {

    @SerializedName("left")
    @Expose
    private String left;
    @SerializedName("operator")
    @Expose
    private String operator;
    @SerializedName("right")
    @Expose
    private String right;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("options")
    @Expose
    private String options;

    /**
     * 
     * @return
     *     The left
     */
    public String getLeft() {
        return left;
    }

    /**
     * 
     * @param left
     *     The left
     */
    public void setLeft(String left) {
        this.left = left;
    }

    public WhereCondition withLeft(String left) {
        this.left = left;
        return this;
    }

    /**
     * 
     * @return
     *     The operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * 
     * @param operator
     *     The operator
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    public WhereCondition withOperator(String operator) {
        this.operator = operator;
        return this;
    }

    /**
     * 
     * @return
     *     The right
     */
    public String getRight() {
        return right;
    }

    /**
     * 
     * @param right
     *     The right
     */
    public void setRight(String right) {
        this.right = right;
    }

    public WhereCondition withRight(String right) {
        this.right = right;
        return this;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    public WhereCondition withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * 
     * @return
     *     The options
     */
    public String getOptions() {
        return options;
    }

    /**
     * 
     * @param options
     *     The options
     */
    public void setOptions(String options) {
        this.options = options;
    }

    public WhereCondition withOptions(String options) {
        this.options = options;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
