package com.gether.bigdata.nosql.cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by myp on 2016/12/7.
 */
public class CassandraTest {
    @Before
    public void before() throws Throwable {
        afterPropertiesSet();
    }

    private static final byte[] sync = new byte[0];

    private Cluster cluster = null;
    private Session session;

    private String addresses = "127.0.0.1";
    private int port = 9042;
    private String username;
    private String password;
    private String clusterName = "myCluster";

    private String keyspace = "dbtest";
    private String TABLENAME_EXAMPLE = "example";

    public Session getSession() {
        return session;
    }

    private void afterPropertiesSet() {
        try {
            Cluster.Builder clusterBuilder = Cluster.builder();
            String[] adr = addresses.split(",");
            clusterBuilder.addContactPoints(adr);
            clusterBuilder.withPort(port);
            clusterBuilder.withClusterName(clusterName);
            PoolingOptions options = new PoolingOptions();
            options.setPoolTimeoutMillis(1000);
            //clusterBuilder.withPoolingOptions(options);
            // 自定义类型转换器
            //clusterBuilder.withCodecRegistry(null);
            clusterBuilder.withLoadBalancingPolicy(new RoundRobinPolicy());
            clusterBuilder.withMaxSchemaAgreementWaitSeconds(6000);
            clusterBuilder.withQueryOptions(new QueryOptions().setFetchSize(1));
            clusterBuilder.withTimestampGenerator(new ThreadLocalMonotonicTimestampGenerator());
            if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(username)) {
                clusterBuilder.withAuthProvider(new PlainTextAuthProvider(username, password));
            }

            cluster = clusterBuilder.build();
            QueryLogger queryLogger = QueryLogger.builder()
                    .withConstantThreshold(500L)
                    .withMaxQueryStringLength(100)
                    .build();
            cluster.register(queryLogger);
            session = cluster.connect(keyspace);
            ResultSet rs = session.execute("select * from system.local");
            //for (Row r : rs) {
            //    System.out.println(r.getColumnDefinitions());
            //    ColumnDefinitions def = r.getColumnDefinitions();
            //    for (ColumnDefinitions.Definition definition : def) {
            //        System.out.println(definition.getType());
            //        System.err.println(definition.getName() + ":" + r.getObject(definition.getName()));
            //    }
            //}
            Row row = rs.one();
            System.err.println(row.getString("release_version"));                          // (4)
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testCassandra() {
        System.out.println("connecting");
        ResultSet rs = getSession().execute("select * from example where field1 in (1 , 4)");
    }

    @Test
    public void testPagingList() {
        int pageSize = 2;
        getSession().getCluster().getConfiguration().getQueryOptions().setFetchSize(pageSize);
        Select where = QueryBuilder.select().all().from(keyspace, TABLENAME_EXAMPLE);
        ResultSet rs = getSession().execute(where);
        ExecutionInfo info = rs.getExecutionInfo();
        PagingState pageState = info.getPagingState();
        System.err.println(pageState.toString());
        System.out.println(Arrays.toString(pageState.toBytes()));
        //System.out.println(pageState.toBytes()));
        for (Row row : rs) {
            System.out.println(rs.getAvailableWithoutFetching() + ":" + rs.isFullyFetched());
            if (rs.getAvailableWithoutFetching() == pageSize && !rs.isFullyFetched())
                rs.fetchMoreResults(); // this is asynchronous
            System.out.println(row.getObject(0));
        }
    }

    @Test
    public void testInsert() {
        Insert insert = QueryBuilder.insertInto(keyspace, TABLENAME_EXAMPLE)
                .value("field1", 999)
                .value("field2", 333)
                .value("field3", 0);
        insert.setDefaultTimestamp(System.currentTimeMillis());
        getSession().execute(insert);
    }

    @Test
    public void testGet() {
        Select.Where where = QueryBuilder.select().all().from(keyspace, TABLENAME_EXAMPLE).where(QueryBuilder.eq("field1", 999));
        where.setConsistencyLevel(ConsistencyLevel.QUORUM);
        ResultSet rs = getSession().execute(where);
        for (Row row : rs) {
            printRow(row);
        }
    }

    public void printRow(Row r) {
        ColumnDefinitions def = r.getColumnDefinitions();
        for (ColumnDefinitions.Definition definition : def) {
            System.err.println(definition.getName() + ":" + r.getObject(definition.getName()));
        }
    }
}
