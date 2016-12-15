package com.docusign.esignature;

import java.util.List;
import java.util.Map;

import com.fasterxml.classmate.GenericType;

/**
 * Adapted from Invoke API by sending HTTP request with the given options.
 * Encaped options returned and used by older client
 *
 * @param path The sub-path of the HTTP URL
 * @param method The request method, one of "GET", "POST", "PUT", and "DELETE"
 * @param queryParams The query parameters
 * @param body The request body object - if it is not binary, otherwise null
 * @param headerParams The header parameters
 * @param formParams The form parameters
 * @param accept The request's Accept header
 * @param contentType The request's Content-Type header
 * @param authNames The authentications to apply
 *@return (originally the response body in type of string) as object wrapper 
 */
//(String path, String method, List<Pair> queryParams, Object body, Map<String, String> headerParams, 
//Map<String, Object> formParams, String accept, String contentType, String[] authNames, GenericType<T> returnType) throws ApiException {

 class ApiRestRequest{
	 String urlSubPath;
	 String restVerb;
	 List<Pair> queryParams;
	 Object requestBody;
	 Map<String, String> headerParams;
	 Map<String, Object> formParams;
	 String acceptHeader;
	 String contentType;
	 String[] authNames;
	 GenericType returnType;
	 
	public ApiRestRequest(String urlSubPath, String restVerb, List<Pair> queryParams, Object requestBody, Map<String, String> headerParams,
			Map<String, Object> formParams, String acceptHeader, String contentType, String[] authNames, GenericType returnType) {
		super();
		this.urlSubPath = urlSubPath;
		this.restVerb = restVerb;
		this.queryParams = queryParams;
		this.requestBody = requestBody;
		this.headerParams = headerParams;
		this.acceptHeader = acceptHeader;
		this.contentType = contentType;
		
		this.formParams = formParams;
		this.authNames = authNames;
		this.returnType = returnType;
	}
	 
	 
 }