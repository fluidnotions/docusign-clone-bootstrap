<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl" />
<@frameSection title="DocuSign User Admin">
<#--this block is needed by the script internally to get the info about the user session-->
<#assign loginUrlKey=parameters.loginUrlKey?if_exists/>
<#if !loginUrlKey?has_content>
    <#assign loginUrlKey= requestAttributes.loginUrlKey?if_exists/>
</#if>
<#-- the iframe is needed to isolate the jquery module and avoid conflicts from older versions NB: in production remove debug=true -->
<iframe id="frame" src="/docusign/docusign.html?loginUrlKey=${tenantKey?if_exists}&debug=true&admin=true" class="embed-responsive-item" frameborder=0 scrolling="no" style="width:100%;height:200vh;display:block;overflow:hidden;"></iframe>
</@frameSection>
