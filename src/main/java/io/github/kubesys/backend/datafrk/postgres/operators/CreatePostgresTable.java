/*

 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.datafrk.postgres.operators;

import io.github.kubesys.backend.datafrk.operators.CreateTable;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.0
 *
 */
public class CreatePostgresTable extends CreateTable {

	protected CreatePostgresTable(String createTableCommand) {
		super(createTableCommand);
	}
}
