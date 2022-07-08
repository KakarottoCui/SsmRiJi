package com.blog.controller.admin;

import com.blog.entity.Blogger;
import com.blog.service.BloggerService;
import com.blog.util.Const;
import com.blog.util.CryptographyUtil;
import com.blog.util.DateUtil;
import com.blog.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Controller
@RequestMapping({"/admin/blogger"})
public class BloggerAdminController {

  @Resource private BloggerService bloggerService;

  @RequestMapping({"/save"})
  public String save(
      @RequestParam("imageFile") MultipartFile imageFile,
      Blogger blogger,
      HttpServletRequest request,
      HttpServletResponse response)
      throws Exception {
    if (!imageFile.isEmpty()) {
      String filePath = request.getServletContext().getRealPath("/");
      String imageName =
          DateUtil.getCurrentDateStr() + "." + imageFile.getOriginalFilename().split("\\.")[1];
      imageFile.transferTo(new File(filePath + "static/userImages/" + imageName));
      blogger.setImageName(imageName);
    }
    long resultTotal = bloggerService.update(blogger);

    StringBuffer result = new StringBuffer();
    if (resultTotal > 0) {
      result.append("<script lauguage='javascript'>alert('修改成功');</script>");
    } else {
      result.append("<script lauguage='javascript'>alert('修改失败');</script>");
    }

    return null;
  }

  /** 获取博主的json格式 */
  @RequestMapping({"/find"})
  public String find(HttpServletResponse response) throws Exception {
    Blogger blogger =
        (Blogger) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
    // blogger转换为json格式 通过AJAx获取到该数据
    JSONObject jsonObject = JSONObject.fromObject(blogger);
    ResponseUtil.write(response, jsonObject);
    return null;
  }

  /** 修改密码 */
  @RequestMapping({"/modifyPassword"})
  public String modifyPassword(
      @RequestParam("id") String id,
      @RequestParam("newPassword") String newPassword,
      HttpServletResponse response)
      throws Exception {
    Blogger blogger = new Blogger();
    blogger.setId(Integer.parseInt(id));
    blogger.setPassword(CryptographyUtil.md5(newPassword, "loveyourself"));
    int resultTotal = bloggerService.update(blogger);
    JSONObject result = new JSONObject();
    if (resultTotal > 0) {
      result.put("success", Boolean.TRUE);
    } else {
      result.put("success", Boolean.FALSE);
    }
    ResponseUtil.write(response, result);
    return null;
  }

  /** 用户退出 */
  @RequestMapping({"/logout"})
  public String logout() {
    SecurityUtils.getSubject().logout();
    return "redirect:/login.jsp";
  }
}
