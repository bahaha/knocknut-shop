package dev.claycheng.knocknut.api.feign.user;

import dev.claycheng.api.CommonResult;
import dev.claycheng.knocknut.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "knocknut-member", path = "/member")
public interface MemberFeignApi {

  @GetMapping("/profile")
  @ResponseBody
  CommonResult<User> findByUsername(@RequestParam("username") String username);
}
