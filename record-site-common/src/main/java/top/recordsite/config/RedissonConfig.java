//package top.recordsite.config;
//
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RedissonConfig {
//
//    @Bean(destroyMethod = "shutdown") //bean销毁时关闭Redisson实例，但不关闭Redis服务
//    public RedissonClient redissonClient(){
//        //创建配置
//        Config config=new Config();
//        /**
//         *  连接哨兵：config.useSentinelServers().setMasterName("myMaster").addSentinelAddress()
//         *  连接集群： config.useClusterServers().addNodeAddress()
//         */
//        String host="localhost";
//        Integer port=6379;
//
//        config.useSingleServer()
//                .setDatabase(1)
//                .setAddress("redis://" + host + ":" + port);
////                .setPassword(password)
////                .setTimeout(5000);
//        //根据config创建出RedissonClient实例
//        return Redisson.create(config);
//    }
//
//}
