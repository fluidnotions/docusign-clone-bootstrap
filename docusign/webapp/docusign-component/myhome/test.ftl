<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl" />
<#--this block is needed by the script internally to get the info about the user session-->
<#assign loginUrlKey=parameters.loginUrlKey?if_exists/>
<#if !loginUrlKey?has_content>
    <#assign loginUrlKey= requestAttributes.loginUrlKey?if_exists/>
</#if>

<#-- data-admin="true" -->
 <#-- data-debug="true"  -->
<@frameSection title="DocuSign Sandboxed Button Test">
<form class="form-horizontal">
  <a data-callfnnamed="dsstart"  data-dynamicDocUrl="http://localhost:7611/financials/control/invoice.pdf?invoiceId=15120&reportId=FININVOICE&reportType=application/pdf" data-docName="invoice-00000" data-emailBody="invoice due 00/00/00" data-mode="sns" class="signhere btn btn-default pull-left">
    Sign And Send
  </a>
</form>

<iframe id="dssandbox" src="/docusign/docusign.html?loginKey=${loginUrlKey}&debug=true" class="embed-responsive-item" frameborder=0 scrolling="no" style="width:0%;height:0%;display:block;overflow:hidden;"></iframe>
<script src="/docusign/assets/main/dst/docusign-proxy.min.js" type="text/javascript"></script>

</@frameSection>
