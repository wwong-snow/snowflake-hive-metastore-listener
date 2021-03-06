/*
 * Copyright (c) 2018 Snowflake Computing Inc. All right reserved.
 */
package com.snowflake.core.commands;

import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.metastore.events.DropTableEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for the DropExternalTable command
 */
public class DropExternalTable implements Command
{

  public DropExternalTable(DropTableEvent dropTableEvent)
  {
    this.hiveTable = dropTableEvent.getTable();
  }

  /**
   * Generates the command for drop external table
   * @return
   */
  private String generateDropTableCommand()
  {
    StringBuilder sb = new StringBuilder();

    // drop table command
    sb.append("DROP EXTERNAL TABLE ");
    sb.append(hiveTable.getTableName());
    sb.append(";");

    return sb.toString();
  }

  /**
   * Generates the command for drop stage
   * @return
   * @throws Exception
   */
  private String generateDropStageCommand()
  throws Exception
  {
    StringBuilder sb = new StringBuilder();

    // drop stage command
    sb.append("DROP STAGE ");
    sb.append(hiveTable.getTableName());
    sb.append(";");

    return sb.toString();
  }

  /**
   * Generates the necessary commands on a hive drop table event
   */
  public List<String> generateCommands()
    throws Exception
  {
    List<String> queryList = new ArrayList<>();

    String dropTableQuery = generateDropTableCommand();

    queryList.add(dropTableQuery);

    String dropStageQuery = generateDropStageCommand();
    queryList.add(dropStageQuery);

    return queryList;
  }

  private Table hiveTable;
}
