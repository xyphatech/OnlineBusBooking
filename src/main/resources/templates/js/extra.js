const urlParams = new URLSearchParams(window.location.search);
const token = urlParams.get("token");

const forgotDiv = document.getElementById("forgotSection");
const resetDiv = document.getElementById("resetSection");

//If token exists -> pop up reset section
if (token){
    forgotDiv.classList.add("hidden")
    resetDiv.classList.remove("hidden")
}
//forgot password
function sendForgot() {
    const gmail = document.getElementById("gmail").value;

    if (!gmail.trim()){
        return Swal.fire({
            icon: "warning",
            title:"Email Required",
            text:"Please enter your email address..."
        })
    }

    fetch("http://localhost:8080/api/auth/forgot-password", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email: gmail })
    })
        .then(async (res)=> {
            const data = await res.text();

            if (res.status === 404) {
                Swal.fire({
                    icon: "error",
                    title: "Email Not Found",
                    text: "This email does not exist in our system.",
                });
                return;
            }


        if (res.status === 400){
            Swal.fire({
                icon: "error",
                title: "Email Not Found",
                text: "This email does not exist in our system.",
            });
        }else if (res.ok) {
            Swal.fire({
                icon: "success",
                title: "Reset Link Sent",
                text: "Check your email inbox for the password reset link.",
            });

        }
        })
        .catch(() => {
            Swal.fire({
                icon: "error",
                title: "Server Error",
                text: "Could not connect to server."
            });
        });
}

// For reset password
function sendReset() {
    const newPassword = document.getElementById("newPassword").value;

    fetch("http://localhost:8080/api/auth/reset-password", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ token: token, newPassword: newPassword })
    })
        .then(res => res.text())
        .then(text => {
            document.getElementById("resetMessage").innerHTML = text;
        })
        .catch(() => {
            document.getElementById("resetMessage").innerHTML =
                "<span class='error'>Server error</span>";
        });
}