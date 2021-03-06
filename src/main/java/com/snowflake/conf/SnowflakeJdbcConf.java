/*
 * Copyright (c) 2018 Snowflake Computing Inc. All right reserved.
 */
package com.snowflake.conf;

import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class SnowflakeJdbcConf extends Configuration
{
  private static final Logger log = LoggerFactory.getLogger(SnowflakeJdbcConf.class);
  public enum ConfVars
  {
    SNOWFLAKE_JDBC_USERNAME("snowflake.jdbc.username", "user",
      "The user to use to connect to Snowflake."),
    SNOWFLAKE_JDBC_PASSWORD("snowflake.jdbc.password", "password",
      "The password to use to connect to Snowflake."),
    SNOWFLAKE_JDBC_ACCOUNT("snowflake.jdbc.account", "account",
      "The account to use to connect to Snowflake."),
    SNOWFLAKE_JDBC_DB("snowflake.jdbc.db", "db",
      "The database to use to connect to Snowflake."),
    SNOWFLAKE_JDBC_SCHEMA("snowflake.jdbc.schema", "schema",
      "The schema to use to connect to Snowflake."),
    SNOWFLAKE_JDBC_SSL("snowflake.jdbc.ssl", "ssl",
      "Use ssl to connect to Snowflake"),
    SNOWFLAKE_JDBC_CONNECTION("snowflake.jdbc.connection", "connection",
      "The Snowflake connection string.");

    public static final Map<String, ConfVars> BY_VARNAME =
        Arrays.stream(ConfVars.values())
            .collect(Collectors.toMap(e -> e.varname, e -> e));

    public static ConfVars findByName(String varname)
    {
      return BY_VARNAME.get(varname);
    }

    ConfVars(String varname, String snowflakePropertyName,
             String description)
    {
      this.varname = varname;
      this.snowflakePropertyName = snowflakePropertyName;
      this.description = description;
    }

    public String getVarname()
    {
      return this.varname;
    }

    public String getSnowflakePropertyName()
    {
      return this.snowflakePropertyName;
    }

    private final String varname;
    private final String snowflakePropertyName;
    private final String description;
  }

  private static URL snowflakeConfigUrl;

  static
  {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    if (classLoader == null)
    {
      classLoader = SnowflakeJdbcConf.class.getClassLoader();
    }

    snowflakeConfigUrl = findConfigFile(classLoader, "snowflake-config.xml");
  }

  private static URL findConfigFile(ClassLoader classLoader, String name)
  {
    URL result = classLoader.getResource(name);
    if(result == null)
    {
      log.error("Could not find the Snowflake configuration file. " +
          "Ensure that it's named snowflake-config.xml and " +
          "it is in the hive classpath");
    }
    return result;
  }

  private void initialize()
  {
    if (snowflakeConfigUrl != null)
    {
      addResource(snowflakeConfigUrl);
    }
  }

  public SnowflakeJdbcConf()
  {
    super(false);
    initialize();
  }

}
