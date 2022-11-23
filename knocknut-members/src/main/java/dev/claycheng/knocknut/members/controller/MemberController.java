package dev.claycheng.knocknut.members.controller;

import dev.claycheng.api.CommonResult;
import dev.claycheng.common.exception.DataNotFoundException;
import dev.claycheng.knocknut.members.domain.Member;
import dev.claycheng.knocknut.members.repository.MemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "會員資訊管理", tags = "Member")
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/profile")
  public CommonResult<Member> findMemberByUsername(@RequestParam("username") String username) {
    try {
      return CommonResult.success(memberService.findByUsername(username));
    } catch (DataNotFoundException ex) {
      return CommonResult.notFound();
    }
  }
}
