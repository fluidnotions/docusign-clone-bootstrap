package com.groupfio.docusign.services;

import org.opentaps.foundation.service.Service;
import org.opentaps.foundation.service.ServiceException;

import com.groupfio.extension.helper.OTUtil;

public class GeneralUtilServices extends Service {

	public final static String module = GeneralUtilServices.class.getName();


	private String tenantKey;
	private String urlSuffix;
	private String hostBaseUrl;

	public void getTenantdynamicDocDownloadSuffix() throws ServiceException {	
		urlSuffix = OTUtil.getTenantdynamicDocDownloadSuffix(tenantKey);
	}
	
	public void lookupHostBaseUrl() throws ServiceException {	
		hostBaseUrl = OTUtil.getHostBaseUrl(tenantKey);
	}

	
	public String getUrlSuffix() {
		return urlSuffix;
	}

	public void setUrlSuffix(String urlSuffix) {
		this.urlSuffix = urlSuffix;
	}


	public String getTenantKey() {
		return tenantKey;
	}


	public void setTenantKey(String tenantKey) {
		this.tenantKey = tenantKey;
	}

	public void setHostBaseUrl(String hostBaseUrl) {
		this.hostBaseUrl = hostBaseUrl;
	}

	public String getHostBaseUrl() {
		return hostBaseUrl;
	}

	

	

}
