package dev.claycheng.filter

import com.google.common.collect.ImmutableMap
import com.google.common.io.Resources
import dev.claycheng.api.CommonApiResult
import dev.claycheng.api.CommonResult
import dev.claycheng.api.enums.MemberStatus
import io.jsonwebtoken.Jwts
import lombok.Cleanup
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import java.nio.charset.StandardCharsets
import java.security.KeyPair
import java.security.PrivateKey
import java.time.Instant

@TestPropertySource(properties = "knocknut.auth.jwt.enabled=true")
@TestPropertySource(properties = "knocknut.auth.jwt.algo=rsa")
@TestPropertySource(properties = "knocknut.auth.jwt.rsa-pub-key-pem=jwtAuth.key.pub")
@TestPropertySource(properties = "knocknut.auth.jwt.token-type=bearer")
@TestPropertySource(properties = "knocknut.gateway.public-urls[0]=/public/**")
@TestConfiguration
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RsaJwtTokenAuthSpec extends Specification {

    @Autowired
    TestRestTemplate testRestTemplate

    def "reject with 401 Unauthorized while no token from Authorization header"() {
        when:
        def response = testRestTemplate.exchange(
            "/member/profile",
            HttpMethod.GET,
            HttpEntity.EMPTY,
            CommonResult.class
        )

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED
        response.body.code == CommonApiResult.EMPTY_AUTH_HEADER.code
    }

    def "skip authorization if the path meet public rule"() {
        when:
        def response = testRestTemplate.exchange(
            "/public/anything-was-public",
            HttpMethod.GET,
            HttpEntity.EMPTY,
            CommonResult.class
        )

        then:
        response.statusCode != HttpStatus.UNAUTHORIZED
    }

    def "with valid jwt token"() {
        given:
        def keyPair = loadRsaKeyPairByPrivateKeyPem(Resources.getResource("jwtAuth.key"))
        def validToken = generateJws(keyPair.private)
        def header = new HttpHeaders()
        header.setBearerAuth(validToken)

        when:
        def response = testRestTemplate.exchange(
            "/member/profile",
            HttpMethod.GET,
            new HttpEntity<>(null, header),
            CommonResult.class
        )

        then:
        response.statusCode != HttpStatus.UNAUTHORIZED
    }

    def "with invalid jwt token"() {
        given:
        def header = new HttpHeaders()
        header.setBearerAuth(generateJws(null))

        when:
        def response = testRestTemplate.exchange(
            "/member/profile",
            HttpMethod.GET,
            new HttpEntity<>(null, header),
            CommonResult.class
        )

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED
    }

    def "with expired jwt token"() {
        given:
        def keyPair = loadRsaKeyPairByPrivateKeyPem(Resources.getResource("jwtAuth.key"))
        def validToken = expiredJws(keyPair.private)
        def header = new HttpHeaders()
        header.setBearerAuth(validToken)

        when:
        def response = testRestTemplate.exchange(
            "/member/profile",
            HttpMethod.GET,
            new HttpEntity<>(null, header),
            CommonResult.class
        )

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED
    }

    String expiredJws(PrivateKey privateKey) {
        def builder = Jwts.builder()
            .setExpiration(new Date(Instant.now().minusSeconds(60).toEpochMilli()))
            .claim("additionalInfo", ImmutableMap.builder()
                .put("memberId", "1")
                .put("username", "clay@knocknut")
                .put("nickname", "cc")
                .put("email", "clay@knocknutshop.com")
                .put("avatar", "https://my.avatar/clay")
                .put("status", MemberStatus.ENABLED.value)
                .build())

        if (privateKey) {
            builder.signWith(privateKey)
        }

        builder.compact()
    }

    String generateJws(PrivateKey privateKey) {
        def builder = Jwts.builder()
            .claim("additionalInfo", ImmutableMap.builder()
                .put("memberId", "1")
                .put("username", "clay@knocknut")
                .put("nickname", "cc")
                .put("email", "clay@knocknutshop.com")
                .put("avatar", "https://my.avatar/clay")
                .put("status", MemberStatus.ENABLED.value)
                .build())

        if (privateKey) {
            builder.signWith(privateKey)
        }

        builder.compact()
    }

    private KeyPair loadRsaKeyPairByPrivateKeyPem(URL resource) throws IOException {
        @Cleanup
        var keyReader =
            Resources.asCharSource(resource, StandardCharsets.US_ASCII).openBufferedStream()
        var pemParser = new PEMParser(keyReader)
        var pemObject = pemParser.readObject()
        if (!(pemObject instanceof PEMKeyPair)) {
            throw new IOException(
                "unsupported rsa key pair found. only support with non-encrypted pem file.")
        }
        var keyPair = ((PEMKeyPair) pemObject)
        var pemConverter = new JcaPEMKeyConverter()

        return new KeyPair(
            pemConverter.getPublicKey(keyPair.getPublicKeyInfo()),
            pemConverter.getPrivateKey(keyPair.getPrivateKeyInfo()))
    }

}