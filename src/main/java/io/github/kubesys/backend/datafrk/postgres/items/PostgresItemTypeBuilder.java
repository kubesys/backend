/*

 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.datafrk.postgres.items;

import io.github.kubesys.backend.datafrk.items.DatatimeType;
import io.github.kubesys.backend.datafrk.items.ItemTypeBuilder;
import io.github.kubesys.backend.datafrk.items.JsonType;
import io.github.kubesys.backend.datafrk.items.VarcharType;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.2
 *
 */
public class PostgresItemTypeBuilder implements ItemTypeBuilder  {

	@Override
	public DatatimeType datatime() {
		return new PostgresDatatimeType();
	}

	@Override
	public VarcharType varchar(int len) {
		return new PostgresVarcharType(len);
	}

	@Override
	public JsonType json() {
		return new PostgresJsonType();
	}

}
