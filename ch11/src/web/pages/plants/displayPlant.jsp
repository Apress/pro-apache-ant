<%-- JSP Directives --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<c:choose>
  <c:when test="${empty sessionScope.results}">
    <c:redirect url="index.jsp"/>
  </c:when>
  
  <c:otherwise>
    <jsp:include page="/template.jsp">
      <jsp:param name="menuSection" value="Plants"/>
      <jsp:param name="body" value="/plants/displayPlantBody.jsp"/>
    </jsp:include>
  </c:otherwise>
</c:choose>
