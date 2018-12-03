package com.everyone.crowd.dao.sqlprovider;

import java.util.List;
import java.util.Map;

public class DevProfileSQLProvider {

    public String findByIds(Map<String, Object> params) {
        StringBuilder builder = new StringBuilder("SELECT * FROM t_devprofiles WHERE user_id IN (");
        List<Integer> ids = (List<Integer>) params.get("ids");
        for (Integer id : ids) {
            builder.append(id);
            builder.append(",");
        }
        if (ids.size() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append(")");
        return builder.toString();
    }
}
