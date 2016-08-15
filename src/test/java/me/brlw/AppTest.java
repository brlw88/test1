package me.brlw;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import me.brlw.test1.User;
import me.brlw.test1.UserService;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;

import javax.validation.constraints.*;

/**
 * Unit test for simple App.
 */
public class AppTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        final String FIRST_USER_NAME = "Новый юзер";
        final String SECOND_USER_NAME = "Еще более новый юзер";

        String fname = new ClassPathResource("log4j-test.properties").getFilename();
        System.setProperty("log4j.configuration", fname);

        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.load("classpath:META-INF/spring/app-test-context.xml");
        ctx.refresh();

        UserService userService = ctx.getBean("userService", UserService.class);

        User newUser = new User();
        newUser.setName(FIRST_USER_NAME);
        newUser.setAge(100);
        newUser.setisAdmin(true);
        newUser = userService.save(newUser);

        User user1 = userService.findById(newUser.getId());
        System.out.println(user1.toString());
        assertTrue("Неверное значение name в новой записи - включена ли поддержка UTF-8?",  user1.getName().equals(FIRST_USER_NAME));

        newUser.setName(SECOND_USER_NAME);
        newUser = userService.save(newUser);
        user1 = userService.findById(newUser.getId());
        System.out.println(user1.toString());
        assertTrue("Неверное значение name в отредактированной записи - включена ли поддержка UTF-8?",  user1.getName().equals(SECOND_USER_NAME));

        userService.delete(newUser);

        Page<User> page = userService.findAll(1);
        for (User user: page.getContent())
            System.out.println(user.toString());
    }
}
