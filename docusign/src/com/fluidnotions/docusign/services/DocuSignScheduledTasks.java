package com.fluidnotions.docusign.services;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.ofbiz.base.util.Debug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.docusign.esign.model.Signer;
import com.docusign.esignature.DocuSigAPIException;
import com.docusign.esignature.json.Envelope;
import com.docusign.esignature.json.EnvelopesStatusChanged;
import com.fluidnotions.opentaps.integration.docusign.DocuSignEntityUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

@Component
public class DocuSignScheduledTasks {

    @Autowired
    private DocuSignService docuSignService;

    // every 5 min
    @Scheduled(fixedDelay = 300000)
    // @Scheduled(fixedDelay = 30000)
            public
            void
            updateAllEnvelopesStatus() {
        Debug.logInfo("Scheduled called at " + new Date().toString(), this.getClass().getName());
        int periodInMin = new Integer(DocuSignEntityUtil.getPropety("periodInMin"));
        String apiTimezone = DocuSignEntityUtil.getPropety("apiTimezone");

        for (String tenantKey : DocuSignEntityUtil.getTenantKeyList()) {
            try {
                String from = docuSignService.calculateFromDateTime(periodInMin, apiTimezone);
                //Debug.logInfo("Scheduled called for tenantKey: " + tenantKey + " with calculateFromDateTime: " + from, this.getClass().getName());
                EnvelopesStatusChanged changed = docuSignService.getEnvelopesStatusChangesForTenantAccount(tenantKey, from);
                List<Envelope> envolopes = changed.getEnvelopes();
                if(envolopes != null && envolopes.size()>0) lookupSelectDetails(tenantKey, envolopes);
                if(envolopes!=null && (envolopes.size() > 0)) DocuSignEntityUtil.storeEnvelopeUpdates(tenantKey, envolopes);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocuSigAPIException e) {
                e.printStackTrace();
            }
        }
    }

	private void lookupSelectDetails(String tenantKey, List<Envelope> envolopes) {
		 JsonParser parser=new JsonParser();
		for(Envelope env: envolopes){
		
			try {
				String recipientsDetail = docuSignService.getDetailUri(tenantKey, env.getRecipientsUri());
				JsonObject recipientsDetailJsonObject= (JsonObject) parser.parse(recipientsDetail);
				Type listType = new TypeToken<List<Signer>>() {}.getType();
				List<Signer> signers = new Gson().fromJson(recipientsDetailJsonObject.get("signers"), listType);
				Debug.logInfo("recipientsDetail json: "+recipientsDetail+ "\n, signers.size(): " +signers.size(), this.getClass().getName());
				if(signers != null && signers.size() > 0){
					DocuSignEntityUtil.storeEnvelopeSignerStatusUpdates(tenantKey, env.getEnvelopeId(), signers);
				}			
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocuSigAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
		
	}
}
