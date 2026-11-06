(() => {
	"use strict";

	const counters = document.querySelectorAll("[data-counter]");
	const reducedMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;

	function animateCounter(element) {
		const target = Number(element.dataset.count);
		if (!Number.isSafeInteger(target) || target < 0) {
			return;
		}

		if (reducedMotion || target === 0) {
			element.textContent = target.toLocaleString();
			return;
		}

		const duration = 900;
		const start = performance.now();

		function render(now) {
			const progress = Math.min((now - start) / duration, 1);
			const eased = 1 - Math.pow(1 - progress, 3);
			element.textContent = Math.floor(target * eased).toLocaleString();
			if (progress < 1) {
				window.requestAnimationFrame(render);
			}
		}

		window.requestAnimationFrame(render);
	}

	if ("IntersectionObserver" in window) {
		const observer = new IntersectionObserver((entries) => {
			entries.forEach((entry) => {
				if (entry.isIntersecting) {
					animateCounter(entry.target);
					observer.unobserve(entry.target);
				}
			});
		}, { threshold: 0.35 });

		counters.forEach((counter) => observer.observe(counter));
	} else {
		counters.forEach(animateCounter);
	}
})();
