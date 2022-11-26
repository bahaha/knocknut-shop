package dev.claycheng.authcenter

import com.google.common.io.Resources
import dev.claycheng.api.AuthToken
import dev.claycheng.api.CommonResult
import dev.claycheng.api.enums.MemberStatus
import dev.claycheng.knocknut.api.feign.user.Member
import dev.claycheng.knocknut.api.feign.user.MemberFeignApi
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import lombok.Cleanup
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.client.BaseClientDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Specification

import javax.sql.DataSource
import java.nio.charset.StandardCharsets
import java.security.PublicKey
import java.time.Duration

@TestPropertySource(properties = "knocknut.auth.jwt.enabled=true")
@TestPropertySource(properties = "knocknut.auth.jwt.algo=rsa")
@TestPropertySource(properties = "knocknut.auth.jwt.rsa-private-key-pem=jwtAuth.key")
@TestConfiguration
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RsaJwtTokenOAuth2Spec extends Specification {


    @Autowired
    TestRestTemplate testRestTemplate

    BaseClientDetails integrationClient

    def ccAvatar = 'https://avataaars.io/?avatarStyle=Circle&topType=ShortHairShortCurly&accessoriesType=Blank&hairColor=BrownDark&facialHairType=BeardMajestic&facialHairColor=BrownDark&clotheType=BlazerSweater&eyeType=Surprised&eyebrowType=RaisedExcited&mouthType=Serious&skinColor=Brown'

    @SpringBean
    DataSource dataSource = Mock()

    @SpringBean
    PasswordEncoder plainTextEncoder = NoOpPasswordEncoder.instance

    @SpringBean
    MemberFeignApi memberApi = Stub(MemberFeignApi) {
        findByUsername(_) >> {
            def member = new Member()
            member.id = 5566L
            member.username = 'clay'
            member.nickname = 'cc'
            member.password = '123123123'
            member.status = MemberStatus.ENABLED
            member.email = 'clementcheng56@gmail.com'
            member.phone = '886912345678'
            member.status = MemberStatus.ENABLED
            member.avatar = 'https://avataaars.io/?avatarStyle=Circle&topType=ShortHairShortCurly&accessoriesType=Blank&hairColor=BrownDark&facialHairType=BeardMajestic&facialHairColor=BrownDark&clotheType=BlazerSweater&eyeType=Surprised&eyebrowType=RaisedExcited&mouthType=Serious&skinColor=Brown'
            CommonResult.success(member)
        }
    }

    @SpringBean(name = "clientDetails")
    ClientDetailsService integrationClientStub = Stub(ClientDetailsService) {
        loadClientByClientId('integration-client') >> { _ -> integrationClient }
    }


    def "issue JWS with secret hashing"() {
        given:
        integrationClient = trustClient([
            clientId  : "integration-client", clientSecret: "#dev#clay",
            grantTypes: ["password", "refresh_token"], scopes: ["read", "write"]])

        when:
        def response = requestToken([
            auth  : [clientId: "integration-client", clientSecret: "#dev#clay"],
            params: [grant_type: "password", scope: "read write", username: "clay", password: "123123123"]
        ])

        then:
        response.statusCode == HttpStatus.OK
        AuthToken authToken = response.body
        String jwtToken = authToken.access_token
        Jws<Claims> claims = rsaJwsParser().parseClaimsJws(jwtToken)
        with(claims.body) {
            user_name == 'clay'
            client_id == 'integration-client'
        }

        and: "algo should be rsa"
        claims.header.get('alg') == 'RS256'

        and: "contains user profile for other services usage"
        def userProfile = claims.body.additionalInfo as Map<String, Object>
        userProfile.get("memberId") == 5566L
        userProfile.get("username") == 'clay'
        userProfile.get("nickname") == 'cc'
        userProfile.get("email") == 'clementcheng56@gmail.com'
        userProfile.get("status") == MemberStatus.ENABLED.name()
        userProfile.get("avatar") == ccAvatar
    }

    JwtParser rsaJwsParser() {
        Jwts.parserBuilder()
            .setSigningKey(readRsaPublicKey(Resources.getResource("jwtAuth.key.pub")))
            .build()
    }

    PublicKey readRsaPublicKey(URL resource) throws IOException {
        @Cleanup
        def keyReader =
            Resources.asCharSource(resource, StandardCharsets.US_ASCII).openBufferedStream()
        def pemParser = new PEMParser(keyReader)
        def pemObject = pemParser.readObject()
        return new JcaPEMKeyConverter().getPublicKey((SubjectPublicKeyInfo) pemObject)
    }

    ResponseEntity<AuthToken> requestToken(Map props) {
        Map<String, String> auth = props.auth ?: [:]
        Map<String, String> params = props.params ?: [:]
        def uriBuilder = UriComponentsBuilder.fromUriString("/oauth/token")
        params.each { uriBuilder.queryParam(it.key, it.value) }

        testRestTemplate.withBasicAuth(auth.clientId, auth.clientSecret)
            .exchange(uriBuilder.build().toUri(),
                HttpMethod.POST,
                HttpEntity.EMPTY,
                AuthToken.class)
    }

    BaseClientDetails trustClient(Map<String, Object> props) {
        def client = new BaseClientDetails()
        client.clientId = props.clientId
        client.clientSecret = props.clientSecret
        client.authorizedGrantTypes = props.grantTypes as List<String>
        client.scope = props.scopes as List<String>
        client.accessTokenValiditySeconds = Duration.ofDays(1).toSeconds().intValue()
        client
    }
}