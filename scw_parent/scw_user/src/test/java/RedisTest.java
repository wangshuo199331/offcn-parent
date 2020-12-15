import com.offcn.UserStarter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserStarter.class})
public class RedisTest {

    /*@Autowired
    private RedisTemplate redisTemplate;*/
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testString(){
        stringRedisTemplate.boundValueOps("testCode").set("123456");
        System.out.println("redis存储了12456");
    }
}
