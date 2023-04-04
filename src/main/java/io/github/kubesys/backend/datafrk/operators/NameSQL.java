/*

 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.datafrk.operators;

import io.github.kubesys.backend.datafrk.SQL;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.0
 *
 */
public abstract class NameSQL implements SQL {
	
	protected final String name;

	public NameSQL(String name) {
		super();
		this.name = name;
	}
}
