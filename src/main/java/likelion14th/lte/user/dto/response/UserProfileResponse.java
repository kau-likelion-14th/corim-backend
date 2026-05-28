package likelion14th.lte.user.dto.response;

import likelion14th.lte.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)

public class UserProfileResponse {
    private String username;
    private String profileImageUrl;
    private String introduction;
    // [Q4. Controller가 DB에서 꺼낸 원본 Entity(User)를 클라이언트 화면에 그대로 반환하지 않고,
    // 굳이 from() 메서드를 통해 DTO로 한번 변환해서 내보내는 핵심적인 이유 2가지는 무엇인가요?]
    // 답변: 두가지 이유 중 첫번째는 보안 때문입니다. db 원본데이터를 통째로 넘기면 비밀번호같은 중요한 정보까지
    // 밖으로 새어나갈 수 있기 때문입니다. 두번째는 프론트엔드가 고장나지 않게하기위해서 입니다.
    // 만약 db 구조가 바뀌더라도 중간에 dto라는 포장 상자만 잘 맞춰주면 프론트엔드 코드는 망가지지 않고 안전하게 작동하기 때문입니다.
    public static UserProfileResponse from (User user){
        return new UserProfileResponse(
                user.getUsername() + "#" + user.getUserTag(),
                user.getProfileImage(),
                user.getIntroduction()
        );
    }
}
