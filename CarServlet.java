package com.carrental.web;

import com.carrental.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Simple servlet demonstrating CRUD via JSP pages (classic style)
public class CarServlet extends HttpServlet {
    private CarService service;

    @Override
    public void init() throws ServletException {
        super.init();
        CarDAO dao = new CarDAO();
        service = new CarService(dao);
        service.seedDemoData();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "list":
                    req.setAttribute("cars", service.getAvailableCars());
                    req.getRequestDispatcher("/views/listCars.jsp").forward(req, resp);
                    break;
                case "showAdd":
                    req.getRequestDispatcher("/views/addCar.jsp").forward(req, resp);
                    break;
                case "showEdit":
                    String idStr = req.getParameter("id");
                    if (idStr != null) {
                        int id = Integer.parseInt(idStr);
                        Optional<Car> car = service.dao.findById(id);
                        req.setAttribute("car", car.orElse(null));
                    }
                    req.getRequestDispatcher("/views/editCar.jsp").forward(req, resp);
                    break;
                case "rent":
                    req.getRequestDispatcher("/views/rentCar.jsp").forward(req, resp);
                    break;
                case "return":
                    req.getRequestDispatcher("/views/returnCar.jsp").forward(req, resp);
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/cars");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            switch (action) {
                case "add":
                    int id = Integer.parseInt(req.getParameter("id"));
                    String make = req.getParameter("make");
                    String model = req.getParameter("model");
                    int year = Integer.parseInt(req.getParameter("year"));
                    double rate = Double.parseDouble(req.getParameter("dailyRate"));
                    int seats = Integer.parseInt(req.getParameter("seats"));
                    Car car = new Car(id, make, model, year, rate, seats);
                    service.dao.add(car);
                    resp.sendRedirect(req.getContextPath() + "/cars?action=list");
                    break;
                case "edit":
                    int id2 = Integer.parseInt(req.getParameter("id"));
                    Optional<Car> existing = service.dao.findById(id2);
                    if (existing.isPresent()) {
                        Car c = existing.get();
                        c = new Car(id2, req.getParameter("make"), req.getParameter("model"),
                                Integer.parseInt(req.getParameter("year")),
                                Double.parseDouble(req.getParameter("dailyRate")),
                                Integer.parseInt(req.getParameter("seats")));
                        // preserve availability by checking param (checkbox)
                        c.setAvailable(req.getParameter("available") != null);
                        service.dao.update(c);
                    }
                    resp.sendRedirect(req.getContextPath() + "/cars?action=list");
                    break;
                case "doRent":
                    int rid = Integer.parseInt(req.getParameter("id"));
                    try {
                        service.rentCar(rid);
                        req.getSession().setAttribute("message", "Rented car " + rid);
                    } catch (Exception ex) {
                        req.getSession().setAttribute("message", "Could not rent: " + ex.getMessage());
                    }
                    resp.sendRedirect(req.getContextPath() + "/cars?action=list");
                    break;
                case "doReturn":
                    int rid2 = Integer.parseInt(req.getParameter("id"));
                    service.returnCar(rid2);
                    req.getSession().setAttribute("message", "Returned car " + rid2);
                    resp.sendRedirect(req.getContextPath() + "/cars?action=list");
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/cars");
            }
        } catch (SQLException se) {
            throw new ServletException(se);
        }
    }
}
