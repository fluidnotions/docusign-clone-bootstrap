<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl" />
<@frameSection title="Testing (iframe isolates from interference and makes it possible to see console logging)">
<#--this block is needed by the script internally to get the info about the user session-->
<#assign loginUrlKey=parameters.loginUrlKey?if_exists/>
<#if !loginUrlKey?has_content>
    <#assign loginUrlKey= requestAttributes.loginUrlKey?if_exists/>
</#if>
    <iframe id="frame" src="/docusign/myHome.html?loginKey=${loginUrlKey}" width="100%" height="800px"></iframe>
</@frameSection>
