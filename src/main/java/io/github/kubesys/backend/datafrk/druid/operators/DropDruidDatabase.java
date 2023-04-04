/*

 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.datafrk.druid.operators;

import io.github.kubesys.backend.datafrk.operators.DropDatabase;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.0
 *
 */
public class DropDruidDatabase extends DropDatabase {

	
	public DropDruidDatabase(String name) {
		super(name);
	}

	@Override
	public String toSQL() {
		return "DROP database " + name;
	}

}
