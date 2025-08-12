package zerobase.weather.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Memo;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMemoRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcMemoRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        //dataSource: application.properties에서 작성했던 spring.dataSource.xxx의 dataSource
        //즉 DB에 연결하기 위한 데이터들을 담아두는 객ㅊ체
    }

    //JDBC를 활용한 저장로직
    public Memo save(Memo memo) {
        String sql = "INSERT INTO MEMO VALUES(?,?)";
        jdbcTemplate.update(sql, memo.getId(), memo.getText());
        //jdbcTemplate.update(sql,"변경할 내용",.... ,1) : 데이터를 변경할 때 사용 => INSERT,UPDATE,DELETE
        return memo;
    }

    private RowMapper<Memo> memoRowMapper() {
        //RowMapper란?
        //JDBC를 통해서 데이터를 가져오면 ResultSet의 형태로 담김 // ResultSet= {id=1, text='this is memo'}
        //이렇게 가져온 데이터를 <?> 형태 (여기선 Memo 형태)로 매핑 시켜주기 위해 사용하는 것

        //
        /* return new RowMapper<Memo>() {
            @Override
            public Memo mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Memo(
                        rs.getInt("id"),
                        rs.getString("text")
                );
            }
        }*/

        //람다식
        return ((rs, rowNum) -> new Memo(
                rs.getInt("id"),
                rs.getString("text")
        ));
    }

    //조회
    public List<Memo> findAll(){
        String sql = "SELECT * FROM MEMO";
        return jdbcTemplate.query(sql,memoRowMapper());
        //jdbcTemplate.query(sql,리턴값) : 조회할 때 사용 => SELECT
        // ***필수 요소 : RowMapper<T>로 ResultSet을 객체로 매핑
    }

    public Optional<Memo> findById(int id){
        String sql = "SELECT * FROM MEMO WHERE ID=?";
        return jdbcTemplate.query(sql,memoRowMapper(),id).stream().findFirst();
        // 가져온 값을 하나만 해준다고 지정해야됨 (findFirst로)
    }
}
