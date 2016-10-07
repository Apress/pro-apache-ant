<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<p class="left">
  <a href="/antBook/index.jsp" class="menu-main">Home</a><br/>
  <a href="/antBook/plants/index.jsp" class="menu-main">Plants</a><br/>

  <c:if test="${param.sectionName == 'Plants'}">  
    &emsp;<a href="/antBook/plants/listPlants.jsp?show=common" class="menu-sub">By common name</a><br/>
    &emsp;<a href="/antBook/plants/listPlants.jsp?show=name" class="menu-sub">By botanical name</a><br/>
    &emsp;<a href="/antBook/plants/listPlants.jsp?show=family" class="menu-sub">By family</a><br/>
  </c:if>
</p>