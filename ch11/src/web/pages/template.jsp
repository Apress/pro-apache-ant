<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<?xml version="1.0"?>
<!--
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" 
  "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html>
  <head>
    <style type="text/css" media="all">@import "/antBook/plantStyle.css";</style>
    <title><c:out value="${param.title}" default="Plants"/></title>
  </head>

  <body>
    <table width="100%">
      <tr>
        <td><jsp:include page="/header.jsp"/></td>
      </tr>

      <tr>
        <td>
          <table>
            <tr valign="top" class="template">

              <td class="template">
                <jsp:include page="/menu.jsp">
                  <jsp:param name="sectionName" value="${param.menuSection}"/>
                </jsp:include>
              </td>

              <td>
                <jsp:include page="${param.body}"/>
              </td>

            </tr>
          </table>
        </td>
      </tr>

      <tr class="footer">
        <td><jsp:include page="/footer.html"/></td>
      </tr>
    </table>

  </body>
</html>