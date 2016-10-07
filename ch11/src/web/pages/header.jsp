<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="/plant-taglib" prefix="tags" %>
<div class="center">
  <h1>Plant application</h1>
  <p>
    <c:forEach begin="65" end="90" var="status">
      <c:set var="letter">
        <tags:letters letter="${status}"/>
      </c:set>

      <a class="letters" href="/antBook/plants/listPlants.jsp?show=name&amp;letter=${letter}">
        ${letter}
      </a>
    </c:forEach>
  </p>
</div>