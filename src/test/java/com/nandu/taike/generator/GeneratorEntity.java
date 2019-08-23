package com.nandu.taike.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

/**
 * <p>
 * 测试生成代码
 * </p>
 *
 * @author YXL
 *
 */
public class GeneratorEntity {

    private  static final String URL = "jdbc:mysql://120.78.90.26:3306/STUDY_MINE?useUnicode=true&characterEncoding=utf8&useSSL=false";

    private  static final String USER_NAME = "root";

    private static final String PASS_WORD = "123456";

    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    @Test
    public void generateCode() {
        String packageName = "com.nandu.taike";
        String moduleName = "scSysLogInfo";
        String[] tableNames = {"TK_SYS_LOG_INFO"};   //表名[]，需要修改
        boolean serviceNameStartWithI = false;//user -> UserService, 设置成true: user -> IUserService
        generateByTables(serviceNameStartWithI, packageName,moduleName,tableNames);
        /*System.getProperty("user.dir");
        System.out.println(System.getProperty("user.dir"));*/
    }

    private void generateByTables(boolean serviceNameStartWithI, String packageName, String moduleName, String... tableNames){
        AutoGenerator mpg = new AutoGenerator();
        // 选择 freemarker 引擎，默认 Veloctiy
        // mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        //全局配置
        GlobalConfig config = new GlobalConfig();
        config.setOutputDir(System.getProperty("user.dir")+"\\src\\main\\java")
                .setFileOverride(true)
                .setActiveRecord(false)// 不需要ActiveRecord特性的请改为false
                .setEnableCache(false)// XML 二级缓存
                .setBaseResultMap(true)// XML ResultMap
                .setBaseColumnList(true)// XML columList
                // .setKotlin(true) // 是否生成 kotlin 代码
                .setAuthor("TK");
        if (!serviceNameStartWithI) {
            // 自定义文件命名，注意 %s 会自动填充表实体属性！
            // config.setMapperName("%sDao");
            // config.setXmlName("%sDao");
            // config.setServiceName("%sService");
            // config.setServiceImplName("%sServiceDiy");
            // config.setControllerName("%sAction");
            config.setServiceName("%sService");
        }
        mpg.setGlobalConfig(config);

        //数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        //更改数据库的时候要更改这个
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(URL)
                .setUsername(USER_NAME)
                .setPassword(PASS_WORD)
                .setDriverName(DRIVER_NAME);
        mpg.setDataSource(dataSourceConfig);

        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true)  // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
                .setEntityLombokModel(false)
//                .setDbColumnUnderline(true)
                .setNaming(NamingStrategy.underline_to_camel)// 表名生成策略
                .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组
        mpg.setStrategy(strategyConfig);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(packageName)
                //.setModuleName(moduleName)
                .setController("controller."+moduleName)
                .setService("service."+moduleName)
                .setServiceImpl("service."+moduleName+".impl")
                .setMapper("mapper."+moduleName)
                .setXml("mapper."+moduleName+".mappers")
                .setEntity("pojo."+moduleName);
        mpg.setPackageInfo(pc);

        mpg.execute();
    }
}