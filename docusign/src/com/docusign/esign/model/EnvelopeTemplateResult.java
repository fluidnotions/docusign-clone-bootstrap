package com.docusign.esign.model;

import java.util.Objects;
import com.docusign.esign.model.CustomFields;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EmailSettings;
import com.docusign.esign.model.LockInformation;
import com.docusign.esign.model.Notification;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.UserInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2016-07-18T18:11:15.675-07:00")
public class EnvelopeTemplateResult   {
  
  private String templateId = null;
  private String name = null;
  private String shared = null;
  private String password = null;
  private String description = null;
  private String lastModified = null;
  private Integer pageCount = null;
  private String uri = null;
  private String folderName = null;
  private String folderId = null;
  private String folderUri = null;
  private String parentFolderUri = null;
  private UserInfo owner = null;
  private java.util.List<Document> documents = new java.util.ArrayList<Document>();
  private String transactionId = null;
  private String status = null;
  private String documentsUri = null;
  private String recipientsUri = null;
  private String asynchronous = null;
  private String envelopeUri = null;
  private String emailSubject = null;
  private String emailBlurb = null;
  private String envelopeId = null;
  private String signingLocation = null;
  private String customFieldsUri = null;
  private CustomFields customFields = null;
  private String autoNavigation = null;
  private String envelopeIdStamping = null;
  private String authoritativeCopy = null;
  private Notification notification = null;
  private String notificationUri = null;
  private String enforceSignerVisibility = null;
  private String enableWetSign = null;
  private String allowMarkup = null;
  private String allowReassign = null;
  private String createdDateTime = null;
  private String lastModifiedDateTime = null;
  private String deliveredDateTime = null;
  private String initialSentDateTime = null;
  private String sentDateTime = null;
  private String completedDateTime = null;
  private String voidedDateTime = null;
  private String voidedReason = null;
  private String deletedDateTime = null;
  private String declinedDateTime = null;
  private String statusChangedDateTime = null;
  private String documentsCombinedUri = null;
  private String certificateUri = null;
  private String templatesUri = null;
  private String messageLock = null;
  private String recipientsLock = null;
  private Recipients recipients = null;
  private String brandLock = null;
  private String brandId = null;
  private String useDisclosure = null;
  private EmailSettings emailSettings = null;
  private String purgeState = null;
  private LockInformation lockInformation = null;
  private String is21CFRPart11 = null;
  private String isSignatureProviderEnvelope = null;

  
  /**
   * The unique identifier of the template. If this is not provided, DocuSign will generate a value. 
   **/
  public EnvelopeTemplateResult templateId(String templateId) {
    this.templateId = templateId;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "The unique identifier of the template. If this is not provided, DocuSign will generate a value. ")
  @JsonProperty("templateId")
  public String getTemplateId() {
    return templateId;
  }
  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult name(String name) {
    this.name = name;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  
  /**
   * When set to **true**, this custom tab is shared.
   **/
  public EnvelopeTemplateResult shared(String shared) {
    this.shared = shared;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "When set to **true**, this custom tab is shared.")
  @JsonProperty("shared")
  public String getShared() {
    return shared;
  }
  public void setShared(String shared) {
    this.shared = shared;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult password(String password) {
    this.password = password;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult description(String description) {
    this.description = description;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult lastModified(String lastModified) {
    this.lastModified = lastModified;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("lastModified")
  public String getLastModified() {
    return lastModified;
  }
  public void setLastModified(String lastModified) {
    this.lastModified = lastModified;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult pageCount(Integer pageCount) {
    this.pageCount = pageCount;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("pageCount")
  public Integer getPageCount() {
    return pageCount;
  }
  public void setPageCount(Integer pageCount) {
    this.pageCount = pageCount;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult uri(String uri) {
    this.uri = uri;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("uri")
  public String getUri() {
    return uri;
  }
  public void setUri(String uri) {
    this.uri = uri;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult folderName(String folderName) {
    this.folderName = folderName;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("folderName")
  public String getFolderName() {
    return folderName;
  }
  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult folderId(String folderId) {
    this.folderId = folderId;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("folderId")
  public String getFolderId() {
    return folderId;
  }
  public void setFolderId(String folderId) {
    this.folderId = folderId;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult folderUri(String folderUri) {
    this.folderUri = folderUri;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("folderUri")
  public String getFolderUri() {
    return folderUri;
  }
  public void setFolderUri(String folderUri) {
    this.folderUri = folderUri;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult parentFolderUri(String parentFolderUri) {
    this.parentFolderUri = parentFolderUri;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("parentFolderUri")
  public String getParentFolderUri() {
    return parentFolderUri;
  }
  public void setParentFolderUri(String parentFolderUri) {
    this.parentFolderUri = parentFolderUri;
  }

  
  /**
   **/
  public EnvelopeTemplateResult owner(UserInfo owner) {
    this.owner = owner;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("owner")
  public UserInfo getOwner() {
    return owner;
  }
  public void setOwner(UserInfo owner) {
    this.owner = owner;
  }

  
  /**
   * Complex element contains the details on the documents in the envelope.
   **/
  public EnvelopeTemplateResult documents(java.util.List<Document> documents) {
    this.documents = documents;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Complex element contains the details on the documents in the envelope.")
  @JsonProperty("documents")
  public java.util.List<Document> getDocuments() {
    return documents;
  }
  public void setDocuments(java.util.List<Document> documents) {
    this.documents = documents;
  }

  
  /**
   *  Used to identify an envelope. The id is a sender-generated value and is valid in the DocuSign system for 7 days. It is recommended that a transaction ID is used for offline signing to ensure that an envelope is not sent multiple times. The `transactionId` property can be used determine an envelope's status (i.e. was it created or not) in cases where the internet connection was lost before the envelope status was returned.
   **/
  public EnvelopeTemplateResult transactionId(String transactionId) {
    this.transactionId = transactionId;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = " Used to identify an envelope. The id is a sender-generated value and is valid in the DocuSign system for 7 days. It is recommended that a transaction ID is used for offline signing to ensure that an envelope is not sent multiple times. The `transactionId` property can be used determine an envelope's status (i.e. was it created or not) in cases where the internet connection was lost before the envelope status was returned.")
  @JsonProperty("transactionId")
  public String getTransactionId() {
    return transactionId;
  }
  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  
  /**
   * Indicates the envelope status. Valid values are:\n\n* sent - The envelope is sent to the recipients. \n* created - The envelope is saved as a draft and can be modified and sent later.
   **/
  public EnvelopeTemplateResult status(String status) {
    this.status = status;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Indicates the envelope status. Valid values are:\n\n* sent - The envelope is sent to the recipients. \n* created - The envelope is saved as a draft and can be modified and sent later.")
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }

  
  /**
   * Contains a URI for an endpoint that you can use to retrieve the documents.
   **/
  public EnvelopeTemplateResult documentsUri(String documentsUri) {
    this.documentsUri = documentsUri;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Contains a URI for an endpoint that you can use to retrieve the documents.")
  @JsonProperty("documentsUri")
  public String getDocumentsUri() {
    return documentsUri;
  }
  public void setDocumentsUri(String documentsUri) {
    this.documentsUri = documentsUri;
  }

  
  /**
   * Contains a URI for an endpoint that you can use to retrieve the recipients.
   **/
  public EnvelopeTemplateResult recipientsUri(String recipientsUri) {
    this.recipientsUri = recipientsUri;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Contains a URI for an endpoint that you can use to retrieve the recipients.")
  @JsonProperty("recipientsUri")
  public String getRecipientsUri() {
    return recipientsUri;
  }
  public void setRecipientsUri(String recipientsUri) {
    this.recipientsUri = recipientsUri;
  }

  
  /**
   * When set to **true**, the envelope is queued for processing and the value of the `status` property is set to 'Processing'. Additionally, get status calls return 'Processing' until completed.
   **/
  public EnvelopeTemplateResult asynchronous(String asynchronous) {
    this.asynchronous = asynchronous;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "When set to **true**, the envelope is queued for processing and the value of the `status` property is set to 'Processing'. Additionally, get status calls return 'Processing' until completed.")
  @JsonProperty("asynchronous")
  public String getAsynchronous() {
    return asynchronous;
  }
  public void setAsynchronous(String asynchronous) {
    this.asynchronous = asynchronous;
  }

  
  /**
   * Contains a URI for an endpoint that you can use to retrieve the envelope or envelopes.
   **/
  public EnvelopeTemplateResult envelopeUri(String envelopeUri) {
    this.envelopeUri = envelopeUri;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Contains a URI for an endpoint that you can use to retrieve the envelope or envelopes.")
  @JsonProperty("envelopeUri")
  public String getEnvelopeUri() {
    return envelopeUri;
  }
  public void setEnvelopeUri(String envelopeUri) {
    this.envelopeUri = envelopeUri;
  }

  
  /**
   * Specifies the subject of the email that is sent to all recipients.\n\nSee [ML:Template Email Subject Merge Fields] for information about adding merge field information to the email subject.
   **/
  public EnvelopeTemplateResult emailSubject(String emailSubject) {
    this.emailSubject = emailSubject;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Specifies the subject of the email that is sent to all recipients.\n\nSee [ML:Template Email Subject Merge Fields] for information about adding merge field information to the email subject.")
  @JsonProperty("emailSubject")
  public String getEmailSubject() {
    return emailSubject;
  }
  public void setEmailSubject(String emailSubject) {
    this.emailSubject = emailSubject;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult emailBlurb(String emailBlurb) {
    this.emailBlurb = emailBlurb;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("emailBlurb")
  public String getEmailBlurb() {
    return emailBlurb;
  }
  public void setEmailBlurb(String emailBlurb) {
    this.emailBlurb = emailBlurb;
  }

  
  /**
   * The envelope ID of the envelope status that failed to post.
   **/
  public EnvelopeTemplateResult envelopeId(String envelopeId) {
    this.envelopeId = envelopeId;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "The envelope ID of the envelope status that failed to post.")
  @JsonProperty("envelopeId")
  public String getEnvelopeId() {
    return envelopeId;
  }
  public void setEnvelopeId(String envelopeId) {
    this.envelopeId = envelopeId;
  }

  
  /**
   * Specifies the physical location where the signing takes place. It can have two enumeration values; InPerson and Online. The default value is Online.
   **/
  public EnvelopeTemplateResult signingLocation(String signingLocation) {
    this.signingLocation = signingLocation;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Specifies the physical location where the signing takes place. It can have two enumeration values; InPerson and Online. The default value is Online.")
  @JsonProperty("signingLocation")
  public String getSigningLocation() {
    return signingLocation;
  }
  public void setSigningLocation(String signingLocation) {
    this.signingLocation = signingLocation;
  }

  
  /**
   * Contains a URI for an endpoint that you can use to retrieve the custom fields.
   **/
  public EnvelopeTemplateResult customFieldsUri(String customFieldsUri) {
    this.customFieldsUri = customFieldsUri;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Contains a URI for an endpoint that you can use to retrieve the custom fields.")
  @JsonProperty("customFieldsUri")
  public String getCustomFieldsUri() {
    return customFieldsUri;
  }
  public void setCustomFieldsUri(String customFieldsUri) {
    this.customFieldsUri = customFieldsUri;
  }

  
  /**
   **/
  public EnvelopeTemplateResult customFields(CustomFields customFields) {
    this.customFields = customFields;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("customFields")
  public CustomFields getCustomFields() {
    return customFields;
  }
  public void setCustomFields(CustomFields customFields) {
    this.customFields = customFields;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult autoNavigation(String autoNavigation) {
    this.autoNavigation = autoNavigation;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("autoNavigation")
  public String getAutoNavigation() {
    return autoNavigation;
  }
  public void setAutoNavigation(String autoNavigation) {
    this.autoNavigation = autoNavigation;
  }

  
  /**
   * When set to **true**, Envelope ID Stamping is enabled.
   **/
  public EnvelopeTemplateResult envelopeIdStamping(String envelopeIdStamping) {
    this.envelopeIdStamping = envelopeIdStamping;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "When set to **true**, Envelope ID Stamping is enabled.")
  @JsonProperty("envelopeIdStamping")
  public String getEnvelopeIdStamping() {
    return envelopeIdStamping;
  }
  public void setEnvelopeIdStamping(String envelopeIdStamping) {
    this.envelopeIdStamping = envelopeIdStamping;
  }

  
  /**
   * Specifies the Authoritative copy feature. If set to true the Authoritative copy feature is enabled.
   **/
  public EnvelopeTemplateResult authoritativeCopy(String authoritativeCopy) {
    this.authoritativeCopy = authoritativeCopy;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Specifies the Authoritative copy feature. If set to true the Authoritative copy feature is enabled.")
  @JsonProperty("authoritativeCopy")
  public String getAuthoritativeCopy() {
    return authoritativeCopy;
  }
  public void setAuthoritativeCopy(String authoritativeCopy) {
    this.authoritativeCopy = authoritativeCopy;
  }

  
  /**
   **/
  public EnvelopeTemplateResult notification(Notification notification) {
    this.notification = notification;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("notification")
  public Notification getNotification() {
    return notification;
  }
  public void setNotification(Notification notification) {
    this.notification = notification;
  }

  
  /**
   * Contains a URI for an endpoint that you can use to retrieve the notifications.
   **/
  public EnvelopeTemplateResult notificationUri(String notificationUri) {
    this.notificationUri = notificationUri;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Contains a URI for an endpoint that you can use to retrieve the notifications.")
  @JsonProperty("notificationUri")
  public String getNotificationUri() {
    return notificationUri;
  }
  public void setNotificationUri(String notificationUri) {
    this.notificationUri = notificationUri;
  }

  
  /**
   * When set to **true**, documents with tabs can only be viewed by signers that have a tab on that document. Recipients that have an administrative role (Agent, Editor, or Intermediaries) or informational role (Certified Deliveries or Carbon Copies) can always see all the documents in an envelope, unless they are specifically excluded using this setting when an envelope is sent. Documents that do not have tabs are always visible to all recipients, unless they are specifically excluded using this setting when an envelope is sent.\n\nYour account must have Document Visibility enabled to use this.
   **/
  public EnvelopeTemplateResult enforceSignerVisibility(String enforceSignerVisibility) {
    this.enforceSignerVisibility = enforceSignerVisibility;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "When set to **true**, documents with tabs can only be viewed by signers that have a tab on that document. Recipients that have an administrative role (Agent, Editor, or Intermediaries) or informational role (Certified Deliveries or Carbon Copies) can always see all the documents in an envelope, unless they are specifically excluded using this setting when an envelope is sent. Documents that do not have tabs are always visible to all recipients, unless they are specifically excluded using this setting when an envelope is sent.\n\nYour account must have Document Visibility enabled to use this.")
  @JsonProperty("enforceSignerVisibility")
  public String getEnforceSignerVisibility() {
    return enforceSignerVisibility;
  }
  public void setEnforceSignerVisibility(String enforceSignerVisibility) {
    this.enforceSignerVisibility = enforceSignerVisibility;
  }

  
  /**
   * When set to **true**, the signer is allowed to print the document and sign it on paper.
   **/
  public EnvelopeTemplateResult enableWetSign(String enableWetSign) {
    this.enableWetSign = enableWetSign;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "When set to **true**, the signer is allowed to print the document and sign it on paper.")
  @JsonProperty("enableWetSign")
  public String getEnableWetSign() {
    return enableWetSign;
  }
  public void setEnableWetSign(String enableWetSign) {
    this.enableWetSign = enableWetSign;
  }

  
  /**
   * When set to **true**, Document Markup is enabled for envelope. Account must have Document Markup enabled to use this
   **/
  public EnvelopeTemplateResult allowMarkup(String allowMarkup) {
    this.allowMarkup = allowMarkup;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "When set to **true**, Document Markup is enabled for envelope. Account must have Document Markup enabled to use this")
  @JsonProperty("allowMarkup")
  public String getAllowMarkup() {
    return allowMarkup;
  }
  public void setAllowMarkup(String allowMarkup) {
    this.allowMarkup = allowMarkup;
  }

  
  /**
   * When set to **true**, the recipient can redirect an envelope to a more appropriate recipient.
   **/
  public EnvelopeTemplateResult allowReassign(String allowReassign) {
    this.allowReassign = allowReassign;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "When set to **true**, the recipient can redirect an envelope to a more appropriate recipient.")
  @JsonProperty("allowReassign")
  public String getAllowReassign() {
    return allowReassign;
  }
  public void setAllowReassign(String allowReassign) {
    this.allowReassign = allowReassign;
  }

  
  /**
   * Indicates the date and time the item was created.
   **/
  public EnvelopeTemplateResult createdDateTime(String createdDateTime) {
    this.createdDateTime = createdDateTime;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Indicates the date and time the item was created.")
  @JsonProperty("createdDateTime")
  public String getCreatedDateTime() {
    return createdDateTime;
  }
  public void setCreatedDateTime(String createdDateTime) {
    this.createdDateTime = createdDateTime;
  }

  
  /**
   * The date and time the item was last modified.
   **/
  public EnvelopeTemplateResult lastModifiedDateTime(String lastModifiedDateTime) {
    this.lastModifiedDateTime = lastModifiedDateTime;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "The date and time the item was last modified.")
  @JsonProperty("lastModifiedDateTime")
  public String getLastModifiedDateTime() {
    return lastModifiedDateTime;
  }
  public void setLastModifiedDateTime(String lastModifiedDateTime) {
    this.lastModifiedDateTime = lastModifiedDateTime;
  }

  
  /**
   * Reserved: For DocuSign use only.
   **/
  public EnvelopeTemplateResult deliveredDateTime(String deliveredDateTime) {
    this.deliveredDateTime = deliveredDateTime;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Reserved: For DocuSign use only.")
  @JsonProperty("deliveredDateTime")
  public String getDeliveredDateTime() {
    return deliveredDateTime;
  }
  public void setDeliveredDateTime(String deliveredDateTime) {
    this.deliveredDateTime = deliveredDateTime;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult initialSentDateTime(String initialSentDateTime) {
    this.initialSentDateTime = initialSentDateTime;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("initialSentDateTime")
  public String getInitialSentDateTime() {
    return initialSentDateTime;
  }
  public void setInitialSentDateTime(String initialSentDateTime) {
    this.initialSentDateTime = initialSentDateTime;
  }

  
  /**
   * The date and time the envelope was sent.
   **/
  public EnvelopeTemplateResult sentDateTime(String sentDateTime) {
    this.sentDateTime = sentDateTime;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "The date and time the envelope was sent.")
  @JsonProperty("sentDateTime")
  public String getSentDateTime() {
    return sentDateTime;
  }
  public void setSentDateTime(String sentDateTime) {
    this.sentDateTime = sentDateTime;
  }

  
  /**
   * Specifies the date and time this item was completed.
   **/
  public EnvelopeTemplateResult completedDateTime(String completedDateTime) {
    this.completedDateTime = completedDateTime;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Specifies the date and time this item was completed.")
  @JsonProperty("completedDateTime")
  public String getCompletedDateTime() {
    return completedDateTime;
  }
  public void setCompletedDateTime(String completedDateTime) {
    this.completedDateTime = completedDateTime;
  }

  
  /**
   * The date and time the envelope or template was voided.
   **/
  public EnvelopeTemplateResult voidedDateTime(String voidedDateTime) {
    this.voidedDateTime = voidedDateTime;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "The date and time the envelope or template was voided.")
  @JsonProperty("voidedDateTime")
  public String getVoidedDateTime() {
    return voidedDateTime;
  }
  public void setVoidedDateTime(String voidedDateTime) {
    this.voidedDateTime = voidedDateTime;
  }

  
  /**
   * The reason the envelope or template was voided.
   **/
  public EnvelopeTemplateResult voidedReason(String voidedReason) {
    this.voidedReason = voidedReason;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "The reason the envelope or template was voided.")
  @JsonProperty("voidedReason")
  public String getVoidedReason() {
    return voidedReason;
  }
  public void setVoidedReason(String voidedReason) {
    this.voidedReason = voidedReason;
  }

  
  /**
   * Specifies the data and time the item was deleted.
   **/
  public EnvelopeTemplateResult deletedDateTime(String deletedDateTime) {
    this.deletedDateTime = deletedDateTime;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Specifies the data and time the item was deleted.")
  @JsonProperty("deletedDateTime")
  public String getDeletedDateTime() {
    return deletedDateTime;
  }
  public void setDeletedDateTime(String deletedDateTime) {
    this.deletedDateTime = deletedDateTime;
  }

  
  /**
   * The date and time the recipient declined the document.
   **/
  public EnvelopeTemplateResult declinedDateTime(String declinedDateTime) {
    this.declinedDateTime = declinedDateTime;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "The date and time the recipient declined the document.")
  @JsonProperty("declinedDateTime")
  public String getDeclinedDateTime() {
    return declinedDateTime;
  }
  public void setDeclinedDateTime(String declinedDateTime) {
    this.declinedDateTime = declinedDateTime;
  }

  
  /**
   * The data and time the status changed.
   **/
  public EnvelopeTemplateResult statusChangedDateTime(String statusChangedDateTime) {
    this.statusChangedDateTime = statusChangedDateTime;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "The data and time the status changed.")
  @JsonProperty("statusChangedDateTime")
  public String getStatusChangedDateTime() {
    return statusChangedDateTime;
  }
  public void setStatusChangedDateTime(String statusChangedDateTime) {
    this.statusChangedDateTime = statusChangedDateTime;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult documentsCombinedUri(String documentsCombinedUri) {
    this.documentsCombinedUri = documentsCombinedUri;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("documentsCombinedUri")
  public String getDocumentsCombinedUri() {
    return documentsCombinedUri;
  }
  public void setDocumentsCombinedUri(String documentsCombinedUri) {
    this.documentsCombinedUri = documentsCombinedUri;
  }

  
  /**
   * Retrieves a URI for an endpoint that allows you to easily retrieve certificate information.
   **/
  public EnvelopeTemplateResult certificateUri(String certificateUri) {
    this.certificateUri = certificateUri;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Retrieves a URI for an endpoint that allows you to easily retrieve certificate information.")
  @JsonProperty("certificateUri")
  public String getCertificateUri() {
    return certificateUri;
  }
  public void setCertificateUri(String certificateUri) {
    this.certificateUri = certificateUri;
  }

  
  /**
   * Contains a URI for an endpoint which you can use to retrieve the templates.
   **/
  public EnvelopeTemplateResult templatesUri(String templatesUri) {
    this.templatesUri = templatesUri;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "Contains a URI for an endpoint which you can use to retrieve the templates.")
  @JsonProperty("templatesUri")
  public String getTemplatesUri() {
    return templatesUri;
  }
  public void setTemplatesUri(String templatesUri) {
    this.templatesUri = templatesUri;
  }

  
  /**
   * When set to **true**, prevents senders from changing the contents of `emailBlurb` and `emailSubject` properties for the envelope. \n\nAdditionally, this prevents users from making changes to the contents of `emailBlurb` and `emailSubject` properties when correcting envelopes. \n\nHowever, if the `messageLock` node is set to true**** and the `emailSubject` property is empty, senders and correctors are able to add a subject to the envelope.
   **/
  public EnvelopeTemplateResult messageLock(String messageLock) {
    this.messageLock = messageLock;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "When set to **true**, prevents senders from changing the contents of `emailBlurb` and `emailSubject` properties for the envelope. \n\nAdditionally, this prevents users from making changes to the contents of `emailBlurb` and `emailSubject` properties when correcting envelopes. \n\nHowever, if the `messageLock` node is set to true**** and the `emailSubject` property is empty, senders and correctors are able to add a subject to the envelope.")
  @JsonProperty("messageLock")
  public String getMessageLock() {
    return messageLock;
  }
  public void setMessageLock(String messageLock) {
    this.messageLock = messageLock;
  }

  
  /**
   * When set to **true**, prevents senders from changing, correcting, or deleting the recipient information for the envelope.
   **/
  public EnvelopeTemplateResult recipientsLock(String recipientsLock) {
    this.recipientsLock = recipientsLock;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "When set to **true**, prevents senders from changing, correcting, or deleting the recipient information for the envelope.")
  @JsonProperty("recipientsLock")
  public String getRecipientsLock() {
    return recipientsLock;
  }
  public void setRecipientsLock(String recipientsLock) {
    this.recipientsLock = recipientsLock;
  }

  
  /**
   **/
  public EnvelopeTemplateResult recipients(Recipients recipients) {
    this.recipients = recipients;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("recipients")
  public Recipients getRecipients() {
    return recipients;
  }
  public void setRecipients(Recipients recipients) {
    this.recipients = recipients;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult brandLock(String brandLock) {
    this.brandLock = brandLock;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("brandLock")
  public String getBrandLock() {
    return brandLock;
  }
  public void setBrandLock(String brandLock) {
    this.brandLock = brandLock;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult brandId(String brandId) {
    this.brandId = brandId;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("brandId")
  public String getBrandId() {
    return brandId;
  }
  public void setBrandId(String brandId) {
    this.brandId = brandId;
  }

  
  /**
   * When set to **true**, the disclosure is shown to recipients in accordance with the account\u2019s Electronic Record and Signature Disclosure frequency setting. When set to **false**, the Electronic Record and Signature Disclosure is not shown to any envelope recipients. \n\nIf the `useDisclosure` property is not set, then the account's normal disclosure setting is used and the value of the `useDisclosure` property is not returned in responses when getting envelope information.
   **/
  public EnvelopeTemplateResult useDisclosure(String useDisclosure) {
    this.useDisclosure = useDisclosure;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "When set to **true**, the disclosure is shown to recipients in accordance with the account\u2019s Electronic Record and Signature Disclosure frequency setting. When set to **false**, the Electronic Record and Signature Disclosure is not shown to any envelope recipients. \n\nIf the `useDisclosure` property is not set, then the account's normal disclosure setting is used and the value of the `useDisclosure` property is not returned in responses when getting envelope information.")
  @JsonProperty("useDisclosure")
  public String getUseDisclosure() {
    return useDisclosure;
  }
  public void setUseDisclosure(String useDisclosure) {
    this.useDisclosure = useDisclosure;
  }

  
  /**
   **/
  public EnvelopeTemplateResult emailSettings(EmailSettings emailSettings) {
    this.emailSettings = emailSettings;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("emailSettings")
  public EmailSettings getEmailSettings() {
    return emailSettings;
  }
  public void setEmailSettings(EmailSettings emailSettings) {
    this.emailSettings = emailSettings;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult purgeState(String purgeState) {
    this.purgeState = purgeState;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("purgeState")
  public String getPurgeState() {
    return purgeState;
  }
  public void setPurgeState(String purgeState) {
    this.purgeState = purgeState;
  }

  
  /**
   **/
  public EnvelopeTemplateResult lockInformation(LockInformation lockInformation) {
    this.lockInformation = lockInformation;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("lockInformation")
  public LockInformation getLockInformation() {
    return lockInformation;
  }
  public void setLockInformation(LockInformation lockInformation) {
    this.lockInformation = lockInformation;
  }

  
  /**
   * When set to **true**, indicates that this module is enabled on the account.
   **/
  public EnvelopeTemplateResult is21CFRPart11(String is21CFRPart11) {
    this.is21CFRPart11 = is21CFRPart11;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "When set to **true**, indicates that this module is enabled on the account.")
  @JsonProperty("is21CFRPart11")
  public String getIs21CFRPart11() {
    return is21CFRPart11;
  }
  public void setIs21CFRPart11(String is21CFRPart11) {
    this.is21CFRPart11 = is21CFRPart11;
  }

  
  /**
   * 
   **/
  public EnvelopeTemplateResult isSignatureProviderEnvelope(String isSignatureProviderEnvelope) {
    this.isSignatureProviderEnvelope = isSignatureProviderEnvelope;
    return this;
  }
  
  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("isSignatureProviderEnvelope")
  public String getIsSignatureProviderEnvelope() {
    return isSignatureProviderEnvelope;
  }
  public void setIsSignatureProviderEnvelope(String isSignatureProviderEnvelope) {
    this.isSignatureProviderEnvelope = isSignatureProviderEnvelope;
  }

  

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnvelopeTemplateResult envelopeTemplateResult = (EnvelopeTemplateResult) o;
    return Objects.equals(this.templateId, envelopeTemplateResult.templateId) &&
        Objects.equals(this.name, envelopeTemplateResult.name) &&
        Objects.equals(this.shared, envelopeTemplateResult.shared) &&
        Objects.equals(this.password, envelopeTemplateResult.password) &&
        Objects.equals(this.description, envelopeTemplateResult.description) &&
        Objects.equals(this.lastModified, envelopeTemplateResult.lastModified) &&
        Objects.equals(this.pageCount, envelopeTemplateResult.pageCount) &&
        Objects.equals(this.uri, envelopeTemplateResult.uri) &&
        Objects.equals(this.folderName, envelopeTemplateResult.folderName) &&
        Objects.equals(this.folderId, envelopeTemplateResult.folderId) &&
        Objects.equals(this.folderUri, envelopeTemplateResult.folderUri) &&
        Objects.equals(this.parentFolderUri, envelopeTemplateResult.parentFolderUri) &&
        Objects.equals(this.owner, envelopeTemplateResult.owner) &&
        Objects.equals(this.documents, envelopeTemplateResult.documents) &&
        Objects.equals(this.transactionId, envelopeTemplateResult.transactionId) &&
        Objects.equals(this.status, envelopeTemplateResult.status) &&
        Objects.equals(this.documentsUri, envelopeTemplateResult.documentsUri) &&
        Objects.equals(this.recipientsUri, envelopeTemplateResult.recipientsUri) &&
        Objects.equals(this.asynchronous, envelopeTemplateResult.asynchronous) &&
        Objects.equals(this.envelopeUri, envelopeTemplateResult.envelopeUri) &&
        Objects.equals(this.emailSubject, envelopeTemplateResult.emailSubject) &&
        Objects.equals(this.emailBlurb, envelopeTemplateResult.emailBlurb) &&
        Objects.equals(this.envelopeId, envelopeTemplateResult.envelopeId) &&
        Objects.equals(this.signingLocation, envelopeTemplateResult.signingLocation) &&
        Objects.equals(this.customFieldsUri, envelopeTemplateResult.customFieldsUri) &&
        Objects.equals(this.customFields, envelopeTemplateResult.customFields) &&
        Objects.equals(this.autoNavigation, envelopeTemplateResult.autoNavigation) &&
        Objects.equals(this.envelopeIdStamping, envelopeTemplateResult.envelopeIdStamping) &&
        Objects.equals(this.authoritativeCopy, envelopeTemplateResult.authoritativeCopy) &&
        Objects.equals(this.notification, envelopeTemplateResult.notification) &&
        Objects.equals(this.notificationUri, envelopeTemplateResult.notificationUri) &&
        Objects.equals(this.enforceSignerVisibility, envelopeTemplateResult.enforceSignerVisibility) &&
        Objects.equals(this.enableWetSign, envelopeTemplateResult.enableWetSign) &&
        Objects.equals(this.allowMarkup, envelopeTemplateResult.allowMarkup) &&
        Objects.equals(this.allowReassign, envelopeTemplateResult.allowReassign) &&
        Objects.equals(this.createdDateTime, envelopeTemplateResult.createdDateTime) &&
        Objects.equals(this.lastModifiedDateTime, envelopeTemplateResult.lastModifiedDateTime) &&
        Objects.equals(this.deliveredDateTime, envelopeTemplateResult.deliveredDateTime) &&
        Objects.equals(this.initialSentDateTime, envelopeTemplateResult.initialSentDateTime) &&
        Objects.equals(this.sentDateTime, envelopeTemplateResult.sentDateTime) &&
        Objects.equals(this.completedDateTime, envelopeTemplateResult.completedDateTime) &&
        Objects.equals(this.voidedDateTime, envelopeTemplateResult.voidedDateTime) &&
        Objects.equals(this.voidedReason, envelopeTemplateResult.voidedReason) &&
        Objects.equals(this.deletedDateTime, envelopeTemplateResult.deletedDateTime) &&
        Objects.equals(this.declinedDateTime, envelopeTemplateResult.declinedDateTime) &&
        Objects.equals(this.statusChangedDateTime, envelopeTemplateResult.statusChangedDateTime) &&
        Objects.equals(this.documentsCombinedUri, envelopeTemplateResult.documentsCombinedUri) &&
        Objects.equals(this.certificateUri, envelopeTemplateResult.certificateUri) &&
        Objects.equals(this.templatesUri, envelopeTemplateResult.templatesUri) &&
        Objects.equals(this.messageLock, envelopeTemplateResult.messageLock) &&
        Objects.equals(this.recipientsLock, envelopeTemplateResult.recipientsLock) &&
        Objects.equals(this.recipients, envelopeTemplateResult.recipients) &&
        Objects.equals(this.brandLock, envelopeTemplateResult.brandLock) &&
        Objects.equals(this.brandId, envelopeTemplateResult.brandId) &&
        Objects.equals(this.useDisclosure, envelopeTemplateResult.useDisclosure) &&
        Objects.equals(this.emailSettings, envelopeTemplateResult.emailSettings) &&
        Objects.equals(this.purgeState, envelopeTemplateResult.purgeState) &&
        Objects.equals(this.lockInformation, envelopeTemplateResult.lockInformation) &&
        Objects.equals(this.is21CFRPart11, envelopeTemplateResult.is21CFRPart11) &&
        Objects.equals(this.isSignatureProviderEnvelope, envelopeTemplateResult.isSignatureProviderEnvelope);
  }

  @Override
  public int hashCode() {
    return Objects.hash(templateId, name, shared, password, description, lastModified, pageCount, uri, folderName, folderId, folderUri, parentFolderUri, owner, documents, transactionId, status, documentsUri, recipientsUri, asynchronous, envelopeUri, emailSubject, emailBlurb, envelopeId, signingLocation, customFieldsUri, customFields, autoNavigation, envelopeIdStamping, authoritativeCopy, notification, notificationUri, enforceSignerVisibility, enableWetSign, allowMarkup, allowReassign, createdDateTime, lastModifiedDateTime, deliveredDateTime, initialSentDateTime, sentDateTime, completedDateTime, voidedDateTime, voidedReason, deletedDateTime, declinedDateTime, statusChangedDateTime, documentsCombinedUri, certificateUri, templatesUri, messageLock, recipientsLock, recipients, brandLock, brandId, useDisclosure, emailSettings, purgeState, lockInformation, is21CFRPart11, isSignatureProviderEnvelope);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnvelopeTemplateResult {\n");
    
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    shared: ").append(toIndentedString(shared)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    lastModified: ").append(toIndentedString(lastModified)).append("\n");
    sb.append("    pageCount: ").append(toIndentedString(pageCount)).append("\n");
    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
    sb.append("    folderName: ").append(toIndentedString(folderName)).append("\n");
    sb.append("    folderId: ").append(toIndentedString(folderId)).append("\n");
    sb.append("    folderUri: ").append(toIndentedString(folderUri)).append("\n");
    sb.append("    parentFolderUri: ").append(toIndentedString(parentFolderUri)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    documentsUri: ").append(toIndentedString(documentsUri)).append("\n");
    sb.append("    recipientsUri: ").append(toIndentedString(recipientsUri)).append("\n");
    sb.append("    asynchronous: ").append(toIndentedString(asynchronous)).append("\n");
    sb.append("    envelopeUri: ").append(toIndentedString(envelopeUri)).append("\n");
    sb.append("    emailSubject: ").append(toIndentedString(emailSubject)).append("\n");
    sb.append("    emailBlurb: ").append(toIndentedString(emailBlurb)).append("\n");
    sb.append("    envelopeId: ").append(toIndentedString(envelopeId)).append("\n");
    sb.append("    signingLocation: ").append(toIndentedString(signingLocation)).append("\n");
    sb.append("    customFieldsUri: ").append(toIndentedString(customFieldsUri)).append("\n");
    sb.append("    customFields: ").append(toIndentedString(customFields)).append("\n");
    sb.append("    autoNavigation: ").append(toIndentedString(autoNavigation)).append("\n");
    sb.append("    envelopeIdStamping: ").append(toIndentedString(envelopeIdStamping)).append("\n");
    sb.append("    authoritativeCopy: ").append(toIndentedString(authoritativeCopy)).append("\n");
    sb.append("    notification: ").append(toIndentedString(notification)).append("\n");
    sb.append("    notificationUri: ").append(toIndentedString(notificationUri)).append("\n");
    sb.append("    enforceSignerVisibility: ").append(toIndentedString(enforceSignerVisibility)).append("\n");
    sb.append("    enableWetSign: ").append(toIndentedString(enableWetSign)).append("\n");
    sb.append("    allowMarkup: ").append(toIndentedString(allowMarkup)).append("\n");
    sb.append("    allowReassign: ").append(toIndentedString(allowReassign)).append("\n");
    sb.append("    createdDateTime: ").append(toIndentedString(createdDateTime)).append("\n");
    sb.append("    lastModifiedDateTime: ").append(toIndentedString(lastModifiedDateTime)).append("\n");
    sb.append("    deliveredDateTime: ").append(toIndentedString(deliveredDateTime)).append("\n");
    sb.append("    initialSentDateTime: ").append(toIndentedString(initialSentDateTime)).append("\n");
    sb.append("    sentDateTime: ").append(toIndentedString(sentDateTime)).append("\n");
    sb.append("    completedDateTime: ").append(toIndentedString(completedDateTime)).append("\n");
    sb.append("    voidedDateTime: ").append(toIndentedString(voidedDateTime)).append("\n");
    sb.append("    voidedReason: ").append(toIndentedString(voidedReason)).append("\n");
    sb.append("    deletedDateTime: ").append(toIndentedString(deletedDateTime)).append("\n");
    sb.append("    declinedDateTime: ").append(toIndentedString(declinedDateTime)).append("\n");
    sb.append("    statusChangedDateTime: ").append(toIndentedString(statusChangedDateTime)).append("\n");
    sb.append("    documentsCombinedUri: ").append(toIndentedString(documentsCombinedUri)).append("\n");
    sb.append("    certificateUri: ").append(toIndentedString(certificateUri)).append("\n");
    sb.append("    templatesUri: ").append(toIndentedString(templatesUri)).append("\n");
    sb.append("    messageLock: ").append(toIndentedString(messageLock)).append("\n");
    sb.append("    recipientsLock: ").append(toIndentedString(recipientsLock)).append("\n");
    sb.append("    recipients: ").append(toIndentedString(recipients)).append("\n");
    sb.append("    brandLock: ").append(toIndentedString(brandLock)).append("\n");
    sb.append("    brandId: ").append(toIndentedString(brandId)).append("\n");
    sb.append("    useDisclosure: ").append(toIndentedString(useDisclosure)).append("\n");
    sb.append("    emailSettings: ").append(toIndentedString(emailSettings)).append("\n");
    sb.append("    purgeState: ").append(toIndentedString(purgeState)).append("\n");
    sb.append("    lockInformation: ").append(toIndentedString(lockInformation)).append("\n");
    sb.append("    is21CFRPart11: ").append(toIndentedString(is21CFRPart11)).append("\n");
    sb.append("    isSignatureProviderEnvelope: ").append(toIndentedString(isSignatureProviderEnvelope)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

