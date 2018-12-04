package com.everyone.crowd.dao.sqlprovider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class DemandSQLProvider {

    public String countByMultipleConditions(Map<String, Object> params) {
        StringBuilder builder = new StringBuilder("SELECT COUNT(id) FROM t_demands WHERE");
        buildMultipleConditionsSQL(builder, params);
        System.out.println(builder.toString());
        return builder.toString();
    }

    public String findByMultipleConditions(Map<String, Object> params) {
        StringBuilder builder = new StringBuilder("SELECT * FROM t_demands WHERE");
        buildMultipleConditionsSQL(builder, params);
        builder.append(" LIMIT ").append(params.get("offset")).append(", ").append(params.get("size"));
        System.out.println(builder.toString());
        return builder.toString();
    }

    private void buildMultipleConditionsSQL(StringBuilder builder, Map<String, Object> params) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean firstCondition = true;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.contains("param") || key.equals("offset") || key.equals("size")) continue;

            if (value != null && !value.toString().isEmpty()) {
                if (!firstCondition) {
                    builder.append(" AND");
                } else {
                    firstCondition = false;
                }
                switch (key) {
                    case "keyword":
                        builder.append(" (title LIKE '%").append(value).append("%' OR detail LIKE '%")
                                .append(value).append("%')");
                        break;
                    case "categoryId":
                        builder.append(" category_id = ").append(value);
                        break;
                    case "region":
                        builder.append(" region = '").append(value).append("'");
                        break;
                    case "lowPrice":
                        builder.append(" price >= ").append(value);
                        break;
                    case "highPrice":
                        builder.append(" price <= ").append(value);
                        break;
                    case "startDateFrom":
                        builder.append(" start_date >= '").append(sdf.format((Date) value)).append("'");
                        break;
                    case "startDateTo":
                        builder.append(" start_date <= '").append(sdf.format((Date) value)).append("'");
                        break;
                    case "endDateFrom":
                        builder.append(" end_date >= '").append(sdf.format((Date) value)).append("'");
                        break;
                    case "endDateTo":
                        builder.append(" end_date <= '").append(sdf.format((Date) value)).append("'");
                        break;
                    case "status":
                        builder.append(" status = '").append(value).append("'");
                }
            }
        }
    }

}
