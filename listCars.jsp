<%@ page import="java.util.*, com.carrental.Car" %>
<html><head><title>Available Cars</title></head><body>
<h2>Available Cars</h2>
<a href="cars?action=showAdd">Add New Car</a> |
<a href="cars?action=rent">Rent a Car</a> |
<a href="cars?action=return">Return a Car</a>
<hr/>
<c:if test="${not empty sessionScope.message}">
  <p style="color:green">${sessionScope.message}</p>
  <%
     session.removeAttribute("message");
  %>
</c:if>
<ul>
<% List<Car> cars = (List<Car>) request.getAttribute("cars");
   if (cars != null) {
     for (Car c : cars) { %>
       <li><%= c.toString() %> - <a href="cars?action=showEdit&id=<%=c.getId()%>">Edit</a></li>
<%   }
   } %>
</ul>
</body></html>
