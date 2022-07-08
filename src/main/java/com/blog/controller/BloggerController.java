package com.blog.controller;

import com.blog.entity.Blogger;
import com.blog.service.BloggerService;
import com.blog.util.CryptographyUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/** 博主后台登录 */
@Controller
@RequestMapping("/blogger")
public class BloggerController {
  @Resource private BloggerService bloggerService;

  @RequestMapping("/login")
  public String login(Blogger blogger, HttpServletRequest request) {
    String userName = blogger.getUserName();
    String password = blogger.getPassword();
    // 因为数据库中的密码加密过 所以这里要加密后进行对比  密码+盐
    String pw = CryptographyUtil.md5(password, "loveyourself");

    Subject subject = SecurityUtils.getSubject();
    UsernamePasswordToken token = new UsernamePasswordToken(userName, pw);
    try {
      // 传递token给shiro的realm
      subject.login(token);
      // 成功之后就进入主页
      return "redirect:/admin/main.jsp";
    } catch (Exception e) {
      e.printStackTrace();
      // 把用户名密码传回前台页面，让用户修改正确的再次提交
      request.setAttribute("blogger", blogger);
      request.setAttribute("errorInfo", "用户名密码错误");
    }

    return "login";
  }
  /** 关于博主 */
  @RequestMapping("/aboutMe")
  public ModelAndView aboutMe() {
    ModelAndView mv = new ModelAndView();
    mv.addObject("blogger", bloggerService.find());
    mv.addObject("mainPage", "foreground/blogger/blogInfo.jsp");
    mv.addObject("pageTitle", "关于博主");
    mv.setViewName("index");
    return mv;
  }
}
