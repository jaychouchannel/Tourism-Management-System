package com.utils;

import com.entity.EIException;
import java.util.*;

public class SqlIdentifierValidator {

    private static final Set<String> ALLOWED_TABLES = new HashSet<>(Arrays.asList(
        "baotuanxinxi", "cantingyuyue", "chatmessage", "config",
        "daoyou", "discussjiudianxinxi", "discusslvyougonglve",
        "discusslvyouxianlu", "discussmeishicanting", "discussremenjingdian",
        "forum", "friend", "friendlink", "jingdianleixing",
        "jiudianxinxi", "jiudianyuding", "kefangleixing",
        "lvyougonglve", "lvyouxianlu", "meishicanting",
        "menpiaodingdan", "news", "newstype", "qiujiu",
        "remenjingdian", "storeup", "token", "users", "yonghu",
        "xianluleixing"
    ));

    private static final String IDENTIFIER_PATTERN = "^[a-zA-Z][a-zA-Z0-9_]*$";

    private static final Map<String, Set<String>> ALLOWED_COLUMNS = new HashMap<>();

    static {
        ALLOWED_COLUMNS.put("users", new HashSet<>(Arrays.asList(
            "id", "username", "password", "image", "role", "addtime"
        )));
        ALLOWED_COLUMNS.put("yonghu", new HashSet<>(Arrays.asList(
            "id", "yonghuzhanghao", "mima", "yonghuxingming", "touxiang",
            "xingbie", "lianxifangshi", "status", "passwordwrongnum", "addtime"
        )));
        ALLOWED_COLUMNS.put("daoyou", new HashSet<>(Arrays.asList(
            "id", "daoyougonghao", "mima", "daoyouxingming", "touxiang",
            "zhuanyelingyu", "yuyannengli", "lianxifangshi", "gerenlvli",
            "status", "passwordwrongnum", "addtime"
        )));
        ALLOWED_COLUMNS.put("token", new HashSet<>(Arrays.asList(
            "id", "userid", "username", "tablename", "role", "token", "expiratedtime", "addtime"
        )));
        ALLOWED_COLUMNS.put("config", new HashSet<>(Arrays.asList(
            "id", "name", "value"
        )));
        ALLOWED_COLUMNS.put("forum", new HashSet<>(Arrays.asList(
            "id", "userid", "nickname", "content", "addtime"
        )));
        ALLOWED_COLUMNS.put("friend", new HashSet<>(Arrays.asList(
            "id", "userid", "friendid", "addtime"
        )));
        ALLOWED_COLUMNS.put("friendlink", new HashSet<>(Arrays.asList(
            "id", "name", "url", "addtime"
        )));
        ALLOWED_COLUMNS.put("jingdianleixing", new HashSet<>(Arrays.asList(
            "id", "leixingmingcheng", "addtime"
        )));
        ALLOWED_COLUMNS.put("jiudianxinxi", new HashSet<>(Arrays.asList(
            "id", "jiudianmingcheng", "jiudiantuijian", "tupian", "dizhi",
            "jiudianjieshao", "jiudianweizhi", "jiudianphone", "jiudianshoujia",
            "addtime"
        )));
        ALLOWED_COLUMNS.put("jiudianyuding", new HashSet<>(Arrays.asList(
            "id", "jiudianmingcheng", "jiudianleixing", "jiudianphone",
            "jiudiandizhi", "jiudiantupian", "yudingshuliang", "yudingjine",
            "beizhu", "xiadanren", "jiudianmingcheng", "yudingshijian", "addtime"
        )));
        ALLOWED_COLUMNS.put("kefangleixing", new HashSet<>(Arrays.asList(
            "id", "kefangleixing", "addtime"
        )));
        ALLOWED_COLUMNS.put("lvyougonglve", new HashSet<>(Arrays.asList(
            "id", "gonglvebianhao", "gonglvemingcheng", "fabuzhe", "tupian",
            "dizhi", "gonglvejieshao", "gonglveweizhi", "clicknum",
            "discussnum", "storeupnum", "addtime"
        )));
        ALLOWED_COLUMNS.put("lvyouxianlu", new HashSet<>(Arrays.asList(
            "id", "xianlubianhao", "xianlumingcheng", "chufadi", "mudidi",
            "xianlujiage", "tupian", "xianlujieshao", "clicknum",
            "discussnum", "storeupnum", "addtime"
        )));
        ALLOWED_COLUMNS.put("meishicanting", new HashSet<>(Arrays.asList(
            "id", "cantingmingcheng", "cantingdizhi", "cantingjieshao",
            "cantingphone", "cantingtupian", "cantingyuyue", "clicknum",
            "discussnum", "storeupnum", "addtime"
        )));
        ALLOWED_COLUMNS.put("menpiaodingdan", new HashSet<>(Arrays.asList(
            "id", "jingdianmingcheng", "jingdianleixing", "menpiaodanjia",
            "goumaishuliang", "zongjine", "beizhu", "xiadanren",
            "goumaishijian", "addtime"
        )));
        ALLOWED_COLUMNS.put("news", new HashSet<>(Arrays.asList(
            "id", "biaoti", "xiangqing", "faburiqi", "fabuzhe", "addtime"
        )));
        ALLOWED_COLUMNS.put("newstype", new HashSet<>(Arrays.asList(
            "id", "newstypename", "addtime"
        )));
        ALLOWED_COLUMNS.put("qiujiu", new HashSet<>(Arrays.asList(
            "id", "jiudianmingcheng", "jiudianleixing", "jiudianphone",
            "jiudiandizhi", "jiudiantupian", "fabuzhe", "addtime"
        )));
        ALLOWED_COLUMNS.put("remenjingdian", new HashSet<>(Arrays.asList(
            "id", "jingdianmingcheng", "jingdianleixing", "jingdiantupian",
            "jingdiandizhi", "jingdianjieshao", "clicknum", "discussnum",
            "storeupnum", "addtime"
        )));
        ALLOWED_COLUMNS.put("storeup", new HashSet<>(Arrays.asList(
            "id", "userid", "refid", "tablename", "name", "addtime"
        )));
        ALLOWED_COLUMNS.put("xianluleixing", new HashSet<>(Arrays.asList(
            "id", "xianluleixingmingcheng", "addtime"
        )));
        ALLOWED_COLUMNS.put("chatmessage", new HashSet<>(Arrays.asList(
            "id", "userid", "adminid", "ask", "reply", "isreply", "addtime"
        )));
        ALLOWED_COLUMNS.put("cantingyuyue", new HashSet<>(Arrays.asList(
            "id", "cantingmingcheng", "cantingdizhi", "cantingphone",
            "cantingtupian", "cantingyuyue", "yuyueren", "yuyueshijian",
            "beizhu", "addtime"
        )));
        ALLOWED_COLUMNS.put("baotuanxinxi", new HashSet<>(Arrays.asList(
            "id", "baotuanmingcheng", "baotuanjieshao", "chufadi", "mudidi",
            "chufashijian", "fanhuishijian", "jiage", "tupian", "clicknum",
            "discussnum", "storeupnum", "addtime"
        )));
        ALLOWED_COLUMNS.put("discussjiudianxinxi", new HashSet<>(Arrays.asList(
            "id", "refid", "userid", "content", "reply", "addtime"
        )));
        ALLOWED_COLUMNS.put("discusslvyougonglve", new HashSet<>(Arrays.asList(
            "id", "refid", "userid", "content", "reply", "addtime"
        )));
        ALLOWED_COLUMNS.put("discusslvyouxianlu", new HashSet<>(Arrays.asList(
            "id", "refid", "userid", "content", "reply", "addtime"
        )));
        ALLOWED_COLUMNS.put("discussmeishicanting", new HashSet<>(Arrays.asList(
            "id", "refid", "userid", "content", "reply", "addtime"
        )));
        ALLOWED_COLUMNS.put("discussremenjingdian", new HashSet<>(Arrays.asList(
            "id", "refid", "userid", "content", "reply", "addtime"
        )));
    }

    private static final Set<String> OPTION_SENSITIVE_COLUMNS = new HashSet<>(Arrays.asList(
        "password", "mima", "token", "expiratedtime", "passwordwrongnum"
    ));

    public static void validateTable(String tableName) {
        if (tableName == null || !tableName.matches(IDENTIFIER_PATTERN)) {
            throw new EIException("非法表名");
        }
        if (!ALLOWED_TABLES.contains(tableName.toLowerCase())) {
            throw new EIException("表名不允许");
        }
    }

    public static void validateColumn(String tableName, String columnName) {
        if (columnName == null || !columnName.matches(IDENTIFIER_PATTERN)) {
            throw new EIException("非法列名");
        }
        String table = tableName.toLowerCase();
        String column = columnName.toLowerCase();
        Set<String> allowed = ALLOWED_COLUMNS.get(table);
        if (allowed == null || !allowed.contains(column)) {
            throw new EIException("列名不允许");
        }
    }

    public static void validateOptionColumn(String tableName, String columnName) {
        validateColumn(tableName, columnName);
        if (OPTION_SENSITIVE_COLUMNS.contains(columnName.toLowerCase())) {
            throw new EIException("敏感字段不允许查询");
        }
    }
}
