<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="end" value="${param.start + 4}"/>

<h1>Search Results</h1>

<c:choose>
  <c:when test="${empty sessionScope.results}">
    <p>Sorry, there were no results for the search. Please try again.</p>
  </c:when>

  <c:otherwise>
    <div class="results">
      <p>Number of results: <c:out value="${sessionScope.resultsSize}"/></p>
      <table>
        <tr class="resultRow" valign="top">
          <td>
            <c:forEach items="${sessionScope.results}" var="item" varStatus="number"
                       begin="${param.start}" end="${end}">
              <a class="italic" href="displayPlant.jsp?id=<c:out value="${number.index}"/>"><c:out value="${item.name}"/></a>
              <br/>
              <c:out value="${item.common_name}"/>
              <br/>
            </c:forEach>
          </td>
        </tr>
        <tr>
          <td>
            <fmt:parseNumber value="${initParam.resultsPerPage}" var="resultsPerPage"/>

            <c:if test="${param.start > resultsPerPage}">
              <c:url value="displayResults.jsp" var="first">
                <c:param name="start" value="0"/>
              </c:url>
              <a href="<c:out value="${first}"/>">First</a>
            </c:if>

            <c:if test="${param.start ne 0}">
              <c:url value="displayResults.jsp" var="back">
                <c:param name="start" value="${param.start - 5}"/>
              </c:url>
              <a href="<c:out value="${back}"/>">Back</a>
            </c:if>

            <c:if test="${(sessionScope.resultsSize - end - 1) > 0}">
              <c:url value="displayResults.jsp" var="next">
                <c:param name="start" value="${end + 1}"/>
              </c:url>
              <a href="<c:out value="${next}"/>">Next</a>
            </c:if>

            <c:if test="${(sessionScope.resultsSize - param.start) gt (resultsPerPage * 2)}">
              <c:url value="displayResults.jsp" var="last">
                <c:param name="start" value="${sessionScope.resultsSize - (sessionScope.resultsSize mod resultsPerPage)}"/>
              </c:url>
              <a href="<c:out value="${last}"/>">Last</a>
            </c:if>
          </td>
        </tr>
      </table>
    <div/>
  </c:otherwise>
</c:choose>