package com.danbi.recruit.service;
        import com.danbi.recruit.domain.Users;
        import com.danbi.recruit.repository.UserRepository;
        import jakarta.persistence.EntityManager;
        import org.apache.catalina.User;
        import org.junit.Test;
        import org.junit.jupiter.api.Assertions;
        import org.junit.runner.RunWith;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.context.SpringBootTest;
        import org.springframework.test.context.junit4.SpringRunner;
        import org.springframework.transaction.annotation.Transactional;

        import java.util.List;

        import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Test
    public void 사용자조회() throws Exception {
        // given
        Users user = new Users();
        user.setName("user1");
        userService.join(user);
        // when
        Users findUser = userService.findUser(user.getId());
        // then
        assertEquals(findUser.getId(), user.getId(), "만들어진 유저 아이디와 저장된 유저 아이디가 같아야합니다.");
    }

    @Test
    public void 사용자_목록_조회() throws Exception {
        //given
        Users user1 = new Users();
        user1.setName("user1");
        userService.join(user1);

        Users user2 = new Users();
        user2.setName("user2");
        userService.join(user2);

        Users user3 = new Users();
        user3.setName("user3");
        userService.join(user3);

        //when
        List<Users> allUsers = userService.findAllUsers();

        //then
        assertEquals(3, allUsers.size());
    }
}
