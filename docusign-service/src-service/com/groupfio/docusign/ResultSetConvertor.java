package com.groupfio.docusign;

import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utility for converting ResultSets into some Output formats
 */
public class ResultSetConvertor {

    /**
     * Convert a result set into a JSON Array
     * @param resultSet
     * @return a JSONArray
     * @throws Exception
     */
    public static JSONArray convertResultSetIntoJSON(ResultSet resultSet) throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                String columnName = resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase();
                Object columnValue = resultSet.getObject(i + 1);
                // if value in DB is null, then we set it to default value
                if (columnValue == null){
                    columnValue = "";
                }
                
                obj.put(convertUnderscoreNameToPropertyName(columnName), columnValue);
            }
            jsonArray.put(obj);
        }
        return jsonArray;
    }
    
    private static String convertUnderscoreNameToPropertyName(String name) {
		StringBuilder result = new StringBuilder();
		boolean nextIsUpper = false;
		if (name != null && name.length() > 0) {
			if (name.length() > 1 && name.substring(1, 2).equals("_")) {
				result.append(name.substring(0, 1).toUpperCase());
			} else {
				result.append(name.substring(0, 1).toLowerCase());
			}
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				if (s.equals("_")) {
					nextIsUpper = true;
				} else {
					if (nextIsUpper) {
						result.append(s.toUpperCase());
						nextIsUpper = false;
					} else {
						result.append(s.toLowerCase());
					}
				}
			}
		}
		return result.toString();
	}

    
}