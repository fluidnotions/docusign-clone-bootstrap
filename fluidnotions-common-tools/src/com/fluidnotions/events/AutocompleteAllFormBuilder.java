package com.fluidnotions.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityComparisonOperator;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.model.ModelEntity;

import com.fluidnotions.helper.FluidUtil;
import com.fluidnotions.models.AutocompleteAllFormBuilderSpec;
import com.fluidnotions.models.WhereCondition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;


public class AutocompleteAllFormBuilder {

    private static final String module = null;
    private static Gson gson = new GsonBuilder().create();

    public static String entityInfo(HttpServletRequest request, HttpServletResponse response) {
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String entityName = (String) request.getParameter("entityName");
	try {
	    Map<String, String> fieldType = entityFieldTypes(entityName, delegator);
	    String jsonStringResult = gson.toJson(fieldType);
	    return FluidUtil.doJSONResponse(response, jsonStringResult);
	} catch (Exception e) {
	    e.printStackTrace();
	    return "error";
	}

    }

    public static Map<String, String> entityFieldTypes(String entityName, Delegator delegator) {
	Map<String, String> fieldType = null;
	try {
	    ModelEntity modelEntity = delegator.getModelEntity(entityName);
	    List<String> fieldNames = modelEntity.getAllFieldNames();
	    fieldType = new HashMap<String, String>(fieldNames.size());
	    for (String name : fieldNames) {
		fieldType.put(name, modelEntity.getField(name).getType());
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return fieldType;

    }

//  {
//  inputFieldArray: [{
//      name: "firstName",
//      type: "text"
//  }, {
//      name: "lastName",
//      type: "text"
//  }, {
//      name: "email",
//      type: "email"
//  }, {
//      name: "userLoginId",
//      type: "text"
//  }],
//  formId: "addUserForm",
//  formButtonId: "addUserToTenantAccBtn",
//  formButtonName: "Add",
//  formButtonClasses: "btn btn-xl btn-primary pull-right",
//  lookupUrl: "/docusign-component/control/addDocusignUserSuggestFillForm",
//  targetSelector: "formWrapper",
//  templatesFolderPath: "/docusign/assets/main/templates/",
//  otherFormGroupTypesHtml: '<div class="form-group"><div class="col-sm-4"></div><div class="col-sm-8"><label><input class="cb" name="closeDocusignUser" type="checkbox"> close DocuSign User Profile</label></div></div><div class="form-group"><div class="col-sm-4"></div><div class="col-sm-8"><label><input class="cb" name="voidInProgressEnvelopes" type="checkbox"> Void users in-progress envelopes</label></div></div>'
//  entityName: "UserLoginPersonNameAndEmail",
//  fieldName: "", //form field user is typing in
//  searchTerm: "jus",//always case insensitive
//  whereConditionArray: [{
//      left: "contactMechPurposeTypeId",
//      operator: "EQUALS",
//      right: "PRIMARY_EMAIL",
//      type: "and"
//  }]
//}
    public static String dynamicAutocompleteAll(HttpServletRequest request, HttpServletResponse response) {
	Delegator delegator = (Delegator) request.getAttribute("delegator");
	String fieldName = (String) request.getParameter("fieldName");
	String searchTerm = (String) request.getParameter("searchTerm");
	String entityName = (String) request.getParameter("entityName");
	try {
	    AutocompleteAllFormBuilderSpec spec = gson.fromJson(FluidUtil.readJSONResquest(request),
		    AutocompleteAllFormBuilderSpec.class);
	    if (UtilValidate.isEmpty(fieldName)) {
		fieldName = spec.getFieldName();
	    }
	    if (UtilValidate.isEmpty(searchTerm)) {
		searchTerm = spec.getSearchTerm();
	    }
	    if (UtilValidate.isEmpty(entityName)) {
		entityName = spec.getEntityName();
	    }
	    EntityCondition finalcumlative = new EntityExpr(fieldName, true, EntityOperator.LIKE, searchTerm + "%",
		    true);// ignore case always on active input form field;

	    Map<String, String> fieldType = entityFieldTypes(entityName, delegator);
	    List<EntityCondition> andExprs = new ArrayList<EntityCondition>();
	    List<EntityCondition> orExprs = new ArrayList<EntityCondition>();

	    for (WhereCondition whereCondition : spec.getWhereConditionList()) {
		EntityOperator<Object, Object, Object> expression = EntityOperator.lookup(whereCondition.getOperator());
		boolean and = whereCondition.getType().equalsIgnoreCase("AND");
		boolean caseinsensitive = false;
		if (UtilValidate.isNotEmpty(whereCondition.getOptions())) {
		    String[] ops = { whereCondition.getOptions() };
		    if (whereCondition.getOptions().indexOf(";") > -1) {
			ops = whereCondition.getOptions().split(";");
		    }
		    caseinsensitive = ops[0].equalsIgnoreCase("case-insensitive");

		}
		EntityCondition condition = buildComparisonEntityCondition(caseinsensitive, whereCondition.getLeft(),
			fieldType.get(whereCondition.getLeft()), (EntityComparisonOperator) expression,
			whereCondition.getRight());
		if (and)
		    andExprs.add(condition);
		else
		    orExprs.add(condition);
	    }
	    EntityCondition combinedAnd = null;
	    if (andExprs.size() > 0)
		combinedAnd = EntityCondition.makeCondition(andExprs, EntityOperator.AND);
	    EntityCondition combinedOr = null;
	    if (orExprs.size() > 0)
		combinedOr = EntityCondition.makeCondition(orExprs, EntityOperator.OR);
	    // of course we can have no way of telling so just use and for now
	    finalcumlative = EntityCondition.makeCondition(combinedAnd, combinedOr);
	
	    List<GenericValue> resultList = delegator.findByConditionCache(entityName, finalcumlative, null, null);
	    if (UtilValidate.isNotEmpty(resultList)) {
		Set<Map<String, Object>> resultEntityArray = new HashSet<Map<String, Object>>();
		for (GenericValue gv : resultList) {
		    resultEntityArray.add(gv.getAllFields());
		}
		if (UtilValidate.isNotEmpty(resultEntityArray)) {
		    String jsonStringResult = gson.toJson(resultEntityArray);
		    Debug.logInfo("jsonStringResult: " + jsonStringResult, module);
		    return FluidUtil.doJSONResponse(response, jsonStringResult);
		} else {
		    return FluidUtil.doJSONResponse(response, "[]");
		}

	    }
	} catch (GenericEntityException e) {
	    e.printStackTrace();
	    return "error";
	} catch (JsonSyntaxException e) {
	    e.printStackTrace();
	    return "error";
	} catch (IOException e) {
	    e.printStackTrace();
	    return "error";
	}
	return "error";
    }

    private static EntityCondition buildComparisonEntityCondition(boolean caseinsensitive, String lhs, String fieldType,
	    EntityComparisonOperator operator, Object rhs) {
	EntityCondition newInst = new EntityExpr(lhs, caseinsensitive, operator, rhs, caseinsensitive);
	return newInst;
    }

}
