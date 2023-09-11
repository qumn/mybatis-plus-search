package xyz.qumn.mybatis.plus.search.util

import com.baomidou.mybatisplus.core.MybatisConfiguration
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.transaction.TransactionFactory
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import org.h2.jdbcx.JdbcDataSource
import xyz.qumn.mybatis.plus.search.mapper.PersonMapper
import java.sql.Connection
import java.sql.Statement
import javax.sql.DataSource



fun getDataSource(): DataSource {
    val dataSource = JdbcDataSource()
    dataSource.setURL("jdbc:h2:mem:test")
    dataSource.user = "sa"

    dataSource.password = ""
    val connection: Connection = dataSource.getConnection()
    val statement: Statement = connection.createStatement()
    statement.execute(
        """
            drop table if exists person;
            create table person (
                uid BIGINT NOT NULL,
                uname VARCHAR(30) NULL,
                age INT NULL,
                create_at datetime default now(),
                PRIMARY KEY (uid)
                )"""
    )
    return dataSource
}

fun getSessionFactory(): SqlSessionFactory {
    val dataSource: DataSource = getDataSource()
    val transactionFactory: TransactionFactory = JdbcTransactionFactory()
    val environment = Environment("Production", transactionFactory, dataSource)
    val configuration = MybatisConfiguration(environment)
    configuration.addMapper(PersonMapper::class.java)
    return MybatisSqlSessionFactoryBuilder().build(configuration)
}

