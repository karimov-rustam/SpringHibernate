package spring.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.logging.Logger;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "spring")
@PropertySource("classpath:persistence-mysql.properties")
public class DemoAppConfig {

    private final Environment env;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public DemoAppConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public DataSource dataSource() {

        ComboPooledDataSource dataSource = new ComboPooledDataSource();

//        set jdbc driver class
        try {
            dataSource.setDriverClass(env.getProperty("jdbc.driver"));
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        logger.info(">>> jdb.url=" + env.getProperty("jdbc.url"));
        logger.info(">>> jdb.user=" + env.getProperty("jdbc.user"));

//        set db connection props
        dataSource.setJdbcUrl(env.getProperty("jdbc.url"));
        dataSource.setUser(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.password"));

        dataSource.setInitialPoolSize(
                Integer.parseInt(env.getProperty("connection.pool.initialPoolSize")));
        dataSource.setMinPoolSize(
                Integer.parseInt(env.getProperty("connection.pool.minPoolSize")));
        dataSource.setMaxPoolSize(
                Integer.parseInt(env.getProperty("connection.pool.maxPoolSize")));
        dataSource.setMaxIdleTime(
                Integer.parseInt(env.getProperty("connection.pool.maxIdleTime")));

        return dataSource;
    }
}
