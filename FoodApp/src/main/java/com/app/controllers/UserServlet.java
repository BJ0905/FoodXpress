private void handleRegister(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    String name = req.getParameter("name");
    String username = req.getParameter("username");
    String email = req.getParameter("email");
    String phone = req.getParameter("phone");
    String address = req.getParameter("address");
    String plainPassword = req.getParameter("password");

    if (username != null) username = username.trim();
    if (email != null) email = email.trim();

    String role = req.getParameter("role");
    if (role == null || role.isEmpty()) role = "customer";

    if (username == null || username.isEmpty()
            || plainPassword == null || plainPassword.isEmpty()) {
        req.setAttribute("error", "Username and password are required.");
        forward(req, resp, "/jsp/register.jsp");
        return;
    }

    try {
        if (userDao.getUserByUsername(username) != null) {
            req.setAttribute("error", "Username already exists.");
            forward(req, resp, "/jsp/register.jsp");
            return;
        }

        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));

        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setRole(role);

        userDao.addUser(user);

        req.setAttribute("message", "Registration successful. Please login.");
        forward(req, resp, "/jsp/login.jsp");

    } catch (Exception e) {
        e.printStackTrace();
        req.setAttribute("error", "Server error. Please try again later.");
        forward(req, resp, "/jsp/register.jsp");
    }
}