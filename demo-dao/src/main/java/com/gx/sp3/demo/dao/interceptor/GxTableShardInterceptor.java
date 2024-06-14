package com.gx.sp3.demo.dao.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.gx.sp3.demo.dao.annotation.GxTableShard;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}
)})
public class GxTableShardInterceptor implements Interceptor {
    /**
     *
     */
    private static final ReflectorFactory defaultReflectorFactory = new DefaultReflectorFactory();

    /**
     *
     */
    private static final String DELEGATE_MAPPER_STATEMENT = "delegate.mappedStatement";
    private static final String DELEGATE_BOUND_SQL = "delegate.boundSql";
    private static final String DELEGATE_BOUND_SQL_SQL = "delegate.boundSql.sql";

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {}

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        String originalSql = statementHandler.getBoundSql().getSql();
        System.out.printf("!!! TableShardInterceptor#intercept originalSql=%s\n", originalSql);

        // 获取mapper元信息
        MetaObject metaObject = MetaObject.forObject(statementHandler,
                SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                defaultReflectorFactory);
        // 获取MappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue(DELEGATE_MAPPER_STATEMENT);
        // 获取SQL元数据
        BoundSql boundSql = (BoundSql) metaObject.getValue(DELEGATE_BOUND_SQL);
        System.out.printf("!!! TableShardInterceptor#intercept boundSql=%s\n", boundSql.getSql());

        // 拦截分表逻辑，获取注解
        GxTableShard annotation = getAnnotation(mappedStatement);
        // 当前方法不包含注解，走默认
        if (annotation == null || !annotation.tableShardOpen()) {
            return invocation.proceed();
        }

        // 获取入参值，根据入参值进行分表
        Map<String, String> columnValueMap = getColumnValue(annotation.columns(), boundSql);
        // 获取替换表名称
        String shardName = annotation.shardName();
        // 获取默认表名称
        String defaultName = annotation.defaultName();
        // 新表名称
        String newName = defaultName;

        String tableName = "your_table_" + computeTableSuffix(columnValueMap); // 根据实际逻辑计算表后缀
        String modifiedSql = originalSql.replaceAll("your_table", tableName);
        System.out.printf("!!! TableShardInterceptor#intercept originalSql2=%s\n", originalSql);

        // 拦截并替换sql
        metaObject.setValue(DELEGATE_BOUND_SQL_SQL, modifiedSql);
        return invocation.proceed();
    }

    private String computeTableSuffix(Map<String, String> columnValueMap) {
        System.out.printf("!!! TableShardInterceptor#computeTableSuffix columnValues=%s\n", columnValueMap);
        // 实现分表逻辑，如通过用户ID取模等
        return "01"; // 示例返回值
    }

    private GxTableShard getAnnotation(MappedStatement mappedStatement) throws ClassNotFoundException {
        String id = mappedStatement.getId();
        String className = id.substring(0, id.lastIndexOf("."));
        String methodName = id.substring(id.lastIndexOf(".") + 1);
        // 获取pageHelper拦截方法
        if (methodName.endsWith("_COUNT")) {
            methodName = methodName.replace("_COUNT", "");
        }
        Class<?> aClass = Class.forName(className);
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().equals(methodName)) {
                return declaredMethod.getAnnotation(GxTableShard.class);
            }
        }
        return null;
    }

    /**
     * 获取sql参数值
     *
     * @param columns  参数名称
     * @param boundSql sql元数据
     * @return 参数列表
     */
    private Map<String, String> getColumnValue(String[] columns, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        if (parameterObject == null) {
            throw new RuntimeException("分表参数异常！");
        }

        // 判断参数类型获取参数值
        Map<String, Object> paramMap;
        if (parameterObject instanceof MapperMethod.ParamMap) { // 查询条件是ParamMap
            paramMap = (MapperMethod.ParamMap) parameterObject;
        } else if (parameterObject instanceof Map) { // 查询条件是Map
            paramMap = (Map) parameterObject;
        } else { // 查询条件是其他bean
            paramMap = BeanUtil.beanToMap(parameterObject);
        }

        Map<String, String> resultMap = new HashMap<>();
        for (int i = 0; i < columns.length; i++) {
            if (paramMap.containsKey(columns[i])) {
                resultMap.put(columns[i], String.valueOf(paramMap.get(columns[i])));
            } else {
                resultMap.put(columns[i], null);
            }
        }
        return resultMap;
    }
}
