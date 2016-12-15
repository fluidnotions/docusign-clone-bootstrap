<@import location="component://opentaps-common/webapp/common/includes/lib/opentapsFormMacros.ftl" />
<#--this block is needed by the script internally to get the info about the user session-->
<#assign loginUrlKey=parameters.loginUrlKey?if_exists/>
<#if !loginUrlKey?has_content>
    <#assign loginUrlKey= requestAttributes.loginUrlKey?if_exists/>
</#if>
<script data-loginKey="${loginUrlKey}" src="/docusign/assets/main/dst/docusign-bundle.js" type="text/javascript"></script>

<@frameSection title="DocuSign User Admin">
<div id="ds-ui-wrapper" style="display:none;">
    <div class="page-content-body" style="height:700px;width:100%">
        <ul class="nav nav-pills">
            <li class="active">
                <a data-show="showAddUserToTenantAccForm" href="#">Add User</a>
            </li>
            <li>
                <a data-show="showDisableUserForm" href="#">Disable User</a>
            </li>
            <li>
                <a data-show="showSetupDocumentForm" href="#">Set up Document</a>
            </li>
            <li>
                <a data-show="showEnvelopeStatusTable" href="#">Envelope Status</a>
            </li>
        </ul>

        <div id="docusign"></div>
        <div id="docusign-modal"></div>
    </div>

  </div>
  <div id="spinner" style="position: fixed;  top: 50%;  left: 50%;  margin-top: -50px;  margin-left: -100px;"></div>

    <script>

        var docusignviewhandler =
            setInterval(function() {
                if (window.ds) {
                    clearInterval(docusignviewhandler);
                    var ds = window.ds;
                    ds.showAddUserToTenantAccForm();

                    $(".nav").click(function(e) {
                        var show = e.target.dataset.show;
                        $(".nav-pills > li.active").removeClass("active");
                        $(e.target.parentElement).addClass("active");
                        console.log("show: " + show);
                        ds[show]();
                    });

                    setTimeout(function(){
                      $("#ds-ui-wrapper").css("display", "block");
                    }, 2000);
                }
            }, 800);
    </script>
</@frameSection>
