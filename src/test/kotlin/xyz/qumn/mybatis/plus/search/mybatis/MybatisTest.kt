package xyz.qumn.mybatis.plus.search.mybatis;

import com.baomidou.mybatisplus.core.MybatisConfiguration
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.transaction.TransactionFactory
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import org.h2.jdbcx.JdbcDataSource
import xyz.qumn.mybatis.plus.search.Entity.Person
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
        """create table person (
                id BIGINT NOT NULL,
                name VARCHAR(30) NULL,
                age INT NULL,
                PRIMARY KEY (id)
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

class MybatisTest : StringSpec({
    "mybatis plus should work" {
        val sessionFactory = getSessionFactory()
        val session = sessionFactory.openSession(true)
        val personMapper = session.getMapper(PersonMapper::class.java)
        val person = Person(name = "zs", age = 1)
        personMapper.insert(person)
        val personSelected = personMapper.selectById(person.id)
        personSelected shouldNotBe null
        personSelected.name shouldBe person.name
        personSelected.age shouldBe person.age
    }
})
