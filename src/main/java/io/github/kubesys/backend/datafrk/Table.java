/*

 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.datafrk;

import io.github.kubesys.backend.datafrk.operators.InsertData;
import io.github.kubesys.backend.datafrk.operators.QueryData;
import io.github.kubesys.backend.datafrk.operators.RemoveData;
import io.github.kubesys.backend.datafrk.operators.UpdateData;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.0
 *
 */
public interface Table<T> extends Schema {
	
	public T query(QueryData query);
	
	public boolean insert(InsertData insert);
	
	public boolean update(UpdateData update);
	
	public boolean delete(RemoveData delete);
	
	public String name();
}
