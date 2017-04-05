package com.fluidnotions.opentaps.integration.docusign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transaction;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.DelegatorFactory;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.transaction.GenericTransactionException;
import org.ofbiz.entity.transaction.TransactionUtil;

import com.docusign.esignature.json.Envelope;
import com.docusign.esignature.json.EnvelopeInformation;
import com.docusign.esign.model.Signer;
import com.fluidnotions.docusign.models.AuthenticatingUser;
import com.fluidnotions.docusign.models.DocuSignUser;

public class DocuSignEntityUtil {

	private static final String module = DocuSignEntityUtil.class.getName();

    public static List<String> getTenantKeyList() {

		List<String> keys = new ArrayList<String>();
		try {

			Delegator delegator = DelegatorFactory.getDelegator("default");
			List<GenericValue> gvs = delegator.findByAnd(
					"DocuSignAuthenticatingUser", "enabled", "Y");
			for (GenericValue gv : gvs) {
				keys.add(gv.getString("tenantKey"));
			}

		} catch (GenericEntityException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return keys;
	}

	// in-progress are all non-terminal states eg: Completed, Declined, Void
	public static List<String> lookupInProgressEnvelopesForUser(
			String tenantKey, String targetUserLoginId) {

		List<String> targetEnvelopeIds = new ArrayList<String>();
		try {
			Delegator delegator = getTenantDelegator(tenantKey);
			EntityCondition userCondition = EntityCondition.makeCondition(
					"senderUserLoginId", targetUserLoginId);
			EntityCondition statusCondition = EntityCondition.makeCondition(
					EntityOperator.OR,
					EntityCondition.makeCondition("status", "sent"),
					EntityCondition.makeCondition("status", "delivered")

			);
			EntityCondition conditions = EntityCondition.makeCondition(
					EntityOperator.AND, userCondition, statusCondition);

			List<GenericValue> gvs = delegator.findByAnd("DocuSignEnvelope",
					conditions);
			for (GenericValue gv : gvs) {
				targetEnvelopeIds.add(gv.getString("envelopeId"));
			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return targetEnvelopeIds;
	}

	public static AuthenticatingUser lookupAuthenticatingUserEntity(
			String tenantKey) {
		AuthenticatingUser entity = null;
		try {

			Delegator delegator = DelegatorFactory.getDelegator("default");
			GenericValue gv = delegator.findByPrimaryKey(
					"DocuSignAuthenticatingUser",
					UtilMisc.toMap("tenantKey", tenantKey));
			if (gv != null) {
				entity = new AuthenticatingUser(gv.getString("username"),
						gv.getString("password"),
						gv.getString("integratorKey"),
						gv.getString("afterSendRedirectUrlBase"));
			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return entity;
	}

	public static DocuSignUser lookupDocuSignUserEntity(String tenantKey,
			String userLoginId) {
		DocuSignUser entity = null;
		try {
			Delegator delegator = getTenantDelegator(tenantKey);
			GenericValue gv = delegator.findByPrimaryKey("DocuSignUser",
					UtilMisc.toMap("userLoginId", userLoginId));
			
			if(gv!=null){
//				 DocuSignUser(String userLoginId, String docuSignUserId,String email, String name, boolean enabled) 
				entity = new DocuSignUser(gv.getString("userLoginId"), 
						gv.getString("docuSignUserId"), 
						gv.getString("email"), 
						gv.getString("name"), 
						gv.getString("enabled").equals("Y"));
			}else{
				return null;
			}

		} catch (GenericEntityException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return entity;
	}

	public static void createDocuSignUserEntity(String tenantKey,
			DocuSignUser dsu) {
		try {
			Delegator delegator = getTenantDelegator(tenantKey);
			GenericValue gv = delegator.makeValue("DocuSignUser", UtilMisc
					.toMap("userLoginId", dsu.getUserLoginId(),
							"docuSignUserId", dsu.getDocuSignUserId(), "email",
							dsu.getEmail(), "name",
							dsu.getName(), "enabled", "Y"));
			delegator.create(gv);
		} catch (GenericEntityException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public static void disableDocuSignUserEntity(String tenantKey,
			String targetUserLoginId) {
		try {
			Delegator delegator = getTenantDelegator(tenantKey);
			GenericValue gv = delegator.makeValue("DocuSignUser", UtilMisc
					.toMap("userLoginId", targetUserLoginId, "enabled", "N"));
			delegator.store(gv);
		} catch (GenericEntityException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	// entity envelope is Envelope (which is updated by scheduled
	// getEnvelopesStatusChanged call per tenent) from
	// EnvelopeInformation (which is the same with some extra fields) along with
	// a few additional id fields get OT
	// userLoginId and docuSignUserId
	public static void createEnvelope(String tenantKey, String userLoginId,
			String docuSignUserEmail, EnvelopeInformation ei) {
		try {
			Delegator delegator = getTenantDelegator(tenantKey);

			Map<String, String> newEntity = new HashMap<String, String>();
			newEntity.put("senderUserLoginId", userLoginId);
			newEntity.put("docuSignUserEmail", docuSignUserEmail);
			newEntity.put("emailSubject", ei.getEmailSubject());
			newEntity.put("status", ei.getStatus());
			newEntity.put("documentsUri", ei.getDocumentsUri());
			newEntity.put("recipientsUri", ei.getRecipientsUri());
			newEntity.put("envelopeUri", ei.getEnvelopeUri());
			newEntity.put("envelopeId", ei.getEnvelopeId());
			newEntity.put("customFieldsUri", ei.getCustomFieldsUri());
			newEntity.put("notificationUri", ei.getNotificationUri());
			newEntity.put("statusChangedDateTime",
					ei.getStatusChangedDateTime());
			newEntity.put("documentsCombinedUri", ei.getDocumentsCombinedUri());
			newEntity.put("certificateUri", ei.getCertificateUri());
			newEntity.put("templatesUri", ei.getTemplatesUri());

			GenericValue gv = delegator
					.makeValue("DocuSignEnvelope", newEntity);
			delegator.create(gv);
		} catch (GenericEntityException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public static void storeEnvelopeUpdates(String tenantKey, List<Envelope> envelopes) {
		//Debug.logInfo("OTUtil storeEnvelopeUpdates: " + envelopes.size(), DocuSignEntityUtil.class.getName());

		Delegator delegator = getTenantDelegator(tenantKey);
//		List<GenericValue> values = new ArrayList<GenericValue>();
		 // need a nested transaction to insure failures to not bubble up and
        // prevent a response
        Transaction parentTx = null;
        boolean beganTransaction = false;

        try {
            try {
                parentTx = TransactionUtil.suspend();
            } catch (GenericTransactionException e) {
                Debug.logError(e, "Could not suspend transaction: " + e.getMessage(), module);
            }

            try {
                beganTransaction = TransactionUtil.begin();
                for (Envelope e : envelopes) {
                    try {
                        Debug.logInfo("EnvelopeId: " + e.getEnvelopeId(),
                                DocuSignEntityUtil.class.getName());

                        Map<String, String> updateEntity = new HashMap<String, String>();
                        updateEntity.put("envelopeId", e.getEnvelopeId());
                        updateEntity.put("status", e.getStatus());
                        updateEntity.put("documentsUri", e.getDocumentsUri());
                        updateEntity.put("recipientsUri", e.getRecipientsUri());
                        updateEntity.put("envelopeUri", e.getEnvelopeUri());
                        updateEntity.put("customFieldsUri", e.getCustomFieldsUri());
                        updateEntity.put("notificationUri", e.getNotificationUri());
                        updateEntity.put("statusChangedDateTime",
                                e.getStatusChangedDateTime());
                        updateEntity.put("documentsCombinedUri",
                                e.getDocumentsCombinedUri());
                        updateEntity.put("certificateUri", e.getCertificateUri());
                        updateEntity.put("templatesUri", e.getTemplatesUri());

                        GenericValue gv = delegator.makeValue("DocuSignEnvelope",
                                updateEntity);
//                        values.add(gv);
                        delegator.store(gv);

                    }catch (Exception ex) {
                        ex.printStackTrace();
                    } 
                }
//                This is different than the normal store method in that the store method only does an update, 
//                while the storeAll method checks to see if each entity exists, then either does an insert or an 
//                update as appropriate. 
//				  NOTE: since we only want to update already existing envolopes created after integration -- we can't use this!
//                delegator.storeAll(values);

                
            } catch (GenericEntityException e) {
                e.printStackTrace();
            } finally {
                try {
                    TransactionUtil.commit(beganTransaction);
                } catch (GenericTransactionException e) {
                    Debug.logError(e, "Could not commit nested transaction: " + e.getMessage(), module);
                }
            }
        } finally {
            // resume/restore parent transaction
            if (parentTx != null) {
                try {
                    TransactionUtil.resume(parentTx);
                    Debug.logVerbose("Resumed the parent transaction.", module);
                } catch (GenericTransactionException e) {
                    Debug.logError(e, "Could not resume parent nested transaction: " + e.getMessage(), module);
                }
            }
        }
	
	}

	private static final String propFileName = "docusign.properties";

	public static String getPropety(String propName) {
		return UtilProperties.getPropertyValue(propFileName, propName);
	}

	public static Delegator getTenantDelegator(String tenantKey) {

		Delegator delegator = DelegatorFactory.getDelegator("default");
		Delegator tenantDelegator = null;
		
		try {
	
			List<GenericValue> gvs = delegator.findByAnd("Tenant", "loginUrlKey", tenantKey.trim());
			if(gvs.size()>0){
				String tenantId = gvs.get(0).getString("tenantId");
				tenantDelegator = DelegatorFactory.getDelegator("default#"+ tenantId);
			}else{
				tenantDelegator = DelegatorFactory.getDelegator("default#"+ tenantKey);
			}
					
			
		} catch (GenericEntityException e) {
			Debug.logError(e, DocuSignEntityUtil.class.getName());
		} catch (Exception e) {
			Debug.logError(e, DocuSignEntityUtil.class.getName());
		}

		return tenantDelegator != null ? tenantDelegator : delegator;
	}

	public static void storeEnvelopeSignerStatusUpdates(String tenantKey, String envelopeId, List<Signer> signers) {
		Delegator delegator = getTenantDelegator(tenantKey);
		List<GenericValue> values = new ArrayList<GenericValue>();
		 // need a nested transaction to insure failures to not bubble up and
        // prevent a response
        Transaction parentTx = null;
        boolean beganTransaction = false;

        try {
            try {
                parentTx = TransactionUtil.suspend();
            } catch (GenericTransactionException e) {
                Debug.logError(e, "Could not suspend transaction: " + e.getMessage(), module);
            }

            try {
                beganTransaction = TransactionUtil.begin();
                for (Signer s : signers) {
                    try {
                        Debug.logInfo("storeEnvelopeSignerStatusUpdates: EnvelopeId: " + envelopeId,
                                DocuSignEntityUtil.class.getName());

                        Map<String, String> updateEntity = new HashMap<String, String>();
                        updateEntity.put("envelopeId", envelopeId);
                        updateEntity.put("isBulkRecipient", s.getIsBulkRecipient());
                        updateEntity.put("name", s.getName());
                        updateEntity.put("email", s.getEmail());
                        updateEntity.put("recipientId", s.getRecipientId());
                        updateEntity.put("recipientIdGuid", s.getRecipientIdGuid());
                        updateEntity.put("requireIdLookup", s.getRequireIdLookup());
                        updateEntity.put("userId", s.getUserId());
                        updateEntity.put("routingOrder", s.getRoutingOrder());
                        updateEntity.put("status", s.getStatus());
                        updateEntity.put("signedDateTime", s.getSignedDateTime());
                        updateEntity.put("deliveredDateTime", s.getDeliveredDateTime());
                        
                        GenericValue gv = delegator.makeValue("DocuSignEnvelopeSignerStatus",
                                updateEntity);
                        values.add(gv);

                    }catch (Exception ex) {
                        ex.printStackTrace();
                    } 
                }
                
                delegator.storeAll(values);

                
            } catch (GenericEntityException e) {
                e.printStackTrace();
            } finally {
                try {
                    TransactionUtil.commit(beganTransaction);
                } catch (GenericTransactionException e) {
                    Debug.logError(e, "Could not commit nested transaction: " + e.getMessage(), module);
                }
            }
        } finally {
            // resume/restore parent transaction
            if (parentTx != null) {
                try {
                    TransactionUtil.resume(parentTx);
                    Debug.logVerbose("Resumed the parent transaction.", module);
                } catch (GenericTransactionException e) {
                    Debug.logError(e, "Could not resume parent nested transaction: " + e.getMessage(), module);
                }
            }
        }
		
	}

}
