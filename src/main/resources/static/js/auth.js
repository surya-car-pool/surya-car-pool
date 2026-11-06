document.addEventListener("DOMContentLoaded", () => {
	const params = new URLSearchParams(window.location.search);
	const selectedTab = params.has("registered")
		? document.querySelector("#existing-tab")
		: params.has("registerError")
			? document.querySelector("#new-tab")
			: null;
	if (selectedTab) {
		bootstrap.Tab.getOrCreateInstance(selectedTab).show();
	}

	document.querySelectorAll("[data-password-toggle]").forEach((button) => {
		button.addEventListener("click", () => {
			const input = document.getElementById(button.dataset.passwordToggle);
			const visible = input.type === "password";
			input.type = visible ? "text" : "password";
			button.setAttribute("aria-pressed", String(visible));
			button.setAttribute("aria-label", visible ? "Hide password" : "Show password");
			button.querySelector("i").className = visible ? "bi bi-eye-slash" : "bi bi-eye";
		});
	});

	const form = document.getElementById("registration-form");
	const password = document.getElementById("regPassword");
	const confirmation = document.getElementById("regConfirmPassword");
	if (form && password && confirmation) {
		const validateConfirmation = () => {
			confirmation.setCustomValidity(
				confirmation.value && confirmation.value !== password.value
					? "Passwords do not match."
					: ""
			);
		};
		password.addEventListener("input", validateConfirmation);
		confirmation.addEventListener("input", validateConfirmation);
		form.addEventListener("submit", validateConfirmation);
	}

	const birthDate = document.getElementById("regDateOfBirth");
	if (birthDate) {
		const today = new Date();
		birthDate.max = [
			today.getFullYear(),
			String(today.getMonth() + 1).padStart(2, "0"),
			String(today.getDate()).padStart(2, "0")
		].join("-");
	}
});
