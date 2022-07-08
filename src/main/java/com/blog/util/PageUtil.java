package com.blog.util;
/** 翻页工具类 */
public class PageUtil {

  /** 翻页方法 */
  public static String genPagination(
      String targetUrl, long totalNum, int currentPage, int pageSize, String param) {
    // 总共页数
    if (totalNum == 0) {
      return "未查询到数据";
    }
    long totalPage = 1;
    if (totalNum % pageSize == 0) {
      totalPage = totalNum / pageSize;
    } else {
      totalPage = totalNum / pageSize + 1;
    }

    StringBuffer pageCode = new StringBuffer();
    pageCode.append("<li><a href='" + targetUrl + "?page=1&" + param + "'>首页</a></li>");
    // 上一页
    if (currentPage > 1) { // 当前页不是第一页，显示上一页并且能点击
      pageCode.append(
          "<li><a href='"
              + targetUrl
              + "?page="
              + (currentPage - 1)
              + "&"
              + param
              + "'>上一页</a></li>");
    } else { // 当前页是第一页，显示上一页但不能点击击
      pageCode.append("<li class='disabled'> <a href='#'>上一页</a></li>");
    }

    // 显示页数
    for (int i = 1; i <= totalPage; i++) {
      if (i == currentPage) {
        pageCode.append(
            "<li class='active'> <a href='"
                + targetUrl
                + "?page="
                + i
                + "&"
                + param
                + "'>"
                + i
                + "</a></li>");
      } else {
        pageCode.append(
            "<li> <a href='" + targetUrl + "?page=" + i + "&" + param + "'>" + i + "</a></li>");
      }
    }
    // 下一页
    if (currentPage < totalPage) {
      pageCode.append(
          "<li><a href='"
              + targetUrl
              + "?page="
              + (currentPage + 1)
              + "&"
              + param
              + "'>下一页</a></li>");
    } else {
      pageCode.append("<li class='disabled'> <a href='#'>下一页</a></li>");
    }
    // 尾页
    pageCode.append(
        "<li> <a href='" + targetUrl + "?page=" + totalPage + "&" + param + "'>尾页</a></li>");

    return pageCode.toString();
  }
}
