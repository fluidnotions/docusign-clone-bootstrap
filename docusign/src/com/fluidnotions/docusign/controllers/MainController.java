package com.fluidnotions.docusign.controllers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.docusign.esignature.DocuSigAPIException;
import com.docusign.esignature.json.EnvelopeInformation;
import com.docusign.esignature.json.NewUser;
import com.docusign.esignature.json.NewUsers;
import com.docusign.esignature.json.RecipientInformation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluidnotions.docusign.models.AuthenticatingUser;
import com.fluidnotions.docusign.models.AutoPositionedRecipientModelRequest;
import com.fluidnotions.docusign.models.DocuSignUser;
import com.fluidnotions.docusign.models.RecipientModel;
import com.fluidnotions.docusign.models.SetupDocumentRequest;
import com.fluidnotions.docusign.services.DocuSignService;
import com.fluidnotions.docusign.services.Download;
import com.fluidnotions.opentaps.integration.docusign.DocuSignEntityUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



@RestController
public class MainController {

	@Autowired
	private Download download;

	@Autowired
	private DocuSignService docuSignService;

	@Autowired
	private ObjectMapper om;


	class DocuSignUsers{
		private DocuSignUser docuSignUser;
		private AuthenticatingUser authenticatingUser;
		public DocuSignUsers(DocuSignUser docuSignUser, AuthenticatingUser authenticatingUser) {
			super();
			this.docuSignUser = docuSignUser;
			this.authenticatingUser = authenticatingUser;
		}
		public DocuSignUser getDocuSignUser() {
			return docuSignUser;
		}
		public void setDocuSignUser(DocuSignUser docuSignUser) {
			this.docuSignUser = docuSignUser;
		}
		public AuthenticatingUser getAuthenticatingUser() {
			return authenticatingUser;
		}
		public void setAuthenticatingUser(AuthenticatingUser authenticatingUser) {
			this.authenticatingUser = authenticatingUser;
		}

	}


	/**
	 * Stand alone paging list. Demonstrates use of the wrapper
	 *
	 * @param pageSize            the page size
	 * @param pageIndex            the page index
	 * @param sqlStringOrTableName the sql string or table name
	 * @param tenantId the tenant id
	 * @return the JSON object string expected by the client pager
	 */
//	@RequestMapping(value = "/listResultSet", method = { RequestMethod.POST,
//			RequestMethod.GET })
//	public @ResponseBody String standAlonePagingList(
//			@RequestParam("pageSize") Integer pageSize,
//			@RequestParam("pageIndex") Integer pageIndex,
//			@RequestParam("sqlStringOrTableName") String sqlStringOrTableName,
//			@RequestParam("tenantId") String tenantId) {
//		PaginationWrapper pw = new PaginationWrapper(mainUrl, mainUsername,
//				mainPassword);
//		String json = null;
//		try {
//			json = pw.paginateTable(pageSize, pageIndex, sqlStringOrTableName,
//					tenantId);
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//
//		return json;
//	}

	@RequestMapping(value = "/getDocuSignUser", method = RequestMethod.POST)
	public Response getdocuSignUserEmail(
			@RequestParam("userLoginId") String userLoginId,
			@RequestParam("tenantKey") String tenantKey) throws JsonProcessingException {

		DocuSignUser dsu = DocuSignEntityUtil.lookupDocuSignUserEntity(tenantKey, userLoginId);
		AuthenticatingUser au = DocuSignEntityUtil.lookupAuthenticatingUserEntity(tenantKey);
		DocuSignUsers dsus = new DocuSignUsers(dsu, au);

		String docuSignUserEmail = (dsu != null) ? dsu.getEmail() : null;

		boolean enabled = (dsu != null) ? dsu.isEnabled() : false;
		return docuSignUserEmail != null ? new Response("success", docuSignUserEmail, om.writeValueAsString(dsus)):
		    				   new Response("failed", (!enabled ? "is disabled": "does not exist"), om.writeValueAsString(dsus));
	}

	@RequestMapping(value = "/getEnvelopeInformation", method = RequestMethod.GET)
	public EnvelopeInformation getEnvelopeInformation(
			@RequestParam("docuSignUserEmail") String docuSignUserEmail,
			@RequestParam("tenantKey") String tenantKey,
			@RequestParam("envelopeId") String envelopeId)
			throws MalformedURLException, IOException {

		EnvelopeInformation response = null;

		try {
			response = docuSignService.getEnvelopeInformation(tenantKey,
					docuSignUserEmail, envelopeId);
		} catch (DocuSigAPIException e) {
			e.printStackTrace();
		}

		return response;
	}

	@RequestMapping(value = "/getRecipientInformation", method = RequestMethod.GET)
	public RecipientInformation getRecipientInformation(
			@RequestParam("docuSignUserEmail") String docuSignUserEmail,
			@RequestParam("tenantKey") String tenantKey,
			@RequestParam("envelopeId") String envelopeId)
			throws MalformedURLException, IOException {
		RecipientInformation response = null;
		try {
			response =  docuSignService.getRecipientInformation(tenantKey,
					docuSignUserEmail, envelopeId);
		} catch (DocuSigAPIException e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping(value = "/getEnvelopeSummary", method = RequestMethod.GET)
	public Map<String, Object> getEnvelopeSummary(
			@RequestParam("userLoginId") String userLoginId,
			@RequestParam("docuSignUserEmail") String docuSignUserEmail,
			@RequestParam("tenantKey") String tenantKey,
			@RequestParam("envelopeId") String envelopeId)
			throws MalformedURLException, IOException {

		Map<String, Object> response = null;
		try {
			response = docuSignService.getCombinedEnvelopeRecipientInformation(tenantKey, userLoginId, docuSignUserEmail, envelopeId);
		} catch (DocuSigAPIException e) {
			e.printStackTrace();
		}

		return response;
	}

	class EnvelopeSummary {
		private RecipientInformation recipientInformation;
		private EnvelopeInformation envelopeInformation;

		public EnvelopeSummary(RecipientInformation recipientInformation,
				EnvelopeInformation envelopeInformation) {
			super();
			this.recipientInformation = recipientInformation;
			this.envelopeInformation = envelopeInformation;
		}

		/**
		 * @return the recipientInformation
		 */
		public RecipientInformation getRecipientInformation() {
			return recipientInformation;
		}

		/**
		 * @param recipientInformation
		 *            the recipientInformation to set
		 */
		public void setRecipientInformation(
				RecipientInformation recipientInformation) {
			this.recipientInformation = recipientInformation;
		}

		/**
		 * @return the envelopeInformation
		 */
		public EnvelopeInformation getEnvelopeInformation() {
			return envelopeInformation;
		}

		/**
		 * @param envelopeInformation
		 *            the envelopeInformation to set
		 */
		public void setEnvelopeInformation(
				EnvelopeInformation envelopeInformation) {
			this.envelopeInformation = envelopeInformation;
		}

	}

	//public Map<String, String> addSignerTabsAndAllowSenderToSignEmbedded(List<RecipientModel> recipientModels, String dynamicPdfTempFilePath, String afterSendRedirectCompleteUrl){
		@RequestMapping(value = "/autoPositionedSigner", method = RequestMethod.POST)
		public Response autoPositionedSigner(@RequestBody AutoPositionedRecipientModelRequest  autoPositioned)
				throws MalformedURLException, IOException {
			
			Gson gson = new Gson();
			RecipientModel r1 = gson.fromJson(autoPositioned.getFistRecipient(), RecipientModel.class);
			RecipientModel r2 = gson.fromJson(autoPositioned.getSecondRecipient(), RecipientModel.class);
			
			List<RecipientModel> RecipientModels = new ArrayList<RecipientModel>();
			RecipientModels.add(r1);
			RecipientModels.add(r2);
			autoPositioned.setRecipientModels(RecipientModels);
			
			String ts = new Long(new Date().getTime()).toString();
			String dst = autoPositioned.getTenantKey() + File.separator + autoPositioned.getDocuSignUserEmail()+ File.separator + ts;

			String serverTempDocPath = null;
			try {
				download.download(autoPositioned.getDynamicDocUrl(), dst, autoPositioned.getTitle(), autoPositioned.getMime(), autoPositioned.getTenantKey());
				serverTempDocPath = download.getFullFilePathForTempFile(dst);
			}catch(IOException e){
				e.printStackTrace();
				return new Response("failed", e.getCause().getMessage());
			}


			String senderResult = null;
			try {
				senderResult = docuSignService.addSignerTabsAndAllowSenderToSignEmbedded(autoPositioned.getTenantKey(),
						autoPositioned.getDocuSignUserEmail(), autoPositioned, serverTempDocPath);
			} catch (DocuSigAPIException e) {
				e.printStackTrace();
				return new Response("failed", e.getCause().getMessage());
			}

			if(senderResult != null){
			   
			    Debug.logInfo("senderResult: "+senderResult, this.getClass().getName());
			    return new Response("success",senderResult);
			}

			return new Response("failed", "Result doesn't contain sender view!");

		}




	@RequestMapping(value = "/setupDocument", method = RequestMethod.POST)
	public Response setupDocument(@RequestBody SetupDocumentRequest setupDocRequest) {

		String ts = new Long(new Date().getTime()).toString();
		String dst = setupDocRequest.getTenantKey() + File.separator + setupDocRequest.getDocuSignUserEmail()+ File.separator + ts;

		String serverTempDocPath = null;
		String embeddedSendingViewUrl = null;
		try {
			if (setupDocRequest.getIsDynamic().equals("true")) {
				download.download(setupDocRequest.getDocUrl(), dst, setupDocRequest.getDocName(), setupDocRequest.getMime(), setupDocRequest.getTenantKey());
				serverTempDocPath = download.getFullFilePathForTempFile(dst);
			} else {
				serverTempDocPath = setupDocRequest.getDocUrl();
			}

			embeddedSendingViewUrl = docuSignService
					.prepareEnvelopeForDocumentAndRequestSendingView(setupDocRequest.getTenantKey(),
							setupDocRequest.getDocuSignUserEmail(), serverTempDocPath, setupDocRequest.getDocName(), setupDocRequest.getEmailSubject(),
							setupDocRequest.getEmailBody(), setupDocRequest.getAfterSendRedirectUrl());

		} catch (IOException e) {
			e.printStackTrace();
			return new Response("failed", e.getCause().getMessage());
		} catch (DocuSigAPIException e) {
			e.printStackTrace();
			return new Response("failed", e.getMessage());
		}

		return new Response("success", embeddedSendingViewUrl);
	}

	class Response {

		String status;
		String response;
		String otherJson;

		public Response(String status, String response) {
			this(status, response, null);
		}

		public Response(String status, String response, String otherJson) {
			super();
			this.status = status;
			this.response = response;
			this.otherJson = otherJson;
		}

		public String getStatus() {
			return status;
		}

		public String getResponse() {
			return response;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public void setResponse(String response) {
			this.response = response;
		}

		public String getOtherJson() {
			return otherJson;
		}

		public void setOtherJson(String otherJson) {
			this.otherJson = otherJson;
		}


	}

	@RequestMapping(value = "/addNewUsers", method = RequestMethod.POST)
	public NewUsers addUserToTenantAccount(
			@RequestParam("tenantKey") String tenantKey,
			@RequestParam("firstName") String first,
			@RequestParam("lastName") String second,
			@RequestParam("email") String email,
			@RequestParam("userLoginId") String targetUserLoginId)
			throws MalformedURLException, IOException {

		NewUser user = docuSignService.getDefaultNewUserInstance();
		String password = UtilProperties.getPropertyValue("docusign", "defaultNewDocuSignPwd");
	    String name = (first != null? first: "")+(second != null? " "+second:"");
		user.withUserName(name).withEmail(email).withPassword(password);

		List<NewUser> newUsers = new ArrayList<NewUser>();
		newUsers.add(user);
		NewUsers response = null;
		try {
			response = docuSignService.addNewUsers(tenantKey, targetUserLoginId,
					newUsers);
		} catch (DocuSigAPIException e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping(value = "/disableUser", method = RequestMethod.POST)
	public Response disableUser(
			@RequestParam("tenantKey") String tenantKey,
			@RequestParam("userLoginId") String targetUserLoginId,
			@RequestParam("close") String closeDocusignUser,
			@RequestParam("void") String voidInProgressEnvelopes)
			throws MalformedURLException, IOException {

		boolean closed = false, voided = false;
		String responseMessage = "";
		try {
			if (voidInProgressEnvelopes.equals("on")) {
				voided = docuSignService.voidInProgressEnvelopesForUser(tenantKey,
						targetUserLoginId);
				responseMessage += targetUserLoginId
						+ (voided ? " user's in-progress envelopes were voided."
								: " user's in-progress envelopes could not be voided.");
			} else {
				voided = true;// not required
			}
			if (closeDocusignUser.equals("on")) {
				String result = docuSignService.closeDocuSignUser(tenantKey,
						targetUserLoginId);
				closed = (result == null);
				responseMessage += "\n "+targetUserLoginId
						+ (closed ? " was closed successfully."
								: " could not be closed. Error Message: " + result);
			} else {
				closed = true;// not required
			}
		} catch (DocuSigAPIException e) {
			e.printStackTrace();
			responseMessage = e.getMessage();
		}

		if(closed || voided){
			DocuSignEntityUtil.disableDocuSignUserEntity(tenantKey, targetUserLoginId);
		}

		return (closed && voided ? new Response("success", responseMessage)
				: new Response("failed", responseMessage));
	}


}
