package likelion14th.lte.user.repository;

import likelion14th.lte.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
// [추가문제] (필수 X) 이 코드는 인터페이스일 뿐이고 구현체(implements) 클래스가 없습니다.
// 그런데 어떻게 프로그램 실행 시 DB와 통신하는 객체로 동작할 수 있나요?
// 답변 : 인터페이스 껍데기만 만들어 두면 프로그램이 켜질 때 스프링이 알아서 이 껍데기에 맞는
// 가짜 객체인 동적 프록시를 메모리에 만들어줍니다.
// 그 안에 db랑 통신하는 복잡한 코드들을 알아서 채워주기 때문에 직접 클래스를 만들지 않아도 완벽하게 작동합니다.

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
}
