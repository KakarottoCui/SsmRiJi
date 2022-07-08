package com.blog.realm;

import com.blog.entity.Blogger;
import com.blog.service.BloggerService;
import com.blog.util.Const;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

public class MyRealm extends AuthorizingRealm {
  @Resource private BloggerService bloggerService;
  /** 获取授权信息 */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    return null;
  }
  /** 登陆验证 token:令牌，基于用户名密码的令牌 */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
      throws AuthenticationException {
    // 从令牌中取出用户名
    String userName = (String) token.getPrincipal();
    // 让shiro去验证账号密码
    Blogger blogger = bloggerService.getByUserName(userName);
    if (blogger != null) {
      // 把当前用户存储到session中让其他地方可以直接取到blogger信息
      SecurityUtils.getSubject().getSession().setAttribute(Const.CURRENT_USER, blogger);
      // AuthenticationInfo主要是用来获取认证信息的
      AuthenticationInfo authenticationInfo =
          new SimpleAuthenticationInfo(blogger.getUserName(), blogger.getPassword(), getName());
      return authenticationInfo;
    }
    return null;
  }
}
