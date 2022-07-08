package com.blog.controller.admin;

import com.blog.entity.Link;
import com.blog.entity.PageBean;
import com.blog.service.BlogService;
import com.blog.service.LinkService;
import com.blog.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 博客友情链接 */
@Controller
@RequestMapping({"/admin/link"})
public class LinkAdminController {
  @Resource private LinkService linkService;
  @Resource private BlogService blogService;
  /** 博客类型列表 */
  @RequestMapping({"/list"})
  public String list(
      @RequestParam(value = "page", required = false) String page,
      @RequestParam(value = "rows", required = false) String rows,
      HttpServletResponse response)
      throws Exception {
    PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("start", pageBean.getStart());
    map.put("size", pageBean.getPageSize());
    // 查询博客类型列表
    List<Link> linkList = linkService.list(map);
    // 查询总共有多少条数据
    Long total = linkService.getTotal(map);
    // 将数据写入response
    JSONObject result = new JSONObject();
    JSONArray jsonArray = JSONArray.fromObject(linkList);
    result.put("rows", jsonArray);
    result.put("total", total);
    ResponseUtil.write(response, result);
    return null;
  }

  /** 保存友情链接信息 */
  @RequestMapping({"/save"})
  public String save(Link link, HttpServletResponse response) throws Exception {
    int resultTotal = 0;
    // 添加
    if (link.getId() == null) {
      resultTotal = linkService.add(link).intValue();
    } else {
      // 更新
      resultTotal = linkService.update(link).intValue();
    }

    JSONObject result = new JSONObject();
    if (resultTotal > 0) {
      result.put("success", Boolean.valueOf(true));
    } else {
      result.put("success", Boolean.valueOf(false));
    }
    ResponseUtil.write(response, result);
    return null;
  }

  /** 删除博客类型 */
  @RequestMapping({"/delete"})
  public String delete(@RequestParam("ids") String ids, HttpServletResponse response)
      throws Exception {
    String[] idsStr = ids.split(",");
    JSONObject result = new JSONObject();
    for (int i = 0; i < idsStr.length; i++) {
      linkService.delete(Integer.valueOf(idsStr[i]));
    }
    result.put("success", Boolean.valueOf(true));
    ResponseUtil.write(response, result);
    return null;
  }
}
