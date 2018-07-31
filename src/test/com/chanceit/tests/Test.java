package com.chanceit.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String json = "[{\"accountId\":6,\"point\":{\"x\":1,\"y\":2\"},\"accountName\":\"sys\",\"level\":{\"levelId\":2,\"levelName\":\"免费用户\",\"limitNum\":10},\"companyName\":\"sys公司名称\",\"accountTel\":null,\"address\":\"武汉\",\"email\":null,\"visitIp\":\"192.168.0.130\",\"createTime\":null,\"createIp\":null,\"visitTime\":\"2013-12-24\"}," +
				"{\"accountId\":6,\"point\":{\"x\":1,\"y\":2\"},\"accountName\":\"sys\",\"level\":{\"levelId\":2,\"levelName\":\"免费用户\",\"limitNum\":10},\"companyName\":\"sys公司名称\",\"accountTel\":null,\"address\":\"武汉\",\"email\":null,\"visitIp\":\"192.168.0.130\",\"createTime\":null,\"createIp\":null,\"visitTime\":\"2013-12-24\"}]";
		List list = JSONToList(json);
		
		String email=" ";
		System.out.println(StringUtils.isEmpty(email));
	}
	
	@SuppressWarnings( { "unchecked" })
	public static List<Map<String, String>> JSONToList(String string) {
		try {

			JSONArray Data_jsonArray = new JSONArray(string);
			if (!Data_jsonArray.isNull(0)) {
				JSONObject jobj = Data_jsonArray.getJSONObject(0);
				@SuppressWarnings("rawtypes")
				ArrayList keys = new ArrayList();
				int keys_posi = 0;
				Iterator<String> IT = jobj.keys();
				while (IT.hasNext()) {
					keys.add(keys_posi, IT.next());
					System.out.println(keys.get(keys_posi));
					keys_posi++;
				}

				List<Map<String, String>> list = new ArrayList<Map<String, String>>();

				for (int i = 0; i < Data_jsonArray.length(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					for (int j = 0; j < keys.size(); j++) {
						map.put(keys.get(j).toString(), Data_jsonArray
								.getJSONObject(i).getString(
										keys.get(j).toString()));
					}
					list.add(map);
				}
				return list;
			}
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
