/*

 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.datafrk.druid.items;

import io.github.kubesys.backend.datafrk.items.JsonType;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.2
 *
 */
public abstract class DruidJsonType extends JsonType  {

	@Override
	public String type() {
		return "json";
	}

}
