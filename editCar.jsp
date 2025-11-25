<%@ page import="com.carrental.Car" %>
<html><head><title>Edit Car</title></head><body>
<h2>Edit Car</h2>
<%
    Car car = (Car) request.getAttribute("car");
    if (car == null) {
%>
  <p>Car not found.</p>
  <a href="cars">Back</a>
<%
    } else {
%>
<form method="post" action="cars">
  <input type="hidden" name="action" value="edit"/>
  ID: <input name="id" value="<%=car.getId()%>" readonly/><br/>
  Make: <input name="make" value="<%=car.getMake()%>"/><br/>
  Model: <input name="model" value="<%=car.getModel()%>"/><br/>
  Year: <input name="year" value="<%=car.getYear()%>"/><br/>
  Daily Rate: <input name="dailyRate" value="<%=car.getDailyRate()%>"/><br/>
  Seats: <input name="seats" value="<%=car.getSeats()%>"/><br/>
  Available: <input type="checkbox" name="available" <%= car.isAvailable() ? "checked" : "" %> /><br/>
  <button type="submit">Save</button>
</form>
<a href="cars">Back to list</a>
<%
    }
%>
</body></html>
