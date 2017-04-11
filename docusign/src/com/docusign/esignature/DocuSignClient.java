package com.docusign.esignature;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.ViewUrl;
import com.docusign.esignature.json.Document;
import com.docusign.esignature.json.EnvelopeInformation;
import com.docusign.esignature.json.EnvelopesStatusChanged;
import com.docusign.esignature.json.LoginAccount;
import com.docusign.esignature.json.LoginResult;
import com.docusign.esignature.json.NewUsers;
import com.docusign.esignature.json.RecipientInformation;
import com.docusign.esignature.json.RequestSignatureFromDocuments;
import com.docusign.esignature.json.RequestSignatureFromTemplate;
import com.docusign.esignature.json.RequestSignatureResponse;
import com.docusign.esignature.json.StatusInformation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluidnotions.docusign.models.AuthenticatingUser;
import com.fluidnotions.docusign.models.RecipientModel;
import com.groupfio.docusign.AutoPositionedSignerHelper;

@Component
public class DocuSignClient {
	private static final String module = DocuSignClient.class.getName();
	private String email;
	private String password;
	private List<LoginAccount> loginAccounts = new ArrayList<LoginAccount>();
	private String accountId;
	private String integratorKey;
	private String serverUrl = "";
	private String baseUrl = "";
	private String lastResponse;
	private String lastRequest;
	private String lastError;
	private String accessToken;
	private TabsAndSignEmbedded tabsAndSignEmbedded;
	private AutoPositionedSignerHelper autoPosHelper;

	private static ObjectMapper mapper = new ObjectMapper();

	// EDITS
	// attaches X-DocuSign-Act-As-User header to any REST API call
	private String operatingUser;
	private String afterSendRedirectUrl;
	private AuthenticatingUser authenticatingUser;

	public DocuSignClient asUser(String operatingUser) throws MalformedURLException, IOException, DocuSigAPIException {
		if (operatingUser != null)
			this.operatingUser = operatingUser;
		Debug.logInfo("operatingUser: " + operatingUser + ", AuthenticatingUser: email: " + email, this.getClass().getName());
		login();
		return this;
	}

	private void clearOperatingUser() {
		this.operatingUser = null;
	}

	public DocuSignClient() {
		this.serverUrl = UtilProperties.getPropertyValue("docusign", "serverUrl");
	}

	public void init(AuthenticatingUser au) throws MalformedURLException, IOException {
		this.authenticatingUser = au;
		init(au.getUsername(), au.getPassword(), au.getIntegratorKey());
		this.tabsAndSignEmbedded = new TabsAndSignEmbedded(serverUrl);
		this.afterSendRedirectUrl = au.getAfterSendRedirectUrlBase();

	}

	public void init(String email, String password, String integratorKey) {
		this.email = email;
		this.password = password;
		this.integratorKey = integratorKey;
		this.autoPosHelper = AutoPositionedSignerHelper.newInstance(serverUrl, this);
	}

	/*
	 * see https://docs.docusign.com/esign/guide/authentication/sobo.html this
	 * would be the xml header... impl if operatingUser is not set we are auth
	 * with super user authenticationUser this user needs Required Permissions
	 * The authenticating user must have the following user setting properties
	 * set to true: apiAccountWideAccess: Specifies that the user can send and
	 * manage envelopes for the entire account using the DocuSign API.
	 * 
	 * allowSendOnBehalfOf: Specifies that the user can send envelopes and
	 * perform other tasks on behalf of other users through the API.
	 * 
	 * for the so called username to be the email address is normal
	 */
	public String getAuthHeader() {
		// xml opting for json format
		// String soboUser = operatingUser != null ? "<SendOnBehalfOf>" +
		// operatingUser + "</SendOnBehalfOf>" : "";
		// String authenticateStr = "<DocuSignCredentials>" + soboUser +
		// "<Username>" + email + "</Username>" + "<Password>" + password +
		// "</Password>" + "<IntegratorKey>" + integratorKey +
		// "</IntegratorKey>" + "</DocuSignCredentials>";
		// return authenticateStr;
		String authenticateStr = null;
		try {
			authenticateStr = this.authenticatingUser.toJsonAuthenticationHeader(this.operatingUser);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Debug.logError(e, "getAuthHeader: " + authenticateStr, module);
		}
		if (UtilValidate.isEmpty(authenticateStr)) {
			Debug.logError("getAuthHeader is EMPTY", module);
		}
		return authenticateStr;
	}

	public String addSignerTabsAndAllowSenderToSignEmbedded(String dynamicPdfTempFilePath, String emailSubject, String emailBlurb,
			String afterSendRedirectCompleteUrl, List<RecipientModel> recipientModels) throws DocuSigAPIException {
		assert (recipientModels.size() == 2);// element position math not
												// defined for more then 2
												// recipients
		String result = null;
		try {
			result = autoPosHelper.applyAutoPositionedSignTabsToDoc(dynamicPdfTempFilePath, emailSubject, emailBlurb, afterSendRedirectCompleteUrl,
					recipientModels);
		} catch (IOException e) {
			e.printStackTrace();
			throw new DocuSigAPIException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocuSigAPIException(e.getMessage());
		}
		return result;
	}

	public ViewUrl addSignerTabsAndAllowSenderToSignEmbedded(String dynamicPdfTempFilePath, Map<String, String> nameToEmail, String afterSendRedirectCompleteUrl)
			throws IOException, DocuSigAPIException, ApiException {
		tabsAndSignEmbedded.setupTabsSign(dynamicPdfTempFilePath, nameToEmail, "Please sign this document", "Follow the link to sign the document online.",
				afterSendRedirectCompleteUrl, accountId);
		String rawResponse = newInvokeAdapter(tabsAndSignEmbedded.createEnvelope());
		EnvelopeSummary envelopeSummary = mapper.readValue(rawResponse, EnvelopeSummary.class);
		String envelopeId = envelopeSummary.getEnvelopeId();
		String rawResponse2 = newInvokeAdapter(tabsAndSignEmbedded.senderEmbeddedSignView(envelopeId));
		ViewUrl url = mapper.readValue(rawResponse2, ViewUrl.class);
		Debug.logInfo("envelopeId: " + envelopeId + ", url: " + url, module);

		return url;
	}

	// * @param path The sub-path of the HTTP URL
	// * @param method The request method, one of "GET", "POST", "PUT", and
	// "DELETE"
	// * @param queryParams The query parameters
	// * @param body The request body object - if it is not binary, otherwise
	// null
	// * @param headerParams The header parameters
	// * @param formParams The form parameters
	// * @param accept The request's Accept header
	// * @param contentType The request's Content-Type header
	// * @param authNames The authentications to apply
	// *@return (originally the response body in type of string) as object
	// wrapper
	public String newInvokeAdapter(ApiRestRequest apiRestRequest) throws MalformedURLException, IOException, DocuSigAPIException {
		HttpURLConnection conn = null;
		if (apiRestRequest.formParams != null && apiRestRequest.formParams.size() > 0) {
			throw new RuntimeException("newInvokeAdapter has no implemnetation for request with form paramaters");
		}
		try {

			conn = getRestConnection(getBaseUrl() + EnvelopesApi.buildUrl(apiRestRequest.urlSubPath, apiRestRequest.queryParams), apiRestRequest.contentType,
					apiRestRequest.acceptHeader);
			conn.setRequestMethod(apiRestRequest.restVerb);

			for (Map.Entry<String, String> ent : apiRestRequest.headerParams.entrySet()) {
				conn.setRequestProperty(ent.getKey(), ent.getValue());
			}

			for (Pair pair : apiRestRequest.queryParams) {
				conn.setRequestProperty(pair.getName(), pair.getValue());
			}

			if (apiRestRequest.requestBody != null) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				try {
					bos.write(mapper.writeValueAsBytes(apiRestRequest.requestBody));// (om.writeValueAsString(apiRestRequest.requestBody));
					byte[] yourBytes = bos.toByteArray();
					DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
					dos.write(yourBytes);
					dos.flush();
					dos.close();
				} finally {
					try {
						bos.close();
					} catch (IOException ex) {
						// ignore close exception
					}
				}
			}

			int status = conn.getResponseCode(); // triggers the request
			if (status != 201) // 201 = Created
			{
				processErrorDetails(conn, status);
				return null;
			}

			// Read the response
			String line;
			InputStreamReader isr = new InputStreamReader(extractAndSaveOutput(conn));
			BufferedReader br = new BufferedReader(isr);
			StringBuilder rawResponse = new StringBuilder();
			while ((line = br.readLine()) != null)
				rawResponse.append(line);

			return rawResponse.toString();
		} finally {
			if (conn != null)
				conn.disconnect();
			this.clearOperatingUser();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#login()
	 */

	public boolean login() throws MalformedURLException, IOException, DocuSigAPIException, UnknownHostException {
		String loginUrl = serverUrl + "/restapi/v2/login_information";
		HttpURLConnection conn = null;

		try {

			conn = getRestConnection(loginUrl);
			int status = conn.getResponseCode(); // triggers the request
			if (status != 200) // 200 = OK
			{
				processErrorDetails(conn, status);

				return false;
			}

			BufferedInputStream bufferStream = extractAndSaveOutput(conn);

			LoginResult loginResult = mapper.readValue(bufferStream, LoginResult.class);
			loginAccounts = loginResult.getLoginAccounts();

			// NOTE: there should never be a time when we get a positive result
			// and less than one account.
			accountId = loginAccounts.get(0).getAccountId();
			baseUrl = loginAccounts.get(0).getBaseUrl();

			Debug.logInfo("accountId: " + accountId + ", baseUrl: " + baseUrl, this.getClass().getName());

		}  finally {
			if (conn != null)
				conn.disconnect();
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#getUri(java.lang.String)
	 */

	public String getUri(String uri) throws MalformedURLException, IOException, DocuSigAPIException {
		HttpURLConnection conn = null;
		try {

			conn = getRestConnection(baseUrl + uri);
			conn.setRequestMethod("GET");

			conn.setDoInput(true); // true if we want to read server's response
			conn.setDoOutput(false); // false indicates this is a GET request

			int status = conn.getResponseCode(); // triggers the request
			if (status != 200) {
				processErrorDetails(conn, status);
				return "";
			}

			StringWriter writer = new StringWriter();
			IOUtils.copy(conn.getInputStream(), writer);

			return writer.toString();
		} finally {
			if (conn != null)
				conn.disconnect();
			this.clearOperatingUser();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#voidEnvelope(java.lang.String,
	 * java.lang.String)
	 */

	public boolean voidEnvelope(String envelopeId, String voidedReason) throws MalformedURLException, IOException, DocuSigAPIException {
		HttpURLConnection conn = null;
		try {

			String request = "{\"status\":\"voided\", \"voidedReason\":\"" + voidedReason + "\" }";
			conn = getRestConnection(baseUrl + "/envelopes/" + envelopeId);
			conn.setRequestMethod("PUT");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Length", Integer.toString(request.length()));

			// Write body of the POST request
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(request);
			dos.flush();
			dos.close();

			int status = conn.getResponseCode(); // triggers the request
			if (status != 200) {
				processErrorDetails(conn, status);
				return false;
			}

			return true;
		} finally {
			if (conn != null)
				conn.disconnect();
			this.clearOperatingUser();
		}
	}

	// docuSignUserId: 637fe479-bce0-416a-b2d4-3c7dc19c7c84
	// returns null string for all ok or errorDetails message
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#closeUser(java.lang.String)
	 */

	public String closeUser(String docuSignUserId) throws MalformedURLException, IOException, DocuSigAPIException {
		HttpURLConnection conn = null;
		try {

			String request = "{\"users\":[{\"userId\":\"" + docuSignUserId + "\", \"userName\":\"\" }]}";
			conn = getRestConnection(baseUrl + "/users");
			conn.setRequestMethod("DELETE");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Length", Integer.toString(request.length()));

			// Write body of the POST request
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(request);
			dos.flush();
			dos.close();

			int status = conn.getResponseCode(); // triggers the request
			if (status != 200) {
				processErrorDetails(conn, status);
				return "request failed with code " + status;
			}

			StringWriter writer = new StringWriter();
			IOUtils.copy(conn.getInputStream(), writer);
			String response = writer.toString();
			Debug.logInfo("response: " + response, this.getClass().getName());

			String result = null;
			// check for error message
			if (response.contains("errorDetails")) {
				// return error message
				String start = "\"message\":";
				result = response.substring(response.indexOf(start) + start.length(), response.indexOf("\"", response.indexOf(start) + start.length() + 1));
			}

			return result;
		} finally {
			if (conn != null)
				conn.disconnect();
			this.clearOperatingUser();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#getEnvelopesStatusChanged(java
	 * .lang.String)
	 */

	public EnvelopesStatusChanged getEnvelopesStatusChanged(String fromDateTime) throws MalformedURLException, IOException, DocuSigAPIException {
		HttpURLConnection conn = null;
		try {

			Map<String, String> params = new HashMap<String, String>();
			params.put("from_date", fromDateTime);
			params.put("from_to_status", "changed");
			params.put("status", "Any");

			String request = prepRequestParam(params);
			// Debug.logInfo("request: " + request, this.getClass().getName());
			conn = getRestConnection(baseUrl + "/envelopes?" + request);
			conn.setRequestMethod("GET");

			conn.setDoInput(true); // true if we want to read server's response
			conn.setDoOutput(false); // false indicates this is a GET request

			int status = conn.getResponseCode(); // triggers the request
			if (status != 200) {
				processErrorDetails(conn, status);
				return null;
			}

			StringWriter writer = new StringWriter();
			IOUtils.copy(conn.getInputStream(), writer);

			// Debug.logInfo("response: " + writer.toString(),
			// this.getClass().getName());

			return mapper.readValue(writer.toString(), EnvelopesStatusChanged.class);
		} finally {
			if (conn != null)
				conn.disconnect();
			this.clearOperatingUser();
		}
	}

	private String prepRequestParam(Map<String, String> params) throws UnsupportedEncodingException {
		StringBuffer requestParams = new StringBuffer();

		if (params != null && params.size() > 0) {

			// creates the params string, encode them using URLEncoder
			Iterator<String> paramIterator = params.keySet().iterator();
			while (paramIterator.hasNext()) {
				String key = paramIterator.next();
				String value = params.get(key);
				requestParams.append(URLEncoder.encode(key, "UTF-8"));
				requestParams.append("=").append(URLEncoder.encode(value, "UTF-8"));
				requestParams.append("&");
			}
		}

		return requestParams.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#requestSignatureFromTemplate(com
	 * .docusign.esignature.json.RequestSignatureFromTemplate)
	 */

	public String requestSignatureFromTemplate(RequestSignatureFromTemplate request) throws MalformedURLException, IOException, DocuSigAPIException {
		HttpURLConnection conn = null;

		try {
			// append "/envelopes" to the baseUrl and use in the request
			conn = getRestConnection(baseUrl + "/envelopes");

			lastRequest = mapper.writeValueAsString(request);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Length", Integer.toString(lastRequest.length()));

			// Write body of the POST request
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(lastRequest);
			dos.flush();
			dos.close();
			int status = conn.getResponseCode(); // triggers the request
			if (status != 201) // 201 = Created
			{
				processErrorDetails(conn, status);
				return "";
			}

			// Read the response
			RequestSignatureResponse response = mapper.readValue(conn.getInputStream(), RequestSignatureResponse.class);
			return response.getEnvelopeId();
		} finally {
			if (conn != null)
				conn.disconnect();
			this.clearOperatingUser();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#requestSignatureFromDocuments(
	 * com.docusign.esignature.json.RequestSignatureFromDocuments,
	 * java.io.InputStream[])
	 */

	public String requestSignatureFromDocuments(RequestSignatureFromDocuments request, InputStream[] stream) throws MalformedURLException, IOException,
			DocuSigAPIException {

		// TODO: lastRequest is not properly logged here

		if (stream == null || stream.length == 0)
			throw new IllegalArgumentException("You need to pass in at least one file");

		if (stream.length != request.getDocuments().size())
			throw new IllegalArgumentException("The number of file bytes should be equal to the number of documents in the request. Got " + stream.length
					+ " byte arrays, and " + request.getDocuments().size() + " file definitions.");

		HttpURLConnection conn = null;

		try {
			// append "/envelopes" to the baseUrl and use in the request
			conn = getRestConnection(baseUrl + "/envelopes");

			String jsonBody = mapper.writeValueAsString(request);

			String startRequest = "\r\n\r\n--BOUNDARY\r\n" + "Content-Type: application/json\r\n" + "Content-Disposition: form-data\r\n" + "\r\n" + jsonBody;

			// we break this up into two string since the PDF doc bytes go here
			// and are not in string format.
			// see further below where we write to the outputstream...
			String endBoundary = "\r\n" + "--BOUNDARY--\r\n\r\n";

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=BOUNDARY");

			// write the body of the request...
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(startRequest.toString());

			for (int i = 0; i < stream.length; ++i) {
				Document next = request.getDocuments().get(i);

				String contentType = next.getContentType();
				if (contentType == null) {
					contentType = "application/pdf";
				}

				String boundary = "\r\n--BOUNDARY\r\n"
						+ // opening boundary for next document
						"Content-Type: " + contentType + "\r\n" + "Content-Disposition: file; filename=\"" + next.getName() + "\"; documentId="
						+ next.getDocumentId() + "\r\n" + "\r\n";

				dos.writeBytes(boundary);

				byte[] buffer = new byte[1024];
				int len;
				while ((len = stream[i].read(buffer)) != -1) {
					dos.write(buffer, 0, len);
				}
			}

			dos.writeBytes(endBoundary);
			dos.flush();
			dos.close();

			int status = conn.getResponseCode(); // triggers the request
			if (status != 201) // 201 = Created
			{
				processErrorDetails(conn, status);
				return "";
			}
			// Read the response
			RequestSignatureResponse response = mapper.readValue(conn.getInputStream(), RequestSignatureResponse.class);
			return response.getEnvelopeId();
		} finally {
			if (conn != null)
				conn.disconnect();
			this.clearOperatingUser();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#requestSignatureFromDocuments(
	 * com.docusign.esignature.json.RequestSignatureFromDocuments,
	 * java.io.File[])
	 */

	public String requestSignatureFromDocuments(RequestSignatureFromDocuments request, File[] files) throws MalformedURLException, IOException,
			DocuSigAPIException {
		// Convert the files to input streams
		List<InputStream> streams = new ArrayList<InputStream>();
		for (int i = 0; i < files.length; i++) {
			streams.add(new FileInputStream(files[i]));
		}

		return requestSignatureFromDocuments(request, streams.toArray(new InputStream[streams.size()]));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#requestConsoleView()
	 */

	public String requestConsoleView() throws IOException, DocuSigAPIException {
		HttpURLConnection conn = null;

		// construct an outgoing xml request body
		lastRequest = "<consoleViewRequest xmlns=\"http://www.docusign.com/restapi\">" + "<accountId>" + getAccountId() + "</accountId>"
				+ "</consoleViewRequest>";

		// append "/views/console" to the baseUrl and use in the request
		try {
			conn = getRestConnection(getBaseUrl() + "/views/console");

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/xml");
			conn.setRequestProperty("Accept", "application/xml");
			conn.setRequestProperty("Content-Length", Integer.toString(lastRequest.length()));

			// write the body of the request...
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(lastRequest);
			dos.flush();
			dos.close();
			int status = conn.getResponseCode();
			if (status != 201) // 201 = Created
			{
				processErrorDetails(conn, status);
				return "";
			}
			// Read the response
			String line;
			InputStreamReader isr = new InputStreamReader(extractAndSaveOutput(conn));
			BufferedReader br = new BufferedReader(isr);
			StringBuilder response2 = new StringBuilder();
			while ((line = br.readLine()) != null)
				response2.append(line);
			String token1 = "//*[1]/*[local-name()='url']";
			XPath xPath = XPathFactory.newInstance().newXPath();
			String viewUrl = xPath.evaluate(token1, new InputSource(new StringReader(response2.toString())));
			return viewUrl;
		} catch (ProtocolException e) {
			e.printStackTrace();
			return "";
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return "";
		} finally {
			if (conn != null)
				conn.disconnect();
			this.clearOperatingUser();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#requestSendingView(java.lang.String
	 * , java.lang.String)
	 */

	public String requestSendingView(String envelopeId, String returnUrl) throws MalformedURLException, IOException, DocuSigAPIException {
		HttpURLConnection conn = null;
		try {
			lastRequest = "{\"returnUrl\": \"" + returnUrl + "\"}";

			conn = getRestConnection(getBaseUrl() + "/envelopes/" + envelopeId + "/views/sender");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(lastRequest.length()));
			conn.setRequestProperty("Accept", "application/xml");

			// write the body of the request...
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(lastRequest);
			dos.flush();
			dos.close();
			int status = conn.getResponseCode(); // triggers the request
			if (status != 201) // 201 = Created
			{
				processErrorDetails(conn, status);
				return "";
			}

			// Read the response
			String line;
			InputStreamReader isr = new InputStreamReader(extractAndSaveOutput(conn));
			BufferedReader br = new BufferedReader(isr);
			StringBuilder response3 = new StringBuilder();
			while ((line = br.readLine()) != null)
				response3.append(line);
			String token1 = "//*[1]/*[local-name()='url']";
			XPath xPath = XPathFactory.newInstance().newXPath();
			String viewUrl;
			try {
				viewUrl = xPath.evaluate(token1, new InputSource(new StringReader(response3.toString())));
			} catch (XPathExpressionException e) {
				e.printStackTrace();
				return "";
			}
			return viewUrl;
		} finally {
			if (conn != null)
				conn.disconnect();
			this.clearOperatingUser();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#addNewUsers(com.docusign.esignature
	 * .json.NewUsers)
	 */

	public NewUsers addNewUsers(NewUsers newUsers) throws MalformedURLException, IOException, DocuSigAPIException {
		HttpURLConnection conn = null;
		try {
			lastRequest = mapper.writeValueAsString(newUsers);

			conn = getRestConnection(getBaseUrl() + "/users");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(lastRequest.length()));

			// write the body of the request...
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(lastRequest);
			dos.flush();
			dos.close();
			int status = conn.getResponseCode(); // triggers the request
			if (status != 201) // 201 = Created
			{
				processErrorDetails(conn, status);
				return null;
			}

			// Read the response
			String line;
			InputStreamReader isr = new InputStreamReader(extractAndSaveOutput(conn));
			BufferedReader br = new BufferedReader(isr);
			StringBuilder rawResponse = new StringBuilder();
			while ((line = br.readLine()) != null)
				rawResponse.append(line);

			return mapper.readValue(rawResponse.toString(), NewUsers.class);
		} finally {
			if (conn != null)
				conn.disconnect();
			this.clearOperatingUser();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#requestRecipientView(java.lang
	 * .String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */

	public String requestRecipientView(String envelopeId, String userName, String email, String clientUserId, String returnUrl, String authenticationMethod)
			throws MalformedURLException, IOException, DocuSigAPIException {
		HttpURLConnection conn = null;
		try {

			lastRequest = "{\"authenticationMethod\": \"" + authenticationMethod + "\",\"email\": \"" + email + "\",\"returnUrl\": \"" + returnUrl
					+ "\",\"userName\": \"" + userName + "\",\"clientUserId\": \"" + clientUserId + "\"}";

			conn = getRestConnection(getBaseUrl() + "/envelopes/" + envelopeId + "/views/recipient");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(lastRequest.length()));
			conn.setRequestProperty("Accept", "application/xml");

			// write the body of the request...
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(lastRequest);
			dos.flush();
			dos.close();
			int status = conn.getResponseCode(); // triggers the request
			if (status != 201) // 201 = Created
			{
				processErrorDetails(conn, status);
				return "";
			}

			// Read the response
			String line;
			InputStreamReader isr = new InputStreamReader(extractAndSaveOutput(conn));
			BufferedReader br = new BufferedReader(isr);
			StringBuilder response3 = new StringBuilder();
			while ((line = br.readLine()) != null)
				response3.append(line);
			String token1 = "//*[1]/*[local-name()='url']";
			XPath xPath = XPathFactory.newInstance().newXPath();
			String viewUrl;
			try {
				viewUrl = xPath.evaluate(token1, new InputSource(new StringReader(response3.toString())));
			} catch (XPathExpressionException e) {
				e.printStackTrace();
				return "";
			}
			return viewUrl;
		} finally {
			if (conn != null)
				conn.disconnect();
			this.clearOperatingUser();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#requestStatusInformation(java.
	 * util.Date, java.lang.String)
	 */

	public StatusInformation requestStatusInformation(Date fromDate, String fromToStatus) throws MalformedURLException, IOException, DocuSigAPIException {
		String fromDateStr = new SimpleDateFormat("MM/dd/yyyy").format(fromDate);
		String envelopeUrl = baseUrl + "/envelopes?from_date=" + URLEncoder.encode(fromDateStr, "UTF-8") + "&from_to_status=" + fromToStatus;
		HttpURLConnection conn = null;

		try {

			conn = getRestConnection(envelopeUrl);
			int status = conn.getResponseCode(); // triggers the request
			if (status != 200) // 200 = OK
			{
				processErrorDetails(conn, status);
				return null;
			}

			BufferedInputStream bufferStream = extractAndSaveOutput(conn);

			StatusInformation statusInformation = mapper.readValue(bufferStream, StatusInformation.class);

			return statusInformation;
		} finally {
			if (conn != null)
				conn.disconnect();
			this.clearOperatingUser();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#requestEnvelopeInformation(java
	 * .lang.String)
	 */

	public EnvelopeInformation requestEnvelopeInformation(String envelopeId) throws MalformedURLException, IOException, DocuSigAPIException {
		String envelopeUrl = baseUrl + "/envelopes/" + envelopeId;
		HttpURLConnection conn = null;

		try {

			conn = getRestConnection(envelopeUrl);
			int status = conn.getResponseCode(); // triggers the request
			if (status != 200) // 200 = OK
			{
				processErrorDetails(conn, status);
				return null;
			}

			BufferedInputStream bufferStream = extractAndSaveOutput(conn);

			EnvelopeInformation envelopeInformation = mapper.readValue(bufferStream, EnvelopeInformation.class);

			return envelopeInformation;
		} finally {
			if (conn != null)
				conn.disconnect();
			this.clearOperatingUser();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#requestCombinedDocument(java.lang
	 * .String)
	 */

	public InputStream requestCombinedDocument(String envelopeId) throws MalformedURLException, IOException, DocuSigAPIException {
		String envelopeUrl = baseUrl + "/envelopes/" + envelopeId + "/documents/combined";
		HttpURLConnection conn = null;

		try {
			conn = getRestConnection(envelopeUrl);
			conn.setRequestProperty("Accept", "application/pdf");
			int status = conn.getResponseCode();
			if (status != 200) // 200 = OK
			{
				processErrorDetails(conn, status);
				return null;
			}
			return conn.getInputStream();
		} finally {
			if (conn != null)
				conn.disconnect();
			this.clearOperatingUser();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#requestRecipientInformation(java
	 * .lang.String)
	 */

	public RecipientInformation requestRecipientInformation(String envelopeId) throws MalformedURLException, IOException, DocuSigAPIException {
		String envelopeUrl = baseUrl + "/envelopes/" + envelopeId + "/recipients";
		HttpURLConnection conn = null;

		try {
			conn = getRestConnection(envelopeUrl);
			int status = conn.getResponseCode(); // triggers the request
			if (status != 200) // 200 = OK
			{
				processErrorDetails(conn, status);
				return null;
			}

			BufferedInputStream bufferStream = extractAndSaveOutput(conn);

			RecipientInformation info = mapper.readValue(bufferStream, RecipientInformation.class);
			return info;
		} finally {
			if (conn != null)
				conn.disconnect();
			this.clearOperatingUser();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#getRestConnection(java.lang.String
	 * )
	 */
	public HttpURLConnection getRestConnection(String url) throws IOException, MalformedURLException {
		return getRestConnection(url, null, null);
	}

	public HttpURLConnection getRestConnection(String url, String contentType, String accept) throws IOException, MalformedURLException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		if (this.accessToken != null) {
			throw new IllegalStateException("docusign oauth approach not implemented");
			// conn.setRequestProperty("Authorization",
			// "bearer "+this.accessToken);
		} else {
			conn.setRequestProperty("X-DocuSign-Authentication", getAuthHeader());

		}
		conn.setRequestProperty("Content-Type", contentType == null ? "application/json" : contentType);
		conn.setRequestProperty("Accept", accept == null ? "application/json" : accept);
		conn.setDoOutput(true);

		return conn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#getLastError()
	 */

	public String getLastError() {
		return lastError;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#setLastError(java.lang.String)
	 */

	public void setLastError(String str) {
		String lastError = str;
	}

	private void processErrorDetails(HttpURLConnection conn, int status) throws IOException, DocuSigAPIException {
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		StringBuilder responseText = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null)
			responseText.append(line);
		lastError = responseText.toString();

		Debug.logError("Error calling webservice (" + conn.getURL().getPath() + "), status is: " + status + ", error message is: " + lastError, this.getClass()
				.getName());

		throw new DocuSigAPIException(lastError);

	}

	private BufferedInputStream extractAndSaveOutput(HttpURLConnection conn) throws IOException {
		BufferedInputStream bufferStream = new BufferedInputStream(conn.getInputStream(), 1024 * 1024);
		bufferStream.mark(0);
		BufferedReader br = new BufferedReader(new InputStreamReader(bufferStream));
		String line = null;
		StringBuilder responseText = new StringBuilder();
		while ((line = br.readLine()) != null)
			responseText.append(line);

		lastResponse = responseText.toString();
		bufferStream.reset();
		return bufferStream;
	}

	//
	// getters and setters
	//
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#getEmail()
	 */

	public String getEmail() {
		return email;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#getPassword()
	 */

	public String getPassword() {
		return password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#setAccountId(java.lang.String)
	 */

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#getAccountId()
	 */

	public String getAccountId() {
		return accountId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#getIntegratorKey()
	 */

	public String getIntegratorKey() {
		return integratorKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#setServerUrl(java.lang.String)
	 */

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#getServerUrl()
	 */

	public String getServerUrl() {
		return serverUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#setBaseUrl(java.lang.String)
	 */

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#getBaseUrl()
	 */

	public String getBaseUrl() {
		return baseUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#getLastResponseText()
	 */

	public String getLastResponseText() {
		return lastResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#getLastRequestText()
	 */

	public String getLastRequestText() {
		return lastRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#getAccessToken()
	 */

	public String getAccessToken() {
		return accessToken;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.docusign.esignature.DocuSignClient#setAccessToken(java.lang.String)
	 */

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.docusign.esignature.DocuSignClient#getLoginAccounts()
	 */

	public List<LoginAccount> getLoginAccounts() {
		return this.loginAccounts;
	}

	public String getAfterSendRedirectUrl() {
		return afterSendRedirectUrl;
	}

}
