package dnd.studyplanner.config;

import static dnd.studyplanner.config.SecretConstant.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.Test;

class JasyptConfigTest {

	/**
	 * application.yml 암호화 테스트
	 *
	 */
	@Test
	public void jasypt_test() throws IOException {

		//암호화 할 Text
		String targetText = "";

		//jasypt config
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(JASYPT_KEY);
		config.setAlgorithm("PBEWithMD5AndDES"); // 암호화 알고리즘
		config.setKeyObtentionIterations("1000"); // 반복할 해싱 회수
		config.setPoolSize("2"); // 인스턴스 pool
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성 클래스
		config.setStringOutputType("base64"); //인코딩 방식
		encryptor.setConfig(config);

		//암호화 결과
		String encryptText = encryptor.encrypt(targetText);
		System.out.println(encryptText);

		//복호화
		String decryptText = encryptor.decrypt(encryptText);
		Assertions.assertThat(targetText).isEqualTo(decryptText);

	}
}