package com.semenov.rightnabove.junit.config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

@Configuration
public class TestConfiguration {

	private DataSource dataSource;

	private Connection connection;
	private PostgresProcess process;

	@Before
	public void init() throws IOException, SQLException {
		PostgresStarter<PostgresExecutable, PostgresProcess> runtime = PostgresStarter
				.getDefaultInstance();
		final PostgresConfig config = PostgresConfig
				.defaultWithDbName("rightnabove");
		PostgresExecutable exec = runtime.prepare(config);
		process = exec.start();

		String url = String.format("jdbc:postgresql://%s:%s/%s", config.net()
				.host(), config.net().port(), config.storage().dbName());

		connection = DriverManager.getConnection(url);
		dataSource = new SingleConnectionDataSource(connection, false);
	}

	@After
	public void cleanup() throws SQLException {
		connection.close();
		process.stop();
	}

	@Bean
	public DataSource dataSource() {
		return dataSource;
	}

}
