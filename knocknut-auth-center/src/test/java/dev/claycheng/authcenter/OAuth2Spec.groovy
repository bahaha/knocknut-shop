package dev.claycheng.authcenter

import dev.claycheng.api.AuthToken
import dev.claycheng.api.CommonResult
import dev.claycheng.api.enums.MemberStatus
import dev.claycheng.knocknut.api.feign.user.Member
import dev.claycheng.knocknut.api.feign.user.MemberFeignApi
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
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.client.BaseClientDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Specification

import javax.sql.DataSource
import java.time.Duration

@TestConfiguration
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OAuth2Spec extends Specification {


    @Autowired
    ClientDetailsServiceConfigurer client
    @Autowired
    TestRestTemplate testRestTemplate

    BaseClientDetails integrationClient

    @SpringBean
    DataSource dataSource = Mock()

    @SpringBean
    PasswordEncoder plainTextEncoder = NoOpPasswordEncoder.instance

    @SpringBean
    MemberFeignApi memberApi = Stub(MemberFeignApi) {
        findByUsername(_) >> {
            def member = new Member()
            member.username = 'clay'
            member.password = '123123123'
            member.status = MemberStatus.ENABLED
            CommonResult.success(member)
        }
    }

    @SpringBean(name = "clientDetails")
    ClientDetailsService integrationClientStub = Stub(ClientDetailsService) {
        loadClientByClientId('integration-client') >> { _ -> integrationClient }
    }

    def "should issue token if request is from the trusted client"() {
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
        with(response.body) {
            token_type == 'bearer'
            (!access_token.blank)
            (!refresh_token.blank)
            scope == 'read write'
        }
    }

    def "400 bad request if bad credentials from member request"() {
        given:
        integrationClient = trustClient([
            clientId  : "integration-client", clientSecret: "#dev#clay",
            grantTypes: ["password", "refresh_token"], scopes: ["read", "write"]])

        when:
        def response = requestToken([
            auth  : [clientId: "integration-client", clientSecret: "#dev#clay"],
            params: [grant_type: "password", scope: "read write", username: "clay",
                     password  : "BAD_PASSWORD"
            ]
        ])

        then:
        response.statusCode == HttpStatus.BAD_REQUEST
    }

    def "401 unauthorized request if no match trust client"() {
        given:
        integrationClient = trustClient([
            clientId  : "integration-client", clientSecret: "#dev#clay",
            grantTypes: ["password", "refresh_token"], scopes: ["read", "write"]])

        when:
        def response = requestToken([
            auth  : [clientId: "integration-client", clientSecret: "BAD_SECRET"],
            params: [grant_type: "password", scope: "read write", username: "clay", password: "123123123"]
        ])

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED
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