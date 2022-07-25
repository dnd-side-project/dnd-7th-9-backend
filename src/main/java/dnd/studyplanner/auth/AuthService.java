package dnd.studyplanner.auth;

import org.springframework.stereotype.Service;

import dnd.studyplanner.auth.dto.TokenResponseDto;
import dnd.studyplanner.auth.model.AuthEntity;
import dnd.studyplanner.auth.model.AuthRepository;
import dnd.studyplanner.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthRepository authRepository;
	private final JwtService jwtService;

	public boolean isExpiredRefreshToken(String refreshToken) {
		return jwtService.isExpiredRefreshToken(refreshToken);
	}


	public TokenResponseDto reissueAccessToken(String refreshToken) {
		Long memberId = jwtService.getMemberId(refreshToken);
		String newAccessToken = jwtService.createJwt(memberId); //새로운 Access-Token 생성
		String newRefreshToken = refreshToken;
		if (jwtService.isUpdatableRefreshToken(refreshToken)) { //Refresh-Token 만료 시간에 따라 재발급
			log.debug("[REFRESH TOKEN REFRESH] : {} \n to {}", refreshToken, newRefreshToken);
			newRefreshToken = jwtService.createRefreshToken(memberId);
		};

		AuthEntity authEntity = authRepository.findById(memberId).orElse(
			AuthEntity.builder()
				.jwt(newAccessToken)
				.refreshToken(newRefreshToken)
				.memberId(memberId)
				.build()
		);

		authRepository.save(authEntity);

		return new TokenResponseDto(newAccessToken, newRefreshToken);
	}
}
