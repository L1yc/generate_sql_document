package com.demo;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "config")
@Data
public class Generate implements CommandLineRunner {

    @Autowired
    ApplicationContext applicationContext;

    private List<String> designated;

    private List<String> designatedPrefix;

    private List<String> designatedSuffix;

    private List<String> ignorePrefix;

    private List<String> ignoreTable;

    private List<String> ignoreSuffix;

    private String version;

    private String description;

    private String filePath;

    private EngineFileType fileType;


    @Override
    public void run(String... args) {

        DataSource dataSource = applicationContext.getBean(DataSource.class);
        EngineConfig engineConfig = EngineConfig.builder()
                .fileOutputDir(filePath)
                .openOutputDir(true)
                .fileType(fileType)
                .produceType(EngineTemplateType.freemarker).build();

        Configuration config = Configuration.builder()
                .version(version)
                .description(description)
                .dataSource(dataSource)
                .engineConfig(engineConfig)
                .produceConfig(getProcessConfig())
                .build();
        new DocumentationExecute(config).execute();
        System.exit(0);
    }

    private ProcessConfig getProcessConfig() {


        return ProcessConfig.builder()
                .designatedTableName(designated)
                .designatedTablePrefix(designatedPrefix)
                .designatedTableSuffix(designatedSuffix)
                .ignoreTableName(ignoreTable)
                .ignoreTablePrefix(ignorePrefix)
                .ignoreTableSuffix(ignoreSuffix)
                .build();


    }
}
