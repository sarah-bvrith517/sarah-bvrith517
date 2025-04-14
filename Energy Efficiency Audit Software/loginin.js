document.getElementById('login-form').addEventListener('submit', function(e) {
    e.preventDefault();

    // Get user input values
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    // Retrieve user data from localStorage
    const user = JSON.parse(localStorage.getItem(email));  // Fix: JSON.parse instead of JSON.parsea

    // Check if user exists and the password matches
    if (user && user.password === password) {
        alert('Login successful!');
        localStorage.setItem('loggedInUser', JSON.stringify(user));  // Store logged-in user
        window.location.href = 'dashboard.html';  // Redirect to dashboard
    } else {
        alert('Invalid email or password.');
    }
});
