package com.rigo.noo.db;

/**
 * Created by kbg82 on 2017-01-14.
 */

public interface NormalItemDBInfo {
    //table name
    final String ITEM_NORMAL_DB_TABLE_NAME		= "NormalItem";
    final String ITEM_RUN_DB_TABLE_NAME		= "RunItem";

    //field name
    final String ITEM_NORMAL_DB_FIELD_PEKY 		= "db_id";
    final String ITEM_NORMAL_DB_FIELD_STARTTIME = "db_start_time";
    final String ITEM_NORMAL_DB_FIELD_DISTANCE 	= "db_end_time";
    final String ITEM_NORMAL_DB_FIELD_DATA 	= "db_data";
    final String ITEM_NORMAL_DB_FIELD_FLAG 	= "db_flag";

    //field index
    final int ITEM_NORMAL_DB_FIELD_INDEX_PKEY = 0;
    final int ITEM_NORMAL_DB_FIELD_INDEX_STARTTIME = 1;
    final int ITEM_NORMAL_DB_FIELD_INDEX_DISTANCE = 2;
    final int ITEM_NORMAL_DB_FIELD_INDEX_DATA = 3;
    final int ITEM_NORMAL_DB_FIELD_INDEX_FLAGE = 4;
}
