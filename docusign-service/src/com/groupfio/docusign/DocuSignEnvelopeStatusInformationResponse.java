package com.groupfio.docusign;

import java.util.List;

public class DocuSignEnvelopeStatusInformationResponse {

    private Integer total;
    private List<DocuSignEnvelopeStatusInformation> rows = null;
  
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
}
