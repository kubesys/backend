/**
 * Copyright (2020, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.operators;

import java.sql.ResultSet;


import io.github.kubesys.backend.SQLMapper;
import io.github.kubesys.backend.utils.ClientUtil;
import io.github.kubesys.datafrk.core.Table;
import io.github.kubesys.datafrk.core.operators.QueryData;

/**
 * @author wuheng09@gmail.com
 *
 */
public class GetTokenTest {

	
	public static void main(String[] args) throws Exception {
		SQLMapper sqlMapper = ClientUtil.sqlMapper();
		Table<?> table = null;
		for (Table<?> t : sqlMapper.listTables()) {
			if (t.name().equals("secrets")) {
				table = t;
				break;
			}
		}
		QueryData query = new QueryData("select name from secrets where region = 'local' and name like '%admin-token%'");
		ResultSet res = (ResultSet) table.query(query);
		res.next();
		System.out.println(res.getString("name"));
	}
		
}
