package com.groupfio.docusign.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.model.ModelEntity;
import org.ofbiz.entity.util.EntityUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.groupfio.docusign.OTUtil;

public class DocusignSuggestEvents {

	private static final String module = null;
	private static Gson gson = new GsonBuilder().create();

	@SuppressWarnings("deprecation")
	public static String disableDocusignUserSuggest(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");

		String fieldName = (String) request.getParameter("fieldName");
		String searchTerm = (String) request.getParameter("searchTerm");

		try {

			EntityCondition[] suggestConditions = {
					new EntityExpr(fieldName, true, EntityOperator.LIKE, searchTerm + "%", true),
					EntityCondition.makeCondition("enabled", EntityOperator.EQUALS, "Y"),
					EntityCondition.makeCondition("contactMechPurposeTypeId", EntityOperator.EQUALS, "PRIMARY_EMAIL"),
					EntityCondition.makeCondition("name", EntityOperator.NOT_EQUAL, null),
					EntityCondition.makeCondition("docuSignUserId", EntityOperator.NOT_EQUAL, null) };

			List<EntityCondition> exprs = new ArrayList<EntityCondition>(Arrays.asList(suggestConditions));

			EntityCondition combined = EntityCondition.makeCondition(exprs, EntityOperator.AND);

			List<GenericValue> resultList = delegator.findByConditionCache("DocusignUserLoginPersonNameAndEmail", combined, null,
					UtilMisc.toList("purposeFromDate ASC"));

			if (UtilValidate.isNotEmpty(resultList)) {
				Set<String> ensureUnque = new HashSet<String>();
				List<Map<String, Object>> resultEntityArray = new ArrayList<Map<String, Object>>();
				for (GenericValue gv : resultList) {
					if (ensureUnque.add(gv.getString("userLoginId"))) {
						resultEntityArray.add(gv.getAllFields());
					}

				}
				if (UtilValidate.isNotEmpty(resultEntityArray)) {
					String jsonStringResult = gson.toJson(resultEntityArray);
					Debug.logInfo("jsonStringResult: " + jsonStringResult, module);
					return OTUtil.doJSONResponse(response, jsonStringResult);
				} else {
					return OTUtil.doJSONResponse(response, "[]");
				}

			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return "error";
		}
		return "error";
	}

	@SuppressWarnings("deprecation")
	public static String addDocusignUserSuggest(HttpServletRequest request, HttpServletResponse response) {
		Delegator delegator = (Delegator) request.getAttribute("delegator");

		String fieldName = (String) request.getParameter("fieldName");
		String searchTerm = (String) request.getParameter("searchTerm");

		try {

			EntityCondition[] suggestConditions = {
					new EntityExpr(fieldName, true, EntityOperator.LIKE, searchTerm + "%", true),// ignore
																									// case

					EntityCondition.makeCondition("email", EntityOperator.NOT_EQUAL, null),
					EntityCondition.makeCondition("firstName", EntityOperator.NOT_EQUAL, null),
					EntityCondition.makeCondition("contactMechId", EntityOperator.NOT_EQUAL, null),
					EntityCondition.makeCondition("lastName", EntityOperator.NOT_EQUAL, null),
					EntityCondition.makeCondition("userLoginId", EntityOperator.NOT_EQUAL, null),
					EntityCondition.makeCondition("contactMechPurposeTypeId", EntityOperator.EQUALS, "PRIMARY_EMAIL") };

			List<EntityCondition> exprs = new ArrayList<EntityCondition>(Arrays.asList(suggestConditions));

			EntityCondition combined = EntityCondition.makeCondition(exprs, EntityOperator.AND);

			List<GenericValue> resultList = EntityUtil.filterByDate(
					delegator.findByConditionCache("UserLoginPersonNameAndEmail", combined, null, UtilMisc.toList("fromDate DESC")), true);
			;

			if (UtilValidate.isNotEmpty(resultList)) {
				Set<String> ensureUnque = new HashSet<String>();
				List<Map<String, Object>> resultEntityArray = new ArrayList<Map<String, Object>>();
				for (GenericValue gv : resultList) {
					if (ensureUnque.add(gv.getString("userLoginId"))) {
						resultEntityArray.add(gv.getAllFields());
					}

				}
				if (UtilValidate.isNotEmpty(resultEntityArray)) {
					String jsonStringResult = gson.toJson(resultEntityArray);
					Debug.logInfo("jsonStringResult: " + jsonStringResult, module);
					return OTUtil.doJSONResponse(response, jsonStringResult);
				} else {
					return OTUtil.doJSONResponse(response, "[]");
				}

			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return "error";
		}
		return "error";
	}

}
