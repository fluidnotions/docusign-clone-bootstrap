package com.groupfio.docusign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocuSignEnvelopeStatusInformationResponse {

    private Integer total;
    private List<DocuSignEnvelopeStatusInformation> rows = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

  
    public List<DocuSignEnvelopeStatusInformation> getRows() {
        return rows;
    }
    
    public void setRows(List<DocuSignEnvelopeStatusInformation> rows) {
        this.rows = rows;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }


    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
