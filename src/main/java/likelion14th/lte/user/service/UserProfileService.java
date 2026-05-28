package likelion14th.lte.user.service;


import likelion14th.lte.global.api.ErrorCode;
import likelion14th.lte.global.exception.GeneralException;
import likelion14th.lte.user.dto.request.CreateTestUserRequest;
import likelion14th.lte.user.dto.response.UserProfileResponse;
import likelion14th.lte.user.entity.User;
import likelion14th.lte.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)

public class UserProfileService {
    // [Q5. Service 안에서 new UserRepository() 로 객체를 직접 생성하지 않고,
    // 외부에서 의존성 주입(DI)을 받는 이유는 무엇인가요? (결합도와 단위 테스트 관점)]
    // 답변:

    private final UserRepository userRepository;
    // [Q6. (코딩 문제) 만약 클래스 위의 @RequiredArgsConstructor를 지운다면,
    // 우리가 직접 작성해야 할 의존성 주입용 자바 '생성자' 코드는 어떤 모습일까요? 아래에 직접 코딩해 보세요.]
    /*
       여기에 생성자 코드 작성:
    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    */
    @Transactional
    public UserProfileResponse createTestUser(CreateTestUserRequest request){

        // [Q7. 일반적인 생성자 new User(name, intro, tag) 방식을 쓰지 않고,
        // User.builder()...build() 라는 '빌더 패턴'을 사용하여 객체를 조립했을 때 얻는 장점은 무엇인가요?]
        // 답변: 그냥 new User("이름", "태그") 처럼 쓰면 실수로 순서를 거꾸로 넣어도 구분이 안됩니다.
        // 그런데 빌더 패턴을 쓰면 .username("이름") 처럼 어디에 뭘 넣는지 이름표를 딱딱 붙여서 적기 때문에
        // 실수할 일도 없고 코드를 읽기도 훨씬 편해지기 때문입니다.

        User newUSer = User.builder()
                .username(request.getUsername())
                .userTag(request.getUserTag())
                .introduction(request.getIntroduction())
                .build();
        User savedUser;
        try{
            // [Q8. 데이터를 저장하는 이 메서드 위에 @Transactional이 반드시 붙어야 하는 이유는 무엇인가요?
            // (저장 도중 DB 서버가 끊겼을 때의 상황을 가정해서 설명하세요)]
            // 답변 : db에 데이터를 저장하다가 갑자기 서버가 꺼지거나 에러가 난다면 데이터가 반만 저장되면서 꼬여버릴 수 있는데
            // 이 어노테이션을 붙여두면 중간에 에러가 나면 아예 처음으로 돌려버리라고 지시할 수 있어서 데이터가 꼬이는 걸 막아줍니다.

            savedUser = userRepository.save(newUSer);
        } catch (Exception e) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }
        return UserProfileResponse.from(savedUser);
    }

    @Transactional
    public UserProfileResponse getUserProfile(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new GeneralException(ErrorCode.USER_NOT_FOUND));
        return UserProfileResponse.from(user);
    }
}
