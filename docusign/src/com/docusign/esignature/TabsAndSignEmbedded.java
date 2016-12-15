package com.docusign.esignature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.ofbiz.base.util.Debug;

import com.docusign.esign.model.DateSigned;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.FullName;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.ViewUrl;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

// DocuSignClient is too differrent
//faster to use as decorator
public class TabsAndSignEmbedded {

	static final String module = TabsAndSignEmbedded.class.getName();

	private String basePath = "https://demo.docusign.net/restapi";
	private ObjectMapper om;

	private EnvelopesApi envelopesApi;

	private EnvelopeDefinition envDef;
	private String accountId;
	private String afterSendRedirectCompleteUrl;
	private MySigner msSender;

	public TabsAndSignEmbedded(String basePath) {	
		if(!basePath.endsWith("/restapi")){
		    basePath += "/restapi";
		}
		this.basePath = basePath;
	}

	// first in the map is fio docusign user and the second is the customer that
	// the document is being sent to

	public void setupTabsSign(String dynamicPdfTempFilePath, Map<String, String> nameToEmail, String emailSubject, String emailBlurb,
			String afterSendRedirectCompleteUrl, String accountId) throws IOException {
		this.accountId = accountId;
		this.afterSendRedirectCompleteUrl = afterSendRedirectCompleteUrl;
		try {

			// create a new envelope to manage the signature request
			envDef = new EnvelopeDefinition();
			envDef.setEmailSubject(emailSubject);
			envDef.setEmailBlurb(emailBlurb);

			File pdfFile = new File(dynamicPdfTempFilePath);
			PDDocument pdfDocObject = null;
			double llx = 0;
			double lly = 0;
			double wdt = 0;
			double hdt = 0;
			try {
				pdfDocObject = PDDocument.load(pdfFile);
				PDPage doc = pdfDocObject.getPage(pdfDocObject.getNumberOfPages() - 1);
				llx = new Float(doc.getCropBox().getLowerLeftX()).doubleValue();
				lly = new Float(doc.getCropBox().getLowerLeftY()).doubleValue();
				wdt = new Float(doc.getCropBox().getWidth()).doubleValue();
				hdt = new Float(doc.getCropBox().getHeight()).doubleValue();
				Debug.logInfo("llx: " + llx + ", lly: " + lly + ", wdt: " + wdt, module);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				if (pdfDocObject != null) {
					pdfDocObject.close();
				}
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
			Debug.logInfo("lastPageNumber: " + lastPageNumber, module);
			envDef.setRecipients(new Recipients());
			envDef.getRecipients().setSigners(new ArrayList<Signer>());

			long ts = new Date().getTime();
			boolean senderFirst = true;
			assert (nameToEmail.size() == 2);
			this.msSender = null;
			int i = 0;
			for (Map.Entry<String, String> entry : nameToEmail.entrySet()) {
				MySigner ms = new MySigner(++i, entry.getValue(), "1", (senderFirst ? ts : null), llx + (wdt * (senderFirst ? 0.3 : 0.6)), lly + (hdt * 0.7));
				if (senderFirst) {
					senderFirst = false;
					ms.setUserId(entry.getKey());
					msSender = ms;
				} else {
					ms.setSignerName(entry.getKey());
				}
				// add a recipient to sign the document, identified by name and
				// email we used above
				// while leaving clientUserId out means that the signature
				// request will be sent by email
				addDefaultSigner(envDef, lastPageNumber, ms);

			}
			// providing clientUserId means this signer will use embedded
			// signing
			// MySigner ms = new MySigner(1, "justin@fluidnotions.com",
			// "justin Robinson", "1", "001", llx + (wdt * 0.3), lly + (hdt *
			// 0.7));
			// // add a recipient to sign the document, identified by name and
			// email we used above
			// // while leaving clientUserId out means that the signature
			// request will be sent by email
			// addDefaultSigner(envDef, lastPageNumber, ms);
			// MySigner ms2 = new MySigner(2, "ben777ben777b@gmail.com",
			// "Ben Seven", "2", llx + (wdt * 0.6), lly + (hdt * 0.7));
			// addDefaultSigner(envDef, lastPageNumber, ms2);

			// send the envelope by setting |status| to "sent". To save as a
			// draft set to "created"
			envDef.setStatus("sent");

			try {
				Debug.logInfo(getObjectMapper().writeValueAsString(envDef), module);
			} catch (JsonProcessingException e) {

				e.printStackTrace();
			}

			// instantiate a new EnvelopesApi object
			envelopesApi = new EnvelopesApi();

		} catch (Exception ex) {
			Debug.logInfo("Exception: " + ex, module);
		}

	}

	public ApiRestRequest senderEmbeddedSignView(String envelopeId) throws ApiException {
		// set the url where you want the recipient to go once they are done
		// signing
		RecipientViewRequest returnUrl = new RecipientViewRequest();
		returnUrl.setReturnUrl(afterSendRedirectCompleteUrl);
		returnUrl.setAuthenticationMethod("email");

		// recipient information must match embedded recipient info we
		// provided in step #2
		returnUrl.setEmail(this.msSender.signerEmail);
		returnUrl.setUserName(msSender.signerName);
		returnUrl.setRecipientId(msSender.recipientId);
		returnUrl.setClientUserId(msSender.clientUserId);

		// call the CreateRecipientView API
		return envelopesApi.createRecipientView(accountId, envelopeId, returnUrl);

	}

	public ApiRestRequest createEnvelope() throws ApiException {
		return envelopesApi.createEnvelope(accountId, envDef);
	}

	private void addDefaultSigner(EnvelopeDefinition envDef, String lastPageNumber, MySigner ms) {
		Tabs tabs = new Tabs();

		Signer signer = new Signer();
		signer.setEmail(ms.signerEmail);
		if (ms.signerName != null)
			signer.setName(ms.signerName);
		if (ms.userId != null)
			signer.setUserId(ms.userId);
		signer.setRecipientId(ms.recipientId);
		signer.setRoutingOrder(ms.routingOrder);

		// Must set |clientUserId| for embedded recipients and provide the same
		// value when requesting
		// the recipient view URL in the next step
		if (ms.clientUserId != null)
			signer.setClientUserId(ms.clientUserId);

		DateSigned dateSigned = new DateSigned();
		dateSigned.setDocumentId("1");
		dateSigned.setPageNumber(lastPageNumber);
		dateSigned.setRecipientId(ms.recipientId);
		dateSigned.setXPosition(ms.xPos.str());
		dateSigned.setYPosition(ms.yPos.str());

		List<DateSigned> dateSignedTabs = new ArrayList<DateSigned>();
		dateSignedTabs.add(dateSigned);
		tabs.setDateSignedTabs(dateSignedTabs);

		FullName fullNameTab = new FullName();
		fullNameTab.setDocumentId("1");
		fullNameTab.setPageNumber(lastPageNumber);
		fullNameTab.setRecipientId(ms.recipientId);
		fullNameTab.setXPosition(ms.xPos.str());
		fullNameTab.setYPosition(ms.subFromPos(ms.yPos, 70).str());

		List<FullName> fullNameTabs = new ArrayList<FullName>();
		fullNameTabs.add(fullNameTab);

		tabs.setFullNameTabs(fullNameTabs);
		signer.setTabs(tabs);

		SignHere signHere = new SignHere();
		signHere.setDocumentId("1");
		signHere.setPageNumber(lastPageNumber);
		signHere.setRecipientId(ms.recipientId);
		signHere.setXPosition(ms.xPos.str());
		signHere.setYPosition(ms.subFromPos(ms.yPos, 60).str());

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

	private String encodeFileToBase64Binary(File file) throws IOException {

		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);

		return encodedString;
	}

	@SuppressWarnings("resource")
	private byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		is.close();
		return bytes;
	}

	static class MySigner {
		String signerEmail;
		String signerName;
		String userId;
		String recipientId;
		Pos xPos;
		Pos yPos;
		String routingOrder;
		String clientUserId;

		public MySigner() {
		}

		public MySigner(Integer routingOrder, String signerEmail, String recipientId, Double xPos, Double yPos) {
			this(routingOrder, signerEmail, recipientId, null, xPos, yPos);
		}

		public MySigner(Integer routingOrder, String signerEmail, String recipientId, Long clientUserId, Double xPos, Double yPos) {
			super();
			this.signerEmail = signerEmail;
			this.recipientId = recipientId;
			this.xPos = new Pos(xPos);
			this.yPos = new Pos(yPos);
			this.routingOrder = routingOrder.toString();
			this.clientUserId = clientUserId != null ? clientUserId.toString() : null;
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

		public String getSignerName() {
			return signerName;
		}

		public void setSignerName(String signerName) {
			this.signerName = signerName;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

	}

	public String getBasePath() {
		return basePath;
	}
}
