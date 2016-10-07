<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:forEach items="${sessionScope.results}" var="plant"
           begin="${param.id}" end="${param.id}">
  <table>
    <c:choose>
      <c:when test="${!empty plant.image}">
        <tr>
          <td><img height="135" width="120" border="1" src="/antBook/images/${plant.image}"/></td>
        </tr>
      </c:when>
      <c:otherwise>
        <tr>
          <td><img height="135" width="120" border="1" src="/antBook/images/no_image.jpg"/></td>
        </tr>
      </c:otherwise>
    </c:choose>
    <tr>
      <td>
        <p>
          Name: <span class="italic"><c:out value="${plant.name}"/></span><br/>
          Common name: <c:out value="${plant.common_name}"/><br/>
          Family: <c:out value="${plant.family}"/><br/>
          Description: <c:out value="${plant.description}"/><br/>
        </p>
      </td>
    </tr>
  </table>
</c:forEach>

<%-- We need to go back to where we came from --%>
<a href="${header.referer}"/>Back</a>