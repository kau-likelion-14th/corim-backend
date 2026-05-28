package likelion14th.lte.user.entity;

import jakarta.persistence.*;
import likelion14th.lte.Entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
// [Q1. @NoArgsConstructor는 매개변수가 없는 기본 생성자를 만듭니다.
// 그런데 왜 누구나 쓸 수 있게 PUBLIC으로 열어두지 않고, 굳이 PROTECTED로 막아두었을까요? (객체 생성의 안전성과 JPA 관점)]
// 답변:JPA가 DB에서 객체를 만들 때 빈 생성자가 꼭 필요하기 때문입니다. 그런데 이걸 아무나 쓸 수 있게 열어두면 텅 빈 객체를
// 실수로 막 만들 수 있기 때문에 객체를 막 만들지 못하게 하면서 JPA는 사용할 수 있게 하려고 protected로 잠굽니다.
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // [Q2. @Column(nullable = false) 어노테이션이 DB와 자바 코드 사이에서 하는 역할은 무엇인가요?]
    // 답변: DB 테이블을 만들 때 이 칸은 비워두면 안된다고 설정해주는 것 입니다. 그리고 자바 쪽에서도 값이
    // 비어있는지 미리 체크해줘서 이상한 빈 데이터가 DB에 들어가는 걸 막아주는 역할을 합니다.

    @Column(nullable = false)
    private String username;

    @Column(length = 16, nullable = false, unique = true)
    private String userTag;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(columnDefinition = "TEXT")
    private String profileImage;

    @Column(columnDefinition = "TEXT")
    private String s3ImageKey;

    @Builder(access = AccessLevel.PUBLIC)
    private User (String username, String userTag, String introduction){
        this.username = username;
        this.userTag = userTag;
        this.introduction = introduction;
    }
    // [Q3. @Setter를 위 @Getter 처럼 사용하면 모든 맴버들에 setIntruduction() 같은 setter 메서드가 생성됩니다. 하지만 왜 @Setter를 쓰지않고 updateIntroduction() 이라는 명확한 메서드를 만든 객체지향적인 이유는 무엇인가요?]
    // 답변: @Setter를 다 열어두면 여기저기서 데이터를 막 바꿔버릴 수 있어서 나중에 에러 찾기가 너무 힘들기 때문입니다.
    // 그리고 아이디어나 가입일처럼 절대 바뀌면 안되는 값도 실수로 바뀔 위험이 있습니다.
    // 그래서 updateIntroduction() 처럼 이름만 봐도 소개글을 수정하는구나 하고
    // 딱 알 수 있는 정용 메서드를 따로 만드는게 안전하기 때문입니다.

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }
}

