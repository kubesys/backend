/*

 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.datafrk;

import io.github.kubesys.backend.datafrk.operators.CheckDatabase;
import io.github.kubesys.backend.datafrk.operators.CreateDatabase;
import io.github.kubesys.backend.datafrk.operators.DropDatabase;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.0
 *
 */
public interface DataContext {
	
	public Database currentDatabase();

	public boolean createDatabase(CreateDatabase createDatabase);
	
	public boolean checkDababase(CheckDatabase checkDatabase);
	
	public boolean dropDababase(DropDatabase dropDatabase);
	
	public String defaultDriver();
	
}
