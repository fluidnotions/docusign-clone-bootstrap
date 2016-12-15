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
@JsonPropertyOrder({ "signerEmail", "signerName", "recipientId", "routingOrder", "clientUserId" })
public class RecipientModel {

	@JsonProperty("signerEmail")
	private String signerEmail;
	@JsonProperty("signerName")
	private String signerName;
	@JsonProperty("recipientId")
	private String recipientId;
	@JsonProperty("routingOrder")
	private String routingOrder;
	@JsonProperty("clientUserId")
	private String clientUserId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	Pos xPos;
	Pos yPos;
	
	public Pos getXPos() {
		return xPos;
	}

	public void setXPos(Pos xPos) {
		this.xPos = xPos;
	}

	public Pos getYPos() {
		return yPos;
	}

	public void setYPos(Pos yPos) {
		this.yPos = yPos;
	}

	public RecipientModel() {
	}

	public RecipientModel(Integer routingOrder, String signerEmail, String signerName, String recipientId) {
		this(routingOrder, signerEmail, signerName, recipientId, null);
	}

	public RecipientModel(Integer routingOrder, String signerEmail, String signerName, String recipientId, String clientUserId) {
		super();
		this.signerEmail = signerEmail;
		this.signerName = signerName;
		this.recipientId = recipientId;
		this.routingOrder = routingOrder.toString();
		this.clientUserId = clientUserId;
	}

	public boolean isEmbeddedSigning() {
		return (clientUserId != null);
	}

	public void setEmbeddedSigningTrue() {
		this.clientUserId = "001";
	}

	public void setxPos(Double xPos) {
		this.xPos = new Pos(xPos);
	}

	public void setyPos(Double yPos) {
		this.yPos = new Pos(yPos);
	}

	public Pos subFromPos(Pos pos, Integer addition) {
		return new Pos(new Double(pos.intgr() - addition));
	}

	public Pos addToPos(Pos pos, Integer addition) {
		return new Pos(new Double(pos.intgr() + addition));
	}

	public static class Pos {
		Double pf;

		public Pos(Double pf) {
			super();
			this.pf = pf;
		}

		public String str() {
			return new Integer(pf.intValue()).toString();
		}

		public Integer intgr() {
			return new Integer(pf.intValue());
		}

	}

	/**
	 * 
	 * @return The signerEmail
	 */
	@JsonProperty("signerEmail")
	public String getSignerEmail() {
		return signerEmail;
	}

	/**
	 * 
	 * @param signerEmail
	 *            The signerEmail
	 */
	@JsonProperty("signerEmail")
	public void setSignerEmail(String signerEmail) {
		this.signerEmail = signerEmail;
	}

	/**
	 * 
	 * @return The signerName
	 */
	@JsonProperty("signerName")
	public String getSignerName() {
		return signerName;
	}

	/**
	 * 
	 * @param signerName
	 *            The signerName
	 */
	@JsonProperty("signerName")
	public void setSignerName(String signerName) {
		this.signerName = signerName;
	}

	/**
	 * 
	 * @return The recipientId
	 */
	@JsonProperty("recipientId")
	public String getRecipientId() {
		return recipientId;
	}

	/**
	 * 
	 * @param recipientId
	 *            The recipientId
	 */
	@JsonProperty("recipientId")
	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	/**
	 * 
	 * @return The routingOrder
	 */
	@JsonProperty("routingOrder")
	public String getRoutingOrder() {
		return routingOrder;
	}

	/**
	 * 
	 * @param routingOrder
	 *            The routingOrder
	 */
	@JsonProperty("routingOrder")
	public void setRoutingOrder(Integer routingOrder) {
		this.routingOrder = routingOrder.toString();
	}

	/**
	 * 
	 * @return The clientUserId
	 */
	@JsonProperty("clientUserId")
	public String getClientUserId() {
		return clientUserId;
	}

	/**
	 * 
	 * @param clientUserId
	 *            The clientUserId
	 */
	@JsonProperty("clientUserId")
	public void setClientUserId(String clientUserId) {
		this.clientUserId = clientUserId;
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
