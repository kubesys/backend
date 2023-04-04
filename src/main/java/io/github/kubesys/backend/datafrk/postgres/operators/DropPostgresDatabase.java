/*

 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.datafrk.postgres.operators;

import io.github.kubesys.backend.datafrk.druid.operators.DropDruidDatabase;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.0
 *
 */
public class DropPostgresDatabase extends DropDruidDatabase {

	
	public DropPostgresDatabase(String name) {
		super(name);
	}
}
