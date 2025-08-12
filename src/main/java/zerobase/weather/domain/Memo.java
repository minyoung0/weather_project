package zerobase.weather.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="memo")
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //기본 키 값을 어떻게 생성할지 전략을 지정
    //GenerationType.IDENTITY : DB의 AUTO_INCREMENT 기능을 사용
    //GenerationType.Sequence: DB 시퀀스 객체를 사용해서 PK 값 생성
    //GenerationType.TABLE: 시퀀스 대신 테이블을 이용해 PK 값 관리( strategy,generator ="테이블이름")
    //GenerationType.AUTO: JPA가 DB 종류에 따라 자동으로 전략 선택
    private int id;
    private String text;


    public Memo(String text) { this.text = text; } // 편의 생성자
}
