package com.example;


import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * @author zhl
 * @version 1.0
 * @date 2021/12/10 10:49
 * @classDesc: 功能描述:
 * @description
 */
public class ActivitiTest {
    /**
     * 生成Activiti的数据库表
     */
    @Test
    public void testCreateDbTable() {
        //默认创建方式
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //通用的创建方式，指定配置文件和bean名称,见testCreateDbTable2
        System.out.println(processEngine);
    }

    @Test
/**
 * 运行此方法，加载activiti的数据库表信息
 */
    public void testCreateDbTable2() {
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        processEngineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/activitiDemo?serverTimezone=GMT&nullCatalogMeansCurrent=true&useUnicode=true&characterEncoding=utf8");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("root");

        /**
         public static final String DB_SCHEMA_UPDATE_FALSE = "false";不能自动创建表，需要表存在
         public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";先删除表再创建表
         public static final String DB_SCHEMA_UPDATE_TRUE = "true";如果表不存在，自动创建表
         */
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
    }
    /**
     * 部署流程定义  文件上传方式
     */
    @Test
    public void testDeployment(){
//        1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2、得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        3、使用RepositoryService进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/Leave.bpmn") // 添加bpmn资源
                //png资源命名是有规范的。Leave.myLeave.png|jpg|gif|svg  或者Leave.png|jpg|gif|svg
                .addClasspathResource("bpmn/Leave.myLeave.png")  // 添加png资源
                .name("请假申请流程")
                .deploy();
//        4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }
    /**
     * zip压缩文件上传方式
     */
    @Test
    public void deployProcessByZip() {
        // 定义zip输入流
        InputStream inputStream = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream(
                        "bpmn/Leave.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 获取repositoryService
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine
                .getRepositoryService();
        // 流程部署
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }
}
