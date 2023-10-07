package site.ymango.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.ymango.exception.BaseException;
import site.ymango.user.enums.Gender;
import site.ymango.user.enums.Mbti;
import site.ymango.user.enums.UserStatus;
import site.ymango.user.model.Location;
import site.ymango.user.model.User;
import site.ymango.user.model.UserCompany;
import site.ymango.user.model.UserProfile;

@SpringBootTest
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Test
  @DisplayName("회원 조회 - 성공")
  void getUser() {
    String email = "dalcon@gmail.com";
    User user = userService.getUser(email);
    assertEquals(email, user.email());
  }

  @Test
  @DisplayName("회원 조회 - 사용자를 찾을 수 없습니다.")
  void getUserFail() {
    String email = "nomail@no.no";
    BaseException baseException = assertThrows(BaseException.class, () -> userService.getUser(email));
    assertEquals("사용자를 찾을 수 없습니다.", baseException.getMessage());
  }

  @Test
  @Transactional
  @DisplayName("이메일 인증 요청 생성")
  void createEmailVerification() {
    String email = "dalcon@heybit.io";
    String deviceId = "deviceId";
    String verificationNumber = "1234";

    userService.createEmailVerification(email, deviceId, verificationNumber);
  }

  @Test
  @Transactional
  @DisplayName("이메일 검증 - 성공")
  void verifyEmail1() {
    String email = "test@test.com";
    String deviceId = "test_device_id";
    String verificationNumber = "1234";

    userService.verifyEmail(email, deviceId, verificationNumber);
  }

  @Test
  @Transactional
  @DisplayName("이메일 검증 - 이메일 인증 정보가 유효하지 않습니다.")
  void verifyEmail2() {
    String email = "test2@test.com";
    String deviceId = "test_device_id";
    String verificationNumber = "1234";

    BaseException baseException = assertThrows(BaseException.class, () -> userService.verifyEmail(email, deviceId, verificationNumber));
    assertEquals("이메일 인증 정보가 유효하지 않습니다.", baseException.getMessage());
  }


  @Test
  @Transactional
  @DisplayName("회원가입 - 성공")
  void signup() {
    String email = "test2@test.com";
    String password = "1234";
    Gender gender = Gender.MALE;
    String username = "username";
    LocalDate birthdate = LocalDate.now();
    String sido = "서울";
    String sigungu = "강남구";
    Mbti mbti = Mbti.INFJ;
    String preferMbti = "EOFP";
    Location location = new Location(1.0, 2.0);
    String deviceId = "test_device_id";

    UserCompany company = new UserCompany(
        null, null, "gmail.com", null, null, null, null
    );
    UserProfile userProfile = new UserProfile(
        null, username, gender, birthdate, sido, sigungu, mbti, preferMbti, location, company, null, null, null
    );
    User user = new User(
        null, email, password, UserStatus.ACTIVE, null, null, null, userProfile
    );

    User newUser = userService.create(user, deviceId);
    UserProfile newUserProfile = newUser.userProfile();

    assertEquals(email, newUser.email());
    assertEquals(password, newUser.password());
    assertEquals(username, newUserProfile.username());
    assertEquals(gender, userProfile.gender());
    assertEquals(birthdate, userProfile.birthdate());
    assertEquals(sido, userProfile.sido());
    assertEquals(sigungu, userProfile.sigungu());
    assertEquals(mbti, userProfile.mbti());
    assertEquals(preferMbti, userProfile.preferMbti());
    assertEquals(location, userProfile.location());
    assertEquals(company.name(), userProfile.userCompany().name());
    assertEquals(company.domain(), userProfile.userCompany().domain());
  }

  @Test
  @Transactional
  @DisplayName("회원가입 - 이메일이 인증되지 않았습니다.")
  void signup2() {
    String email = "test@test.com";
    String password = "1234";
    Gender gender = Gender.MALE;
    String username = "username";
    LocalDate birthdate = LocalDate.now();
    String sido = "서울";
    String sigungu = "강남구";
    Mbti mbti = Mbti.INFJ;
    String preferMbti = "EOFP";
    Location location = new Location(1.0, 2.0);
    String deviceId = "test_device_id";

    UserCompany company = new UserCompany(
        null, null, "gmail.com", null, null, null, null
    );
    UserProfile userProfile = new UserProfile(
        null, username, gender, birthdate, sido, sigungu, mbti, preferMbti, location, company, null, null, null
    );
    User user = new User(
        null, email, password, UserStatus.ACTIVE, null, null, null, userProfile
    );

    BaseException baseException = assertThrows(BaseException.class, () -> userService.create(user, deviceId));
    assertEquals("이메일이 인증되지 않았습니다.", baseException.getMessage());
  }

  @Test
  @Transactional
  @DisplayName("회원가입 - 이미 존재하는 사용자입니다.")
  void signup3() {
    String email = "test@gmail.com";
    String password = "1234";
    Gender gender = Gender.MALE;
    String username = "username";
    LocalDate birthdate = LocalDate.now();
    String sido = "서울";
    String sigungu = "강남구";
    Mbti mbti = Mbti.INFJ;
    String preferMbti = "EOFP";
    Location location = new Location(1.0, 2.0);
    String deviceId = "test_device_id";

    UserCompany company = new UserCompany(
        null, null, "gmail.com", null, null, null, null
    );
    UserProfile userProfile = new UserProfile(
        null, username, gender, birthdate, sido, sigungu, mbti, preferMbti, location, company, null, null, null
    );
    User user = new User(
        null, email, password, UserStatus.ACTIVE, null, null, null, userProfile
    );

    BaseException baseException = assertThrows(BaseException.class, () -> userService.create(user, deviceId));
    assertEquals("이미 존재하는 사용자입니다.", baseException.getMessage());
  }
}