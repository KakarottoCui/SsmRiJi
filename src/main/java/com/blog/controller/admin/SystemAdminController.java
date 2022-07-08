package com.blog.controller.admin;

import com.blog.entity.BlogType;
import com.blog.entity.Blogger;
import com.blog.entity.Link;
import com.blog.service.BlogTypeService;
import com.blog.service.BloggerService;
import com.blog.service.LinkService;
import com.blog.util.Const;
import com.blog.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 刷新系统缓存 */
@Controller
@RequestMapping({"/admin/system"})
public class SystemAdminController {
  @Resource BlogTypeService blogTypeService;
  @Resource BloggerService bloggerService;
  @Resource LinkService linkService;

  @RequestMapping({"/refreshSystem"})
  public String refreshSystem(HttpServletResponse response, HttpServletRequest request)
      throws Exception {
    // 获取上下文
    ServletContext application =
        RequestContextUtils.getWebApplicationContext(request).getServletContext();
    // 博客列表
    Map<String, Object> map = new HashMap<>();
    List<BlogType> list = blogTypeService.list(map);
    // 这里的名字和之前的初始化时获取的数据一样
    application.setAttribute(Const.BLOG_TYPE_LIST_SELECT, list);
    // 个人信息
    Blogger blogger = bloggerService.find();
    application.setAttribute(Const.BLOGGER, blogger);
    // 友情链接
    List<Link> linkList = linkService.list(null);
    application.setAttribute(Const.LINK_LIST, linkList);
    // 返回结果
    JSONObject result = new JSONObject();
    result.put("success", Boolean.TRUE);
    ResponseUtil.write(response, result);
    return null;
  }
}
