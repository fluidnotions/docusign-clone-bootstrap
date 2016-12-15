package com.fluidnotions.docusign.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.docusign.esignature.DocuSigAPIException;
import com.docusign.esignature.DocuSignClient;
import com.docusign.esignature.json.Document;
import com.docusign.esignature.json.EnvelopeInformation;
import com.docusign.esignature.json.EnvelopesStatusChanged;
import com.docusign.esignature.json.GroupList;
import com.docusign.esignature.json.NewUser;
import com.docusign.esignature.json.NewUsers;
import com.docusign.esignature.json.RecipientInformation;
import com.docusign.esignature.json.RequestSignatureFromDocuments;
import com.fasterxml.jackson.core.JsonParseException;
import com.fluidnotions.docusign.models.AuthenticatingUser;
import com.fluidnotions.docusign.models.AutoPositionedRecipientModelRequest;
import com.fluidnotions.docusign.models.DocuSignUser;
import com.fluidnotions.opentaps.integration.docusign.DocuSignEntityUtil;


@Service
public class DocuSignService {

    private static final String module = DocuSignService.class.getName();
    private static Map<String, DocuSignClient> tenantToClientMap = new HashMap<String, DocuSignClient>();
    private static ObjectMapper mapper = new ObjectMapper();
    private static String newUser_groupId;
    private static String newUser_groupName;
    private static String newUser_permissionProfileId;
    private static String newUser_groupType;
    
    static{
	newUser_groupId = UtilProperties.getPropertyValue("docusign", "groupId");
	newUser_groupName = UtilProperties.getPropertyValue("docusign", "groupName");
	newUser_permissionProfileId = UtilProperties.getPropertyValue("docusign", "permissionProfileId");
	newUser_groupType = UtilProperties.getPropertyValue("docusign", "groupType");
    }

    @Autowired
    private ApplicationContext context;

    private DocuSignClient tenantClient(String tenantKey) {

        if (!tenantToClientMap.containsKey(tenantKey)) {
            try {

                DocuSignClient docuSignClient = new DocuSignClient();

                AuthenticatingUser authUser = DocuSignEntityUtil.lookupAuthenticatingUserEntity(tenantKey);

                docuSignClient.init(authUser);

                tenantToClientMap.put(tenantKey, docuSignClient);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return tenantToClientMap.get(tenantKey);
    }

    // unsure if the alternative bellow approach works.. anyway I already
    // implemented this before
    // I even spotted that method
    public String addSignerTabsAndAllowSenderToSignEmbedded(
            String tenantKey,
            String docuSignUserEmail,
            AutoPositionedRecipientModelRequest autoPos,
            String dynamicPdfTempFilePath) throws DocuSigAPIException {

        DocuSignClient client = tenantClient(tenantKey);
        try {
            client.login();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new DocuSigAPIException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new DocuSigAPIException(e.getMessage());
        }
        String result = null;
        try {
            //so here we are setting on behald as and since we are now passing the whole client and calling getHeader from within behavior class we should get the right header
            result = client.asUser(docuSignUserEmail).addSignerTabsAndAllowSenderToSignEmbedded(dynamicPdfTempFilePath, autoPos.getEmailSubject(), autoPos.getEmailblurb(), client.getAfterSendRedirectUrl() + autoPos.getAfterSendRedirectUrl(), autoPos.getRecipientModels());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new DocuSigAPIException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new DocuSigAPIException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DocuSigAPIException(e.getMessage());
        }
        return result;
    }

    // public ViewUrl addSignerTabsAndAllowSenderToSignEmbedded(String
    // tenantKey,
    // String docuSignUserEmail, String dynamicPdfTempFilePath,
    // Map<String, String> nameToEmail, String afterSendRedirectCompleteUrl) {
    // DocuSignClient client = tenantClient(tenantKey);
    // try {
    // client.login();
    // } catch (MalformedURLException e) {
    //
    // e.printStackTrace();
    // } catch (IOException e) {
    //
    // e.printStackTrace();
    // } catch (DocuSigAPIException e) {
    //
    // e.printStackTrace();
    // }
    // try {
    // return client.asUser(docuSignUserEmail)
    // .addSignerTabsAndAllowSenderToSignEmbedded(
    // dynamicPdfTempFilePath, nameToEmail,
    // afterSendRedirectCompleteUrl);
    // } catch (MalformedURLException e) {
    //
    // e.printStackTrace();
    // } catch (IOException e) {
    //
    // e.printStackTrace();
    // } catch (DocuSigAPIException e) {
    //
    // e.printStackTrace();
    // } catch (ApiException e) {
    //
    // e.printStackTrace();
    // }
    // return null;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see com.fluidnotions.docusign.services.InterFaceDocuSignService#
     * getEnvelopeInformation(java.lang.String, java.lang.String,
     * java.lang.String)
     */

    public EnvelopeInformation getEnvelopeInformation(
            String tenantKey,
            String docuSignUserEmail,
            String envelopeId) throws MalformedURLException, IOException, DocuSigAPIException {
        return tenantClient(tenantKey).asUser(docuSignUserEmail).requestEnvelopeInformation(envelopeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fluidnotions.docusign.services.InterFaceDocuSignService#
     * getRecipientInformation(java.lang.String, java.lang.String,
     * java.lang.String)
     */

    public RecipientInformation getRecipientInformation(
            String tenantKey,
            String docuSignUserEmail,
            String envelopeId) throws MalformedURLException, IOException, DocuSigAPIException {
        return tenantClient(tenantKey).asUser(docuSignUserEmail).requestRecipientInformation(envelopeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fluidnotions.docusign.services.InterFaceDocuSignService#
     * getCombinedEnvelopeRecipientInformation(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */

    public Map<String, Object> getCombinedEnvelopeRecipientInformation(
            String tenantKey,
            String userLoginId,
            String docuSignUserEmail,
            String envelopeId) throws MalformedURLException, IOException, DocuSigAPIException {
        RecipientInformation ri = tenantClient(tenantKey).asUser(docuSignUserEmail).requestRecipientInformation(envelopeId);
        EnvelopeInformation ei = tenantClient(tenantKey).asUser(docuSignUserEmail).requestEnvelopeInformation(envelopeId);
        Map<String, Object> combined = new HashMap<String, Object>();
        combined.put("envelopeInformation", ei);
        combined.put("recipientInformation", ri);
        // intercept and store to OT entity - just EnvelopeInformation since it
        // contains uri link to RecipientInformation which can be obtained with
        // client.getUri() call
        DocuSignEntityUtil.createEnvelope(tenantKey, userLoginId, docuSignUserEmail, ei);

        return combined;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fluidnotions.docusign.services.InterFaceDocuSignService#
     * prepareEnvelopeForDocumentAndRequestSendingView(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */

    public String prepareEnvelopeForDocumentAndRequestSendingView(
            String tenantKey,
            String docuSignUserEmail,
            String serverTempDocPath,
            String docName,
            String emailSubject,
            String emailBody,
            String afterSendRedirectUrl)
            throws MalformedURLException,
            IOException,
            DocuSigAPIException {
        String embeddedSendingViewUrl = null;

        RequestSignatureFromDocuments request = new RequestSignatureFromDocuments();

        Document document = new Document();
        document.setName(docName);
        document.setDocumentId("1");
        List<Document> documents = Arrays.asList(document);

        request.setDocuments(documents);
        request.setEmailSubject(emailSubject);// title
        request.setEmailBlurb(emailBody);
        request.setStatus("created"); // "sent" to send, "created" to save as
                                      // draft in cloud ready to add other
                                      // details in embedded Sending View

        File doc = new File(serverTempDocPath);
        InputStream[] files = new FileInputStream[] { new FileInputStream(doc) };
        // FIXME this call seems to require admin permissions so we don't use
        // asUser() on it
        DocuSignClient client = tenantClient(tenantKey);
        String envelopeId = client.asUser(docuSignUserEmail).requestSignatureFromDocuments(request, files);
        Debug.logInfo("prepareEnvelopeForDocumentAndRequestSendingView with envelopeId: " + envelopeId, module);
        if (envelopeId != null) {
            embeddedSendingViewUrl = tenantClient(tenantKey).asUser(docuSignUserEmail).requestSendingView(envelopeId, client.getAfterSendRedirectUrl() + afterSendRedirectUrl);
        }
        Debug.logInfo("embeddedSendingViewUrl for Envelope with envelopeId = " + envelopeId + " is " + embeddedSendingViewUrl, module);
        return embeddedSendingViewUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.fluidnotions.docusign.services.InterFaceDocuSignService#addNewUsers
     * (java.lang.String, java.lang.String, java.util.List)
     */

    public NewUsers addNewUsers(String tenantKey, String targetUserLoginId, List<NewUser> newUsers)
            throws MalformedURLException,
            IOException,
            DocuSigAPIException {
	assert(newUsers.size() == 1);
	NewUser newUserInput = newUsers.get(0);
        DocuSignClient client = tenantClient(tenantKey);
        String newUserName = newUserInput.getUserName();
        client.login();
        GroupList groupListObj = new GroupList();
        //DocuSignService
        groupListObj.setGroupId(DocuSignService.newUser_groupId);
        groupListObj.setGroupName(DocuSignService.newUser_groupName);
        groupListObj.setGroupType(DocuSignService.newUser_groupType);
        groupListObj.setPermissionProfileId(DocuSignService.newUser_permissionProfileId);
        List<GroupList> groupList = new ArrayList<GroupList>();
        groupList.add(groupListObj);
        
        NewUsers newUsersObj = new NewUsers();
        
        newUserInput.setGroupList(groupList);
        newUsersObj.setNewUsers(newUsers);
        NewUsers response = tenantClient(tenantKey).addNewUsers(newUsersObj);
        // if created successfully there with be a userId, then we need to save
        // to OT entity
        if (response.getNewUsers().get(0).getUserId() != null) {
            DocuSignUser dsu = new DocuSignUser(targetUserLoginId, response.getNewUsers().get(0).getUserId(), response.getNewUsers().get(0).getEmail(), newUserName, true);
            DocuSignEntityUtil.createDocuSignUserEntity(tenantKey, dsu);
        }

        return response;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fluidnotions.docusign.services.InterFaceDocuSignService#
     * getDefaultNewUserInstance()
     */

    public NewUser getDefaultNewUserInstance()
            throws JsonParseException,
            IOException {
        URL url = this.getClass().getResource("NewUserDefaults.json");
        File userDefaults = new File(url.getFile());
        return mapper.readValue(userDefaults, NewUser.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fluidnotions.docusign.services.InterFaceDocuSignService#
     * getEnvelopesStatusChangesForTenantAccount(java.lang.String,
     * java.lang.String)
     */

    public EnvelopesStatusChanged getEnvelopesStatusChangesForTenantAccount(
            String tenantKey,
            String fromDateTime) throws MalformedURLException, IOException, DocuSigAPIException {
        DocuSignClient client = tenantClient(tenantKey);
        client.login();
        return client.getEnvelopesStatusChanged(fromDateTime);

    }

    // check for envelope status changes every 5 minutes with a 1 minute overlap
    // to ensure that no
    // changes are missed from the previous request.
    // So our period is set to 5 min (recommend is 15 min per account - might
    // not exceed limit is small number of changes)
    // therefore our From should be 6 min ago.
    /*
     * (non-Javadoc)
     * 
     * @see com.fluidnotions.docusign.services.InterFaceDocuSignService#
     * calculateFromDateTime(int, java.lang.String)
     */

    public String calculateFromDateTime(int periodInMin, String apiTimezone) {

        TimeZone timeZone = TimeZone.getTimeZone(apiTimezone);
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US);
        simpleDateFormat.setTimeZone(timeZone);
        //Debug.logInfo(apiTimezone + "(api timezone) time now is: " + simpleDateFormat.format(calendar.getTime()), this.getClass().getName());
        // 1 min overlap
        calendar.add(Calendar.MINUTE, (periodInMin + 1) * -1);

        return simpleDateFormat.format(calendar.getTime());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.fluidnotions.docusign.services.InterFaceDocuSignService#closeDocuSignUser
     * (java.lang.String, java.lang.String)
     */

    public String closeDocuSignUser(String tenantKey, String targetUserLoginId)
            throws MalformedURLException,
            IOException,
            DocuSigAPIException {

        String result = null;
        DocuSignUser userEntity = DocuSignEntityUtil.lookupDocuSignUserEntity(tenantKey, targetUserLoginId);

        if (userEntity != null) {
            String docuSignUserEmail = userEntity.getDocuSignUserId();
            DocuSignClient client = tenantClient(tenantKey);
            client.login();
            // null result indicates there where no errors therefore user was
            // closed successfully
            if (docuSignUserEmail != null) {
                result = client.closeUser(docuSignUserEmail);
            }
        } else {
            result = "No enabaled DocuSignUser found for userLoginId: " + targetUserLoginId;
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fluidnotions.docusign.services.InterFaceDocuSignService#
     * voidInProgressEnvelopesForUser(java.lang.String, java.lang.String)
     */

    public boolean voidInProgressEnvelopesForUser(String tenantKey, String targetUserLoginId)
            throws MalformedURLException,
            IOException,
            DocuSigAPIException {
        List<String> inProgressEnvelopesForUser = DocuSignEntityUtil.lookupInProgressEnvelopesForUser(tenantKey, targetUserLoginId);
        List<String> errors = new ArrayList<String>();
        boolean result = true;
        if (inProgressEnvelopesForUser != null && inProgressEnvelopesForUser.size() > 0) {
            DocuSignClient client = tenantClient(tenantKey);
            client.login();
            for (String envelopeId : inProgressEnvelopesForUser) {
                if (!client.voidEnvelope(envelopeId, "sender user was disabled.")) {
                    errors.add(envelopeId);
                    result = false;
                }

            }
        }
        if (!result) {
            Debug.logError("The following in-progress envelopes, could not be closed:", this.getClass().getName());
            for (String err : errors) {
                Debug.logError(err, this.getClass().getName());
            }

        }
        return result;
    }

}
