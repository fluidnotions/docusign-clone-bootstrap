package com.groupfio.docusign;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.ofbiz.base.util.Debug;

import com.docusign.esign.api.AuthenticationApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.Configuration;
import com.docusign.esign.model.DateSigned;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.FullName;
import com.docusign.esign.model.LoginInformation;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.ViewUrl;
import com.docusign.esignature.DocuSignClient;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fluidnotions.docusign.models.RecipientModel;
import com.google.gson.Gson;

public class AutoPositionedSignerHelper {

	ObjectMapper om;
	RecipientModel embeddedSigner;
	String basePath = "https://demo.docusign.net/restapi";
	DocuSignClient usedToGetAuthHeader;

	public AutoPositionedSignerHelper(String basePath, DocuSignClient usedToGetAuthHeader) {
	    if(!basePath.endsWith("/restapi")){
		    basePath += "/restapi";
		}
		this.basePath = basePath;
		this.usedToGetAuthHeader = usedToGetAuthHeader;
	}

	public static AutoPositionedSignerHelper newInstance(String basePath, DocuSignClient usedToGetAuthHeader) {
		return new AutoPositionedSignerHelper(basePath, usedToGetAuthHeader);
	}
	
	public String applyAutoPositionedSignTabsToDoc(String filePath,
			String emailSubject, String emailBody, String afterSendRedirectUrl, List<RecipientModel> signers) throws IOException {

		double llx = 0.0;
		double lly = 0.0;
		double wdt = 0.0;
		double hdt = 0.0;
		// initialize client for desired environment and add
		// X-DocuSign-Authentication header
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(basePath);

		// configure 'X-DocuSign-Authentication' authentication header
		//String authHeader = "{\"Username\":\"" + username + "\",\"Password\":\"" + password + "\",\"IntegratorKey\":\"" + integratorKey + "\"}";
		//we should be using send on behalf as since the sender is the docusign user, asUser must therefore be used in controller or service call level
		apiClient.addDefaultHeader("X-DocuSign-Authentication", usedToGetAuthHeader.getAuthHeader());
		Configuration.setDefaultApiClient(apiClient);
		try {
			AuthenticationApi authApi = new AuthenticationApi();
			LoginInformation loginInfo = authApi.login();

			// parse first account ID (user might belong to multiple accounts)
			String accountId = loginInfo.getLoginAccounts().get(0).getAccountId();

			// create a new envelope to manage the signature request
			EnvelopeDefinition envDef = new EnvelopeDefinition();
			envDef.setEmailSubject(emailSubject);
			envDef.setEmailBlurb(emailBody);

			File pdfFile = new File(filePath);
			PDDocument pdfDocObject = null;
			try {

				pdfDocObject = PDDocument.load(pdfFile);
				PDPage doc = pdfDocObject.getPage(pdfDocObject.getNumberOfPages() - 1);
				llx = new Float(doc.getCropBox().getLowerLeftX()).doubleValue();
				lly = new Float(doc.getCropBox().getLowerLeftY()).doubleValue();
				wdt = new Float(doc.getCropBox().getWidth()).doubleValue();
				hdt = new Float(doc.getCropBox().getHeight()).doubleValue();
				Debug.log("llx: " + llx + ", lly: " + lly + ", wdt: " + wdt);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}finally{
				pdfDocObject.close();
			}

			// add a document to the envelope
			Document doc = new Document();
			doc.setDocumentBase64(encodeFileToBase64Binary(pdfFile));
			doc.setName(pdfFile.getName()); // can be different from actual file
											// name
			doc.setDocumentId("1");

			List<Document> docs = new ArrayList<Document>();
			docs.add(doc);
			envDef.setDocuments(docs);
			String lastPageNumber = new Integer(pdfDocObject.getNumberOfPages()).toString();
			Debug.log("lastPageNumber: " + lastPageNumber);
			envDef.setRecipients(new Recipients());
			envDef.getRecipients().setSigners(new ArrayList<Signer>());

			int i = 0;
			for (RecipientModel mms : signers) {
				// add a recipient to sign the document, identified by name and
				// email we used above
				// while leaving clientUserId out means that the signature
				// request will be sent by email

				if (i == 0) {
					mms.setxPos(llx + (wdt * 0.3));
					mms.setyPos(lly + (hdt * 0.8));
					
					addDefaultSigner(envDef, lastPageNumber, mms, true);
				} else if (i == 1) {
					mms.setxPos(llx + (wdt * 0.6));
					mms.setyPos(lly + (hdt * 0.8));
					
					addDefaultSigner(envDef, lastPageNumber, mms, false);
				}
				i++;
			}

			// send the envelope by setting |status| to "sent". To save as a
			// draft set to "created"
			envDef.setStatus("sent");

			try {
				Debug.log(getObjectMapper().writeValueAsString(envDef));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// instantiate a new EnvelopesApi object
			EnvelopesApi envelopesApi = new EnvelopesApi();

			// call the createEnvelope() API
			EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envDef);

			Debug.log("EnvelopeSummary: " + envelopeSummary);

			RecipientViewRequest returnUrl2 = new RecipientViewRequest();
			returnUrl2.setReturnUrl(afterSendRedirectUrl);
			returnUrl2.setAuthenticationMethod("email");
//
//			// recipient information must match embedded recipient info we
//			// provided in step #2
			returnUrl2.setEmail(signers.get(0).getSignerEmail());
			returnUrl2.setUserName(signers.get(0).getSignerName());
			returnUrl2.setRecipientId(signers.get(0).getRecipientId());
			returnUrl2.setClientUserId(signers.get(0).getClientUserId());
//
//			// call the CreateRecipientView API
			ViewUrl recipientView = envelopesApi.createRecipientView(accountId, envelopeSummary.getEnvelopeId(), returnUrl2);

			Map<String, String> senderResult = new HashMap<String, String>();
			senderResult.put("senderRecipientUrl", recipientView.getUrl());
//			added envelopeId for after dialog/iframe summary call which also creates new initial envelope record
			senderResult.put("envelopeId", envelopeSummary.getEnvelopeId());
			
			return new Gson().toJson(senderResult);

		} catch (com.docusign.esign.client.ApiException ex) {
			Debug.log("Exception: " + ex);
		
		}
		return null;
	}

	

	public void addDefaultSigner(EnvelopeDefinition envDef, String lastPageNumber, RecipientModel ms, boolean embedded) {
		Tabs tabs = new Tabs();

		Signer signer = new Signer();
		signer.setEmail(ms.getSignerEmail());
		signer.setName(ms.getSignerName());
		signer.setRecipientId(ms.getRecipientId());
		signer.setRoutingOrder(ms.getRoutingOrder());

		// Must set |clientUserId| for embedded recipients and provide the same
		// value when requesting
		// the recipient view URL in the next step
		if (embedded)
			signer.setClientUserId(ms.getClientUserId());

		DateSigned dateSigned = new DateSigned();
		dateSigned.setDocumentId("1");
		dateSigned.setPageNumber(lastPageNumber);
		dateSigned.setRecipientId(ms.getRecipientId());
		dateSigned.setXPosition(ms.getXPos().str());
		dateSigned.setYPosition(ms.getYPos().str());

		List<DateSigned> dateSignedTabs = new ArrayList<DateSigned>();
		dateSignedTabs.add(dateSigned);
		tabs.setDateSignedTabs(dateSignedTabs);

		FullName fullNameTab = new FullName();
		fullNameTab.setDocumentId("1");
		fullNameTab.setPageNumber(lastPageNumber);
		fullNameTab.setRecipientId(ms.getRecipientId());
		fullNameTab.setXPosition(ms.getXPos().str());
		fullNameTab.setYPosition(ms.subFromPos(ms.getYPos(), 70).str());

		List<FullName> fullNameTabs = new ArrayList<FullName>();
		fullNameTabs.add(fullNameTab);

		tabs.setFullNameTabs(fullNameTabs);
		signer.setTabs(tabs);

		SignHere signHere = new SignHere();
		signHere.setDocumentId("1");
		signHere.setPageNumber(lastPageNumber);
		signHere.setRecipientId(ms.getRecipientId());
		signHere.setXPosition(ms.getXPos().str());
		signHere.setYPosition(ms.subFromPos(ms.getYPos(), 60).str());

		List<SignHere> signHereTabs = new ArrayList<SignHere>();
		signHereTabs.add(signHere);

		tabs.setSignHereTabs(signHereTabs);

		envDef.getRecipients().getSigners().add(signer);
	}

	private ObjectMapper getObjectMapper() {
		if (om == null) {
			om = new ObjectMapper();
			om.enable(SerializationFeature.INDENT_OUTPUT);
			om.setSerializationInclusion(Include.NON_NULL);
			om.setSerializationInclusion(Include.NON_EMPTY);

		}
		return om;
	}

	public String encodeFileToBase64Binary(File file) throws IOException {

		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);

		return encodedString;
	}

	public byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		byte[] bytes;
		try {
			long length = file.length();
			if (length > Integer.MAX_VALUE) {
				throw new IOException("File is too large");
			}
			bytes = new byte[(int) length];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			if (offset < bytes.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}
		} finally {
			is.close();
		}

		return bytes;
	}

}
