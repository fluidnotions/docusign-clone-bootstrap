package com.fluidnotions.docusign.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.ofbiz.base.util.Debug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.docusign.esignature.DocuSigAPIException;
import com.docusign.esignature.json.Envelope;
import com.docusign.esignature.json.EnvelopesStatusChanged;
import com.fluidnotions.opentaps.integration.docusign.DocuSignEntityUtil;

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
}
