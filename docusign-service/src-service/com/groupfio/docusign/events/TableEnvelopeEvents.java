package com.groupfio.docusign.events;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.jdbc.SQLProcessor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.groupfio.docusign.OTUtil;
import com.groupfio.docusign.ResultSetConvertor;

public class TableEnvelopeEvents {

    private static final String module = TableEnvelopeEvents.class.getName();
    private static Gson gson = new GsonBuilder().create();

    public static String getEnvelopeStatusData(HttpServletRequest request, HttpServletResponse response) {
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	Debug.logInfo("should be using tenant delegator, current delegator name: " + delegator.getDelegatorName(),
		module);
	String tenantKey = request.getParameter("tenantKey");
	// FIXME for testing only
	if (tenantKey == null) {
	    tenantKey = "111111";
	}

	if (UtilValidate.isEmpty(delegator.getDelegatorTenantId())) {
	    delegator = OTUtil.getTenantDelegator(tenantKey);
	}

	Integer offset = new Integer(request.getParameter("offset"));
	Integer fetchSize = new Integer(request.getParameter("limit"));

	Debug.logInfo("pageIndex: " + offset + ",fetchSize: " + fetchSize, module);

	String sql = "SELECT SQL_CALC_FOUND_ROWS * FROM " + "DOCU_SIGN_ENVELOPE ORDER BY  LAST_UPDATED_STAMP DESC "
		+ " LIMIT " + offset + ", " + fetchSize;

	Debug.logInfo(sql, module);
	SQLProcessor sqlProcessor = new SQLProcessor(delegator.getGroupHelperInfo("org.ofbiz"));
	String jsonArray = "[]";
	try {
	    ResultSet rs = sqlProcessor.executeQuery(sql);

	    JSONArray jsArr = ResultSetConvertor.convertResultSetIntoJSON(rs);
	    if (jsArr != null) {
		jsonArray = wrapDataForDataTablesJS(jsArr);
	    }

	} catch (GenericDataSourceException e) {
	    e.printStackTrace();
	} catch (GenericEntityException e) {
	    e.printStackTrace();
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}

	Debug.logInfo("result: " + jsonArray, module);
	return doJSONResponse(response, jsonArray);

    }

    // {
    // "draw": 1,
    // "recordsTotal": 57,
    // "recordsFiltered": 57,
    // "data": [

    private static String wrapDataForDataTablesJS(JSONArray jsArr) {
	Map<String, Object> wrapperData = new HashMap<String, Object>();
	wrapperData.put("total", jsArr.length()*3);
	wrapperData.put("rows", jsArr.toString());

	return gson.toJson(wrapperData);

    }

    public static String doJSONResponse(HttpServletResponse response, String jsonString) {
	String result = "success";

	response.setContentType("application/x-json");
	try {
	    response.setContentLength(jsonString.getBytes("UTF-8").length);
	} catch (UnsupportedEncodingException e) {
	    Debug.logWarning(
		    "Could not get the UTF-8 json string due to UnsupportedEncodingException: " + e.getMessage(), "");
	    response.setContentLength(jsonString.length());
	}

	Writer out;
	try {
	    out = response.getWriter();
	    out.write(jsonString);
	    out.flush();
	} catch (IOException e) {
	    Debug.logError(e, "Failed to get response writer", "");
	    result = "error";
	}
	return result;
    }

}
