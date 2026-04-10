const form = document.querySelector("form");
const passwordInput = form.password;
const confirmPasswordInput = form.confirm_password;
const errorBox = document.createElement("div");
errorBox.classList.add("error");
form.appendChild(errorBox);

form.addEventListener("submit",function(event) {
    errorBox.innerHTML = "";
    let errors = [];

    const password = passwordInput.value.trim();
    const confirmPassword = confirmPasswordInput.value.trim();

    const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,16}$/;
    if (!passwordPattern.test(password)) {
        errors.push("-Password must be 8-16 characters long and include at least one letter and one number.");
    }

    if (password !== confirmPassword) {
        errors.push("-Passwords do not match.");
    }

    if (errors.length > 0) {
        event.preventDefault();
        errorBox.innerHTML = errors.join("<br>");
        errorBox.style.display = "block";
    } else {
        errorBox.style.display = "none";
    }

});


    