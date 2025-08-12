package zerobase.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;
import zerobase.weather.repository.JdbcMemoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class})
    public class JdbcMemoRepositoryTest {
    @Autowired
    JdbcMemoRepository jdbcMemoRepository;
    @Autowired
    private JdbcClient jdbcClient;

    @Test
    void insertMemoTest(){
    //given
        Memo memo =new Memo(3,"this is new memo~");
    //when
        jdbcMemoRepository.save(memo);
    //then
        Optional<Memo> result = jdbcMemoRepository.findById(1);
        assertEquals(result.get().getText(),"this is new memo~");    }

    @Test
    void findAllMemoTest(){
        List<Memo> memoList= jdbcMemoRepository.findAll();
        System.out.println(memoList);
        assertNotNull(memoList);
    }


}
