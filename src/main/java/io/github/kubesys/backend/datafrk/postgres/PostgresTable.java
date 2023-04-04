/*

 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.datafrk.postgres;

import io.github.kubesys.backend.datafrk.druid.DruidExecutor;
import io.github.kubesys.backend.datafrk.druid.DruidTable;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.0
 *
 */
public class PostgresTable extends DruidTable {

	public PostgresTable(DruidExecutor executor, String name) {
		super(executor, name);
	}

	@Override
	public String schema() {
		return this.executor.execWithValue(
				"SELECT generate_create_table_statement('" + name + "')", 
				"generate_create_table_statement").get(0);
	}
	
}
