document.getElementById('signup-form').addEventListener('submit', function(e) {
    e.preventDefault();

    // Get user input values
    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    // Create user object
    const user = {
        username: username,
        email: email,
        password: password,
        issues: []  // This will store issues reported by the user
    };

    // Store user object in localStorage
    localStorage.setItem(email, JSON.stringify(user));  // Key is email, value is user object

    alert('Account created successfully! Please log in.');
    window.location.href = 'login.html';  // Redirect to login page
});
