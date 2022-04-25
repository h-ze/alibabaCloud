import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
//classes属性指定启动类，SpringBootTest.WebEnvironment.RANDOM_PORT经常和测试类中@LocalServerPort一起在注入属性时使用。会随机生成一个端口号。
@SpringBootTest(classes = UserTest.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

// 由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上
@WebAppConfiguration

//写单元测试，难免需要操作数据库。有时候单元测试的数据库跟开发时候的数据库是同一个，为了不影响数据库的数据，需要在单测完成之后，将操作回滚。这在springboot中也是很容易解决的事情，只需要将单测类继承AbstractTransactionalJUnit4SpringContextTests即可
public class UserTest extends AbstractTransactionalJUnit4SpringContextTests {

    @BeforeClass
    public static void setup() throws Exception {

    }


    @Test
    public void testUser(){

    }
}
