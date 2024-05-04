//package top.recordsite;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import top.recordsite.utils.RedisUtlis.RedisUtils;
//
//import javax.annotation.Resource;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.SynchronousQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//@SpringBootTest
//public class RedisTest {
//
//    @Autowired
//    private RedisUtils redisUtils;
//    @Resource
//    private RedissonClient redissonClient;
//
//    @Test
//    void test() {
//        long ttl = redisUtils.ttl("user_login14");
//        System.out.println("ttl = " + TimeUnit.SECONDS.toDays(ttl));
//    }
//
//    @Test
//    void delete() {
//        boolean testqeq = redisUtils.del("testqeq");
//        System.out.println("testqeq = " + testqeq);
//    }
//
//    @Test
//    void testRedisson() {
//        Integer productId = 1;
//        RLock lock = redissonClient.getLock("stock:" + productId);
//        log.info("开启锁");
//        lock.lock(10, TimeUnit.SECONDS);
//        log.info("抢到锁");
//        lock.unlock();
//    }
//
//    @Test
//    void testT() {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        ExecutorService executorService = new ThreadPoolExecutor(
//                10, 10,
//                1, TimeUnit.SECONDS, new SynchronousQueue<>()
//        );
//        for (int i = 0; i < 100; i++) {
//            executorService.execute(() -> new RedisTest().testRedisson());
//        }
//    }
//
//}
