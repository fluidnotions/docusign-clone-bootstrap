<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl" />



<@frameSection title="DocuSign Sandboxed Button Test">
<form class="form-horizontal">
  <a 
  data-dynamicDocUrl="http://localhost:7611/financials/control/invoice.pdf?invoiceId=15120&reportId=FININVOICE&reportType=application/pdf" data-docName="invoice-00000" data-emailBody="invoice due 00/00/00" data-mode="sns" class="signhere btn btn-default">
    Sign And Send
  </a>
</form>

<form method="post" action="quote.pdf" name="toQuotePdf">
  <input type="hidden" name="reportType" value="application/pdf">
  <input type="hidden" name="reportId" value="SALESQUOTE">
  <input type="hidden" name="quoteId" value="10130">
</form>

<#--this block is needed by the script internally to get the info about the user session-->
<#--<#assign loginUrlKey=parameters.loginUrlKey?if_exists/>
<#if !loginUrlKey?has_content>
    <#assign loginUrlKey= requestAttributes.loginUrlKey?if_exists/>
</#if>-->
<script data-loginUrlKey="${tenantKey?if_exists}" data-userLoginId="${userLoginId?if_exists}" src="/docusign/main/dst/docusign-bundle.js" type="text/javascript"></script>

</@frameSection>
