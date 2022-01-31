package com.seasongg;

import com.seasongg.config.security.util.CryptoUtil;
import com.seasongg.user.models.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import javax.sql.DataSource;

@Configuration
public class SggApplicationConfig {

    @Autowired
    CryptoUtil cryptoUtil;

    @Value("${SGG_DB_URL}")
    private String sggDbUrl;

    @Value("${SGG_DB_USER}")
    private String sggDbUser;

    @Value("${SGG_DB_PW}")
    private String sggDbPassword;

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:security"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public UserBuilder userBuilder(String username, String password, String passwordVerify) {
        return new UserBuilder(username, password, passwordVerify);
    }

    @Bean
    public DataSource getDataSource() {

        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();

        dataSourceBuilder.url(cryptoUtil.decrypt(sggDbUrl));
        dataSourceBuilder.username(cryptoUtil.decrypt(sggDbUser));
        dataSourceBuilder.password(cryptoUtil.decrypt(sggDbPassword));

        return dataSourceBuilder.build();

    }

}
