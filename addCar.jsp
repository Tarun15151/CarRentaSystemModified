<html><head><title>Add Car</title></head><body>
<h2>Add Car</h2>
<form method="post" action="cars">
  <input type="hidden" name="action" value="add"/>
  ID: <input name="id"/><br/>
  Make: <input name="make"/><br/>
  Model: <input name="model"/><br/>
  Year: <input name="year"/><br/>
  Daily Rate: <input name="dailyRate"/><br/>
  Seats: <input name="seats"/><br/>
  <button type="submit">Add</button>
</form>
<a href="cars">Back to list</a>
</body></html>
